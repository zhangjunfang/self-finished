package org.zstacks.znet;
 

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class RemotingServer extends ServerEventAdaptor implements Closeable {  
	private static final Logger log = LoggerFactory.getLogger(RemotingServer.class); 
	protected String serverHost = "0.0.0.0";
	protected int serverPort = 15555;   
	
	protected String serverAddr = String.format("%s:%d",  this.serverHost, this.serverPort);
	protected String serverName = "RemoteServer";
	
	protected ServerDispatcherManager dispatcherManager;
	protected ServerSocketChannel serverSocketChannel;
	
	public RemotingServer(String serverHost) throws IOException{
		this(serverHost, 15555);
	}
	
	public RemotingServer(int serverPort) throws IOException{
		this("0.0.0.0", serverPort); 
	}

    public RemotingServer(String serverHost, int serverPort) throws IOException { 
    	super();
    	
		this.dispatcherManager = new ServerDispatcherManager(this);
    	this.serverHost = serverHost;
    	this.serverPort = serverPort; 
    	
    	if("0.0.0.0".equals(this.serverHost)){
    		this.serverAddr = String.format("%s:%d", Helper.getLocalIp(), this.serverPort);
    	} else {
    		this.serverAddr = String.format("%s:%d", this.serverHost, this.serverPort);
    	}
    }   
    
    public void init(){
    	
    }
    
    public void start() throws IOException{   
    	this.init();
    	if(!this.dispatcherManager.isStarted()){
    		dispatcherManager.start();
    	}
    	
    	serverSocketChannel = ServerSocketChannel.open();
    	serverSocketChannel.configureBlocking(false);
    	serverSocketChannel.socket().bind(new InetSocketAddress(this.serverHost, this.serverPort)); 
    	dispatcherManager.getDispatcher(0).registerChannel(serverSocketChannel, SelectionKey.OP_ACCEPT); 
    	log.info("{} serving@{}:{}", this.serverName, this.serverHost, this.serverPort);
    }
    
    public void close() throws IOException { 
    	destroy();
    }
    
    public void destroy() throws IOException{
    	serverSocketChannel.close();
    	dispatcherManager.close();
    }

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}



