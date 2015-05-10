package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionListener;

public class SessionListenerBuilder extends AbstractListenerBuilder<HttpSessionListener> {

	public boolean isTypeMatch(Object object) {
		return HttpSessionListener.class.isInstance(object);
	}

	@Override
	protected HttpSessionListener replaceListener(
			HttpSessionListener listener) {
		return new DefaultSessionListener(listener);
	}

}
