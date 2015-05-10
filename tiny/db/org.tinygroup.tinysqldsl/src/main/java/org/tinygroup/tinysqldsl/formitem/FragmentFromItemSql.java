package org.tinygroup.tinysqldsl.formitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;
import org.tinygroup.tinysqldsl.base.FragmentSql;

/**
 * fromitem的sql片段，允许加入SQL字符串片断
 * 
 * @author renhui
 */
public class FragmentFromItemSql extends FragmentSql implements FromItem {

	private Alias alias;

	public FragmentFromItemSql(String fragment, Alias alias) {
		super(fragment);
		this.alias = alias;
	}

	public FragmentFromItemSql(String fragment) {
		super(fragment);
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		return getFragment() + ((alias != null) ? alias.toString() : "");
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(toString());
	}

}
