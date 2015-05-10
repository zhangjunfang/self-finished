package org.tinygroup.templateweb;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;

/**
 * TinyTempalte Servlet Created by luoguo on 2014/7/14.
 */
public class TinyTemplateServlet extends HttpServlet {

	private TemplateEngine engine;
	private static final Logger logger = LoggerFactory
			.getLogger(TinyTemplateServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		engine = BeanContainerFactory.getBeanContainer(
				TinyTemplateServlet.class.getClassLoader()).getBean(
				"templateEngine");
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			long startTime = System.currentTimeMillis();
			String servletPath = request.getServletPath();
			if (servletPath == null || servletPath.length() == 0) {
				servletPath = request.getPathInfo();
			}
			TemplateContext context = new RequestTemplateContext(request);
			engine.renderTemplate(servletPath, context, response.getWriter());
			long endTime = System.currentTimeMillis();
			logger.logMessage(LogLevel.INFO, "模板路径<{0}>的处理时间：{1}ms",
					servletPath, endTime - startTime);
		} catch (Exception e) {
			logger.errorMessage(e.getMessage(), e);
			throw new ServletException(e);
		}
	}
}
