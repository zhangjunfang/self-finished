/*
 * Copyright 2013-2014 the original author or authors.
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test of {@link DefaultStringRedisConnection} that executes commands in a pipeline
 * 
 * @author Jennifer Hickey
 * @author Christoph Strobl
 */
public class DefaultStringRedisConnectionPipelineTests extends DefaultStringRedisConnectionTests {

	@Override
	@Before
	public void setUp() {
		super.setUp();
		connection.setDeserializePipelineAndTxResults(true);
		when(nativeConnection.isPipelined()).thenReturn(true);
	}

	@Override
	@Test
	public void testAppend() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testAppend();
	}

	@Override
	@Test
	public void testAppendBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testAppendBytes();
	}

	@Override
	@Test
	public void testBlPopBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testBlPopBytes();
	}

	@Override
	@Test
	public void testBlPop() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testBlPop();
	}

	@Override
	@Test
	public void testBrPopBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testBrPopBytes();
	}

	@Override
	@Test
	public void testBrPop() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testBrPop();
	}

	@Override
	@Test
	public void testBrPopLPushBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testBrPopLPushBytes();
	}

	@Override
	@Test
	public void testBrPopLPush() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testBrPopLPush();
	}

	@Override
	@Test
	public void testDbSize() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testDbSize();
	}

	@Override
	@Test
	public void testDecrBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testDecrBytes();
	}

	@Override
	@Test
	public void testDecr() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testDecr();
	}

	@Override
	@Test
	public void testDecrByBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testDecrByBytes();
	}

	@Override
	@Test
	public void testDecrBy() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testDecrBy();
	}

	@Override
	@Test
	public void testDelBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testDelBytes();
	}

	@Override
	@Test
	public void testDel() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testDel();
	}

	@Override
	@Test
	public void testEchoBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testEchoBytes();
	}

	@Override
	@Test
	public void testEcho() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testEcho();
	}

	@Test
	public void testTxResultsNotPipelined() {
		doReturn(true).when(nativeConnection).isQueueing();
		List<Object> results = Arrays.asList(new Object[] { bar, 8l });
		doReturn(Arrays.asList(new Object[] { results })).when(nativeConnection).closePipeline();
		doReturn(8l).when(nativeConnection).lLen(fooBytes);
		connection.lLen(fooBytes);
		connection.exec();
		// closePipeline should only return the results of exec, not of llen
		verifyResults(Arrays.asList(new Object[] { results }));
	}

	@Override
	@Test
	public void testExistsBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testExistsBytes();
	}

	@Override
	@Test
	public void testExists() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testExists();
	}

	@Override
	@Test
	public void testExpireBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testExpireBytes();
	}

	@Override
	@Test
	public void testExpire() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testExpire();
	}

	@Override
	@Test
	public void testExpireAtBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testExpireAtBytes();
	}

	@Override
	@Test
	public void testExpireAt() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testExpireAt();
	}

	@Override
	@Test
	public void testGetBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testGetBytes();
	}

	@Override
	@Test
	public void testGet() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testGet();
	}

	@Override
	@Test
	public void testGetBitBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testGetBitBytes();
	}

	@Override
	@Test
	public void testGetBit() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testGetBit();
	}

	@Override
	@Test
	public void testGetConfig() {
		List<String> results = Collections.singletonList("bar");
		doReturn(Arrays.asList(new Object[] { results })).when(nativeConnection).closePipeline();
		super.testGetConfig();
	}

	@Override
	@Test
	public void testGetNativeConnection() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testGetNativeConnection();
	}

	@Override
	@Test
	public void testGetRangeBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testGetRangeBytes();
	}

	@Override
	@Test
	public void testGetRange() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testGetRange();
	}

	@Override
	@Test
	public void testGetSetBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testGetSetBytes();
	}

	@Override
	@Test
	public void testGetSet() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testGetSet();
	}

	@Override
	@Test
	public void testHDelBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testHDelBytes();
	}

	@Override
	@Test
	public void testHDel() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testHDel();
	}

	@Override
	@Test
	public void testHExistsBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testHExistsBytes();
	}

	@Override
	@Test
	public void testHExists() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testHExists();
	}

	@Override
	@Test
	public void testHGetBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testHGetBytes();
	}

	@Override
	@Test
	public void testHGet() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testHGet();
	}

	@Override
	@Test
	public void testHGetAllBytes() {
		doReturn(Arrays.asList(new Object[] { bytesMap })).when(nativeConnection).closePipeline();
		super.testHGetAllBytes();
	}

	@Override
	@Test
	public void testHGetAll() {
		doReturn(Arrays.asList(new Object[] { bytesMap })).when(nativeConnection).closePipeline();
		super.testHGetAll();
	}

	@Override
	@Test
	public void testHIncrByBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testHIncrByBytes();
	}

	@Override
	@Test
	public void testHIncrBy() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testHIncrBy();
	}

	@Override
	@Test
	public void testHIncrByDoubleBytes() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).closePipeline();
		super.testHIncrByDoubleBytes();
	}

	@Override
	@Test
	public void testHIncrByDouble() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).closePipeline();
		super.testHIncrByDouble();
	}

	@Override
	@Test
	public void testHKeysBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testHKeysBytes();
	}

	@Override
	@Test
	public void testHKeys() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testHKeys();
	}

	@Override
	@Test
	public void testHLenBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testHLenBytes();
	}

	@Override
	@Test
	public void testHLen() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testHLen();
	}

	@Override
	@Test
	public void testHMGetBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testHMGetBytes();
	}

	@Override
	@Test
	public void testHMGet() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testHMGet();
	}

	@Override
	@Test
	public void testHSetBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testHSetBytes();
	}

	@Override
	@Test
	public void testHSet() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testHSet();
	}

	@Override
	@Test
	public void testHSetNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testHSetNXBytes();
	}

	@Override
	@Test
	public void testHSetNX() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testHSetNX();
	}

	@Override
	@Test
	public void testHValsBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testHValsBytes();
	}

	@Override
	@Test
	public void testHVals() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testHVals();
	}

	@Override
	@Test
	public void testIncrBytes() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).closePipeline();
		super.testIncrBytes();
	}

	@Override
	@Test
	public void testIncr() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).closePipeline();
		super.testIncr();
	}

	@Override
	@Test
	public void testIncrByBytes() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).closePipeline();
		super.testIncrByBytes();
	}

	@Override
	@Test
	public void testIncrBy() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).closePipeline();
		super.testIncrBy();
	}

	@Override
	@Test
	public void testIncrByDoubleBytes() {
		doReturn(Arrays.asList(new Object[] { 2d })).when(nativeConnection).closePipeline();
		super.testIncrByDoubleBytes();
	}

	@Override
	@Test
	public void testIncrByDouble() {
		doReturn(Arrays.asList(new Object[] { 2d })).when(nativeConnection).closePipeline();
		super.testIncrByDouble();
	}

	@Override
	@Test
	public void testInfo() {
		Properties props = new Properties();
		props.put("foo", "bar");
		doReturn(Arrays.asList(new Object[] { props })).when(nativeConnection).closePipeline();
		super.testInfo();
	}

	@Override
	@Test
	public void testInfoBySection() {
		Properties props = new Properties();
		props.put("foo", "bar");
		doReturn(Arrays.asList(new Object[] { props })).when(nativeConnection).closePipeline();
		super.testInfoBySection();
	}

	@Override
	@Test
	public void testKeysBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testKeysBytes();
	}

	@Override
	@Test
	public void testKeys() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testKeys();
	}

	@Override
	@Test
	public void testLastSave() {
		doReturn(Arrays.asList(new Object[] { 6l })).when(nativeConnection).closePipeline();
		super.testLastSave();
	}

	@Override
	@Test
	public void testLIndexBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testLIndexBytes();
	}

	@Override
	@Test
	public void testLIndex() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testLIndex();
	}

	@Override
	@Test
	public void testLInsertBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLInsertBytes();
	}

	@Override
	@Test
	public void testLInsert() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLInsert();
	}

	@Override
	@Test
	public void testLLenBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLLenBytes();
	}

	@Override
	@Test
	public void testLLen() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLLen();
	}

	@Override
	@Test
	public void testLPopBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		actual.add(connection.lPop(fooBytes));
		verifyResults(Arrays.asList(new Object[] { barBytes }));
	}

	@Override
	@Test
	public void testLPop() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testLPop();
	}

	@Override
	@Test
	public void testLPushBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLPushBytes();
	}

	@Override
	@Test
	public void testLPush() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLPush();
	}

	@Override
	@Test
	public void testLPushXBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLPushXBytes();
	}

	@Override
	@Test
	public void testLPushX() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLPushX();
	}

	@Override
	@Test
	public void testLRangeBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testLRangeBytes();
	}

	@Override
	@Test
	public void testLRange() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testLRange();
	}

	@Override
	@Test
	public void testLRemBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLRemBytes();
	}

	@Override
	@Test
	public void testLRem() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).closePipeline();
		super.testLRem();
	}

	@Override
	@Test
	public void testMGetBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testMGetBytes();
	}

	@Override
	@Test
	public void testMGet() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testMGet();
	}

	@Override
	@Test
	public void testMSetNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testMSetNXBytes();
	}

	@Override
	@Test
	public void testMSetNXString() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testMSetNXString();
	}

	@Override
	@Test
	public void testPersistBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testPersistBytes();
	}

	@Override
	@Test
	public void testPersist() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testPersist();
	}

	@Override
	@Test
	public void testMoveBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testMoveBytes();
	}

	@Override
	@Test
	public void testMove() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testMove();
	}

	@Override
	@Test
	public void testPing() {
		doReturn(Arrays.asList(new Object[] { "pong" })).when(nativeConnection).closePipeline();
		super.testPing();
	}

	@Override
	@Test
	public void testPublishBytes() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).closePipeline();
		super.testPublishBytes();
	}

	@Override
	@Test
	public void testPublish() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).closePipeline();
		super.testPublish();
	}

	@Override
	@Test
	public void testRandomKey() {
		doReturn(Arrays.asList(new Object[] { fooBytes })).when(nativeConnection).closePipeline();
		super.testRandomKey();
	}

	@Override
	@Test
	public void testRenameNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testRenameNXBytes();
	}

	@Override
	@Test
	public void testRenameNX() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testRenameNX();
	}

	@Override
	@Test
	public void testRPopBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testRPopBytes();
	}

	@Override
	@Test
	public void testRPop() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testRPop();
	}

	@Override
	@Test
	public void testRPopLPushBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testRPopLPushBytes();
	}

	@Override
	@Test
	public void testRPopLPush() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testRPopLPush();
	}

	@Override
	@Test
	public void testRPushBytes() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).closePipeline();
		super.testRPushBytes();
	}

	@Override
	@Test
	public void testRPush() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).closePipeline();
		super.testRPush();
	}

	@Override
	@Test
	public void testRPushXBytes() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).closePipeline();
		super.testRPushXBytes();
	}

	@Override
	@Test
	public void testRPushX() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).closePipeline();
		super.testRPushX();
	}

	@Override
	@Test
	public void testSAddBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testSAddBytes();
	}

	@Override
	@Test
	public void testSAdd() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testSAdd();
	}

	@Override
	@Test
	public void testSCardBytes() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).closePipeline();
		super.testSCardBytes();
	}

	@Override
	@Test
	public void testSCard() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).closePipeline();
		super.testSCard();
	}

	@Override
	@Test
	public void testSDiffBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSDiffBytes();
	}

	@Override
	@Test
	public void testSDiff() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSDiff();
	}

	@Override
	@Test
	public void testSDiffStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testSDiffStoreBytes();
	}

	@Override
	@Test
	public void testSDiffStore() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testSDiffStore();
	}

	@Override
	@Test
	public void testSetNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testSetNXBytes();
	}

	@Override
	@Test
	public void testSetNX() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testSetNX();
	}

	@Override
	@Test
	public void testSInterBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSInterBytes();
	}

	@Override
	@Test
	public void testSInter() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSInter();
	}

	@Override
	@Test
	public void testSInterStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testSInterStoreBytes();
	}

	@Override
	@Test
	public void testSInterStore() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testSInterStore();
	}

	@Override
	@Test
	public void testSIsMemberBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testSIsMemberBytes();
	}

	@Override
	@Test
	public void testSIsMember() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testSIsMember();
	}

	@Override
	@Test
	public void testSMembersBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSMembersBytes();
	}

	@Override
	@Test
	public void testSMembers() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSMembers();
	}

	@Override
	@Test
	public void testSMoveBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testSMoveBytes();
	}

	@Override
	@Test
	public void testSMove() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testSMove();
	}

	@Override
	@Test
	public void testSortStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testSortStoreBytes();
	}

	@Override
	@Test
	public void testSortStore() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).closePipeline();
		super.testSortStore();
	}

	@Override
	@Test
	public void testSortBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testSortBytes();
	}

	@Override
	@Test
	public void testSort() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testSort();
	}

	@Override
	@Test
	public void testSPopBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testSPopBytes();
	}

	@Override
	@Test
	public void testSPop() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testSPop();
	}

	@Override
	@Test
	public void testSRandMemberBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testSRandMemberBytes();
	}

	@Override
	@Test
	public void testSRandMember() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testSRandMember();
	}

	@Override
	@Test
	public void testSRandMemberCountBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testSRandMemberCountBytes();
	}

	@Override
	@Test
	public void testSRandMemberCount() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).closePipeline();
		super.testSRandMemberCount();
	}

	@Override
	@Test
	public void testSRemBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testSRemBytes();
	}

	@Override
	@Test
	public void testSRem() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testSRem();
	}

	@Override
	@Test
	public void testStrLenBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testStrLenBytes();
	}

	@Override
	@Test
	public void testStrLen() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testStrLen();
	}

	@Override
	@Test
	public void testBitCountBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testBitCountBytes();
	}

	@Override
	@Test
	public void testBitCount() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testBitCount();
	}

	@Override
	@Test
	public void testBitCountRangeBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testBitCountRangeBytes();
	}

	@Override
	@Test
	public void testBitCountRange() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testBitCountRange();
	}

	@Override
	@Test
	public void testBitOpBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testBitOpBytes();
	}

	@Override
	@Test
	public void testBitOp() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testBitOp();
	}

	@Override
	@Test
	public void testSUnionBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSUnionBytes();
	}

	@Override
	@Test
	public void testSUnion() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testSUnion();
	}

	@Override
	@Test
	public void testSUnionStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testSUnionStoreBytes();
	}

	@Override
	@Test
	public void testSUnionStore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testSUnionStore();
	}

	@Override
	@Test
	public void testTtlBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testTtlBytes();
	}

	@Override
	@Test
	public void testTtl() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testTtl();
	}

	@Override
	@Test
	public void testTypeBytes() {
		doReturn(Arrays.asList(new Object[] { DataType.HASH })).when(nativeConnection).closePipeline();
		super.testTypeBytes();
	}

	@Override
	@Test
	public void testType() {
		doReturn(Arrays.asList(new Object[] { DataType.HASH })).when(nativeConnection).closePipeline();
		super.testType();
	}

	@Override
	@Test
	public void testZAddBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testZAddBytes();
	}

	@Override
	@Test
	public void testZAdd() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testZAdd();
	}

	@Override
	@Test
	public void testZAddMultipleBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testZAddMultipleBytes();
	}

	@Override
	@Test
	public void testZAddMultiple() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testZAddMultiple();
	}

	@Override
	@Test
	public void testZCardBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZCardBytes();
	}

	@Override
	@Test
	public void testZCard() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZCard();
	}

	@Override
	@Test
	public void testZCountBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZCountBytes();
	}

	@Override
	@Test
	public void testZCount() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZCount();
	}

	@Override
	@Test
	public void testZIncrByBytes() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).closePipeline();
		super.testZIncrByBytes();
	}

	@Override
	@Test
	public void testZIncrBy() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).closePipeline();
		super.testZIncrBy();
	}

	@Override
	@Test
	public void testZInterStoreAggWeightsBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZInterStoreAggWeightsBytes();
	}

	@Override
	@Test
	public void testZInterStoreAggWeights() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZInterStoreAggWeights();
	}

	@Override
	@Test
	public void testZInterStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZInterStoreBytes();
	}

	@Override
	@Test
	public void testZInterStore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZInterStore();
	}

	@Override
	@Test
	public void testZRangeBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRangeBytes();
	}

	@Override
	@Test
	public void testZRange() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRange();
	}

	@Override
	@Test
	public void testZRangeByScoreOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRangeByScoreOffsetCount() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreOffsetCount();
	}

	@Override
	@Test
	public void testZRangeByScoreBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreBytes();
	}

	@Override
	@Test
	public void testZRangeByScore() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScore();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScoresOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreWithScoresOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScoresOffsetCount() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreWithScoresOffsetCount();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreWithScoresBytes();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRangeByScoreWithScores();
	}

	@Override
	@Test
	public void testZRangeWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRangeWithScoresBytes();
	}

	@Override
	@Test
	public void testZRangeWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRangeWithScores();
	}

	@Override
	@Test
	public void testZRevRangeByScoreOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScoreOffsetCount() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreOffsetCount();
	}

	@Override
	@Test
	public void testZRevRangeByScoreBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScore() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScore();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScoresOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreWithScoresOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScoresOffsetCount() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreWithScoresOffsetCount();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreWithScoresBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeByScoreWithScores();
	}

	@Override
	@Test
	public void testZRankBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRankBytes();
	}

	@Override
	@Test
	public void testZRank() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRank();
	}

	@Override
	@Test
	public void testZRemBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testZRemBytes();
	}

	@Override
	@Test
	public void testZRem() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).closePipeline();
		super.testZRem();
	}

	@Override
	@Test
	public void testZRemRangeBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRemRangeBytes();
	}

	@Override
	@Test
	public void testZRemRange() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRemRange();
	}

	@Override
	@Test
	public void testZRemRangeByScoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRemRangeByScoreBytes();
	}

	@Override
	@Test
	public void testZRemRangeByScore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRemRangeByScore();
	}

	@Override
	@Test
	public void testZRevRangeBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeBytes();
	}

	@Override
	@Test
	public void testZRevRange() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).closePipeline();
		super.testZRevRange();
	}

	@Override
	@Test
	public void testZRevRangeWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeWithScoresBytes();
	}

	@Override
	@Test
	public void testZRevRangeWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).closePipeline();
		super.testZRevRangeWithScores();
	}

	@Override
	@Test
	public void testZRevRankBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRevRankBytes();
	}

	@Override
	@Test
	public void testZRevRank() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZRevRank();
	}

	@Override
	@Test
	public void testZScoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).closePipeline();
		super.testZScoreBytes();
	}

	@Override
	@Test
	public void testZScore() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).closePipeline();
		super.testZScore();
	}

	@Override
	@Test
	public void testZUnionStoreAggWeightsBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZUnionStoreAggWeightsBytes();
	}

	@Override
	@Test
	public void testZUnionStoreAggWeights() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZUnionStoreAggWeights();
	}

	@Override
	@Test
	public void testZUnionStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZUnionStoreBytes();
	}

	@Override
	@Test
	public void testZUnionStore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testZUnionStore();
	}

	@Override
	@Test
	public void testPExpireBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testPExpireBytes();
	}

	@Override
	@Test
	public void testPExpire() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testPExpire();
	}

	@Override
	@Test
	public void testPExpireAtBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testPExpireAtBytes();
	}

	@Override
	@Test
	public void testPExpireAt() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).closePipeline();
		super.testPExpireAt();
	}

	@Override
	@Test
	public void testPTtlBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testPTtlBytes();
	}

	@Override
	@Test
	public void testPTtl() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).closePipeline();
		super.testPTtl();
	}

	@Override
	@Test
	public void testDump() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		super.testDump();
	}

	@Override
	@Test
	public void testScriptLoadBytes() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testScriptLoadBytes();
	}

	@Override
	@Test
	public void testScriptLoad() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testScriptLoad();
	}

	@Override
	@Test
	public void testScriptExists() {
		List<Boolean> results = Collections.singletonList(true);
		doReturn(Arrays.asList(new Object[] { results })).when(nativeConnection).closePipeline();
		super.testScriptExists();
	}

	@Override
	@Test
	public void testEvalBytes() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testEvalBytes();
	}

	@Override
	@Test
	public void testEval() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testEval();
	}

	@Override
	@Test
	public void testEvalShaBytes() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testEvalShaBytes();
	}

	@Override
	@Test
	public void testEvalSha() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testEvalSha();
	}

	@Override
	@Test
	public void testExecute() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testExecute();
	}

	@Override
	@Test
	public void testExecuteByteArgs() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testExecuteByteArgs();
	}

	@Override
	@Test
	public void testExecuteStringArgs() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).closePipeline();
		super.testExecuteStringArgs();
	}

	@Test
	public void testDisablePipelineDeserialize() {
		connection.setDeserializePipelineAndTxResults(false);
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).closePipeline();
		doReturn(barBytes).when(nativeConnection).get(fooBytes);
		connection.get(foo);
		verifyResults(Arrays.asList(new Object[] { barBytes }));
	}

	@Test
	public void testPipelineNotSameSizeAsResults() {
		// Only call one method, but return 2 results from nativeConnection.closePipeline()
		// Emulates scenario where user has called some methods directly on the native connection
		// while pipeline is open
		doReturn(Arrays.asList(new Object[] { barBytes, 3l })).when(nativeConnection).closePipeline();
		doReturn(barBytes).when(nativeConnection).get(fooBytes);
		connection.get(foo);
		verifyResults(Arrays.asList(new Object[] { barBytes, 3l }));
	}

	/**
	 * @see DATAREDIS-206
	 */
	@Test
	@Override
	public void testTimeIsDelegatedCorrectlyToNativeConnection() {

		doReturn(Arrays.asList(1L)).when(nativeConnection).closePipeline();
		super.testTimeIsDelegatedCorrectlyToNativeConnection();
	}

	@Override
	protected List<Object> getResults() {
		return connection.closePipeline();
	}
}
