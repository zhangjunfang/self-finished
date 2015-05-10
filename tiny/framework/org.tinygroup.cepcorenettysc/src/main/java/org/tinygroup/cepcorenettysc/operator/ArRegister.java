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

import java.util.List;
import java.util.Map;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcorenettysc.NettyCepCoreUtil;
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

public class ArRegister {
	EventClientDaemonRunnable client;
	CEPCore core;
	Node node;

	protected static Logger logger = LoggerFactory
			.getLogger(ArRegister.class);

	public ArRegister(EventClientDaemonRunnable client, CEPCore core, Node node) {
		super();
		this.client = client;
		this.core = core;
		this.node = node;
	}

	public void regToSc() {
		logger.logMessage(LogLevel.INFO, "开始向服务中心发起注册");
		List<ServiceInfo> list = core.getServiceInfos();
		Context c = new ContextImpl();
		c.put(NettyCepCoreUtil.AR_TO_SC_SERVICE_KEY, list);
		logger.logMessage(LogLevel.INFO, "当前节点服务数{}", list.size());
		c.put(NettyCepCoreUtil.NODE_KEY, node);
		c.put(NettyCepCoreUtil.TYPE_KEY, NettyCepCoreUtil.REG_KEY);
		Event e = Event.createEvent(NettyCepCoreUtil.AR_TO_SC, c);
		Event result = client.getClient().sendObject(e);
		Context c2 = result.getServiceRequest().getContext();
		Map<Node, List<ServiceInfo>> nodeServices = c2
				.get(NettyCepCoreUtil.SC_TO_AR_SERVICE_KEY);
		logger.logMessage(LogLevel.INFO, "接收到服务中心发送的其他节点数{}",
				nodeServices.size());
		for (Node node : nodeServices.keySet()) {
			logger.logMessage(LogLevel.INFO, "为节点:{}创建服务处理器", node.toString());
			NettyEventProcessor ne = new NettyEventProcessor(node,
					nodeServices.get(node));
			NettyEventProcessorConatiner.add(node.toString(), ne, core);
			logger.logMessage(LogLevel.INFO, "为节点:{}创建服务处理器完成", node.toString());
		}
		logger.logMessage(LogLevel.INFO, "向服务中心注册完成");

	}
}
