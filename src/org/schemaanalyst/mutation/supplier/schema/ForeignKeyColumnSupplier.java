package org.schemaanalyst.mutation.supplier.schema;

import java.util.ArrayList;
import java.util.List;

import org.schemaanalyst.mutation.supplier.SolitaryComponentSupplier;
import org.schemaanalyst.sqlrepresentation.Column;
import org.schemaanalyst.sqlrepresentation.constraint.ForeignKeyConstraint;
import org.schemaanalyst.util.Pair;

public class ForeignKeyColumnSupplier extends
        SolitaryComponentSupplier<ForeignKeyConstraint, List<Pair<Column>>> {

    @Override
    public void putComponentBackInDuplicate(List<Pair<Column>> columnPairs) {
        List<Column> columns = new ArrayList<>();
        List<Column> referenceColumns = new ArrayList<>();

        for (Pair<Column> columnPair : columnPairs) {
            columns.add(columnPair.getFirst());
            referenceColumns.add(columnPair.getSecond());
        }

        currentDuplicate.setColumns(columns);
        currentDuplicate.setReferenceColumns(referenceColumns);
    }

    @Override
    protected List<Pair<Column>> getComponent(ForeignKeyConstraint foreignKeyConstraint) {
        List<Column> columns = foreignKeyConstraint.getColumns();
        List<Column> referenceColumns = foreignKeyConstraint.getReferenceColumns();

        List<Pair<Column>> pairedColumns = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            pairedColumns.add(new Pair<>(columns.get(i), referenceColumns.get(i)));
        }

        return pairedColumns;
    }
}