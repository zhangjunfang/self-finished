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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.sql.SqlAndValues;
import org.tinygroup.tinydb.sql.StatementTransform;
import org.tinygroup.tinydb.sql.impl.StatementTransformComposite;
import org.tinygroup.tinydb.test.BaseTest;

public class TestSingleOperator extends BaseTest {

	private Bean getBean() {
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id", "aaaaaa");
		bean.setProperty("name", "123");
		bean.setProperty("length", "123");
		return bean;
	}

	public void testUpdate() throws TinyDbException {
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		bean.setProperty("name", "1235");
		bean.setProperty("length", "1235");
		assertEquals(1, getOperator().update(bean));
		getOperator().delete(bean);
	}

	public void testUpdateWithCondition() throws TinyDbException {
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		bean.setProperty("name", "1235");
		bean.setProperty("length", "123");
		List<String> conditions = new ArrayList<String>();
		conditions.add("LENGTH");
		conditions.add("ID");
		assertEquals(1, getOperator().update(bean, conditions));
		getOperator().delete(bean);
	}

	public void testDelete() throws TinyDbException {
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		assertEquals(1, getOperator().delete(bean));
	}

	public void testQuery() throws TinyDbException {
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		Bean b = getOperator().getBean(String.valueOf(bean.getProperty("id")),
				ANIMAL);
		getOperator().delete(b);
	}

	public void testQuerys() throws TinyDbException {
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		Bean[] beans = getOperator().getBeans(bean);
		assertEquals(1, beans.length);
		getOperator().deleteBean(beans);
	}

	public void testSelectSql() throws TinyDbException {

		StatementTransform statementTransform = new StatementTransformComposite(
				configuration);
		Bean bean = getBean();
		SqlAndValues sqlAndValues = statementTransform.toSelect(bean);
		assertEquals(
				"select * from opensource.animal where ID = ?  and NAME  like  ?  and LENGTH = ?  order by name asc",
				sqlAndValues.getSql().trim());
		bean.set(Bean.SELECT_ITEMS_KEY, "id,name");
		bean.set(Bean.CONDITION_FIELD_KEY, new String[] { "id", "name" });
		bean.set(Bean.CONDITION_MODE_KEY, new String[] { "equals",
				"containsWith" });
		bean.set(Bean.ORDER_BY_KEY, new String[] { "id", "name" });
		bean.set(Bean.SORT_DIRECTION_KEY, new String[] { "asc", "desc" });
		bean.set(Bean.GROUP_BY_KEY, new String[] { "id", "name" });
		sqlAndValues = statementTransform.toSelect(bean);
		assertEquals(
				"select id,name from opensource.animal where ID = ?  and NAME  like  ?  and LENGTH = ?  group by id,name order by id asc ,name desc",
				sqlAndValues.getSql().trim());
		bean.set(Bean.EMPTY_CONDITION_KEY, "name");
		bean.set("name", null);
		sqlAndValues = statementTransform.toSelect(bean);
		assertEquals(
				"select id,name from opensource.animal where ID = ?  and NAME is null  and LENGTH = ?  group by id,name order by id asc ,name desc",
				sqlAndValues.getSql().trim());
		bean.remove(Bean.EMPTY_CONDITION_KEY);
		sqlAndValues = statementTransform.toSelect(bean);
		assertEquals(
				"select id,name from opensource.animal where ID = ?  and LENGTH = ?  group by id,name order by id asc ,name desc",
				sqlAndValues.getSql().trim());
		bean.set("id", new String[] { "123", "235" });
		bean.set("name", new String[] { "xuan", "test", "ballack" });
		bean.set(Bean.CONDITION_MODE_KEY, new String[] { "betweenAnd", "in" });
		sqlAndValues = statementTransform.toSelect(bean);
		assertEquals(
				"select id,name from opensource.animal where ID between ? and ?  and NAME in (?,?,?) and LENGTH = ?  group by id,name order by id asc ,name desc",
				sqlAndValues.getSql().trim());
	}

}
