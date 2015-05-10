/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.cepcorenettysc.test.service.server;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcorenetty.NettyCepCoreImpl;
import org.tinygroup.cepcorenettysc.operator.ArOperator;
import org.tinygroup.cepcorenettysc.test.service.EventProcessorB;
import org.tinygroup.cepcorenettysc.test.service.ServiceB;
import org.tinygroup.tinytestutil.AbstractTestUtil;


public class TestServiceAsB {
	public static void main(String[] args) {
		startAsB();
	}
	
	public static EventProcessor getEventProcessorB(){
		EventProcessorB b = new EventProcessorB();
		b.addServiceInfo(new ServiceB("b0"));
		b.addServiceInfo(new ServiceB("b1"));
		b.addServiceInfo(new ServiceB("b2"));
		
		return b;
	}
	
	public static void startAsB(){
		AbstractTestUtil.init("applicationb.xml", true);
		NettyCepCoreImpl p  = BeanContainerFactory.getBeanContainer(
				TestServiceAsB.class.getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
		p.setNodeName("asb");
		ArOperator aro = new ArOperator(IpUtil.IP,"6666",IpUtil.IP,"8888",10);
		p.setOperator(aro);
		p.registerEventProcessor(getEventProcessorB());
		p.startCEPCore(p);
	}
	
}
