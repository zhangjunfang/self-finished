package org.tinygroup.cepcoreremoteimpl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.event.central.Node;

public class CEPCoreEventHandler {
	// 由AR发向SC的服务的服务ID
	public static String NODE_REG_TO_SC_SERVICE_KEY = "node_regto_sc_services";
	public static String NODE_REG_TO_SC_SERVICE_VERSION_KEY = "node_regto_sc_version_services";
	
	public static String NODE_REG_TO_SC_REQUEST = "node_to_sc_request";
	public static String NODE_RE_REG_TO_SC_REQUEST = "node_re_to_sc_request";
	public static String NODE_UNREG_TO_SC_REQUEST = "node_to_sc_request";
	public static String SC_REG_NODE_TO_NODE_REQUEST = "sc_reg_node_to_node_request";
	public static String SC_UNREG_NODE_TO_NODE_REQUEST = "sc_unreg_node_to_node_request";
	// 由SC发向AR的服务的服务ID
	public static String SC_TO_NODE = "sc_to_node";
	public static String SC_TO_NODE_SERVICE_KEY = "sc_regto_node_services";
	public static String SC_TO_NODE_SERVICE_VERSIONS_KEY = "sc_regto_node_services_versions";
	// 存放请求传递中的节点列表
	public static String NODES_KEY = "nodes_key";
	// 存放请求传递中的单个节点
	public static String NODE_KEY = "node_key";
	public static String NODE_PATH = "node_path";
	// 请求类型KEY，值为后面两者
	public static String TYPE_KEY = "type_key";
	public static String REG_KEY = "type_reg";
	public static String UNREG_KEY = "type_unreg";

	
	private Node node;
	private CEPCore core;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public CEPCore getCore() {
		if (core == null) {
			core = BeanContainerFactory.getBeanContainer(
					this.getClass().getClassLoader()).getBean(
					CEPCore.CEP_CORE_BEAN);
		}
		return core;
	}

	public void setCore(CEPCore core) {
		this.core = core;
	}

}
