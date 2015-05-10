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
package org.tinygroup.tinydb.testcase.operator;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.test.BaseTest;

public class TestUpdateMark extends BaseTest{
	
	
	private Bean getBean(){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id","aaaaaa");
		bean.setProperty("name","123");
		bean.setProperty("length","123");
		return bean;
	}
	
	public void testUpdateMark() throws TinyDbException{
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		Bean[] beans=getOperator().getBeans("select * from animal where name=?","123");
		bean=beans[0];
		bean.setProperty("name","testMark");
		assertEquals(1, getOperator().update(bean));
		bean=getOperator().getBean(bean.get("id").toString(),ANIMAL);
		assertEquals("testMark", bean.get("name"));
		assertEquals(123, bean.get("length"));
		getOperator().delete(bean);
	}
	

}
