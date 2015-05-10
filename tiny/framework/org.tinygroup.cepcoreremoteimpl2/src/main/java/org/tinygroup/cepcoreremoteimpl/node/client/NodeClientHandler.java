package org.tinygroup.cepcoreremoteimpl.node.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.node.ResponseManager;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class NodeClientHandler extends SimpleChannelInboundHandler<Event> {
	private static Logger logger = LoggerFactory
			.getLogger(NodeClientHandler.class);
	
	private NodeClientImpl client;

	public NodeClientHandler(NodeClientImpl client, Node node, CEPCore core) {
		this.client = client;
	}
	
	//当前连接的是另一个Node,无需做额外处理
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 设置客户端为就绪状态
		client.doReady();
	}

	//接收到的肯定是请求响应
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Event event = (Event) msg;
		String serviceId = event.getServiceRequest().getServiceId();
		logger.logMessage(LogLevel.INFO, "接收到请求,id:{},type:{}", serviceId,
				event.getType());
		boolean isResponse = (Event.EVENT_TYPE_RESPONSE == event.getType());
		if (isResponse) {
			processResult(event, ctx); // 处理服务的返回结果
		} else {
			logger.errorMessage("客户端收到未知请求" + serviceId);

		}
	}

	protected void processResult(Object response, ChannelHandlerContext ctx)
			throws Exception {
		Event event = (Event) response;
		String eventId = event.getEventId();
		logger.logMessage(LogLevel.INFO, "接收到Event:{0}的请求响应,请求id:{1}", eventId,
				event.getServiceRequest().getServiceId());
		ResponseManager.updateResponse(eventId, event);
	}

	//连接的是Node服务端，无需作额外处理
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		client.setReady(false);
		ctx.fireChannelInactive();
	}

	protected void channelRead0(ChannelHandlerContext ctx, Event msg)
			throws Exception {

	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.errorMessage("客户端收到异常", cause);
		ctx.fireExceptionCaught(cause);
	}
}
