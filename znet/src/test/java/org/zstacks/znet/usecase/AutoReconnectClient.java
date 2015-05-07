package org.zstacks.znet.usecase;

import java.io.IOException;

import org.zstacks.znet.ClientDispatcherManager;
import org.zstacks.znet.Message;
import org.zstacks.znet.RemotingClient;

public class AutoReconnectClient {

	public static void main(String[] args) throws Exception { 
		ClientDispatcherManager manager = new ClientDispatcherManager();
		
		@SuppressWarnings("resource")
		final RemotingClient client = new RemotingClient("127.0.0.1:80", manager);
	
		while(true){ 
			Message msg = new Message();
			msg.setCommand("hello");
			msg.setBody("hello");
			try{
				Message res = client.invokeSync(msg); //产看源码，connectIfNeed(), 自动重连
				System.out.println(res);
			}catch(IOException e){
				//ignore
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}
		
		
		//manager.close(); 
	}

}
