package parsedcasestudy;

import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.sqlrepresentation.Table;
import org.schemaanalyst.sqlrepresentation.datatype.IntDataType;
import org.schemaanalyst.sqlrepresentation.datatype.VarCharDataType;

/*
 * BankAccount schema.
 * Java code originally generated: 2013/08/15 10:51:37
 *
 */

@SuppressWarnings("serial")
public class BankAccount extends Schema {

	public BankAccount() {
		super("BankAccount");

		Table tableUserinfo = this.createTable("UserInfo");
		tableUserinfo.createColumn("card_number", new IntDataType());
		tableUserinfo.createColumn("pin_number", new IntDataType());
		tableUserinfo.createColumn("user_name", new VarCharDataType(50));
		tableUserinfo.createColumn("acct_lock", new IntDataType());
		tableUserinfo.createPrimaryKeyConstraint(tableUserinfo.getColumn("card_number"));
		tableUserinfo.createNotNullConstraint(tableUserinfo.getColumn("pin_number"));
		tableUserinfo.createNotNullConstraint(tableUserinfo.getColumn("user_name"));

		Table tableAccount = this.createTable("Account");
		tableAccount.createColumn("id", new IntDataType());
		tableAccount.createColumn("account_name", new VarCharDataType(50));
		tableAccount.createColumn("user_name", new VarCharDataType(50));
		tableAccount.createColumn("balance", new IntDataType());
		tableAccount.createColumn("card_number", new IntDataType());
		tableAccount.createPrimaryKeyConstraint(tableAccount.getColumn("id"));
		tableAccount.createForeignKeyConstraint(tableAccount.getColumn("card_number"), tableUserinfo, tableAccount.getColumn("card_number"));
		tableAccount.createNotNullConstraint(tableAccount.getColumn("account_name"));
		tableAccount.createNotNullConstraint(tableAccount.getColumn("user_name"));
		tableAccount.createNotNullConstraint(tableAccount.getColumn("card_number"));
	}
}

