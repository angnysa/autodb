package org.angnysa.autodb.metadata.api;

import lombok.NonNull;
import org.angnysa.autodb.metadata.api.relations.Relation;

import java.util.List;
import java.util.Map;

public interface Database<T extends Table> {
    String getName();
    String getVersion();
    List<String> getTablesName();
    List<? extends T> getTables();
    T getTable(@NonNull String tableName);
    List<String> getRelationsName(@NonNull T table);
    Relation getRelation(@NonNull T table, @NonNull String relationName);
    List<? extends Relation> getRelations(T table);
}
