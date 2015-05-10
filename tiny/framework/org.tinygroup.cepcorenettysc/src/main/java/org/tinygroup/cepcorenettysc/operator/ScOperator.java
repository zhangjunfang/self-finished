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

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreOperator;
import org.tinygroup.cepcorenettysc.EventServer;
import org.tinygroup.xmlparser.node.XmlNode;

public class ScOperator implements CEPCoreOperator{
//	private CEPCore core;
	private String port;
	EventServer server;
	XmlNode node;
	public void startCEPCore(CEPCore cep) {
		server = new EventServer(Integer.parseInt(port));
		server.run();
	}

	public void stopCEPCore(CEPCore cep) {
		server.stop();
	}

	public void setCEPCore(CEPCore cep) {
//		this.core = cep;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setParam(XmlNode node) {
		this.node = node;
	}

}
