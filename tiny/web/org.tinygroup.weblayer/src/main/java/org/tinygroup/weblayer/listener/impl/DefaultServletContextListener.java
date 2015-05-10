package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.tinygroup.weblayer.listener.TinyServletContextListener;

/**
 * 默认的TinyServletContextListener接口实现类，对原生ServletContextListener接口的包装
 * 
 * @author renhui
 * 
 */
public class DefaultServletContextListener extends SimpleBasicTinyConfigAware
		implements TinyServletContextListener {

	private ServletContextListener servletContextListener;

	public DefaultServletContextListener(
			ServletContextListener servletContextListener) {
		this.servletContextListener = servletContextListener;
	}

	public void contextInitialized(ServletContextEvent sce) {
		servletContextListener.contextInitialized(sce);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		servletContextListener.contextDestroyed(sce);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
