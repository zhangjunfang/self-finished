package org.tinygroup.weblayer.listener.impl;

import org.tinygroup.weblayer.BasicTinyConfig;
import org.tinygroup.weblayer.BasicTinyConfigAware;

public class SimpleBasicTinyConfigAware implements BasicTinyConfigAware {

	protected BasicTinyConfig basicTinyConfig;

	public void setBasicConfig(BasicTinyConfig basicTinyConfig) {
		this.basicTinyConfig = basicTinyConfig;
	}
}
