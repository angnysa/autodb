package org.angnysa.autodb.metadata.jdbc.loader.expected;

import lombok.Value;

import java.sql.JDBCType;

@Value
public class ExpectedColumn {
    String name;
    JDBCType type;
    boolean mandatory;
}
