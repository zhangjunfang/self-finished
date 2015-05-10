package org.tinygroup.cepcorenetty.test;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.exception.RequestNotFoundException;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class TestCepcoreNetty extends TestCase{

	public void setUp(){
		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private EventProcessor getEventProcessor(String id,String service, int length) {
		MyTestProcessor t = new MyTestProcessor();
		for (int i = 0; i < length; i++) {
			t.getServiceInfos().add(new ServiceInfoImpl(service + i));
		}
		t.setId(id);
		return t;
	}

	public void testAdd() {
		AbstractTestUtil.init("application.xml", true);
		CEPCore core = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader())
				.getBean(CEPCore.CEP_CORE_BEAN);
		core.registerEventProcessor(getEventProcessor("b","b", 10));
		core.registerEventProcessor(getEventProcessor("c","c", 10));
		ServiceInfo service = core.getServiceInfo("b0");
		assertTrue(service!=null);
	}
	
	
	public void testRemove() {
		AbstractTestUtil.init("application.xml", true);
		CEPCore core = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader())
				.getBean(CEPCore.CEP_CORE_BEAN);
		EventProcessor p = getEventProcessor("a","a", 10);
		core.registerEventProcessor(p);
		core.unregisterEventProcessor(p);
		try {
			core.getServiceInfo("a0");
		} catch (RequestNotFoundException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
		
		
	}
	
}
