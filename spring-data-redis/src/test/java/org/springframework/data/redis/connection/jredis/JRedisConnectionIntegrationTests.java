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

package org.springframework.data.redis.connection.jredis;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsInstanceOf;
import org.jredis.JRedis;
import org.jredis.protocol.BulkResponse;
import org.jredis.ri.alphazero.protocol.SyncProtocol.SyncMultiBulkResponse;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.SettingsUtils;
import org.springframework.data.redis.connection.AbstractConnectionIntegrationTests;
import org.springframework.data.redis.connection.DefaultSortParameters;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.test.util.RelaxedJUnit4ClassRunner;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;

/**
 * Integration test of {@link JredisConnection}
 * 
 * @author Costin Leau
 * @author Jennifer Hickey
 * @author Christoph Strobl
 */
@RunWith(RelaxedJUnit4ClassRunner.class)
@ContextConfiguration
public class JRedisConnectionIntegrationTests extends AbstractConnectionIntegrationTests {

	@Override
	@After
	public void tearDown() {
		try {
			connection.flushDb();
			connection.close();
		} catch (DataAccessException e) {
			// Jredis closes a connection on Exception (which some tests
			// intentionally throw)
			// Attempting to close the connection again will result in error
			System.out.println("Connection already closed");
		}
		connection = null;
	}

	@Override
	@Ignore("Pub/Sub not supported")
	public void testPubSubWithPatterns() {}

	@Override
	@Ignore("Pub/Sub not supported")
	public void testPubSubWithNamedChannels() {}

	@Override
	@Ignore("https://github.com/alphazero/jredis/issues/64 Protocol error: expected '$' got '*' on mset")
	public void testMSet() {}

	@Override
	@Ignore("https://github.com/alphazero/jredis/issues/64 Protocol error: expected '$' got '*' on mset")
	public void testMSetNx() {}

	@Override
	@Ignore("https://github.com/alphazero/jredis/issues/64 Protocol error: expected '$' got '*' on mset")
	public void testMSetNxFailure() {}

	@Override
	@Ignore("JRedis casts to int")
	public void testIncrDecrByLong() {}

	@Override
	@Ignore("Ping returns status response instead of value response")
	public void testExecuteNoArgs() {}

	@Test
	public void testConnectionClosesWhenNotPooled() {
		connection.close();
		try {
			connection.ping();
			fail("Expected RedisConnectionFailureException trying to use a closed connection");
		} catch (RedisConnectionFailureException e) {}
	}

	@Test
	public void testConnectionStaysOpenWhenPooled() {
		JredisConnectionFactory factory2 = new JredisConnectionFactory(new JredisPool(SettingsUtils.getHost(),
				SettingsUtils.getPort()));
		RedisConnection conn2 = factory2.getConnection();
		conn2.close();
		conn2.ping();
	}

	@Test
	public void testConnectionNotReturnedOnException() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(1);
		config.setMaxWaitMillis(1);
		JredisConnectionFactory factory2 = new JredisConnectionFactory(new JredisPool(SettingsUtils.getHost(),
				SettingsUtils.getPort(), config));
		RedisConnection conn2 = factory2.getConnection();
		((JRedis) conn2.getNativeConnection()).quit();
		try {
			conn2.ping();
			fail("Expected RedisConnectionFailureException trying to use a closed connection");
		} catch (RedisConnectionFailureException e) {}
		conn2.close();
		// Verify we get a new connection from the pool and not the broken one
		RedisConnection conn3 = factory2.getConnection();
		conn3.ping();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testMultiExec() throws Exception {
		super.testMultiExec();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testMultiAlreadyInTx() throws Exception {
		super.testMultiAlreadyInTx();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testMultiDiscard() throws Exception {
		super.testMultiDiscard();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testWatch() throws Exception {
		super.testWatch();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testUnwatch() throws Exception {
		super.testUnwatch();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testErrorInTx() {
		super.testErrorInTx();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testExecWithoutMulti() {
		super.testExecWithoutMulti();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBLPop() {
		super.testBLPop();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBRPop() {
		super.testBRPop();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testLInsert() {
		super.testLInsert();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBRPopLPush() {
		super.testBRPopLPush();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testLPushX() {
		super.testLPushX();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testRPushX() {
		super.testRPushX();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testGetRangeSetRange() {
		super.testGetRangeSetRange();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testStrLen() {
		super.testStrLen();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testGetConfig() {
		super.testGetConfig();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZInterStore() {
		super.testZInterStore();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZInterStoreAggWeights() {
		super.testZInterStoreAggWeights();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRangeWithScores() {
		super.testZRangeWithScores();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRangeByScoreOffsetCount() {
		super.testZRangeByScoreOffsetCount();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRangeByScoreWithScores() {
		super.testZRangeByScoreWithScores();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRangeByScoreWithScoresOffsetCount() {
		super.testZRangeByScoreWithScoresOffsetCount();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeWithScores() {
		super.testZRevRangeWithScores();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZUnionStore() {
		super.testZUnionStore();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZUnionStoreAggWeights() {
		super.testZUnionStoreAggWeights();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testHSetNX() throws Exception {
		super.testHSetNX();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testHIncrBy() {
		super.testHIncrBy();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testHMGetSet() {
		super.testHMGetSet();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPersist() throws Exception {
		super.testPersist();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testSetEx() throws Exception {
		super.testSetEx();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBRPopTimeout() throws Exception {
		super.testBRPopTimeout();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBLPopTimeout() throws Exception {
		super.testBLPopTimeout();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBRPopLPushTimeout() throws Exception {
		super.testBRPopLPushTimeout();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScore() {
		super.testZRevRangeByScore();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScoreOffsetCount() {
		super.testZRevRangeByScoreOffsetCount();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScoreWithScores() {
		super.testZRevRangeByScoreWithScores();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRevRangeByScoreWithScoresOffsetCount() {
		super.testZRevRangeByScoreWithScoresOffsetCount();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testSelect() {
		super.testSelect();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPExpire() {
		super.testPExpire();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPExpireKeyNotExists() {
		super.testPExpireKeyNotExists();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPExpireAt() {
		super.testPExpireAt();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPExpireAtKeyNotExists() {
		super.testPExpireAtKeyNotExists();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPTtl() {
		super.testPTtl();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPTtlNoExpire() {
		super.testPTtlNoExpire();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testDumpAndRestore() {
		super.testDumpAndRestore();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testDumpNonExistentKey() {
		super.testDumpNonExistentKey();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testRestoreBadData() {
		super.testRestoreBadData();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testRestoreExistingKey() {
		super.testRestoreExistingKey();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testRestoreTtl() {
		super.testRestoreTtl();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitCount() {
		super.testBitCount();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitCountInterval() {
		super.testBitCountInterval();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitCountNonExistentKey() {
		super.testBitCountNonExistentKey();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitOpAnd() {
		super.testBitOpAnd();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitOpOr() {
		super.testBitOpOr();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitOpXOr() {
		super.testBitOpXOr();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testBitOpNot() {
		super.testBitOpNot();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testHIncrByDouble() {
		super.testHIncrByDouble();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testHashIncrDecrByLong() {
		super.testHashIncrDecrByLong();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testIncrByDouble() {
		super.testIncrByDouble();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testScriptLoadEvalSha() {
		super.testScriptLoadEvalSha();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalShaArrayStrings() {
		super.testEvalShaArrayStrings();
	}
	
	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalShaArrayBytes() {
		super.testEvalShaArrayBytes();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalShaNotFound() {
		super.testEvalShaNotFound();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalShaArrayError() {
		super.testEvalShaArrayError();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalArrayScriptError() {
		super.testEvalArrayScriptError();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnString() {
		super.testEvalReturnString();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnNumber() {
		super.testEvalReturnNumber();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnSingleOK() {
		super.testEvalReturnSingleOK();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnSingleError() {
		super.testEvalReturnSingleError();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnFalse() {
		super.testEvalReturnFalse();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnTrue() {
		super.testEvalReturnTrue();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnArrayStrings() {
		super.testEvalReturnArrayStrings();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnArrayNumbers() {
		super.testEvalReturnArrayNumbers();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnArrayOKs() {
		super.testEvalReturnArrayOKs();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnArrayFalses() {
		super.testEvalReturnArrayFalses();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnArrayTrues() {
		super.testEvalReturnArrayTrues();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testScriptExists() {
		super.testScriptExists();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testScriptKill() throws Exception {
		connection.scriptKill();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testScriptFlush() {
		connection.scriptFlush();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testSRandMemberCount() {
		super.testSRandMemberCount();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testSRandMemberCountKeyNotExists() {
		super.testSRandMemberCountKeyNotExists();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testSRandMemberCountNegative() {
		super.testSRandMemberCountNegative();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testInfoBySection() throws Exception {
		super.testInfoBySection();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testHDelMultiple() {
		super.testHDelMultiple();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testLPushMultiple() {
		super.testLPushMultiple();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testRPushMultiple() {
		super.testRPushMultiple();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testSAddMultiple() {
		super.testSAddMultiple();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testSRemMultiple() {
		super.testSRemMultiple();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZAddMultiple() {
		super.testZAddMultiple();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testZRemMultiple() {
		super.testZRemMultiple();
	}

	// Jredis returns null for rPush and lPush
	@Override
	@Test
	public void testLLen() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "big");
		connection.rPush("PopList", "world");
		connection.rPush("PopList", "hello");
		actual.add(connection.lLen("PopList"));
		verifyResults(Arrays.asList(new Object[] { 4l }));
	}

	@Override
	@Test
	public void testSort() {
		connection.rPush("sortlist", "foo");
		connection.rPush("sortlist", "bar");
		connection.rPush("sortlist", "baz");
		assertEquals(Arrays.asList(new String[] { "bar", "baz", "foo" }),
				connection.sort("sortlist", new DefaultSortParameters(null, Order.ASC, true)));
	}

	@Override
	@Test
	public void testSortStore() {
		connection.rPush("sortlist", "foo");
		connection.rPush("sortlist", "bar");
		connection.rPush("sortlist", "baz");
		assertEquals(Long.valueOf(3),
				connection.sort("sortlist", new DefaultSortParameters(null, Order.ASC, true), "newlist"));
		assertEquals(Arrays.asList(new String[] { "bar", "baz", "foo" }), connection.lRange("newlist", 0, 9));
	}

	@Override
	@Test
	public void testSortNullParams() {
		connection.rPush("sortlist", "5");
		connection.rPush("sortlist", "2");
		connection.rPush("sortlist", "3");
		actual.add(connection.sort("sortlist", null));
		verifyResults(Arrays.asList(new Object[] { Arrays.asList(new String[] { "2", "3", "5" }) }));
	}

	@Override
	@Test
	public void testSortStoreNullParams() {
		connection.rPush("sortlist", "9");
		connection.rPush("sortlist", "3");
		connection.rPush("sortlist", "5");
		actual.add(connection.sort("sortlist", null, "newlist"));
		actual.add(connection.lRange("newlist", 0, 9));
		verifyResults(Arrays.asList(new Object[] { 3l, Arrays.asList(new String[] { "3", "5", "9" }) }));
	}

	@Override
	@Test
	public void testLPop() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "world");
		assertEquals("hello", connection.lPop("PopList"));
	}

	@Override
	@Test
	public void testLRem() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "big");
		connection.rPush("PopList", "world");
		connection.rPush("PopList", "hello");
		assertEquals(Long.valueOf(2), connection.lRem("PopList", 2, "hello"));
		assertEquals(Arrays.asList(new String[] { "big", "world" }), connection.lRange("PopList", 0, -1));
	}

	@Override
	@Test
	public void testLSet() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "big");
		connection.rPush("PopList", "world");
		connection.lSet("PopList", 1, "cruel");
		assertEquals(Arrays.asList(new String[] { "hello", "cruel", "world" }), connection.lRange("PopList", 0, -1));
	}

	@Override
	@Test
	public void testLTrim() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "big");
		connection.rPush("PopList", "world");
		connection.lTrim("PopList", 1, -1);
		assertEquals(Arrays.asList(new String[] { "big", "world" }), connection.lRange("PopList", 0, -1));
	}

	@Override
	@Test
	public void testRPop() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "world");
		assertEquals("world", connection.rPop("PopList"));
	}

	@Override
	@Test
	public void testRPopLPush() {
		connection.rPush("PopList", "hello");
		connection.rPush("PopList", "world");
		connection.rPush("pop2", "hey");
		assertEquals("world", connection.rPopLPush("PopList", "pop2"));
		assertEquals(Arrays.asList(new String[] { "hello" }), connection.lRange("PopList", 0, -1));
		assertEquals(Arrays.asList(new String[] { "world", "hey" }), connection.lRange("pop2", 0, -1));
	}

	@Override
	@Test
	public void testLIndex() {
		connection.lPush("testylist", "foo");
		assertEquals("foo", connection.lIndex("testylist", 0));
	}

	@Override
	@Test
	public void testLPush() throws Exception {
		connection.lPush("testlist", "bar");
		connection.lPush("testlist", "baz");
		assertEquals(Arrays.asList(new String[] { "baz", "bar" }), connection.lRange("testlist", 0, -1));
	}

	@Override
	@Test
	public void testExecute() {
		connection.set("foo", "bar");
		BulkResponse response = (BulkResponse) connection.execute("GET", "foo".getBytes());
		assertEquals("bar", stringSerializer.deserialize(response.getBulkData()));
	}

	@Override
	@Test
	public void testSDiffStore() {
		actual.add(connection.sAdd("myset", "foo"));
		actual.add(connection.sAdd("myset", "bar"));
		actual.add(connection.sAdd("otherset", "bar"));
		actual.add(connection.sDiffStore("thirdset", "myset", "otherset"));
		actual.add(connection.sMembers("thirdset"));
		// JRedis returns void for sDiffStore, so we always return -1
		verifyResults(Arrays
				.asList(new Object[] { 1l, 1l, 1l, -1l, new HashSet<String>(Collections.singletonList("foo")) }));
	}

	@Override
	@Test
	public void testSInterStore() {
		actual.add(connection.sAdd("myset", "foo"));
		actual.add(connection.sAdd("myset", "bar"));
		actual.add(connection.sAdd("otherset", "bar"));
		actual.add(connection.sInterStore("thirdset", "myset", "otherset"));
		actual.add(connection.sMembers("thirdset"));
		// JRedis returns void for sInterStore, so we always return -1
		verifyResults(Arrays
				.asList(new Object[] { 1l, 1l, 1l, -1l, new HashSet<String>(Collections.singletonList("bar")) }));
	}

	@Override
	@Test
	public void testSUnionStore() {
		actual.add(connection.sAdd("myset", "foo"));
		actual.add(connection.sAdd("myset", "bar"));
		actual.add(connection.sAdd("otherset", "bar"));
		actual.add(connection.sAdd("otherset", "baz"));
		actual.add(connection.sUnionStore("thirdset", "myset", "otherset"));
		actual.add(connection.sMembers("thirdset"));
		// JRedis returns void for sUnionStore, so we always return -1
		verifyResults(Arrays.asList(new Object[] { 1l, 1l, 1l, 1l, -1l,
				new HashSet<String>(Arrays.asList(new String[] { "foo", "bar", "baz" })) }));
	}

	@Override
	@Test
	public void testMove() {
		connection.set("foo", "bar");
		actual.add(connection.move("foo", 1));
		verifyResults(Arrays.asList(new Object[] { true }));
		// JRedis does not support select() on existing conn, create new one
		JredisConnectionFactory factory2 = new JredisConnectionFactory();
		factory2.setDatabase(1);
		factory2.afterPropertiesSet();
		StringRedisConnection conn2 = new DefaultStringRedisConnection(factory2.getConnection());
		try {
			assertEquals("bar", conn2.get("foo"));
		} finally {
			if (conn2.exists("foo")) {
				conn2.del("foo");
			}
			conn2.close();
		}
	}

	/**
	 * @see DATAREDIS-206
	 */
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testGetTimeShouldRequestServerTime() {
		super.testGetTimeShouldRequestServerTime();
	}

	/**
	 * @see DATAREDIS-285
	 */
	@Test
	public void testExecuteShouldConvertArrayReplyCorrectly() {
		connection.set("spring", "awesome");
		connection.set("data", "cool");
		connection.set("redis", "supercalifragilisticexpialidocious");

		Object result = connection.execute("MGET", "spring".getBytes(), "data".getBytes(), "redis".getBytes());

		assertThat(result, IsInstanceOf.instanceOf(SyncMultiBulkResponse.class));

		List<byte[]> data = ((SyncMultiBulkResponse) result).getMultiBulkData();
		assertThat(
				data,
				IsCollectionContaining.hasItems("awesome".getBytes(), "cool".getBytes(),
						"supercalifragilisticexpialidocious".getBytes()));
	}

	/**
	 * @see DATAREDIS-271
	 */
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testPsetEx() throws Exception {
		super.testPsetEx();
	}

	/**
	 * @see DATAREDIS-269
	 */
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void clientSetNameWorksCorrectly() {
		super.clientSetNameWorksCorrectly();
	}

	/**
	 * @see DATAREDIS-268
	 */
	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testListClientsContainsAtLeastOneElement() {
		super.testListClientsContainsAtLeastOneElement();
	}
}
