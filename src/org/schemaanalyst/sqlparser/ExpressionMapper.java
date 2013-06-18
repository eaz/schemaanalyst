package org.schemaanalyst.sqlparser;

import java.util.ArrayList;
import java.util.List;

import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.nodes.TConstant;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TExpressionList;

import org.schemaanalyst.data.NumericValue;
import org.schemaanalyst.data.StringValue;
import org.schemaanalyst.logic.RelationalOperator;
import org.schemaanalyst.representation.Column;
import org.schemaanalyst.representation.Table;
import org.schemaanalyst.representation.expression.AndExpression;
import org.schemaanalyst.representation.expression.BetweenExpression;
import org.schemaanalyst.representation.expression.Expression;
import org.schemaanalyst.representation.expression.InExpression;
import org.schemaanalyst.representation.expression.ListExpression;
import org.schemaanalyst.representation.expression.NullExpression;
import org.schemaanalyst.representation.expression.OrExpression;
import org.schemaanalyst.representation.expression.ParenthesisedExpression;
import org.schemaanalyst.representation.expression.RelationalExpression;

public class ExpressionMapper {

	// REFER TO the JavaDocs for TExpression
	// http://sqlparser.com/kb/javadoc/gudusoft/gsqlparser/nodes/TExpression.html
	
	Table currentTable;
	
	static Expression map(Table currentTable, TExpression node) {
		return (new ExpressionMapper(currentTable)).getExpression(node);
	}	
	
	ExpressionMapper(Table currentTable) {
		this.currentTable = currentTable;
	}
	
	Expression getExpression(TExpression node) {
		EExpressionType expressionType = node.getExpressionType();

		// *** OBJECT NAME/CONSTANT/SOURCE TOKEN/FUNCTION CALL ***
		if (expressionType == EExpressionType.simple_object_name_t) {			
			String columnName = QuoteStripper.stripQuotes(node.getObjectOperand());
			Column column = currentTable.getColumn(columnName);
			if (column == null) {
				throw new SQLParseException("Unknown column \"" + column + "\"");
			}
			return column;
		}
		
		// *** UNARY ***
		if (expressionType == EExpressionType.unary_minus_t && node.getRightOperand().getExpressionType() == EExpressionType.simple_constant_t) {
			// assume negative number
			String value = node.toString();
			return new NumericValue(value);
		}
		
		if (expressionType == EExpressionType.simple_constant_t) {
			TConstant constant = node.getConstantOperand();
			String valueString = constant.toString();
		
			if (QuoteStripper.isQuoted(valueString)) {
				return new StringValue(QuoteStripper.stripQuotes(valueString));
			} else {
				return new NumericValue(valueString);
			}
		}
		
		// *** LOGICAL ***		
		if (expressionType == EExpressionType.logical_and_t) {
			return new AndExpression(getExpression(node.getLeftOperand()), 
									 getExpression(node.getRightOperand()));
		}
		
		if (expressionType == EExpressionType.logical_or_t) {
			return new OrExpression(getExpression(node.getLeftOperand()), 
									getExpression(node.getRightOperand()));
		}		
		
		// *** EXPRESSION WITH PARENTHESIS ***		
		if (expressionType == EExpressionType.parenthesis_t) {
			TExpression subnode = node.getLeftOperand();
			return new ParenthesisedExpression(getExpression(subnode));
		}
		
		// *** LIST EXPRESSION ***		
		if (expressionType == EExpressionType.list_t) {
			TExpressionList expressionList = node.getExprList();
			
			List<Expression> subexpressions = new ArrayList<>();
			for (int i=0; i < expressionList.size(); i++) {
				TExpression subNode = expressionList.getExpression(i);
				subexpressions.add(getExpression(subNode));
			}
			
			return new ListExpression(subexpressions.toArray(new Expression[0]));
		}	
		
		// *** COMPARISON *** 		
		if (expressionType == EExpressionType.simple_comparison_t) {
			TExpression lhsNode = node.getLeftOperand();
			TExpression rhsNode = node.getRightOperand();
			String operatorString = node.getOperatorToken().toString();
			
			RelationalOperator op = RelationalOperator.getRelationalOperator(operatorString);
			return new RelationalExpression(getExpression(lhsNode), op, getExpression(rhsNode));
		}
		
		// *** IN ***
		if (expressionType == EExpressionType.in_t) {
			boolean notIn = node.getNotToken() != null;
			return new InExpression(getExpression(node.getLeftOperand()), 
									getExpression(node.getRightOperand()), 
									notIn);
		}
		
		// *** NULL *** 				
		if (expressionType == EExpressionType.null_t) {
			boolean notNull = node.getOperatorToken().toString().equals("NOTNULL") || node.getNotToken() != null;	
			return new NullExpression(getExpression(node.getLeftOperand()), notNull);			
		}
		
		// *** BETWEEN ***
		if (expressionType == EExpressionType.between_t) {
			boolean notBetween = node.getNotToken() != null;
			return new BetweenExpression(getExpression(node.getBetweenOperand()), 
										 getExpression(node.getLeftOperand()),
										 getExpression(node.getRightOperand()),
										 notBetween);						
		}
		
		throw new ExpressionMappingException(node);
	}
}