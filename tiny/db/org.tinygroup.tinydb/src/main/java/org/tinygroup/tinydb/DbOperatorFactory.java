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
package org.tinygroup.tinydb;

import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.impl.BeanOperatorManagerImpl;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * Created by luoguo on 2014/5/23.
 */
public  class DbOperatorFactory {
	private Configuration configuration;
	private BeanOperatorManager beanOperatorManager;
	public DbOperatorFactory(Configuration configuration) {
		this.configuration=configuration;
		beanOperatorManager = new BeanOperatorManagerImpl(configuration);
	}
	
	public DBOperator getDBOperator() throws TinyDbException{
		return beanOperatorManager.getDbOperator();
	}
	
	public DBOperator getDBOperator(String schema) throws TinyDbException{
		return beanOperatorManager.getDbOperator(schema);
	}
	
	public DBOperator getNewDBOperator() throws TinyDbException{
		return beanOperatorManager.getNewDbOperator();
	}
	
	public DBOperator getNewDBOperator(String schema) throws TinyDbException{
		return beanOperatorManager.getNewDbOperator(schema);
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public BeanOperatorManager getBeanOperatorManager() {
		return beanOperatorManager;
	}

}
