package deprecated.mutation.mutators;

import java.util.List;

import org.schemaanalyst.sqlrepresentation.Column;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.constraint.PrimaryKeyConstraint;

public class NotNullConstraintMutator extends Mutator {

    @Override
    public void produceMutants(Table table, List<Schema> mutants) {
        PrimaryKeyConstraint primaryKey = table.getPrimaryKeyConstraint();

        for (Column column : table.getColumns()) {

            // do not create mutants on primary key columns -- these are always not null
            if (primaryKey != null && primaryKey.involvesColumn(column)) {
                continue;
            }

            mutants.add(makeMutant(table, column, !column.isNotNull()));
        }
    }

    protected Schema makeMutant(Table table, Column column, boolean makeNotNull) {
        Schema mutant = table.getSchema().duplicate();
        mutant.addComment("Mutant with NOT NULL property reversed on column \"" + column + "\" in table \"" + table + "\"");
        mutant.addComment("(Not Null, 5)");
        mutant.addComment("table=" + table);

        Table mutantTable = mutant.getTable(table.getName());

        Column mutantColumn = mutantTable.getColumn(column.getName());

        if (mutantTable.isNotNull(mutantColumn)) {
            mutantTable.removeNotNullConstraint(mutantColumn);
        } else {
            mutantTable.addNotNullConstraint(mutantColumn);
        }

        return mutant;
    }
}