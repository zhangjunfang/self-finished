package org.tinygroup.weblayer.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.BasicTinyConfig;
import org.tinygroup.weblayer.config.BasicConfigInfo;

public class SimpleBasicTinyConfig implements BasicTinyConfig {

	protected String configName;

	protected Map<String, String> parameterMap = new HashMap<String, String>();

	protected static Logger logger = LoggerFactory
			.getLogger(SimpleBasicTinyConfig.class);

	public SimpleBasicTinyConfig(String configName,
			BasicConfigInfo basicConfigInfo) {
		super();
		this.configName = configName;
		this.parameterMap.putAll(basicConfigInfo.getParameterMap());
	}

	public SimpleBasicTinyConfig(List<BasicConfigInfo> basicConfigInfos) {
		super();
		this.configName = "compositeConfig";
		for (BasicConfigInfo basicConfigInfo : basicConfigInfos) {
			this.parameterMap.putAll(basicConfigInfo.getParameterMap());
		}
	}

	public String getConfigName() {
		return configName;
	}

	public String getInitParameter(String name) {
		return parameterMap.get(name);
	}

	public Iterator<String> getInitParameterNames() {
		return parameterMap.keySet().iterator();
	}

	public Map<String, String> getParameterMap() {
		return parameterMap;
	}
}
