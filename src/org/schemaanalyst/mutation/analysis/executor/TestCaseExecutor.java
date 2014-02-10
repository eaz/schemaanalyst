package org.schemaanalyst.mutation.analysis.executor;

import java.util.List;
import org.schemaanalyst.coverage.testgeneration.TestCase;
import org.schemaanalyst.data.Data;
import org.schemaanalyst.data.Row;
import org.schemaanalyst.dbms.DBMS;
import org.schemaanalyst.dbms.DatabaseInteractor;
import org.schemaanalyst.mutation.analysis.executor.exceptions.CreateStatementException;
import org.schemaanalyst.mutation.analysis.executor.exceptions.DropStatementException;
import org.schemaanalyst.mutation.analysis.executor.exceptions.InsertStatementException;
import org.schemaanalyst.mutation.analysis.executor.exceptions.TestCaseException;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlwriter.SQLWriter;

public class TestCaseExecutor {

    final private Schema schema;
    final private List<Table> tables;
    final private DatabaseInteractor databaseInteractor;
    final private SQLWriter sqlWriter;

    public TestCaseExecutor(Schema schema, DBMS dbms, DatabaseInteractor databaseInteractor) {
        this.schema = schema;
        tables = schema.getTablesInOrder();
        sqlWriter = dbms.getSQLWriter();
        this.databaseInteractor = databaseInteractor;
    }

    private void executeCreates() throws CreateStatementException {
        List<String> createStatements = sqlWriter.writeCreateTableStatements(schema);
        for (String statement : createStatements) {
            Integer result = databaseInteractor.executeUpdate(statement);
            if (result < 0) {
                throw new CreateStatementException("Failed, result was: " + result, statement);
            }
        }
    }

    private void executeDrops() throws DropStatementException {
        List<String> dropTableStatements = sqlWriter.writeDropTableStatements(schema, true);
        for (String statement : dropTableStatements) {
            Integer result = databaseInteractor.executeUpdate(statement);
            if (result < 0) {
                throw new DropStatementException("Failed, result was: " + result, statement);
            }
        }
    }
    
    private void executeInserts(Data data) {
        List<Table> stateTables = data.getTables();
        for (Table table : tables) {
            if (stateTables.contains(table)) {
                List<Row> rows = data.getRows(table);
                for (Row row : rows) {
                    String statement = sqlWriter.writeInsertStatement(row);
                    Integer result = databaseInteractor.executeUpdate(statement);
                    if (result != 1) {
                        throw new InsertStatementException("Failed, result was: " + result, statement);
                    }
                }
            }
        }
    }

    public TestCaseResult executeTestCase(TestCase testCase) {
        TestCaseResult result;
        try {
            executeDrops();
            executeCreates();
            executeInserts(testCase.getState());
            executeInserts(testCase.getData());
            executeDrops();
            result = TestCaseResult.SuccessfulTestCaseResult;
        } catch (TestCaseException ex) {
            result = new TestCaseResult(ex);
        }
        return result;
    }
}
