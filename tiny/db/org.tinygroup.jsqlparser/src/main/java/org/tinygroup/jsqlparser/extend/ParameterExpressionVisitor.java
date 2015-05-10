package org.tinygroup.jsqlparser.extend;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitorAdapter;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.expression.operators.relational.MultiExpressionList;
import org.tinygroup.jsqlparser.statement.select.SelectVisitor;
import org.tinygroup.jsqlparser.statement.select.SubSelect;

/**
 * 
 * @author renhui
 * 
 */
public class ParameterExpressionVisitor extends ExpressionVisitorAdapter {
	private Map<String, Integer> positionMap;
	private AtomicInteger paramLength;
	private SelectVisitor selectVisitor;

	public ParameterExpressionVisitor(Map<String, Integer> positionMap,
			AtomicInteger paramLength, SelectVisitor selectVisitor) {
		super();
		this.positionMap = positionMap;
		this.paramLength = paramLength;
		this.selectVisitor = selectVisitor;
	}

	@Override
	public void visit(JdbcParameter parameter) {
		paramLength.incrementAndGet();
	}

	@Override
	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(selectVisitor);
	}

	@Override
	public void visit(ExpressionList expressionList) {
		List<Expression> expressions = expressionList.getExpressions();
		if (expressions != null) {
			for (Expression expression : expressions) {
				expression.accept(this);
			}
		}
	}

	@Override
	public void visit(MultiExpressionList multiExprList) {
		List<ExpressionList> expressionLists = multiExprList.getExprList();
		if (expressionLists != null) {
			for (ExpressionList expressionList : expressionLists) {
				expressionList.accept(this);
			}
		}
	}
}
