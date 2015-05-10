package com.sun.xml.ws.transport.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

public class WebServiceProcessor extends AbstractTinyProcessor {
	
	private TG_WSServlet servlet = new TG_WSServlet();

	public void reallyProcess(String urlString, WebContext context) throws ServletException, IOException {
		servlet.service(context.getRequest(), context.getResponse());
	}

	@Override
	protected void customInit() throws ServletException {
		
	}

}
