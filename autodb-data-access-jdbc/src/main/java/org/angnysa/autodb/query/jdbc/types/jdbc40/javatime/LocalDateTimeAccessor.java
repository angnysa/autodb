package org.angnysa.autodb.query.jdbc.types.jdbc40.javatime;

import lombok.NonNull;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LocalDateTimeAccessor implements TypeAccessor<LocalDateTime> {
    @Override
    public JDBCType getJdbcType() {
        return JDBCType.TIMESTAMP;
    }

    @Override
    public String getSqlType() {
        return null;
    }

    @Override
    public Class<LocalDateTime> getJavaClass() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime get(@NonNull ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, LocalDateTime data) throws SQLException {

    }
}
