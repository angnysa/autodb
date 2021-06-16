package org.angnysa.autodb.metadata.jdbc.loader.expected;

import lombok.Value;

@Value
public class ExpectedForeignKey {
    String name;
    boolean mandatory;
    String[] referencingColumns;
    String referencedTable;
    String[] referencedColumns;
}
