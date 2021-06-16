package org.angnysa.autodb.query.jdbc.types;

import lombok.NonNull;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeAccessor<C> {
    JDBCType getJdbcType();
    String getSqlType();
    Class<C> getJavaClass();
    C get(@NonNull ResultSet rs, int index) throws SQLException;
    void set(@NonNull PreparedStatement stmt, int index, C data) throws SQLException;
}
