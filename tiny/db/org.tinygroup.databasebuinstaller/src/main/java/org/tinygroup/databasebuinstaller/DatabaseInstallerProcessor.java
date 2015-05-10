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
package org.tinygroup.databasebuinstaller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.database.util.DataSourceInfo;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明:数据库安装类
 * <p>
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class DatabaseInstallerProcessor implements ApplicationProcessor {
	public static final String DATABASE_INSTALLER_BEAN_NAME = "databaseInstaller";
	private Logger logger = LoggerFactory
			.getLogger(DatabaseInstallerProcessor.class);
	private String defaultLanuage = "oracle";
	private String dbLanguage = "";
	private List<InstallProcessor> installProcessors = new ArrayList<InstallProcessor>();
	private XmlNode componentConfig;
	private XmlNode applicationConfig;

	public String getDbLanguage() {
		return dbLanguage;
	}

	public void setDbLanguage(String dbLanguage) {
		this.dbLanguage = dbLanguage;
	}

	public List<InstallProcessor> getInstallProcessors() {
		return installProcessors;
	}

	public void setInstallProcessors(List<InstallProcessor> installProcessors) {
		this.installProcessors = installProcessors;
	}

	public String getNodeName() {
		return "database-installer";
	}

	public String getLanguage() {
		if ("".equals(dbLanguage))
			return defaultLanuage;
		return dbLanguage;
	}

	public Map<Class, List<String>> getChangeSqls() {
		installSort();
		Map<Class, List<String>> processSqls = new HashMap<Class, List<String>>();
		DataSource dataSource = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				DataSourceInfo.DATASOURCE_NAME);
		Connection con = null;
		try {
			con = dataSource.getConnection();
			for (InstallProcessor processor : installProcessors) {
				long startTime = System.currentTimeMillis();
				processSqls.put(processor.getClass(),
						processor.getDealSqls(dbLanguage, con));
				logger.logMessage(LogLevel.INFO, "processor:[{0}]的处理时间：[{1}]",
						processor.getClass().getSimpleName(),
						System.currentTimeMillis() - startTime);
			}
		} catch (Exception ex) {
			logger.errorMessage(ex.getMessage(), ex);
			throw new TinySysRuntimeException(ex);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
		return processSqls;
	}

	private void installSort() {
		Collections.sort(installProcessors, new Comparator<InstallProcessor>() {
			public int compare(InstallProcessor o1, InstallProcessor o2) {
				if (o1 != null && o2 != null) {
					return o1.getOrder() > o2.getOrder() ? 1
							: (o1.getOrder() == o2.getOrder() ? 0 : -1);
				}
				return 0;
			}
		});
	}

	public void process() {
		logger.logMessage(LogLevel.INFO, "开始进行{0}数据库安装处理", dbLanguage);
		installSort();
		installProcess();
		logger.logMessage(LogLevel.INFO, "{0}数据库安装处理结束", dbLanguage);
	}

	private void installProcess() {
		for (InstallProcessor installProcessor : installProcessors) {
			if (installProcessor != null) {
				try {
					installProcessor.process(dbLanguage);
				} catch (Exception e) {
					logger.errorMessage(
							"执行installProcessor时出现异常,processor:{0},language:{1}",
							e, installProcessor.getClass(), dbLanguage);
				}

			}
		}
	}

	public String getApplicationNodePath() {
		return "/application/database-install-processor/database-installer";
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
		if (applicationConfig == null) {
			dbLanguage = defaultLanuage;
		} else {
			XmlNode node = applicationConfig.getSubNode("database");
			dbLanguage = node.getAttribute("type");
			if (dbLanguage == null || "".equals(dbLanguage))
				dbLanguage = defaultLanuage;
		}

		logger.logMessage(LogLevel.INFO, "当前数据库语言为:{dbLanguage}", dbLanguage);
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public void start() {
		process();
	}

	public void stop() {

	}

	public void setApplication(Application application) {

	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

	public void init() {
		// TODO Auto-generated method stub

	}
}
