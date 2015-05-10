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
package org.tinygroup.dbrouter.impl.keygenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.dbrouter.RouterKeyGenerator;
import org.tinygroup.dbrouter.config.DataSourceConfig;
import org.tinygroup.dbrouter.config.KeyTable;
import org.tinygroup.dbrouter.config.KeyTables;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 功能说明:集群主键生成器的抽象类
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-6 <br>
 * <br>
 */
public abstract class AbstractRouterKeyGenerator<T extends Number> implements
		RouterKeyGenerator<T> {

	protected static final String END_NUMBER = "end_number";

	transient protected Router router;

	transient private Connection connection = null;
	// 表名：主键配置
	protected transient  Map<String, KeyConfigArea> caches = new HashMap<String, KeyConfigArea>();

	private static Logger logger = LoggerFactory
			.getLogger(AbstractRouterKeyGenerator.class);

	private static final int DEFAULT_STEP = 100;

	public static final String DEFAULT_KEY_TABLE_NAME = "key_table";
	
	@XStreamAlias("increment")
	@XStreamAsAttribute
	private int increment = 1;
	@XStreamAlias("key-table-name")
	@XStreamAsAttribute
	private String keyTableName = DEFAULT_KEY_TABLE_NAME;
	/**
	 * 每次从数据库获取的幅度
	 */
	private int step = DEFAULT_STEP;
	@XStreamAlias("data-source-id")
	@XStreamAsAttribute
	private String dataSourceId;
	
	@XStreamAlias("auto-create")
	@XStreamAsAttribute
	private boolean autoCreate = false;

	public String getKeyTableName() {
		if (keyTableName == null) {
			keyTableName = DEFAULT_KEY_TABLE_NAME;
		}
		return keyTableName;
	}

	public void setKeyTableName(String keyTableName) {
		this.keyTableName = keyTableName;
	}

	public int getIncrement() {
		if (increment == 0) {
			increment = 1;
		}
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	public int getStep() {
		if (step == 0) {
			step = DEFAULT_STEP;
		}
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public boolean isAutoCreate() {
		return autoCreate;
	}

	public void setAutoCreate(boolean autoCreate) {
		this.autoCreate = autoCreate;
	}
	

	public void createKeyTable(KeyTables keyTables) {
		DataSourceConfig dataSourceConfig=router.getDataSourceConfig(getDataSourceId());
		//解析配置文件
		String language = DbRouterUtil.getLanguageByUrl(dataSourceConfig.getUrl());
		KeyTable keyTable = keyTables.getKeyTable(language, getClass().getName());
		if(keyTable==null){
			logger.logMessage(LogLevel.ERROR, "router:{0},查找language:{1},类型:{2}的主键表配置失败", router.getId(),language,getClass().getName());
			return;
		}
		
		//初始化参数
		KeyTableInstallProcessor tableInstallProcessor = new KeyTableInstallProcessor();
		tableInstallProcessor.setKeyTable(keyTable);
		tableInstallProcessor.setTableName(getKeyTableName());
		tableInstallProcessor.setDataSourceConfig(dataSourceConfig);
		
		//执行创建逻辑
		tableInstallProcessor.process(language);
	}

	public T getKey(String tableName) {
		if(caches==null){
			caches=new HashMap<String, KeyConfigArea>();
		}
		KeyConfigArea area = caches.get(tableName);
		if (area == null) {
			area = new KeyConfigArea();
			updateKey(tableName, area, new WithNoResultCallBack() {

				public void callback(String tableName, Statement statement)
						throws SQLException {
					String sql = "insert into " + getKeyTableName()
							+ "(end_number,table_name) values("
							+ getStep() + ",'" + tableName + "')";
					statement.executeUpdate(sql);
				}
			});
			caches.put(tableName, area);
		}
		if (area.checkUpdateKey()) {
			updateKey(tableName, area, new WithNoResultCallBack() {

				public void callback(String tableName, Statement statement)
						throws SQLException {
					throw new DbrouterRuntimeException("集群主键表:"
							+ getKeyTableName() + "查询不到" + tableName
							+ "的记录");
				}
			});
		}
		long nowCurrentNumber = area.getCurrentNumber()
				+ getIncrement();
		area.setCurrentNumber(nowCurrentNumber);
		return generatorNextKey(nowCurrentNumber);
	}

	private synchronized void updateKey(String tableName, KeyConfigArea area,
			WithNoResultCallBack callback) {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			if (connection == null || connection.isClosed()) {
				connection = DbRouterUtil.createConnection(router
						.getDataSourceConfig(getDataSourceId()));
			}
			String generatorTableName = getKeyTableName();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from "
					+ generatorTableName + " where table_name='" + tableName
					+ "'");
			if (resultSet.next()) {
				long oldEndNumber = resultSet.getLong(END_NUMBER);
				long newEndNumber = oldEndNumber + getStep();
				String sql = "update " + generatorTableName
						+ " set end_number=" + newEndNumber
						+ " where table_name='" + tableName + "'";
				statement.executeUpdate(sql);
				area.setCurrentNumber(oldEndNumber);
				area.setEndNumber(newEndNumber);
			} else {
				callback.callback(tableName, statement);
				area.setEndNumber(getStep());
				area.setCurrentNumber(0);
			}
		} catch (SQLException e) {
			logger.errorMessage("获取表" + tableName + "的主键时发生异常", e);
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					logger.errorMessage("关闭连接时发生异常！", ex);
					throw new DbrouterRuntimeException(ex);
				}
			}
			throw new DbrouterRuntimeException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException ex) {
				logger.errorMessage(ex.getMessage(), ex);
				throw new DbrouterRuntimeException(ex);
			}
		}
	}

	/**
	 * 获取下一个key值
	 * 
	 * @param currentNumber
	 * @return
	 */
	protected abstract T generatorNextKey(Long currentNumber);

	public void setRouter(Router router) {
		this.router = router;
	}

	class KeyConfigArea {
		private long currentNumber;// 当前key值
		private long endNumber;// 范围

		public long getCurrentNumber() {
			return currentNumber;
		}

		public void setCurrentNumber(long currentNumber) {
			this.currentNumber = currentNumber;
		}

		public long getEndNumber() {
			return endNumber;
		}

		public void setEndNumber(long endNumber) {
			this.endNumber = endNumber;
		}

		public boolean checkUpdateKey() {
			return currentNumber + getIncrement() - endNumber > 0;
		}

	}

	/**
	 * 
	 * 查询记录不存在的回调操作
	 */
	interface WithNoResultCallBack {
		void callback(String tableName, Statement statement)
				throws SQLException;
	}

}
