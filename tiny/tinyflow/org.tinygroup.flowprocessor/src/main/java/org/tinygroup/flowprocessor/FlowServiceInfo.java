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
package org.tinygroup.flowprocessor;

import java.util.List;

import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.flow.config.Flow;

/**
 * Created by luoguo on 14-4-9.
 */
public class FlowServiceInfo implements ServiceInfo {
	private static final long serialVersionUID = -7451038800051026910L;
	private String serviceId;
	private List<Parameter> parameters;
	private List<Parameter> results;
	private String category;

	public FlowServiceInfo(Flow flow) {
		serviceId = flow.getId();
		parameters = flow.getParameters();
		results = flow.getOutputParameters();
		category = flow.getCategory();
	}

	public String getServiceId() {
		return serviceId;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<Parameter> getResults() {
		return results;
	}

	public int compareTo(ServiceInfo o) {
		return o.getServiceId().compareTo(serviceId);
	}

	public String getCategory() {
		return category;
	}

}
