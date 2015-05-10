package org.tinygroup.database.connect;

import java.util.Collection;

/**
 * 连接监控信息提供者
 * @author yancheng11334
 *
 */
public interface ConnectionTraceProvider {
    
	/**
	 * 返回提供者监控的连接信息集合
	 * @return
	 */
	Collection<ConnectionTrace> getConnectionTraces();
}
