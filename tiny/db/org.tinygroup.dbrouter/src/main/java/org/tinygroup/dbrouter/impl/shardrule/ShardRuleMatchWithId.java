/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.dbrouter.impl.shardrule;

import java.util.List;

import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
import org.tinygroup.jsqlparser.expression.operators.relational.EqualsTo;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.FromItem;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;
import org.tinygroup.jsqlparser.statement.update.Update;

/**
 * 
 * @author renhui
 * 
 */
public class ShardRuleMatchWithId {

	private long remainder;
	private String tableName;
	private String primaryKeyFieldName;
	private Partition partition;
	private Object[] preparedParams;

	public ShardRuleMatchWithId(long remainder, String tableName,
			String primaryKeyFieldName, Partition partition, Object[] preparedParams) {
		super();
		this.remainder = remainder;
		this.tableName = tableName;
		this.primaryKeyFieldName = primaryKeyFieldName;
		this.partition = partition;
		this.preparedParams = preparedParams;
	}

	public boolean insertMatch(Statement statement) {
		Insert insert = (Insert) statement;
		int paramIndex=0;
		if (tableName.equalsIgnoreCase(insert.getTable().getName())) {
			ItemsList itemsList = insert.getItemsList();
			if (itemsList instanceof ExpressionList) {
				List<Expression> expressions = ((ExpressionList) itemsList)
						.getExpressions();
				int shardSize = partition.getShards().size();
				for (int i = 0; i < insert.getColumns().size(); i++) {
					Column column = insert.getColumns().get(i);
					Expression expression = expressions.get(i);
					if (column.getColumnName().equalsIgnoreCase(
							primaryKeyFieldName)) {
						if (expression instanceof LongValue) {
							LongValue longValue = (LongValue) expression;
							if (longValue.getValue() % shardSize == remainder) {
								return true;
							}
						} else if (expression instanceof JdbcParameter) {
							Long value = (Long) preparedParams[paramIndex];
							if ((value % partition.getShards().size()) == remainder) {
								return true;
							}
						}
					}
					if (expression instanceof JdbcParameter) {
						paramIndex++;
					}
				}
			}
		}
		return false;
	}
	
	public boolean updateMatch(Statement statement){
		Update update = (Update) statement;
		List<Expression> expressions = update.getExpressions();
		int paramIndex=0;
		for (Expression expression : expressions) {
			if (expression instanceof JdbcParameter) {
				paramIndex++;
			}
		}
		List<Table> tables = update.getTables();
		if(tables!=null){
		  for(Table table:tables){
			 if(tableName.equalsIgnoreCase(table.getName())) {
				return getWhereExpression(paramIndex,update.getWhere(), partition,preparedParams);
			} 
		  }
		}
		return false;
	}
	
	public boolean deleteMatch(Statement statement){
		Delete delete = (Delete) statement;
		if (tableName.equalsIgnoreCase(delete.getTable().getName())) {
			return getWhereExpression(0,delete.getWhere(), partition,preparedParams);
		}
		return false;
	}
	
	public boolean selectMatch(Statement statement){
		Select select = (Select) statement;
		SelectBody body = select.getSelectBody();
		if (body instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) body;
			FromItem fromItem = plainSelect.getFromItem();
			if (fromItem instanceof Table) {
				Table table = (Table) fromItem;
				if (tableName.equalsIgnoreCase(table.getName())) {
					return getWhereExpression(0,plainSelect.getWhere(),
							partition,preparedParams);
				}
			}
		}
		return false;
	}
	
	private boolean getWhereExpression(int paramIndex,Expression where, Partition partition,Object... preparedParams) {
		if (where == null) {
			return true;
		}
		return getEqualsToExpression(paramIndex,where, partition,preparedParams);

	}

	private boolean getEqualsToExpression(int paramIndex,Expression where, Partition partition,Object... preparedParams) {
		if (where instanceof EqualsTo) {
			EqualsTo equalsTo = (EqualsTo) where;
			Expression leftExpression = equalsTo.getLeftExpression();
			Expression rightExpression = equalsTo.getRightExpression();
			if (leftExpression instanceof Column) {
				Column column = (Column) leftExpression;
				if (column.getColumnName().equalsIgnoreCase(primaryKeyFieldName)) {
					if(rightExpression instanceof LongValue){
						  LongValue longValue=(LongValue)rightExpression;
						  if(longValue.getValue()% partition.getShards()
									.size() == remainder){
							  return true;
						  }
					  }else if(rightExpression instanceof JdbcParameter){
						   Long value = (Long) preparedParams[paramIndex];
							if ((value % partition.getShards().size()) == remainder) {
								return true;
							}
					  }
				}
			}

		}
		return false;
	}

}
