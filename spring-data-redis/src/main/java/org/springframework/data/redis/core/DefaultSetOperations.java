/*
 * Copyright 2011-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

/**
 * Default implementation of {@link SetOperations}.
 * 
 * @author Costin Leau
 * @author Christoph Strobl
 */
class DefaultSetOperations<K, V> extends AbstractOperations<K, V> implements SetOperations<K, V> {

	public DefaultSetOperations(RedisTemplate<K, V> template) {
		super(template);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long add(K key, V... values) {
		final byte[] rawKey = rawKey(key);
		final byte[][] rawValues = rawValues(values);
		return execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.sAdd(rawKey, rawValues);
			}
		}, true);
	}

	@Override
	public Set<V> difference(K key, K otherKey) {
		return difference(key, Collections.singleton(otherKey));
	}

	@Override
	public Set<V> difference(final K key, final Collection<K> otherKeys) {
		final byte[][] rawKeys = rawKeys(key, otherKeys);
		Set<byte[]> rawValues = execute(new RedisCallback<Set<byte[]>>() {

			@Override
			public Set<byte[]> doInRedis(RedisConnection connection) {
				return connection.sDiff(rawKeys);
			}
		}, true);

		return deserializeValues(rawValues);
	}

	@Override
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		return differenceAndStore(key, Collections.singleton(otherKey), destKey);
	}

	@Override
	public Long differenceAndStore(final K key, final Collection<K> otherKeys, K destKey) {
		final byte[][] rawKeys = rawKeys(key, otherKeys);
		final byte[] rawDestKey = rawKey(destKey);
		return execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.sDiffStore(rawDestKey, rawKeys);
			}
		}, true);
	}

	@Override
	public Set<V> intersect(K key, K otherKey) {
		return intersect(key, Collections.singleton(otherKey));
	}

	@Override
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		final byte[][] rawKeys = rawKeys(key, otherKeys);
		Set<byte[]> rawValues = execute(new RedisCallback<Set<byte[]>>() {

			@Override
			public Set<byte[]> doInRedis(RedisConnection connection) {
				return connection.sInter(rawKeys);
			}
		}, true);

		return deserializeValues(rawValues);
	}

	@Override
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		return intersectAndStore(key, Collections.singleton(otherKey), destKey);
	}

	@Override
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		final byte[][] rawKeys = rawKeys(key, otherKeys);
		final byte[] rawDestKey = rawKey(destKey);
		return execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) {
				connection.sInterStore(rawDestKey, rawKeys);
				return null;
			}
		}, true);
	}

	@Override
	public Boolean isMember(K key, Object o) {
		final byte[] rawKey = rawKey(key);
		final byte[] rawValue = rawValue(o);
		return execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) {
				return connection.sIsMember(rawKey, rawValue);
			}
		}, true);
	}

	@Override
	public Set<V> members(K key) {
		final byte[] rawKey = rawKey(key);
		Set<byte[]> rawValues = execute(new RedisCallback<Set<byte[]>>() {

			@Override
			public Set<byte[]> doInRedis(RedisConnection connection) {
				return connection.sMembers(rawKey);
			}
		}, true);

		return deserializeValues(rawValues);
	}

	@Override
	public Boolean move(K key, V value, K destKey) {
		final byte[] rawKey = rawKey(key);
		final byte[] rawDestKey = rawKey(destKey);
		final byte[] rawValue = rawValue(value);

		return execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) {
				return connection.sMove(rawKey, rawDestKey, rawValue);
			}
		}, true);
	}

	@Override
	public V randomMember(K key) {

		return execute(new ValueDeserializingRedisCallback(key) {

			@Override
			protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
				return connection.sRandMember(rawKey);
			}
		}, true);
	}

	@Override
	public Set<V> distinctRandomMembers(K key, final long count) {
		if (count < 0) {
			throw new IllegalArgumentException("Negative count not supported. "
					+ "Use randomMembers to allow duplicate elements.");
		}
		final byte[] rawKey = rawKey(key);
		Set<byte[]> rawValues = execute(new RedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> doInRedis(RedisConnection connection) {
				return new HashSet<byte[]>(connection.sRandMember(rawKey, count));
			}
		}, true);

		return deserializeValues(rawValues);
	}

	@Override
	public List<V> randomMembers(K key, final long count) {
		if (count < 0) {
			throw new IllegalArgumentException("Use a positive number for count. "
					+ "This method is already allowing duplicate elements.");
		}
		final byte[] rawKey = rawKey(key);
		List<byte[]> rawValues = execute(new RedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> doInRedis(RedisConnection connection) {
				return connection.sRandMember(rawKey, -count);
			}
		}, true);

		return deserializeValues(rawValues);
	}

	@Override
	public Long remove(K key, Object... values) {
		final byte[] rawKey = rawKey(key);
		final byte[][] rawValues = rawValues(values);
		return execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.sRem(rawKey, rawValues);
			}
		}, true);
	}

	@Override
	public V pop(K key) {
		return execute(new ValueDeserializingRedisCallback(key) {

			@Override
			protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
				return connection.sPop(rawKey);
			}
		}, true);
	}

	@Override
	public Long size(K key) {
		final byte[] rawKey = rawKey(key);
		return execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.sCard(rawKey);
			}
		}, true);
	}

	@Override
	public Set<V> union(K key, K otherKey) {
		return union(key, Collections.singleton(otherKey));
	}

	@Override
	public Set<V> union(K key, Collection<K> otherKeys) {
		final byte[][] rawKeys = rawKeys(key, otherKeys);
		Set<byte[]> rawValues = execute(new RedisCallback<Set<byte[]>>() {

			@Override
			public Set<byte[]> doInRedis(RedisConnection connection) {
				return connection.sUnion(rawKeys);
			}
		}, true);

		return deserializeValues(rawValues);
	}

	@Override
	public Long unionAndStore(K key, K otherKey, K destKey) {
		return unionAndStore(key, Collections.singleton(otherKey), destKey);
	}

	@Override
	public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
		final byte[][] rawKeys = rawKeys(key, otherKeys);
		final byte[] rawDestKey = rawKey(destKey);
		return execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) {
				return connection.sUnionStore(rawDestKey, rawKeys);
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.SetOperations#sScan(java.lang.Object, org.springframework.data.redis.core.ScanOptions)
	 */
	@Override
	public Cursor<V> scan(K key, final ScanOptions options) {

		final byte[] rawKey = rawKey(key);
		return execute(new RedisCallback<Cursor<V>>() {

			@Override
			public Cursor<V> doInRedis(RedisConnection connection) throws DataAccessException {
				return new ConvertingCursor<byte[], V>(connection.sScan(rawKey, options), new Converter<byte[], V>() {

					@Override
					public V convert(byte[] source) {
						return deserializeValue(source);
					}
				});
			}
		}, true);

	}
}
