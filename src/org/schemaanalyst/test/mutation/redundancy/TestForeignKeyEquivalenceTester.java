/*
 */
package org.schemaanalyst.test.mutation.redundancy;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.schemaanalyst.mutation.redundancy.ForeignKeyEquivalenceTester;
import org.schemaanalyst.sqlrepresentation.Column;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.constraint.ForeignKeyConstraint;
import org.schemaanalyst.sqlrepresentation.datatype.IntDataType;

/**
 *
 * @author Chris J. Wright
 */
public class TestForeignKeyEquivalenceTester {

    @Test
    public void testSameInstance() {
        ForeignKeyEquivalenceTester tester = new ForeignKeyEquivalenceTester();
        Table t1 = new Table("t1");
        t1.addColumn(new Column("a", new IntDataType()));
        Table t2 = new Table("t2");
        t2.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fk1 = new ForeignKeyConstraint(t1, t1.getColumn("a"), t2, t2.getColumn("a"));
        assertTrue("A foreign key should be equivalent to itself",
                tester.areEquivalent(fk1, fk1));
    }

    @Test
    public void testDifferentInstance() {
        ForeignKeyEquivalenceTester tester = new ForeignKeyEquivalenceTester();
        Table t1 = new Table("t1");
        t1.addColumn(new Column("a", new IntDataType()));
        Table t2 = new Table("t2");
        t2.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fk1 = new ForeignKeyConstraint(t1, t1.getColumn("a"), t2, t2.getColumn("a"));
        ForeignKeyConstraint fk2 = new ForeignKeyConstraint(t1, t1.getColumn("a"), t2, t2.getColumn("a"));
        assertTrue("Two instances of the same foreign key should be equivalent",
                tester.areEquivalent(fk1, fk2));
    }

    @Test
    public void testDifferentTables() {
        ForeignKeyEquivalenceTester tester = new ForeignKeyEquivalenceTester();
        Table t1A = new Table("t1");
        t1A.addColumn(new Column("a", new IntDataType()));
        Table t2A = new Table("t2");
        t2A.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fkA = new ForeignKeyConstraint(t1A, t1A.getColumn("a"), t2A, t2A.getColumn("a"));
        Table t1B = new Table("t1");
        t1B.addColumn(new Column("a", new IntDataType()));
        Table t2B = new Table("t2");
        t2B.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fkB = new ForeignKeyConstraint(t1B, t1B.getColumn("a"), t2B, t2B.getColumn("a"));
        assertTrue("Two identical foreign keys on two different tables should "
                + "be equivalent", tester.areEquivalent(fkA, fkB));
    }

    @Test
    public void testNonEquivalentTables() {
        ForeignKeyEquivalenceTester tester = new ForeignKeyEquivalenceTester();
        Table t1A = new Table("t1");
        t1A.addColumn(new Column("a", new IntDataType()));
        t1A.addColumn(new Column("b", new IntDataType()));
        Table t2A = new Table("t2");
        t2A.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fkA = new ForeignKeyConstraint(t1A, t1A.getColumn("a"), t2A, t2A.getColumn("a"));
        Table t1B = new Table("t1");
        t1B.addColumn(new Column("a", new IntDataType()));
        Table t2B = new Table("t2");
        t2B.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fkB = new ForeignKeyConstraint(t1B, t1B.getColumn("a"), t2B, t2B.getColumn("a"));
        assertTrue("Two identical foreign keys on two different tables should "
                + "be equivalent", tester.areEquivalent(fkA, fkB));
    }

    @Test
    public void testDifferentColumnCount() {
        ForeignKeyEquivalenceTester tester = new ForeignKeyEquivalenceTester();
        Table t1A = new Table("t1");
        t1A.addColumn(new Column("a", new IntDataType()));
        t1A.addColumn(new Column("b", new IntDataType()));
        Table t2A = new Table("t2");
        t2A.addColumn(new Column("a", new IntDataType()));
        t2A.addColumn(new Column("b", new IntDataType()));
        ForeignKeyConstraint fkA = new ForeignKeyConstraint(
                t1A, Arrays.asList(new Column[]{t1A.getColumn("a"), t1A.getColumn("b")}),
                t2A, Arrays.asList(new Column[]{t2A.getColumn("a"), t2A.getColumn("b")}));
        Table t1B = new Table("t1");
        t1B.addColumn(new Column("a", new IntDataType()));
        t1B.addColumn(new Column("b", new IntDataType()));
        Table t2B = new Table("t2");
        t2B.addColumn(new Column("a", new IntDataType()));
        t2B.addColumn(new Column("b", new IntDataType()));
        ForeignKeyConstraint fkB = new ForeignKeyConstraint(t1B, t1B.getColumn("a"), t2B, t2B.getColumn("a"));
        assertFalse("Two foreign keys with different numbers of columns should "
                + "not be equivalent", tester.areEquivalent(fkA, fkB));
    }
    
    @Test
    public void testDifferentReferenceTable() {
        ForeignKeyEquivalenceTester tester = new ForeignKeyEquivalenceTester();
        Table t1A = new Table("t1");
        t1A.addColumn(new Column("a", new IntDataType()));
        Table t2A = new Table("t2");
        t2A.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fkA = new ForeignKeyConstraint(t1A, t1A.getColumn("a"), t2A, t2A.getColumn("a"));
        Table t1B = new Table("t1");
        t1B.addColumn(new Column("a", new IntDataType()));
        Table t2B = new Table("t3");
        t2B.addColumn(new Column("a", new IntDataType()));
        ForeignKeyConstraint fkB = new ForeignKeyConstraint(t1B, t1B.getColumn("a"), t2B, t2B.getColumn("a"));
        assertFalse("Two foreign keys with different reference tables should "
                + "not be equivalent", tester.areEquivalent(fkA, fkB));
    }
}
