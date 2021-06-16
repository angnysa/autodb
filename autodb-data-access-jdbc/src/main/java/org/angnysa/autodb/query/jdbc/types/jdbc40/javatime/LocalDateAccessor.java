package org.angnysa.autodb.query.jdbc.types.jdbc40.javatime;

import lombok.NonNull;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LocalDateAccessor implements TypeAccessor<LocalDate> {
    @Override
    public JDBCType getJdbcType() {
        return JDBCType.DATE;
    }

    @Override
    public String getSqlType() {
        return null;
    }

    @Override
    public Class<LocalDate> getJavaClass() {
        return LocalDate.class;
    }

    @Override
    public LocalDate get(@NonNull ResultSet rs, int index) throws SQLException {
        return null;
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, LocalDate data) throws SQLException {

    }
}
