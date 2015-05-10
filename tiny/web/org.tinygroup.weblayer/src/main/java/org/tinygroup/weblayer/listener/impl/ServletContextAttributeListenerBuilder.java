package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletContextAttributeListener;

/**
 * 创建ServletContextAttributeListener
 * @author renhui
 *
 */
public class ServletContextAttributeListenerBuilder extends
		AbstractListenerBuilder<ServletContextAttributeListener> {

	public boolean isTypeMatch(Object object) {
		return ServletContextAttributeListener.class.isInstance(object);
	}

	@Override
	protected DefaultServletContextAttributeListener replaceListener(
			ServletContextAttributeListener contextListener) {
		return new DefaultServletContextAttributeListener(contextListener);
	}

}
