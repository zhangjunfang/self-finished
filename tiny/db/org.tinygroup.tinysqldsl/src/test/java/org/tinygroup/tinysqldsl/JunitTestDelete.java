package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Delete.delete;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;
import junit.framework.TestCase;


public class JunitTestDelete extends TestCase {

	
	public void testDelete() {
		assertEquals(delete(CUSTOM).where(CUSTOM.NAME.eq("悠然")).sql(), "DELETE FROM custom WHERE custom.name = ?");
		
		assertEquals(delete(CUSTOM).where(and(CUSTOM.NAME.leftLike("A"), CUSTOM.AGE.between(20, 30))).sql(), "DELETE FROM custom WHERE custom.name LIKE ? and custom.age BETWEEN ? AND ?");
	}

}
