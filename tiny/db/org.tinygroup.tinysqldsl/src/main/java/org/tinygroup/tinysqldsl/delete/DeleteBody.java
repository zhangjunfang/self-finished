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
package org.tinygroup.tinysqldsl.delete;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.StatementBody;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.expression.Expression;

/**
 * delete语句的对象结构
 * 
 * @author renhui
 * 
 */
public class DeleteBody implements StatementBody {
	/**
	 * 删除的表对象
	 */
	private Table table;
	/**
	 * 删除条件
	 */
	private Expression where;

	public Table getTable() {
		return table;
	}

	public Expression getWhere() {
		return where;
	}

	public void setTable(Table name) {
		table = name;
	}

	public void setWhere(Expression expression) {
		where = expression;
	}

	public String toString() {
		return "DELETE FROM " + table
				+ ((where != null) ? " WHERE " + where : "");
	}


	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append("DELETE FROM ")
				.append(getTable().getFullyQualifiedName());
		if (getWhere() != null) {
			buffer.append(" WHERE ");
			getWhere().builder(builder);
		}
	}
}
