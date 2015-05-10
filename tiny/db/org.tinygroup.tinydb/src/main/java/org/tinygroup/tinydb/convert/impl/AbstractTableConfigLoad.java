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
package org.tinygroup.tinydb.convert.impl;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.config.TableConfigurationContainer;
import org.tinygroup.tinydb.convert.TableConfigLoad;
import org.tinygroup.tinydb.exception.TinyDbException;

/**
 * 表信息转换的抽象实现
 * @author renhui
 *
 */
public abstract class AbstractTableConfigLoad implements TableConfigLoad {
	protected Configuration configuration;
	
	protected static Logger logger = LoggerFactory.getLogger(AbstractTableConfigLoad.class);


	public void loadTable(Configuration configuration) throws TinyDbException {
		this.configuration=configuration;
		realLoadTable();
	}

	protected abstract void realLoadTable()throws TinyDbException;

	protected void addTableConfiguration(TableConfiguration table) {
		 configuration.addTableConfiguration(table);
	}
	
	protected boolean existsTable(String tableName, String schema){
		TableConfigurationContainer container=configuration.getContainer();
		return container.isExistTable(schema, tableName);
	}
	
	protected String getSchema(String schema){
		if(StringUtil.isBlank(schema)){
			return configuration.getDefaultSchema();
		}
		return schema;
	}
	
	
}
