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
package org.tinygroup.weblayer.listener;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.Enumerator;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManager;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManagerHolder;

/**
 * tiny框架 ServletContext实现
 * 
 * @author renhui
 * 
 */
public class TinyServletContext implements ServletContext {

	private ServletContext originalContext;

	private FullContextFileRepository fullContextFileRepository;

	private Map<String, String> parameters = CollectionUtil.createHashMap();

	private TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
			.getInstance();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TinyServletContext.class);

	public TinyServletContext(ServletContext servletContext) {
		Assert.assertNotNull(servletContext, "servletContext must not null");
		this.originalContext = servletContext;
	}

	public String getContextPath() {
		return originalContext.getContextPath();
	}

	public ServletContext getContext(String uripath) {
		return originalContext.getContext(uripath);
	}

	public int getMajorVersion() {
		return originalContext.getMajorVersion();
	}

	public int getMinorVersion() {
		return originalContext.getMinorVersion();
	}

	public String getMimeType(String file) {
		return originalContext.getMimeType(file);
	}

	public Set getResourcePaths(String path) {
		return originalContext.getResourcePaths(path);
	}

	public URL getResource(String path) throws MalformedURLException {

		if (fullContextFileRepository != null) {
			FileObject fileObject = fullContextFileRepository
					.getFileObject(path);
			if (fileObject != null && fileObject.isExist()) {
				return fileObject.getURL();
			}
		}
		return originalContext.getResource(path);
	}

	public InputStream getResourceAsStream(String path) {
		if (fullContextFileRepository != null) {
			FileObject fileObject = fullContextFileRepository
					.getFileObject(path);
			if (fileObject != null && fileObject.isExist()) {
				return fileObject.getInputStream();
			}
		}
		return originalContext.getResourceAsStream(path);
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return originalContext.getRequestDispatcher(path);
	}

	public RequestDispatcher getNamedDispatcher(String name) {
		return originalContext.getNamedDispatcher(name);
	}

	@Deprecated
	public Servlet getServlet(String name) throws ServletException {
		return originalContext.getServlet(name);
	}

	@Deprecated
	public Enumeration getServlets() {
		return originalContext.getServlets();
	}

	public Enumeration getServletNames() {
		return originalContext.getServletNames();
	}

	public void log(String msg) {
		originalContext.log(msg);
	}

	@Deprecated
	public void log(Exception exception, String msg) {
		originalContext.log(exception, msg);

	}

	public void log(String message, Throwable throwable) {
		originalContext.log(message, throwable);

	}

	public String getRealPath(String path) {
		if (fullContextFileRepository != null) {
			FileObject fileObject = fullContextFileRepository
					.getFileObject(path);
			if (fileObject != null && fileObject.isExist()) {
				return fileObject.getAbsolutePath();
			}
		}
		return originalContext.getRealPath(path);
	}

	public String getServerInfo() {
		return originalContext.getServerInfo();
	}

	public String getInitParameter(String name) {
		String value = parameters.get(name);
		if (value == null) {
			value = originalContext.getInitParameter(name);
		}
		return value;
	}

	public Enumeration getInitParameterNames() {
		Enumeration enumeration = originalContext.getInitParameterNames();
		Set<String> parameterSet = parameters.keySet();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			parameterSet.add(name);
		}
		return new Enumerator(parameterSet);
	}

	public Object getAttribute(String name) {
		return originalContext.getAttribute(name);
	}

	public Enumeration getAttributeNames() {
		return originalContext.getAttributeNames();
	}

	public void setAttribute(String name, Object object) {
		Object oldValue = getAttribute(name);
		originalContext.setAttribute(name, object);// 如果有注册到web.xml的，也会触发ServletContextAttributeListener
		setAttributeListener(name, object,oldValue);
	}

	private void setAttributeListener(String name, Object object,Object oldValue) {
		List<ServletContextAttributeListener> contextAttributeListeners = configManager
				.getContextAttributeListeners();
		ServletContextAttributeEvent event = new ServletContextAttributeEvent(
				this, name, object);
		if (oldValue == null) {
			for (ServletContextAttributeListener listener : contextAttributeListeners) {
				LOGGER.logMessage(
						LogLevel.DEBUG,
						"ServletContextAttributeListener:[{0}] will be attributeAdded",
						listener);
				listener.attributeAdded(event);
				LOGGER.logMessage(LogLevel.DEBUG,
						"ServletContextAttributeListener:[{0}] attributeAdded",
						listener);
			}
		} else {
			for (ServletContextAttributeListener listener : contextAttributeListeners) {
				LOGGER.logMessage(
						LogLevel.DEBUG,
						"ServletContextAttributeListener:[{0}] will be attributeReplaced,the oldValue:[{1}]",
						listener,oldValue);
				listener.attributeReplaced(event);
				LOGGER.logMessage(
						LogLevel.DEBUG,
						"ServletContextAttributeListener:[{0}] attributeReplaced",
						listener);
			}
		}
	}

	public void removeAttribute(String name) {
		originalContext.removeAttribute(name);
		removeAttributeListener(name);
	}

	private void removeAttributeListener(String name) {
		List<ServletContextAttributeListener> contextAttributeListeners = configManager
				.getContextAttributeListeners();
		ServletContextAttributeEvent event = new ServletContextAttributeEvent(
				this, name, null);
		for (ServletContextAttributeListener listener : contextAttributeListeners) {
			LOGGER.logMessage(
					LogLevel.DEBUG,
					"ServletContextAttributeListener:[{0}] will be attributeRemoved",
					listener);
			listener.attributeRemoved(event);
			LOGGER.logMessage(LogLevel.DEBUG,
					"ServletContextAttributeListener:[{0}] attributeRemoved",
					listener);
		}
	}

	public String getServletContextName() {
		return originalContext.getServletContextName();
	}

	public void setInitParameter(String name, String value) {
		parameters.put(name, value);
	}

	public void setFullContextFileRepository(
			FullContextFileRepository fullContextFileRepository) {
		this.fullContextFileRepository = fullContextFileRepository;
	}

}
