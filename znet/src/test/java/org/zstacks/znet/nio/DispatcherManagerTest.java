package org.zstacks.znet.nio;

import org.zstacks.znet.MessageCodec;
import org.zstacks.znet.nio.DispatcherManager;
import org.zstacks.znet.nio.EventAdaptor;

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
