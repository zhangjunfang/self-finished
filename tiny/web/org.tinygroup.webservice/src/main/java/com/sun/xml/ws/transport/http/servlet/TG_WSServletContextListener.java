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
package com.sun.xml.ws.transport.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.WebServiceException;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.listener.TinyListenerProcessor;
import org.tinygroup.webservice.util.WebserviceUtil;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlParser;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.resources.WsservletMessages;
import com.sun.xml.ws.transport.http.TG_DeploymentDescriptorParser;

public class TG_WSServletContextListener implements
		ServletContextAttributeListener, ServletContextListener {
	CEPCore core;

	public CEPCore getCore() {
		return core;
	}

	public void setCore(CEPCore core) {
		this.core = core;
	}

	private TG_WSServletDelegate delegate;
	private List<TG_ServletAdapter> adapters;
	private final TG_JAXWSRIDeploymentProbeProvider probe = new TG_JAXWSRIDeploymentProbeProvider();

	public void attributeAdded(ServletContextAttributeEvent event) {
	}

	public void attributeRemoved(ServletContextAttributeEvent event) {
	}

	public void attributeReplaced(ServletContextAttributeEvent event) {
	}

	public void contextDestroyed(ServletContextEvent event) {
		if (delegate != null) { // the deployment might have failed.
			delegate.destroy();
		}

		if (adapters != null) {

			for (TG_ServletAdapter a : adapters) {
				try {
					a.getEndpoint().dispose();
				} catch (Throwable e) {
					logger.errorMessage(e.getMessage(), e);
				}

				// Emit undeployment probe event for each endpoint
				probe.undeploy(a);
			}
		}

		// if (logger.isLoggable(Level.INFO)) {
		logger.logMessage(LogLevel.INFO,
				WsservletMessages.LISTENER_INFO_DESTROY());
		// }
	}

	private final static String PAST_TAG = "past-pattern";
	private final static String SKIP_TAG = "skip-pattern";
	private final static String SKIP_ATTRIBUTE = "pattern";
	private final static String PAST_ATTRIBUTE = "pattern";
	private Map<String, Pattern> skipPathPatternMap = new HashMap<String, Pattern>();
	private Map<String, Pattern> pastPathPatternMap = new HashMap<String, Pattern>();

	private List<ServiceInfo> getPublishService(String linsenterConfig) {

		if (linsenterConfig == null || "".equals(linsenterConfig)) {
			return new ArrayList<ServiceInfo>();
		}
		XmlParser<String> parser = new XmlStringParser();
		XmlNode xmlNode = parser.parse(linsenterConfig).getRoot();
		addSkipPathPattern(xmlNode);
		addPastPathPattern(xmlNode);
		core = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
		List<ServiceInfo> serviceInfos = core.getServiceInfos();
		List<ServiceInfo> publishList = new ArrayList<ServiceInfo>();
		for (ServiceInfo serviceInfo : serviceInfos) {
			String serviceId = serviceInfo.getServiceId();
			if (!isSkip(serviceId) && isPast(serviceId)) {
				publishList.add(serviceInfo);
			}
		}
		return publishList;
	}

	private void addSkipPathPattern(XmlNode xmlNode) {
		List<XmlNode> skipTags = xmlNode.getSubNodes(SKIP_TAG);
		if(skipTags==null){
			return;
		}
		for (XmlNode tag : skipTags) {
			String pattern = tag.getAttribute(SKIP_ATTRIBUTE);
			addSkipPathPattern(pattern);
		}
	}
	public void addSkipPathPattern(String pattern) {
		skipPathPatternMap.put(pattern, Pattern.compile(pattern));
	}

	private void addPastPathPattern(XmlNode xmlNode) {
		List<XmlNode> pastTags = xmlNode.getSubNodes(PAST_TAG);
		if(pastTags==null){
			return;
		}
		for (XmlNode tag : pastTags) {
			String pattern = tag.getAttribute(PAST_ATTRIBUTE);
			addPastPathPattern(pattern);
		}
	}
	public void addPastPathPattern(String pattern) {
		pastPathPatternMap.put(pattern, Pattern.compile(pattern));
	}

	boolean isSkip(String serviceId) {
		for (String patternString : skipPathPatternMap.keySet()) {
			Pattern pattern = skipPathPatternMap.get(patternString);
			Matcher matcher = pattern.matcher(serviceId);
			if (matcher.find()) {
				logger.logMessage(LogLevel.INFO, "服务<{}>由于匹配了忽略正则表达式<{}>而被忽略。",
						serviceId, patternString);
				return true;
			}
		}
		return false;
	}

	boolean isPast(String serviceId) {
		for (String patternString : pastPathPatternMap.keySet()) {
			Pattern pattern = pastPathPatternMap.get(patternString);
			Matcher matcher = pattern.matcher(serviceId);
			if (matcher.find()) {
				return true;
			}
		}
		logger.logMessage(LogLevel.INFO, "服务<{}>由于不匹配所有的Past正则表达式而被忽略。",
				serviceId);
		return false;
	}

	public void contextInitialized(ServletContextEvent event) {

		logger.logMessage(LogLevel.INFO,
				WsservletMessages.LISTENER_INFO_INITIALIZE());

		ServletContext context = event.getServletContext();
		String linsenterConfig = context
				.getInitParameter(TinyListenerProcessor.LISTENER_NODE_CONFIG);

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			classLoader = getClass().getClassLoader();
		}
		List<ServiceInfo> serviceInfos = getPublishService(linsenterConfig);
		for (ServiceInfo serviceInfo : serviceInfos) {
			try {
				logger.logMessage(LogLevel.INFO,
						"开始发布服务" + serviceInfo.getServiceId());
				createService(serviceInfo, classLoader, context);
				logger.logMessage(LogLevel.INFO,
						"发布服务" + serviceInfo.getServiceId() + "成功");
			} catch (Throwable e) {
				logger.errorMessage(
						WsservletMessages.LISTENER_PARSING_FAILED(e), e);
				// logger.logMessage(LogLevel.ERROR,
				// "发布服务" + serviceInfo.getServiceId() +
				// "失败"+WsservletMessages.LISTENER_PARSING_FAILED(e));

			}
		}
		if(adapters==null){
			adapters=new ArrayList<TG_ServletAdapter>();
		}
		delegate = createDelegate(adapters, context);
		context.setAttribute(TG_WSServlet.JAXWS_RI_RUNTIME_INFO, delegate);

		// Emit deployment probe event for each endpoint
		for (TG_ServletAdapter adapter : adapters) {
			probe.deploy(adapter);
		}

	}

	private void createService(ServiceInfo serviceInfo,
			ClassLoader classLoader, ServletContext context) throws IOException {
		WebserviceUtil.genWSDL(serviceInfo);
		// Parse the descriptor file and build endpoint infos
		TG_DeploymentDescriptorParser<TG_ServletAdapter> parser = new TG_DeploymentDescriptorParser<TG_ServletAdapter>(
				classLoader, new ServletResourceLoader(context),
				createContainer(context), new TG_ServletAdapterList(context));

		/**
		 * 将serviceInfos 写成 JAXWS_RI_RUNTIME
		 * 
		 * */
		URL sunJaxWsXml = context.getResource(JAXWS_RI_RUNTIME);
		if (sunJaxWsXml == null)
			throw new WebServiceException(
					WsservletMessages.NO_SUNJAXWS_XML(JAXWS_RI_RUNTIME));
		InputStream is = WebserviceUtil.getXmlInputStream(serviceInfo);
		if (adapters == null) {
			adapters = parser.parse(sunJaxWsXml.toExternalForm(), is);
		} else {
			adapters.addAll(parser.parse(sunJaxWsXml.toExternalForm(), is));
		}
	}

	/**
	 * Creates {@link Container} implementation that hosts the JAX-WS endpoint.
	 */
	protected @NotNull
	Container createContainer(ServletContext context) {
		return new ServletContainer(context);
	}

	/**
	 * Creates {@link WSServletDelegate} that does the real work.
	 */
	protected @NotNull
	TG_WSServletDelegate createDelegate(List<TG_ServletAdapter> adapters,
			ServletContext context) {
		return new TG_WSServletDelegate(adapters, context);
	}

	private static final String JAXWS_RI_RUNTIME = "/WEB-INF/sun-jaxws.xml";

	private static final Logger logger = LoggerFactory
			.getLogger(com.sun.xml.ws.util.Constants.LoggingDomain
					+ ".server.http");
}
