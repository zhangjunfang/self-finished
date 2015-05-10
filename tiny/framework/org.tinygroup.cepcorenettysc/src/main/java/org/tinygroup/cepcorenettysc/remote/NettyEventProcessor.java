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
package org.tinygroup.cepcorenettysc.remote;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.exception.CEPConnectException;
import org.tinygroup.cepcorenettysc.NettyCepCoreUtil;
import org.tinygroup.cepcorenettysc.operator.ArUnregTrigger;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.net.daemon.DaemonUtils;
import org.tinygroup.net.exception.InterruptedRuntimeException;

public class NettyEventProcessor implements EventProcessor {
	private static Logger logger = LoggerFactory
			.getLogger(NettyEventProcessor.class);
	private EventClientDaemonRunnable client;
	private Node remoteNode;
	private int timeout = 10;
	private List<ServiceInfo> list = new ArrayList<ServiceInfo>();
	private CEPCore core;

	public NettyEventProcessor(Node remoteNode, List<ServiceInfo> list) {
		this.list = list;
		this.remoteNode = remoteNode;

	}

	private void initClient() {
		client = getNewClient(remoteNode);
	}

	public void process(Event event) {
		if (client == null) {
			initClient();
		}
//		EventClient eventClient = client.getClient();
		logger.logMessage(LogLevel.INFO,
				"发送请求,目标节点{0}:{1}:{2},请求信息:[serviceId:{3}]",
				remoteNode.getIp(), remoteNode.getPort(), remoteNode
						.getNodeName(), event.getServiceRequest()
						.getServiceId());
		try {

//			int i = 1;
//			while (!eventClient.isReady()) {
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					// 此处无须处理
//				}
//				i++;
//				if (i > timeout) {
//					break;
//				}
//			}
//			Event newEvent = eventClient.sendObject(event);
			Event newEvent = NettyCepCoreUtil.sendEvent(client.getClient(), event);
			event.getServiceRequest()
					.getContext()
					.putSubContext(newEvent.getEventId(),
							newEvent.getServiceRequest().getContext());

			logger.logMessage(LogLevel.INFO,
					"请求成功,目标节点{0}:{1}:{2},请求信息:[serviceId:{3}]", remoteNode
							.getIp(), remoteNode.getPort(), remoteNode
							.getNodeName(), event.getServiceRequest()
							.getServiceId());

		} catch (InterruptedRuntimeException e) {
			logger.logMessage(LogLevel.ERROR,
					"请求失败,目标节点{0}:{1}:{2},请求信息:[serviceId:{3},信息:{5}",
					remoteNode.getIp(), remoteNode.getPort(), remoteNode
							.getNodeName(), event.getServiceRequest()
							.getServiceId(), e.getMessage());
			stopConnect();
			throw new CEPConnectException(e, remoteNode);
		} catch (RuntimeException e) {
			logger.errorMessage(e.getMessage(),e);
//			stopConnect();
			throw e;
		}
	}

	public void stopConnect() {
		client.stop();
		client = null;
	}

	public void setCepCore(CEPCore cepCore) {
		core = cepCore;
		initClient();
	}

	public List<ServiceInfo> getServiceInfos() {
		return list;
	}

	public String getId() {
		return remoteNode.toString();
	}

	public int getType() {
		return TYPE_REMOTE;
	}

	public int getWeight() {
		return remoteNode.getWeight();
	}

	public List<String> getRegex() {
		return null;
	}

	private EventClientDaemonRunnable getNewClient(Node remoteNode) {
		String nodeInfo = remoteNode.toString();
		EventClientDaemonRunnable client = new EventClientDaemonRunnable(
				remoteNode.getIp(), Integer.parseInt(remoteNode.getPort()),
				false);
		client.addPostEventTrigger(new ArUnregTrigger(core, this));
		DaemonUtils.daemon(nodeInfo, client);
		return client;
	}

	public boolean isRead() {
		return true;
	}

	public void setRead(boolean read) {

	}
}
