package org.angnysa.autodb.typed.api.metadata.relations;

import org.angnysa.autodb.metadata.api.relations.OneTo;
import org.angnysa.autodb.typed.api.metadata.TypedDatabase;
import org.angnysa.autodb.typed.api.metadata.TypedTable;
import org.angnysa.autodb.typed.api.metadata.TypedUniqueIndex;

public interface TypedOneTo<D extends TypedDatabase<D>, F extends TypedTable<D, F>, T extends TypedTable<D, T>> extends TypedRelation<D, F, T>, OneTo {
    @Override
    TypedUniqueIndex<F> getReferencing();
}
