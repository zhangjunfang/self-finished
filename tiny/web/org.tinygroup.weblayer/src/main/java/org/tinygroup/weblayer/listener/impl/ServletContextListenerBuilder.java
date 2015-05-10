package org.tinygroup.weblayer.listener.impl;

import javax.servlet.ServletContextListener;

/**
 * 创建ServletContextListener
 * @author renhui
 *
 */
public class ServletContextListenerBuilder extends
		AbstractListenerBuilder<ServletContextListener> {

	public boolean isTypeMatch(Object object) {
		return ServletContextListener.class.isInstance(object);
	}

	@Override
	protected ServletContextListener replaceListener(
			ServletContextListener contextListener) {
		return new DefaultServletContextListener(contextListener);
	}

}
