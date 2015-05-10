package org.tinygroup.cepcoreremoteimpl.test.testcase;

import java.util.ArrayList;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinyrunner.Runner;

public class TestClient {
	private static Logger logger = LoggerFactory.getLogger(TestClient.class);

	public static void main(String[] args) throws Exception {
		Runner.init("application.xml", new ArrayList<String>());
		CEPCore cep = BeanContainerFactory.getBeanContainer(
				TestServer.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		Thread.sleep(2000);
//		NodeOperator nodeOp = BeanContainerFactory.getBeanContainer(
//				TestServer.class.getClassLoader()).getBean("node");
		long beginTime = System.currentTimeMillis();
		logger.logMessage(LogLevel.INFO, beginTime + "");
		Event t = getEvent();
		cep.process(t);
		logger.logMessage(LogLevel.INFO, t.getServiceRequest().getContext()
				.get("result")
				+ "");
		long endTime = System.currentTimeMillis();
		logger.logMessage(LogLevel.INFO, endTime + "");
	}

	public static Event getEvent() {
		Context c = new ContextImpl();
		c.put("name", "name");
		Event e = Event.createEvent("hello", c);
		return e;
	}
}
