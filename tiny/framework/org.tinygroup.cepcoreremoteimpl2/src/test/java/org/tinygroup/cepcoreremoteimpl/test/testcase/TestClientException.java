package org.tinygroup.cepcoreremoteimpl.test.testcase;

import java.util.ArrayList;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.tinyrunner.Runner;

public class TestClientException {
//	private static Logger logger = LoggerFactory.getLogger(TestClientException.class);
	public static void main(String[] args) throws Exception {
		
		Runner.init("application.xml", new ArrayList<String>());
		CEPCore cep = BeanContainerFactory.getBeanContainer(
				TestServer.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		Thread.sleep(2000);
//		NodeOperator nodeOp = BeanContainerFactory.getBeanContainer(
//				TestServer.class.getClassLoader()).getBean("node");
		Event t = getEvent();
		cep.process(t);
	}

	public static Event getEvent() {
		Context c = new ContextImpl();
		c.put("name", "name");
		Event e = Event.createEvent("helloException", c);
		return e;
	}
}
