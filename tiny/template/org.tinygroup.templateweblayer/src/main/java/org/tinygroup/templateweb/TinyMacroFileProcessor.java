package org.tinygroup.templateweb;

import java.util.List;
import java.util.Map;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.config.BundleDefine;
import org.tinygroup.bundle.loader.TinyClassLoader;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.template.I18nVisitor;
import org.tinygroup.template.ResourceLoader;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.TemplateFunction;
import org.tinygroup.template.loader.ClassLoaderResourceLoader;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tiny宏文件扫描
 *
 * @author renhui
 */
public class TinyMacroFileProcessor extends AbstractFileProcessor {

    private static final String COMPONENT_FILE_NAME = ".component";

    private static final String TINY_TEMPLATE_CONFIG = "/application/template-config";
    private static final String TINY_TEMPLATE_CONFIG_PATH = "/templateconfig.config.xml";
    //配置的三个参数名
    private static final String TEMPLATE_EXT_FILE_NAME = "templateExtFileName";
    private static final String LAYOUT_EXT_FILE_NAME = "layoutExtFileName";
    private static final String COMPONENT_EXT_FILE_NAME = "componentExtFileName";
    //配置参数的默认值
    private static final String TEMPLATE_EXT_DEFALUT = "page";
    private static final String LAYOUT_EXT_DEFALUT = "layout";
    private static final String COMPONENT_EXT_DEFALUT = "component";
    
    private static final String RESOURCE_CONFIG_NAME = "resource-loader";
    private static final String INIT_PARAM_NAME = "init-param";
    private static final String I18N_VISITOR_NAME = "i18n-visitor";
    private static final String TEMPLATE_FUNCTION_NAME = "template-function";

    private TemplateEngine engine;

    private BundleManager bundleManager;

    private static boolean hasResourceLoader = false;

    public TemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public BundleManager getBundleManager() {
        return bundleManager;
    }

    public void setBundleManager(BundleManager bundleManager) {
        this.bundleManager = bundleManager;
    }

	public boolean isMatch(FileObject fileObject) {
        if (!fileObject.isFolder() && fileObject.getFileName().endsWith(COMPONENT_FILE_NAME)) {
            return true;
        }
        return false;
    }

    public void process() {
        if (!hasResourceLoader) {
        	reloadTemplateConfig();
            hasResourceLoader = true;
        }
        for (FileObject fileObject : changeList) {
            logger.logMessage(LogLevel.INFO, "模板配置文件[{0}]开始加载",
                    fileObject.getAbsolutePath());
            try {
                engine.registerMacroLibrary(fileObject.getPath());
            } catch (TemplateException e) {
                logger.errorMessage("加载模板配置文件[{0}]出错", e,
                        fileObject.getAbsolutePath());
            }
            logger.logMessage(LogLevel.INFO, "模板配置文件[{0}]加载完毕",
                    fileObject.getAbsolutePath());
        }

    }
    
    /**
     * 设置模板引擎TemplateEngine
     */
    private void reloadTemplateConfig(){
    	//合并节点
        XmlNode totalConfig = ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);
        
       //设置模板引擎的参数
        configEngineProperties(totalConfig);
        
        //设置国际化访问接口
        configI18nVisitor(totalConfig);
        
        //加载资源加载器
        addResourceLoaders(totalConfig);
        
        //加载模板函数
        addFunction(totalConfig);
        
    }

    private void addResourceLoaders(XmlNode totalConfig) {
        String templateExtFileName = TEMPLATE_EXT_DEFALUT;
        String layoutExtFileName = LAYOUT_EXT_DEFALUT;
        String componentExtFileName = COMPONENT_EXT_DEFALUT;
        
        if (totalConfig != null) {
            templateExtFileName = StringUtil.defaultIfBlank(totalConfig.getAttribute(TEMPLATE_EXT_FILE_NAME), TEMPLATE_EXT_DEFALUT);
            layoutExtFileName = StringUtil.defaultIfBlank(totalConfig.getAttribute(LAYOUT_EXT_FILE_NAME), LAYOUT_EXT_DEFALUT);
            componentExtFileName = StringUtil.defaultIfBlank(totalConfig.getAttribute(COMPONENT_EXT_FILE_NAME), COMPONENT_EXT_DEFALUT);
        }
        //系统内置资源加载器
        Map<BundleDefine, TinyClassLoader> bundles = bundleManager
                .getBundleMap();
        for (TinyClassLoader classLoader : bundles.values()) {
            ClassLoaderResourceLoader classResourceLoader = new ClassLoaderResourceLoader(
                    templateExtFileName, layoutExtFileName,
                    componentExtFileName, classLoader);
            engine.addResourceLoader(classResourceLoader);
        }
        List<String> scanningPaths = fileResolver.getScanningPaths();
        for (String path : scanningPaths) {
            FileObjectResourceLoader fileResourceLoader = new FileObjectResourceLoader(
                    templateExtFileName, layoutExtFileName,
                    componentExtFileName, path);
            engine.addResourceLoader(fileResourceLoader);
        }
        //用户扩展的第三方资源加载器
        List<XmlNode> list = totalConfig.getSubNodes(RESOURCE_CONFIG_NAME);
        if(list!=null){
        	for(XmlNode node:list){
            	try {
    				ResourceLoader<?> loader = createResourceLoader(node);
    				engine.addResourceLoader(loader);
    			} catch (Exception e) {
    				logger.errorMessage("加载用户扩展的资源加载器出错", e);
    			}
            }
        }
        
    }
    
    private void addFunction(XmlNode totalConfig){
    	List<XmlNode> list = totalConfig.getSubNodes(TEMPLATE_FUNCTION_NAME);
    	if(list!=null){
    		for(XmlNode node:list){
        		try {
        			TemplateFunction function = createFunction(node);
    				engine.addTemplateFunction(function);
    			} catch (Exception e) {
    				logger.errorMessage("加载模板函数出错", e);
    			}
        	}
    	}
    	
    }
    
    private void configI18nVisitor(XmlNode totalConfig){
    	XmlNode node = totalConfig.getSubNode(I18N_VISITOR_NAME);
    	try {
    		I18nVisitor i18n = createI18nVisitor(node);
	    	engine.setI18nVisitor(i18n);
		} catch (Exception e) {
			logger.errorMessage("加载国际化资源访问器出错", e);
		}
    }
    
    private void configEngineProperties(XmlNode totalConfig){
    	List<XmlNode> list = totalConfig.getSubNodes(INIT_PARAM_NAME);
    	if(list!=null){
    		for(XmlNode node:list){
        		try{
        			String name = node.getAttribute("name");
        			String value = node.getAttribute("value");
        			
        			if("encode".equalsIgnoreCase(name)){
        			   engine.setEncode(StringUtil.defaultIfBlank(value, "UTF-8"));
        			}else if("cacheEnabled".equalsIgnoreCase(name)){
        				engine.setCacheEnabled(Boolean.parseBoolean(StringUtil.defaultIfBlank(value, "false")));
        			}else if("safeVariable".equalsIgnoreCase(name)){
        				engine.setSafeVariable(Boolean.parseBoolean(StringUtil.defaultIfBlank(value, "false")));
        			}else if("compactMode".equalsIgnoreCase(name)){
        				engine.setCompactMode(Boolean.parseBoolean(StringUtil.defaultIfBlank(value, "false")));
        			}else if("engineId".equalsIgnoreCase(name)){
        				if(!StringUtil.isEmpty(value)){ //指定模板引擎实例Id
        					engine.setEngineId(value);
        				}
        			}
        		}catch (Exception e) {
    				logger.errorMessage("设置模板引擎属性[{0}]出错,属性值[{1}]", e ,node.getAttribute("name"),node.getAttribute("value"));
    			}
        	}
    	}
    	
    }
    
    private ResourceLoader<?> createResourceLoader(XmlNode node) throws Exception{
    	if(node==null){
  		   return null;
  		}
    	String beanName = node.getAttribute("name");
    	logger.logMessage(LogLevel.INFO, "正在加载用户扩展的资源加载器[{0}]",beanName);
    	ClassLoader loader = this.getClass().getClassLoader();
    	return BeanContainerFactory.getBeanContainer(loader).getBean(beanName);
    }
    
    private I18nVisitor createI18nVisitor(XmlNode node) throws Exception{
    	if(node==null){
 		   return null;
 		}
    	String beanName = node.getAttribute("name");
    	logger.logMessage(LogLevel.INFO, "正在加载国际化资源访问器[{0}]",beanName);
    	ClassLoader loader = this.getClass().getClassLoader();
    	return BeanContainerFactory.getBeanContainer(loader).getBean(beanName);
    }
    
    private TemplateFunction createFunction(XmlNode node) throws Exception{
    	if(node==null){
 		   return null;
 		}
    	String beanName = node.getAttribute("name");
    	logger.logMessage(LogLevel.INFO, "正在加载模板函数[{0}]",beanName);
    	ClassLoader loader = this.getClass().getClassLoader();
    	return BeanContainerFactory.getBeanContainer(loader).getBean(beanName);
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
    }

    public String getComponentConfigPath() {
		return TINY_TEMPLATE_CONFIG_PATH;
	}
    
    public String getApplicationNodePath() {
        return TINY_TEMPLATE_CONFIG;
    }

}
