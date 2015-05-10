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
package org.tinygroup.tinysqldsl.selectitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Column;

/**
 * A DISTINCT [(expression, ...)] clause
 */
public class Distinct implements SelectItem {

	private SelectItem selectItem;

	public Distinct() {
		this.selectItem = new AllColumns();
	}

	public Distinct(String columnName) {
		this.selectItem = new Column(columnName);
	}

	public Distinct(SelectItem selectItem) {
		this.selectItem = selectItem;
	}

	public static Distinct distinct(SelectItem selectItem) {
		return new Distinct(selectItem);
	}

	public static Distinct distinct(String columnName) {
		return new Distinct(columnName);
	}

	public static Distinct distinct() {
		return new Distinct();
	}

	public SelectItem getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(SelectItem selectItem) {
		this.selectItem = selectItem;
	}

	public String toString() {
		String sql = "DISTINCT";
		sql += "(" + selectItem + ")";
		return sql;
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append("DISTINCT ");
		SelectItem selectItem = getSelectItem();
		if (selectItem != null) {
			buffer.append("(");
			selectItem.builder(builder);
			buffer.append(") ");
		}
	}
}
