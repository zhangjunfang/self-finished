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

import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;

public class ServiceB implements ServiceInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4823466287226452738L;
	private String serviceId;
	public ServiceB(String s){
		serviceId = s;
	}
	public int compareTo(ServiceInfo o) {
		return 0;
	}

	public String getServiceId() {
		return serviceId;
	}

	public List<Parameter> getParameters() {
		return null;
	}

	public List<Parameter> getResults() {
		List<Parameter> list = new ArrayList<Parameter>();
		Parameter p = new Parameter();
		p.setArray(false);
		p.setName("result");
		p.setType("java.lang.String");
		return list;
	}
	public String getCategory() {
		return null;
	}

}
