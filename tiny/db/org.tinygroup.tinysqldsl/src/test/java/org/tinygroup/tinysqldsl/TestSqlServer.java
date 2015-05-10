package org.tinygroup.tinysqldsl;

import junit.framework.TestCase;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.extend.SqlServerSelect.selectFrom;
import static org.tinygroup.tinysqldsl.select.OrderByElement.desc;
import static org.tinygroup.tinysqldsl.select.Offset.*;
import static org.tinygroup.tinysqldsl.select.Fetch.*;

public class TestSqlServer extends TestCase {

	
	
	public void testSql(){
		String sql = selectFrom(CUSTOM).orderBy(desc(CUSTOM.NAME)).offset(offsetRow(1)).fetch(fetchWithNextRow(10)).sql();
		assertEquals("SELECT * FROM custom ORDER BY custom.name DESC OFFSET 1 ROW FETCH NEXT 10 ROW ONLY", sql);
	}
	
	
}
