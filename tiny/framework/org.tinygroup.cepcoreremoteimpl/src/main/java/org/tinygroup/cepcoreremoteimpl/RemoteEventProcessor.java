package org.tinygroup.cepcoreremoteimpl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreremoteimpl.node.CEPCoreClientImpl;
import org.tinygroup.cepcoreremoteimpl.util.ClientGroup;
import org.tinygroup.cepcoreremoteimpl.util.RemoteCepCoreUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class RemoteEventProcessor implements EventProcessor {
	private static Logger logger = LoggerFactory
			.getLogger(RemoteEventProcessor.class);
	private Node remoteNode;
	private List<ServiceInfo> list = new ArrayList<ServiceInfo>();
	private CEPCoreClientImpl client;

	public RemoteEventProcessor(Node remoteNode, List<ServiceInfo> list
			) {
		this.list = list;
		this.remoteNode = remoteNode;
	}

	private void initClient() {
		client = ClientGroup.getClient(remoteNode);
	}

	public void process(Event event) {
		if (client == null) {
			initClient();
		}
		logger.logMessage(LogLevel.INFO,
				"发送请求,目标节点{0}:{1}:{2},请求信息:[serviceId:{3}]",
				remoteNode.getIp(), remoteNode.getPort(), remoteNode
						.getNodeName(), event.getServiceRequest()
						.getServiceId());
		// try {
		CEPCoreClientImpl dealClient = ClientGroup.getClient(remoteNode);
		Event newEvent = RemoteCepCoreUtil.sendEvent(dealClient, event);
		ClientGroup.back(dealClient, remoteNode);
		event.getServiceRequest()
				.getContext()
				.putSubContext(newEvent.getEventId(),
						newEvent.getServiceRequest().getContext());

		logger.logMessage(LogLevel.INFO,
				"请求成功,目标节点{0}:{1}:{2},请求信息:[serviceId:{3}]",
				remoteNode.getIp(), remoteNode.getPort(), remoteNode
						.getNodeName(), event.getServiceRequest()
						.getServiceId());

		// } catch (Exception e) {
		// logger.logMessage(LogLevel.ERROR,
		// "请求失败,目标节点{0}:{1}:{2},请求信息:[serviceId:{3},信息:{5}",
		// remoteNode.getIp(), remoteNode.getPort(), remoteNode
		// .getNodeName(), event.getServiceRequest()
		// .getServiceId(), e.getMessage());
		// stopConnect();
		// throw new CEPConnectException(e, remoteNode);
		// }
	}

	public void stopConnect() {
		// client.stop();
		ClientGroup.unRegRemoteNode(remoteNode);
	}

	public void setCepCore(CEPCore cepCore) {
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

	public boolean isRead() {
		return true;
	}

	public void setRead(boolean read) {

	}

	public void setServiceInfos(List<ServiceInfo> services) {
		this.list = services;
	}
}