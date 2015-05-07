package com.transilink.znet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.transilink.znet.nio.EventAdaptor;
import com.transilink.znet.nio.Session;

public class ServerEventAdaptor extends EventAdaptor{  
	
	protected Map<String, MessageHandler> handlerMap = new ConcurrentHashMap<String, MessageHandler>();
	
	protected MessageHandler globalHandler;
	
	public ServerEventAdaptor() {
		this.registerHandler(Message.HEARTBEAT, new MessageHandler() { 
			public void handleMessage(Message msg, Session sess) throws IOException { 
				//ignore
			}
		});
	}
   
	public void registerHandler(String command, MessageHandler handler){
    	this.handlerMap.put(command, handler);
    }
    
    public void registerGlobalHandler(MessageHandler beforeHandler) {
		this.globalHandler = beforeHandler;
	}  
    
    public String findHandlerKey(Message msg){
    	return msg.getCommand();
    }
    @Override
    public void onMessage(Object obj, Session sess) throws IOException {  
    	Message msg = (Message)obj;  
    	if(this.globalHandler != null){
    		this.globalHandler.handleMessage(msg, sess);
    	}
    	
    	String cmd = findHandlerKey(msg);
    	if(cmd == null){ 
    		Message res = new Message();
    		res.setMsgId(msg.getMsgId()); 
        	res.setStatus("400");
        	res.setBody("Bad format: missing command"); 
        	sess.write(res);
    		return;
    	}
    	
    	MessageHandler handler = handlerMap.get(cmd);
    	if(handler != null){
    		handler.handleMessage(msg, sess);
    		return;
    	}
    	
    	Message res = new Message();
    	res.setMsgId(msg.getMsgId()); 
    	res.setStatus("400");
    	String text = String.format("Bad format: command(%s) not support", cmd);
    	res.setBody(text); 
    	sess.write(res); 
    }  
}

