package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletRequestListener;

public class RequestListenerBuilder extends AbstractListenerBuilder<ServletRequestListener> {

	public boolean isTypeMatch(Object object) {
		return ServletRequestListener.class.isInstance(object);
	}

	@Override
	protected ServletRequestListener replaceListener(
			ServletRequestListener listener) {
		return new DefaultRequestListener(listener);
	}

}
