package org.zstacks.znet.callback;

import java.io.IOException;

import org.zstacks.znet.nio.Session;

 
public interface ErrorCallback { 
	public void onError(IOException e, Session sess) throws IOException;   
}
