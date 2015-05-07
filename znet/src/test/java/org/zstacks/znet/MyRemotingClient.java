package org.zstacks.znet;

public class MyRemotingClient {
	public static void main(String[] args) throws Exception {
		// 1) 创建ClientDispatcherManager
		ClientDispatcherManager clientDispatcherManager = new ClientDispatcherManager();

		// 2) 创建RemotingClient
		RemotingClient client = new RemotingClient("127.0.0.1:8080",
				clientDispatcherManager);
		Message msg = new Message();
		msg.setCommand("hello");
		// 3）同步请求
		Message res = client.invokeSync(msg);
		System.out.println(res);

		// 4）释放链接资源与线程池相关资源
		client.close();
		clientDispatcherManager.close();
	}
}
