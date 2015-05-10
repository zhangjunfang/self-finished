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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;
import org.tinygroup.tinysqldsl.base.SelectBody;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.relational.ItemsList;

/**
 * 子查询
 * 
 * @author renhui
 * 
 */
public class SubSelect implements FromItem, Expression, ItemsList {

	private SelectBody selectBody;
	private Alias alias;
	private boolean useBrackets = true;

	public SubSelect(SelectBody selectBody, Alias alias, boolean useBrackets) {
		super();
		this.selectBody = selectBody;
		this.alias = alias;
		this.useBrackets = useBrackets;
	}
	
	public static SubSelect subSelect(Select select){
		return subSelect(select, "", true);
	}
	
	public static SubSelect subSelect(Select select,Alias alias, boolean useBrackets){
		SubSelect subSelect=new SubSelect(select.getPlainSelect(), alias, useBrackets);
		return subSelect;
	}
	
	public static SubSelect subSelect(Select select,String aliasName, boolean useBrackets){
		Alias alias=null;
		if(!StringUtil.isBlank(aliasName)){
			alias=new Alias(aliasName);
		}
		return subSelect(select, alias, useBrackets);
	}

	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(SelectBody body) {
		selectBody = body;
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public boolean isUseBrackets() {
		return useBrackets;
	}

	public void setUseBrackets(boolean useBrackets) {
		this.useBrackets = useBrackets;
	}

	public String toString() {
		return (useBrackets ? "(" : "") + selectBody + (useBrackets ? ")" : "")
				+ ((alias != null) ? alias.toString() : "");
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append(useBrackets ? "(" : "");
		getSelectBody().builder(builder);
		buffer.append(useBrackets ? ")" : "");
		Alias alias = getAlias();
		if (alias != null) {
			buffer.append(alias.toString());
		}
	}
}
