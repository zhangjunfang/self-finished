package com.transilink.znet.callback;

import java.io.IOException;

import com.transilink.znet.Message;
import com.transilink.znet.nio.Session;

 
public interface MessageCallback { 
	public void onMessage(Message msg, Session sess) throws IOException;   
}
