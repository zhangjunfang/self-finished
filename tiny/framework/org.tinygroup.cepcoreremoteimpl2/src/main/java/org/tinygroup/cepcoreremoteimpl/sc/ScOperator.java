package org.tinygroup.cepcoreremoteimpl.sc;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.cepcoreremoteimpl.util.ParamUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class ScOperator implements CEPCoreOperator {
	private int port;
	private CEPCoreServerImpl server;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ScOperator(int port) {
		this.port = port;
	}
	
	public ScOperator() {}

	public void startCEPCore(CEPCore cep) {
		server = new CEPCoreServerImpl(port);
		server.start();
	}

	public void stopCEPCore(CEPCore cep) {
		server.stop();
	}

	public void setCEPCore(CEPCore cep) {

	}

	public void setParam(XmlNode node) {
		ParamUtil.parseParam(node);
	}

}
