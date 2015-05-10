package org.tinygroup.tinysqldsl;

import junit.framework.TestCase;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Select.selectFrom;
import static org.tinygroup.tinysqldsl.extend.OracleSelect.selectFrom;
import static org.tinygroup.tinysqldsl.select.OrderByElement.desc;

public class TestOracle extends TestCase {

	public void testSql() {
		String sql = selectFrom(CUSTOM).orderBy(desc(CUSTOM.NAME)).page(1, 10)
				.sql();
		assertEquals(
				"select * from ( select row_.*, rownum db_rownum from ( SELECT * FROM custom ORDER BY custom.name DESC ) row_ where rownum <=10) where db_rownum >=1",
				sql);
	}
}
