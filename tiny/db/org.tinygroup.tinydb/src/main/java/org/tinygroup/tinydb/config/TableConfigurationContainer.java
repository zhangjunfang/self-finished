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
package org.tinygroup.tinydb.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;

/**
 * 存储表配置信息的容器
 * @author renhui
 *
 */
public class TableConfigurationContainer {
    /**
     * 存储所有表配置信息的列表
     */
	private List<TableConfiguration> tableConfigurations=new ArrayList<TableConfiguration>();
	/**
	 * 保存模式下所有表信息
	 */
	private Map<String, List<TableConfiguration>> schemaTables=new HashMap<String, List<TableConfiguration>>();
	/**
	 * 保存所有模式配置
	 */
	private List<SchemaConfig> schemaConfigs=new ArrayList<SchemaConfig>();
	
	public void addTableConfiguration(TableConfiguration configuration){
		if(!tableConfigurations.contains(configuration)){
			tableConfigurations.add(configuration);
		}
		List<TableConfiguration> tables=schemaTables.get(configuration.getSchema());
		if(tables==null){
			tables=new ArrayList<TableConfiguration>();
			schemaTables.put(configuration.getSchema(), tables);
		}
		tables.add(configuration);
	}
	
	public TableConfiguration getTableConfiguration(String schema,String tableName){
	     List<TableConfiguration> tableConfigs=schemaTables.get(schema);
	     if(!CollectionUtil.isEmpty(tableConfigs)){
	    	 for (TableConfiguration tableConfig : tableConfigs) {
				if(tableConfig.getName().equalsIgnoreCase(tableName)){
					return tableConfig;
				}
			 }
	     }
		return null;
	}
	
	public List<SchemaConfig> getSchemaConfigs(){
		return schemaConfigs;
	}
	
	public boolean isExistTable(String schema,String tableName){
		TableConfiguration configuration=getTableConfiguration(schema, tableName);
        return configuration!=null;
	}
	
}
