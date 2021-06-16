package org.angnysa.autodb.query.jdbc.types.jdbc40.javatime;

import lombok.NonNull;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class LocalTimeAccessor implements TypeAccessor<LocalTime> {
    @Override
    public JDBCType getJdbcType() {
        return JDBCType.TIME;
    }

    @Override
    public String getSqlType() {
        return null;
    }

    @Override
    public Class<LocalTime> getJavaClass() {
        return LocalTime.class;
    }

    @Override
    public LocalTime get(@NonNull ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, LocalTime data) throws SQLException {

    }
}
