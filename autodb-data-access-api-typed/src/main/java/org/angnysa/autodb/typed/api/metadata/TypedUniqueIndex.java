package org.angnysa.autodb.typed.api.metadata;

import org.angnysa.autodb.metadata.api.UniqueIndex;

public interface TypedUniqueIndex<T extends TypedTable<?, T>> extends TypedIndex<T>, UniqueIndex {

}
