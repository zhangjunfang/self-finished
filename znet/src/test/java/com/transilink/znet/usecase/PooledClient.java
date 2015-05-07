package com.transilink.znet.usecase;

import com.transilink.znet.Message;
import com.transilink.znet.RemotingClient;
import com.transilink.znet.pool.RemotingClientPool;
import com.transilink.znet.pool.RemotingClientPoolConfig;

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
