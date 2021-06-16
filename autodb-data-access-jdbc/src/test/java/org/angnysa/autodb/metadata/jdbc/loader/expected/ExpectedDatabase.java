package org.angnysa.autodb.metadata.jdbc.loader.expected;

import lombok.Value;

@Value
public class ExpectedDatabase {
    ExpectedTable[] expectedTables;
    ExpectedForeignKey[][] expectedForeignKeys;
    ExpectedRelation[][] expectedRelations;
}
