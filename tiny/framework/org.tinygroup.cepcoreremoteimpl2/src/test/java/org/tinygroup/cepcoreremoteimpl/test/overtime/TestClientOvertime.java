package org.tinygroup.cepcoreremoteimpl.test.overtime;

import java.util.ArrayList;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.test.testcase.TestServer;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.tinyrunner.Runner;

public class TestClientOvertime {
	public static void main(String[] args) throws Exception {
		Runner.init("application.xml", new ArrayList<String>());
		CEPCore cep = BeanContainerFactory.getBeanContainer(
				TestServer.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		Thread.sleep(2000);
		Event t = getEvent();
		cep.process(t);
	}

	public static Event getEvent() {
		Context c = new ContextImpl();
		c.put("name", "name");
		Event e = Event.createEvent("overtime", c);
		return e;
	}
}
