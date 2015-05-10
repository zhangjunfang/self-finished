package org.tinygroup.tinysqldsl.selectitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.FragmentSql;

/**
 * select 与 from 之间的特殊化sql片段
 *
 * @author renhui
 */
public class FragmentSelectItemSql extends FragmentSql implements SelectItem {

    public FragmentSelectItemSql(String fragment) {
        super(fragment);
    }

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(getFragment());
	}
}
