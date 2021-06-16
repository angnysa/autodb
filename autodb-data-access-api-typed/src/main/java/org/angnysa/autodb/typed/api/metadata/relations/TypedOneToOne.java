package org.angnysa.autodb.typed.api.metadata.relations;

import org.angnysa.autodb.metadata.api.relations.OneToOne;
import org.angnysa.autodb.typed.api.metadata.TypedDatabase;
import org.angnysa.autodb.typed.api.metadata.TypedTable;

public interface TypedOneToOne<D extends TypedDatabase<D>, F extends TypedTable<D, F>, T extends TypedTable<D, T>> extends TypedOneTo<D, F, T>, TypedToOne<D, F, T>, OneToOne {

}
