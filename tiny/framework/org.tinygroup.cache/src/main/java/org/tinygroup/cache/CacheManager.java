package org.tinygroup.cache;

/**
 * 缓存管理接口
 * @author renhui
 *
 */
public interface CacheManager {

	/**
	 * 根据region创建相应的cache实例
	 * @param region
	 * @return
	 */
	public Cache createCache(String region);
	/**
	 * 清除cache指定的缓存的内容
	 * @param cache
	 */
	public void clearCache(Cache cache);
	/**
	 * 清楚所有缓存的内容
	 */
	public void clearCaches();
	/**
	 * 从缓存管理器中移除缓存
	 * @param cache
	 */
	public void removeCache(Cache cache);
	/**
	 * 从管理器中移除所有缓存
	 */
	public void removeCaches();
	/**
	 * 缓存关闭接口
	 */
	public void  shutDown();
	
}
