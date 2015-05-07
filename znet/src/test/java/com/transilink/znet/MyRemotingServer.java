package com.transilink.znet;

import java.io.IOException;

import com.transilink.znet.Message;
import com.transilink.znet.MessageHandler;
import com.transilink.znet.nio.Session;

public class MyRemotingServer extends Rs {
	
	/**
	 * 个性化定义Message中的那部分解释为命令，
	 * 这里为了支持浏览器直接访问，
	 * 把Message中的path理解为command
	 * */
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
		
	}
	public  void  testxxx(){
		//注册命令处理Callback
		System.err.println("URL:"+this.getRoute().toString());
		this.registerHandler(this.getRoute().toString(), new MessageHandler() {
					public void handleMessage(Message msg, Session sess) throws IOException {
						System.out.println("msg:\n\r"+msg);
						//
						System.out.println("get方式获取===msg.name:"+msg.getParam("name"));
						System.out.println("HeadOrParam方式获取===msg.name:"+msg.getHeadOrParam("name"));
						//post方式提交数据，获取参数数据方式
						System.out.println("post方式获取===msg.name:"+new String(msg.body));
						
						sess.attr("sex", "男");
						System.err.println("sess:\n\r"+sess);
						msg.setStatus("200");   
						msg.setBody("hello world");
						sess.write(msg);
					}
				});
	}

//	public static void main(String[] args) throws Exception {   
//		@SuppressWarnings("resource")
//		MyRemotingServer server = new MyRemotingServer(8080);
//		server.start(); 
//	}
}
