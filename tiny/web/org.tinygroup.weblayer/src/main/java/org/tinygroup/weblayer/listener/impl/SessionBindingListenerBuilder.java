package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionBindingListener;

public class SessionBindingListenerBuilder extends AbstractListenerBuilder<HttpSessionBindingListener> {

	public boolean isTypeMatch(Object object) {
		return HttpSessionBindingListener.class.isInstance(object);
	}

	@Override
	protected HttpSessionBindingListener replaceListener(
			HttpSessionBindingListener listener) {
		return new DefaultSessionBindingListener(listener);
	}

}
