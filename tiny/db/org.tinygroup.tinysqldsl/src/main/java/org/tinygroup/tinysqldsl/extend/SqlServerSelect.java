package org.tinygroup.tinysqldsl.extend;

import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.formitem.FromItemList;
import org.tinygroup.tinysqldsl.select.Fetch;
import org.tinygroup.tinysqldsl.select.Offset;
import org.tinygroup.tinysqldsl.selectitem.AllColumns;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * 与sqlserver数据库相关的查询语句
 * 
 * @author renhui
 * 
 */
public class SqlServerSelect extends Select<SqlServerSelect> {

	private SqlServerSelect() {
		super();
	}

	public static SqlServerSelect select(SelectItem... selectItems) {
		SqlServerSelect select = new SqlServerSelect();
		select.getPlainSelect().addSelectItems(selectItems);
		return select;
	}

	public static SqlServerSelect selectFrom(Table... tables) {
		SqlServerSelect select = new SqlServerSelect();
		select.getPlainSelect().addSelectItems(new AllColumns());
		select.getPlainSelect().setFromItem(new FromItemList(tables));
		return select;
	}

	public SqlServerSelect into(Table... tables) {
		plainSelect.addIntoTables(tables);
		return this;
	}

	public SqlServerSelect offset(Offset offset) {
		plainSelect.setOffset(offset);
		return this;
	}

	public SqlServerSelect fetch(Fetch fetch) {
		plainSelect.setFetch(fetch);
		return this;
	}
}
