package org.tinygroup.cepcorenetty.test;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

public class MyTestProcessor implements EventProcessor {
	List<ServiceInfo> list = new ArrayList<ServiceInfo>();
	String id;

	public void process(Event event) {
		String serviceId = event.getServiceRequest().getServiceId();

		for (ServiceInfo service : list) {
			if (serviceId.endsWith(service.getServiceId())) {
				System.out
						.println("testProcessor exexute service：" + serviceId);
			}
		}
		throw new RuntimeException();
	}

	public void setCepCore(CEPCore cepCore) {

	}

	public List<ServiceInfo> getServiceInfos() {
		return list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return EventProcessor.TYPE_LOCAL;
	}

	public int getWeight() {
		return 0;
	}

	public List<String> getRegex() {
		return null;
	}

	public boolean isRead() {
		return false;
	}

	public void setRead(boolean read) {

	}

}
