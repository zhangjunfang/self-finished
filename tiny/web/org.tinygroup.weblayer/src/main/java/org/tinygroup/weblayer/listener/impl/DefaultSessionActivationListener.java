package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import org.tinygroup.weblayer.listener.TinySessionActivationListener;

public class DefaultSessionActivationListener extends
		SimpleBasicTinyConfigAware implements TinySessionActivationListener {

	private HttpSessionActivationListener sessionActivationListener;

	public DefaultSessionActivationListener(
			HttpSessionActivationListener sessionActivationListener) {
		super();
		this.sessionActivationListener = sessionActivationListener;
	}

	public void sessionWillPassivate(HttpSessionEvent se) {
		sessionActivationListener.sessionWillPassivate(se);
	}

	public void sessionDidActivate(HttpSessionEvent se) {
		sessionActivationListener.sessionDidActivate(se);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
