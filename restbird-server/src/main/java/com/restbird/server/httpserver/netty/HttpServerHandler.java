package com.restbird.server.httpserver.netty;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.restbird.server.httpserver.netty.bean.Modle;
import com.restbird.server.httpserver.netty.mvc.Controller;

/**
 * HttpServer Handler, refer to http://kanpiaoxue.iteye.com/blog/2163332
 * 
 * @author littleBirdTao
 *
 */
// @Sharable //note that this Handler is not sharable
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	private HttpRequest request;
	/** Get request flag **/
	private boolean requestGetFl;
	/** Post request flag **/
	private boolean requestPostFl;
	private String uriPath;

	private ControllerMap controllerMap;

	public HttpServerHandler(ControllerMap controllerMap) {
		this.controllerMap = controllerMap;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;
			if (HttpHeaders.is100ContinueExpected(request)) {
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
				ctx.writeAndFlush(response);
			}
			String uriStr = request.getUri();
			uriPath = new URI(uriStr).getPath();
			HttpMethod method = request.getMethod();

			requestGetFl = method.equals(HttpMethod.GET);
			requestPostFl = method.equals(HttpMethod.POST);
			// if Get request
			if (requestGetFl) {
				QueryStringDecoder decoderQuery = new QueryStringDecoder(uriStr);
				handleController(ctx, decoderQuery);
				return;
			}
		}
		// if Post request
		if (requestPostFl) {
			if (msg instanceof HttpContent) {
				HttpContent content = (HttpContent) msg;
				ByteBuf buf = content.content();
				String contentString = buf.toString(Charsets.UTF_8);
				buf.release();
				QueryStringDecoder decoderQuery = new QueryStringDecoder("some?" + contentString);
				handleController(ctx, decoderQuery);
			}
		}
	}

	private void handleController(ChannelHandlerContext ctx, QueryStringDecoder decoderQuery) {
		Request request = new Request();
		request.setDecoderQuery(decoderQuery);
		Controller controller = controllerMap.getControllerMap().get(uriPath);
		Modle modle = null;
		if (controller == null) {
			modle = new Modle();
			modle.setHttpResponseStatus(HttpResponseStatus.NOT_FOUND);
			modle.setContent("404 Controller Not Found");
		} else {
			try {
				modle = controller.handleRequest(request);
			} catch (Exception e) {
				logger.error("excep: ", e);
				modle.setHttpResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
				modle.setContent("500 Internal Server Error");
			}
		}
		writeHttpResponse(ctx, modle);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("excep: ", cause);
		ctx.close();
	}

	private void writeHttpResponse(ChannelHandlerContext ctx, Modle modle) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, modle.getHttpResponseStatus(), modle.getContent());
		response.headers().set(Names.CONTENT_TYPE, modle.getContentType());
		response.headers().set(Names.CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(Names.EXPIRES, 0);
		if (HttpHeaders.isKeepAlive(request)) {
			response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
			ctx.writeAndFlush(response);
		} else {
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}
	}

	public ControllerMap getControllerMap() {
		return controllerMap;
	}

	public void setControllerMap(ControllerMap controllerMap) {
		this.controllerMap = controllerMap;
	}

}