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

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.Pager;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.test.BaseTest;

public class TestSqlOperator extends BaseTest{
	
//	BeanType[] getPagedBeans(String beanType,String sql);
	public void testGetBeansBySql() throws TinyDbException{
		
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL";
		Bean[] beans =getOperator().getBeans(sql);
		assertEquals(25, beans.length);
		
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, int start, int limit);
	public void testPagingGetBeansBySql() throws TinyDbException{
		
		//getOperator().delete(getPagedBeans());
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		
		String sql = "select * from ANIMAL";
		Bean[] beans = getOperator().getPageBeans(sql, 0, 10);
		assertEquals(10, beans.length);
		beans = getOperator().getPageBeans(sql, 11, 10);
		assertEquals(10, beans.length);
		beans = getOperator().getPageBeans(sql, 21, 10);
		assertEquals(5, beans.length);
		
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, Map<String, Object> parameters);
	public void testGetBeansBySqlAndMap() throws TinyDbException{
		
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=@name";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "testSql");
		Bean[] beans =getOperator().getBeans(sql, parameters);
		assertEquals(25, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, int start, int limit,
//			Map<String, Object> parameters);
	public void testPagingGetBeansBySqlAndMap() throws TinyDbException{
		
		Bean[] insertBeans = getBeans(25);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=@name";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "testSql");
		
		Bean[] beans =getOperator().getPageBeans(sql, 0, 10, parameters);
		assertEquals(10, beans.length);
		beans =getOperator().getPageBeans(sql, 11, 10, parameters);
		assertEquals(10, beans.length);
		beans =getOperator().getPageBeans(sql, 21, 10, parameters);
		assertEquals(5, beans.length);
		
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, Object... parameters);
	public void testGetBeansBySqlAndArray() throws TinyDbException{
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=? and length=?";
		Bean[] beans =getOperator().getBeans(sql, "testSql",1234);
//		System.out.println(beans[0].get("length").getClass());
//		assertEquals(12345, beans[0].get("length"));
		assertEquals(25, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, int start, int limit, Object... parameters);
	public void testPagingGetBeansBySqlAndArray() throws TinyDbException{
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=? and length=?";
		Bean[] beans =getOperator().getPageBeans(sql, 0, 10, "testSql", 1234);
		assertEquals(10, beans.length);
		beans =getOperator().getPageBeans(sql, 11, 10, "testSql", 1234);
		assertEquals(10, beans.length);
		beans =getOperator().getPageBeans(sql, 21, 10, "testSql", 1234);
		assertEquals(5, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
	public void testCursorPagingGetBeansBySqlAndArray() throws TinyDbException{
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=? and length=?";
		Bean[] beans =getOperator().getCursorPageBeans(sql, 0, 10, "testSql", 1234);
		assertEquals(10, beans.length);
		beans =getOperator().getCursorPageBeans(sql, 11, 10, "testSql", 1234);
		assertEquals(10, beans.length);
		beans =getOperator().getCursorPageBeans(sql, 21, 10, "testSql", 1234);
		assertEquals(5, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
	
	
	public void testSynColumnName() throws TinyDbException{
		Bean animalBean=new Bean(ANIMAL);
		animalBean.setProperty("name","testSql");
		animalBean.setProperty("length","1234");
		getOperator().delete(animalBean);
		getOperator().insert(animalBean);
		DBOperator operator2=factory.getDBOperator();
		Bean branchBean = new Bean(BRANCH);
		branchBean.set("branchName", "sfsf");
		operator2.delete(branchBean);
		operator2.insert(branchBean);
		Bean[] beans=getOperator().getBeans("select a.name name,b.branch_name name from ANIMAL a,A_BRANCH b");
		for (Bean bean : beans) {
			assertEquals("testSql", bean.get("name"));
			assertEquals("sfsf", bean.get("name1"));
		}
		getOperator().delete(animalBean);
		operator2.delete(branchBean);
	}
	
	public void testPagingByBean() throws TinyDbException{
		
		//getOperator().delete(getPagedBeans());
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		
		Bean bean=new Bean(ANIMAL);
		bean.set("name", "testSql");
		Pager pager = getOperator().getPager(bean, 0, 10);
		assertEquals(10, pager.getCurrentBeans().length);
		pager = getOperator().getPager(bean, 11, 10);
		assertEquals(10, pager.getCurrentBeans().length);
		pager = getOperator().getPager(bean, 21, 10);
		assertEquals(5, pager.getCurrentBeans().length);
		
		getOperator().batchDelete(insertBeans);
	}
	
	public void testRelationQueryBySql() throws TinyDbException{
		Bean animal=new Bean(ANIMAL);
		animal.set("name", "test");
		animal.set("length", 12);
		Bean branch=new Bean(BRANCH);
		branch.set("branchName", "test");
		getOperator().delete(animal);
		getOperator().delete(branch);
		getOperator().insert(animal);
		getOperator().insert(branch);
		String sql="select a.id,a.name,a.length,b.branch_id,b.branch_name from animal a,A_BRANCH b where a.name=b.branch_name";
		Bean bean=getOperator().getSingleValue(sql);
		assertEquals("test",bean.get("name"));
		assertEquals(12,bean.get("length"));
		assertEquals("test",bean.get("branchName"));
		getOperator().delete(animal);
		getOperator().delete(branch);
        		
	}
}
