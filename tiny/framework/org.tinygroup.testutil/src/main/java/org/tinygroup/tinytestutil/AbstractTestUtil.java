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
package org.tinygroup.tinytestutil;

import java.io.InputStream;
import java.util.List;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.application.impl.ApplicationDefault;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.ConfigurationFileProcessor;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.springutil.fileresolver.SpringBeansFileProcessor;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public abstract class AbstractTestUtil {
	// private static FullContextFileRepository repository;
	// private static VelocityHelperImpl helper;
	// private static final String DEFAULT_FILERESOLVER_BEAN_XML =
	// "/Application.preloadbeans.xml";
	private static boolean init = false;
	private static Application application;
	private static String DEFAULT_CONFIG = "application.xml";
	private static Logger logger = LoggerFactory
			.getLogger(AbstractTestUtil.class);
	private static final String TINY_JAR_PATTERN = "org\\.tinygroup\\.(.)*\\.jar";

	public static void init(String xmlFile, boolean classPathResolve) {
		if (init) {
			return;
		}
		initDirect(xmlFile, classPathResolve);
	}

	/**
	 * 初始化
	 * 
	 * @param classPathResolve
	 *            是否对classPath进行处理
	 */
	public static void initDirect(String xmlFile, boolean classPathResolve) {

		// init(xmlFile, classPathResolve, null, null);
		String configXml = xmlFile;
		if (null == configXml || "".equals(configXml)) {
			configXml = DEFAULT_CONFIG;
		}
		InputStream inputStream = AbstractTestUtil.class.getClassLoader()
				.getResourceAsStream(configXml);
		if (inputStream == null) {
			inputStream = AbstractTestUtil.class.getResourceAsStream(configXml);
		}
		String applicationConfig = "";
		if (inputStream != null) {
			try {
				applicationConfig = StreamUtil.readText(inputStream, "UTF-8",
						false);
				if (applicationConfig != null) {
					ConfigurationManager c = ConfigurationUtil
							.getConfigurationManager();
					XmlNode applicationXml = ConfigurationUtil
							.loadApplicationConfig(applicationConfig);
					c.setApplicationConfiguration(applicationXml);

				}
				application = new ApplicationDefault();
				initSpring(applicationConfig);

				ConfigurationUtil.getConfigurationManager()
						.distributeConfiguration();

				FileResolver fileResolver = BeanContainerFactory
						.getBeanContainer(
								AbstractTestUtil.class.getClassLoader())
						.getBean(FileResolver.BEAN_NAME);
				FileResolverUtil.addClassPathPattern(fileResolver);
				fileResolver.addResolvePath(FileResolverUtil
						.getClassPath(fileResolver));
				fileResolver.addResolvePath(FileResolverUtil.getWebClasses());
				try {
					fileResolver.addResolvePath(FileResolverUtil
							.getWebLibJars(fileResolver));
				} catch (Exception e) {
					logger.errorMessage("为文件扫描器添加webLibJars时出现异常", e);
				}
				fileResolver.addIncludePathPattern(TINY_JAR_PATTERN);
				XmlNode applicationXml = ConfigurationUtil
						.getConfigurationManager()
						.getApplicationConfiguration();
				if (applicationXml != null) {
					List<XmlNode> processorConfigs = applicationXml
							.getSubNodesRecursively("application-processor");
					if (processorConfigs != null) {
						for (XmlNode processorConfig : processorConfigs) {
							String processorBean = processorConfig
									.getAttribute("bean");
							ApplicationProcessor processor = BeanContainerFactory
									.getBeanContainer(
											AbstractTestUtil.class
													.getClassLoader()).getBean(
											processorBean);// TODO
							application.addApplicationProcessor(processor);
						}
					}
				}
			} catch (Exception e) {
				logger.errorMessage("载入应用配置信息时出现异常，错误原因：{}！", e, e.getMessage());
			}
		}

		application.init();
		application.start();
		init = true;
	}

	public static void stop() {
		if (init && application != null){
			application.stop();
		}
	}

	private static void initSpring(String applicationConfig) {
		BeanContainerFactory.setBeanContainer(SpringBeanContainer.class
				.getName());
		FileResolver fileResolver = new FileResolverImpl();

		FileResolverUtil.addClassPathPattern(fileResolver);
		fileResolver
				.addResolvePath(FileResolverUtil.getClassPath(fileResolver));
		fileResolver.addResolvePath(FileResolverUtil.getWebClasses());
		try {
			fileResolver.addResolvePath(FileResolverUtil
					.getWebLibJars(fileResolver));
		} catch (Exception e) {
			logger.errorMessage("为文件扫描器添加webLibJars时出现异常", e);
		}
		fileResolver.addIncludePathPattern(TINY_JAR_PATTERN);
		loadFileResolverConfig(fileResolver, applicationConfig);
		fileResolver.addFileProcessor(new SpringBeansFileProcessor());
		fileResolver.addFileProcessor(new ConfigurationFileProcessor());
		// SpringUtil.regSpringConfigXml(xmlFile);
		fileResolver.resolve();
	}

	private static void loadFileResolverConfig(FileResolver fileResolver,
			String applicationConfig) {

		XmlStringParser parser = new XmlStringParser();
		XmlNode root = parser.parse(applicationConfig).getRoot();
		PathFilter<XmlNode> filter = new PathFilter<XmlNode>(root);
		List<XmlNode> classPathList = filter
				.findNodeList("/application/file-resolver-configuration/class-paths/class-path");
		for (XmlNode classPath : classPathList) {
			fileResolver.addResolvePath(classPath.getAttribute("path"));
		}

		List<XmlNode> includePatternList = filter
				.findNodeList("/application/file-resolver-configuration/include-patterns/include-pattern");
		for (XmlNode includePatternNode : includePatternList) {
			fileResolver.addIncludePathPattern(includePatternNode
					.getAttribute("pattern"));
		}

	}

}
