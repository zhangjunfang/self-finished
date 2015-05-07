package org.zstacks.znet.callback;

import java.io.IOException;

import org.zstacks.znet.nio.Session;

 
public interface ConnectedCallback { 
	public void onConnected(Session sess) throws IOException;   
}
