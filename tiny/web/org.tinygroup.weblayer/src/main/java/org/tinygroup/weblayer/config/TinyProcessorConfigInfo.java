package org.tinygroup.weblayer.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * TinyProcessor的配置信息
 * 
 * @author renhui
 * 
 */
@XStreamAlias("tiny-processor")
public class TinyProcessorConfigInfo extends BasicConfigInfo {
	@XStreamImplicit
	List<ServletMapping> servletMappings;

	public List<ServletMapping> getServletMappings() {
		if (servletMappings == null) {
			servletMappings = new ArrayList<ServletMapping>();
		}
		return servletMappings;
	}

	public void setServletMappings(List<ServletMapping> servletMappings) {
		this.servletMappings = servletMappings;
	}

	public void combine(TinyProcessorConfigInfo configInfo) {
		getParameterMap().putAll(configInfo.getParameterMap());
		getServletMappings().addAll(configInfo.getServletMappings());
	}
}
