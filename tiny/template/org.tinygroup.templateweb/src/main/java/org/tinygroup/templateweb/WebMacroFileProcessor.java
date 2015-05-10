package org.tinygroup.templateweb;

import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.loader.ClassLoaderResourceLoader;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.vfs.FileObject;

public class WebMacroFileProcessor extends AbstractFileProcessor {

	private static final String COMPONENT_FILE_NAME = ".component";

	private static final String TINY_TEMPLATE_CONFIG = "template-config";

	private TemplateEngine engine;
	
	private static boolean hasResouceLoader=false; 
	
	public TemplateEngine getEngine() {
		return engine;
	}

	public void setEngine(TemplateEngine engine) {
		this.engine = engine;
	}

	public boolean isMatch(FileObject fileObject) {
		if (fileObject.getFileName().endsWith(COMPONENT_FILE_NAME)) {
			return true;
		}
		return false;
	}

	public void process() {
		if(!hasResouceLoader){
			addResourceLoaders();
			hasResouceLoader=true;
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "宏模板配置文件[{0}]开始加载",
					fileObject.getAbsolutePath());
			try {
				engine.registerMacroLibrary(fileObject.getPath());
			} catch (TemplateException e) {
				logger.errorMessage("加载宏模板配置文件[{0}]出错", e,
						fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "宏模板配置文件[{0}]加载完毕",
					fileObject.getAbsolutePath());
		}

	}

	private void addResourceLoaders() {
		String templateExtFileName = null;
		String layoutExtFileName = null;
		String componentExtFileName = null;
		if (applicationConfig != null) {
			templateExtFileName = applicationConfig
					.getAttribute("templateExtFileName");
			layoutExtFileName = applicationConfig
					.getAttribute("layoutExtFileName");
			componentExtFileName = applicationConfig
					.getAttribute("componentExtFileName");
		}
		if (StringUtil.isBlank(templateExtFileName)) {
			templateExtFileName = "vm";
		}
		if (StringUtil.isBlank(layoutExtFileName)) {
			layoutExtFileName = "layout";
		}
		if (StringUtil.isBlank(componentExtFileName)) {
			componentExtFileName = "component";
		}
		ClassLoaderResourceLoader classResourceLoader = new ClassLoaderResourceLoader(
				templateExtFileName, layoutExtFileName, componentExtFileName,
				getClass().getClassLoader());
		engine.addResourceLoader(classResourceLoader);
		List<String> scanningPaths = fileResolver.getScanningPaths();
		for (String path : scanningPaths) {
			FileObjectResourceLoader fileResourceLoader = new FileObjectResourceLoader(
					templateExtFileName, layoutExtFileName,
					componentExtFileName, path);
			engine.addResourceLoader(fileResourceLoader);
		}
	}
	
	public String getApplicationNodePath() {
		return TINY_TEMPLATE_CONFIG;
	}

}
