package org.tinygroup.cache;

import org.tinygroup.cache.exception.CacheException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * 
 * @author renhui
 * 
 */
public abstract class AbstractCacheManager implements CacheManager {

	protected BiMap<String, Cache> cacheMap = HashBiMap.create();

	public Cache createCache(String region) {
		Cache cache = newCache(region);
		if (cache == null) {
			throw new CacheException(String.format("region:%s,未在配置中定义", region));
		}
		cacheMap.put(region, cache);
		return cache;
	}

	protected abstract Cache newCache(String region);

	public void clearCache(Cache cache) {
		cache.clear();
	}

	public void clearCaches() {
		for (Cache cache : cacheMap.values()) {
			clearCache(cache);
		}
	}

	public void removeCaches() {
		for (Cache cache : cacheMap.values()) {
			removeCache(cache);
		}
	}

	public void removeCache(Cache cache) {
		clearCache(cache);
		internalRemoveCache(cache);
	}

	protected abstract void internalRemoveCache(Cache cache);
}
