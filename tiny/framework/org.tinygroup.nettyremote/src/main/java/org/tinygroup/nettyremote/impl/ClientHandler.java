package org.tinygroup.nettyremote.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

	}

}
