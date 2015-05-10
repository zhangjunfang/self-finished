package org.tinygroup.tinyrunner;

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

public class Runner {
	
		// "/Application.preloadbeans.xml";
		private static boolean init = false;
		private static Application application;
		private static String DEFAULT_CONFIG = "application.xml";
		private static Logger logger = LoggerFactory
				.getLogger(Runner.class);
		private static final String TINY_JAR_PATTERN = "org\\.tinygroup\\.(.)*\\.jar";

		public static void init(String xmlFile, List<String> includePathPatterns) {
			if (init) {
				return;
			}
			initDirect(xmlFile,includePathPatterns);
		}
		/**
		 * 初始化
		 * 
		 * @param classPathResolve
		 *            是否对classPath进行处理
		 */
		public static void initDirect(String xmlFile,List<String> includePathPatterns) {
			
			// init(xmlFile, classPathResolve, null, null);
			String configXml = xmlFile;
			if (null == configXml || "".equals(configXml)) {
				configXml = DEFAULT_CONFIG;
			}
			InputStream inputStream = Runner.class.getClassLoader()
					.getResourceAsStream(configXml);
			if (inputStream == null) {
				inputStream = Runner.class.getResourceAsStream(configXml);
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
					initSpring(applicationConfig,includePathPatterns);

					ConfigurationUtil.getConfigurationManager()
							.distributeConfiguration();

					FileResolver fileResolver = BeanContainerFactory
							.getBeanContainer(
									Runner.class.getClassLoader())
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
					addIncludePathPatterns(fileResolver,includePathPatterns);
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
												Runner.class
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

		private static void initSpring(String applicationConfig,List<String> includePathPatterns) {
			BeanContainerFactory.setBeanContainer(SpringBeanContainer.class.getName());
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
			addIncludePathPatterns(fileResolver,includePathPatterns);
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

		private static void addIncludePathPatterns(FileResolver fileResolver,List<String> includePathPatterns){
			if(includePathPatterns==null){
				return;
			}
			for(String pattern:includePathPatterns){
				fileResolver.addIncludePathPattern(pattern);
			}
		}

		public static void stop(){
			if(init){
				application.stop();
			}
		}

}
