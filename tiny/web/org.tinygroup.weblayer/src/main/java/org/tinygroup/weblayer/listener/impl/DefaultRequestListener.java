package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.tinygroup.weblayer.listener.TinyRequestListener;

public class DefaultRequestListener extends SimpleBasicTinyConfigAware implements TinyRequestListener {

	private ServletRequestListener servletRequestListener;
	
	public DefaultRequestListener(ServletRequestListener servletRequestListener) {
		super();
		this.servletRequestListener = servletRequestListener;
	}

	public void requestDestroyed(ServletRequestEvent sre) {
		servletRequestListener.requestDestroyed(sre);
	}

	public void requestInitialized(ServletRequestEvent sre) {
		servletRequestListener.requestInitialized(sre);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
