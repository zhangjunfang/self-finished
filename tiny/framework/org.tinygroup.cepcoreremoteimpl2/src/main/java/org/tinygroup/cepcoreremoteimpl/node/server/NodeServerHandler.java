package org.tinygroup.cepcoreremoteimpl.node.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 节点服务器
 * @author chenjiao
 *
 */
public class NodeServerHandler extends SimpleChannelInboundHandler<Event> {
	private static Logger logger = LoggerFactory
			.getLogger(NodeServerHandler.class);
	private CEPCore core;
	public NodeServerHandler(CEPCore core) {
		this.core =core;
	}
	/* 
	 * 节点服务器只会收到其他节点发来的请求，不会收到注册注销等一系列系统请求
	 */
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Event event = (Event) msg;
		String serviceId = event.getServiceRequest().getServiceId();
		logger.logMessage(LogLevel.INFO, "接收到请求,id:{},type:{}", serviceId,
				event.getType());
		dealRemoteRequest(event, ctx);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.errorMessage("服务端收到异常", cause);
		ctx.fireExceptionCaught(cause);
	}

	protected void channelRead0(ChannelHandlerContext ctx, Event msg)
			throws Exception {

	}
	
	/**
	 * 处理接收到的远程请求
	 * 
	 * @param event
	 * @param ctx
	 */
	private void dealRemoteRequest(Event event, ChannelHandlerContext ctx) {
		String eventId = event.getEventId();
		String serviceId = event.getServiceRequest().getServiceId();
		if (event.getMode() == Event.EVENT_MODE_ASYNCHRONOUS) {
			// 如果是异步模式，先返回结果
			event.setType(Event.EVENT_TYPE_RESPONSE);
			ctx.writeAndFlush(event);
		}
		try {
			logger.logMessage(LogLevel.INFO, "收到远程请求{0}:{1}", eventId,
					serviceId);
			core.process(event);
			logger.logMessage(LogLevel.INFO, "远程请求已处理{0}:{1}", eventId,
					serviceId);
		} catch (RuntimeException e) {
			logger.errorMessage("远程请求{0}:{1}发生异常", e, eventId, serviceId);
			event.setThrowable(e);
		} catch (Error r) {
			event.setThrowable(r);
		} finally {
			if (ctx != null) {
				event.setType(Event.EVENT_TYPE_RESPONSE);
				ctx.writeAndFlush(event);
				logger.logMessage(LogLevel.INFO, "远程请求已写出{0}:{1}", eventId,
						serviceId);
			}
		}

	}
}
