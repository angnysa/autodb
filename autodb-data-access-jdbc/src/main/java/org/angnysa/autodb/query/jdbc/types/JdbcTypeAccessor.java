package org.angnysa.autodb.query.jdbc.types;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class JdbcTypeAccessor<C> implements TypeAccessor<C> {

    @FunctionalInterface
    public interface ResultSetGetter<C> {
        C get(ResultSet rs, int index) throws SQLException;
    }

    @FunctionalInterface
    public interface PreparedStatementSetter<C> {
        void set(PreparedStatement stmt, int index, C data) throws SQLException;
    }

    @NonNull private final JDBCType jdbcType;
    private final String sqlType;
    @NonNull private final Class<C> javaClass;
    @NonNull private final ResultSetGetter<C> getter;
    @NonNull private final PreparedStatementSetter<C> setter;

    @Override
    public C get(@NonNull ResultSet rs, int index) throws SQLException {
        C data = getter.get(rs, index);
        if (rs.wasNull()) {
            return null;
        } else {
            return data;
        }
    }

    @Override
    public void set(@NonNull PreparedStatement stmt, int index, C data) throws SQLException {
        if (data == null) {
            stmt.setNull(index, jdbcType.getVendorTypeNumber());
        } else {
            setter.set(stmt, index, data);
        }
    }
}
