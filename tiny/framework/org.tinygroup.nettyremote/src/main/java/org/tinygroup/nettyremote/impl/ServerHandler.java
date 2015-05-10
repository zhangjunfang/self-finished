package org.tinygroup.nettyremote.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {


	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ctx.write(msg);

	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}


	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
	}
}