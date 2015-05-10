package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.ScoreTable.TSCORE;
import static org.tinygroup.tinysqldsl.extend.MysqlComplexSelect.union;
import static org.tinygroup.tinysqldsl.extend.MysqlComplexSelect.unionAll;
import static org.tinygroup.tinysqldsl.extend.MysqlSelect.selectFrom;
import static org.tinygroup.tinysqldsl.select.Limit.limitWithParam;
import junit.framework.TestCase;
public class JunitTestMysqlSelect extends TestCase {

	
	public void testMysqlSelect() {
		assertEquals(selectFrom(CUSTOM).limit(1, 10).sql(), "SELECT * FROM custom LIMIT 10 OFFSET 1");
		
		assertEquals(selectFrom(CUSTOM).limit(limitWithParam()).sql(), "SELECT * FROM custom LIMIT ? OFFSET ?");
		
		assertEquals(union(selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) UNION (SELECT * FROM custom)");
		
		assertEquals(unionAll(selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) UNION ALL (SELECT * FROM custom)");

	}

}
