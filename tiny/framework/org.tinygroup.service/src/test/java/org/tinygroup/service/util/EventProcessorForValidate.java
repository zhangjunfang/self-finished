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
package org.tinygroup.service.util;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.service.ServiceProviderInterface;

public class EventProcessorForValidate implements EventProcessor{
	ServiceProviderInterface provider;
	
	
	public ServiceProviderInterface getProvider() {
		return provider;
	}

	public void setProvider(ServiceProviderInterface provider) {
		this.provider = provider;
	}

	List<ServiceInfo> list = new ArrayList<ServiceInfo>();
	public void process(Event event) {
		provider.execute(event.getServiceRequest().getServiceId(), event.getServiceRequest().getContext());
	}

	public void setCepCore(CEPCore cepCore) {
		// TODO Auto-generated method stub
		
	}

	public List<ServiceInfo> getServiceInfos() {
		return list;
	}

	public String getId() {
		return EventProcessorForValidate.class.getName();
	}

	public int getType() {
		return EventProcessor.TYPE_LOCAL;
	}

	public int getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<String> getRegex() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRead() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setRead(boolean read) {
		// TODO Auto-generated method stub
		
	}

}
