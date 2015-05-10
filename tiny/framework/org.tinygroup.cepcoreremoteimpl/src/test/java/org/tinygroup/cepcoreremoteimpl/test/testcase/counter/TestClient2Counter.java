package org.tinygroup.cepcoreremoteimpl.test.testcase.counter;

import java.util.ArrayList;

import org.tinygroup.cepcoreremoteimpl.test.Service2;
import org.tinygroup.tinyrunner.Runner;

public class TestClient2Counter {
	public static void main(String[] args) throws Exception {
		Runner.init("application2.xml", new ArrayList<String>());
		// ScOperator operatpr = new ScOperator(9191);
		// CEPCore cep =
		// BeanContainerFactory.getBeanContainer(TestServer.class.getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
		// operatpr.startCEPCore(cep);
		PrintThread p = new PrintThread();
		p.start();
	}

}

class PrintThread extends Thread {
	public void run() {
		int old = 0;
		while (true) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Service2.i - old);
			old = Service2.i;
		}

	}
}