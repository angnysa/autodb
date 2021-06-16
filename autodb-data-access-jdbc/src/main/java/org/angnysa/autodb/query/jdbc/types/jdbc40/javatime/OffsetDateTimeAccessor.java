package org.angnysa.autodb.query.jdbc.types.jdbc40.javatime;

import lombok.NonNull;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class OffsetDateTimeAccessor implements TypeAccessor<OffsetDateTime> {
    @Override
    public JDBCType getJdbcType() {
        return JDBCType.TIMESTAMP_WITH_TIMEZONE;
    }

    @Override
    public String getSqlType() {
        return null;
    }

    @Override
    public Class<OffsetDateTime> getJavaClass() {
        return OffsetDateTime.class;
    }

    @Override
    public OffsetDateTime get(@NonNull ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, OffsetDateTime data) throws SQLException {

    }
}
