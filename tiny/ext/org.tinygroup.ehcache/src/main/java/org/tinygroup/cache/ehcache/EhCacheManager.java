package org.tinygroup.cache.ehcache;

import net.sf.ehcache.CacheManager;

import org.tinygroup.cache.AbstractCacheManager;
import org.tinygroup.cache.Cache;

/**
 * 
 * @author renhui
 * 
 */
public class EhCacheManager extends AbstractCacheManager {

	private net.sf.ehcache.CacheManager manager;

	private EhCacheManager() {
		this(CacheManager.getInstance());
	}

	public static EhCacheManager getInstance() {
		return new EhCacheManager();
	}

	public static EhCacheManager getInstance(CacheManager manager) {
		return new EhCacheManager(manager);
	}

	private EhCacheManager(CacheManager manager) {
		super();
		this.manager = manager;
	}

	public void shutDown() {
		manager.shutdown();
	}

	@Override
	protected Cache newCache(String region) {
		EhCache cache = new EhCache();
        cache.init(region);
		return cache;
	}

	@Override
	protected void internalRemoveCache(Cache cache) {
		manager.removeCache(cacheMap.inverse().get(cache));
	}

}
