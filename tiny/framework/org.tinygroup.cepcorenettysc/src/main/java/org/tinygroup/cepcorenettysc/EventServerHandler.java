/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.cepcorenettysc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcorenettysc.operator.ScUnregTrigger;
import org.tinygroup.cepcorenettysc.remote.EventClientDaemonRunnable;
import org.tinygroup.cepcorenettysc.remote.NettyEventProcessor;
import org.tinygroup.cepcorenettysc.remote.NettyEventProcessorConatiner;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.net.ServerHandler;
import org.tinygroup.net.daemon.DaemonUtils;
import org.tinygroup.net.exception.InterruptedRuntimeException;

public class EventServerHandler extends ServerHandler {
	static final Logger logger = LoggerFactory
			.getLogger(EventServerHandler.class);
	Map<String, Event> eventMap = new ConcurrentHashMap<String, Event>();// 存放要处理的事件
	Map<String, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<String, ChannelHandlerContext>();// 存放连接上下文
	ExecutorService executorService = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors() * 2);
	static Map<Node, List<ServiceInfo>> nodeServices = new HashMap<Node, List<ServiceInfo>>();
	static Map<String, Node> nodes = new HashMap<String, Node>();
	static Map<String, EventClientDaemonRunnable> clients = new HashMap<String, EventClientDaemonRunnable>();

	public void stop() {
		for (EventClientDaemonRunnable client : clients.values()) {
			client.stop();
		}
	}

	private EventClientDaemonRunnable getClient(String nodeString) {
		if (clients.containsKey(nodeString)) {
			return clients.get(nodeString);
		}
		Node remoteNode = nodes.get(nodeString);
		EventClientDaemonRunnable client = new EventClientDaemonRunnable(
				remoteNode.getIp(), Integer.parseInt(remoteNode.getPort()),
				false);
		client.addPostEventTrigger(new ScUnregTrigger());
		DaemonUtils.daemon(remoteNode.toString(), client);
		clients.put(nodeString, client);
		return client;
	}

	protected void processObject(Object message, ChannelHandlerContext ctx) {
		// 先记录
		Event event = (Event) message;
		String eventId = event.getEventId();
		eventMap.put(eventId, event);
		channelHandlerContextMap.put(eventId, ctx);

		String serviceId = HandlerUtil.getServiceId(event);
		logger.logMessage(LogLevel.INFO, "接收到请求服务id:{}", serviceId);
		if (NettyCepCoreUtil.AR_TO_SC.equals(serviceId)) { // AR向Sc发起
			logger.logMessage(LogLevel.INFO, "请求{}由普通节点发向服务中心", serviceId);
			Context c = event.getServiceRequest().getContext();
			Node remoteNode = c.get(NettyCepCoreUtil.NODE_KEY);
			logger.logMessage(LogLevel.INFO, "发起节点:{}", remoteNode.toString());
			String type = c.get(NettyCepCoreUtil.TYPE_KEY);
			if (NettyCepCoreUtil.REG_KEY.equals(type)) {
				logger.logMessage(LogLevel.INFO, "请求为注册请求");
				arRegToSc(c); // ar向sc注册
			} else if (NettyCepCoreUtil.UNREG_KEY.equals(type)) {
				logger.logMessage(LogLevel.INFO, "请求为注销请求");
				arUnregToSc(c); // ar向sc注销
			}
			clearRequest(event.getEventId());
			event.setType(Event.EVENT_TYPE_RESPONSE);
			ctx.getChannel().write(event);

		} else if (NettyCepCoreUtil.SC_TO_AR.equals(serviceId)) {
			logger.logMessage(LogLevel.INFO, "请求{}由服务中心发向普通节点", serviceId);
			Context c = event.getServiceRequest().getContext();
			String type = c.get(NettyCepCoreUtil.TYPE_KEY);
			if (NettyCepCoreUtil.REG_KEY.equals(type)) {
				logger.logMessage(LogLevel.INFO, "请求为注册请求");
				scRegToAr(c);
			} else if (NettyCepCoreUtil.UNREG_KEY.equals(type)) {
				logger.logMessage(LogLevel.INFO, "请求为注销请求");
				scUnregToAr(c);
			}

			clearRequest(event.getEventId());
			event.setType(Event.EVENT_TYPE_RESPONSE);
			ctx.getChannel().write(event);
		} else {
			run(event);
			// 获取处理器进行处理
			// EventProcessor eventProcessor = new EventProcessor(event);
			// executorService.execute(eventProcessor);
		}

	}

	private void scRegToAr(Context c) {
		Node remoteNode = c.get(NettyCepCoreUtil.NODE_KEY);
		List<ServiceInfo> list = c.get(NettyCepCoreUtil.SC_TO_AR_SERVICE_KEY);
		logger.logMessage(LogLevel.INFO, "开始注册节点:{},为节点创建服务处理器",
				remoteNode.toString());
		NettyEventProcessor ne = new NettyEventProcessor(remoteNode, list);
		CEPCore core = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader())
				.getBean(CEPCore.CEP_CORE_BEAN);
		NettyEventProcessorConatiner.add(remoteNode.toString(), ne, core);
		logger.logMessage(LogLevel.INFO, "为节点:{}创建服务处理器完成",
				remoteNode.toString());
	}

	private void scUnregToAr(Context c) {
		Node remoteNode = c.get(NettyCepCoreUtil.NODE_KEY);
		logger.logMessage(LogLevel.INFO, "开始注销节点:{}", remoteNode.toString());
		CEPCore core = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader())
				.getBean(CEPCore.CEP_CORE_BEAN);
		NettyEventProcessorConatiner.remove(remoteNode.toString(), core);
		logger.logMessage(LogLevel.INFO, "注销节点:{}完成", remoteNode.toString());
	}

	private void arRegToSc(Context c) {
		logger.logMessage(LogLevel.INFO, "开始处理节点向服务中心发起的注册请求");
		List<ServiceInfo> list = c.get(NettyCepCoreUtil.AR_TO_SC_SERVICE_KEY);
		Node remoteNode = c.get(NettyCepCoreUtil.NODE_KEY);
		String nodeString = remoteNode.toString();
		// 判断注册节点是否已存在，若存在则清理
		if (nodes.containsKey(nodeString)) {
			Node containNode = nodes.get(nodeString);
			nodeServices.remove(containNode);
			nodes.remove(nodeString);
		}

		logger.logMessage(LogLevel.INFO, "开始将{0}节点注册至已有节点列表", nodeString);
		// 将注册节点信息 发送到 已有节点列表
		for (String string : nodes.keySet()) {
			logger.logMessage(LogLevel.INFO, "开始将{0}节点注册至已有节点:{1}", nodeString,
					string);
			EventClientDaemonRunnable client = getClient(string);// clients.get(string);
																	// //20150321
																	// 之前此处是直接去clients中获取客户端，会出现空值针异常
			Context newContext = new ContextImpl();
			newContext.put(NettyCepCoreUtil.NODE_KEY, remoteNode);
			newContext.put(NettyCepCoreUtil.TYPE_KEY, NettyCepCoreUtil.REG_KEY);
			newContext.put(NettyCepCoreUtil.SC_TO_AR_SERVICE_KEY, list);
			Event newEvent = Event.createEvent(NettyCepCoreUtil.SC_TO_AR,
					newContext);
			// client.getClient().sendObject(newEvent);
			try {
				NettyCepCoreUtil.sendEvent(client.getClient(), newEvent);
			} catch (InterruptedRuntimeException e) {
				logger.errorMessage("向{0}注册{1}时网络异常", e, string, nodeString);
			}
			logger.logMessage(LogLevel.INFO, "将{0}节点注册至已有节点:{1}完成", nodeString,
					string);
		}
		logger.logMessage(LogLevel.INFO, "将{0}节点注册至已有节点列表完成", nodeString);

		// 将已有节点列表放入上下文，后续会被作为结果集 回写回 注册节点
		c.put(NettyCepCoreUtil.SC_TO_AR_SERVICE_KEY, copy());
		nodes.put(nodeString, remoteNode);
		nodeServices.put(remoteNode, list);
		getClient(nodeString); // 创建nodeString对应的client
		// EventClientDaemonRunnable client = new EventClientDaemonRunnable(
		// remoteNode.getIp(), Integer.parseInt(remoteNode.getPort()),
		// false);
		// client.addPostEventTrigger(new ScUnregTrigger());
		// DaemonUtils.daemon(remoteNode.toString(), client);
		// clients.put(nodeString, client);
		logger.logMessage(LogLevel.INFO, "处理节点向服务中心发起的注册请求完成");
	}

	private void arUnregToSc(Context c) {
		logger.logMessage(LogLevel.INFO, "开始处理节点向服务中心发起的注销请求");
		Node remoteNode = c.get(NettyCepCoreUtil.NODE_KEY);
		String nodeString = remoteNode.toString();
		Node containNode = nodes.remove(nodeString);
		nodeServices.remove(containNode);
		clients.remove(nodeString).stop();
		c.put(NettyCepCoreUtil.NODES_KEY, nodes);
		logger.logMessage(LogLevel.INFO, "开始将注销请求发送至已有节点列表");
		// 讲注销节点 发送至已有节点列表，在已有节点列表上注销该节点
		for (String nodeStringVar : clients.keySet()) {
			logger.logMessage(LogLevel.INFO, "向{0}注销{1}", nodeStringVar,
					nodeString);
			// Node node = nodes.get(nodeStringVar);
			EventClient client = getClient(nodeStringVar).getClient();
			Context c2 = new ContextImpl();
			c2.put(NettyCepCoreUtil.NODE_KEY, containNode);
			c2.put(NettyCepCoreUtil.TYPE_KEY, NettyCepCoreUtil.UNREG_KEY);
			Event e = Event.createEvent(NettyCepCoreUtil.SC_TO_AR, c2);
			try {
				NettyCepCoreUtil.sendEvent(client, e);
			} catch (InterruptedRuntimeException exception) {
				logger.errorMessage("向{0}注销{1}时网络异常", exception, nodeStringVar,
						nodeString);
			}
			logger.logMessage(LogLevel.INFO, "向{0}注销{1}完成", nodeStringVar,
					nodeString);
			// client.sendObject(e);
		}
		logger.logMessage(LogLevel.INFO, "将注销请求发送至已有节点列表完成");
		logger.logMessage(LogLevel.INFO, "处理节点向服务中心发起的注销请求完成");
	}

	private Map<Node, List<ServiceInfo>> copy() {
		Map<Node, List<ServiceInfo>> m = new HashMap<Node, List<ServiceInfo>>();
		for (Node node : nodeServices.keySet()) {
			m.put(node, nodeServices.get(node));
		}
		return m;
	}

	public void clearRequest(String eventId) {
		eventMap.remove(eventId);
		channelHandlerContextMap.remove(eventId);
	}

	public void run(Event event) {
		ChannelHandlerContext channelHandlerContext = channelHandlerContextMap
				.get(event.getEventId());
		// try {
		if (channelHandlerContext != null) {
			if (event.getMode() == Event.EVENT_MODE_ASYNCHRONOUS) {
				// 如果是异步模式，先返回结果
				event.setType(Event.EVENT_TYPE_RESPONSE);
				channelHandlerContext.getChannel().write(
						getAsynchronousResponseEvent(event));
			}
			CEPCore core = BeanContainerFactory.getBeanContainer(
					this.getClass().getClassLoader()).getBean(
					CEPCore.CEP_CORE_BEAN);
			core.process(event);
			channelHandlerContext = channelHandlerContextMap.get(event
					.getEventId());
			if (event.getMode() == Event.EVENT_MODE_SYNCHRONOUS) {
				if (channelHandlerContext != null) {
					event.setType(Event.EVENT_TYPE_RESPONSE);
					channelHandlerContext.getChannel().write(event);
				}
			}
		}
		// }
		// catch (Throwable e) {
		// logger.error(e);
		// for (String key : channelHandlerContextMap.keySet()) {
		// if (channelHandlerContextMap.get(key) == channelHandlerContext) {
		// clearRequest(key);
		// }
		// }
		// } finally {
		clearRequest(event.getEventId());
		// }

	}

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.errorMessage("发生错误", e.getCause());
		for (String key : channelHandlerContextMap.keySet()) {
			if (channelHandlerContextMap.get(key) == ctx) {
				Event event = eventMap.get(key);
				event.setType(Event.EVENT_TYPE_RESPONSE);
				event.setThrowable(e.getCause());
				ctx.getChannel().write(event);
				clearRequest(key);

			}
		}

	}

	private Event getAsynchronousResponseEvent(Event event) {
		Event response = new Event();
		response.setEventId(event.getEventId());
		response.setMode(Event.EVENT_MODE_ASYNCHRONOUS);
		response.setType(Event.EVENT_TYPE_RESPONSE);
		return response;
	}

	// class EventProcessor implements Runnable {
	// private Event event;
	//
	// public EventProcessor(Event event) {
	// this.event = event;
	// }
	//
	// public void run() {
	// ChannelHandlerContext channelHandlerContext = channelHandlerContextMap
	// .get(event.getEventId());
	// try {
	// if (channelHandlerContext != null) {
	// if (event.getMode() == Event.EVENT_MODE_ASYNCHRONOUS) {
	// // 如果是异步模式，先返回结果
	// event.setType(Event.EVENT_TYPE_RESPONSE);
	// channelHandlerContext.getChannel().write(
	// getAsynchronousResponseEvent(event));
	// }
	// CEPCore core = BeanContainerFactory.getBeanContainer(
	// this.getClass().getClassLoader()).getBean(
	// CEPCore.CEP_CORE_BEAN);
	// core.process(event);
	// channelHandlerContext = channelHandlerContextMap.get(event
	// .getEventId());
	// if (event.getMode() == Event.EVENT_MODE_SYNCHRONOUS) {
	// if (channelHandlerContext != null) {
	// event.setType(Event.EVENT_TYPE_RESPONSE);
	// channelHandlerContext.getChannel().write(event);
	// }
	// }
	// }
	// } catch (Throwable e) {
	// logger.error(e);
	// for (String key : channelHandlerContextMap.keySet()) {
	// if (channelHandlerContextMap.get(key) == channelHandlerContext) {
	// clearRequest(key);
	// }
	// }
	// } finally {
	// clearRequest(event.getEventId());
	// }
	//
	// }
	//
	// private Object getAsynchronousResponseEvent(Event event) {
	// Event response = new Event();
	// response.setEventId(event.getEventId());
	// response.setMode(Event.EVENT_MODE_ASYNCHRONOUS);
	// response.setType(Event.EVENT_TYPE_RESPONSE);
	// return response;
	// }
	//
	// }

	public static void remove(EventClientDaemonRunnable client) {
		logger.logMessage(LogLevel.INFO, "开始移除连接");
		String removeNodeString = null;
		for (String s : clients.keySet()) {
			if (client == clients.get(s)) {
				removeNodeString = s;
				break;
			}
		}
		logger.logMessage(LogLevel.INFO, "连接的节点字符串为:{}", removeNodeString);
		if (removeNodeString == null) {
			logger.logMessage(LogLevel.INFO, "无需移除");
			return;
		}
		Node removeNode = nodes.remove(removeNodeString);
		clients.remove(removeNodeString);
		nodeServices.remove(removeNode);
		logger.logMessage(LogLevel.INFO, "移除连接完成");
	}

}
