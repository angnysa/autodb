package org.angnysa.autodb.metadata.jdbc.loader.db.postgresql;

import org.angnysa.autodb.metadata.jdbc.loader.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.CommonDataSource;
import java.io.IOException;
import java.sql.SQLException;

@Testcontainers
public class TestJdbcMetadataLoader_Postgresql_96 extends TestJdbcMetadataLoader_Postgresql {
    private static final CommonDataSource DATASOURCE = new SimpleDataSource("jdbc:tc:postgresql:9.6:///testdb?TC_DAEMON=true");

    public TestJdbcMetadataLoader_Postgresql_96() throws SQLException {
        super(DATASOURCE);
    }

    @BeforeAll
    static void setupDatabase() throws SQLException, IOException {
        setupSchema(DATASOURCE);
    }
}
