package org.angnysa.autodb.metadata.api.relations;

import org.angnysa.autodb.metadata.api.UniqueIndex;

public interface OneTo extends Relation {
    UniqueIndex getReferencing();
}
