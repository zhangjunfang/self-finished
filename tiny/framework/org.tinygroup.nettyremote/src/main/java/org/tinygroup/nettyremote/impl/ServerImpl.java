package org.tinygroup.nettyremote.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.nettyremote.Server;

public class ServerImpl implements Server {
	private Logger logger = LoggerFactory.getLogger(ServerImpl.class);
	private ServerThread serverThread = new ServerThread();
	private boolean start = false;
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private int localPort;

	public ServerImpl(int localPort) {
		this.localPort = localPort;

	}

	public void start() {
		logger.logMessage(LogLevel.INFO, "启动服务端线程,端口:{1}",
				localPort);
		setStart(false);
		serverThread.start();
	}

	protected void init(ServerBootstrap b) {
		b.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					public void initChannel(SocketChannel ch)
							throws IOException {
						ch.pipeline().addLast(
								new ObjectDecoder(ClassResolvers
										.cacheDisabled(null)));
						ch.pipeline().addLast("MessageEncoder",
								new ObjectEncoder());
						ch.pipeline().addLast(new ServerHandler());
					}
				});
	}

	private void bind() throws InterruptedException {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup);
		init(b);
		// 绑定端口，同步等待成功
		b.bind(localPort).sync();
	}

	public void stop() {
		logger.logMessage(LogLevel.INFO, "关闭服务端");
		setStart(false);
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	class ServerThread extends Thread {
		public void run() {
			if (!start) {
				setStart(true);
				try {
					bind();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.errorMessage("服务端启动失败",e);
				}
			}

		}
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

}
