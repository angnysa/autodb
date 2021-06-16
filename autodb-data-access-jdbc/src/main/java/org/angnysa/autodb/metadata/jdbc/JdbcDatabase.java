package org.angnysa.autodb.metadata.jdbc;

import lombok.*;
import org.angnysa.autodb.metadata.api.Database;
import org.angnysa.autodb.metadata.api.Table;
import org.angnysa.autodb.metadata.api.relations.Relation;
import org.angnysa.autodb.metadata.api.utils.Dumper;
import org.angnysa.autodb.metadata.jdbc.relations.JdbcRelation;
import org.angnysa.autodb.metadata.jdbc.utils.ListIndexerUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class JdbcDatabase implements Database<JdbcTable> {
	String name;
	String version;
	@Getter(AccessLevel.PRIVATE)
	Map<String, JdbcTable> tablesByName;
	@Getter(AccessLevel.PRIVATE)
	Map<JdbcTable, Map<String, ForeignKey>> foreignKeys;
	@Getter(AccessLevel.PRIVATE)
	Map<JdbcTable, Map<String, JdbcRelation>> relations;

	public JdbcDatabase(String name, String version, List<JdbcTable> tables, Map<JdbcTable, List<ForeignKey>> foreignKeys, Map<JdbcTable, List<JdbcRelation>> relations) {
		this.name = name;
		this.version = version;
		this.tablesByName = ListIndexerUtil.index(tables, Table::getFullyQualifiedName);
		tables.forEach(t -> t.setDatabase(this));
		this.foreignKeys = foreignKeys.entrySet().stream()
				.collect(Collectors.toUnmodifiableMap(
						Map.Entry::getKey,
						e -> ListIndexerUtil.index(e.getValue(), ForeignKey::getName)));
		this.relations = relations.entrySet().stream()
				.collect(Collectors.toUnmodifiableMap(
						Map.Entry::getKey,
						e -> ListIndexerUtil.index(e.getValue(), Relation::getName)));
	}

	@Override
	public List<String> getTablesName() {
		return tablesByName.values().stream().map(Table::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<JdbcTable> getTables() {
		return tablesByName.values().stream().collect(Collectors.toUnmodifiableList());
	}

	@Override
	public JdbcTable getTable(@NonNull String tableName) {
		tableName = tableName.toLowerCase();
		if (tablesByName.containsKey(tableName)) {
			return tablesByName.get(tableName);
		} else {
			throw new IllegalArgumentException(String.format("No table '%s'", tableName));
		}
	}

	@Override
	public List<String> getRelationsName(@NonNull JdbcTable table) {
		return relations.get(table).values().stream().map(Relation::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<JdbcRelation> getRelations(JdbcTable table) {
		if (relations.containsKey(table)) {
			return relations.get(table).values().stream().collect(Collectors.toUnmodifiableList());
		} else {
			throw new IllegalArgumentException(String.format("No table '%s'", table.getFullyQualifiedName()));
		}
	}

	@Override
	public JdbcRelation getRelation(@NonNull JdbcTable table, @NonNull String relationName) {
		if (relations.containsKey(table)) {
			relationName = relationName.toLowerCase();
			if (relations.get(table).containsKey(relationName)) {
				return relations.get(table).get(relationName.toLowerCase());
			} else {
				throw new IllegalArgumentException(
						String.format("No relation '%s' in table '%s'", relationName, table.getFullyQualifiedName()));
			}
		} else {
			throw new IllegalArgumentException(String.format("No table '%s'", table.getFullyQualifiedName()));
		}
	}

	public List<String> getForeignKeyNames(@NonNull JdbcTable table) {
		return foreignKeys.get(table).values().stream().map(ForeignKey::getName).collect(Collectors.toUnmodifiableList());
	}

	public List<ForeignKey> getForeignKeys(JdbcTable table) {
		if (foreignKeys.containsKey(table)) {
			return foreignKeys.get(table).values().stream().collect(Collectors.toUnmodifiableList());
		} else {
			throw new IllegalArgumentException(String.format("No table '%s'", table.getFullyQualifiedName()));
		}
	}

	public ForeignKey getForeignKey(@NonNull JdbcTable table, @NonNull String relationName) {
		if (foreignKeys.containsKey(table)) {
			relationName = relationName.toLowerCase();
			if (foreignKeys.get(table).containsKey(relationName)) {
				return foreignKeys.get(table).get(relationName.toLowerCase());
			} else {
				throw new IllegalArgumentException(
						String.format("No relation '%s' in table '%s'", relationName, table.getFullyQualifiedName()));
			}
		} else {
			throw new IllegalArgumentException(String.format("No table '%s'", table.getFullyQualifiedName()));
		}
	}

	@SneakyThrows
	@Override
	public String toString() {
		return Dumper.dumpDatabase(this);
	}
}
