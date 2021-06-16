package org.angnysa.autodb.typed.api.metadata.relations;

import org.angnysa.autodb.metadata.api.relations.ToOne;
import org.angnysa.autodb.typed.api.metadata.TypedDatabase;
import org.angnysa.autodb.typed.api.metadata.TypedTable;
import org.angnysa.autodb.typed.api.metadata.TypedUniqueIndex;

public interface TypedToOne<D extends TypedDatabase<D>, F extends TypedTable<D, F>, T extends TypedTable<D, T>> extends TypedRelation<D, F, T>, ToOne {
    @Override
    TypedUniqueIndex<T> getReferenced();
}
