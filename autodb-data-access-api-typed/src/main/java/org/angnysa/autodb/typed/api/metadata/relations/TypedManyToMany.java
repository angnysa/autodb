package org.angnysa.autodb.typed.api.metadata.relations;

import org.angnysa.autodb.metadata.api.relations.ManyToMany;
import org.angnysa.autodb.typed.api.metadata.TypedDatabase;
import org.angnysa.autodb.typed.api.metadata.TypedTable;

public interface TypedManyToMany<D extends TypedDatabase<D>, F extends TypedTable<D, F>, T extends TypedTable<D, T>> extends TypedManyTo<D, F, T>, TypedToMany<D, F, T>, ManyToMany {

}
