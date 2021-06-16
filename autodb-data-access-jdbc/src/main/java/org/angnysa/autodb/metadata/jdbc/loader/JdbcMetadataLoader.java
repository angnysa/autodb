package org.angnysa.autodb.metadata.jdbc.loader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.angnysa.autodb.metadata.jdbc.*;
import org.angnysa.autodb.metadata.jdbc.relations.JdbcRelation;
import org.angnysa.autodb.metadata.jdbc.utils.JdbcConnectionUtil;
import org.angnysa.autodb.metadata.jdbc.utils.NameUtil;

import javax.sql.CommonDataSource;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class JdbcMetadataLoader {

    @FunctionalInterface
    interface SqlBiFunction<T, U, R> {
        R apply(T t, U u) throws SQLException;
    }

    @FunctionalInterface
    interface SqlFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    private final CommonDataSource commonDataSource;

    public JdbcDatabase loadDatabase(String catalogPattern, String schemaPattern, String[] tableTypes) throws SQLException {

        try(Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {

            List<JdbcTable> tables = loadTables(connection, catalogPattern, schemaPattern, tableTypes);

            Map<JdbcTable, List<ForeignKey>> importedForeignKeys = new HashMap<>();

            for (JdbcTable table : tables) {
                importedForeignKeys.put(table, loadImportedForeignKeys(connection, table, tables));
            }

            Map<JdbcTable, List<JdbcRelation>> relations = loadRelations(importedForeignKeys);

            DatabaseMetaData dmd = connection.getMetaData();
            JdbcDatabase db = new JdbcDatabase(dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion(), tables, importedForeignKeys, relations);

            log.debug("Database loaded:\n{}", db);

            return db;
        }
    }

    public List<JdbcTable> loadTables(@NonNull Connection connection, String catalogPattern, String schemaPattern, String[] types) throws SQLException {

        try (ResultSet rs = connection.getMetaData().getTables(null, null, "%", types)) {
            List<JdbcTable> tables = new ArrayList<>();

            while(rs.next()) {
                String catalog   = rs.getString("TABLE_CAT");   // table catalog (may be null)
                String schema    = rs.getString("TABLE_SCHEM"); // table schema (may be null)
                String tableName = rs.getString("TABLE_NAME");  // table name

                if (catalogPattern == null || catalog == null || catalog.matches(catalogPattern)) {
                    if (schemaPattern == null || schema == null || schema.matches(schemaPattern)) {
                        String tableType = safeGetObject(rs, "TABLE_TYPE", ResultSet::getString, "");
                        String remarks   = safeGetObject(rs, "REMARKS"   , ResultSet::getString, "");

                        List<JdbcColumn> columns    = loadColumns(connection, catalog, schema, tableName);
                        List<JdbcIndex>  indexes    = loadIndices(connection, catalog, schema, tableName, columns);
                        JdbcUniqueIndex  primaryKey = loadPrimaryKey(connection, catalog, schema, tableName, columns);

                        JdbcTable table = new JdbcTable(catalog, schema, tableName, remarks, tableType, columns, primaryKey, indexes);
                        log.trace("Table loaded:\n{}", table);

                        tables.add(table);
                    }
                }
            }

            return tables;
        }
    }

    public List<JdbcColumn> loadColumns(@NonNull Connection connection, String catalog, String schema, @NonNull String table) throws SQLException {

        try (ResultSet resultSet = connection.getMetaData().getColumns(catalog, schema, table, null)) {
            List<JdbcColumn> columns = new ArrayList<>();

            while (resultSet.next()) {

                JdbcColumn column = new JdbcColumn(
                        resultSet.getString("COLUMN_NAME"),
                        resultSet.getString("REMARKS"),
                        resultSet.getString("TYPE_NAME"),
                        JDBCType.valueOf(resultSet.getInt("DATA_TYPE")),
                        "YES".equals(resultSet.getString("IS_NULLABLE")),
                        resultSet.getInt("COLUMN_SIZE"),
                        resultSet.getInt("DECIMAL_DIGITS"),
                        resultSet.getInt("NUM_PREC_RADIX"),
                        "YES".equals(resultSet.getString("IS_AUTOINCREMENT")) || "YES".equals(resultSet.getString("IS_GENERATEDCOLUMN")));

                columns.add(column);

                log.trace("Column loaded: {} :: {}", NameUtil.generateFullyQualifiedName(catalog, schema, table), column);
            }

            if (columns.size() == 0) {
                throw new IllegalArgumentException(
                        String.format("No column found for catalog '%s', schema '%s', table '%s'",
                                catalog, schema, table));
            }

            return columns;
        }
    }

    @Value
    private static class IndexInfo {
        String name;
        boolean unique;
    }

    public List<JdbcIndex> loadIndices(@NonNull Connection connection, String catalog, String schema, @NonNull String table, @NonNull List<JdbcColumn> tableColumns) throws SQLException {
        if (tableColumns.size() == 0) {
            throw new IllegalArgumentException("tableColumns canot be empty");
        }

        try (ResultSet resultSet = connection.getMetaData().getIndexInfo(catalog, schema, table, false, true)) {
            return aggregateResultSet(
                    resultSet,
                    rs -> new IndexInfo(rs.getString("INDEX_NAME"), !rs.getBoolean("NON_UNIQUE")),
                    rs -> rs.getShort("ORDINAL_POSITION"),
                    rs -> search(tableColumns, c -> {
                        try {
                            return c.getName().equals(rs.getString("COLUMN_NAME"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    (indexInfo, indexColumns) -> {
                        JdbcIndex index = indexInfo.isUnique()
                                ? new JdbcUniqueIndex(indexInfo.getName(), indexColumns)
                                : new JdbcIndex(indexInfo.getName(), indexColumns);

                        log.trace("Index loaded: {} :: {}", NameUtil.generateFullyQualifiedName(catalog, schema, table), index);
                        return index;
                    })
                    .stream()
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    public JdbcUniqueIndex loadPrimaryKey(@NonNull Connection connection, String catalog, String schema, @NonNull String table, @NonNull List<JdbcColumn> tableColumns) throws SQLException {
        if (tableColumns.size() == 0) {
            throw new IllegalArgumentException("tableColumns canot be empty");
        }

        try (ResultSet resultSet = connection.getMetaData().getPrimaryKeys(catalog, schema, table)) {
            List<JdbcUniqueIndex> primaryKeys = aggregateResultSet(
                    resultSet,
                    rs -> rs.getString("PK_NAME"),
                    rs -> rs.getShort("KEY_SEQ"),
                    rs -> search(tableColumns, c -> {
                        try {
                            return c.getName().equals(rs.getString("COLUMN_NAME"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    (name, columns) -> {
                        JdbcUniqueIndex pk = new JdbcUniqueIndex(name, columns);
                        log.trace("Primary Key loaded: {} :: {}", NameUtil.generateFullyQualifiedName(catalog, schema, table), pk);
                        return pk;
                    });

            if ( primaryKeys.size() == 0) {
                throw new IllegalStateException(String.format("Table '%s' has no primary key", NameUtil.generateFullyQualifiedName(catalog, schema, table)));
            } else if (primaryKeys.size() == 1) {
                return primaryKeys.get(0);
            } else {
                throw new IllegalStateException(String.format("Table '%s' has multiple primary keys", NameUtil.generateFullyQualifiedName(catalog, schema, table)));
            }
        }
    }

    @Value
    private static class FKInfo {
        @Value
        private static class Table {
            String catalog, schema;
            @NonNull String table;
        }

        @NonNull String fkName;
        @NonNull Table pkTable, fkTable;
        @NonNull ForeignKey.Action onUpdate, onDelete;
        @NonNull ForeignKey.ValidationDeferrability deferrability;
    }

    @Value
    private static class FKColInfo {
        @NonNull String pkColumn, fkColumn;
    }

    public List<ForeignKey> loadImportedForeignKeys(@NonNull Connection connection, @NonNull JdbcTable table, List<JdbcTable> tables) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getImportedKeys(table.getCatalog(), table.getSchema(), table.getName())) {
            return aggregateForeignKey(
                    tables.stream().collect(Collectors.toUnmodifiableMap(JdbcTable::getFullyQualifiedName, Function.identity())),
                    resultSet);
        }
    }

    private List<ForeignKey> aggregateForeignKey(Map<String, JdbcTable> tables, ResultSet resultSet) throws SQLException {
        return aggregateResultSet(
                resultSet,
                rs -> new FKInfo(
                        rs.getString("FK_NAME"),
                        new FKInfo.Table(
                                rs.getString("PKTABLE_CAT"),
                                rs.getString("PKTABLE_SCHEM"),
                                rs.getString("PKTABLE_NAME")),
                        new FKInfo.Table(
                                rs.getString("FKTABLE_CAT"),
                                rs.getString("FKTABLE_SCHEM"),
                                rs.getString("FKTABLE_NAME")),
                        mapImportedForeignKeyRule(rs.getShort("UPDATE_RULE")),
                        mapImportedForeignKeyRule(rs.getShort("DELETE_RULE")),
                        mapImportedDeferrability(rs.getShort("DEFERRABILITY"))),
                rs -> rs.getShort("KEY_SEQ"),
                rs -> new FKColInfo(rs.getString("PKCOLUMN_NAME"), rs.getString("FKCOLUMN_NAME")),
                (fkInfo, columns) -> buildForeignKey(fkInfo, columns, tables))
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    private ForeignKey buildForeignKey(@NonNull FKInfo fkInfo, @NonNull List<FKColInfo> columns, @NonNull Map<String, JdbcTable> tables) {

        ForeignKey fk = new ForeignKey(
                fkInfo.getFkName(),
                findForeignKeyFKIndex(fkInfo.getFkName(), fkInfo.getFkTable(), columns, tables, false),
                (JdbcUniqueIndex) findForeignKeyFKIndex(fkInfo.getFkName(), fkInfo.getPkTable(), columns, tables, true),
                fkInfo.getOnUpdate(),
                fkInfo.getOnDelete(),
                fkInfo.getDeferrability());

        log.trace("Foreign Key loaded: {}", fk);
        return fk;
    }

    private JdbcIndex findForeignKeyFKIndex(@NonNull String fkName, @NonNull FKInfo.Table fk, @NonNull List<FKColInfo> columns, @NonNull Map<String, JdbcTable> tables, boolean isReferenced) {
        String tableName = NameUtil.generateFullyQualifiedName(fk.getCatalog(), fk.getSchema(), fk.getTable());

        JdbcTable table = tables.get(tableName);

        if (table == null) {
            throw new IllegalStateException(String.format("No table named '%s'", tableName));
        }

        List<JdbcIndex> indexes = table.findIndexCovering(isReferenced, JdbcIndex.Coverage.MustCoverExact,
                columns.stream()
                        .map(isReferenced ? FKColInfo::getPkColumn : FKColInfo::getFkColumn)
                        .collect(Collectors.toUnmodifiableList()));

        if (indexes.size() > 0) {
            if (indexes.size() > 1) {
                StringWriter sw = new StringWriter();
                for (JdbcIndex index : indexes) {
                    sw.append('\n').append(index.toString());
                }

                log.warn("Found duplicate indexes for foreign key '{}': {}", fkName, sw.toString());
            }

            JdbcIndex result = null;
            for (JdbcIndex index : indexes) {
                if (result == null) {
                    result = index;
                } else if (!(result instanceof JdbcUniqueIndex) && index instanceof JdbcUniqueIndex) {
                    result = index;
                }
            }
            return result;

        } else {
            JdbcIndex result;
            if (isReferenced) {
                log.error("The foreign key '{}' on table '{}' does not reference a unique key. Unicity cannot be guaranteed!.",
                        fkName, tableName);
                result = new JdbcUniqueIndex(
                        null,
                        columns.stream()
                                .map(c -> table.getColumn(c.getPkColumn()))
                                .collect(Collectors.toUnmodifiableList()));
            } else {
                result = new JdbcIndex(
                        null,
                        columns.stream()
                                .map(c -> table.getColumn(c.getFkColumn()))
                                .collect(Collectors.toUnmodifiableList()));
            }

            result.setTable(table);
            return result;
        }
    }

    private ForeignKey.Action mapImportedForeignKeyRule(short rule) {
        switch (rule) {
            case DatabaseMetaData.importedKeyNoAction   : return ForeignKey.Action.NoAction;
            case DatabaseMetaData.importedKeyCascade    : return ForeignKey.Action.Cascade;
            case DatabaseMetaData.importedKeySetNull    : return ForeignKey.Action.SetNull;
            case DatabaseMetaData.importedKeySetDefault : return ForeignKey.Action.SetDefault;
            case DatabaseMetaData.importedKeyRestrict   : return ForeignKey.Action.Restrict;
            default: throw new IllegalArgumentException(String.format("Unrecognized rule: %d", rule));
        }
    }

    private ForeignKey.ValidationDeferrability mapImportedDeferrability(short deferrability) {
        switch (deferrability) {
            case DatabaseMetaData.importedKeyInitiallyDeferred  : return ForeignKey.ValidationDeferrability.InitiallyDeferred;
            case DatabaseMetaData.importedKeyInitiallyImmediate : return ForeignKey.ValidationDeferrability.Deferrable;
            case DatabaseMetaData.importedKeyNotDeferrable      : return ForeignKey.ValidationDeferrability.NotDeferrable;
            default: throw new IllegalArgumentException(String.format("Unrecognized deferrability: %d", deferrability));
        }
    }

    public Map<JdbcTable, List<JdbcRelation>> loadRelations(Map<JdbcTable, List<ForeignKey>> foreignKeys) {

        Map<JdbcTable, List<JdbcRelation>> relations = new HashMap<>();
        for (Map.Entry<JdbcTable, List<ForeignKey>> entry : foreignKeys.entrySet()) {
            relations.computeIfAbsent(entry.getKey(), x -> new ArrayList<>());
            for (ForeignKey foreignKey : entry.getValue()) {
                JdbcRelation[] newRelations = JdbcRelation.of(foreignKeys, foreignKey);


                for (JdbcRelation newRelation : newRelations) {
                    List<JdbcRelation> tableRelations = relations.computeIfAbsent(newRelation.getReferencing().getTable(), x -> new ArrayList<>());
                    if (! tableRelations.contains(newRelation)) {
                        log.trace("Relation loaded: {}", newRelation);
                        tableRelations.add(newRelation);
                    }
                }
            }
        }

        return relations;
    }

    private <G, O extends Comparable<O>, V, R> List<R> aggregateResultSet(
            ResultSet resultSet,
            SqlFunction<ResultSet, G> groupSupplier,
            SqlFunction<ResultSet, O> orderSupplier,
            SqlFunction<ResultSet, V> valueSupplier,
            BiFunction<G, List<V>, R> aggregator) throws SQLException {

        Map<G, Map<O, V>> groups = new HashMap<>();

        while (resultSet.next()) {
            G group = groupSupplier.apply(resultSet);
            if (! groups.containsKey(group)) {
                groups.put(group, new HashMap<>());
            }

            O order = orderSupplier.apply(resultSet);
            if (! groups.get(group).containsKey(order)) {
                groups.get(group).put(order, valueSupplier.apply(resultSet));
            } else {
                throw new IllegalStateException(String.format("Duplicate order values are not allowed: %s", order));
            }
        }

        return groups
                .entrySet()
                .stream()
                .map(e -> aggregator.apply(
                        e.getKey(),
                        e.getValue()
                                .entrySet()
                                .stream()
                                .sorted(Map.Entry.comparingByKey())
                                .map(Map.Entry::getValue)
                                .collect(Collectors.toUnmodifiableList())))
                .collect(Collectors.toUnmodifiableList());
    }

    private <T> T search(@NonNull List<T> list, @NonNull Predicate<T> predicate) {
        for (T t : list) {
            if (predicate.test(t)) {
                return t;
            }
        }

        throw new IllegalStateException("No result");
    }

    private <T> T safeGetObject(@NonNull ResultSet rs, @NonNull String column, @NonNull SqlBiFunction<ResultSet, String, T> getter, @NonNull T ifNull) throws SQLException {
        for (int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
            if (rs.getMetaData().getColumnName(i).equals(column)) {
                return getter.apply(rs, column);
            }
        }

        return ifNull;
    }
}
