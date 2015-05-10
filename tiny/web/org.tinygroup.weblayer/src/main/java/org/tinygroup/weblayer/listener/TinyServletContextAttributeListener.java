package org.tinygroup.weblayer.listener;

import javax.servlet.ServletContextAttributeListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的ServletContextAttributeListener
 * 
 * @author renhui
 * 
 */
public interface TinyServletContextAttributeListener extends
		ServletContextAttributeListener, Ordered, BasicTinyConfigAware {

}
