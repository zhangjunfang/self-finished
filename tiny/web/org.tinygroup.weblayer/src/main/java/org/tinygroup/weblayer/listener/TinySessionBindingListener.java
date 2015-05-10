package org.tinygroup.weblayer.listener;

import javax.servlet.http.HttpSessionBindingListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的HttpSessionBindingListener
 * 
 * @author renhui
 * 
 */
public interface TinySessionBindingListener extends HttpSessionBindingListener,
		Ordered, BasicTinyConfigAware {

}
