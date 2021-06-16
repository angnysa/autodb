package org.angnysa.autodb.query.jdbc.types.jdbc40.javatime;

import lombok.NonNull;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetTime;

public class OffsetTimeAccessor implements TypeAccessor<OffsetTime> {
    @Override
    public JDBCType getJdbcType() {
        return JDBCType.TIME_WITH_TIMEZONE;
    }

    @Override
    public String getSqlType() {
        return null;
    }

    @Override
    public Class<OffsetTime> getJavaClass() {
        return OffsetTime.class;
    }

    @Override
    public OffsetTime get(@NonNull ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, OffsetTime data) throws SQLException {

    }
}
