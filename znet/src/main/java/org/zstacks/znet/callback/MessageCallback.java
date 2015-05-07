package org.zstacks.znet.callback;

import java.io.IOException;

import org.zstacks.znet.Message;
import org.zstacks.znet.nio.Session;

 
public interface MessageCallback { 
	public void onMessage(Message msg, Session sess) throws IOException;   
}
