package org.tinygroup.cepcoreremoteimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class RemoteEventProcessorConatiner {
	private static Map<String, ProcessorInfo> map = new HashMap<String, ProcessorInfo>();
	private static Logger logger = LoggerFactory.getLogger(RemoteEventProcessorConatiner.class);
	public static void add(Node node, List<ServiceInfo> services, CEPCore core,
			int version) {
		String name = node.toString();
		if (map.containsKey(name)) {
			ProcessorInfo info = map.get(name);
			logger.logMessage(LogLevel.INFO, "新接收服务版本{},已有版本{}",version,info.getVersion());
			if(info.getVersion()==version){
				logger.logMessage(LogLevel.INFO, "服务版本相同,无需变更服务处理器");
				return;
			}
			logger.logMessage(LogLevel.INFO, "服务版本不同,进行服务处理器变更");
			//两者版本不同，则删除旧版本
			RemoteEventProcessor eventProcessor = map.get(name).getProcessor();
			core.unregisterEventProcessor(eventProcessor);
			eventProcessor.setServiceInfos(services);
			core.registerEventProcessor(eventProcessor);
			logger.logMessage(LogLevel.INFO, "服务版本不同,服务处理器变更完毕");
			
		}else{
			logger.logMessage(LogLevel.INFO, "无历史版本，新建远程EventProcessor");
			// 如果之前没有这个处理器，则创建并存储、注册
			RemoteEventProcessor processor = new RemoteEventProcessor(node,
					services);
			map.put(name, new ProcessorInfo(processor, version));
			core.registerEventProcessor(processor);
		}
		
	}

	public static void remove(String name, CEPCore core) {
		if (map.containsKey(name)) {
			RemoteEventProcessor processor = map.remove(name).getProcessor();
			core.unregisterEventProcessor(processor);
			processor.stopConnect();
		}

	}

	

	public static void stop() {
		for (ProcessorInfo processorInfo : map.values()) {
			RemoteEventProcessor processor = processorInfo.getProcessor();
			processor.stopConnect();
		}
	}
	
	
}
class ProcessorInfo {
	RemoteEventProcessor processor;
	int version;


	public RemoteEventProcessor getProcessor() {
		return processor;
	}

	public int getVersion() {
		return version;
	}

	public ProcessorInfo(RemoteEventProcessor processor, int version) {
		this.processor = processor;
		this.version = version;
	}


}