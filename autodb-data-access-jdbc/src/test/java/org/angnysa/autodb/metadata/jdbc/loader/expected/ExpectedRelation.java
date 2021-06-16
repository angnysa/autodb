package org.angnysa.autodb.metadata.jdbc.loader.expected;

import lombok.Value;

@Value
public class ExpectedRelation {
    public enum Type { OneToOne, ManyToOne, OneToMany, ManyToMany }
    String name;
    Type type;
    boolean mandatory;
    String[] referencingColumns;
    String referencedTable;
    String[] referencedColumns;
}
