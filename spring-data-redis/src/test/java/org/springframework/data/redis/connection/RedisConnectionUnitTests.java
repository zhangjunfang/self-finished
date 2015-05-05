/*
 * Copyright 2014-2015 the original author or authors.
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
package org.springframework.data.redis.connection;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisNode.RedisNodeBuilder;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.ObjectUtils;

/**
 * @author Christoph Strobl
 * @author Thomas Darimont
 * @author David Liu
 */
public class RedisConnectionUnitTests {

	private final RedisNode SENTINEL_1 = new RedisNodeBuilder().listeningAt("localhost", 23679).build();
	private AbstractDelegatingRedisConnectionStub connection;
	private RedisSentinelConnection sentinelConnectionMock;

	@Before
	public void setUp() {
		sentinelConnectionMock = mock(RedisSentinelConnection.class);

		connection = new AbstractDelegatingRedisConnectionStub(mock(AbstractRedisConnection.class, CALLS_REAL_METHODS));
		connection.setSentinelConfiguration(new RedisSentinelConfiguration().master("mymaster").sentinel(SENTINEL_1));
		connection.setSentinelConnection(sentinelConnectionMock);
	}

	/**
	 * @see DATAREDIS-330
	 */
	@Test
	public void shouldCloseSentinelConnectionAlongWithRedisConnection() throws IOException {

		when(sentinelConnectionMock.isOpen()).thenReturn(true).thenReturn(false);

		connection.setActiveNode(SENTINEL_1);
		connection.getSentinelConnection();
		connection.close();

		verify(sentinelConnectionMock, times(1)).close();
	}

	/**
	 * @see DATAREDIS-330
	 */
	@Test
	public void shouldNotTryToCloseSentinelConnectionsWhenAlreadyClosed() throws IOException {

		when(sentinelConnectionMock.isOpen()).thenReturn(true);
		when(sentinelConnectionMock.isOpen()).thenReturn(false);

		connection.setActiveNode(SENTINEL_1);
		connection.getSentinelConnection();
		connection.close();

		verify(sentinelConnectionMock, never()).close();
	}

	static class AbstractDelegatingRedisConnectionStub extends AbstractRedisConnection {

		RedisConnection delegate;
		RedisNode activeNode;
		RedisSentinelConnection sentinelConnection;

		public AbstractDelegatingRedisConnectionStub(RedisConnection delegate) {
			this.delegate = delegate;
		}

		@Override
		protected boolean isActive(RedisNode node) {
			return ObjectUtils.nullSafeEquals(activeNode, node);
		}

		public void setActiveNode(RedisNode activeNode) {
			this.activeNode = activeNode;
		}

		public void setSentinelConnection(RedisSentinelConnection sentinelConnection) {
			this.sentinelConnection = sentinelConnection;
		}

		@Override
		public boolean isSubscribed() {
			return delegate.isSubscribed();
		}

		@Override
		public void scriptFlush() {
			delegate.scriptFlush();
		}

		@Override
		public void select(int dbIndex) {
			delegate.select(dbIndex);
		}

		@Override
		public void multi() {
			delegate.multi();
		}

		@Override
		public Long rPush(byte[] key, byte[]... values) {
			return delegate.rPush(key, values);
		}

		@Override
		public byte[] get(byte[] key) {
			return delegate.get(key);
		}

		@Override
		public void scriptKill() {
			delegate.scriptKill();
		}

		@Override
		public Long sAdd(byte[] key, byte[]... values) {
			return delegate.sAdd(key, values);
		}

		@Override
		public Boolean exists(byte[] key) {
			return delegate.exists(key);
		}

		@Override
		public Subscription getSubscription() {
			return delegate.getSubscription();
		}

		@Override
		public byte[] echo(byte[] message) {
			return delegate.echo(message);
		}

		@Override
		public Boolean hSet(byte[] key, byte[] field, byte[] value) {
			return delegate.hSet(key, field, value);
		}

		@Override
		public void bgWriteAof() {
			delegate.bgReWriteAof();
		}

		@Override
		public Object execute(String command, byte[]... args) {
			return delegate.execute(command, args);
		}

		@Override
		public String scriptLoad(byte[] script) {
			return delegate.scriptLoad(script);
		}

		@Override
		public byte[] getSet(byte[] key, byte[] value) {
			return delegate.getSet(key, value);
		}

		@Override
		public List<Object> exec() {
			return delegate.exec();
		}

		@Override
		public Long lPush(byte[] key, byte[]... value) {
			return delegate.lPush(key, value);
		}

		@Override
		public Long del(byte[]... keys) {
			return delegate.del(keys);
		}

		@Override
		public void close() throws DataAccessException {
			super.close();
		}

		@Override
		public String ping() {
			return delegate.ping();
		}

		@Override
		public Long sRem(byte[] key, byte[]... values) {
			return delegate.sRem(key, values);
		}

		@Override
		public Boolean zAdd(byte[] key, double score, byte[] value) {
			return delegate.zAdd(key, score, value);
		}

		@Override
		public Long publish(byte[] channel, byte[] message) {
			return delegate.publish(channel, message);
		}

		@Override
		public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
			return delegate.hSetNX(key, field, value);
		}

		@Override
		public void bgReWriteAof() {
			delegate.bgReWriteAof();
		}

		@Override
		public List<byte[]> mGet(byte[]... keys) {
			return delegate.mGet(keys);
		}

		@Override
		public boolean isClosed() {
			return delegate.isClosed();
		}

		@Override
		public Long rPushX(byte[] key, byte[] value) {
			return delegate.rPushX(key, value);
		}

		@Override
		public DataType type(byte[] key) {
			return delegate.type(key);
		}

		@Override
		public List<Boolean> scriptExists(String... scriptShas) {
			return delegate.scriptExists(scriptShas);
		}

		@Override
		public byte[] sPop(byte[] key) {
			return delegate.sPop(key);
		}

		@Override
		public void bgSave() {
			delegate.bgSave();
		}

		@Override
		public void set(byte[] key, byte[] value) {
			delegate.set(key, value);
		}

		@Override
		public void discard() {
			delegate.discard();
		}

		@Override
		public Object getNativeConnection() {
			return delegate.getNativeConnection();
		}

		@Override
		public Long zAdd(byte[] key, Set<Tuple> tuples) {
			return delegate.zAdd(key, tuples);
		}

		@Override
		public void subscribe(MessageListener listener, byte[]... channels) {
			delegate.subscribe(listener, channels);
		}

		@Override
		public Set<byte[]> keys(byte[] pattern) {
			return delegate.keys(pattern);
		}

		@Override
		public byte[] hGet(byte[] key, byte[] field) {
			return delegate.hGet(key, field);
		}

		@Override
		public Long lPushX(byte[] key, byte[] value) {
			return delegate.lPushX(key, value);
		}

		@Override
		public Long lastSave() {
			return delegate.lastSave();
		}

		@Override
		public void watch(byte[]... keys) {
			delegate.watch(keys);
		}

		@Override
		public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
			return delegate.sMove(srcKey, destKey, value);
		}

		@Override
		public Boolean setNX(byte[] key, byte[] value) {
			return delegate.setNX(key, value);
		}

		@Override
		public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
			return delegate.eval(script, returnType, numKeys, keysAndArgs);
		}

		@Override
		public boolean isQueueing() {
			return delegate.isQueueing();
		}

		@Override
		public Cursor<byte[]> scan(ScanOptions options) {
			return delegate.scan(options);
		}

		@Override
		public void save() {
			delegate.save();
		}

		@Override
		public List<byte[]> hMGet(byte[] key, byte[]... fields) {
			return delegate.hMGet(key, fields);
		}

		@Override
		public Long zRem(byte[] key, byte[]... values) {
			return delegate.zRem(key, values);
		}

		@Override
		public Long lLen(byte[] key) {
			return delegate.lLen(key);
		}

		@Override
		public void unwatch() {
			delegate.unwatch();
		}

		@Override
		public Long dbSize() {
			return delegate.dbSize();
		}

		@Override
		public void setEx(byte[] key, long seconds, byte[] value) {
			delegate.setEx(key, seconds, value);
		}

		@Override
		public Long sCard(byte[] key) {
			return delegate.sCard(key);
		}

		@Override
		public byte[] randomKey() {
			return delegate.randomKey();
		}

		@Override
		public <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
			return delegate.evalSha(scriptSha, returnType, numKeys, keysAndArgs);
		}

		@Override
		public List<byte[]> lRange(byte[] key, long begin, long end) {
			return delegate.lRange(key, begin, end);
		}

		@Override
		public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
			delegate.hMSet(key, hashes);
		}

		@Override
		public Double zIncrBy(byte[] key, double increment, byte[] value) {
			return delegate.zIncrBy(key, increment, value);
		}

		@Override
		public void flushDb() {
			delegate.flushDb();
		}

		@Override
		public Boolean sIsMember(byte[] key, byte[] value) {
			return delegate.sIsMember(key, value);
		}

		@Override
		public void pSubscribe(MessageListener listener, byte[]... patterns) {
			delegate.pSubscribe(listener, patterns);
		}

		@Override
		public void rename(byte[] oldName, byte[] newName) {
			delegate.rename(oldName, newName);
		}

		@Override
		public boolean isPipelined() {
			return delegate.isPipelined();
		}

		@Override
		public void pSetEx(byte[] key, long milliseconds, byte[] value) {
			delegate.pSetEx(key, milliseconds, value);
		}

		@Override
		public void flushAll() {
			delegate.flushAll();
		}

		@Override
		public void lTrim(byte[] key, long begin, long end) {
			delegate.lTrim(key, begin, end);
		}

		@Override
		public Long hIncrBy(byte[] key, byte[] field, long delta) {
			return delegate.hIncrBy(key, field, delta);
		}

		@Override
		public Set<byte[]> sInter(byte[]... keys) {
			return delegate.sInter(keys);
		}

		@Override
		public Boolean renameNX(byte[] oldName, byte[] newName) {
			return delegate.renameNX(oldName, newName);
		}

		@Override
		public Long zRank(byte[] key, byte[] value) {
			return delegate.zRank(key, value);
		}

		@Override
		public Properties info() {
			return delegate.info();
		}

		@Override
		public void openPipeline() {
			delegate.openPipeline();
		}

		@Override
		public void mSet(Map<byte[], byte[]> tuple) {
			delegate.mSet(tuple);
		}

		@Override
		public byte[] lIndex(byte[] key, long index) {
			return delegate.lIndex(key, index);
		}

		@Override
		public Long sInterStore(byte[] destKey, byte[]... keys) {
			return delegate.sInterStore(destKey, keys);
		}

		@Override
		public Double hIncrBy(byte[] key, byte[] field, double delta) {
			return delegate.hIncrBy(key, field, delta);
		}

		@Override
		public Long zRevRank(byte[] key, byte[] value) {
			return delegate.zRevRank(key, value);
		}

		@Override
		public Boolean expire(byte[] key, long seconds) {
			return delegate.expire(key, seconds);
		}

		@Override
		public Properties info(String section) {
			return delegate.info(section);
		}

		@Override
		public Boolean mSetNX(Map<byte[], byte[]> tuple) {
			return delegate.mSetNX(tuple);
		}

		@Override
		public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
			return delegate.lInsert(key, where, pivot, value);
		}

		@Override
		public Set<byte[]> sUnion(byte[]... keys) {
			return delegate.sUnion(keys);
		}

		@Override
		public void shutdown() {
			delegate.shutdown();
		}

		@Override
		public Boolean pExpire(byte[] key, long millis) {
			return delegate.pExpire(key, millis);
		}

		@Override
		public Boolean hExists(byte[] key, byte[] field) {
			return delegate.hExists(key, field);
		}

		@Override
		public Set<byte[]> zRange(byte[] key, long begin, long end) {
			return delegate.zRange(key, begin, end);
		}

		@Override
		public void shutdown(ShutdownOption option) {
			delegate.shutdown(option);
		}

		@Override
		public Long sUnionStore(byte[] destKey, byte[]... keys) {
			return delegate.sUnionStore(destKey, keys);
		}

		@Override
		public Long incr(byte[] key) {
			return delegate.incr(key);
		}

		@Override
		public Long hDel(byte[] key, byte[]... fields) {
			return delegate.hDel(key, fields);
		}

		@Override
		public Boolean expireAt(byte[] key, long unixTime) {
			return delegate.expireAt(key, unixTime);
		}

		@Override
		public List<String> getConfig(String pattern) {
			return delegate.getConfig(pattern);
		}

		@Override
		public void lSet(byte[] key, long index, byte[] value) {
			delegate.lSet(key, index, value);
		}

		@Override
		public Set<Tuple> zRangeWithScores(byte[] key, long begin, long end) {
			return delegate.zRangeWithScores(key, begin, end);
		}

		@Override
		public Long incrBy(byte[] key, long value) {
			return delegate.incrBy(key, value);
		}

		@Override
		public Set<byte[]> sDiff(byte[]... keys) {
			return delegate.sDiff(keys);
		}

		@Override
		public Long hLen(byte[] key) {
			return delegate.hLen(key);
		}

		@Override
		public List<Object> closePipeline() throws RedisPipelineException {
			return delegate.closePipeline();
		}

		@Override
		public void setConfig(String param, String value) {
			delegate.setConfig(param, value);
		}

		@Override
		public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
			return delegate.pExpireAt(key, unixTimeInMillis);
		}

		@Override
		public Long lRem(byte[] key, long count, byte[] value) {
			return delegate.lRem(key, count, value);
		}

		@Override
		public Double incrBy(byte[] key, double value) {
			return delegate.incrBy(key, value);
		}

		@Override
		public Long sDiffStore(byte[] destKey, byte[]... keys) {
			return delegate.sDiffStore(destKey, keys);
		}

		@Override
		public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
			return delegate.zRangeByScore(key, min, max);
		}

		@Override
		public Set<byte[]> hKeys(byte[] key) {
			return delegate.hKeys(key);
		}

		@Override
		public void resetConfigStats() {
			delegate.resetConfigStats();
		}

		@Override
		public Long decr(byte[] key) {
			return delegate.decr(key);
		}

		@Override
		public List<byte[]> hVals(byte[] key) {
			return delegate.hVals(key);
		}

		@Override
		public Boolean persist(byte[] key) {
			return delegate.persist(key);
		}

		@Override
		public byte[] lPop(byte[] key) {
			return delegate.lPop(key);
		}

		@Override
		public Set<byte[]> sMembers(byte[] key) {
			return delegate.sMembers(key);
		}

		@Override
		public Long decrBy(byte[] key, long value) {
			return delegate.decrBy(key, value);
		}

		@Override
		public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
			return delegate.zRangeByScoreWithScores(key, min, max);
		}

		@Override
		public Long time() {
			return delegate.time();
		}

		@Override
		public Map<byte[], byte[]> hGetAll(byte[] key) {
			return delegate.hGetAll(key);
		}

		@Override
		public Boolean move(byte[] key, int dbIndex) {
			return delegate.move(key, dbIndex);
		}

		@Override
		public byte[] sRandMember(byte[] key) {
			return delegate.sRandMember(key);
		}

		@Override
		public byte[] rPop(byte[] key) {
			return delegate.rPop(key);
		}

		@Override
		public void killClient(String host, int port) {
			delegate.killClient(host, port);
		}

		@Override
		public Long append(byte[] key, byte[] value) {
			return delegate.append(key, value);
		}

		@Override
		public Cursor<Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
			return delegate.hScan(key, options);
		}

		@Override
		public List<byte[]> sRandMember(byte[] key, long count) {
			return delegate.sRandMember(key, count);
		}

		@Override
		public Long ttl(byte[] key) {
			return delegate.ttl(key);
		}

		@Override
		public List<byte[]> bLPop(int timeout, byte[]... keys) {
			return delegate.bLPop(timeout, keys);
		}

		@Override
		public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
			return delegate.zRangeByScore(key, min, max, offset, count);
		}

		@Override
		public byte[] getRange(byte[] key, long begin, long end) {
			return delegate.getRange(key, begin, end);
		}

		@Override
		public void setClientName(byte[] name) {
			delegate.setClientName(name);
		}

		@Override
		public Long pTtl(byte[] key) {
			return delegate.pTtl(key);
		}

		@Override
		public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
			return delegate.sScan(key, options);
		}

		@Override
		public String getClientName() {
			return delegate.getClientName();
		}

		@Override
		public List<byte[]> sort(byte[] key, SortParameters params) {
			return delegate.sort(key, params);
		}

		@Override
		public List<byte[]> bRPop(int timeout, byte[]... keys) {
			return delegate.bRPop(timeout, keys);
		}

		@Override
		public void setRange(byte[] key, byte[] value, long offset) {
			delegate.setRange(key, value, offset);
		}

		@Override
		public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
			return delegate.zRangeByScoreWithScores(key, min, max, offset, count);
		}

		@Override
		public List<RedisClientInfo> getClientList() {
			return delegate.getClientList();
		}

		@Override
		public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
			return delegate.sort(key, params, storeKey);
		}

		@Override
		public Boolean getBit(byte[] key, long offset) {
			return delegate.getBit(key, offset);
		}

		@Override
		public void slaveOf(String host, int port) {
			delegate.slaveOf(host, port);
		}

		@Override
		public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
			return delegate.rPopLPush(srcKey, dstKey);
		}

		@Override
		public Set<byte[]> zRevRange(byte[] key, long begin, long end) {
			return delegate.zRevRange(key, begin, end);
		}

		@Override
		public byte[] dump(byte[] key) {
			return delegate.dump(key);
		}

		@Override
		public Boolean setBit(byte[] key, long offset, boolean value) {
			return delegate.setBit(key, offset, value);
		}

		@Override
		public void slaveOfNoOne() {
			delegate.slaveOfNoOne();
		}

		@Override
		public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
			delegate.restore(key, ttlInMillis, serializedValue);
		}

		@Override
		public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
			return delegate.bRPopLPush(timeout, srcKey, dstKey);
		}

		@Override
		public Set<Tuple> zRevRangeWithScores(byte[] key, long begin, long end) {
			return delegate.zRevRangeWithScores(key, begin, end);
		}

		@Override
		public Long bitCount(byte[] key) {
			return delegate.bitCount(key);
		}

		@Override
		public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
			return delegate.zRevRangeByScore(key, min, max);
		}

		@Override
		public Long bitCount(byte[] key, long begin, long end) {
			return delegate.bitCount(key, begin, end);
		}

		@Override
		public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
			return delegate.zRevRangeByScoreWithScores(key, min, max);
		}

		@Override
		public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
			return delegate.bitOp(op, destination, keys);
		}

		@Override
		public Long strLen(byte[] key) {
			return delegate.strLen(key);
		}

		@Override
		public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
			return delegate.zRevRangeByScore(key, min, max, offset, count);
		}

		@Override
		public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
			return delegate.zRevRangeByScoreWithScores(key, min, max, offset, count);
		}

		@Override
		public Long zCount(byte[] key, double min, double max) {
			return delegate.zCount(key, min, max);
		}

		@Override
		public Long zCard(byte[] key) {
			return delegate.zCard(key);
		}

		@Override
		public Double zScore(byte[] key, byte[] value) {
			return delegate.zScore(key, value);
		}

		@Override
		public Long zRemRange(byte[] key, long begin, long end) {
			return delegate.zRemRange(key, begin, end);
		}

		@Override
		public Long zRemRangeByScore(byte[] key, double min, double max) {
			return delegate.zRemRangeByScore(key, min, max);
		}

		@Override
		public Long zUnionStore(byte[] destKey, byte[]... sets) {
			return delegate.zUnionStore(destKey, sets);
		}

		@Override
		public Long zUnionStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
			return delegate.zUnionStore(destKey, aggregate, weights, sets);
		}

		@Override
		public Long zInterStore(byte[] destKey, byte[]... sets) {
			return delegate.zInterStore(destKey, sets);
		}

		@Override
		public Long zInterStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
			return delegate.zInterStore(destKey, aggregate, weights, sets);
		}

		@Override
		public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
			return delegate.zScan(key, options);
		}

		public RedisConnection getDelegate() {
			return delegate;
		}

		@Override
		protected RedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
			if (ObjectUtils.nullSafeEquals(this.activeNode, sentinel)) {
				return this.sentinelConnection;
			}
			return null;
		}

		@Override
		public <T> T evalSha(byte[] scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
			return delegate.evalSha(scriptSha, returnType, numKeys, keysAndArgs);
		}

		@Override
		public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
			return delegate.zRangeByScore(key, min, max);
		}

		@Override
		public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
			return delegate.zRangeByScore(key, min, max, offset, count);
		}

		@Override
		public Long pfAdd(byte[] key, byte[]... values) {
			return delegate.pfAdd(key, values);
		}

		@Override
		public Long pfCount(byte[]... keys) {
			return delegate.pfCount(keys);
		}

		@Override
		public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
			delegate.pfMerge(destinationKey, sourceKeys);
		}

		@Override
		public Set<byte[]> zRangeByLex(byte[] key) {
			return delegate.zRangeByLex(key);
		}

		@Override
		public Set<byte[]> zRangeByLex(byte[] key, Range range) {
			return delegate.zRangeByLex(key, range);
		}

		@Override
		public Set<byte[]> zRangeByLex(byte[] key, Range range, Limit limit) {
			return delegate.zRangeByLex(key, range, limit);
		}
	}
}
