package org.tinygroup.weblayer.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class BasicConfigInfo {

	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	@XStreamAlias("class")
	private String className;
	@XStreamAsAttribute
	@XStreamAlias("bean")
	private String beanName;
	@XStreamImplicit
	private List<InitParam> params;
	private transient Map<String, String> parameterMap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public List<InitParam> getParams() {
		if (params == null) {
			params = new ArrayList<InitParam>();
		}
		return params;
	}

	public void setParams(List<InitParam> params) {
		this.params = params;
	}

	public String getConfigName() {
		String name = getName();
		if (StringUtil.isBlank(name)) {
			name = getId();
		}
		return name;
	}
	
	public String getConfigBeanName() {
		String beanName = getBeanName();
		if (StringUtil.isBlank(beanName)) {
			beanName = getClassName();
		}
		return beanName;
	}

	public String getParameterValue(String name) {
		initParams();
		return parameterMap.get(name);
	}

	public Iterator<String> getIterator() {
		initParams();
		return parameterMap.keySet().iterator();
	}

	private void initParams() {
		if (parameterMap == null) {
			parameterMap = new HashMap<String, String>();
			if (!CollectionUtil.isEmpty(params)) {
				for (InitParam param : params) {
					parameterMap.put(param.getName(), param.getValue());
				}
			}
		}
	}

	public Map<String, String> getParameterMap() {
		initParams();
		return parameterMap;
	}

	@Override
	public int hashCode() {
		return getConfigName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}
		if (obj instanceof BasicConfigInfo) {
			BasicConfigInfo other = (BasicConfigInfo) obj;
			return other.getConfigName().equals(this.getConfigName());
		}
		return false;

	}

}
