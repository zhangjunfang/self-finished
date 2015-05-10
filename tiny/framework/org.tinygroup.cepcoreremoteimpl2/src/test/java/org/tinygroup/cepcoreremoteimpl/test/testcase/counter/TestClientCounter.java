package org.tinygroup.cepcoreremoteimpl.test.testcase.counter;

import java.util.ArrayList;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.test.testcase.TestServer;
import org.tinygroup.threadgroup.MultiThreadProcessor;
import org.tinygroup.tinyrunner.Runner;

public class TestClientCounter {
	static int threadNum = 50;
	static MultiThreadProcessor processors = new MultiThreadProcessor("net deal");
	public static void main(String[] args) throws Exception {
		
		Runner.init("application.xml", new ArrayList<String>());
		
		CEPCore cep = BeanContainerFactory.getBeanContainer(
				TestServer.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		Thread.sleep(2000);
		startClient(cep);
	
	}

	private static void startClient(CEPCore cep) {
		for (int i = 0; i < threadNum; i++) {
			processors
					.addProcessor(new CounterDealProcessor(cep, "net deal" + i));
		}
		processors.start();
	}
}