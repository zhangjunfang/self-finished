package org.tinygroup.cepcoreremoteimpl.node;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.tinygroup.cepcoreremoteimpl.exception.TinyRequestException;
import org.tinygroup.cepcoreremoteimpl.exception.TinyRequestHasTimeOutException;
import org.tinygroup.cepcoreremoteimpl.exception.TinyRequestTimeOutException;
import org.tinygroup.cepcoreremoteimpl.util.ParamUtil;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ResponseManager {
	private static Logger logger = LoggerFactory
			.getLogger(ResponseManager.class);
	private static final ConcurrentHashMap<String, BlockingQueue<Event>> responseMap = new ConcurrentHashMap<String, BlockingQueue<Event>>();

	public static void updateResponse(String eventId, Event event) {
		// 如果该eventId已不在列表之中，那么该event也许是之前已被超时清理了
		if (!responseMap.containsKey(eventId)) {
			String serviceId = event.getServiceRequest().getServiceId();
			TinyRequestHasTimeOutException e = new TinyRequestHasTimeOutException(
					eventId, serviceId);
			logger.errorMessage(e.getMessage());
			throw e;
		}
		BlockingQueue<Event> queue = responseMap.get(eventId);
		queue.add(event);
	}

	public static void putIfAbsent(String eventId) {
		responseMap.putIfAbsent(eventId, new LinkedBlockingQueue<Event>(1));
	}

	/**
	 * 获取请求结果集
	 * @param eventId
	 * @return
	 */
	public static Event getResponse(String eventId) {
		Event result;
		logger.logMessage(LogLevel.INFO, "发送请求{0}", eventId);
		try {
			result = responseMap.get(eventId).poll(
					ParamUtil.getRequestTimeOut(),
					TimeUnit.MILLISECONDS); // 超时返回null
		} catch (InterruptedException ex) {
			String info = "请求" + eventId + "发生异常";
			logger.errorMessage(info, ex);
			throw new TinyRequestException(info, ex);
		} finally {
			responseMap.remove(eventId);
		}
		if (result == null) { // 如果拿到结果集为空，则是对列在超时时间内未曾获取到结果，即认为请求超时
			TinyRequestTimeOutException e = new TinyRequestTimeOutException(
					"请求" + eventId + "超时");
			logger.errorMessage(e.getMessage(), e);
			throw e;
		}
		Throwable throwable = result.getThrowable();
		if (throwable != null) {// 如果有异常发生，则抛出异常
			if (throwable instanceof RuntimeException) {
				throw (RuntimeException) throwable;
			} else if (throwable instanceof Error) {
				throw (Error) throwable;
			} else {
				throw new RuntimeException(throwable);// TODO:此处的RuntimeException类型需要调整
			}
		}
		return result;
	}
}
