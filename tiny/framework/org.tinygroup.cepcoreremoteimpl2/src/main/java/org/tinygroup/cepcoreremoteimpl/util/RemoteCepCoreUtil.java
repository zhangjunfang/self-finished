package org.tinygroup.cepcoreremoteimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcoreremoteimpl.node.client.NodeClientImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;

public class RemoteCepCoreUtil {
	public static final String SEPARATOR = ":";
	private static List<String> scs = new ArrayList<String>();

	public static List<String> getScs() {
		return scs;
	}

	public static Event sendEvent(NodeClientImpl client, Event event) {
		return client.sentEvent(event);
	}

	public static void regScAddress(int remotePort, String remoteHost) {
		scs.add(remoteHost + SEPARATOR + remotePort);
	}

	public static boolean checkSc(int remotePort, String remoteHost) {
		return scs.contains(remoteHost + SEPARATOR + remotePort);
	}

	public static NodeClientImpl getClient(Node remoteNode) {
		NodeClientImpl client = new NodeClientImpl(
				Integer.parseInt(remoteNode.getPort()), remoteNode.getIp(),
				false);
		client.start();
		return client;
	}

	
	
}
