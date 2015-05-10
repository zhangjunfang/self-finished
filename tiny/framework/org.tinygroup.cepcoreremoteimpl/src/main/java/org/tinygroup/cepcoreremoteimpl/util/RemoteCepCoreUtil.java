package org.tinygroup.cepcoreremoteimpl.util;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcoreremoteimpl.node.CEPCoreClientImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;

public class RemoteCepCoreUtil {
	
	private static List<String> scs = new ArrayList<String>();

	public static Event sendEvent(CEPCoreClientImpl client, Event event) {
		return client.sentEvent(event);
	}

	public static void regScAddress(int remotePort, String remoteHost) {
		scs.add(remoteHost + ":" + remotePort);
	}

	public static boolean checkSc(int remotePort, String remoteHost) {
		return scs.contains(remoteHost + ":" + remotePort);
	}

	public static CEPCoreClientImpl getClient(Node remoteNode) {
		CEPCoreClientImpl client = new CEPCoreClientImpl(
				Integer.parseInt(remoteNode.getPort()), remoteNode.getIp(),
				false);
		client.start();
		return client;
	}

	
}
