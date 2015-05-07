/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.data.redis.connection.jedis;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.AbstractConnectionIntegrationTests;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.test.util.RedisSentinelRule;
import org.springframework.test.annotation.IfProfileValue;

/**
 * @author Christoph Strobl
 * @author Thomas Darimont
 */
public class JedisSentinelIntegrationTests extends AbstractConnectionIntegrationTests {
	
	private static final String MASTER_NAME = "mymaster";
	private static final RedisServer SENTINEL_0 = new RedisServer("127.0.0.1", 26379);
	private static final RedisServer SENTINEL_1 = new RedisServer("127.0.0.1", 26380);
	
	private static final RedisServer SLAVE_0 = new RedisServer("127.0.0.1", 6380);
	private static final RedisServer SLAVE_1 = new RedisServer("127.0.0.1", 6381);
	
	private static final RedisSentinelConfiguration SENTINEL_CONFIG = new RedisSentinelConfiguration() //
			.master(MASTER_NAME)
			.sentinel(SENTINEL_0)
			.sentinel(SENTINEL_1);

	public @Rule RedisSentinelRule sentinelRule = RedisSentinelRule.forConfig(SENTINEL_CONFIG).oneActive();

	@Override
	@Before
	public void setUp() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(SENTINEL_CONFIG);
		jedisConnectionFactory.afterPropertiesSet();
		connectionFactory = jedisConnectionFactory;
		super.setUp();
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
		((JedisConnectionFactory) connectionFactory).destroy();
	}

	@Override
	@Test
	@Ignore
	public void testScriptKill() throws Exception {
		super.testScriptKill();
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalReturnSingleError() {
		connection.eval("return redis.call('expire','foo')", ReturnType.BOOLEAN, 0);
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalArrayScriptError() {
		super.testEvalArrayScriptError();
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalShaNotFound() {
		connection.evalSha("somefakesha", ReturnType.VALUE, 2, "key1", "key2");
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testEvalShaArrayError() {
		super.testEvalShaArrayError();
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testRestoreBadData() {
		super.testRestoreBadData();
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testRestoreExistingKey() {
		super.testRestoreExistingKey();
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void testExecWithoutMulti() {
		super.testExecWithoutMulti();
	}

	@Override
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void testErrorInTx() {
		super.testErrorInTx();
	}

	/**
	 * @see DATAREDIS-330
	 */
	@Test
	public void shouldReadMastersCorrectly() {

		List<RedisServer> servers = (List<RedisServer>) connectionFactory.getSentinelConnection().masters();
		assertThat(servers.size(), is(1));
		assertThat(servers.get(0).getName(),is(MASTER_NAME));
	}
	
	/**
	 * @see DATAREDIS-330
	 */
	@Test
	public void shouldReadSlavesOfMastersCorrectly() {

		RedisSentinelConnection sentinelConnection = connectionFactory.getSentinelConnection();
		
		List<RedisServer> servers = (List<RedisServer>) sentinelConnection.masters();
		assertThat(servers.size(), is(1));
		
		Collection<RedisServer> slaves = sentinelConnection.slaves(servers.get(0));
		assertThat(slaves.size(), is(2));
		assertThat(slaves, hasItems(SLAVE_0, SLAVE_1));
	}

}