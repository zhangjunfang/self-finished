package com.transilink.znet;

import java.io.IOException;

import com.transilink.znet.RemotingServer;

public abstract class Rs extends RemotingServer {
	
	private Route route;
	public Rs(int serverPort) throws IOException {
		super(serverPort);
	}
    
	

	public Route getRoute() {
		return route;
	}



	public void setRoute(Route route) {
		this.route = route;
	}



	public abstract void testxxx();
}
