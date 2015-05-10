package org.tinygroup.weblayer.listener;

import java.util.List;


/**
 * 创建监听器实例的接口
 * @author renhui
 *
 * @param <INSTANCE>
 */
public interface ListenerInstanceBuilder<INSTANCE>{
	
	/**
	 * 是否匹配参数指定的实例
	 * @param object
	 * @return
	 */
	boolean isTypeMatch(Object object);
	
    /**
     * 构建实例
     */
    public void buildInstances(INSTANCE object);
    /**
     * 获取监听器实例列表
     * @return
     */
    public List<INSTANCE> getInstances();
	
}
