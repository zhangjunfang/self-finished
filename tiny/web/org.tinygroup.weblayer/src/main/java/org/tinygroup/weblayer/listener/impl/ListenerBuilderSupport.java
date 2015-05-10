package org.tinygroup.weblayer.listener.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionListener;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.weblayer.BasicTinyConfigAware;
import org.tinygroup.weblayer.config.BasicConfigInfo;
import org.tinygroup.weblayer.impl.SimpleBasicTinyConfig;
import org.tinygroup.weblayer.listener.ListenerInstanceBuilder;

public class ListenerBuilderSupport {

	@SuppressWarnings("rawtypes")
	private List<ListenerInstanceBuilder> builders = new ArrayList<ListenerInstanceBuilder>();
	private ListenerInstanceBuilder<ServletContextListener> contextListenerBuilder = new ServletContextListenerBuilder();
	private ListenerInstanceBuilder<ServletContextAttributeListener> contextAttributeListenerBuilder = new ServletContextAttributeListenerBuilder();
	private ListenerInstanceBuilder<HttpSessionListener> sessionListenerBuilder = new SessionListenerBuilder();
	private ListenerInstanceBuilder<HttpSessionBindingListener> sessionBindingListenerBuilder = new SessionBindingListenerBuilder();
	private ListenerInstanceBuilder<HttpSessionAttributeListener> sessionAttributeListenerBuilder = new SessionAttributeListenerBuilder();
	private ListenerInstanceBuilder<HttpSessionActivationListener> sessionActivationListenerBuilder = new SessionActivationListenerBuilder();
	private ListenerInstanceBuilder<ServletRequestListener> requestListenerBuilder = new RequestListenerBuilder();
	private ListenerInstanceBuilder<ServletRequestAttributeListener> requestAttributeListenerBuilder = new RequestAttributeListenerBuilder();

	public ListenerBuilderSupport() {
		builders.add(contextListenerBuilder);
		builders.add(contextAttributeListenerBuilder);
		builders.add(sessionListenerBuilder);
		builders.add(sessionBindingListenerBuilder);
		builders.add(sessionAttributeListenerBuilder);
		builders.add(sessionActivationListenerBuilder);
		builders.add(requestListenerBuilder);
		builders.add(requestAttributeListenerBuilder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void listenerInstanceBuilder(Map<String, List<BasicConfigInfo>> configMap) {
		for (String beanName : configMap.keySet()) {
			List<BasicConfigInfo> configInfos = configMap.get(beanName);
			Object listener = BeanContainerFactory.getBeanContainer(
					this.getClass().getClassLoader()).getBean(beanName);
			if (listener instanceof BasicTinyConfigAware) {
				((BasicTinyConfigAware) listener)
						.setBasicConfig(new SimpleBasicTinyConfig(configInfos));
			}
			for (ListenerInstanceBuilder builder : builders) {
				if (builder.isTypeMatch(listener)) {
					builder.buildInstances(listener);
				}
			}
		}
	}
	
	public List<ServletContextListener> getContextListeners() {
		return contextListenerBuilder.getInstances();
	}

	public List<ServletContextAttributeListener> getContextAttributeListeners() {
		return contextAttributeListenerBuilder.getInstances();
	}

	public List<HttpSessionListener> getSessionListeners() {
		return sessionListenerBuilder.getInstances();
	}

	public List<HttpSessionAttributeListener> getSessionAttributeListeners() {
		return sessionAttributeListenerBuilder.getInstances();
	}

	public List<HttpSessionActivationListener> getSessionActivationListeners() {
		return sessionActivationListenerBuilder.getInstances();
	}

	public List<HttpSessionBindingListener> getSessionBindingListeners() {
		return sessionBindingListenerBuilder.getInstances();
	}

	public List<ServletRequestAttributeListener> getRequestAttributeListeners() {
		return requestAttributeListenerBuilder.getInstances();
	}

	public List<ServletRequestListener> getRequestListeners() {
		return requestListenerBuilder.getInstances();
	}
}
