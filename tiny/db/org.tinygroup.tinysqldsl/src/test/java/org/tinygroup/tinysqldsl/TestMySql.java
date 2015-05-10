package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.extend.MysqlSelect.selectFrom;
import static org.tinygroup.tinysqldsl.select.OrderByElement.desc;
import junit.framework.TestCase;

public class TestMySql extends TestCase {

	public void testSql() {
		String sql = selectFrom(CUSTOM).orderBy(desc(CUSTOM.NAME)).limit(1, 10)
				.sql();
		assertEquals(
				"SELECT * FROM custom ORDER BY custom.name DESC LIMIT 10 OFFSET 1",
				sql);
	}
}
