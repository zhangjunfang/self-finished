package org.tinygroup.cepcoreremoteimpl.test.testcase.stop;

import java.util.ArrayList;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.node.NodeOperator;
import org.tinygroup.tinyrunner.Runner;

public class TestClientAsStop {
	public static void main(String[] args) throws Exception {
	
		Runner.init("application2.xml", new ArrayList<String>());
		Thread.sleep(2000);
		CEPCore cepcore = BeanContainerFactory.getBeanContainer(
				TestClientAsStop.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		NodeOperator nodeOperater = BeanContainerFactory.getBeanContainer(
				TestClientAsStop.class.getClassLoader()).getBean("node2");
		nodeOperater.stopCEPCore(cepcore);
	}
}
