package org.tinygroup.cepcoreremoteimpl.node;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.cepcoreremoteimpl.sc.CEPCoreServerImpl;
import org.tinygroup.cepcoreremoteimpl.util.ParamUtil;
import org.tinygroup.cepcoreremoteimpl.util.RemoteCepCoreUtil;
import org.tinygroup.event.central.Node;
import org.tinygroup.xmlparser.node.XmlNode;

public class NodeOperator implements CEPCoreOperator {
	private int localPort;
	private int remotePort;
	private String localHost;
	private String remoteHost;
	private int weight;

	private CEPCoreServerImpl server;
	private CEPCoreClientImpl client;
	private Node node;
	

	public NodeOperator(int localPort, int remotePort, String localHost,
			String remoteHost) {
		this.localPort = localPort;
		this.remotePort = remotePort;
		this.localHost = localHost;
		this.remoteHost = remoteHost;
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
		RemoteCepCoreUtil.regScAddress(remotePort, remoteHost);
		server = new CEPCoreServerImpl(localPort);
		server.start();
		client = new CEPCoreClientImpl(remotePort, remoteHost, getNode(cep),
				cep);
		client.start();
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

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
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

	public void setParam(XmlNode param) {
		ParamUtil.parseParam(param);
	}

}
