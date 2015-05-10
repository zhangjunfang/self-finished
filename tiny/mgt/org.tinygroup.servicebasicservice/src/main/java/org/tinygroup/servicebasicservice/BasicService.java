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
package org.tinygroup.servicebasicservice;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;

public class BasicService {
	private ServiceRegistry serviceRegistry;

	public int getServiceCount() {
		return serviceRegistry.getServiceRegistryItems().size();
	}

	public List<ServiceRegistryItem> getServiceRegistryItems() {
		List<ServiceRegistryItem> list = new ArrayList<ServiceRegistryItem>();
		for (ServiceRegistryItem item : serviceRegistry
				.getServiceRegistryItems()) {
			ServiceRegistryItem newItem = new ServiceRegistryItem();
			newItem.setCategory(item.getCategory());
			newItem.setDescription(item.getDescription());
			newItem.setLocalName(item.getLocalName());
			newItem.setParameters(item.getParameters());
			newItem.setResults(item.getResults());
			newItem.setServiceId(item.getServiceId());
			list.add(newItem);
		} 

		return list;
	}

	public ServiceRegistryItem getServiceRegistryItem(String id) {
		ServiceRegistryItem item = serviceRegistry.getServiceRegistryItem(id);
		if (item == null) {
			return null;
		}
		ServiceRegistryItem newItem = new ServiceRegistryItem();
		newItem.setCategory(item.getCategory());
		newItem.setDescription(item.getDescription());
		newItem.setLocalName(item.getLocalName());
		newItem.setParameters(item.getParameters());
		newItem.setResults(item.getResults());
		newItem.setServiceId(item.getServiceId());
		return newItem;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
