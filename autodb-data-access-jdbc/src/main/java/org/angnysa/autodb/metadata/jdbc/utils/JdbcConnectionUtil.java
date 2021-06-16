package org.angnysa.autodb.metadata.jdbc.utils;

import javax.sql.CommonDataSource;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnectionUtil {
    public static Connection getConnection(CommonDataSource commonDataSource) throws SQLException {
        if (commonDataSource instanceof DataSource) {
            return ((DataSource) commonDataSource).getConnection();
        } else if (commonDataSource instanceof XADataSource) {
            return ((XADataSource) commonDataSource).getXAConnection().getConnection();
        } else if (commonDataSource instanceof ConnectionPoolDataSource) {
            return ((ConnectionPoolDataSource) commonDataSource).getPooledConnection().getConnection();
        } else {
            throw new IllegalStateException(String.format("Unsupported DataSource class: %s", commonDataSource.getClass()));
        }
    }
}
