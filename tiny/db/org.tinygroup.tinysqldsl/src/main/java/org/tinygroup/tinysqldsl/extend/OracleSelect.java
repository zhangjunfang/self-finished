package org.tinygroup.tinysqldsl.extend;

import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.base.Condition;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.expression.OracleHierarchicalExpression;
import org.tinygroup.tinysqldsl.formitem.FromItemList;
import org.tinygroup.tinysqldsl.selectitem.AllColumns;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * oracle数据库相关的查询
 * @author renhui
 *
 */
public class OracleSelect extends Select<OracleSelect> {

	private OracleSelect() {
		super();
	}

	public static OracleSelect select(SelectItem... selectItems) {
		OracleSelect select = new OracleSelect();
		select.getPlainSelect().addSelectItems(selectItems);
		return select;
	}

	public static OracleSelect selectFrom(Table... tables) {
		OracleSelect select = new OracleSelect();
		select.getPlainSelect().addSelectItems(new AllColumns());
		select.getPlainSelect().setFromItem(new FromItemList(tables));
		return select;
	}
	
	public OracleSelect into(Table... tables) {
		plainSelect.addIntoTables(tables);
		return this;
	}

	public OracleSelect page(int start, int limit) {
		StringBuilder pagingSelect = new StringBuilder();
		if (start == 0) {
			start = 1;
		}
		pagingSelect
				.append("select * from ( select row_.*, rownum db_rownum from ( ");
		pagingSelect.append(sql());
		pagingSelect.append(" ) row_ where rownum <=" + (start + limit - 1)
				+ ") where db_rownum >=" + start);
		this.stringBuilder = pagingSelect;
		return this;
	}

	public OracleSelect startWith(Condition startWithCondition,
			Condition connectCondition, boolean noCycle) {
		OracleHierarchicalExpression expression = new OracleHierarchicalExpression();
		expression.setStartExpression(startWithCondition.getExpression());
		expression.setConnectExpression(connectCondition.getExpression());
		expression.setNoCycle(noCycle);
		plainSelect.setOracleHierarchical(expression);
		return this;
	}
}
