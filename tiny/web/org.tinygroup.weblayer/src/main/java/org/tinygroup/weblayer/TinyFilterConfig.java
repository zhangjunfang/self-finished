package org.tinygroup.weblayer;



/**
 * tinyfilter的配置对象
 * @author renhui
 *
 */
public interface TinyFilterConfig  extends BasicTinyConfig{
	
	/**
	 * 请求的url是否匹配定义的映射正则表达式
	 * @param url
	 * @return
	 */
	public boolean isMatch(String url);
	
}
