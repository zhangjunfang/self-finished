package org.tinygroup.nettyremote;

public interface Client {
	void start();
	void write(Object o);
	void stop();
}
