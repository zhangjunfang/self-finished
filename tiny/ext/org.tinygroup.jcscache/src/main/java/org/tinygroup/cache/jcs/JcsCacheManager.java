package org.tinygroup.cache.jcs;

import org.apache.jcs.engine.control.CompositeCacheManager;
import org.tinygroup.cache.AbstractCacheManager;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.exception.CacheException;

/**
 * 
 * @author renhui
 * 
 */
public class JcsCacheManager extends AbstractCacheManager {

	private CompositeCacheManager manager;

	private JcsCacheManager() {
		this(CompositeCacheManager.getInstance());
	}

	private JcsCacheManager(CompositeCacheManager manager) {
		super();
		this.manager = manager;
	}

	public static JcsCacheManager getInstance() {
		return new JcsCacheManager();
	}

	public static JcsCacheManager getInstance(CompositeCacheManager manager) {
		return new JcsCacheManager(manager);
	}

	public void shutDown() {
		manager.shutDown();
	}

	@Override
	protected Cache newCache(String region) {
		try {
			JcsCache jcsCache = new JcsCache();
			jcsCache.init(region);
			return jcsCache;
		} catch (Exception e) {
			throw new CacheException(e);
		}

	}

	@Override
	protected void internalRemoveCache(Cache cache) {
		manager.freeCache(cacheMap.inverse().get(cache));
	}

}
