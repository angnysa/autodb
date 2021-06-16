package org.angnysa.autodb.metadata.jdbc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.angnysa.autodb.metadata.api.Column;
import org.angnysa.autodb.metadata.api.utils.Dumper;

import java.sql.JDBCType;

@Data
@EqualsAndHashCode(exclude = "table")
public final class JdbcColumn implements Column {
    private JdbcTable table;
    private final String name;
    private final String description;
    private final String sqlType;
    private final JDBCType jdbcType;
    private final boolean nullable;
    private final Integer maxLength;
    private final Integer numFraction;
    private final Integer radix;
    private final boolean autoGenerated;

    public void setTable(@NonNull JdbcTable table) {
        if (this.table == null) {
            this.table = table;
        } else {
            throw new IllegalStateException(String.format("Column '%s' is already linked to table '%s'", getName(), getTable().getFullyQualifiedName()));
        }
    }

    @Override
    public String getType() {
        return getSqlType();
    }

    @Override
    public boolean isMandatory() {
        return !isNullable();
    }

    @SneakyThrows
    @Override
    public String toString() {
        return Dumper.dumpColumn(this);
    }
}
