package com.transilink.znet.callback;

import java.io.IOException;

import com.transilink.znet.nio.Session;

 
public interface ConnectedCallback { 
	public void onConnected(Session sess) throws IOException;   
}
