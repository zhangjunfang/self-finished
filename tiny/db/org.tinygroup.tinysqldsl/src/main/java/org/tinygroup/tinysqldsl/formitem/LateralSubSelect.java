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
package org.tinygroup.tinysqldsl.formitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;

/**
 * 来自子查询及别名支持
 * 
 */
public class LateralSubSelect implements FromItem {

	private SubSelect subSelect;
	private Alias alias;

	public LateralSubSelect(SubSelect subSelect, Alias alias) {
		super();
		this.subSelect = subSelect;
		this.alias = alias;
	}

	public void setSubSelect(SubSelect subSelect) {
		this.subSelect = subSelect;
	}

	public SubSelect getSubSelect() {
		return subSelect;
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public String toString() {
		return "LATERAL" + subSelect.toString()
				+ ((alias != null) ? alias.toString() : "");
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(toString());
	}
}
