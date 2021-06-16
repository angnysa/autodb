package org.angnysa.autodb.metadata.jdbc.loader.db;

import lombok.extern.log4j.Log4j2;
import org.angnysa.autodb.metadata.jdbc.loader.AbstractJdbcMetadataLoaderTester;
import org.angnysa.autodb.metadata.jdbc.loader.SimpleDataSource;
import org.angnysa.autodb.metadata.jdbc.utils.JdbcConnectionUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.sql.CommonDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
public class TestJdbcMetadataLoader_HSqlDb extends AbstractJdbcMetadataLoaderTester {
    private static final CommonDataSource DATASOURCE = new SimpleDataSource("jdbc:hsqldb:mem:testdb");

    public TestJdbcMetadataLoader_HSqlDb() throws SQLException {
        super(DATASOURCE);
    }

    @BeforeAll
    static void setupDatabase() throws SQLException, IOException {
        setupSchema(DATASOURCE);
    }

    @AfterAll
    static void stopDatabase() throws SQLException {
        try (Connection con = JdbcConnectionUtil.getConnection(DATASOURCE)) {
            log.info("db shutdown");
            con.createStatement().executeUpdate("SHUTDOWN");
        }
    }
}
