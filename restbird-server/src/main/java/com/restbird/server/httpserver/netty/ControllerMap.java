package com.restbird.server.httpserver.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.restbird.server.httpserver.netty.mvc.Controller;

/**
 * controller mapping, configured in controller-mapping.xml
 * 
 * @author littleBirdTao
 *
 */
public class ControllerMap {
	// private static final Logger logger =
	// LoggerFactory.getLogger(ControllerMap.class);
	private Map<String, Controller> controllerMap = new ConcurrentHashMap<String, Controller>();

	public Map<String, Controller> getControllerMap() {
		return controllerMap;
	}

	public void setControllerMap(Map<String, Controller> controllerMap) {
		this.controllerMap = controllerMap;
	}
}
