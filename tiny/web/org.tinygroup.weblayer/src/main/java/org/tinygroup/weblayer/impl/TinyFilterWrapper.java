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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.FilterWrapper;
import org.tinygroup.weblayer.TinyFilterConfig;
import org.tinygroup.weblayer.TinyFilterHandler;
import org.tinygroup.weblayer.TinyFilterManager;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.config.TinyFilterConfigInfo;
import org.tinygroup.weblayer.configmanager.TinyFilterConfigManager;

/**
 * tiny-filter的包装类
 * 
 * @author renhui
 * 
 */
public class TinyFilterWrapper implements FilterWrapper {

	private List<Filter> filters = new ArrayList<Filter>();

	private Map<String, Filter> filterMap = new TreeMap<String, Filter>();

	private TinyFilterConfigManager tinyFilterConfigManager;

	private TinyFilterManager tinyFilterManager;

	public TinyFilterWrapper(TinyFilterConfigManager tinyFilterConfigManager,
			TinyFilterManager tinyFilterManager) {
		super();
		this.tinyFilterConfigManager = tinyFilterConfigManager;
		this.tinyFilterManager = tinyFilterManager;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(TinyFilterWrapper.class);

	public void filterWrapper(WebContext context, TinyFilterHandler hander)
			throws IOException, ServletException {
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		String servletPath = hander.getServletPath();
		List<Filter> matchFilters = getMatchFilters(servletPath);
		logger.logMessage(LogLevel.DEBUG,
				"the wrapper httpfilters for the requset path:[{0}] is [{1}]",
				servletPath, matchFilters);
		TinyFilterChain filterChain = new TinyFilterChain(matchFilters, hander);
		filterChain.doFilter(request, response);
	}

	private List<Filter> getMatchFilters(String servletPath) {
		List<Filter> matchFilters = new ArrayList<Filter>();
		for (String filterName : filterMap.keySet()) {
			TinyFilterConfig filterConfig = tinyFilterManager
					.getTinyFilterConfig(filterName);
			if (filterConfig.isMatch(servletPath)) {
				matchFilters.add(filterMap.get(filterName));
			}
		}
		return matchFilters;
	}

	public void addHttpFilter(String filterName, Filter filter) {
		filters.add(filter);
		filterMap.put(filterName, filter);
	}

	public void init() throws ServletException {
		logger.logMessage(
				LogLevel.DEBUG,
				"TinyFilterWrapper start initialization wrapper httpfilter:[{0}]",
				filters);
		for (String filterName : filterMap.keySet()) {
			Filter filter = filterMap.get(filterName);
			TinyFilterConfigInfo filterConfigInfo = tinyFilterConfigManager
					.getFilterConfig(filterName);
			filter.init(new TinyWrapperFilterConfig(filterConfigInfo));
		}
		logger.logMessage(LogLevel.DEBUG,
				"TinyFilterWrapper initialization end");
	}

	public void destroy() {
		filters = null;
		filterMap = null;
	}

}
