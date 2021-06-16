package org.angnysa.autodb.query.jdbc.types.jdbc40.javatime;

import lombok.NonNull;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

public class ZonedDateTimeAccessor implements TypeAccessor<ZonedDateTime> {
    @Override
    public JDBCType getJdbcType() {
        return JDBCType.TIMESTAMP_WITH_TIMEZONE;
    }

    @Override
    public String getSqlType() {
        return null;
    }

    @Override
    public Class<ZonedDateTime> getJavaClass() {
        return ZonedDateTime.class;
    }

    @Override
    public ZonedDateTime get(@NonNull ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, ZonedDateTime data) throws SQLException {

    }
}
