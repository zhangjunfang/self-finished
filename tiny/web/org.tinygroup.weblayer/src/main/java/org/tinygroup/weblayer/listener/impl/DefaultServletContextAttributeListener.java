package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.tinygroup.weblayer.listener.TinyServletContextAttributeListener;

public class DefaultServletContextAttributeListener extends SimpleBasicTinyConfigAware implements
		TinyServletContextAttributeListener {
	
	private ServletContextAttributeListener servletContextAttributeListener;
	
	public DefaultServletContextAttributeListener(
			ServletContextAttributeListener servletContextAttributeListener) {
		super();
		this.servletContextAttributeListener = servletContextAttributeListener;
	}

	public void attributeAdded(ServletContextAttributeEvent scab) {
		servletContextAttributeListener.attributeAdded(scab);
	}

	public void attributeRemoved(ServletContextAttributeEvent scab) {
		servletContextAttributeListener.attributeRemoved(scab);
	}

	public void attributeReplaced(ServletContextAttributeEvent scab) {
		servletContextAttributeListener.attributeReplaced(scab);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
