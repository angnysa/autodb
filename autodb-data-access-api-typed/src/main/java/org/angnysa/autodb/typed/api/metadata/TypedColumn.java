package org.angnysa.autodb.typed.api.metadata;

import org.angnysa.autodb.metadata.api.Column;

public interface TypedColumn<T extends TypedTable<?, T>, J> extends Column {
    @Override
    T getTable();
}
