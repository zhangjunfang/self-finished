package com.transilink.znet;

import java.io.IOException;

import com.transilink.znet.Message;
import com.transilink.znet.nio.Session;

 
public interface MessageHandler { 
	public void handleMessage(Message msg, Session sess) throws IOException;   
}
