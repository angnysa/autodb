package org.angnysa.autodb.metadata.api.relations;

import org.angnysa.autodb.metadata.api.Index;

public interface Relation {
    String getName();
    String getDescription();
    boolean isMandatory();
    Index getReferencing();
    Index getReferenced();
}
