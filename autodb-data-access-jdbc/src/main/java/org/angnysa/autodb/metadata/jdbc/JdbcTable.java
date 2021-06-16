package org.angnysa.autodb.metadata.jdbc;

import lombok.*;
import org.angnysa.autodb.metadata.api.Column;
import org.angnysa.autodb.metadata.api.Index;
import org.angnysa.autodb.metadata.api.Table;
import org.angnysa.autodb.metadata.api.UniqueIndex;
import org.angnysa.autodb.metadata.api.relations.Relation;
import org.angnysa.autodb.metadata.api.utils.Dumper;
import org.angnysa.autodb.metadata.jdbc.utils.ListIndexerUtil;
import org.angnysa.autodb.metadata.jdbc.utils.NameUtil;

import java.util.*;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(exclude = "database")
public class JdbcTable implements Table {
	private JdbcDatabase database;
	private final String catalog;
	private final String schema;
	@NonNull private final String name;
	private final String description;
	@NonNull private final String tableType;
	@NonNull private final List<JdbcColumn> columns;
	@NonNull private final JdbcUniqueIndex primaryKey;

	@NonNull private final String fullyQualifiedName;
	@Getter(AccessLevel.PRIVATE)
	@NonNull private final Map<String, JdbcColumn> columnsByName;
	@Getter(AccessLevel.PRIVATE)
	@NonNull private final Map<String, JdbcIndex> indicesByName;
	@Getter(AccessLevel.PRIVATE)
	@NonNull private final Map<String, JdbcUniqueIndex> uniqueIndicesByName;

	public JdbcTable(String catalog, String schema, @NonNull String name, String description, @NonNull String tableType, @NonNull List<JdbcColumn> columns, @NonNull JdbcUniqueIndex primaryKey, @NonNull List<JdbcIndex> indices) {
		this.catalog = catalog;
		this.schema = schema;
		this.name = name;
		this.description = description;
		this.tableType = tableType;
		this.columns = List.copyOf(columns);
		this.primaryKey = primaryKey;


		columns.forEach(c -> c.setTable(this));
		primaryKey.setTable(this);
		indices.forEach(i -> i.setTable(this));

		this.indicesByName = ListIndexerUtil.index(indices, Index::getName);
		this.uniqueIndicesByName = ListIndexerUtil.index(
				indices.stream()
						.filter(JdbcUniqueIndex.class::isInstance)
						.map(JdbcUniqueIndex.class::cast),
				UniqueIndex::getName);

		fullyQualifiedName = NameUtil.generateFullyQualifiedName(catalog, schema, name);

		columnsByName = ListIndexerUtil.index(columns, JdbcColumn::getName);
	}

	void setDatabase(@NonNull JdbcDatabase database) {
		if (this.database == null) {
			this.database = database;
		} else {
			throw new IllegalStateException(String.format("Table '%s' is already linked to a database.", getFullyQualifiedName()));
		}
	}

	public List<JdbcIndex> findIndexCovering(boolean unique, Index.Coverage coverage, List<String> columns) {
		return (unique ? uniqueIndicesByName : indicesByName)
				.values()
				.stream()
				.filter(i -> i.covers(coverage, columns))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<ForeignKey> getForeignKeys() {
		return database.getForeignKeys(this);
	}

	@Override
	public List<String> getColumnsNames() {
		return columns.stream().map(Column::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public JdbcColumn getColumn(int index) {
		return columns.get(index);
	}

	@Override
	public JdbcColumn getColumn(String name) {
		return columnsByName.get(name.toLowerCase());
	}

	@Override
	public List<String> getIndicesName() {
		return indicesByName.values().stream().map(Index::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<JdbcIndex> getIndices() {
		return indicesByName.values().stream().collect(Collectors.toUnmodifiableList());
	}

	@Override
	public Index getIndex(@NonNull String name) {
		if (indicesByName.containsKey(name)) {
			return indicesByName.get(name.toLowerCase());
		} else {
			throw new IllegalArgumentException(
					String.format("No index named '%s' in table '%s'", name, getFullyQualifiedName()));
		}
	}

	@Override
	public List<String> getUniqueIndicesName() {
		return uniqueIndicesByName.values().stream().map(Index::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public UniqueIndex getUniqueIndex(@NonNull String name) {
		if (uniqueIndicesByName.containsKey(name)) {
			return uniqueIndicesByName.get(name.toLowerCase());
		} else {
			throw new IllegalArgumentException(
					String.format("No unique index named '%s' in table '%s'", name, getFullyQualifiedName()));
		}
	}

	@Override
	public List<String> getRelationsName() {
		return database == null ? List.of() : database.getRelationsName(this);
	}

	@Override
	public List<? extends Relation> getRelations() {
		return database == null ? List.of() : database.getRelations(this);
	}

	@Override
	public Relation getRelation(String relationName) {
		return database == null ? null : database.getRelation(this, relationName);
	}

	@SneakyThrows
	@Override
	public String toString() {
		return Dumper.dumpTable(this);
	}
}
