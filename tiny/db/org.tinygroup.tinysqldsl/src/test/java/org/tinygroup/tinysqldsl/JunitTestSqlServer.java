package org.tinygroup.tinysqldsl;

import junit.framework.TestCase;
import static org.tinygroup.tinysqldsl.extend.SqlServerSelect.selectFrom;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.select.Offset.offsetRow;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithFirstRow;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithFirstRowParam;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithFirstRows;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithFirstRowsParam;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithNextRow;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithNextRowParam;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithNextRows;
import static org.tinygroup.tinysqldsl.select.Fetch.fetchWithNextRowsParam;

public class JunitTestSqlServer extends TestCase{

	public void testSqlServer() {
		assertEquals(selectFrom(CUSTOM).offset(offsetRow(0)).sql(),"SELECT * FROM custom OFFSET 0 ROW");
		
		assertEquals(selectFrom(CUSTOM).fetch(fetchWithFirstRow(1)).sql(),"SELECT * FROM custom FETCH FIRST 1 ROW ONLY");
		
		assertEquals(selectFrom(CUSTOM).fetch(fetchWithFirstRowParam(1)).sql(),"SELECT * FROM custom FETCH FIRST ? ROW ONLY");
		
		assertEquals(selectFrom(CUSTOM).fetch(fetchWithFirstRows(5)).sql(),"SELECT * FROM custom FETCH FIRST 5 ROWS ONLY");
		
		assertEquals(selectFrom(CUSTOM).fetch(fetchWithFirstRowsParam(5)).sql(),"SELECT * FROM custom FETCH FIRST ? ROWS ONLY");
		
		assertEquals(selectFrom(CUSTOM).offset(offsetRow(5)).fetch(fetchWithNextRow(1)).sql(),"SELECT * FROM custom OFFSET 5 ROW FETCH NEXT 1 ROW ONLY");
		
		assertEquals(selectFrom(CUSTOM).offset(offsetRow(5)).fetch(fetchWithNextRowParam(1)).sql(),"SELECT * FROM custom OFFSET 5 ROW FETCH NEXT ? ROW ONLY");
		
		assertEquals(selectFrom(CUSTOM).offset(offsetRow(5)).fetch(fetchWithNextRows(5)).sql(),"SELECT * FROM custom OFFSET 5 ROW FETCH NEXT 5 ROWS ONLY");
		
		assertEquals(selectFrom(CUSTOM).offset(offsetRow(5)).fetch(fetchWithNextRowsParam(5)).sql(),"SELECT * FROM custom OFFSET 5 ROW FETCH NEXT ? ROWS ONLY");
	}

}
