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
package org.tinygroup.cepcorenettysc.test.service;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;

public class EventProcessorB implements EventProcessor {
	List<ServiceInfo> list = new ArrayList<ServiceInfo>();
	
	public void process(Event event) {
		ServiceRequest r = event.getServiceRequest();
		String serviceId = r.getServiceId();
		for(ServiceInfo s:list){
			if(s.getServiceId().equals(serviceId)){
				System.out.println("execute ServiceB id:"+serviceId);
				event.getServiceRequest().getContext().put("result", serviceId);
				return;
			}
		}
	}

	public void setCepCore(CEPCore cepCore) {
		
	}

	public List<ServiceInfo> getServiceInfos() {
		return list;
	}

	public String getId() {
		return EventProcessorB.class.getName();
	}

	public int getType() {
		return EventProcessorB.TYPE_LOCAL;
	}
	public void addServiceInfo(ServiceInfo s){
		list.add(s);
	}

	public int getWeight() {
		return 0;
	}

	public List<String> getRegex() {
		return null;
	}

	public boolean isRead() {
		return true;
	}

	public void setRead(boolean read) {
		
	}
}
