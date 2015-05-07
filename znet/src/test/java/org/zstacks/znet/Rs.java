package org.zstacks.znet;

import java.io.IOException;

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
