package org.angnysa.autodb.typed.api.metadata;

import lombok.NonNull;
import org.angnysa.autodb.metadata.api.Database;
import org.angnysa.autodb.metadata.api.Table;
import org.angnysa.autodb.metadata.api.relations.Relation;
import org.angnysa.autodb.typed.api.metadata.relations.TypedRelation;

import java.util.List;
import java.util.Map;

public interface TypedDatabase<D extends TypedDatabase<D>> extends Database<TypedTable<D, ?>> {
    @Override
    List<? extends TypedTable<D, ?>> getTables();

    @Override
    TypedTable<D, ?> getTable(@NonNull String tableName);

    @Override
    List<String> getRelationsName(@NonNull TypedTable<D, ?> table);

    @Override
    TypedRelation<D, ?, ?> getRelation(@NonNull TypedTable<D, ?> table, @NonNull String relationName);

    @Override
    List<? extends TypedRelation<D, ?, ?>> getRelations(TypedTable<D, ?> table);
}
