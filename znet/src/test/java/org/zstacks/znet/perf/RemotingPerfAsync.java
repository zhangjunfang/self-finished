package org.zstacks.znet.perf;

import java.util.concurrent.atomic.AtomicLong;

import org.zstacks.znet.ClientDispatcherManager;
import org.zstacks.znet.Message;
import org.zstacks.znet.RemotingClient;
import org.zstacks.znet.ticket.ResultCallback;

public class RemotingPerfAsync {
	public static void main(String[] args) throws Exception {   
		ClientDispatcherManager clientDispatcherManager = new ClientDispatcherManager();
		 
		
		@SuppressWarnings("resource")
		RemotingClient client = new RemotingClient("127.0.0.1:8080", clientDispatcherManager);
	
		final long start = System.currentTimeMillis();
		final long N = 400000;
		final AtomicLong counter = new AtomicLong(0);
		for(int i=0;i<N;i++){
			Message msg = new Message(); 
			msg.setCommand("hello");
			//3）同步请求
			client.invokeAsync(msg, new ResultCallback() {
				@Override
				public void onCompleted(Message result) { 
					counter.incrementAndGet();
					if(counter.get()%1000 == 0){
						double qps = counter.get()*1000.0/(System.currentTimeMillis()-start);
						System.out.format("QPS: %.2f\n", qps);
					}
				}
			}); 
		} 
		
		//4）释放链接资源与线程池相关资源
		//client.close();
		//clientDispatcherManager.close();
	} 
}
