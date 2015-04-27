/*
 * Copyright Bruce Liang (ldcsaa@gmail.com)
 *
 * Version	: JessMA 3.5.1
 * Author	: Bruce Liang
 * Website	: http://www.jessma.org
 * Project	: http://www.oschina.net/p/portal-basic
 * Blog		: http://www.cnblogs.com/ldcsaa
 * WeiBo	: http://weibo.com/u/1402935851
 * QQ Group	: 75375912
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jessma.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

/**
 * 
 * 日志记录器（用 slf4j 实现）
 *
 */
public class LogUtil
{
	/** JessMA 日志对象名称 */
	public static final String JESSMA_LOGGER_NAME	= "JessMA";
	/** JessMA 日志对象 */
	public static final Logger jessMALogger;
	
	static
	{
		jessMALogger = LoggerFactory.getLogger(JESSMA_LOGGER_NAME);
		
		if(jessMALogger == NOPLogger.NOP_LOGGER)
			System.err.println("!!! --> JessMA Logger is not valid, please check <-- !!!");
	}
	
	/** 获取 JessMA 日志记录器对象 */
	public static final Logger getJessMALogger()
	{
		return jessMALogger;
	}
	
	/** 获取日志记录器对象 */
	public static final Logger getLogger(Class<?> clazz)
	{
		return LoggerFactory.getLogger(clazz);
	}
	
	/** 获取日志记录器对象 */
	public static final Logger getLogger(String name)
	{
		return LoggerFactory.getLogger(name);
	}
	
	/** 记录操作异常日志 */
	public static final void exception(Throwable e, Object action, boolean printStack)
	{
		exception(jessMALogger, e, action, printStack);
	}
	
	/** 记录操作异常日志 */
	public static final void exception(Logger logger, Throwable e, Object action, boolean printStack)
	{
		StringBuilder msg = new StringBuilder("Execption occur on ");
		msg.append(action);
		msg.append(" -> ");
		msg.append(e.toString());
		String error = msg.toString();

		if(printStack)
			logger.error(error, e);
		else
			logger.error(error);
	}
	
	/** 记录操作失败日志 */
	public static final void fail(Object action, Object module, boolean printStack)
	{
		fail(jessMALogger, action, module, printStack);
	}
	
	/** 记录操作失败日志 */
	public static final void fail(Logger logger, Object action, Object module, boolean printStack)
	{
		StringBuilder msg = new StringBuilder("Operation fail on ");
		msg.append(action);
		msg.append(" -> ");
		msg.append(module);

		logger.error(msg.toString());
	}
	
	/** 记录服务器启动日志 */
	public static final void logServerStartup(Object o)
	{
		jessMALogger.info("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->");
		jessMALogger.info("starting: {}", o);
	}
	
	/** 记录服务器关闭日志 */
	public static final void logServerShutdown(Object o)
	{
		jessMALogger.info("stoping: {}", o);
		jessMALogger.info("<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-");
	}
}
