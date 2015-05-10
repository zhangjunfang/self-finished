/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.database;

import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.util.DataBaseUtil;

public class MDATableTest extends TestCase {
	static {
		TestInit.init();

	}
	TableProcessor tableProcessor;

	protected void setUp() throws Exception {
		super.setUp();
		tableProcessor = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(DataBaseUtil.TABLEPROCESSOR_BEAN);
	}

	public void testGetTableStringString() {
		assertNotNull(tableProcessor.getTable("com.hundsun", "user"));
		assertNotNull(tableProcessor.getTable("com.hundsun", "company"));
	}

	public void testGetTableString() {
		assertNotNull(tableProcessor.getTable("user"));
		assertNotNull(tableProcessor.getTable("company"));
	}
	
	public void testOracleCreateSql() {
		System.out.println("com.hundsun.user,oracle sql:");
		List<String> tableSql= tableProcessor.getCreateSql( "user","com.hundsun", "oracle");
		System.out.println(tableSql);
		assertEquals(2, tableSql.size());
		
		System.out.println("com.hundsun.company,oracle sql:");
		List<String> tableSql2= tableProcessor.getCreateSql("company", "com.hundsun", "oracle");
		System.out.println(tableSql2);
		assertEquals(2, tableSql2.size());
	}
	
	public void testDb2CreateSql() {
		System.out.println("com.hundsun.user,db2 sql:");
		List<String> tableSql= tableProcessor.getCreateSql( "user","com.hundsun", "db2");
		System.out.println(tableSql);
		assertEquals(2, tableSql.size());
		
		System.out.println("com.hundsun.company,db2 sql:");
		List<String> tableSql2= tableProcessor.getCreateSql("company", "com.hundsun", "db2");
		System.out.println(tableSql2);
		assertEquals(2, tableSql2.size());
	}
	
	public void testH2CreateSql() {
		System.out.println("com.hundsun.user,h2 sql:");
		List<String> tableSql= tableProcessor.getCreateSql( "user","com.hundsun", "h2");
		System.out.println(tableSql);
		assertEquals(2, tableSql.size());
		
		System.out.println("com.hundsun.company,h2 sql:");
		List<String> tableSql2= tableProcessor.getCreateSql("company", "com.hundsun", "h2");
		System.out.println(tableSql2);
		assertEquals(2, tableSql2.size());
	}
	
	public void testMysqlCreateSql() {
		System.out.println("com.hundsun.user,mysql sql:");
		List<String> tableSql= tableProcessor.getCreateSql( "user","com.hundsun", "mysql");
		System.out.println(tableSql);
		assertEquals(2, tableSql.size());
		
		System.out.println("com.hundsun.company,mysql sql:");
		List<String> tableSql2= tableProcessor.getCreateSql("company", "com.hundsun", "mysql");
		System.out.println(tableSql2);
		assertEquals(2, tableSql2.size());
	}
	
	
}
