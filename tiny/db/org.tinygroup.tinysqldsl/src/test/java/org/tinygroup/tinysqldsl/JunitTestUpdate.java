package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Update.update;
import junit.framework.TestCase;


public class JunitTestUpdate extends TestCase{

	
	public void testUpdate() {
		assertEquals(update(CUSTOM).set(CUSTOM.NAME.value("abc"),CUSTOM.AGE.value(3)).where(CUSTOM.NAME.eq("悠然")).sql()
				, "UPDATE custom SET custom.name = ?, custom.age = ? WHERE custom.name = ?");
	}

}
