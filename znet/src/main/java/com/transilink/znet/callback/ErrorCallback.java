package com.transilink.znet.callback;

import java.io.IOException;

import com.transilink.znet.nio.Session;

 
public interface ErrorCallback { 
	public void onError(IOException e, Session sess) throws IOException;   
}
