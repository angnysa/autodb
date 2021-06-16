package org.angnysa.autodb.typed.api.metadata.relations;

import org.angnysa.autodb.metadata.api.relations.ToMany;
import org.angnysa.autodb.typed.api.metadata.TypedDatabase;
import org.angnysa.autodb.typed.api.metadata.TypedTable;

public interface TypedToMany<D extends TypedDatabase<D>, F extends TypedTable<D, F>, T extends TypedTable<D, T>> extends TypedRelation<D, F, T>, ToMany {
}
