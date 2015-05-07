package org.zstacks.znet;

import java.io.IOException;

import org.zstacks.znet.Message;
import org.zstacks.znet.nio.Session;

 
public interface MessageHandler { 
	public void handleMessage(Message msg, Session sess) throws IOException;   
}
