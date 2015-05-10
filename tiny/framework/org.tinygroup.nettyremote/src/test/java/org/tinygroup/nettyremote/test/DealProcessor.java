package org.tinygroup.nettyremote.test;

import org.tinygroup.nettyremote.impl.ClientImpl;
import org.tinygroup.threadgroup.AbstractProcessor;

public class DealProcessor extends AbstractProcessor {
	int count;
	ClientImpl c;

	public DealProcessor(ClientImpl c, String name, int count) {
		super(name);
		this.count = count;
		this.c = c;
	}

	protected void action() throws Exception {
		for (int i = 0; i < count; i++) {
			c.write(getName() + i);
		}
	}

}
