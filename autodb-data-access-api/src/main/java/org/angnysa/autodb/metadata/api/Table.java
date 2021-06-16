package org.angnysa.autodb.metadata.api;

import org.angnysa.autodb.metadata.api.relations.Relation;

import java.util.List;
import java.util.Map;

public interface Table {
    Database getDatabase();
    String getFullyQualifiedName();
    String getName();
    String getDescription();
    String getTableType();
    List<? extends Column> getColumns();
    List<String> getColumnsNames();
    Column getColumn(int index);
    Column getColumn(String name);
    UniqueIndex getPrimaryKey();
    List<String> getIndicesName();
    List<? extends Index> getIndices();
    Index getIndex(String name);
    List<String> getUniqueIndicesName();
    UniqueIndex getUniqueIndex(String name);
    List<? extends Index> findIndexCovering(boolean unique, Index.Coverage coverage, List<String> columns);
    List<String> getRelationsName();
    List<? extends Relation> getRelations();
    Relation getRelation(String relationName);
}
