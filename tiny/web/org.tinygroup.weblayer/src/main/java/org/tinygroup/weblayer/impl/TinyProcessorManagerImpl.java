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
package org.tinygroup.weblayer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.web.util.NestedServletException;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.TinyProcessor;
import org.tinygroup.weblayer.TinyProcessorConfig;
import org.tinygroup.weblayer.TinyProcessorManager;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.config.TinyProcessorConfigInfo;
import org.tinygroup.weblayer.configmanager.TinyProcessorConfigManager;

/**
 * tiny servlet处理器管理接口的默认实现
 * 
 * @author renhui
 * 
 */
public class TinyProcessorManagerImpl implements TinyProcessorManager {

	private List<TinyProcessor> tinyProcessorList = new ArrayList<TinyProcessor>();

	private Map<String, TinyProcessorConfig> processorMap = new HashMap<String, TinyProcessorConfig>();

	private TinyProcessorConfigManager configManager;

	private static Logger logger = LoggerFactory
			.getLogger(TinyProcessorManagerImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.weblayer.impl.TinyProcessorManager#execute(java
	 * .lang.String, org.tinygroup.weblayer.WebContext)
	 */
	public boolean execute(String url, WebContext context)
			throws ServletException, IOException {
		boolean canExecute = false;
		for (TinyProcessor tinyProcessor : tinyProcessorList) {
			if (tinyProcessor.isMatch(url)) {
				canExecute = true;
				tinyProcessor.process(url, context);
				break;
			}
		}
		return canExecute;
	}

	public void initTinyResources() throws ServletException {
		reset();
		Throwable failureCause = null;
		try {
			addTinyProcessor();
			initTinyProcessor();
		} catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		} catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("filter init processing failed",
					ex);
		} finally {
			if (failureCause != null) {
				logger.errorMessage("Could not init processor", failureCause);
			} else {
				logger.logMessage(LogLevel.DEBUG,
						"Successfully completed processor init");
			}
		}
	}

	private void initTinyProcessor() throws ServletException {
		for (TinyProcessor tinyProcessor : tinyProcessorList) {
			logger.logMessage(LogLevel.DEBUG,
					"tiny processor name:[{0}] start init",
					tinyProcessor.getProcessorName());
			tinyProcessor.init(processorMap.get(tinyProcessor
					.getProcessorName()));
			logger.logMessage(LogLevel.DEBUG,
					"tiny processor name:[{0}] init end",
					tinyProcessor.getProcessorName());
		}

	}

	private void addTinyProcessor() {
		List<TinyProcessorConfigInfo> processorConfigs = configManager
				.getProcessorConfigs();
		for (TinyProcessorConfigInfo processorConfig : processorConfigs) {
			tinyProcessorList.add(createTinyProcessor(processorConfig));
			processorMap.put(processorConfig.getConfigName(),
					new DefaultTinyProcessorConfig(processorConfig));
		}

	}

	private TinyProcessor createTinyProcessor(
			TinyProcessorConfigInfo processorConfigInfo) {
		String processorName = processorConfigInfo.getConfigName();
		logger.logMessage(LogLevel.INFO, "tiny-processor:{}开始被实例化",
				processorName);
		TinyProcessor processor = instanceProcessor(processorConfigInfo
				.getConfigBeanName());
		processor.setProcessorName(processorName);
		logger.logMessage(LogLevel.INFO, "tiny-filter:{}实例化结束", processorName);
		return processor;
	}

	private TinyProcessor instanceProcessor(String beanName) {
		TinyProcessor processor = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(beanName);
		return processor;
	}

	public void reset() {
		tinyProcessorList = new ArrayList<TinyProcessor>();
		processorMap = new HashMap<String, TinyProcessorConfig>();
	}

	public void setConfigManager(TinyProcessorConfigManager configManager) {
		this.configManager = configManager;
	}

	public void destoryTinyResources() {
		for (TinyProcessor tinyProcessor : tinyProcessorList) {
			tinyProcessor.destroy();
		}
		tinyProcessorList = null;
		processorMap = null;
	}

}
