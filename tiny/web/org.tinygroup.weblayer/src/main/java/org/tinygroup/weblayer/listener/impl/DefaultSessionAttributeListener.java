package org.tinygroup.weblayer.listener.impl;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.tinygroup.weblayer.listener.TinySessionAttributeListener;

public class DefaultSessionAttributeListener extends SimpleBasicTinyConfigAware implements
		TinySessionAttributeListener {
	
	private HttpSessionAttributeListener sessionAttributeListener;
	
	public DefaultSessionAttributeListener(
			HttpSessionAttributeListener sessionAttributeListener) {
		super();
		this.sessionAttributeListener = sessionAttributeListener;
	}

	public void attributeAdded(HttpSessionBindingEvent se) {
		sessionAttributeListener.attributeAdded(se);
	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
		sessionAttributeListener.attributeRemoved(se);
	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
		sessionAttributeListener.attributeReplaced(se);
	}

	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
