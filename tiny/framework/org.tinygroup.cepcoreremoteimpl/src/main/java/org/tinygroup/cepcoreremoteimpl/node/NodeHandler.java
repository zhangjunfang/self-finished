package org.tinygroup.cepcoreremoteimpl.node;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.CEPCoreEventHandler;
import org.tinygroup.cepcoreremoteimpl.util.RemoteCepCoreUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

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
		// 如果当前客户端连接的目标是SC，则发起注册
		if (RemoteCepCoreUtil.checkSc(client.getRemotePort(),
				client.getRemoteHost())) {
			nodeEventHandler.regToSc(ctx);
		}

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
		logger.logMessage(LogLevel.INFO, "接收到请求,id:{},type:", serviceId,
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
			processResult(event, ctx); // 处理服务的返回结果
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

	// TODO:是否需要进行以下考虑，暂不执行
	// 如果是和SC断开连接，那么它无进行其他操作的意义
	// 如果是和Node断开连接，那么它同样无需通知SC，因为长连接，SC同样会感知该Node断开
	// 最佳处理，此处向SC发送一个问询行为，问SC是否也发现该Node断开
	// 1、如果SC也发现，则将该Node断开
	// 2、如SC未发现，SC将保存该信息，同时此处Node进行该节点的重连尝试，
	// 当Node进行重连时，若SC发现该Node断开，则SC向Node发送该信息，此节点将不再进行重连
	// (此方案下，建议开启所有的Client reconnect = true，这样所有的连接断开均会进行自动重连
	// 当SC通知时，只需关闭该重连即可
	// 此处要考虑，重连时，无须重连所有连接(因为默认有10个，该属性后期会开放配置)，只需进行一个重连
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// if (!NettyCepCoreUtil.checkSc(client.getRemotePort(),
		// client.getRemoteHost())) {
		// nodeEventHandler.unregToSc(ctx);
		// }
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
