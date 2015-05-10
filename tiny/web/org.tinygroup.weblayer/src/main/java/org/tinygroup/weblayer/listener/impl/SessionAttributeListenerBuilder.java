package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionAttributeListener;

public class SessionAttributeListenerBuilder extends AbstractListenerBuilder<HttpSessionAttributeListener> {

	public boolean isTypeMatch(Object object) {
		return HttpSessionAttributeListener.class.isInstance(object);
	}

	@Override
	protected HttpSessionAttributeListener replaceListener(
			HttpSessionAttributeListener listener) {
		return new DefaultSessionAttributeListener(listener);
	}

}
