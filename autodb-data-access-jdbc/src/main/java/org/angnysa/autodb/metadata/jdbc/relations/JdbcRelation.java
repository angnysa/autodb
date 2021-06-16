package org.angnysa.autodb.metadata.jdbc.relations;

import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.angnysa.autodb.metadata.api.relations.Relation;
import org.angnysa.autodb.metadata.api.utils.Dumper;
import org.angnysa.autodb.metadata.jdbc.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public abstract class JdbcRelation implements Relation {
    private final String name;
    private final String description;

    @Override
    public abstract JdbcIndex getReferencing();

    @Override
    public abstract JdbcIndex getReferenced();

    public static JdbcRelation[] of(Map<JdbcTable, List<ForeignKey>> foreignKeys, @NonNull ForeignKey foreignKey) {
        JdbcRelation[] relations;
        if (foreignKey.getReferencing() instanceof JdbcUniqueIndex) {

            relations = new JdbcRelation[] {
                    new JdbcOneToOne(foreignKey, false),
                    new JdbcOneToOne(foreignKey, true)};

        } else {

            JdbcTable relTable = foreignKey.getReferencing().getTable();
            if (isRelationTable(foreignKeys, relTable)) {
                relations = new JdbcRelation[] {
                        new JdbcManyToMany(foreignKey, getOtherForeignKey(foreignKeys, relTable, foreignKey)),
                        new JdbcManyToMany(getOtherForeignKey(foreignKeys, relTable, foreignKey), foreignKey)};
            } else {
                relations = new JdbcRelation[] {
                        new JdbcManyToOne(foreignKey),
                        new JdbcOneToMany(foreignKey)};
            }

        }

        return relations;
    }

    /**
     * returns the 'other' foreign key for a relation table. See {@link #isRelationTable(Map, JdbcTable)} for definition.
     *
     * @param foreignKeys All the existing foreign keys
     * @param table The table to target
     * @param foreignKey The foreign key not to return
     * @return
     */
    private static ForeignKey getOtherForeignKey(Map<JdbcTable, List<ForeignKey>> foreignKeys, JdbcTable table, ForeignKey foreignKey) {
        for (ForeignKey fk : foreignKeys.get(table)) {
            if (! fk.equals(foreignKey)) {
                return fk;
            }
        }

        throw new IllegalStateException();
    }

    /**
     * checks if the given table is a relation table.
     *
     * A table is a relation table iff:
     * <ul>
     *     <li>it has two and only two foreign keys</li>
     *     <li>both foreign keys cover all columns of the table</li>
     * </ul>
     *
     * @param foreignKeys
     * @param table
     * @return
     */
    public static boolean isRelationTable(Map<JdbcTable, List<ForeignKey>> foreignKeys, @NonNull JdbcTable table) {
        List<ForeignKey> tableForeignKeys = foreignKeys.getOrDefault(table, List.of());
        if (tableForeignKeys.size() == 2) {

            Set<JdbcColumn> columns = new HashSet<>();
            for (ForeignKey foreignKey : tableForeignKeys) {
                columns.addAll(foreignKey.getReferencing().getColumns());
            }

            return columns.equals(new HashSet<>(table.getColumns()));
        }

        return false;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return Dumper.dumpRelation(this);
    }
}
