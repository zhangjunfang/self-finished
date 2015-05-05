package org.springframework.data.redis.connection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.IfProfileValue;

abstract public class AbstractConnectionTransactionIntegrationTests extends AbstractConnectionIntegrationTests {

	@Override
	@Ignore
	public void testMultiDiscard() {}

	@Override
	@Ignore
	public void testMultiExec() {}

	@Override
	@Ignore
	public void testUnwatch() {}

	@Override
	@Ignore
	public void testWatch() {}

	@Override
	@Ignore
	@Test
	public void testExecWithoutMulti() {}

	@Override
	@Ignore
	@Test
	public void testErrorInTx() {}

	/*
	 * Using blocking ops inside a tx does not make a lot of sense as it would require blocking the
	 * entire server in order to execute the block atomically, which in turn does not allow other
	 * clients to perform a push operation. *
	 */

	@Override
	@Ignore
	public void testBLPop() {}

	@Override
	@Ignore
	public void testBRPop() {}

	@Override
	@Ignore
	public void testBRPopLPush() {}

	@Override
	@Ignore
	public void testBLPopTimeout() {}

	@Override
	@Ignore
	public void testBRPopTimeout() {}

	@Override
	@Ignore
	public void testBRPopLPushTimeout() {}

	@Override
	@Ignore("Pub/Sub not supported with transactions")
	public void testPubSubWithNamedChannels() throws Exception {}

	@Override
	@Ignore("Pub/Sub not supported with transactions")
	public void testPubSubWithPatterns() throws Exception {}

	@Override
	@Ignore
	public void testNullKey() throws Exception {}

	@Override
	@Ignore
	public void testNullValue() throws Exception {}

	@Override
	@Ignore
	public void testHashNullKey() throws Exception {}

	@Override
	@Ignore
	public void testHashNullValue() throws Exception {}

	@Test(expected = UnsupportedOperationException.class)
	public void testWatchWhileInTx() {
		connection.watch("foo".getBytes());
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@IfProfileValue(name = "redisVersion", value = "2.6+")
	public void testScriptKill() {
		// Impossible to call script kill in a tx because you can't issue the
		// exec command while Redis is running a script
		connection.scriptKill();
	}

	@Override
	protected void initConnection() {
		connection.multi();
	}

	@Override
	protected List<Object> getResults() {
		return connection.exec();
	}

	@Override
	protected void verifyResults(List<Object> expected) {
		List<Object> expectedTx = new ArrayList<Object>();
		for (int i = 0; i < actual.size(); i++) {
			expectedTx.add(null);
		}
		assertEquals(expectedTx, actual);
		List<Object> results = getResults();
		assertEquals(expected, results);
	}
}
