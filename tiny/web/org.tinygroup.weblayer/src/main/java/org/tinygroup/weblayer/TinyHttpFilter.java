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
package org.tinygroup.weblayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManager;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManagerHolder;
import org.tinygroup.weblayer.impl.WebContextImpl;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class TinyHttpFilter implements Filter {
	private static final String EXCLUDE_PATH = "excludePath";
	private static final Logger logger = LoggerFactory
			.getLogger(TinyHttpFilter.class);
	private TinyProcessorManager tinyProcessorManager;
	private TinyFilterManager tinyFilterManager;

	private List<Pattern> excludePatterns = new ArrayList<Pattern>();

	private static final String POST_DATA_PROCESS = "post-data-process";

	private static final String DATA_MAPPING = "data-mapping";

	private static final String HOST_PATTERN = "host-pattern";

	private static final String POST_DATA_KEY = "post-data-key";

	public static final String DEFAULT_POST_DATA_KEY = "$_post_data_key";

	public static final String DEFAULT_POST_NODE_NAME = "$_post_node_name";

	private Map<String, String> mapping = new HashMap<String, String>();

	private String postDataKey;

	private FilterWrapper wrapper;
	private FullContextFileRepository fullContextFileRepository;
	private static String[] defaultFiles = { "index.page", "index.htm",
			"index.html", "index.jsp" };
	public static final String DEFAULT_PAGE_KEY = "$_default_page";

	public void destroy() {
		destroyTinyProcessors();
		destroyTinyFilters();
	}

	private void destroyTinyFilters() {
		tinyFilterManager.destoryTinyResources();
	}

	/**
	 * 销毁tiny-processors
	 */
	private void destroyTinyProcessors() {
		tinyProcessorManager.destoryTinyResources();
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			requestInitListener(servletRequest);
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			String servletPath = getServletPath(request);
			if (isExcluded(servletPath)) {
				logger.logMessage(LogLevel.DEBUG, "请求路径:<{}>,被拒绝", servletPath);
				filterChain.doFilter(request, response);
				return;
			}
			WebContext context = new WebContextImpl();
			context.put("springUtil", BeanContainerFactory
					.getBeanContainer(getClass().getClassLoader()));
			postDataProcess(request, context);
			context.put("context", context);
			context.putSubContext("applicationproperties", new ContextImpl(
					ConfigurationUtil.getConfigurationManager()
							.getConfiguration()));
			putRequestInfo(request, context);
			context.init(request, response,
					ServletContextHolder.getServletContext());
			if (servletPath.endsWith("/")) {
				for (String defaultFile : defaultFiles) {
					String tmpPath = servletPath + defaultFile;
					FileObject fileObject = fullContextFileRepository
							.getFileObject(tmpPath);
					if (fileObject != null && fileObject.isExist()) {
						servletPath = tmpPath;
						request.setAttribute(DEFAULT_PAGE_KEY, servletPath);
						break;
					}
				}
			}
			TinyFilterHandler hander = new TinyFilterHandler(servletPath,
					filterChain, context, tinyFilterManager,
					tinyProcessorManager);
			if (wrapper != null) {
				wrapper.filterWrapper(context, hander);
			} else {
				hander.tinyFilterProcessor(request, response);
			}
		} finally {
			requestDestroyListener(servletRequest);//抛出异常也要保证执行
		}
	}

	private void requestInitListener(ServletRequest servletRequest) {
		TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
				.getInstance();
		List<ServletRequestListener> listeners = configManager
				.getRequestListeners();
		ServletRequestEvent event = new ServletRequestEvent(
				ServletContextHolder.getServletContext(), servletRequest);
		for (ServletRequestListener listener : listeners) {
			logger.logMessage(LogLevel.DEBUG,
					"ServletRequestListener:[{0}] will be requestInitialized",
					listener);
			listener.requestInitialized(event);
			logger.logMessage(LogLevel.DEBUG,
					"ServletRequestListener:[{0}] requestInitialized", listener);
		}
	}

	private void requestDestroyListener(ServletRequest servletRequest) {
		TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
				.getInstance();
		List<ServletRequestListener> listeners = configManager
				.getRequestListeners();
		ServletRequestEvent event = new ServletRequestEvent(
				ServletContextHolder.getServletContext(), servletRequest);
		for (ServletRequestListener listener : listeners) {
			logger.logMessage(LogLevel.DEBUG,
					"ServletRequestListener:[{0}] will be requestDestroyed",
					listener);
			listener.requestDestroyed(event);
			logger.logMessage(LogLevel.DEBUG,
					"ServletRequestListener:[{0}] requestDestroyed", listener);
		}
	}

	private String getServletPath(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		if (StringUtil.isBlank(servletPath)) {
			servletPath = request.getPathInfo();
		}
		return servletPath;
	}

	private void postDataProcess(HttpServletRequest request, WebContext context)
			throws IOException {
		if (isPostMethod(request)) {
			String remoteHost = request.getRemoteHost();
			String remoteAddr = request.getRemoteAddr();
			for (String pattern : mapping.values()) {
				if (Pattern.matches(pattern, remoteHost)
						|| Pattern.matches(pattern, remoteAddr)) {
					context.put(postDataKey,
							StreamUtil
									.readBytes(request.getInputStream(), true)
									.toByteArray());
					break;
				}
			}
		}

	}

	private boolean isPostMethod(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("post");
	}

	private void initPostDataProcess() {
		ConfigurationManager appConfigManager = ConfigurationUtil
				.getConfigurationManager();
		XmlNode parserNode = appConfigManager.getApplicationConfiguration()
				.getSubNode(POST_DATA_PROCESS);
		if (parserNode != null) {
			postDataKey = StringUtil.defaultIfBlank(
					parserNode.getAttribute(POST_DATA_KEY),
					DEFAULT_POST_DATA_KEY);
			List<XmlNode> dataMapNode = parserNode.getSubNodes(DATA_MAPPING);
			if (!CollectionUtil.isEmpty(dataMapNode)) {
				for (int i = 0; i < dataMapNode.size(); i++) {
					XmlNode xmlNode = dataMapNode.get(i);
					String hostsPattern = xmlNode.getAttribute(HOST_PATTERN);
					if (!StringUtil.isBlank(hostsPattern)) {
						String nodeName = xmlNode.getAttribute("name");
						if (StringUtil.isBlank(nodeName)) {
							nodeName = DEFAULT_POST_NODE_NAME + i;
						}
						mapping.put(nodeName, hostsPattern);
					}
				}
			}
		}
	}

	private void putRequestInfo(HttpServletRequest request, WebContext context) {
		String path = request.getContextPath();
		if (path == null) {
			path = "";
		}
		context.put(WebContextUtil.TINY_CONTEXT_PATH, path);
		context.put(WebContextUtil.TINY_REQUEST_URI, request.getRequestURI());
		String servletPath = request.getServletPath();
		if (servletPath == null || servletPath.length() == 0) {
			servletPath = request.getPathInfo();
		}
		context.put(WebContextUtil.TINY_SERVLET_PATH, servletPath);
	}

	/**
	 * 请求是否被排除
	 * 
	 * @param servletPath
	 * @return
	 */
	private boolean isExcluded(String servletPath) {
		for (Pattern pattern : excludePatterns) {
			if (pattern.matcher(servletPath).matches()) {
				return true;
			}
		}
		return false;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		logger.logMessage(LogLevel.INFO, "filter初始化开始...");
		fullContextFileRepository = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				"fullContextFileRepository");
		initExcludePattern(filterConfig);

		initTinyFilters();

		initTinyFilterWrapper();

		initTinyProcessors();

		initPostDataProcess();

		logger.logMessage(LogLevel.INFO, "filter初始化结束...");

	}

	private void initTinyFilterWrapper() {
		wrapper = tinyFilterManager.getFilterWrapper();
	}

	private void initExcludePattern(FilterConfig filterConfig) {
		excludePatterns.clear();// 先清空
		String excludePath = filterConfig.getInitParameter(EXCLUDE_PATH);
		if (excludePath != null) {
			String[] excludeArray = excludePath.split(",");
			for (String path : excludeArray) {
				excludePatterns.add(Pattern.compile(path));
			}
		}
	}

	private void initTinyFilters() throws ServletException {
		tinyFilterManager = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				TinyFilterManager.TINY_FILTER_MANAGER);
		tinyFilterManager.initTinyResources();
	}

	/**
	 * tiny-processors初始化
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	private void initTinyProcessors() throws ServletException {
		tinyProcessorManager = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				TinyProcessorManager.TINY_PROCESSOR_MANAGER);
		tinyProcessorManager.initTinyResources();
	}
}
