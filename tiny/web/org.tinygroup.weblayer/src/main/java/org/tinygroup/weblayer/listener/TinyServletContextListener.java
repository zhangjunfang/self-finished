package org.tinygroup.weblayer.listener;

import javax.servlet.ServletContextListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的ServletContextListener
 * @author renhui
 *
 */
public interface TinyServletContextListener extends ServletContextListener,
		Ordered,BasicTinyConfigAware {

}
