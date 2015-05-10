package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.tinygroup.weblayer.listener.TinySessionBindingListener;

public class DefaultSessionBindingListener extends SimpleBasicTinyConfigAware
		implements TinySessionBindingListener {

	private HttpSessionBindingListener sessionBindingListener;

	public DefaultSessionBindingListener(
			HttpSessionBindingListener sessionBindingListener) {
		super();
		this.sessionBindingListener = sessionBindingListener;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		sessionBindingListener.valueBound(event);
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		sessionBindingListener.valueUnbound(event);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
