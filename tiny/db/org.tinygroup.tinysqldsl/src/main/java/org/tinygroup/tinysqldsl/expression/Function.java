/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinysqldsl.expression;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.expression.relational.ExpressionList;
import org.tinygroup.tinysqldsl.operator.SimpleBinaryOperator;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * A function as MAX,COUNT...
 */
public class Function extends SimpleBinaryOperator implements Expression,
		SelectItem {

	private String name;
	private ExpressionList parameters;
	private boolean allColumns = false;
	private boolean distinct = false;
	private boolean isEscaped = false;

	public Function() {
		super();
	}

	public Function(String name, boolean allColumns) {
		this.name = name;
		this.allColumns = allColumns;
	}

	public Function(String name, ExpressionList parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}

	public Function(String name, ExpressionList parameters, boolean allColumns,
			boolean distinct, boolean isEscaped) {
		super();
		this.name = name;
		this.parameters = parameters;
		this.allColumns = allColumns;
		this.distinct = distinct;
		this.isEscaped = isEscaped;
	}

	public static Function sum() {
		return new Function("sum", true);
	}

	public static Function count() {
		return new Function("count", true);
	}

	public static Function avg() {
		return new Function("avg", true);
	}

	public static Function max() {
		return new Function("max", true);
	}

	public static Function min() {
		return new Function("min", true);
	}

	public Function sum(String columnName) {
		return new Function("sum", ExpressionList.expressionList(new Column(
				columnName)));
	}

	public Function count(String columnName) {
		return new Function("count", ExpressionList.expressionList(new Column(
				columnName)));
	}

	public Function avg(String columnName) {
		return new Function("avg", ExpressionList.expressionList(new Column(
				columnName)));
	}

	public Function max(String columnName) {
		return new Function("max", ExpressionList.expressionList(new Column(
				columnName)));
	}

	public Function min(String columnName) {
		return new Function("min", ExpressionList.expressionList(new Column(
				columnName)));
	}

	/**
	 * The name of he function, i.e. "MAX"
	 * 
	 * @return the name of he function
	 */
	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	/**
	 * true if the parameter to the function is "*"
	 * 
	 * @return true if the parameter to the function is "*"
	 */
	public boolean isAllColumns() {
		return allColumns;
	}

	public void setAllColumns(boolean b) {
		allColumns = b;
	}

	/**
	 * true if the function is "distinct"
	 * 
	 * @return true if the function is "distinct"
	 */
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean b) {
		distinct = b;
	}

	/**
	 * The list of parameters of the function (if any, else null) If the
	 * parameter is "*", allColumns is set to true
	 * 
	 * @return the list of parameters of the function (if any, else null)
	 */
	public ExpressionList getParameters() {
		return parameters;
	}

	public void setParameters(ExpressionList list) {
		parameters = list;
	}

	/**
	 * Return true if it's in the form "{fn function_body() }"
	 * 
	 * @return true if it's java-escaped
	 */
	public boolean isEscaped() {
		return isEscaped;
	}

	public void setEscaped(boolean isEscaped) {
		this.isEscaped = isEscaped;
	}

	public String toString() {
		String params;

		if (parameters != null) {
			params = parameters.toString();
			if (isDistinct()) {
				params = params.replaceFirst("\\(", "(DISTINCT ");
			} else if (isAllColumns()) {
				params = params.replaceFirst("\\(", "(ALL ");
			}
		} else if (isAllColumns()) {
			params = "(*)";
		} else {
			params = "()";
		}

		String ans = name + "" + params + "";

		if (isEscaped) {
			ans = "{fn " + ans + "}";
		}

		return ans;
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		if (isEscaped()) {
			buffer.append("{fn ");
		}
		buffer.append(name);
		if (isAllColumns() && getParameters() == null) {
			buffer.append("(*)");
		} else if (getParameters() == null) {
			buffer.append("()");
		} else {
			boolean oldUseBracketsInExprList = builder
					.isUseBracketsInExprList();
			if (isDistinct()) {
				buffer.append("(DISTINCT ");
				builder.setUseBracketsInExprList(false);
			} else if (isAllColumns()) {
				buffer.append("(ALL ");
				builder.setUseBracketsInExprList(false);
			}
			getParameters().builder(builder);
			builder.setUseBracketsInExprList(oldUseBracketsInExprList);
			if (isDistinct() || isAllColumns()) {
				buffer.append(")");
			}
		}
		if (isEscaped()) {
			buffer.append("}");
		}
	}
}
