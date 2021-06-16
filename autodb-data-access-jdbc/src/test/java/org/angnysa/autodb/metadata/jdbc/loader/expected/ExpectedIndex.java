package org.angnysa.autodb.metadata.jdbc.loader.expected;

import lombok.Value;

@Value
public class ExpectedIndex {
    boolean unique;
    String[] columns;
}
