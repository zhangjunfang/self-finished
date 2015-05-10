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

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.database.view.ViewProcessor;

public class MDAViewTest extends TestCase {
	static {
		TestInit.init();

	}
	ViewProcessor viewProcessor;
	

	protected void setUp() throws Exception {
		super.setUp();
		viewProcessor = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(DataBaseUtil.VIEW_BEAN);
	}

	public void testGetViewString() {
		assertNotNull(viewProcessor.getView("usercompanyview"));
	}

	public void testOracleViewCreateSql() {
		System.out.println("usercompanyview, oracle sql:");
		String viewSql= viewProcessor.getCreateSql("usercompanyview", "oracle");
		System.out.println(viewSql);
	}
	
	public void testMysqlViewCreateSql() {
		System.out.println("usercompanyview, mysql sql:");
		String viewSql= viewProcessor.getCreateSql("usercompanyview", "mysql");
		System.out.println(viewSql);
	}
	
	public void testH2ViewCreateSql() {
		System.out.println("usercompanyview, h2 sql:");
		String viewSql= viewProcessor.getCreateSql("usercompanyview", "h2");
		System.out.println(viewSql);
	}
	
	public void testDb2ViewCreateSql() {
		System.out.println("usercompanyview, db2 sql:");
		String viewSql= viewProcessor.getCreateSql("usercompanyview", "db2");
		System.out.println(viewSql);
	}
	
	public void testDerbyViewCreateSql() {
		System.out.println("usercompanyview, derby sql:");
		String viewSql= viewProcessor.getCreateSql("usercompanyview", "derby");
		System.out.println(viewSql);
	}
	
}
