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
package org.tinygroup.dbrouter.config;

import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.dbrouter.RouterKeyGenerator;
import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 集群<br>
 * 一个集群可包含多个分区
 * 
 * @author luoguo
 */
@XStreamAlias("router")
public class Router {
	/**
	 * 集群名称,唯一确定一个集群，jdbc数据源连接时，url只要指定ID即可
	 */
	@XStreamAsAttribute
	private String id;
	/**
	 * 用户名
	 */
	@XStreamAlias("user-name")
	@XStreamAsAttribute
	private String userName;
	/**
	 * 密码
	 */
	@XStreamAsAttribute
	private String password;

	@XStreamAlias("key-generator")
	private RouterKeyGenerator keyGenerator;

	/**
	 * 集群中的数据源,这里只是定义
	 */
	@XStreamAlias("data-source-configs")
	private List<DataSourceConfig> dataSources;
	/**
	 * 一个集群由多个分区组成
	 */
	@XStreamAlias("partitions")
	private List<Partition> partitions;
	@XStreamAsAttribute
	@XStreamAlias("time-out")
	private int timeOut;
	@XStreamAsAttribute
	@XStreamAlias("thread-size")
	private int threadSize = DEFAULT_THREAD_SIZE;
	/**
	 * 是否启用JTA事务
	 */
	@XStreamAlias("jta-enabled")
	@XStreamAsAttribute
	private boolean jtaEnabled;
	@XStreamAsAttribute
	private String schema;
	@XStreamAsAttribute
	private String catalog;

	private static final int DEFAULT_THREAD_SIZE = 100;
	
	public Router() {
		super();
	}

	public Router(String id, String userName, String password) {
		this.id = id;
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<DataSourceConfig> getDataSources() {
		return dataSources;
	}

	public void setDataSource(List<DataSourceConfig> dataSources) {
		this.dataSources = dataSources;
	}

	public List<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<Partition> partitions) {
		this.partitions = partitions;
		if (!CollectionUtil.isEmpty(partitions)) {
			for (Partition partition : partitions) {
				partition.setRouter(this);
			}
		}
	}

	public DataSourceConfig getDataSourceConfig(String datasourceId) {
		for (DataSourceConfig dataSourceConfig : dataSources) {
			if (datasourceId.equals(dataSourceConfig.getId())) {
				return dataSourceConfig;
			}
		}
		throw new DbrouterRuntimeException("找不到数据源：" + datasourceId);
	}

	public RouterKeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(RouterKeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	public void setDataSources(List<DataSourceConfig> dataSources) {
		this.dataSources = dataSources;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getThreadSize() {
		if (threadSize <= 0) {
			threadSize = DEFAULT_THREAD_SIZE;
		}
		return threadSize;
	}

	public void setThreadSize(int threadSize) {
		this.threadSize = threadSize;
	}

	public boolean isJtaEnabled() {
		return jtaEnabled;
	}

	public void setJtaEnabled(boolean jtaEnabled) {
		this.jtaEnabled = jtaEnabled;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

}
