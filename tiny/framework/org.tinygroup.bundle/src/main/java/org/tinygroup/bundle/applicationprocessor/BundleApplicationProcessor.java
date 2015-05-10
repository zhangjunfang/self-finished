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
package org.tinygroup.bundle.applicationprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public class BundleApplicationProcessor implements ApplicationProcessor {
	private BundleManager bundleManager;
	private XmlNode appConfig;
	private static final String BUNDLE_CONFIG_PATH = "/application/bundle-configuration";
	private static final String PATH_TAG = "path";
	private static final String COMMON_ROOT = "common-root";
	private static final String BUNDLE_ROOT = "bundle-root";
	private static Logger logger = LoggerFactory
			.getLogger(BundleApplicationProcessor.class);

	public BundleManager getBundleManager() {
		return bundleManager;
	}

	public void setBundleManager(BundleManager bundleManager) {
		this.bundleManager = bundleManager;
	}

	public String getApplicationNodePath() {
		return BUNDLE_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		appConfig = applicationConfig;
	}

	public XmlNode getComponentConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public XmlNode getApplicationConfig() {
		return appConfig;
	}

	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void start() {
		logger.logMessage(LogLevel.INFO, "开始启动BundleApplicationProcessor");
		if (appConfig != null) {
			String commonRoot = appConfig.getSubNode(PATH_TAG).getAttribute(
					COMMON_ROOT);
			bundleManager.setCommonRoot(commonRoot);
			logger.logMessage(LogLevel.INFO, "配置的公共包置路径为:{}", commonRoot);
			String bundleRoot = appConfig.getSubNode(PATH_TAG).getAttribute(
					BUNDLE_ROOT);
			bundleManager.setBundleRoot(bundleRoot);
			logger.logMessage(LogLevel.INFO, "配置的Bundle放置路径为:{}", bundleRoot);
		}
		if (StringUtil.isBlank(bundleManager.getBundleRoot())) {
			bundleManager.setBundleRoot(System.getProperty("user.dir"));
		}
		if (StringUtil.isBlank(bundleManager.getCommonRoot())) {
			bundleManager.setCommonRoot(System.getProperty("user.dir"));
		}
		logger.logMessage(LogLevel.INFO, "Bundle放置路径为:{}",
				bundleManager.getBundleRoot());
		logger.logMessage(LogLevel.INFO, "公共包路径为:{}",
				bundleManager.getCommonRoot());
		logger.logMessage(LogLevel.INFO, "启动BundleApplicationProcessor完毕");
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void setApplication(Application application) {
		// TODO Auto-generated method stub

	}

}
