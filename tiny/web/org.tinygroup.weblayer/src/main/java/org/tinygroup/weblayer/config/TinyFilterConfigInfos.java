package org.tinygroup.weblayer.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("tiny-filters")
public class TinyFilterConfigInfos {
    @XStreamImplicit
	private List<TinyFilterConfigInfo> configs;

	public List<TinyFilterConfigInfo> getConfigs() {
		if(configs==null){
			configs=new ArrayList<TinyFilterConfigInfo>();
		}
		return configs;
	}

	public void setConfigs(List<TinyFilterConfigInfo> configs) {
		this.configs = configs;
	}
	
}
