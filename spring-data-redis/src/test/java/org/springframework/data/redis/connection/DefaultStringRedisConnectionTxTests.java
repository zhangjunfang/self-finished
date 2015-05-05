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
 * @author Jennifer Hickey
 * @author Christoph Strobl
 */
public class DefaultStringRedisConnectionTxTests extends DefaultStringRedisConnectionTests {

	@Override
	@Before
	public void setUp() {
		super.setUp();
		connection.setDeserializePipelineAndTxResults(true);
		when(nativeConnection.isQueueing()).thenReturn(true);
	}

	@Override
	@Test
	public void testAppend() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testAppend();
	}

	@Override
	@Test
	public void testAppendBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testAppendBytes();
	}

	@Override
	@Test
	public void testBlPopBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testBlPopBytes();
	}

	@Override
	@Test
	public void testBlPop() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testBlPop();
	}

	@Override
	@Test
	public void testBrPopBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testBrPopBytes();
	}

	@Override
	@Test
	public void testBrPop() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testBrPop();
	}

	@Override
	@Test
	public void testBrPopLPushBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testBrPopLPushBytes();
	}

	@Override
	@Test
	public void testBrPopLPush() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testBrPopLPush();
	}

	@Override
	@Test
	public void testDbSize() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testDbSize();
	}

	@Override
	@Test
	public void testDecrBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testDecrBytes();
	}

	@Override
	@Test
	public void testDecr() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testDecr();
	}

	@Override
	@Test
	public void testDecrByBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testDecrByBytes();
	}

	@Override
	@Test
	public void testDecrBy() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testDecrBy();
	}

	@Override
	@Test
	public void testDelBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testDelBytes();
	}

	@Override
	@Test
	public void testDel() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testDel();
	}

	@Override
	@Test
	public void testEchoBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testEchoBytes();
	}

	@Override
	@Test
	public void testEcho() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testEcho();
	}

	@Override
	@Test
	public void testExistsBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testExistsBytes();
	}

	@Override
	@Test
	public void testExists() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testExists();
	}

	@Override
	@Test
	public void testExpireBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testExpireBytes();
	}

	@Override
	@Test
	public void testExpire() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testExpire();
	}

	@Override
	@Test
	public void testExpireAtBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testExpireAtBytes();
	}

	@Override
	@Test
	public void testExpireAt() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testExpireAt();
	}

	@Override
	@Test
	public void testGetBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testGetBytes();
	}

	@Override
	@Test
	public void testGet() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testGet();
	}

	@Override
	@Test
	public void testGetBitBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testGetBitBytes();
	}

	@Override
	@Test
	public void testGetBit() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testGetBit();
	}

	@Override
	@Test
	public void testGetConfig() {
		List<String> results = Collections.singletonList("bar");
		doReturn(Arrays.asList(new Object[] { results })).when(nativeConnection).exec();
		super.testGetConfig();
	}

	@Override
	@Test
	public void testGetNativeConnection() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testGetNativeConnection();
	}

	@Override
	@Test
	public void testGetRangeBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testGetRangeBytes();
	}

	@Override
	@Test
	public void testGetRange() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testGetRange();
	}

	@Override
	@Test
	public void testGetSetBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testGetSetBytes();
	}

	@Override
	@Test
	public void testGetSet() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testGetSet();
	}

	@Override
	@Test
	public void testHDelBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testHDelBytes();
	}

	@Override
	@Test
	public void testHDel() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testHDel();
	}

	@Override
	@Test
	public void testHExistsBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testHExistsBytes();
	}

	@Override
	@Test
	public void testHExists() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testHExists();
	}

	@Override
	@Test
	public void testHGetBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testHGetBytes();
	}

	@Override
	@Test
	public void testHGet() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testHGet();
	}

	@Override
	@Test
	public void testHGetAllBytes() {
		doReturn(Arrays.asList(new Object[] { bytesMap })).when(nativeConnection).exec();
		super.testHGetAllBytes();
	}

	@Override
	@Test
	public void testHGetAll() {
		doReturn(Arrays.asList(new Object[] { bytesMap })).when(nativeConnection).exec();
		super.testHGetAll();
	}

	@Override
	@Test
	public void testHIncrByBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testHIncrByBytes();
	}

	@Override
	@Test
	public void testHIncrBy() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testHIncrBy();
	}

	@Override
	@Test
	public void testHIncrByDoubleBytes() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).exec();
		super.testHIncrByDoubleBytes();
	}

	@Override
	@Test
	public void testHIncrByDouble() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).exec();
		super.testHIncrByDouble();
	}

	@Override
	@Test
	public void testHKeysBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testHKeysBytes();
	}

	@Override
	@Test
	public void testHKeys() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testHKeys();
	}

	@Override
	@Test
	public void testHLenBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testHLenBytes();
	}

	@Override
	@Test
	public void testHLen() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testHLen();
	}

	@Override
	@Test
	public void testHMGetBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testHMGetBytes();
	}

	@Override
	@Test
	public void testHMGet() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testHMGet();
	}

	@Override
	@Test
	public void testHSetBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testHSetBytes();
	}

	@Override
	@Test
	public void testHSet() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testHSet();
	}

	@Override
	@Test
	public void testHSetNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testHSetNXBytes();
	}

	@Override
	@Test
	public void testHSetNX() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testHSetNX();
	}

	@Override
	@Test
	public void testHValsBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testHValsBytes();
	}

	@Override
	@Test
	public void testHVals() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testHVals();
	}

	@Override
	@Test
	public void testIncrBytes() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).exec();
		super.testIncrBytes();
	}

	@Override
	@Test
	public void testIncr() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).exec();
		super.testIncr();
	}

	@Override
	@Test
	public void testIncrByBytes() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).exec();
		super.testIncrByBytes();
	}

	@Override
	@Test
	public void testIncrBy() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).exec();
		super.testIncrBy();
	}

	@Override
	@Test
	public void testIncrByDoubleBytes() {
		doReturn(Arrays.asList(new Object[] { 2d })).when(nativeConnection).exec();
		super.testIncrByDoubleBytes();
	}

	@Override
	@Test
	public void testIncrByDouble() {
		doReturn(Arrays.asList(new Object[] { 2d })).when(nativeConnection).exec();
		super.testIncrByDouble();
	}

	@Override
	@Test
	public void testInfo() {
		Properties props = new Properties();
		props.put("foo", "bar");
		doReturn(Arrays.asList(new Object[] { props })).when(nativeConnection).exec();
		super.testInfo();
	}

	@Override
	@Test
	public void testInfoBySection() {
		Properties props = new Properties();
		props.put("foo", "bar");
		doReturn(Arrays.asList(new Object[] { props })).when(nativeConnection).exec();
		super.testInfoBySection();
	}

	@Override
	@Test
	public void testKeysBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testKeysBytes();
	}

	@Override
	@Test
	public void testKeys() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testKeys();
	}

	@Override
	@Test
	public void testLastSave() {
		doReturn(Arrays.asList(new Object[] { 6l })).when(nativeConnection).exec();
		super.testLastSave();
	}

	@Override
	@Test
	public void testLIndexBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testLIndexBytes();
	}

	@Override
	@Test
	public void testLIndex() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testLIndex();
	}

	@Override
	@Test
	public void testLInsertBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLInsertBytes();
	}

	@Override
	@Test
	public void testLInsert() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLInsert();
	}

	@Override
	@Test
	public void testLLenBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLLenBytes();
	}

	@Override
	@Test
	public void testLLen() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLLen();
	}

	@Override
	@Test
	public void testLPopBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		actual.add(connection.lPop(fooBytes));
		verifyResults(Arrays.asList(new Object[] { barBytes }));
	}

	@Override
	@Test
	public void testLPop() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testLPop();
	}

	@Override
	@Test
	public void testLPushBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLPushBytes();
	}

	@Override
	@Test
	public void testLPush() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLPush();
	}

	@Override
	@Test
	public void testLPushXBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLPushXBytes();
	}

	@Override
	@Test
	public void testLPushX() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLPushX();
	}

	@Override
	@Test
	public void testLRangeBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testLRangeBytes();
	}

	@Override
	@Test
	public void testLRange() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testLRange();
	}

	@Override
	@Test
	public void testLRemBytes() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLRemBytes();
	}

	@Override
	@Test
	public void testLRem() {
		doReturn(Arrays.asList(new Object[] { 8l })).when(nativeConnection).exec();
		super.testLRem();
	}

	@Override
	@Test
	public void testMGetBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testMGetBytes();
	}

	@Override
	@Test
	public void testMGet() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testMGet();
	}

	@Override
	@Test
	public void testMSetNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testMSetNXBytes();
	}

	@Override
	@Test
	public void testMSetNXString() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testMSetNXString();
	}

	@Override
	@Test
	public void testPersistBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testPersistBytes();
	}

	@Override
	@Test
	public void testPersist() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testPersist();
	}

	@Override
	@Test
	public void testMoveBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testMoveBytes();
	}

	@Override
	@Test
	public void testMove() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testMove();
	}

	@Override
	@Test
	public void testPing() {
		doReturn(Arrays.asList(new Object[] { "pong" })).when(nativeConnection).exec();
		super.testPing();
	}

	@Override
	@Test
	public void testPublishBytes() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).exec();
		super.testPublishBytes();
	}

	@Override
	@Test
	public void testPublish() {
		doReturn(Arrays.asList(new Object[] { 2l })).when(nativeConnection).exec();
		super.testPublish();
	}

	@Override
	@Test
	public void testRandomKey() {
		doReturn(Arrays.asList(new Object[] { fooBytes })).when(nativeConnection).exec();
		super.testRandomKey();
	}

	@Override
	@Test
	public void testRenameNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testRenameNXBytes();
	}

	@Override
	@Test
	public void testRenameNX() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testRenameNX();
	}

	@Override
	@Test
	public void testRPopBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testRPopBytes();
	}

	@Override
	@Test
	public void testRPop() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testRPop();
	}

	@Override
	@Test
	public void testRPopLPushBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testRPopLPushBytes();
	}

	@Override
	@Test
	public void testRPopLPush() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testRPopLPush();
	}

	@Override
	@Test
	public void testRPushBytes() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).exec();
		super.testRPushBytes();
	}

	@Override
	@Test
	public void testRPush() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).exec();
		super.testRPush();
	}

	@Override
	@Test
	public void testRPushXBytes() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).exec();
		super.testRPushXBytes();
	}

	@Override
	@Test
	public void testRPushX() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).exec();
		super.testRPushX();
	}

	@Override
	@Test
	public void testSAddBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testSAddBytes();
	}

	@Override
	@Test
	public void testSAdd() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testSAdd();
	}

	@Override
	@Test
	public void testSCardBytes() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).exec();
		super.testSCardBytes();
	}

	@Override
	@Test
	public void testSCard() {
		doReturn(Arrays.asList(new Object[] { 4l })).when(nativeConnection).exec();
		super.testSCard();
	}

	@Override
	@Test
	public void testSDiffBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSDiffBytes();
	}

	@Override
	@Test
	public void testSDiff() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSDiff();
	}

	@Override
	@Test
	public void testSDiffStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testSDiffStoreBytes();
	}

	@Override
	@Test
	public void testSDiffStore() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testSDiffStore();
	}

	@Override
	@Test
	public void testSetNXBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testSetNXBytes();
	}

	@Override
	@Test
	public void testSetNX() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testSetNX();
	}

	@Override
	@Test
	public void testSInterBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSInterBytes();
	}

	@Override
	@Test
	public void testSInter() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSInter();
	}

	@Override
	@Test
	public void testSInterStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testSInterStoreBytes();
	}

	@Override
	@Test
	public void testSInterStore() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testSInterStore();
	}

	@Override
	@Test
	public void testSIsMemberBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testSIsMemberBytes();
	}

	@Override
	@Test
	public void testSIsMember() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testSIsMember();
	}

	@Override
	@Test
	public void testSMembersBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSMembersBytes();
	}

	@Override
	@Test
	public void testSMembers() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSMembers();
	}

	@Override
	@Test
	public void testSMoveBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testSMoveBytes();
	}

	@Override
	@Test
	public void testSMove() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testSMove();
	}

	@Override
	@Test
	public void testSortStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testSortStoreBytes();
	}

	@Override
	@Test
	public void testSortStore() {
		doReturn(Arrays.asList(new Object[] { 3l })).when(nativeConnection).exec();
		super.testSortStore();
	}

	@Override
	@Test
	public void testSortBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testSortBytes();
	}

	@Override
	@Test
	public void testSort() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testSort();
	}

	@Override
	@Test
	public void testSPopBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testSPopBytes();
	}

	@Override
	@Test
	public void testSPop() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testSPop();
	}

	@Override
	@Test
	public void testSRandMemberBytes() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testSRandMemberBytes();
	}

	@Override
	@Test
	public void testSRandMember() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testSRandMember();
	}

	@Override
	@Test
	public void testSRandMemberCountBytes() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testSRandMemberCountBytes();
	}

	@Override
	@Test
	public void testSRandMemberCount() {
		doReturn(Arrays.asList(new Object[] { bytesList })).when(nativeConnection).exec();
		super.testSRandMemberCount();
	}

	@Override
	@Test
	public void testSRemBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testSRemBytes();
	}

	@Override
	@Test
	public void testSRem() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testSRem();
	}

	@Override
	@Test
	public void testStrLenBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testStrLenBytes();
	}

	@Override
	@Test
	public void testStrLen() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testStrLen();
	}

	@Override
	@Test
	public void testBitCountBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testBitCountBytes();
	}

	@Override
	@Test
	public void testBitCount() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testBitCount();
	}

	@Override
	@Test
	public void testBitCountRangeBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testBitCountRangeBytes();
	}

	@Override
	@Test
	public void testBitCountRange() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testBitCountRange();
	}

	@Override
	@Test
	public void testBitOpBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testBitOpBytes();
	}

	@Override
	@Test
	public void testBitOp() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testBitOp();
	}

	@Override
	@Test
	public void testSUnionBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSUnionBytes();
	}

	@Override
	@Test
	public void testSUnion() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testSUnion();
	}

	@Override
	@Test
	public void testSUnionStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testSUnionStoreBytes();
	}

	@Override
	@Test
	public void testSUnionStore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testSUnionStore();
	}

	@Override
	@Test
	public void testTtlBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testTtlBytes();
	}

	@Override
	@Test
	public void testTtl() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testTtl();
	}

	@Override
	@Test
	public void testTypeBytes() {
		doReturn(Arrays.asList(new Object[] { DataType.HASH })).when(nativeConnection).exec();
		super.testTypeBytes();
	}

	@Override
	@Test
	public void testType() {
		doReturn(Arrays.asList(new Object[] { DataType.HASH })).when(nativeConnection).exec();
		super.testType();
	}

	@Override
	@Test
	public void testZAddBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testZAddBytes();
	}

	@Override
	@Test
	public void testZAdd() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testZAdd();
	}

	@Override
	@Test
	public void testZAddMultipleBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testZAddMultipleBytes();
	}

	@Override
	@Test
	public void testZAddMultiple() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testZAddMultiple();
	}

	@Override
	@Test
	public void testZCardBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZCardBytes();
	}

	@Override
	@Test
	public void testZCard() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZCard();
	}

	@Override
	@Test
	public void testZCountBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZCountBytes();
	}

	@Override
	@Test
	public void testZCount() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZCount();
	}

	@Override
	@Test
	public void testZIncrByBytes() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).exec();
		super.testZIncrByBytes();
	}

	@Override
	@Test
	public void testZIncrBy() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).exec();
		super.testZIncrBy();
	}

	@Override
	@Test
	public void testZInterStoreAggWeightsBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZInterStoreAggWeightsBytes();
	}

	@Override
	@Test
	public void testZInterStoreAggWeights() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZInterStoreAggWeights();
	}

	@Override
	@Test
	public void testZInterStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZInterStoreBytes();
	}

	@Override
	@Test
	public void testZInterStore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZInterStore();
	}

	@Override
	@Test
	public void testZRangeBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRangeBytes();
	}

	@Override
	@Test
	public void testZRange() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRange();
	}

	@Override
	@Test
	public void testZRangeByScoreOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRangeByScoreOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRangeByScoreOffsetCount() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRangeByScoreOffsetCount();
	}

	@Override
	@Test
	public void testZRangeByScoreBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRangeByScoreBytes();
	}

	@Override
	@Test
	public void testZRangeByScore() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRangeByScore();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScoresOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRangeByScoreWithScoresOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScoresOffsetCount() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRangeByScoreWithScoresOffsetCount();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRangeByScoreWithScoresBytes();
	}

	@Override
	@Test
	public void testZRangeByScoreWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRangeByScoreWithScores();
	}

	@Override
	@Test
	public void testZRangeWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRangeWithScoresBytes();
	}

	@Override
	@Test
	public void testZRangeWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRangeWithScores();
	}

	@Override
	@Test
	public void testZRevRangeByScoreOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScoreOffsetCount() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreOffsetCount();
	}

	@Override
	@Test
	public void testZRevRangeByScoreBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScore() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRevRangeByScore();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScoresOffsetCountBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreWithScoresOffsetCountBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScoresOffsetCount() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreWithScoresOffsetCount();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreWithScoresBytes();
	}

	@Override
	@Test
	public void testZRevRangeByScoreWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRevRangeByScoreWithScores();
	}

	@Override
	@Test
	public void testZRankBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRankBytes();
	}

	@Override
	@Test
	public void testZRank() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRank();
	}

	@Override
	@Test
	public void testZRemBytes() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testZRemBytes();
	}

	@Override
	@Test
	public void testZRem() {
		doReturn(Arrays.asList(new Object[] { 1l })).when(nativeConnection).exec();
		super.testZRem();
	}

	@Override
	@Test
	public void testZRemRangeBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRemRangeBytes();
	}

	@Override
	@Test
	public void testZRemRange() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRemRange();
	}

	@Override
	@Test
	public void testZRemRangeByScoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRemRangeByScoreBytes();
	}

	@Override
	@Test
	public void testZRemRangeByScore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRemRangeByScore();
	}

	@Override
	@Test
	public void testZRevRangeBytes() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRevRangeBytes();
	}

	@Override
	@Test
	public void testZRevRange() {
		doReturn(Arrays.asList(new Object[] { bytesSet })).when(nativeConnection).exec();
		super.testZRevRange();
	}

	@Override
	@Test
	public void testZRevRangeWithScoresBytes() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRevRangeWithScoresBytes();
	}

	@Override
	@Test
	public void testZRevRangeWithScores() {
		doReturn(Arrays.asList(new Object[] { tupleSet })).when(nativeConnection).exec();
		super.testZRevRangeWithScores();
	}

	@Override
	@Test
	public void testZRevRankBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRevRankBytes();
	}

	@Override
	@Test
	public void testZRevRank() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZRevRank();
	}

	@Override
	@Test
	public void testZScoreBytes() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).exec();
		super.testZScoreBytes();
	}

	@Override
	@Test
	public void testZScore() {
		doReturn(Arrays.asList(new Object[] { 3d })).when(nativeConnection).exec();
		super.testZScore();
	}

	@Override
	@Test
	public void testZUnionStoreAggWeightsBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZUnionStoreAggWeightsBytes();
	}

	@Override
	@Test
	public void testZUnionStoreAggWeights() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZUnionStoreAggWeights();
	}

	@Override
	@Test
	public void testZUnionStoreBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZUnionStoreBytes();
	}

	@Override
	@Test
	public void testZUnionStore() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testZUnionStore();
	}

	@Override
	@Test
	public void testPExpireBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testPExpireBytes();
	}

	@Override
	@Test
	public void testPExpire() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testPExpire();
	}

	@Override
	@Test
	public void testPExpireAtBytes() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testPExpireAtBytes();
	}

	@Override
	@Test
	public void testPExpireAt() {
		doReturn(Arrays.asList(new Object[] { true })).when(nativeConnection).exec();
		super.testPExpireAt();
	}

	@Override
	@Test
	public void testPTtlBytes() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testPTtlBytes();
	}

	@Override
	@Test
	public void testPTtl() {
		doReturn(Arrays.asList(new Object[] { 5l })).when(nativeConnection).exec();
		super.testPTtl();
	}

	@Override
	@Test
	public void testDump() {
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		super.testDump();
	}

	@Override
	@Test
	public void testScriptLoadBytes() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testScriptLoadBytes();
	}

	@Override
	@Test
	public void testScriptLoad() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testScriptLoad();
	}

	@Override
	@Test
	public void testScriptExists() {
		List<Boolean> results = Collections.singletonList(true);
		doReturn(Arrays.asList(new Object[] { results })).when(nativeConnection).exec();
		super.testScriptExists();
	}

	@Override
	@Test
	public void testEvalBytes() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testEvalBytes();
	}

	@Override
	@Test
	public void testEval() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testEval();
	}

	@Override
	@Test
	public void testEvalShaBytes() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testEvalShaBytes();
	}

	@Override
	@Test
	public void testEvalSha() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testEvalSha();
	}

	@Override
	@Test
	public void testExecute() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testExecute();
	}

	@Override
	@Test
	public void testExecuteByteArgs() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testExecuteByteArgs();
	}

	@Override
	@Test
	public void testExecuteStringArgs() {
		doReturn(Arrays.asList(new Object[] { "foo" })).when(nativeConnection).exec();
		super.testExecuteStringArgs();
	}

	@Test
	public void testDisablePipelineAndTxDeserialize() {
		connection.setDeserializePipelineAndTxResults(false);
		doReturn(Arrays.asList(new Object[] { barBytes })).when(nativeConnection).exec();
		doReturn(Arrays.asList(new Object[] { Arrays.asList(new Object[] { barBytes }) })).when(nativeConnection)
				.closePipeline();
		doReturn(barBytes).when(nativeConnection).get(fooBytes);
		connection.get(foo);
		verifyResults(Arrays.asList(new Object[] { barBytes }));
	}

	@Test
	public void testTxResultsNotSameSizeAsResults() {
		// Only call one method, but return 2 results from nativeConnection.exec()
		// Emulates scenario where user has called some methods directly on the native connection
		// while tx is open
		doReturn(Arrays.asList(new Object[] { barBytes, 3l })).when(nativeConnection).exec();
		doReturn(barBytes).when(nativeConnection).get(fooBytes);
		connection.get(foo);
		verifyResults(Arrays.asList(new Object[] { barBytes, 3l }));
	}

	@Test
	public void testDiscard() {
		doReturn(Arrays.asList(new Object[] { fooBytes })).when(nativeConnection).exec();
		doReturn(Arrays.asList(new Object[] { Arrays.asList(new Object[] { fooBytes }) })).when(nativeConnection)
				.closePipeline();
		doReturn(barBytes).when(nativeConnection).get(fooBytes);
		doReturn(fooBytes).when(nativeConnection).get(barBytes);
		connection.get(foo);
		connection.discard();
		connection.get(bar);
		// Converted results of get(bar) should be included
		verifyResults(Arrays.asList(new Object[] { foo }));
	}

	/**
	 * @see DATAREDIS-206
	 */
	@Test
	@Override
	public void testTimeIsDelegatedCorrectlyToNativeConnection() {

		doReturn(Arrays.asList(new Object[] { 1L })).when(nativeConnection).exec();
		doReturn(Arrays.asList(new Object[] { Arrays.asList(new Object[] { 1L }) })).when(nativeConnection).closePipeline();
		super.testTimeIsDelegatedCorrectlyToNativeConnection();
	}

	@Override
	protected List<Object> getResults() {
		return connection.exec();
	}
}
