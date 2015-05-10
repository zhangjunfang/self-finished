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
package org.tinygroup.cepcorenetty;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public class NettyCepCoreProcessor implements ApplicationProcessor {
	private static final String CEP_CONFIG_PATH = "/application/cep-configuration";
	private static final String OPERATOR_TAG = "operator";
	private static final String OPERATOR_ATTRIBUTE = "name";
	private static final String NODE_NAME = "node-name";
	private static Logger logger = LoggerFactory
			.getLogger(NettyCepCoreProcessor.class);

	private CEPCore cepcore ;
	
	private XmlNode appConfig;

	

	public CEPCore getCepcore() {
		return cepcore;
	}

	public void setCepcore(CEPCore cepcore) {
		this.cepcore = cepcore;
	}

	public String getApplicationNodePath() {
		return CEP_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.appConfig = applicationConfig;
	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return appConfig;
	}

	public int getOrder() {
		return 0;
	}

	public void start() {
		logger.logMessage(LogLevel.INFO, "开始启动CEPCoreProcessor");
		if (appConfig == null) {
			logger.logMessage(LogLevel.INFO, "配置为空，启动CEPCoreProcessor完毕");
			return;
		}
		String operatorName = appConfig.getSubNode(OPERATOR_TAG).getAttribute(
				OPERATOR_ATTRIBUTE);
		CEPCoreOperator operator = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(operatorName);
		if (operator == null) {
			return;
		}
		String nodeName = appConfig.getAttribute(NODE_NAME);
		logger.logMessage(LogLevel.INFO, "NodeName为:{0}",nodeName);
		
//		CEPCore cepcore = BeanContainerFactory.getBeanContainer(
//				this.getClass().getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
		cepcore.setOperator(operator);
		cepcore.setNodeName(nodeName);
		cepcore.start();
		cepcore.addEventProcessorRegisterTrigger(new ArReRegisterTrigger());
		logger.logMessage(LogLevel.INFO, "启动CEPCoreProcessor完毕");

	}

	public void init() {

	}

	public void stop() {
		logger.logMessage(LogLevel.INFO, "开始关闭CEPCoreProcessor");
		CEPCore core = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
		core.stop();
		logger.logMessage(LogLevel.INFO, "关闭CEPCoreProcessor完毕");
	}

	public void setApplication(Application application) {
		
	}

}
