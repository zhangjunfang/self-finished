package org.tinygroup.nettyremote.test;

import org.tinygroup.nettyremote.impl.ServerImpl;

public class TestServerImpl {
	public static void main(String[] args) {
		ServerImpl serverImpl = new ServerImpl(9090);
		serverImpl.start();
		System.out.println("start");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("before stop");
//		serverImpl.stop();
//		System.out.println("stop");
	}
}
