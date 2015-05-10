package org.tinygroup.weblayer.listener;

import javax.servlet.http.HttpSessionActivationListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的HttpSessionActivationListener
 * 
 * @author renhui
 * 
 */
public interface TinySessionActivationListener extends
		HttpSessionActivationListener, Ordered, BasicTinyConfigAware {

}
