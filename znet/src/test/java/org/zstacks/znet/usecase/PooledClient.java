package org.zstacks.znet.usecase;

import org.zstacks.znet.Message;
import org.zstacks.znet.RemotingClient;
import org.zstacks.znet.pool.RemotingClientPool;
import org.zstacks.znet.pool.RemotingClientPoolConfig;

public class PooledClient {

	public static void main(String[] args) throws Exception { 
		//创建连接池
		RemotingClientPoolConfig config = new RemotingClientPoolConfig(); 
		config.setBrokerAddress("127.0.0.1:80"); 
		RemotingClientPool pool = new RemotingClientPool(config);
		 
		RemotingClient client = pool.borrowObject();
		
		Message msg = new Message();
		msg.setCommand("hello");
		Message res = client.invokeSync(msg);
		System.out.println(res);
		
		pool.returnObject(client); 
	
		
		//销毁连接池
		pool.close();  
	}

}
