package org.tinygroup.cepcoreremoteimpl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.cepcoreremoteimpl.node.client.NodeClientImpl;
import org.tinygroup.event.central.Node;
import org.tinygroup.nettyremote.Exception.TinyRemoteConnectException;

public class ClientGroup {
//	private static Logger logger = LoggerFactory.getLogger(ClientGroup.class);
	private static Map<String, List<ClientInfo>> clients = new HashMap<String, List<ClientInfo>>();
	private static int MAX_CLIENT = 0;
	private static int MAX_REQUEST = 20;

	public static void unRegRemoteNode(Node remoteNode) {
		String nodeString = remoteNode.toString();
		unRegRemoteNode(nodeString);
	}
	
	private static void unRegRemoteNode(String nodeString){
		List<ClientInfo> infos = clients.get(nodeString);
		for (ClientInfo info : infos) {
			info.getClient().stop();
		}
	}
	
	public static void stop(){
		for(String nodeString:clients.keySet()){
			unRegRemoteNode(nodeString);
		}
	}

	public static void regRemoteNode(Node remoteNode, NodeClientImpl client) {
		String nodeString = remoteNode.toString();
		if (!clients.containsKey(nodeString)) {
			clients.put(nodeString, new ArrayList<ClientInfo>());
		}
		ClientInfo info = new ClientInfo(client);
		clients.get(nodeString).add(info);
		for (int i = 0; i < MAX_CLIENT; i++) {
			addNewClient(remoteNode);
		}
	}

	public static NodeClientImpl getClient(Node remoteNode) {
		String nodeString = remoteNode.toString();
		List<ClientInfo> list = clients.get(nodeString);
		if(list==null){
			NodeClientImpl client = RemoteCepCoreUtil.getClient(remoteNode);
			regRemoteNode(remoteNode, client);
			return client;
		}else{
			return choose(list, remoteNode);
		}
		
	}

	private static NodeClientImpl choose(List<ClientInfo> list,
			Node remoteNode) {
		ClientInfo info = list.get(0);
		if (list.size() == 1) {
			return whenOne(info, remoteNode);
		} else {
			return whenMoreThanOne(list, remoteNode);
		}
	}

	public static void back(NodeClientImpl client, Node remoteNode) {
		String nodeString = remoteNode.toString();
		List<ClientInfo> list = clients.get(nodeString);
		for (ClientInfo info : list) {
			if (client == info.getClient()) {
				info.back();
			}
		}
	}

	/**
	 * 选出所有连接中请求数最少的连接 如果最少连接大于最大连接的一半， 且连接数少于最大连接，则新建一个连接，并返回当前所选中的最小连接
	 * 
	 * @param list
	 * @param remoteNode
	 * @return
	 */
	private static NodeClientImpl whenMoreThanOne(List<ClientInfo> list,
			Node remoteNode) {
		int min = -1;
		ClientInfo target = null;
		for (ClientInfo info : list) {
			if (!info.getClient().isReady()) {
				continue;
			}
			if (min == -1 || min > info.getCounter()) {
				min = info.getCounter();
				target = info;
			}
			if (min == 0) {
				break;
			}
		}
		target.use();
		if (min > (MAX_REQUEST / 2) && list.size() < MAX_CLIENT) {
			addNewClient(remoteNode);
		}
		return target.getClient();
	}

	/**
	 * 当只有一个连接时 如果请求数小于一半，则用当前连接 如果请求数大于一半，则新建一个连接，继续当前连接
	 * 
	 * @param info
	 * @param remoteNode
	 */
	private static NodeClientImpl whenOne(ClientInfo info, Node remoteNode) {
		int HALF_MAX = MAX_REQUEST / 2;
		if (info.getCounter() < HALF_MAX) { // 如果只有一个
		} else if (info.getCounter() < MAX_REQUEST) {
			addNewClient(remoteNode);
		}
		info.use();
		if (!info.getClient().isReady()) {
			throw new TinyRemoteConnectException("连接" + remoteNode.getIp()
					+ ":" + remoteNode.getPort() + "尚未就绪，暂无法提供服务");
		}
		return info.getClient();
	}

	/**
	 * 创建一个新连接，加入队列
	 * 
	 * @param remoteNode
	 */
	private static void addNewClient(Node remoteNode) {
		ClientInfo info = new ClientInfo(RemoteCepCoreUtil.getClient(remoteNode));
		clients.get(remoteNode.toString()).add(info);
	}

}

class ClientInfo {
	private volatile Integer counter = 0;
	private NodeClientImpl client;

	public ClientInfo(NodeClientImpl client) {
		super();
		this.client = client;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public NodeClientImpl getClient() {
		return client;
	}

	public void setClient(NodeClientImpl client) {
		this.client = client;
	}

	public void use() {
		// synchronized (counter) {
		counter++;
		// }
	}

	public void back() {
		// synchronized (counter) {
		counter--;
		// }
	}

}