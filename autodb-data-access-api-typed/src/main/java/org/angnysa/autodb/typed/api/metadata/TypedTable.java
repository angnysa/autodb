package org.angnysa.autodb.typed.api.metadata;


import org.angnysa.autodb.metadata.api.Index;
import org.angnysa.autodb.metadata.api.Table;
import org.angnysa.autodb.typed.api.metadata.relations.TypedRelation;

import java.util.List;

public interface TypedTable<D extends TypedDatabase<D>, T extends TypedTable<D, T>> extends Table {
    @Override
    D getDatabase();

    @Override
    List<? extends TypedColumn<T, ?>> getColumns();

    @Override
    TypedColumn<T, ?> getColumn(int index);

    @Override
    TypedColumn<T, ?> getColumn(String name);

    @Override
    TypedUniqueIndex<T> getPrimaryKey();

    @Override
    List<? extends TypedIndex<T>> getIndices();

    @Override
    List<? extends TypedIndex<T>> findIndexCovering(boolean unique, Index.Coverage coverage, List<String> columns);

    @Override
    List<? extends TypedRelation<D, T, ?>> getRelations();

    @Override
    TypedRelation<D, T, ?> getRelation(String relationName);
}
