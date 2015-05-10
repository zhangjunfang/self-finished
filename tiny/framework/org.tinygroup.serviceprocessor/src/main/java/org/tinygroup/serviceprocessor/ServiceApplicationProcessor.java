package org.tinygroup.serviceprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.xmlparser.node.XmlNode;

public class ServiceApplicationProcessor implements ApplicationProcessor{
	private static Logger logger = LoggerFactory.getLogger(ServiceEventProcessorImpl.class);
	private ServiceEventProcessorImpl processor;
	private ServiceProviderInterface provider;
	private CEPCore cepcore;
	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return null;
	}

	public int getOrder() {
		return 0;
	}

	public void start() {
		logger.logMessage(LogLevel.DEBUG, "启动ServiceProcessor");
		cepcore.registerEventProcessor(processor);
		logger.logMessage(LogLevel.DEBUG, "启动ServiceProcessor完成");
		
	}

	public void init() {
		initProcessors();
	}
	
	private void initProcessors() {
		logger.logMessage(LogLevel.DEBUG, "初始化ServiceProcessor");
		processor = new ServiceEventProcessorImpl();
		processor.setServiceProvider(provider);
		logger.logMessage(LogLevel.DEBUG, "初始化ServiceProcessor完成");
	}
	

	public void stop() {
		logger.logMessage(LogLevel.DEBUG, "停止ServiceProcessor");
		cepcore.unregisterEventProcessor(processor);
		logger.logMessage(LogLevel.DEBUG, "停止ServiceProcessor完成");
	}

	public void setApplication(Application application) {
		
	}

	public ServiceProviderInterface getProvider() {
		return provider;
	}

	public void setProvider(ServiceProviderInterface provider) {
		this.provider = provider;
	}

	public CEPCore getCepcore() {
		return cepcore;
	}

	public void setCepcore(CEPCore cepcore) {
		this.cepcore = cepcore;
	}

}
