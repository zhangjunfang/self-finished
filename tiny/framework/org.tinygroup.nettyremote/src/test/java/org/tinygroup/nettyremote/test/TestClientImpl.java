package org.tinygroup.nettyremote.test;

import org.tinygroup.nettyremote.impl.ClientImpl;

public class TestClientImpl {
	public static void main(String[] args) {
		ClientImpl c = new ClientImpl(9090, "127.0.0.1",false);
		c.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.write("a");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.write("a2");
		System.out.println("main end");
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// System.out.println("before stop");
		// c.stop();
		// System.out.println("stop");
	}
}
