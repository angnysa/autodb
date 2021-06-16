package org.angnysa.autodb.query.jdbc.types.jdbc41;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.angnysa.autodb.query.jdbc.types.TypeAccessor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Getter
public class Jdbc41TypeAccessor<T> implements TypeAccessor<T> {
    private final JDBCType jdbcType;
    private final String sqlType;
    private final Class<T> javaClass;

    @Override
    public T get(@NonNull ResultSet rs, int index) throws SQLException {
        T t = rs.getObject(index, getJavaClass());
        if (rs.wasNull()) {
            return null;
        } else {
            return t;
        }
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, T data) throws SQLException {
        if (data == null) {
            stmt.setNull(index, jdbcType.getVendorTypeNumber());
        } else {
            stmt.setObject(index, data);
        }
    }
}
