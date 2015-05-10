package org.tinygroup.weblayer.listener;

import javax.servlet.ServletRequestAttributeListener;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.BasicTinyConfigAware;

/**
 * 实现顺序接口的ServletRequestAttributeListener
 * 
 * @author renhui
 * 
 */
public interface TinyRequestAttributeListener extends
		ServletRequestAttributeListener, Ordered, BasicTinyConfigAware {

}
