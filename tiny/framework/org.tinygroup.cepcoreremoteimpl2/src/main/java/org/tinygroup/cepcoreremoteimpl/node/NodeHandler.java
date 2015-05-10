package org.tinygroup.cepcoreremoteimpl.node;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.CEPCoreEventHandler;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 和SC连接的处理器
 * @author chenjiao
 *
 */
public class NodeHandler extends SimpleChannelInboundHandler<Event> {
	private static Logger logger = LoggerFactory.getLogger(NodeHandler.class);
	private NodeEventHandler nodeEventHandler;
	private CEPCoreClientImpl client;

	public NodeHandler(CEPCoreClientImpl client, Node node, CEPCore core) {
		this.client = client;
		nodeEventHandler = new NodeEventHandler(core, node);
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 设置客户端为就绪状态
		client.doReady();
		// 当前客户端连接的目标是SC，则发起注册
		nodeEventHandler.regToSc(ctx);
	}

	/**
	 * 向SC重新发起注册(用于bundle start时)
	 * 
	 * @param client
	 */
	public void reRegToSc(CEPCoreClientImpl client) {
		nodeEventHandler.reRegToSc(client);
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Event event = (Event) msg;
		String serviceId = event.getServiceRequest().getServiceId();
		logger.logMessage(LogLevel.INFO, "接收到请求,id:{},type:{}", serviceId,
				event.getType());
		boolean isResponse = (Event.EVENT_TYPE_RESPONSE == event.getType());
		if (CEPCoreEventHandler.NODE_RE_REG_TO_SC_REQUEST.equals(serviceId)
				&& isResponse) {// 向SC重新注册时SC的返回
			nodeEventHandler.dealNodeRegResponse(event);
			ResponseManager.updateResponse(event.getEventId(), event);
		} else if (CEPCoreEventHandler.NODE_REG_TO_SC_REQUEST.equals(serviceId)
				&& isResponse) {// 向SC注册时SC的返回
			nodeEventHandler.dealNodeRegResponse(event);
		} else if (CEPCoreEventHandler.NODE_UNREG_TO_SC_REQUEST
				.equals(serviceId) && isResponse) {// 向SC注销时SC的返回
			nodeEventHandler.dealNodeUnregResponse(event);
		} else if (CEPCoreEventHandler.SC_REG_NODE_TO_NODE_REQUEST
				.equals(serviceId)) {// SC向AR发起的注册AR请求
			nodeEventHandler.dealScRegNodeToNode(event, ctx);
		} else if (CEPCoreEventHandler.SC_UNREG_NODE_TO_NODE_REQUEST
				.equals(serviceId)) {// SC向AR发起的注销AR请求
			nodeEventHandler.dealScUnregNodeToNode(event, ctx);
		} else if (isResponse) {
			// processResult(event, ctx); // 处理服务的返回结果
			logger.errorMessage("预料外的请求响应,服务id" + serviceId);
		} else {
			// processResult(event, ctx); // 处理服务的返回结果
			logger.errorMessage("预料外的请求,服务id" + serviceId);
		}
	}


	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		client.setReady(false);
		// NodeHandler此处是和SC断开连接
		// 和SC断开会自动重连，所以此处无需处理
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
