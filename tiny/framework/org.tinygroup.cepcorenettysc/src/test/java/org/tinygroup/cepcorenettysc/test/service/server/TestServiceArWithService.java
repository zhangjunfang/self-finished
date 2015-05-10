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
import org.tinygroup.cepcorenetty.NettyCepCoreImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.tinytestutil.AbstractTestUtil;


public class TestServiceArWithService {
	public static void main(String[] args) {
		startAr("a0");
	}
	public static void startAr(String serviceId){
		AbstractTestUtil.init("application.xml", true);
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		NettyCepCoreImpl p = BeanContainerFactory.getBeanContainer(
				TestServiceArWithService.class.getClassLoader()).getBean(
				CEPCore.CEP_CORE_BEAN);
		Event e = getEvent(serviceId);
		p.process(e);
		if(!"a0".equals( e.getServiceRequest().getContext().get("result"))){
			throw new RuntimeException("服务执行失败");
		}else{
			System.out.println("服务执行完成");
		}
	}
	public static Event getEvent(String id) {
		Event e = new Event();
		ServiceRequest s = new ServiceRequest();
		s.setServiceId(id);
		e.setServiceRequest(s);
		return e;
	}
	
	
}
