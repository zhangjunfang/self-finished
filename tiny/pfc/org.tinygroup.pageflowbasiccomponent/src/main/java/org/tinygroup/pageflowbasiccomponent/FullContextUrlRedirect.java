/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.pageflowbasiccomponent;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.WebContext;

public class FullContextUrlRedirect implements ComponentInterface {
	private final static Logger logger = LoggerFactory
			.getLogger(FullContextUrlRedirect.class);
	private String path;
	private static final String PAGELET_EXT_FILE_NAME = ".pagelet";
	private static final String PAGE_EXT_FILE_NAME = ".page";
	private String templeteWithLayout = PAGE_EXT_FILE_NAME;
	private String template = PAGELET_EXT_FILE_NAME;
	private FullContextFileRepository fullContextFileRepository;
	private TemplateEngine engine;
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void init() {
		BeanContainer container = BeanContainerFactory.getBeanContainer(this
				.getClass().getClassLoader());
		fullContextFileRepository = (FullContextFileRepository) container
				.getBean("fullContextFileRepository");
		engine = new TemplateEngineDefault();
		// 初始化时候对xml文档的内容进行读取 //这个是重写超类方法
		templeteWithLayout = StringUtil.defaultIfBlank(templeteWithLayout,PAGE_EXT_FILE_NAME);
		template = StringUtil.defaultIfBlank(template,PAGELET_EXT_FILE_NAME);
		
	}

	public void doExecute(Context context) throws FileNotFoundException,
			IOException, Exception {

		boolean isPagelet = false;
		if (path.endsWith(template)) {
			isPagelet = true;
			path = path.substring(0,
					path.length() - template.length())
					+ templeteWithLayout;
		}
		FileObject fileObject = fullContextFileRepository.getFileObject(path);
		WebContext webContent = null;
		if (context instanceof WebContext)
			webContent = (WebContext) context;
		else
			return;
		if (fileObject != null && fileObject.isExist()) {
			
			TemplateContext templateContext = new TemplateContextDefault(context.getItemMap());
			if (isPagelet) {
				engine.renderTemplateWithOutLayout(path, templateContext, webContent.getResponse().getWriter());
			} else {
				engine.renderTemplate(path, templateContext, webContent.getResponse().getWriter());
			}
			return;
		} else {
			webContent.getResponse()
					.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	public void execute(Context context) {

		try {
			init();
			doExecute(context);
		} catch (Exception e) {
			logger.errorMessage("页面跳转出错", e);
		}
	}

}
