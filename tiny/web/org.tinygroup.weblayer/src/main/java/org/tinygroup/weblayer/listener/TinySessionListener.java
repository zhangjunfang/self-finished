package org.tinygroup.weblayer.listener;

import javax.servlet.http.HttpSessionListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的HttpSessionListener
 * 
 * @author renhui
 * 
 */
public interface TinySessionListener extends HttpSessionListener, Ordered,
		BasicTinyConfigAware {

}
