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
package org.tinygroup.tinysqldsl.operator;

import org.tinygroup.tinysqldsl.base.Condition;
import org.tinygroup.tinysqldsl.expression.BinaryExpression;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.JdbcParameter;
import org.tinygroup.tinysqldsl.expression.relational.*;
import org.tinygroup.tinysqldsl.transform.ExpressionTransform;
import org.tinygroup.tinysqldsl.transform.JdbcParameterExpressionTransform;

/**
 * 二元操作接口的简单实现
 * 
 * @author renhui
 */
public abstract class SimpleBinaryOperator implements BinaryOperator,
		Expression, ExpressionTransform {

	private ExpressionTransform transform = new JdbcParameterExpressionTransform();

	public ExpressionTransform getTransform() {
		return transform;
	}

	public void setTransform(ExpressionTransform transform) {
		this.transform = transform;
	}

	public Condition eq(Object value) {
		return equal(value);
	}

	public Condition equal(Object value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new EqualsTo(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value;
			}
		});
	}

	public Condition toCondition(Object value,
			ExpressionInstanceCallBack callBack) {
		if (value == null||value.toString().trim().equals("")) {
			return null;
		}
		Object newValue = callBack.format(value);
		Expression rightExpression = transform(newValue);
		BinaryExpression expression = callBack.instance(this, rightExpression);
		Condition condition = null;
		if (isParameterExpression(rightExpression)) {
			condition = new Condition(expression, newValue);
		} else {
			condition = new Condition(expression);
		}
		return condition;
	}

	public Condition neq(Object value) {
		return notEqual(value);
	}

	public Condition notEqual(Object value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new NotEqualsTo(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value;
			}
		});
	}

	public Condition gt(Object value) {
		return greaterThan(value);
	}

	public Condition greaterThan(Object value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new GreaterThan(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value;
			}
		});
	}

	public Condition gte(Object value) {
		return greaterThanEqual(value);
	}

	public Condition greaterThanEqual(Object value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new GreaterThanEquals(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value;
			}
		});
	}

	public Condition lt(Object value) {
		return lessThan(value);
	}

	public Condition lessThan(Object value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new MinorThan(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value;
			}
		});
	}

	public Condition lte(Object value) {
		return lessThanEqual(value);
	}

	public Condition lessThanEqual(Object value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new MinorThanEquals(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value;
			}
		});
	}

	public Condition isNull() {
		IsNullExpression isNull = new IsNullExpression(this);
		Condition condition = new Condition(isNull);
		return condition;
	}
	
	public Condition isNotNull() {
		IsNullExpression isNotNull = new IsNullExpression(this, true);
		Condition condition = new Condition(isNotNull);
		return condition;
	}
	
	public Condition isEmpty() {
		IsEmptyExpression isEmpty=new IsEmptyExpression(this);
		return new Condition(isEmpty);
	}

	public Condition isNotEmpty() {
		IsEmptyExpression isEmpty=new IsEmptyExpression(this,true);
		return new Condition(isEmpty);
	}


	public Condition like(String value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new LikeExpression(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return "%" + value + "%";
			}
		});
	}

	public Condition notLike(String value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new LikeExpression(leExpression, rightExpression, true);
			}

			public Object format(Object value) {
				return "%" + value + "%";
			}
		});
	}

	public Condition notLeftLike(String value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new LikeExpression(leExpression, rightExpression, true);
			}

			public Object format(Object value) {
				return value + "%";
			}
		});
	}

	public Condition notRightLike(String value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new LikeExpression(leExpression, rightExpression, true);
			}

			public Object format(Object value) {
				return "%" + value;
			}
		});
	}

	public Condition leftLike(String value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new LikeExpression(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return value + "%";
			}
		});
	}

	public Condition rightLike(String value) {
		return toCondition(value, new ExpressionInstanceCallBack() {
			public BinaryExpression instance(Expression leExpression,
					Expression rightExpression) {
				return new LikeExpression(leExpression, rightExpression);
			}

			public Object format(Object value) {
				return "%" + value;
			}
		});
	}

	public Condition between(Object begin, Object end) {
		Between between = new Between(this, new JdbcParameter(),
				new JdbcParameter());
		return new Condition(between, begin, end);
	}
	
	
	public Condition notBetween(Object begin, Object end) {
		Between between = new Between(this, new JdbcParameter(),
				new JdbcParameter(),true);
		return new Condition(between, begin, end);
	}

	public Condition in(Object... values) {
		ExpressionList rightItemsList=new ExpressionList();
		for (int i = 0; i < values.length; i++) {
			rightItemsList.addExpression(new JdbcParameter());
		}
		InExpression inExpression=new InExpression(this, rightItemsList);
		return new Condition(inExpression, values);
	}

	public Condition notIn(Object... values) {
		ExpressionList rightItemsList=new ExpressionList();
		for (int i = 0; i < values.length; i++) {
			rightItemsList.addExpression(new JdbcParameter());
		}
		InExpression inExpression=new InExpression(this, rightItemsList,true);
		return new Condition(inExpression, values);
	}

	public Expression transform(Object value) {
		return transform.transform(value);
	}

	public boolean isParameterExpression(Expression expression) {
		return transform.isParameterExpression(expression);
	}
}
