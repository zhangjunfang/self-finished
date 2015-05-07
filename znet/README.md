**znet是NIO通讯小框架，功能上有点类似netty与mina，是zbus消息总线的通讯基础项目，**

##为什么没有选择netty或者mina？

个人观点：netty与mina过于庞大，需要学习的成本比较高，debug中的chain过长，自己不方便改写

##znet对JAVA NIO通讯做了个简单的封装
核心目标：
 
* 1) **具备多线程单机扩容能力--尽量吃完CPU**
* 2) **应用上层简单扩展，快速搞定高效Server与同步异步Client**

尽管这两个目标也是netty与mina的目标之一，但是znet将更加简洁，方便二次个性化修改

## 性能欢迎大家给数据:)，跟机器有关

[性能ISSUE](http://git.oschina.net/rushmore/znet/issues/1 "perf") 

[性能测试代码](http://git.oschina.net/rushmore/znet/tree/master/src/test/java/org/zstacks/znet/perf "perf_code") 


##znet应用项目

[zbus消息队列、服务总线](http://git.oschina.net/rushmore/zbus "zbus") 

[zwall DMZ安全隔离](http://git.oschina.net/rushmore/zwall "zwall") 




##设计概要

DispatcherManager管理Dispatcher线程生命周期，通过Codec暴露编码解码消息协议，通过EventAdaptor暴露事件处理。

DispatcherManager+Dispatcher+Session组成的 **引擎** 提供了 “【目标1】多线程单机扩容能力”

EventAdaptor扩展应用提供了“【目标2】快速搞定高效Server与同步异步Client”.

![znet](http://git.oschina.net/uploads/images/2015/0420/214040_d5f0babd_7458.png)


默认提供MessageCodec的实现，默认采用HTTP兼容协议--KV头部+Binary消息体。一般来说应用只需要个性化EventAdaptor。比如zbus消息总线就是一个案例。

由于Server和Client在事件的范围上的区别（Client 的EventAdaptor希望是每个Client一个，Server的EventAdaptor希望是共享的），我们目前实现方案中默认给出了两个不同DispatcherManager：ClientDispatcherManager、ServerDispatcherManager


## maven依赖

	<dependency>
		<groupId>org.zbus</groupId>
		<artifactId>zbus-remoting</artifactId>
		<version>5.2.0</version>
	</dependency>



## 示例： 简单编写一个高性能NIO Server
	
	public class MyRemotingServer extends RemotingServer {
		//个性化定义Message中的那部分解释为命令，这里为了支持浏览器直接访问，把Message中的path理解为command
		@Override
		public String findHandlerKey(Message msg) { 
			String cmd = msg.getCommand();
			if(cmd == null){
				cmd = msg.getPath();
			}
			return cmd;
		}
		
		public MyRemotingServer(int serverPort) throws IOException {
			super(serverPort); 
			//注册命令处理Callback
			this.registerHandler("hello", new MessageHandler() {
				public void handleMessage(Message msg, Session sess) throws IOException {
					System.out.println(msg);
					msg.setStatus("200");   
					msg.setBody("hello world");
					sess.write(msg);
				}
			});
		}
		public static void main(String[] args) throws Exception {   
			@SuppressWarnings("resource")
			MyRemotingServer server = new MyRemotingServer(80);
			server.start(); 
		}
	}
	
上面这段代码就完成了一个简单的高性能NIO Server的编写，并且可以在浏览器中直接调用 http://localhost/hello 因为znet的MessageCodec直接兼容了HTTP协议。

## 示例： 简单编写一个高性能NIO Client

	public class MyRemotingClient {
		public static void main(String[] args) throws Exception {  
			//1) 创建ClientDispatcherManager
			ClientDispatcherManager clientDispatcherManager = new ClientDispatcherManager();
			
			//2) 创建RemotingClient
			RemotingClient client = new RemotingClient("127.0.0.1:80", clientDispatcherManager);
			Message msg = new Message(); 
			msg.setCommand("hello");
			//3）同步请求
			Message res = client.invokeSync(msg);
			System.out.println(res);
			
			//4）释放链接资源与线程池相关资源
			client.close();
			clientDispatcherManager.close();
		} 
	}


## 同步异步客户端，连接池等应用代码示例

[UseCase](http://git.oschina.net/rushmore/znet/tree/master/src/test/java/org/zstacks/znet/usecase "usecase") 

