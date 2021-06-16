package org.angnysa.autodb.metadata.jdbc.loader.expected;

import lombok.Value;

@Value
public class ExpectedTable {
    String name;
    ExpectedColumn[] expectedColumns;
    ExpectedIndex primaryKey;
    ExpectedIndex[] expectedIndices;
}
