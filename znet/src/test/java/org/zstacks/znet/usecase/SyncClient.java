package org.zstacks.znet.usecase;


import org.zstacks.znet.ClientDispatcherManager;
import org.zstacks.znet.Message;
import org.zstacks.znet.RemotingClient;

public class SyncClient {

	public static void main(String[] args) throws Exception { 
		ClientDispatcherManager manager = new ClientDispatcherManager();

		final RemotingClient client = new RemotingClient("127.0.0.1:80", manager);
	
		Message msg = new Message();
		msg.setCommand("hello");
		msg.setBody("hello");
		Message res = client.invokeSync(msg); //同步请求
		System.out.println(res);
		
		
		client.close();
		manager.close(); 
	}

}
