package org.tinygroup.weblayer.listener;

import javax.servlet.http.HttpSessionAttributeListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的HttpSessionAttributeListener
 * 
 * @author renhui
 * 
 */
public interface TinySessionAttributeListener extends
		HttpSessionAttributeListener, Ordered, BasicTinyConfigAware {

}
