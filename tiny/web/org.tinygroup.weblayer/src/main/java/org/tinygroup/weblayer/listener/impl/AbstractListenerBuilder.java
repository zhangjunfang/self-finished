package org.tinygroup.weblayer.listener.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.listener.ListenerInstanceBuilder;

/**
 * 
 * @author renhui
 * 
 * @param <INSTANCE>
 */
public abstract class AbstractListenerBuilder<INSTANCE> implements
		ListenerInstanceBuilder<INSTANCE> {

	protected List<INSTANCE> listeners = new ArrayList<INSTANCE>();
	
	private boolean ordered;//是否已经进行排序了

	private static Logger logger = LoggerFactory
			.getLogger(AbstractListenerBuilder.class);

	public void buildInstances(INSTANCE object) {
		INSTANCE listener = object;
		if (!imptOrdered(object)) {
			listener = replaceListener(object);
			logger.logMessage(LogLevel.DEBUG,
					"listener:[{0}] not implements Ordered will replace [{1}]",
					object.getClass().getSimpleName(), listener.getClass()
							.getSimpleName());
		}
		listeners.add(listener);
	}

	protected abstract INSTANCE replaceListener(INSTANCE listener);

	private boolean imptOrdered(Object object) {
		return object instanceof Ordered;
	}

	public List<INSTANCE> getInstances() {
		if(!ordered){
			Collections.sort(listeners,new Comparator<INSTANCE>() {
				public int compare(INSTANCE o1, INSTANCE o2) {
					Ordered order1=(Ordered)o1;
					Ordered order2=(Ordered)o2;
					if (order1 != null && order2 != null) {
						return order1.getOrder() > order2.getOrder() ? 1
								: (order1.getOrder() == order2.getOrder() ? 0 : -1);
					}
					return 0;
				}
			});
			ordered=true;
		}
		return listeners;
	}

}
