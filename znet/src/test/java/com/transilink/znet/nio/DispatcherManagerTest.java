package com.transilink.znet.nio;

import com.transilink.znet.MessageCodec;
import com.transilink.znet.nio.DispatcherManager;
import com.transilink.znet.nio.EventAdaptor;

public class DispatcherManagerTest {

	public static void main(String[] args) throws Exception {
		DispatcherManager mgr = new DispatcherManager(new MessageCodec()) { 
			@Override
			public EventAdaptor buildEventAdaptor() { 
				return null;
			}
		};
		
		mgr.start();
		mgr.close();
	} 
}
