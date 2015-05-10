package org.tinygroup.weblayer.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("tiny-listeners")
public class TinyListenerConfigInfos {

	@XStreamImplicit
	private List<TinyListenerConfigInfo> listenerConfigInfos;

	public List<TinyListenerConfigInfo> getListenerConfigInfos() {
		if (listenerConfigInfos == null) {
			listenerConfigInfos = new ArrayList<TinyListenerConfigInfo>();
		}
		return listenerConfigInfos;
	}

	public void setListenerConfigInfos(
			List<TinyListenerConfigInfo> listenerConfigInfos) {
		this.listenerConfigInfos = listenerConfigInfos;
	}

}
