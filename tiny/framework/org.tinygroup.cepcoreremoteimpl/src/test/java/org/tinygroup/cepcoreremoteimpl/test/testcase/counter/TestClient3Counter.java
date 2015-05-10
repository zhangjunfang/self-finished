package org.tinygroup.cepcoreremoteimpl.test.testcase.counter;

import java.util.ArrayList;

import org.tinygroup.tinyrunner.Runner;

public class TestClient3Counter {
	public static void main(String[] args) throws Exception {
		Runner.init("application3.xml", new ArrayList<String>());
//		ScOperator operatpr = new ScOperator(9191);
//		CEPCore cep = BeanContainerFactory.getBeanContainer(TestServer.class.getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
//		operatpr.startCEPCore(cep);
		PrintThread p  = new PrintThread();
		p.start();
	}
	
	
}
