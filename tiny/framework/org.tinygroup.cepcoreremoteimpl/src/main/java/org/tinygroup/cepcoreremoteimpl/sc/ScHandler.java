package org.tinygroup.cepcoreremoteimpl.sc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.tinygroup.cepcoreremoteimpl.CEPCoreEventHandler;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ScHandler extends SimpleChannelInboundHandler<Event> {
	private static Logger logger = LoggerFactory.getLogger(ScHandler.class);
	private ScEventHandler scEventHandler;
	private CEPCoreServerImpl server;

	public ScHandler(CEPCoreServerImpl server) {
		scEventHandler = new ScEventHandler();
		this.server = server;
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Event event = (Event) msg;
		String serviceId = event.getServiceRequest().getServiceId();
		logger.logMessage(LogLevel.INFO, "接收到请求,id:{},type:", serviceId,
				event.getType());
		// Node向SC发起的注册请求
		if (CEPCoreEventHandler.NODE_RE_REG_TO_SC_REQUEST.equals(serviceId)) {
			scEventHandler.dealNodeRegToSc(event, ctx);
		} else if (CEPCoreEventHandler.NODE_REG_TO_SC_REQUEST.equals(serviceId)) {
			scEventHandler.dealNodeRegToSc(event, ctx);
		} else if (CEPCoreEventHandler.NODE_UNREG_TO_SC_REQUEST
				.equals(serviceId)) {// Node向SC发起的注销请求
			scEventHandler.dealNodeUnregToSc(event, ctx);
		} else {// 处理Node发来的远程请求
			scEventHandler.dealRemoteRequest(event, ctx);
		}
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (server.isStart()) {
			scEventHandler.removeNodeByCtx(ctx);
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.errorMessage("服务端收到异常",cause);
		ctx.fireExceptionCaught(cause);
	}

	protected void channelRead0(ChannelHandlerContext ctx, Event msg)
			throws Exception {

	}
}
