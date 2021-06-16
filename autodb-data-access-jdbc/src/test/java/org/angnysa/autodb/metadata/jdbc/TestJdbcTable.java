package org.angnysa.autodb.metadata.jdbc;

import org.angnysa.autodb.metadata.api.Index;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestJdbcTable {
    private static final JdbcColumn COLUMN_A = new JdbcColumn("COLA", "", "INT", JDBCType.INTEGER, false, 0, 0, 0, false);
    private static final JdbcColumn COLUMN_B = new JdbcColumn("COLB", "", "INT", JDBCType.INTEGER, false, 0, 0, 0, false);
    private static final JdbcColumn COLUMN_C = new JdbcColumn("COLC", "", "INT", JDBCType.INTEGER, false, 0, 0, 0, false);
    private static final JdbcColumn COLUMN_D = new JdbcColumn("COLD", "", "INT", JDBCType.INTEGER, false, 0, 0, 0, false);
    private static final JdbcColumn COLUMN_E = new JdbcColumn("COLE", "", "INT", JDBCType.INTEGER, false, 0, 0, 0, false);
    private static final JdbcColumn COLUMN_F = new JdbcColumn("COLF", "", "INT", JDBCType.INTEGER, false, 0, 0, 0, false);

    private static final JdbcUniqueIndex TABLE_PK = new JdbcUniqueIndex("PK", List.of(COLUMN_A));

    private static final JdbcIndex INDEX_1 = new JdbcIndex("INDEX1", List.of(COLUMN_A));
    private static final JdbcIndex INDEX_2 = new JdbcIndex("INDEX2", List.of(COLUMN_A, COLUMN_B));
    private static final JdbcIndex INDEX_3 = new JdbcIndex("INDEX3", List.of(COLUMN_A, COLUMN_B, COLUMN_C));
    private static final JdbcIndex UINDEX_1 = new JdbcUniqueIndex("UINDEX1", List.of(COLUMN_A));
    private static final JdbcIndex UINDEX_2 = new JdbcUniqueIndex("UINDEX2", List.of(COLUMN_A, COLUMN_B));
    private static final JdbcIndex UINDEX_3 = new JdbcUniqueIndex("UINDEX3", List.of(COLUMN_A, COLUMN_B, COLUMN_C));

    private static final JdbcTable TABLE = new JdbcTable(
            null, null, "TEST", "", "TABLE",
            List.of(COLUMN_A, COLUMN_B, COLUMN_C, COLUMN_D, COLUMN_E, COLUMN_F),
            TABLE_PK,
            List.of(INDEX_1, INDEX_2, INDEX_3, UINDEX_1, UINDEX_2, UINDEX_3));

    @Test
    void testFindCovering_Unique_Exact() {
        List<JdbcIndex> indices = TABLE.findIndexCovering(true, Index.Coverage.MustCoverExact, List.of(COLUMN_A.getName(), COLUMN_B.getName()));
        assertEquals(Set.of(UINDEX_2), new HashSet<>(indices));
    }

    @Test
    void testFindCovering_Unique_CoverLess() {
        List<JdbcIndex> indices = TABLE.findIndexCovering(true, Index.Coverage.CanCoverLess, List.of(COLUMN_A.getName(), COLUMN_B.getName()));
        assertEquals(Set.of(UINDEX_1, UINDEX_2, UINDEX_3), new HashSet<>(indices));
    }

    @Test
    void testFindCovering_Unique_CoverMore() {
        List<JdbcIndex> indices = TABLE.findIndexCovering(true, Index.Coverage.CanCoverMore, List.of(COLUMN_A.getName(), COLUMN_B.getName()));
        assertEquals(Set.of(UINDEX_2, UINDEX_3), new HashSet<>(indices));
    }

    @Test
    void testFindCovering_All_Exact() {
        List<JdbcIndex> indices = TABLE.findIndexCovering(false, Index.Coverage.MustCoverExact, List.of(COLUMN_A.getName(), COLUMN_B.getName()));
        assertEquals(Set.of(INDEX_2, UINDEX_2), new HashSet<>(indices));
    }

    @Test
    void testFindCovering_All_CoverLess() {
        List<JdbcIndex> indices = TABLE.findIndexCovering(false, Index.Coverage.CanCoverLess, List.of(COLUMN_A.getName(), COLUMN_B.getName()));
        assertEquals(Set.of(UINDEX_1, UINDEX_2, UINDEX_3, INDEX_1, INDEX_2, INDEX_3), new HashSet<>(indices));
    }

    @Test
    void testFindCovering_All_CoverMore() {
        List<JdbcIndex> indices = TABLE.findIndexCovering(false, Index.Coverage.CanCoverMore, List.of(COLUMN_A.getName(), COLUMN_B.getName()));
        assertEquals(Set.of(INDEX_2, INDEX_3, UINDEX_2, UINDEX_3), new HashSet<>(indices));
    }
}
