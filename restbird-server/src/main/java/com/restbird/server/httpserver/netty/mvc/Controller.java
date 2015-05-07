package com.restbird.server.httpserver.netty.mvc;

import com.restbird.server.httpserver.netty.Request;
import com.restbird.server.httpserver.netty.bean.Modle;

public interface Controller {
	Modle handleRequest(Request request) throws Exception;
}
