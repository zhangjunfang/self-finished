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
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.test.BaseTest;

public class TestDbMonitor extends BaseTest{

	public void testMonitor() throws TinyDbException{
		DBOperator operator=factory.getDBOperator();
		operator.execute("delete from animal");//刪除
		Thread thread1=new Thread(new Runnable() {
			public void run() {
				beanOperator();
			}
		});
		Thread thread2=new Thread(new Runnable() {
			public void run() {
				beanOperator();
			}
		});
		Thread thread3=new Thread(new Runnable() {
			public void run() {
				beanOperator();
			}
		});
		thread1.start();
		thread2.start();
		thread3.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		assertEquals(0, operator.queryAllActiveConnection().size());
		operator.execute("delete from animal");//刪除
	}
	
	/**
	 * @param operator
	 * @param bean
	 */
	private void beanOperator() {
		final Bean bean=getAnimalBean();
		try {
			DBOperator operator=factory.getDBOperator();
			operator.beginTransaction();
			Bean newBean=operator.insert(bean);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			operator.getBean(newBean.get("id"), ANIMAL);
			operator.delete(newBean);
			operator.commitTransaction();//此时才关闭连接
		} catch (TinyDbException e) {
			fail(e.getMessage());
		}
	}
	
	private Bean getAnimalBean() {
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id", "beanId");
		bean.setProperty("name", "1234");
		bean.setProperty("length", "1234");
		return bean;
	}
	
}
