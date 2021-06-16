package org.angnysa.autodb.metadata.jdbc.loader;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.angnysa.autodb.metadata.jdbc.*;
import org.angnysa.autodb.metadata.jdbc.loader.expected.*;
import org.angnysa.autodb.metadata.jdbc.utils.JdbcConnectionUtil;
import org.angnysa.autodb.metadata.jdbc.utils.NameUtil;
import org.angnysa.autodb.metadata.jdbc.JdbcScriptRunner;
import org.angnysa.autodb.metadata.jdbc.relations.*;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import javax.sql.CommonDataSource;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public abstract class AbstractJdbcMetadataLoaderTester {

    public static final String[] DEFAULT_DDL_SCRIPTS = new String[] {"/db/ddl/metadata/_default.sql"};
    public static final Charset DEFAULT_DDL_CHARSET = Charset.defaultCharset();

    private static final String[] TABLE_TYPES = new String[] { "TABLE", "VIEW" };

    public final String TABLE_NAME_121_A = expectedNameToActual("TEST_121_A");
    public final String TABLE_NAME_121_B = expectedNameToActual("TEST_121_B");
    public final String TABLE_NAME_M21_A = expectedNameToActual("TEST_M21_A");
    public final String TABLE_NAME_M21_B = expectedNameToActual("TEST_M21_B");
    public final String TABLE_NAME_M2M_A = expectedNameToActual("TEST_M2M_A");
    public final String TABLE_NAME_M2M_B = expectedNameToActual("TEST_M2M_B");
    public final String TABLE_NAME_M2M_REL = expectedNameToActual("TEST_M2M_REL");

    public final String FK_NAME_121_B_A = expectedNameToActual("FK_TEST_121_B_A");
    public final String FK_NAME_M21_B_A = expectedNameToActual("FK_TEST_M21_B_A");
    public final String FK_NAME_M2M_REL_A = expectedNameToActual("FK_TEST_M2M_REL_A");
    public final String FK_NAME_M2M_REL_B = expectedNameToActual("FK_TEST_M2M_REL_B");

    private final String COL_NAME_ID1 = expectedNameToActual("ID1");
    private final String COL_NAME_ID2 = expectedNameToActual("ID2");
    private final String COL_NAME_AID1 = expectedNameToActual("A_ID1");
    private final String COL_NAME_AID2 = expectedNameToActual("A_ID2");
    private final String COL_NAME_ABID1 = expectedNameToActual("AB_ID1");
    private final String COL_NAME_BID2 = expectedNameToActual("B_ID2");

    protected final CommonDataSource commonDataSource;
    protected final ExpectedDatabase expectedDatabase;
    protected final JdbcMetadataLoader jdbcMetadataLoader;

    public AbstractJdbcMetadataLoaderTester(CommonDataSource commonDataSource) throws SQLException {
        this.commonDataSource = commonDataSource;
        this.expectedDatabase = buildDefaultExpected();
        this.jdbcMetadataLoader = new JdbcMetadataLoader(commonDataSource);

        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            log.debug("Using database: {} {}, driver: {} {}, default catalog: '{}', default schema: '{}'",
                    connection.getMetaData().getDatabaseProductName(),
                    connection.getMetaData().getDatabaseProductVersion(),
                    connection.getMetaData().getDriverName(),
                    connection.getMetaData().getDriverVersion(),
                    connection.getCatalog(),
                    connection.getSchema());
        }
    }

    public AbstractJdbcMetadataLoaderTester(CommonDataSource commonDataSource, ExpectedDatabase expectedDatabase) throws SQLException {
        this.commonDataSource = commonDataSource;
        this.expectedDatabase = expectedDatabase;
        this.jdbcMetadataLoader = new JdbcMetadataLoader(commonDataSource);

        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            log.debug("Using database: {} {}, driver: {} {}, default catalog: '{}', default schema: '{}'",
                    connection.getMetaData().getDatabaseProductName(),
                    connection.getMetaData().getDatabaseProductVersion(),
                    connection.getMetaData().getDriverName(),
                    connection.getMetaData().getDriverVersion(),
                    connection.getCatalog(),
                    connection.getSchema());
        }
    }

    protected static void setupSchema(CommonDataSource commonDataSource) throws SQLException, IOException {
        setupSchema(commonDataSource, DEFAULT_DDL_SCRIPTS);
    }

    protected static void setupSchema(CommonDataSource commonDataSource, String[] ddlScripts) throws SQLException, IOException {
        setupSchema(commonDataSource, DEFAULT_DDL_SCRIPTS, DEFAULT_DDL_CHARSET);
    }

    protected static void setupSchema(CommonDataSource commonDataSource, String[] ddlScripts, Charset ddlScriptsCharset) throws SQLException, IOException {
        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            try {
                connection.setAutoCommit(false);
                for (String ddlScript : ddlScripts) {
                    try (InputStream is = AbstractJdbcMetadataLoaderTester.class.getResourceAsStream(ddlScript)) {
                        if (is == null) {
                            throw new IllegalArgumentException(String.format("DDL Script '%s' not found.", ddlScript));
                        } else {
                            JdbcScriptRunner scriptRunner = new JdbcScriptRunner(connection);
                            scriptRunner.setStopOnError(true);

                            Reader reader = new BufferedReader(new InputStreamReader(is, ddlScriptsCharset));
                            scriptRunner.runScript(reader);
                        }
                        connection.commit();
                    }
                }
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

    protected ExpectedDatabase buildDefaultExpected() {
        return new ExpectedDatabase(
                new ExpectedTable[] {
                        new ExpectedTable(
                                TABLE_NAME_121_A,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_ID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                new ExpectedIndex[] {}),
                        new ExpectedTable(
                                TABLE_NAME_121_B,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_ID2, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_AID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_AID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                new ExpectedIndex[] {
                                        new ExpectedIndex(true, new String[] { COL_NAME_AID2, COL_NAME_AID2 })}),
                        new ExpectedTable(
                                TABLE_NAME_M21_A,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_ID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                new ExpectedIndex[] {}),
                        new ExpectedTable(
                                TABLE_NAME_M21_B,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_ID2, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_AID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_AID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                new ExpectedIndex[] {
                                        new ExpectedIndex(false, new String[] { COL_NAME_AID2, COL_NAME_AID2 })}),
                        new ExpectedTable(
                                TABLE_NAME_M2M_A,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_ID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                new ExpectedIndex[] {}),
                        new ExpectedTable(
                                TABLE_NAME_M2M_B,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_ID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                new ExpectedIndex[] {}),
                        new ExpectedTable(
                                TABLE_NAME_M2M_REL,
                                new ExpectedColumn[] {
                                        new ExpectedColumn(COL_NAME_ABID1, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_AID2, JDBCType.INTEGER, true),
                                        new ExpectedColumn(COL_NAME_BID2, JDBCType.INTEGER, true)},
                                new ExpectedIndex(true, new String[] { COL_NAME_ABID1, COL_NAME_AID2, COL_NAME_BID2 }),
                                new ExpectedIndex[] {
                                        new ExpectedIndex(false, new String[] { COL_NAME_ABID1, COL_NAME_AID2 }),
                                        new ExpectedIndex(false, new String[] { COL_NAME_ABID1, COL_NAME_BID2 })})},
                        new ExpectedForeignKey[][] {
                                new ExpectedForeignKey[] {},
                                new ExpectedForeignKey[] {
                                        new ExpectedForeignKey(FK_NAME_121_B_A, true, new String[]{ COL_NAME_AID1, COL_NAME_AID2 }, TABLE_NAME_121_A, new String[] { COL_NAME_ID1, COL_NAME_ID2 })},
                                new ExpectedForeignKey[] {},
                                new ExpectedForeignKey[] {
                                        new ExpectedForeignKey(FK_NAME_M21_B_A, true, new String[]{ COL_NAME_AID1, COL_NAME_AID2 }, TABLE_NAME_M21_A, new String[] { COL_NAME_ID1, COL_NAME_ID2 })},
                                new ExpectedForeignKey[] {},
                                new ExpectedForeignKey[] {},
                                new ExpectedForeignKey[] {
                                        new ExpectedForeignKey(FK_NAME_M2M_REL_A, true, new String[]{ COL_NAME_ABID1, COL_NAME_AID2 }, TABLE_NAME_M2M_A, new String[] { COL_NAME_ID1, COL_NAME_ID2 }),
                                        new ExpectedForeignKey(FK_NAME_M2M_REL_B, true, new String[]{ COL_NAME_ABID1, COL_NAME_BID2 }, TABLE_NAME_M2M_B, new String[] { COL_NAME_ID1, COL_NAME_ID2 })}},
                        new ExpectedRelation[][] {
                                new ExpectedRelation[] {
                                        new ExpectedRelation(FK_NAME_121_B_A, ExpectedRelation.Type.OneToOne, false, new String[] { COL_NAME_ID1, COL_NAME_ID2 }, TABLE_NAME_121_B, new String[]{ COL_NAME_AID1, COL_NAME_AID2 })},
                                new ExpectedRelation[] {
                                        new ExpectedRelation(FK_NAME_121_B_A, ExpectedRelation.Type.OneToOne, true, new String[] { COL_NAME_AID1, COL_NAME_AID2 }, TABLE_NAME_121_A, new String[]{ COL_NAME_ID1, COL_NAME_ID2 })},
                                new ExpectedRelation[] {
                                        new ExpectedRelation(FK_NAME_M21_B_A, ExpectedRelation.Type.OneToMany, false, new String[] { COL_NAME_ID1, COL_NAME_ID2 }, TABLE_NAME_M21_B, new String[]{ COL_NAME_AID1, COL_NAME_AID2 })},
                                new ExpectedRelation[] {
                                        new ExpectedRelation(FK_NAME_M21_B_A, ExpectedRelation.Type.ManyToOne, true, new String[] { COL_NAME_AID1, COL_NAME_AID2 }, TABLE_NAME_M21_A, new String[]{ COL_NAME_ID1, COL_NAME_ID2 })},
                                new ExpectedRelation[] {
                                        new ExpectedRelation(FK_NAME_M2M_REL_A, ExpectedRelation.Type.ManyToMany, false, new String[] { COL_NAME_ID1, COL_NAME_ID2 }, TABLE_NAME_M2M_B, new String[]{ COL_NAME_ID1, COL_NAME_ID2 })},
                                new ExpectedRelation[] {
                                        new ExpectedRelation(FK_NAME_M2M_REL_B, ExpectedRelation.Type.ManyToMany, false, new String[] { COL_NAME_ID1, COL_NAME_ID2 }, TABLE_NAME_M2M_A, new String[]{ COL_NAME_ID1, COL_NAME_ID2 })},
                                new ExpectedRelation[] {}});
    }

    @TestFactory
    List<DynamicTest> testLoadColumns() {
        List<DynamicTest> tests = new ArrayList<>();
        for (ExpectedTable expectedTable : expectedDatabase.getExpectedTables()) {
            tests.add(DynamicTest.dynamicTest("testLoadColumns_"+expectedTable.getName(), () -> testLoadColumns(expectedTable.getName(), expectedTable.getExpectedColumns())));
        }

        return tests;
    }

    @TestFactory
    List<DynamicTest> testLoadPrimaryKey() {
        List<DynamicTest> tests = new ArrayList<>();
        for (ExpectedTable expectedTable : expectedDatabase.getExpectedTables()) {
            tests.add(DynamicTest.dynamicTest("testLoadPrimaryKey_"+expectedTable.getName(), () -> testLoadPrimaryKey(expectedTable.getName(), expectedTable.getPrimaryKey())));
        }

        return tests;
    }

    @TestFactory
    List<DynamicTest> testLoadIndices() {
        List<DynamicTest> tests = new ArrayList<>();
        for (ExpectedTable expectedTable : expectedDatabase.getExpectedTables()) {
            tests.add(DynamicTest.dynamicTest("testLoadIndices_"+expectedTable.getName(), () -> testLoadIndices(expectedTable.getName(), expectedTable.getExpectedIndices())));
        }

        return tests;
    }

    @TestFactory
    List<DynamicTest> testLoadTables() {
        List<DynamicTest> tests = new ArrayList<>();
        for (ExpectedTable expectedTable : expectedDatabase.getExpectedTables()) {
            tests.add(DynamicTest.dynamicTest("testLoadTables_"+expectedTable.getName(), () -> testLoadTable(expectedTable.getName(), expectedTable)));
        }

        return tests;
    }

    @TestFactory
    List<DynamicTest> testLoadForeignKeys() {
        List<DynamicTest> tests = new ArrayList<>();

        ExpectedTable[] expectedTables = expectedDatabase.getExpectedTables();
        for (int i = 0, expectedTablesLength = expectedTables.length; i < expectedTablesLength; i++) {
            ExpectedTable expectedTable = expectedTables[i];
            ExpectedForeignKey[] expectedForeignKeys = expectedDatabase.getExpectedForeignKeys()[i];

            tests.add(DynamicTest.dynamicTest("testLoadForeignKeys_" + expectedTable.getName(), () -> testLoadForeignKeys(expectedTable.getName(), expectedForeignKeys)));
        }

        return tests;
    }

    @TestFactory
    List<DynamicTest> testLoadRelations() {
        List<DynamicTest> tests = new ArrayList<>();

        ExpectedTable[] expectedTables = expectedDatabase.getExpectedTables();
        for (int i = 0, expectedTablesLength = expectedTables.length; i < expectedTablesLength; i++) {
            ExpectedTable expectedTable = expectedTables[i];
            ExpectedRelation[] expectedRelations = expectedDatabase.getExpectedRelations()[i];

            tests.add(DynamicTest.dynamicTest("testLoadRelations_" + expectedTable.getName(), () -> testLoadRelations(expectedTable.getName(), expectedRelations)));
        }

        return tests;
    }

    private void testLoadColumns(String tableName, ExpectedColumn[] expectedColumns) throws SQLException {
        log.debug("Target table: {}", tableName);
        log.debug("Expected columns: {}", (Object) expectedColumns);

        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            List<JdbcColumn> columns = jdbcMetadataLoader.loadColumns(
                    connection,
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName);

            assertColumns(expectedColumns, null, columns);
        }
    }

    private void testLoadPrimaryKey(String tableName, ExpectedIndex expectedIndex) throws SQLException {
        log.debug("Target table: {}", tableName);
        log.debug("Expected Primary Key: {}", expectedIndex);

        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            JdbcUniqueIndex primaryKey = jdbcMetadataLoader.loadPrimaryKey(
                    connection,
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName,
                    jdbcMetadataLoader.loadColumns(
                            connection,
                            connection.getCatalog(),
                            connection.getSchema(),
                            tableName));

            assertIndex(expectedIndex, null, primaryKey);
        }
    }

    private void testLoadIndices(String tableName, ExpectedIndex[] expectedIndices) throws SQLException {
        log.debug("Target table: {}", tableName);
        log.debug("Expected Indices: {}", (Object) expectedIndices);

        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            List<JdbcIndex> indices = jdbcMetadataLoader.loadIndices(
                    connection,
                    connection.getCatalog(),
                    connection.getSchema(),
                    tableName,
                    jdbcMetadataLoader.loadColumns(connection, connection.getCatalog(), connection.getSchema(), tableName));

            assertIndices(expectedIndices, null, indices);
        }
    }

    private void testLoadTable(String tableName, ExpectedTable expectedTable) throws SQLException {
        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            List<JdbcTable> tables = jdbcMetadataLoader.loadTables(connection, connection.getCatalog(), connection.getSchema(), TABLE_TYPES);

            assertTable(expectedTable, null, findByName(tables, qname(connection, tableName), JdbcTable::getFullyQualifiedName));
        }
    }

    private void testLoadForeignKeys(String tableName, ExpectedForeignKey[] expectedForeignKeys) throws SQLException {
        log.debug("Target table: {}", tableName);
        log.debug("Expected Foreign Keys: {}", (Object) expectedForeignKeys);


        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            List<JdbcTable> tables = jdbcMetadataLoader.loadTables(connection, connection.getCatalog(), connection.getSchema(), TABLE_TYPES);

            JdbcTable table = findByName(tables, qname(connection, tableName), JdbcTable::getFullyQualifiedName);

            List<ForeignKey> foreignKeys = jdbcMetadataLoader.loadImportedForeignKeys(connection, table, tables);

            assertForeignKeys(expectedForeignKeys, table, foreignKeys);
        }
    }

    private void testLoadRelations(String tableName, ExpectedRelation[] expectedRelations) throws SQLException {
        log.debug("Target table: {}", tableName);
        log.debug("Expected Relations: {}", (Object) expectedRelations);

        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            List<JdbcTable> tables = jdbcMetadataLoader.loadTables(connection, connection.getCatalog(), connection.getSchema(), TABLE_TYPES);
            Map<JdbcTable, List<ForeignKey>> foreignKeys = new HashMap<>();
            for (JdbcTable table : tables) {
                foreignKeys.put(table, jdbcMetadataLoader.loadImportedForeignKeys(connection, table, tables));
            }

            JdbcTable table = findByName(tables, qname(connection, tableName), JdbcTable::getFullyQualifiedName);

            Map<JdbcTable, List<JdbcRelation>> relations = jdbcMetadataLoader.loadRelations(foreignKeys);

            assertRelations(expectedRelations, table, relations.getOrDefault(table, List.of()));
        }
    }

    @Test
    protected void testLoadDatabase() throws SQLException {
        try (Connection connection = JdbcConnectionUtil.getConnection(commonDataSource)) {
            JdbcDatabase database = jdbcMetadataLoader.loadDatabase(connection.getCatalog(), connection.getSchema(), TABLE_TYPES);

            assertDatabase(connection, expectedDatabase, database);
        }
    }

    protected String qname(@NonNull Connection connection, @NonNull String table) throws SQLException {
        return NameUtil.generateFullyQualifiedName(connection.getCatalog(), connection.getSchema(), table);
    }

    protected void assertDatabase(@NonNull Connection connection, ExpectedDatabase expected, JdbcDatabase actual) throws SQLException {
        assertEquals(expected.getExpectedTables().length, actual.getTables().size());

        for (int i=0; i < expected.getExpectedTables().length; i++) {
            ExpectedTable expectedTable = expected.getExpectedTables()[i];
            log.debug("Verifying table: {}", expectedTable.getName());

            String actualQName = qname(connection, expectedTable.getName());
            JdbcTable actualTable = actual.getTable(actualQName);

            assertTable(expectedTable, actual, actualTable);
            assertForeignKeys(
                    expected.getExpectedForeignKeys()[i],
                    actualTable,
                    actual.getForeignKeys(actualTable));
            assertRelations(expected.getExpectedRelations()[i], actualTable, actual.getRelations(actualTable));
        }
    }

    protected void assertTable(ExpectedTable expected, JdbcDatabase expectedDatabase, JdbcTable actual) {
        assertEquals(expectedDatabase, actual.getDatabase());

        assertColumns(expected.getExpectedColumns(), actual, actual.getColumns());
        assertIndex  (expected.getPrimaryKey()     , actual, actual.getPrimaryKey());
        assertIndices(expected.getExpectedIndices(), actual, actual.getIndices());
    }

    protected void assertColumns(ExpectedColumn[] expected, JdbcTable expectedTable, List<JdbcColumn> actual) {
        assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            log.debug("Verifying column: {} :: {}", expectedTable == null ? "null" : expectedTable.getName(), expected[i].getName());

            assertColumn(expected[i], expectedTable, actual.get(i));
        }
    }

    protected void assertColumn(ExpectedColumn expected, JdbcTable expectedTable, JdbcColumn actual) {
        assertEquals(expected.getName().toUpperCase(), actual.getName().toUpperCase());
        assertEquals(expected.getType(), actual.getJdbcType());
        assertEquals(expected.isMandatory(), actual.isMandatory());
        assertEquals(expectedTable, actual.getTable());
    }

    protected void assertIndices(ExpectedIndex[] expected, JdbcTable expectedTable, List<JdbcIndex> actual) {
        for (ExpectedIndex expectedIndex : expected) {
            log.debug("Verifying index: {} :: {}", expectedTable == null ? "null" : expectedTable.getName(), expectedIndex.getColumns());

            // index names for constraints don't necessarily match.
            // search by structure instead
            JdbcIndex actualIndex = findIndexByStructure(expectedIndex, actual);

            assertIndex(expectedIndex, expectedTable, actualIndex);
        }
    }

    private JdbcIndex findIndexByStructure(@NonNull ExpectedIndex expectedIndex, List<JdbcIndex> actual) {
        for (JdbcIndex actualIndex : actual) {
            if (expectedIndex.isUnique() == (actualIndex instanceof JdbcUniqueIndex)) {
                for (int i = 0; i < expectedIndex.getColumns().length; i++) {
                    String expectedColumnName = expectedIndex.getColumns()[i];
                    String actualColumnName = actualIndex.getColumns().get(i).getName();

                    if (! expectedColumnName.equalsIgnoreCase(actualColumnName)) {
                        // mismatch
                        break;
                    }
                }

                // match
                return actualIndex;
            }
        }

        // no match
        fail(String.format("Expected index matching: %s", expectedIndex));
        throw new IllegalStateException();
    }

    protected void assertIndex(ExpectedIndex expected, JdbcTable expectedTable, JdbcIndex actual) {
        assertEquals(expectedTable, actual.getTable());
        assertEquals(expected.isUnique(), actual instanceof JdbcUniqueIndex);

        if (expectedTable != null) {
            for (String expectedColumn : expected.getColumns()) {
                assertNotNull(expectedTable.getColumn(expectedColumn), String.format("Expected column '%s' in index '%s'.", expectedColumn, actual.getName()));
            }
        }
    }

    protected void assertForeignKeys(ExpectedForeignKey[] expected, JdbcTable expectedReferencing, List<ForeignKey> actual) {
        assertEquals(expected.length, actual.size());

        for (ExpectedForeignKey expectedForeignKey : expected) {
            log.debug("Verifying foreign key: {}", expectedForeignKey.getName());

            ForeignKey actualForeignKey = findByName(actual, expectedForeignKey.getName(), ForeignKey::getName);

            assertForeignKey(expectedForeignKey, expectedReferencing, actualForeignKey);
        }
    }

    protected void assertForeignKey(ExpectedForeignKey expected, JdbcTable expectedReferencing, ForeignKey actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.isMandatory(), actual.isMandatory());

        assertEquals(expected.getReferencingColumns().length, expected.getReferencedColumns().length);

        assertEquals(expectedReferencing, actual.getReferencing().getTable());
        assertEquals(expected.getReferencingColumns().length, actual.getReferencing().getColumns().size());
        for (int i = 0; i < expected.getReferencingColumns().length; i++) {
            String referencingColumn = expected.getReferencingColumns()[i];
            assertEquals(referencingColumn, actual.getReferencing().getColumns().get(i).getName());
        }

        assertEquals(expected.getReferencedTable(), actual.getReferenced().getTable().getName());
        assertEquals(expected.getReferencedColumns().length, actual.getReferenced().getColumns().size());
        for (int i = 0; i < expected.getReferencedColumns().length; i++) {
            String referencedColumn = expected.getReferencedColumns()[i];
            assertEquals(referencedColumn, actual.getReferenced().getColumns().get(i).getName());
        }
    }

    protected void assertRelations(ExpectedRelation[] expected, JdbcTable expectedReferencing, List<JdbcRelation> actual) {
        assertEquals(expected.length, actual.size());

        for (ExpectedRelation expectedRelation : expected) {
            log.debug("Verifying relation: {} :: {}", expectedReferencing.getName(), expectedRelation.getName());

            JdbcRelation actualRelation = findByName(actual, expectedRelation.getName(), JdbcRelation::getName);

            assertRelation(expectedRelation, expectedReferencing, actualRelation);
        }
    }

    protected void assertRelation(ExpectedRelation expected, JdbcTable expectedReferencing, JdbcRelation actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.isMandatory(), actual.isMandatory());

        switch (expected.getType()) {
            case OneToOne  : assertTrue(actual instanceof JdbcOneToOne  ); break;
            case ManyToOne : assertTrue(actual instanceof JdbcManyToOne ); break;
            case OneToMany : assertTrue(actual instanceof JdbcOneToMany ); break;
            case ManyToMany: assertTrue(actual instanceof JdbcManyToMany); break;
        }

        assertEquals(expected.getReferencingColumns().length, expected.getReferencedColumns().length);

        assertEquals(expectedReferencing, actual.getReferencing().getTable());
        assertEquals(expected.getReferencingColumns().length, actual.getReferencing().getColumns().size());
        for (int i = 0; i < expected.getReferencingColumns().length; i++) {
            String referencingColumn = expected.getReferencingColumns()[i];
            assertEquals(referencingColumn, actual.getReferencing().getColumns().get(i).getName());
        }

        assertEquals(expected.getReferencedTable(), actual.getReferenced().getTable().getName());
        assertEquals(expected.getReferencedColumns().length, actual.getReferenced().getColumns().size());
        for (int i = 0; i < expected.getReferencedColumns().length; i++) {
            String referencedColumn = expected.getReferencedColumns()[i];
            assertEquals(referencedColumn, actual.getReferenced().getColumns().get(i).getName());
        }
    }

    protected String expectedNameToActual(String name) {
        return name;
    }

    private <T> T findByName(List<T> list, String name, Function<T, String> nameGetter) {
        for (T object : list) {
            if (nameGetter.apply(object).equals(name)) {
                return object;
            }
        }

        fail(String.format("Expected item named '%s', found none", name));
        throw new IllegalStateException();
    }
}
