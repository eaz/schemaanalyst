package org.schemaanalyst.test.testgeneration.coveragecriterion.requirements;

import org.junit.Before;
import org.junit.Test;
import org.schemaanalyst.logic.predicate.Predicate;
import org.schemaanalyst.logic.predicate.clause.NullClause;
import org.schemaanalyst.sqlrepresentation.Column;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.constraint.NotNullConstraint;
import org.schemaanalyst.test.testutil.mock.SimpleSchema;
import org.schemaanalyst.testgeneration.coveragecriterion_old.requirements.NotNullConstraintRequirementsGenerator;
import org.schemaanalyst.testgeneration.coveragecriterion_old.requirements.Requirements;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by phil on 24/02/2014.
 */
public class TestNotNullConstraintRequirementsGenerator {

    private Schema schema;
    private Table tab1;
    private Column tab1Col1;

    @Before
    public void loadSchema() {
        schema = new SimpleSchema();
        tab1 = schema.getTable("Tab1");
        tab1Col1 = tab1.getColumn("Tab1Col1");
    }

    @Test
    public void testGeneratedRequirements() {
        NotNullConstraint nn =
                schema.createNotNullConstraint(tab1, tab1Col1);

        NotNullConstraintRequirementsGenerator reqGen
                = new NotNullConstraintRequirementsGenerator(schema, nn);

        Requirements requirements = reqGen.generateRequirements();
        assertEquals("Number of requirements should be equal to 2", 2, requirements.size());

        NullClause colNull = new NullClause(tab1, tab1Col1, true);
        NullClause colNotNull = new NullClause(tab1, tab1Col1, false);

        List<Predicate> predicates = requirements.getPredicates();

        Predicate predicate1 = predicates.get(0);
        assertTrue(predicate1.hasClause(colNotNull));
        assertFalse(predicate1.hasClause(colNull));

        Predicate predicate2 = predicates.get(1);
        assertFalse(predicate2.hasClause(colNotNull));
        assertTrue(predicate2.hasClause(colNull));
    }

}
