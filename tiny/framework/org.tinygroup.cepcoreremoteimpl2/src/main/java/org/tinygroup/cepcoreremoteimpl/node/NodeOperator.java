package org.tinygroup.cepcoreremoteimpl.node;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.cepcoreremoteimpl.node.server.NodeServerImpl;
import org.tinygroup.cepcoreremoteimpl.util.ParamUtil;
import org.tinygroup.cepcoreremoteimpl.util.RemoteCepCoreUtil;
import org.tinygroup.event.central.Node;
import org.tinygroup.xmlparser.node.XmlNode;

public class NodeOperator implements CEPCoreOperator {
	private int localPort;
	private String localHost;
	private int weight;

	private NodeServerImpl server;
	private CEPCoreClientImpl client;
	private Node node;
	public NodeOperator(int localPort, String localHost) {
		this.localPort = localPort;
		this.localHost = localHost;
	}

	public NodeOperator() {
	}

	private void initNode(CEPCore cep) {
		node = new Node();
		node.setIp(localHost);
		node.setNodeName(cep.getNodeName());
		node.setPort(String.valueOf(localPort));
		node.setWeight(weight);
	}

	private Node getNode(CEPCore cep) {
		if (node == null) {
			initNode(cep);
		}
		return node;
	}

	public void startCEPCore(CEPCore cep) {
		server = new NodeServerImpl(localPort,cep);
		server.start();
		startClient(cep);

	}

	public void startClient(CEPCore cep) {
		//连接各个SC
		for (String remoteString : RemoteCepCoreUtil.getScs()) {
			String[] remoteInfo = remoteString.split(RemoteCepCoreUtil.SEPARATOR);
			client = new CEPCoreClientImpl(Integer.parseInt(remoteInfo[1]), remoteInfo[0], getNode(cep),
					cep);
			client.start();
		}
	}

	public void stopCEPCore(CEPCore cep) {
		client.stop();
		server.stop();
	}

	public void reReg() {
		client.reReg();
	}

	public void setCEPCore(CEPCore cep) {

	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public CEPCoreClientImpl getClient() {
		return client;
	}

	public void setParam(XmlNode config) {
		ParamUtil.parseScs(config);
		ParamUtil.parseParam(config);
	}

}
