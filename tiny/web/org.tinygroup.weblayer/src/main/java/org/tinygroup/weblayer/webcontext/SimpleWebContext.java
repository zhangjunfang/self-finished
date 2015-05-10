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
package org.tinygroup.weblayer.webcontext;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.TinyFilterHandler;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManager;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManagerHolder;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteWebContext;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

/**
 * 
 * 功能说明: 实现了<code>WebContext</code>接口，包含request、response和servletContext的信息。
 * <p>
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-5-7 <br>
 * <br>
 */
public class SimpleWebContext extends CommitMonitor implements WebContext {

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleWebContext.class);

	/**
	 * 创建一个新的<code>RequestContext</code>对象。
	 * 
	 * @param webContext
	 *            当前请求所在的<code>ServletContext</code>
	 * @param request
	 *            <code>HttpServletRequest</code>对象
	 * @param response
	 *            <code>HttpServletResponse</code>对象
	 */
	public SimpleWebContext(WebContext webContext, TinyFilterHandler handler,
			HttpServletRequest request, HttpServletResponse response) {
		super(webContext, handler);
		setRequest(new RequestWrapper(request));
		setResponse(new CommittingAwareResponse(response, this));
	}

	/** 开始一个请求。 */
	public void prepare() {
	}

	/** 结束一个请求。 */
	public void commit() {
	}

	/**
	 * 显示当前<code>RequestContext</code>的内容。
	 * 
	 * @return 字符串表示
	 */

	public String toString() {
		MapBuilder mb = new MapBuilder();

		mb.append("request", getRequest());
		mb.append("response", getResponse());
		mb.append("webapp", getServletContext());

		return new ToStringBuilder().append(getClass().getSimpleName())
				.append(mb).toString();
	}

	private class RequestWrapper extends AbstractRequestWrapper {

		public RequestWrapper(HttpServletRequest request) {
			super(SimpleWebContext.this, request);
		}

		public Object getAttribute(String name) {
			Object object = super.getAttribute(name);
			if (object == null) {
				WebContext topWebContext = getTopWebContext();
				if (topWebContext != null) {
					object = getFromWrapperContext(name, getTopWebContext());
				}
			}
			return object;
		}

		public Enumeration getAttributeNames() {
			return super.getAttributeNames();
		}

		public void setAttribute(String name, Object value) {
			Object oldValue = getAttribute(name);
			super.setAttribute(name, value);
			setAttributeListener(name, value,oldValue);
		}

		private void setAttributeListener(String name, Object newValue,Object oldValue) {
			TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
					.getInstance();
			List<ServletRequestAttributeListener> listeners = configManager
					.getRequestAttributeListeners();
			ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(
					ServletContextHolder.getServletContext(), this, name, newValue);
			if (oldValue == null) {
				for (ServletRequestAttributeListener listener : listeners) {
					logger.logMessage(
							LogLevel.DEBUG,
							"ServletRequestAttributeListener:[{0}] will be attributeAdded",
							listener);
					listener.attributeAdded(event);
					logger.logMessage(
							LogLevel.DEBUG,
							"ServletRequestAttributeListener:[{0}] attributeAdded",
							listener);
				}

			} else {
				for (ServletRequestAttributeListener listener : listeners) {
					logger.logMessage(
							LogLevel.DEBUG,
							"ServletRequestAttributeListener:[{0}] will be attributeReplaced,the oldValue:[{1}]",
							listener,oldValue);
					listener.attributeReplaced(event);
					logger.logMessage(
							LogLevel.DEBUG,
							"ServletRequestAttributeListener:[{0}] attributeReplaced",
							listener);
				}
			}
		}

		public void removeAttribute(String name) {
			super.removeAttribute(name);
			removeAttributeListener(name);
		}

		private void removeAttributeListener(String name) {
			TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
					.getInstance();
			List<ServletRequestAttributeListener> listeners = configManager
					.getRequestAttributeListeners();
			ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(
					ServletContextHolder.getServletContext(), this, name, null);
			for (ServletRequestAttributeListener listener : listeners) {
				logger.logMessage(
						LogLevel.DEBUG,
						"ServletRequestAttributeListener:[{0}] will be attributeRemoved",
						listener);
				listener.attributeRemoved(event);
				logger.logMessage(
						LogLevel.DEBUG,
						"ServletRequestAttributeListener:[{0}] attributeRemoved",
						listener);
			}
		}

		public RequestDispatcher getRequestDispatcher(String path) {
			RewriteWebContext webContext = WebContextUtil.findWebContext(
					(HttpServletRequest) getRequest(), RewriteWebContext.class);
			webContext.setPath(StringUtil.substringBefore(path, "?"));//去除带？后面的字符串
			return super.getRequestDispatcher(path);
		}

	}

}
