package org.tinygroup.cepcoreremoteimpl.test;

public class Service2 {
	public static volatile Integer i = 0;
	public void add(){
		synchronized (i) {
			i++;
		}
	}
	
	public void overtime(){
		System.out.println(System.currentTimeMillis());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			
		}
		System.out.println(System.currentTimeMillis());
	}
}
