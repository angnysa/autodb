package org.angnysa.autodb.typed.api.metadata;

import org.angnysa.autodb.metadata.api.Index;

import java.util.List;

public interface TypedIndex<T extends TypedTable<?, T>> extends Index {
    @Override
    T getTable();

    @Override
    List<? extends TypedColumn<T, ?>> getColumns();

    @Override
    TypedColumn<T, ?> getColumn(int index);

    @Override
    TypedColumn<T, ?> getColumn(String columnName);
}
