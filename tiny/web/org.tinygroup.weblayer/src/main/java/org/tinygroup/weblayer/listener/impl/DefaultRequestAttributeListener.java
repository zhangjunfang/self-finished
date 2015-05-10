package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

import org.tinygroup.weblayer.listener.TinyRequestAttributeListener;

public class DefaultRequestAttributeListener extends SimpleBasicTinyConfigAware implements
		TinyRequestAttributeListener {
	
	private ServletRequestAttributeListener requestAttributeListener;
	
	public DefaultRequestAttributeListener(
			ServletRequestAttributeListener requestAttributeListener) {
		super();
		this.requestAttributeListener = requestAttributeListener;
	}

	public void attributeAdded(ServletRequestAttributeEvent srae) {
		requestAttributeListener.attributeAdded(srae);
	}

	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		requestAttributeListener.attributeRemoved(srae);
	}

	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		requestAttributeListener.attributeReplaced(srae);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
