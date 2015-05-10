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
package org.tinygroup.tinysqldsl;

import org.tinygroup.tinysqldsl.base.Condition;
import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.formitem.FromItem;
import org.tinygroup.tinysqldsl.formitem.FromItemList;
import org.tinygroup.tinysqldsl.select.Join;
import org.tinygroup.tinysqldsl.select.OrderByElement;
import org.tinygroup.tinysqldsl.select.PlainSelect;
import org.tinygroup.tinysqldsl.selectitem.AllColumns;
import org.tinygroup.tinysqldsl.selectitem.CustomSelectItem;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * Created by luoguo on 2015/3/11.
 */
public class Select<T extends Select<T>> extends StatementSqlBuilder implements
		Statement {

	protected PlainSelect plainSelect;
	private String id;

	public String getId() {
		return id;
	}

	protected Select() {
		super();
		plainSelect = new PlainSelect();
	}

	public PlainSelect getPlainSelect() {
		return plainSelect;
	}

	@SuppressWarnings("rawtypes")
	public static Select select(SelectItem... selectItems) {
		Select select = new Select();
		select.getPlainSelect().addSelectItems(selectItems);
		return select;
	}

	public static CustomSelectItem customSelectItem(String format,
			SelectItem... selectItems) {
		return new CustomSelectItem(format, selectItems);
	}

	@SuppressWarnings("rawtypes")
	public static Select selectFrom(Table... tables) {
		Select select = new Select();
		select.getPlainSelect().addSelectItems(new AllColumns());
		select.getPlainSelect().setFromItem(new FromItemList(tables));
		return select;
	}

	@SuppressWarnings("unchecked")
	public T from(FromItem fromItems) {
		plainSelect.setFromItem(fromItems);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T join(Join... joins) {
		plainSelect.addJoins(joins);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T where(Condition condition) {
		plainSelect.setWhere(condition);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T groupBy(Expression... expressions) {
		plainSelect.addGroupByExpressions(expressions);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T orderBy(OrderByElement... orderByElements) {
		plainSelect.addOrderByElements(orderByElements);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T having(Condition condition) {
		plainSelect.setHaving(condition);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T forUpdate() {
		plainSelect.setForUpdate(true);
		return (T) this;
	}

	@Override
	public String toString() {
		return sql();
	}

	@Override
	protected void parserStatementBody() {
		build(plainSelect);
	}

	public void id(String id) {
		this.id = id;
	}
}
