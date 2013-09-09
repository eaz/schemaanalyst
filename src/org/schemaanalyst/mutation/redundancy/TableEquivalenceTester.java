/*
 */
package org.schemaanalyst.mutation.redundancy;

import org.schemaanalyst.sqlrepresentation.Column;
import org.schemaanalyst.sqlrepresentation.Table;

/**
 *
 * @author Chris J. Wright
 */
public class TableEquivalenceTester extends EquivalenceTester<Table> {
    
    EquivalenceTester<Column> columnEquivalenceTester;
    
    public TableEquivalenceTester(EquivalenceTester<Column> columnEquivalenceTester) {
        this.columnEquivalenceTester = columnEquivalenceTester;
    }

    @Override
    public boolean areEquivalent(Table a, Table b) {
        if (super.areEquivalent(a, b)) {
            return true;
        } else if (!a.getIdentifier().equals(b.getIdentifier())) {
            return false;
        } else if (a.getColumns().size() != b.getColumns().size()) {
            return false;
        } else {
            for (int i = 0; i < a.getColumns().size(); i++) {
                Column aColumn = a.getColumns().get(i);
                Column bColumn = b.getColumns().get(i);
                if (!columnEquivalenceTester.areEquivalent(aColumn, bColumn)) {
                    return false;
                }
            }
            return true;
        }
    }
    
}
