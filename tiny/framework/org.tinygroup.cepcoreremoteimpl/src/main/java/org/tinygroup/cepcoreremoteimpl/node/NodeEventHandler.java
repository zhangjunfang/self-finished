package org.tinygroup.cepcoreremoteimpl.node;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.CEPCoreEventHandler;
import org.tinygroup.cepcoreremoteimpl.RemoteEventProcessorConatiner;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class NodeEventHandler extends CEPCoreEventHandler {
	private static Logger logger = LoggerFactory
			.getLogger(NodeEventHandler.class);

	public NodeEventHandler(CEPCore core, Node node) {
		setCore(core);
		setNode(node);
	}

	public NodeEventHandler() {
	}

	/**
	 * 向SC注册当前节点
	 * 
	 * @param ctx
	 */
	public void regToSc(ChannelHandlerContext ctx) {
		logger.logMessage(LogLevel.INFO, "开始向服务中心发起注册");
		Event request = getNodeRegRequestEvent();
		ctx.writeAndFlush(request);
		logger.logMessage(LogLevel.INFO, "向服务中心发起注册完成");
	}

	/**
	 * 重新向SC注册当前节点
	 * 
	 * @param client
	 */
	public void reRegToSc(CEPCoreClientImpl client) {
		logger.logMessage(LogLevel.INFO, "重新向服务中心发起注册");
		Event request = getNodeReRegRequestEvent();
		client.sentEvent(request);
		logger.logMessage(LogLevel.INFO, "向服务中心重新注册完成");
	}

	/**
	 * 创建节点注册Event
	 * 
	 * @return
	 */
	public Event getNodeReRegRequestEvent() {
		Context c = new ContextImpl();
		List<ServiceInfo> list = getCore().getServiceInfos();
		if (logger.isEnabled(LogLevel.INFO)) {
			logger.logMessage(LogLevel.INFO, "当前节点服务数{}", list.size());
		}
		int version = getCore()
				.getServiceInfosVersion();
		c.put(NODE_REG_TO_SC_SERVICE_VERSION_KEY, version);
		c.put(NODE_REG_TO_SC_SERVICE_KEY, list);
		c.put(NODE_KEY, getNode());
		Event e = Event.createEvent(NODE_RE_REG_TO_SC_REQUEST, c);
		return e;
	}

	public Event getNodeRegRequestEvent() {
		Context c = new ContextImpl();
		List<ServiceInfo> list = getCore().getServiceInfos();
		if (logger.isEnabled(LogLevel.INFO)) {
			logger.logMessage(LogLevel.INFO, "当前节点服务数{}", list.size());
		}
		int version = getCore()
				.getServiceInfosVersion();
		c.put(NODE_REG_TO_SC_SERVICE_VERSION_KEY, version);
		c.put(NODE_REG_TO_SC_SERVICE_KEY, list);
		c.put(NODE_KEY, getNode());
		Event e = Event.createEvent(NODE_REG_TO_SC_REQUEST, c);
		return e;
	}

	/**
	 * 向SC注销当前节点
	 * 
	 * @param ctx
	 */
	public void unregToSc(ChannelHandlerContext ctx) {
		logger.logMessage(LogLevel.INFO, "开始向服务中心发起注销");
		Event request = getNodeUnregRequestEvent();
		ctx.writeAndFlush(request);
		logger.logMessage(LogLevel.INFO, "向服务中心发起注销完成");
	}

	/**
	 * 创建节点注册Event
	 * 
	 * @return
	 */
	private Event getNodeUnregRequestEvent() {
		Context c = new ContextImpl();
		c.put(NODE_KEY, getNode());
		Event e = Event.createEvent(NODE_UNREG_TO_SC_REQUEST, c);
		return e;
	}

	/**
	 * 处理RA注册时SC返回的响应Event
	 * 
	 * @param result
	 */
	public void dealNodeRegResponse(Event result) {
		logger.logMessage(LogLevel.INFO, "接收到服务中心返回的注册响应");
		Context c2 = result.getServiceRequest().getContext();
		Map<Node, List<ServiceInfo>> nodeServices = c2
				.get(SC_TO_NODE_SERVICE_KEY);
		Map<String, Integer> versions = c2.get(SC_TO_NODE_SERVICE_VERSIONS_KEY);

		logger.logMessage(LogLevel.INFO, "接收到服务中心发送的其他节点数{}",
				nodeServices.size() - 1);
		for (Node node : nodeServices.keySet()) {
			if (getNode().toString().equals(node.toString())) {
				continue;
			}
			int version = versions.get(node.toString());
			logger.logMessage(LogLevel.INFO, "为节点:{}创建服务处理器,服务版本{}",
					node.toString(), version);
			RemoteEventProcessorConatiner.add(node, nodeServices.get(node), getCore(),version);
			logger.logMessage(LogLevel.INFO, "为节点:{}创建服务处理器完成", node.toString());
		}
		logger.logMessage(LogLevel.INFO, "处理服务中心返回的注册响应完毕");
	}

	public void dealNodeUnregResponse(Event result) {
		logger.logMessage(LogLevel.INFO, "接收到服务中心返回的注销响应");
		Context c2 = result.getServiceRequest().getContext();
		Map<String, Node> nodeServices = c2.get(NODES_KEY);
		logger.logMessage(LogLevel.INFO, "需要注销的其他节点数{}", nodeServices.size());
		for (String nodeString : nodeServices.keySet()) {
			RemoteEventProcessorConatiner.remove(nodeString, getCore());
		}
		logger.logMessage(LogLevel.INFO, "处理服务中心返回的注销响应完毕");
	}

	public void dealScRegNodeToNode(Event event, ChannelHandlerContext ctx) {
		logger.logMessage(LogLevel.INFO, "开始处理服务中心发来的其它节点注册请求");
		Context c = event.getServiceRequest().getContext();
		Node remoteNode = c.get(NODE_KEY);
		List<ServiceInfo> list = c.get(SC_TO_NODE_SERVICE_KEY);
		int version = c.get(SC_TO_NODE_SERVICE_VERSIONS_KEY);
		logger.logMessage(LogLevel.INFO, "为节点{}创建服务处理器,服务版本{}",
				remoteNode.toString(),version);
		RemoteEventProcessorConatiner.add(remoteNode, list, getCore(),version);
		logger.logMessage(LogLevel.INFO, "为节点{}创建服务处理器完成",
				remoteNode.toString());
		logger.logMessage(LogLevel.INFO, "处理服务中心发来的其它节点注册请求完成");
	}

	public void dealScUnregNodeToNode(Event event, ChannelHandlerContext ctx) {
		logger.logMessage(LogLevel.INFO, "开始处理服务中心发来的其它节点注销请求");
		Context c = event.getServiceRequest().getContext();
		Node remoteNode = c.get(NODE_KEY);
		logger.logMessage(LogLevel.INFO, "开始注销节点:{}", remoteNode.toString());
		RemoteEventProcessorConatiner.remove(remoteNode.toString(), getCore());
		logger.logMessage(LogLevel.INFO, "注销节点:{}完成", remoteNode.toString());
		logger.logMessage(LogLevel.INFO, "处理服务中心发来的其它节点注销请求完成");
	}

}
