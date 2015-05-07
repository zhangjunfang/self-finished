package com.transilink.znet.perf;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import com.transilink.znet.ClientDispatcherManager;
import com.transilink.znet.Message;
import com.transilink.znet.RemotingClient;

class Task extends Thread {
	private final RemotingClient client;
	private final AtomicLong counter;
	private final long startTime;
	private final long N;

	public Task(RemotingClient client, AtomicLong counter, long startTime,
			long N) {
		this.client = client;
		this.counter = counter;
		this.startTime = startTime;
		this.N = N;
	}

	@Override
	public void run() {
		for (int i = 0; i < N; i++) {
			Message msg = new Message();
			msg.setCommand("hello");
			try {
				client.invokeSync(msg);
				counter.incrementAndGet();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (counter.get() % 1000 == 0) {
				double qps = counter.get() * 1000.0
						/ (System.currentTimeMillis() - startTime);
				System.out.format("QPS: %.2f\n", qps);
			}
		}
	}
}

public class RemotingPerf {
	public static void main(String[] args) throws Exception {
		ClientDispatcherManager clientDispatcherManager = new ClientDispatcherManager();

		final long N = 100000;
		final AtomicLong counter = new AtomicLong(0);
		int threadCount = 16;
		RemotingClient[] clients = new RemotingClient[threadCount];
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new RemotingClient("127.0.0.1:8080",
					clientDispatcherManager);
		}

		final long startTime = System.currentTimeMillis();
		Task[] tasks = new Task[threadCount];
		for (int i = 0; i < threadCount; i++) {
			tasks[i] = new Task(clients[i], counter, startTime, N);
		}
		for (Task task : tasks) {
			task.start();
		}

		// 4）释放链接资源与线程池相关资源
		// client.close();
		// clientDispatcherManager.close();
	}
}
