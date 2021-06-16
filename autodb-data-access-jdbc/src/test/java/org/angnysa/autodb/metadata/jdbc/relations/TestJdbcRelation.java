package org.angnysa.autodb.metadata.jdbc.relations;

import lombok.extern.log4j.Log4j2;
import org.angnysa.autodb.metadata.jdbc.*;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class TestJdbcRelation {

    private static final JdbcColumn REFERENCING_COL_ID = new JdbcColumn(
            "ID",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    private static final JdbcColumn REFERENCING_COL_FK = new JdbcColumn(
            "FKID",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    public static final JdbcUniqueIndex REFERENCING_PK = new JdbcUniqueIndex("REFERENCING_PK", List.of(REFERENCING_COL_ID));
    public static final JdbcIndex REFERENCING_IDX_FK = new JdbcIndex("REFERENCING_IDX_FK", List.of(REFERENCING_COL_FK));

    private static final JdbcTable REFERENCING_TABLE = new JdbcTable(
            null, null, "REFERENCING_TABLE",
            "", "TABLE",
            List.of(REFERENCING_COL_ID, REFERENCING_COL_FK),
            REFERENCING_PK,
            List.of(REFERENCING_PK, REFERENCING_IDX_FK));




    private static final JdbcColumn REFERENCING_121_COL_ID = new JdbcColumn(
            "ID",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    private static final JdbcColumn REFERENCING_121_COL_FK = new JdbcColumn(
            "FKID",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    public static final JdbcUniqueIndex REFERENCING_121_PK = new JdbcUniqueIndex("REFERENCING_121_PK", List.of(REFERENCING_121_COL_ID));
    public static final JdbcUniqueIndex REFERENCING_121_IDX_FK = new JdbcUniqueIndex("REFERENCING_121_IDX_FK", List.of(REFERENCING_121_COL_FK));

    private static final JdbcTable REFERENCING_TABLE_121 = new JdbcTable(
            null, null, "REFERENCING_TABLE_121",
            "", "TABLE",
            List.of(REFERENCING_121_COL_ID, REFERENCING_121_COL_FK),
            REFERENCING_121_PK,
            List.of(REFERENCING_121_PK, REFERENCING_121_IDX_FK));




    private static final JdbcColumn REFERENCED_COL_ID = new JdbcColumn(
            "ID",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    public static final JdbcUniqueIndex REFERENCED_PK = new JdbcUniqueIndex("REFERENCED_PK", List.of(REFERENCED_COL_ID));

    private static final JdbcTable REFERENCED_TABLE = new JdbcTable(
            null, null, "REFERENCED_TABLE",
            "", "TABLE",
            List.of(REFERENCED_COL_ID),
            REFERENCED_PK,
            List.of(REFERENCED_PK));




    private static final JdbcColumn RELATION_FKID1 = new JdbcColumn(
            "ID1",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    private static final JdbcColumn RELATION_FKID2 = new JdbcColumn(
            "ID2",
            "",
            "INT",
            JDBCType.INTEGER,
            false,
            10,
            0, 10, false);

    public static final JdbcUniqueIndex RELATION_PK = new JdbcUniqueIndex("RELATION_PK", List.of(RELATION_FKID1, RELATION_FKID2));
    public static final JdbcIndex RELATION_FK1 = new JdbcIndex("RELATION_FK1", List.of(RELATION_FKID1));
    public static final JdbcIndex RELATION_FK2 = new JdbcIndex("RELATION_FK2", List.of(RELATION_FKID2));

    private static final JdbcTable RELATION_TABLE = new JdbcTable(
            null, null, "RELATION_TABLE",
            "", "TABLE",
            List.of(RELATION_FKID1, RELATION_FKID2),
            RELATION_PK,
            List.of(RELATION_PK, RELATION_FK1, RELATION_FK2));



    private static final ForeignKey FK_ONETOONE    = new ForeignKey("ONETOONE"   , REFERENCING_121_IDX_FK, REFERENCED_PK, ForeignKey.Action.Cascade, ForeignKey.Action.Cascade, ForeignKey.ValidationDeferrability.NotDeferrable);
    private static final ForeignKey FK_MANYTOONE   = new ForeignKey("MANYTOONE"  , REFERENCING_IDX_FK, REFERENCED_PK, ForeignKey.Action.Cascade, ForeignKey.Action.Cascade, ForeignKey.ValidationDeferrability.NotDeferrable);
    private static final ForeignKey FK_MANYTOMANY1 = new ForeignKey("MANYTOMANY1", RELATION_FK1, REFERENCED_PK, ForeignKey.Action.Cascade, ForeignKey.Action.Cascade, ForeignKey.ValidationDeferrability.NotDeferrable);
    private static final ForeignKey FK_MANYTOMANY2 = new ForeignKey("MANYTOMANY2", RELATION_FK2, REFERENCING_PK, ForeignKey.Action.Cascade, ForeignKey.Action.Cascade, ForeignKey.ValidationDeferrability.NotDeferrable);

    private static final Map<JdbcTable, List<ForeignKey>> FOREIGNKEYS = Map.of(
            REFERENCING_TABLE_121, List.of(FK_ONETOONE),
            REFERENCING_TABLE    , List.of(FK_MANYTOONE),
            RELATION_TABLE       , List.of(FK_MANYTOMANY1, FK_MANYTOMANY2));


    @Test
    void testDetectOneToOne() {
        assertRelation(FK_ONETOONE, JdbcOneToOne.class, JdbcOneToOne.class, FK_ONETOONE.getName(), FK_ONETOONE.getName(), REFERENCING_121_IDX_FK, REFERENCED_PK);
    }

    @Test
    void testDetectManyToOne() {
        assertRelation(FK_MANYTOONE, JdbcManyToOne.class, JdbcOneToMany.class, FK_MANYTOONE.getName(), FK_MANYTOONE.getName(), REFERENCING_IDX_FK, REFERENCED_PK);
    }

    @Test
    void testDetectManyToMany() {
        assertRelation(FK_MANYTOMANY1, JdbcManyToMany.class, JdbcManyToMany.class, FK_MANYTOMANY1.getName(), FK_MANYTOMANY2.getName(), REFERENCED_PK, REFERENCING_PK);
    }

    private void assertRelation(ForeignKey foreignKey,
                                Class<? extends JdbcRelation> expectClass0, Class<? extends JdbcRelation> expectClass1,
                                String expectName0, String expectName1,
                                JdbcIndex expectReferencing, JdbcIndex expectReferenced) {
        JdbcRelation[] relations = JdbcRelation.of(FOREIGNKEYS, foreignKey);

        log.debug("Relation: {}", relations[0]);
        log.debug("Relation: {}", relations[1]);
        log.debug("Expect Classes: '{}' ; '{}'", expectClass0.getSimpleName(), expectClass1.getSimpleName());
        log.debug("Expect Names: '{}' ; '{}'", expectName0, expectName1);
        log.debug("Expect Referencing: {} :: {}", expectReferencing.getTable().getFullyQualifiedName(), expectReferencing);
        log.debug("Expect Referenced: {} :: {}", expectReferenced.getTable().getFullyQualifiedName(), expectReferenced);

        assertTrue(expectClass0.isInstance(relations[0]));
        assertEquals(expectName0      , relations[0].getName());
        assertEquals(expectReferencing, relations[0].getReferencing());
        assertEquals(expectReferenced , relations[0].getReferenced());

        assertTrue(expectClass1.isInstance(relations[1]));
        assertEquals(expectName1      , relations[1].getName());
        assertEquals(expectReferencing, relations[1].getReferenced());
        assertEquals(expectReferenced , relations[1].getReferencing());
    }
}
