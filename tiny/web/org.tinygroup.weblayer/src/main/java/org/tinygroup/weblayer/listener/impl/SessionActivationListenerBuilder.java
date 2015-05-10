package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionActivationListener;

public class SessionActivationListenerBuilder extends AbstractListenerBuilder<HttpSessionActivationListener> {

	public boolean isTypeMatch(Object object) {
		return HttpSessionActivationListener.class.isInstance(object);
	}

	@Override
	protected HttpSessionActivationListener replaceListener(
			HttpSessionActivationListener listener) {
		return new DefaultSessionActivationListener(listener);
	}

}
