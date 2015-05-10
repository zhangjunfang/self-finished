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
package org.tinygroup.cepcorenettysc.operator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.cepcore.EventProcessorChoose;
import org.tinygroup.cepcore.exception.CEPRunException;
import org.tinygroup.cepcorenettysc.EventClient;
import org.tinygroup.cepcorenettysc.EventServer;
import org.tinygroup.cepcorenettysc.NettyCepCoreUtil;
import org.tinygroup.cepcorenettysc.remote.EventClientDaemonRunnable;
import org.tinygroup.cepcorenettysc.remote.NettyEventProcessorConatiner;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.net.daemon.DaemonUtils;
import org.tinygroup.xmlparser.node.XmlNode;

public class ArOperator implements CEPCoreOperator {
	protected static Logger logger = LoggerFactory.getLogger(ArOperator.class);
	private CEPCore core;
	private String ip;
	private String port;
	private String remotePort;
	String remoteIp;
	private int weight = EventProcessorChoose.DEFAULT_WEIGHT;
	private Node localNode;
	EventServer server;
	EventClientDaemonRunnable client;
	int timeout = 5000;
	XmlNode node;
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ArOperator() {
		super();
	}

	public ArOperator(String ip, String port, String remoteIp,
			String remotePort, int weight) {
		super();
		this.ip = ip;
		this.port = port;
		this.remoteIp = remoteIp;
		this.remotePort = remotePort;
		this.weight = weight;
	}

	protected Node getNode() {
		if (localNode != null) {
			return localNode;
		}
		localNode = new Node();
		String lIp = this.ip;
		if (lIp == null || "".equals(lIp)) {
			try {
				lIp = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				throw new CEPRunException(e, "cepcore.getIpError");
			}
		}
		localNode.setIp(lIp);
		if (port == null || "".equals(port)) {
			port = "8888";
		}
		localNode.setPort(port);
		localNode.setNodeName(core.getNodeName());
		localNode.setType(Node.CEP_NODE);
		localNode.setWeight(weight);
		return localNode;
	}

	public void startCEPCore(CEPCore cep) {
		server = new EventServer(Integer.parseInt(port));
		server.run();
		client = new EventClientDaemonRunnable(remoteIp,
				Integer.parseInt(remotePort),true);
		client.setTimeout(timeout);
		client.addPreEventTrigger( new ArRegTrigger(cep, getNode()));
		DaemonUtils.daemon(getNode().toString(), client);
		
	}

	public void reReg(){
		ArRegister ar = new ArRegister(client, core, getNode());
		ar.regToSc();
	}
	

	private void unregToSc(EventClient client) {
		logger.logMessage(LogLevel.INFO, "开始向服务中心发起注销");
		Context c = new ContextImpl();
		c.put(NettyCepCoreUtil.NODE_KEY, getNode());
		c.put(NettyCepCoreUtil.TYPE_KEY, NettyCepCoreUtil.UNREG_KEY);
		Event e = Event.createEvent(NettyCepCoreUtil.AR_TO_SC, c);
		Event result = client.sendObject(e);
		Context c2 = result.getServiceRequest().getContext();
		Map<String, Node> nodeServices = c2
				.get(NettyCepCoreUtil.NODES_KEY);
		logger.logMessage(LogLevel.INFO, "需要注销的其他节点数{}",nodeServices.size());
		for (String nodeString : nodeServices.keySet()) {
			NettyEventProcessorConatiner.remove(nodeString, core);
		}
		logger.logMessage(LogLevel.INFO, "向服务中心注销完成");
	}

	public void stopCEPCore(CEPCore cep) {
		unregToSc(client.getClient());
		//停止自己的监听端口
		server.stop();
		//停止自己向sc发起的连接
		client.stop();
		//停止自己向其他ar的连接
		NettyEventProcessorConatiner.stop();
	}
	
	public void setCEPCore(CEPCore cep) {
		core = cep;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setParam(XmlNode node) {
		this.node = node;
	}

}
