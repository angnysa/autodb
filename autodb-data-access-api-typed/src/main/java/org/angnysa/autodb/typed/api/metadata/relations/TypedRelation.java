package org.angnysa.autodb.typed.api.metadata.relations;

import org.angnysa.autodb.metadata.api.relations.Relation;
import org.angnysa.autodb.typed.api.metadata.TypedDatabase;
import org.angnysa.autodb.typed.api.metadata.TypedIndex;
import org.angnysa.autodb.typed.api.metadata.TypedTable;

public interface TypedRelation<D extends TypedDatabase<D>, F extends TypedTable<D, F>, T extends TypedTable<D, T>> extends Relation {
    @Override
    TypedIndex<F> getReferencing();

    @Override
    TypedIndex<T> getReferenced();
}
