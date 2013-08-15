package parsedcasestudy;

import org.schemaanalyst.data.NumericValue;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.datatype.CharDataType;
import org.schemaanalyst.sqlrepresentation.datatype.IntDataType;
import org.schemaanalyst.sqlrepresentation.expression.BetweenExpression;
import org.schemaanalyst.sqlrepresentation.expression.ColumnExpression;
import org.schemaanalyst.sqlrepresentation.expression.ConstantExpression;

/*
 * NistWeather schema.
 * Java code originally generated: 2013/08/15 10:52:09
 *
 */

@SuppressWarnings("serial")
public class NistWeather extends Schema {

	public NistWeather() {
		super("NistWeather");

		Table tableStation = this.createTable("Station");
		tableStation.createColumn("ID", new IntDataType());
		tableStation.createColumn("CITY", new CharDataType(20));
		tableStation.createColumn("STATE", new CharDataType(2));
		tableStation.createColumn("LAT_N", new IntDataType());
		tableStation.createColumn("LONG_W", new IntDataType());
		tableStation.createPrimaryKeyConstraint(tableStation.getColumn("ID"));
		tableStation.createNotNullConstraint(tableStation.getColumn("LAT_N"));
		tableStation.createNotNullConstraint(tableStation.getColumn("LONG_W"));
		tableStation.createCheckConstraint(new BetweenExpression(new ColumnExpression(tableStation, tableStation.getColumn("LAT_N")), new ConstantExpression(new NumericValue(0)), new ConstantExpression(new NumericValue(90)), false, false));
		tableStation.createCheckConstraint(new BetweenExpression(new ColumnExpression(tableStation, tableStation.getColumn("LONG_W")), new ConstantExpression(new NumericValue(180)), new ConstantExpression(new NumericValue(-180)), false, true));

		Table tableStats = this.createTable("Stats");
		tableStats.createColumn("ID", new IntDataType());
		tableStats.createColumn("MONTH", new IntDataType());
		tableStats.createColumn("TEMP_F", new IntDataType());
		tableStats.createColumn("RAIN_I", new IntDataType());
		tableStats.createPrimaryKeyConstraint(tableStats.getColumn("ID"), tableStats.getColumn("MONTH"));
		tableStats.createForeignKeyConstraint(tableStats.getColumn("ID"), tableStation, tableStats.getColumn("ID"));
		tableStats.createNotNullConstraint(tableStats.getColumn("MONTH"));
		tableStats.createNotNullConstraint(tableStats.getColumn("TEMP_F"));
		tableStats.createNotNullConstraint(tableStats.getColumn("RAIN_I"));
		tableStats.createCheckConstraint(new BetweenExpression(new ColumnExpression(tableStats, tableStats.getColumn("MONTH")), new ConstantExpression(new NumericValue(1)), new ConstantExpression(new NumericValue(12)), false, false));
		tableStats.createCheckConstraint(new BetweenExpression(new ColumnExpression(tableStats, tableStats.getColumn("TEMP_F")), new ConstantExpression(new NumericValue(80)), new ConstantExpression(new NumericValue(150)), false, false));
		tableStats.createCheckConstraint(new BetweenExpression(new ColumnExpression(tableStats, tableStats.getColumn("RAIN_I")), new ConstantExpression(new NumericValue(0)), new ConstantExpression(new NumericValue(100)), false, false));
	}
}

