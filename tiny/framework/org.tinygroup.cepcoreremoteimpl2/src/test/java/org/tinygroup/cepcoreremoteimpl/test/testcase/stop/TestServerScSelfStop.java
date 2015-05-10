package org.tinygroup.cepcoreremoteimpl.test.testcase.stop;

import java.util.ArrayList;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.sc.ScOperator;
import org.tinygroup.tinyrunner.Runner;

public class TestServerScSelfStop {
	public static void main(String[] args) throws Exception {
		
		Runner.init("applicationserver.xml", new ArrayList<String>());
		Thread.sleep(2000);
		CEPCore cepcore = BeanContainerFactory.getBeanContainer(TestServerScSelfStop.class.getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
		ScOperator scOperater = BeanContainerFactory.getBeanContainer(TestServerScSelfStop.class.getClassLoader()).getBean("sc");
		scOperater.stopCEPCore(cepcore);
	}
}
