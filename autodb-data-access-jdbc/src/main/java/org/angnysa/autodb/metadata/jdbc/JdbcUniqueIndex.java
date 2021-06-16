package org.angnysa.autodb.metadata.jdbc;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;
import org.angnysa.autodb.metadata.api.UniqueIndex;
import org.angnysa.autodb.metadata.api.utils.Dumper;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class JdbcUniqueIndex extends JdbcIndex implements UniqueIndex {
    public JdbcUniqueIndex(String name, List<JdbcColumn> columns) {
        super(name, columns);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return Dumper.dumpIndex(this);
    }
}
