package org.angnysa.autodb.metadata.jdbc;

import org.angnysa.autodb.metadata.api.Index;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJdbcIndex {
    private static final JdbcColumn COLUMN_A = new JdbcColumn("COLA", "", "INT", JDBCType.INTEGER, false, 10,0, 10, false);
    private static final JdbcColumn COLUMN_B = new JdbcColumn("COLB", "", "INT", JDBCType.INTEGER, false, 10,0, 10, false);
    private static final JdbcColumn COLUMN_C = new JdbcColumn("COLC", "", "INT", JDBCType.INTEGER, false, 10,0, 10, false);

    private static final JdbcIndex INDEX = new JdbcIndex("IDX", List.of(COLUMN_A, COLUMN_B, COLUMN_C));

    @Test
    void testCoversExact_Equals() {
        assertTrue(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLA", "COLB", "COLC")));
    }

    @Test
    void testCoversExact_LessThanIndex() {
        assertFalse(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLA", "COLB")));
    }

    @Test
    void testCoversExact_MoreThanIndex() {
        assertFalse(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLA", "COLB", "COLC", "COLD")));
    }

    @Test
    void testCoversExact_Partial() {
        assertFalse(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLA", "COLB", "COLD")));
    }

    @Test
    void testCoversExact_Different() {
        assertFalse(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLD", "COLE", "COLF")));
    }

    @Test
    void testCoversLess_Equals() {
        assertTrue(INDEX.covers(Index.Coverage.CanCoverLess, List.of("COLA", "COLB", "COLC")));
    }

    @Test
    void testCoversLess_LessThanIndex() {
        assertTrue(INDEX.covers(Index.Coverage.CanCoverLess, List.of("COLA", "COLB")));
    }

    @Test
    void testCoversLess_MoreThanIndex() {
        assertTrue(INDEX.covers(Index.Coverage.CanCoverLess, List.of("COLA", "COLB", "COLC", "COLD")));
    }

    @Test
    void testCoversLess_Partial() {
        assertTrue(INDEX.covers(Index.Coverage.CanCoverLess, List.of("COLA", "COLB", "COLD")));
    }

    @Test
    void testCoversLess_Different() {
        assertFalse(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLD", "COLE", "COLF")));
    }

    @Test
    void testCoversOrMore_Equals() {
        assertTrue(INDEX.covers(Index.Coverage.CanCoverMore, List.of("COLA", "COLB", "COLC")));
    }

    @Test
    void testCoversOrMore_LessThanIndex() {
        assertTrue(INDEX.covers(Index.Coverage.CanCoverMore, List.of("COLA", "COLB")));
    }

    @Test
    void testCoversOrMore_MoreThanIndex() {
        assertFalse(INDEX.covers(Index.Coverage.CanCoverMore, List.of("COLA", "COLB", "COLC", "COLD")));
    }

    @Test
    void testCoversOrMore_Partial() {
        assertFalse(INDEX.covers(Index.Coverage.CanCoverMore, List.of("COLA", "COLB", "COLD")));
    }

    @Test
    void testCoversOrMore_Different() {
        assertFalse(INDEX.covers(Index.Coverage.MustCoverExact, List.of("COLD", "COLE", "COLF")));
    }
}
