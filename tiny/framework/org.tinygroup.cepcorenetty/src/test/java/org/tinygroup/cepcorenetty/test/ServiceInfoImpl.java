package org.tinygroup.cepcorenetty.test;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;

public class ServiceInfoImpl implements ServiceInfo {


	 
	private static final long serialVersionUID = -7928548728812463948L;

	private String serviceId;
	
	public ServiceInfoImpl(String serviceId){
		this.serviceId = serviceId;
	}

	public int compareTo(ServiceInfo o) {
		if (this.getServiceId().equals(o.getServiceId()))
			return 0;
		return -1;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public List<Parameter> getParameters() {

		return new ArrayList<Parameter>();
	}

	public List<Parameter> getResults() {
		return new ArrayList<Parameter>();
	}

	public String getCategory() {
		
		return null;
	}

}
