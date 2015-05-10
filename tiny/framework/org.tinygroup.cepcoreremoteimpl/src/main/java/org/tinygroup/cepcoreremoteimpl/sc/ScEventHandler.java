package org.tinygroup.cepcoreremoteimpl.sc;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tinygroup.cepcoreremoteimpl.CEPCoreEventHandler;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ScEventHandler extends CEPCoreEventHandler {
	private static Logger logger = LoggerFactory
			.getLogger(ScEventHandler.class);
	private static Map<String, Node> nodes = new ConcurrentHashMap<String, Node>();
	private static Map<Node, List<ServiceInfo>> nodeServices = new ConcurrentHashMap<Node, List<ServiceInfo>>();
	private static Map<String,Integer> versions = new ConcurrentHashMap<String, Integer>();
	//此列表，只会在SC节点时，会有数据存放
	private static Map<String, ChannelHandlerContext> ctxs = new ConcurrentHashMap<String, ChannelHandlerContext>();

	/**
	 * 处理Node向SC发起的注册请求
	 * 
	 * @param event
	 * @param ctx
	 */
	public void dealNodeRegToSc(Event event, ChannelHandlerContext ctx) {

		logger.logMessage(LogLevel.INFO, "开始处理节点向服务中心发起的注册请求");
		Context c = event.getServiceRequest().getContext();
		List<ServiceInfo> list = c.get(NODE_REG_TO_SC_SERVICE_KEY);
		Node remoteNode = c.get(NODE_KEY);
		int version = c.get(NODE_REG_TO_SC_SERVICE_VERSION_KEY);
		
		String nodeString = remoteNode.toString();
		ctxs.put(nodeString, ctx);
		
		// 判断注册节点是否已存在，若存在则清理
//		checkCurrentNode(nodeString);
		// 将节点注册信息放入静态存储中
		versions.put(nodeString, version);
		nodes.put(nodeString, remoteNode);
		nodeServices.put(remoteNode, list);
		logger.logMessage(LogLevel.INFO, "节点{}服务版本{}",nodeString,version);
		// 将该节点注册至已有节点列表
		scRegCurrentNodeToNodes(nodeString, remoteNode, list);
		// 将已有节点列表注册至该节点
		scRegNodesToCurrentNode(c, event, ctx, nodeString);
		logger.logMessage(LogLevel.INFO, "处理节点向服务中心发起的注册请求完成");
	}

//	/**
//	 * 判断当前注册的Node节点是否已存在，若存在则清理
//	 * 
//	 * @param nodeString
//	 */
//	private void checkCurrentNode(String nodeString) {
//		if (nodes.containsKey(nodeString)) {
//			logger.logMessage(LogLevel.INFO, "{0}节点已存在,开始清理", nodeString);
//			Node containNode = nodes.get(nodeString);
//			nodeServices.remove(containNode);
//			nodes.remove(nodeString);
//			logger.logMessage(LogLevel.INFO, "{0}节点已存在,开始清理结束", nodeString);
//		}
//	}

	/**
	 * 将当前注册的Node节点发送到SC上已有的Node节点列表
	 * 
	 * @param nodeString
	 * @param node
	 * @param list
	 */
	private void scRegCurrentNodeToNodes(String nodeString, Node node,
			List<ServiceInfo> list) {
		logger.logMessage(LogLevel.INFO, "开始将{0}节点注册至已有节点列表", nodeString);
		// 将注册节点信息 发送到 已有节点列表
		for (String string : nodes.keySet()) {
			if(nodeString.equals(string)){
				continue;
			}
			logger.logMessage(LogLevel.INFO, "开始将{0}节点注册至已有节点:{1}", nodeString,
					string);
			Context newContext = new ContextImpl();
			newContext.put(NODE_KEY, node);
			newContext.put(TYPE_KEY, REG_KEY);
			newContext.put(SC_TO_NODE_SERVICE_KEY, list);
			newContext.put(SC_TO_NODE_SERVICE_VERSIONS_KEY, versions.get(nodeString));
			Event newEvent = Event.createEvent(SC_REG_NODE_TO_NODE_REQUEST,
					newContext);
			ctxs.get(string).writeAndFlush(newEvent);
			logger.logMessage(LogLevel.INFO, "将{0}节点注册至已有节点:{1}完成", nodeString,
					string);
		}
		logger.logMessage(LogLevel.INFO, "将{0}节点注册至已有节点列表完成", nodeString);
	}

	/**
	 * 将SC上已有的Node节点信息返写回当前注册的Node节点
	 * 
	 * @param c
	 * @param event
	 * @param ctx
	 */
	private void scRegNodesToCurrentNode(Context c, Event event,
			ChannelHandlerContext ctx, String nodeString) {
		// 将已有节点列表放入上下文，作为结果集回写回注册节点
		logger.logMessage(LogLevel.INFO, "开始将已有节点列表注册至{0}节点", nodeString);
		c.put(SC_TO_NODE_SERVICE_KEY, copy());
		c.put(SC_TO_NODE_SERVICE_VERSIONS_KEY, versions);
		event.setType(Event.EVENT_TYPE_RESPONSE);
		ctx.writeAndFlush(event);
		logger.logMessage(LogLevel.INFO, "将已有节点列表注册至{0}节点完成", nodeString);
	}

	private Map<Node, List<ServiceInfo>> copy() {
		Map<Node, List<ServiceInfo>> m = new HashMap<Node, List<ServiceInfo>>();
		for (Node node : nodeServices.keySet()) {
			m.put(node, nodeServices.get(node));
		}
		return m;
	}

	public void dealNodeUnregToSc(Event event, ChannelHandlerContext ctx) {
		logger.logMessage(LogLevel.INFO, "开始处理节点向服务中心发起的注销请求");
		Context c = event.getServiceRequest().getContext();
		Node remoteNode = c.get(NODE_KEY);
		String nodeString = remoteNode.toString();
		// 从节点缓存中移出当前节点
		Node currentNode = nodes.remove(nodeString);
		nodeServices.remove(nodeString);
		// 从链接缓存中移出当前链接，避免后续循环会向自己发送自己
		ctxs.remove(nodeString);
		// 将该注销消息推送至其它节点
		scUnregCurrentNodeToNodes(nodeString, currentNode);
		// 返回SC上其它的节点信息
		c.put(NODES_KEY, nodes);
		ctx.writeAndFlush(event);
		ctx.close();
		logger.logMessage(LogLevel.INFO, "处理节点向服务中心发起的注销请求完成");
	}

	/**
	 * 将当前注册的Node节点发送到SC上已有的Node节点列表
	 * 
	 * @param nodeString
	 * @param node
	 * @param list
	 */
	private void scUnregCurrentNodeToNodes(String nodeString, Node currentNode) {
		// 讲注销节点 发送至已有节点列表，在已有节点列表上注销该节点
		logger.logMessage(LogLevel.INFO, "开始将注销请求发送至已有节点列表");
		for (String nodeStringVar : ctxs.keySet()) {
			logger.logMessage(LogLevel.INFO, "向{0}注销{1}", nodeStringVar,
					nodeString);
			// Node node = nodes.get(nodeStringVar);
			Context c2 = new ContextImpl();
			c2.put(NODE_KEY, currentNode);
			Event e = Event.createEvent(SC_UNREG_NODE_TO_NODE_REQUEST, c2);
			ctxs.get(nodeStringVar).writeAndFlush(e);
			logger.logMessage(LogLevel.INFO, "向{0}注销{1}完成", nodeStringVar,
					nodeString);
		}
		logger.logMessage(LogLevel.INFO, "将注销请求发送至已有节点列表完成");
	}

	/**
	 * 处理接收到的远程请求
	 * 
	 * @param event
	 * @param ctx
	 */
	public void dealRemoteRequest(Event event, ChannelHandlerContext ctx) {
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
			getCore().process(event);
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

	/**
	 * SC移除Node节点的连接信息
	 * @param ctx
	 */
	public void removeNodeByCtx(ChannelHandlerContext ctx) {
		String targetToRemoveNode = null;
		for (String nodeString : ctxs.keySet()) {
			if (ctxs.get(nodeString) == ctx) {
				targetToRemoveNode = nodeString;
			}
		}
		if (targetToRemoveNode == null) {
			return;
		}
		logger.logMessage(LogLevel.INFO, "开始移除节点:{}", targetToRemoveNode);
		if (targetToRemoveNode != null) {
			Node targetNode = nodes.remove(targetToRemoveNode);
			ctxs.remove(targetToRemoveNode);
			nodeServices.remove(targetNode);
			scUnregCurrentNodeToNodes(targetToRemoveNode, targetNode);
		}

		logger.logMessage(LogLevel.INFO, "移除节点:{}完成", targetToRemoveNode);
	}
}
