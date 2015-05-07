package com.restbird.server.httpserver.netty.bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Modle {
	private static final Logger logger = LoggerFactory.getLogger(Modle.class);

	public static final String CONTENT_TYPE_HTML = "text/html";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_XML = "text/xml";

	private String charset = "utf-8";
	private ByteBuf contentByteBuf = Unpooled.EMPTY_BUFFER;
	private String contentType = CONTENT_TYPE_HTML;
	private HttpResponseStatus httpResponseStatus = HttpResponseStatus.OK;

	public String getCharset() {
		return charset;
	}

	public Modle setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public ByteBuf getContent() {
		return contentByteBuf;
	}

	public Modle setContent(String content) {
		if (content == null) {
			logger.info("returned modle's content is null");
			return this;
		}
		this.contentByteBuf = Unpooled.wrappedBuffer(content.getBytes(Charset.forName(charset)));
		return this;
	}

	public Modle setContent(byte[] content) {
		if (content == null) {
			logger.info("returned modle's content is null");
			return this;
		}
		this.contentByteBuf = Unpooled.wrappedBuffer(content);
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public Modle setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public HttpResponseStatus getHttpResponseStatus() {
		return httpResponseStatus;
	}

	public Modle setHttpResponseStatus(HttpResponseStatus httpResponseStatus) {
		this.httpResponseStatus = httpResponseStatus;
		return this;
	}
}
