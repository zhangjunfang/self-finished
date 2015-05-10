package com.restbird.server.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.restbird.server.httpserver.netty.ControllerMap;
import com.restbird.server.httpserver.netty.HttpServerHandler;

/**
 * start http server
 * 
 * @author ocean
 *
 */
public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	/** listening port **/
	private int port;
	/** controller mapping **/
	private ControllerMap controllerMap;

	

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ControllerMap getControllerMap() {
		return controllerMap;
	}

	public void setControllerMap(ControllerMap controllerMap) {
		this.controllerMap = controllerMap;
	}
	
	
	
	
	
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new HttpRequestDecoder());
					// pipeline.addLast(new HttpObjectAggregator(1024 * 1024 *
					// 64));//for FullHttpRequest
					pipeline.addLast(new HttpServerCodec());
					pipeline.addLast(new HttpServerHandler(controllerMap));
					pipeline.addLast(new HttpResponseEncoder());
				}
			}).option(ChannelOption.SO_BACKLOG, 1024);

			ChannelFuture f = b.bind(port).sync();
			logger.info("restbird server is running on port:{}", port);
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void start() {
		try {
			run();
		} catch (Exception e) {
			logger.error("restbird server start failed: ", e);
		}
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings({ "unused", "resource" })
			ApplicationContext context = new ClassPathXmlApplicationContext("main/server.xml");
			logger.info("restbird server started successfully");
		} catch (Exception e) {
			logger.error("restbird server start failed: ", e);
			System.exit(-1);
		}
	}
}
