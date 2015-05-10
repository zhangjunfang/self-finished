package org.tinygroup.cepcoreremoteimpl.node.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;

import java.io.IOException;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.nettyremote.codec.serialization.HessianDecoder;
import org.tinygroup.nettyremote.codec.serialization.HessianEncoder;
import org.tinygroup.nettyremote.impl.ServerImpl;

public class NodeServerImpl extends ServerImpl {
	private CEPCore core;

	public CEPCore getCore() {
		return core;
	}

	public NodeServerImpl(int localPort, CEPCore core) {
		super(localPort);
		this.core = core;
	}

	protected void init(ServerBootstrap b) {
		b.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					public void initChannel(SocketChannel ch)
							throws IOException {
						ch.pipeline().addLast(
								new HessianDecoder(ClassResolvers
										.cacheDisabled(null)));
						ch.pipeline().addLast("MessageEncoder",
								new HessianEncoder());
						ch.pipeline().addLast(new NodeServerHandler(core));
					}
				});
	}
}
