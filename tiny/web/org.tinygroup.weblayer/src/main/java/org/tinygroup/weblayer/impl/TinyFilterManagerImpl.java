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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.springframework.web.util.NestedServletException;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.order.OrderUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.FilterWrapper;
import org.tinygroup.weblayer.TinyFilter;
import org.tinygroup.weblayer.TinyFilterConfig;
import org.tinygroup.weblayer.TinyFilterManager;
import org.tinygroup.weblayer.config.TinyFilterConfigInfo;
import org.tinygroup.weblayer.config.TinyWrapperFilterConfigInfo;
import org.tinygroup.weblayer.configmanager.TinyFilterConfigManager;

/**
 * tiny-filter的管理类
 * 
 * @author renhui
 * 
 */
public class TinyFilterManagerImpl implements TinyFilterManager {

	private TinyFilterConfigManager configManager;

	private static Logger logger = LoggerFactory
			.getLogger(TinyFilterManagerImpl.class);

	private List<TinyFilter> tinyFilters = new ArrayList<TinyFilter>();

	private Map<String, TinyFilterConfig> filterConfigMap = new HashMap<String, TinyFilterConfig>();

	private TinyFilterWrapper wrapper;

	private static final String SPLIT_CHAR = ",";

	public List<TinyFilter> getTinyFiltersWithUrl(String url) {
		List<TinyFilter> filters = new ArrayList<TinyFilter>();
		for (TinyFilter tinyFilter : tinyFilters) {
			if (tinyFilter.isMatch(url)) {
				filters.add(tinyFilter);
			}
		}
		return filters;
	}

	public void initTinyResources() throws ServletException {
		reset();
		Throwable failureCause = null;
		try {
			addWrapperFilter();
			addTinyFilter();
			initWrapperFilter();
			// 对tiny-filter列表进行排序
			OrderUtil.order(tinyFilters);
			initTinyFilter();
		} catch (ServletException ex) {
			failureCause = ex;
			throw ex;
		} catch (Throwable ex) {
			failureCause = ex;
			throw new NestedServletException("filter init processing failed",
					ex);
		} finally {
			if (failureCause != null) {
				logger.errorMessage("Could not init filter", failureCause);
			} else {
				logger.logMessage(LogLevel.DEBUG,
						"Successfully completed filter init");
			}
		}

	}

	/**
	 * 重设环境
	 */
	private void reset() {
		tinyFilters = new ArrayList<TinyFilter>();// 先清空
		wrapper = new TinyFilterWrapper(configManager, this);
		filterConfigMap = new HashMap<String, TinyFilterConfig>();
	}

	private void initWrapperFilter() throws ServletException {
		wrapper.init();
	}

	private void initTinyFilter() throws ServletException {
		for (TinyFilter tinyFilter : tinyFilters) {
			logger.logMessage(LogLevel.DEBUG,
					"tiny filter name:[{0}] start init",
					tinyFilter.getFilterName());
			TinyFilterConfigInfo configInfo = configManager
					.getFilterConfig(tinyFilter.getFilterName());
			tinyFilter.initTinyFilter(new DefaultTinyFilterConfig(configInfo));
			logger.logMessage(LogLevel.DEBUG,
					"tiny filter name:[{0}] init end",
					tinyFilter.getFilterName());
		}
	}

	private void addTinyFilter(){
		List<TinyFilterConfigInfo> filterConfigs = configManager
				.getFilterConfigs();
		for (TinyFilterConfigInfo filterConfig : filterConfigs) {
			tinyFilters.add(createTinyFilter(filterConfig));
			filterConfigMap.put(filterConfig.getConfigName(),
					new DefaultTinyFilterConfig(filterConfig));
		}
	}

	private void addWrapperFilter() {
		List<TinyWrapperFilterConfigInfo> wrapperConfigs = configManager
				.getWrapperFilterConfigs();
		for (TinyWrapperFilterConfigInfo wrapperConfig : wrapperConfigs) {
			String filterBeanName = wrapperConfig.getFilterBeanName();
			String[] beanNameArray = filterBeanName.split(SPLIT_CHAR);
			for (String beanName : beanNameArray) {
				Filter filter = BeanContainerFactory.getBeanContainer(
						this.getClass().getClassLoader()).getBean(beanName);
				wrapper.addHttpFilter(wrapperConfig.getConfigName(), filter);
				filterConfigMap.put(wrapperConfig.getConfigName(),
						new DefaultTinyFilterConfig(wrapperConfig));
			}
		}
	}

	private TinyFilter createTinyFilter(TinyFilterConfigInfo filterConfigInfo){
		String filterName = filterConfigInfo.getConfigName();
		logger.logMessage(LogLevel.INFO, "tiny-filter:{}开始被实例化", filterName);
		TinyFilter filter = instanceFilter(filterConfigInfo.getConfigBeanName());
		filter.setFilterName(filterName);
		logger.logMessage(LogLevel.INFO, "tiny-filter:{}实例化结束", filterName);
		return filter;
	}

	private TinyFilter instanceFilter(String beanName){
		TinyFilter filter = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(beanName);
		return filter;
	}

	public void destoryTinyResources() {
		for (TinyFilter tinyFilter : tinyFilters) {
			tinyFilter.destroyTinyFilter();
		}
		tinyFilters = null;
		wrapper = null;
	}

	public void setConfigManager(TinyFilterConfigManager configManager) {
		this.configManager = configManager;
	}

	public boolean existFilterWrapper() {
		return wrapper != null;
	}

	public FilterWrapper getFilterWrapper() {
		return wrapper;
	}

	public TinyFilterConfig getTinyFilterConfig(String filterBeanName) {
		return filterConfigMap.get(filterBeanName);
	}

}
