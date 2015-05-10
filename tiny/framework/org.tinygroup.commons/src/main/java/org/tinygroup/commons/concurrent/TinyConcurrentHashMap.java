package org.tinygroup.commons.concurrent;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 继承ConcurrentHashMap使之支持存放value为null
 * @author renhui
 *
 */
public class TinyConcurrentHashMap extends ConcurrentHashMap<Object, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -806384232108465010L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object put(Object key, Object value) {
		SoftReference ref = new SoftReference(value);
		SoftReference prev = (SoftReference) super.put(key, ref);
		return prev == null ? null : prev.get();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object putIfAbsent(Object key, Object value) {
		SoftReference ref = new SoftReference(value);
		SoftReference prev = (SoftReference) super.putIfAbsent(key, ref);
		return prev == null ? null : prev.get();
	}

	@SuppressWarnings("rawtypes")
	public Object get(Object key) {
		SoftReference ref = (SoftReference) super.get(key);
		if (ref != null && ref.get() == null) {
			remove(key);
		}
		return ref != null ? ref.get() : null;
	}
	
	@SuppressWarnings("rawtypes")
	public Object remove(Object key) {
		SoftReference ref = (SoftReference) super.remove(key);
		return ref != null ? ref.get() : null;
	}

}
