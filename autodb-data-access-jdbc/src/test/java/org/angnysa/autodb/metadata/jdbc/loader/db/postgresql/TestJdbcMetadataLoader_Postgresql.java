package org.angnysa.autodb.metadata.jdbc.loader.db.postgresql;

import lombok.NonNull;
import org.angnysa.autodb.metadata.jdbc.loader.AbstractJdbcMetadataLoaderTester;
import org.angnysa.autodb.metadata.jdbc.loader.SimpleDataSource;
import org.angnysa.autodb.metadata.jdbc.loader.expected.ExpectedDatabase;
import org.angnysa.autodb.metadata.jdbc.utils.NameUtil;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.CommonDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

@Testcontainers
public abstract class TestJdbcMetadataLoader_Postgresql extends AbstractJdbcMetadataLoaderTester {
    public TestJdbcMetadataLoader_Postgresql(CommonDataSource commonDataSource) throws SQLException {
        super(commonDataSource);
    }

    public TestJdbcMetadataLoader_Postgresql(CommonDataSource commonDataSource, ExpectedDatabase expectedDatabase) throws SQLException {
        super(commonDataSource, expectedDatabase);
    }

    @Override
    protected String expectedNameToActual(String name) {
        return name.toLowerCase();
    }

    @Override
    protected String qname(@NonNull Connection connection, @NonNull String table) throws SQLException {
        return NameUtil.generateFullyQualifiedName(connection.getSchema(), expectedNameToActual(table));
    }
}
