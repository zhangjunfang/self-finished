package org.tinygroup.weblayer;

import java.util.Iterator;
import java.util.Map;


/**
 * tiny获取配置信息的接口
 * @author renhui
 *
 */
public interface BasicTinyConfig {
	
    /**
     * 获取名称
     * @return
     */
	public String getConfigName();
	
	/**
	 * 根据参数名称获取参数值
	 * @param name
	 * @return
	 */
	public String getInitParameter(String name);
	
	/**
	 * 返回所有参数信息
	 * @return
	 */
	public Iterator<String> getInitParameterNames();
	
	
	public Map<String,String> getParameterMap();
	
	
   	
}
