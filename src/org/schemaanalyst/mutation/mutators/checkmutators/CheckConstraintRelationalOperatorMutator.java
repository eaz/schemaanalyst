/*
 */
package org.schemaanalyst.mutation.mutators.checkmutators;

import java.util.ArrayList;
import java.util.List;
import org.schemaanalyst.logic.RelationalOperator;
import org.schemaanalyst.mutation.mutators.Mutator;
import org.schemaanalyst.representation.CheckConstraint;
import org.schemaanalyst.representation.Schema;
import org.schemaanalyst.representation.Table;
import org.schemaanalyst.representation.checkcondition.BetweenCheckCondition;
import org.schemaanalyst.representation.checkcondition.CheckConditionVisitor;
import org.schemaanalyst.representation.checkcondition.InCheckCondition;
import org.schemaanalyst.representation.checkcondition.RelationalCheckCondition;
import org.schemaanalyst.representation.datatype.IntDataType;

/**
 * Mutates the relational operators in relational check constraints. Makes use 
 * of an internal private visitor class to 'visit' each constraint, to easily 
 * determine the type at runtime.
 *
 * @author chris
 */
public class CheckConstraintRelationalOperatorMutator extends Mutator {
    
    @Override
    public void produceMutants(Table table, List<Schema> mutants) {
        for (CheckConstraint checkConstraint: table.getCheckConstraints()) {
            Visitor v = new Visitor(table, checkConstraint);
            mutants.addAll(v.createMutants());
        }
    }

    /**
     * A visitor implementation for mutating relational check predicates
     */
    private class Visitor implements CheckConditionVisitor {

        Table table;
        List<Schema> mutants;
        CheckConstraint constraint;

        /**
         * Constructor.
         * 
         * @param table The table to mutate
         * @param constraint The constraint to mutate
         */
        public Visitor(Table table, CheckConstraint constraint) {
            this.table = table;
            this.constraint = constraint;
        }

        /**
         * Create the mutants for the given constraint.
         * 
         * @return The mutants created
         */
        public List<Schema> createMutants() {
            mutants = new ArrayList<>();
            constraint.getCheckCondition().accept(this);
            return mutants;
        }

        /**
         * Do nothing for BetweenCheckPredicate.
         * 
         * @param predicate The predicate of the constraint
         */
        @Override
        public void visit(BetweenCheckCondition predicate) {
            // Do nothing
        }

        /**
         * Do nothing for InCheckPredicate.
         * 
         * @param predicate The predicate of the constraint
         */
        @Override
        public void visit(InCheckCondition predicate) {
            // Do nothing
        }

        /**
         * Mutate a given RelationalCheckPredicate. Produces a mutant for each 
         * alternative relational operator, with the original LHS and RHS.
         * 
         * @param predicate The predicate of the constraint
         */
        @Override
        public void visit(RelationalCheckCondition predicate) {
            RelationalOperator op = predicate.getOperator();
            for (RelationalOperator replacement : RelationalOperator.values()) {
                if (op != replacement) {
                    Schema mutantSchema = table.getSchema().duplicate();
                    Table mutantTable = mutantSchema.getTable(table.getName());
                    mutantTable.removeCheckConstraint(constraint);
                    mutantTable.addCheckConstraint(new RelationalCheckCondition(
                            predicate.getLHS(),
                            replacement,
                            predicate.getRHS()));
                    mutants.add(mutantSchema);
                }
            }
        }
    }
}