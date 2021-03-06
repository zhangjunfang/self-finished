package org.tinygroup.cepcoreremoteimpl.node;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreremoteimpl.util.ClientGroup;
import org.tinygroup.cepcoreremoteimpl.util.RemoteCepCoreUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.nettyremote.codec.serialization.HessianDecoder;
import org.tinygroup.nettyremote.codec.serialization.HessianEncoder;
import org.tinygroup.nettyremote.impl.ClientImpl;

public class CEPCoreClientImpl extends ClientImpl {
	Logger logger = LoggerFactory.getLogger(CEPCoreClientImpl.class);
	private Node node;
	private CEPCore core;
	private NodeHandler nodeHandler;

	/**
	 * 该函数仅可用于构造node to node
	 * 
	 * @param remotePort 目标客户端端口
	 * @param remoteHost 目标客户端IP
	 * @param reConnect 是否需要重连
	 */
	public CEPCoreClientImpl(int remotePort, String remoteHost,
			boolean reConnect) {
		super(remotePort, remoteHost, reConnect);
	}

	/**
	 * 向SC重新注册
	 */
	public void reReg() {
		nodeHandler.reRegToSc(this);
	}

	public void stop() {
		// 关闭和客户端的连接
		super.stop();
		//如果当前关闭的是向SC发起的连接，则关闭所有连接
		if (RemoteCepCoreUtil.checkSc(getRemotePort(), getRemoteHost())) {
			ClientGroup.stop();
		}
	}

	/**
	 * node to sc需用此函数
	 * 
	 * @param remotePort 服务端端口
	 * @param remoteHost 服务端IP
	 * @param node 当前节点信息
	 * @param core
	 */
	public CEPCoreClientImpl(int remotePort, String remoteHost, Node node,
			CEPCore core) {
		this(remotePort, remoteHost, true);
		this.node = node;
		this.core = core;
	}

	private void writeEvent(Event event) {
		ResponseManager.putIfAbsent(event.getEventId());
		write(event);
	}

	/**
	 * 向目标服务端发送Event
	 * @param event
	 * @return
	 */
	public Event sentEvent(Event event) {
		writeEvent(event);
		return ResponseManager.getResponse(event.getEventId());
	}

	protected void init(Bootstrap b) {
		b.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(
								new HessianDecoder(ClassResolvers
										.cacheDisabled(null)));
						ch.pipeline().addLast("MessageEncoder",
								new HessianEncoder());
						nodeHandler = new NodeHandler(CEPCoreClientImpl.this,
								node, core);
						ch.pipeline().addLast(nodeHandler);
					}
				});
	}
}
