package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletRequestAttributeListener;

public class RequestAttributeListenerBuilder extends AbstractListenerBuilder<ServletRequestAttributeListener> {

	public boolean isTypeMatch(Object object) {
		return ServletRequestAttributeListener.class.isInstance(object);
	}

	@Override
	protected ServletRequestAttributeListener replaceListener(
			ServletRequestAttributeListener listener) {
		return new DefaultRequestAttributeListener(listener);
	}

}
