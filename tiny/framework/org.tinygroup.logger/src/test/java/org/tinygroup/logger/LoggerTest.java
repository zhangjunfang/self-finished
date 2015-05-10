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
package org.tinygroup.logger;

import junit.framework.TestCase;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.i18n.I18nMessageFactory;

import java.util.Locale;
import java.util.Properties;

import static org.tinygroup.logger.LogLevel.*;

public class LoggerTest extends TestCase {

	static Logger logger = LoggerFactory.getLogger(LoggerTest.class);

	protected void setUp() throws Exception {
		super.setUp();
		Properties properties = new Properties();
		properties.load(this.getClass().getResourceAsStream(
				"/i18n/info_zh_CN.properties"));
		I18nMessageFactory.addResource(Locale.SIMPLIFIED_CHINESE, properties);
	}

	public void testStartBusinessLog() {
		System.out.println("------------1中文----------");
		logger.startTransaction();
		System.out.println("------------2----------");
		logger.logMessage(INFO, "start");
		System.out.println("------------3----------");
		new HelloA().sayHello();
		System.out.println("------------4----------");
		new HelloB().sayHello();
		System.out.println("------------5----------");
		logger.logMessage(INFO, "end");
		System.out.println("------------6----------");
		logger.endTransaction();
		System.out.println("------------7----------");
	}

	public void testStartBusinessLogANotSupport() {
		System.out.println("------------1----------");
		logger.startTransaction();
		System.out.println("------------2----------");
		logger.logMessage(INFO, "start");
		System.out.println("------------3----------");
		HelloA helloA = new HelloA();
		HelloA.logger.setSupportTransaction(false);
		helloA.sayHello();
		System.out.println("------------4----------");
		new HelloB().sayHello();
		System.out.println("------------5----------");
		logger.logMessage(INFO, "end");
		System.out.println("------------6----------");
		logger.endTransaction();
		System.out.println("------------7----------");
	}

	public void testLog() {
		logger.log(DEBUG, "name");
		logger.log(ERROR, "name");
		logger.log(INFO, "name");
		logger.log(WARN, "name");
		logger.log(TRACE, "name");
	}

	public void testLogMessage() {
		if (logger.isEnabled(ERROR)) {
			logger.logMessage(ERROR, "n{0}a{1}m{2}e", "-", "&", "|");
		}
	}

	public void testLogMessageContext() {
		Context c = new ContextImpl();
		c.put("a", 1);
		c.put("b", 2);
		c.put("c", 3);
		logger.logMessage(ERROR, "n${a}a${b}m${c}e", c);
	}

	public void testIsEnabled() {
		assertEquals(false, logger.isEnabled(DEBUG));
		assertEquals(false, logger.isEnabled(TRACE));
		assertEquals(true, logger.isEnabled(ERROR));
		assertEquals(true, logger.isEnabled(INFO));
		assertEquals(true, logger.isEnabled(WARN));
	}
	
	public void testMdc(){
		logger.putToMDC("bizId", "testMDC");
		logger.logMessage(LogLevel.INFO, "测试是否输出mdc，打印的日志是否输出testMDC");
	}

	public void testThreadMdc(){
		LoggerFactory.putThreadVariable("key1", "value1");
		LoggerFactory.putThreadVariable("key2", "value2");
		Logger logger1=LoggerFactory.getLogger("name1");
		logger1.logMessage(LogLevel.INFO, "输出MDC,key1和key2");
		LoggerFactory.putThreadVariable("key3", "value3");
		logger1.logMessage(LogLevel.INFO, "输出MDC,key1、key2,key3");
		Thread thread=new Thread(new Runnable() {
			public void run() {
				LoggerFactory.putThreadVariable("key1", "name2-value1");
				Logger logger2=LoggerFactory.getLogger("name1");
				logger2.logMessage(LogLevel.INFO, "只输出MDC,key1");
			}
		});
		thread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	
	public void testLogMaxSize(){
		Logger logger1=LoggerFactory.getLogger("name1");
		logger1.startTransaction();
		logger1.setMaxBufferRecords(10);
		logger1.logMessage(LogLevel.INFO, "打印日志信息：{1}",1);
		System.out.println("日志未输出。。。");
		for (int i = 2; i <= 10; i++) {
			logger1.logMessage(LogLevel.INFO, "打印日志信息：{1}",i);
		}
		System.out.println("日志输出。。。");
		for (int i = 0; i < 5; i++) {
			logger1.logMessage(LogLevel.INFO, "再次打印日志信息：{1}",i+1);
		}
		logger1.endTransaction();
		System.out.println("日志再次输出。。。");
	}
	
	public void testThreadLogLevel(){
		Logger logger1=LoggerFactory.getLogger("name1");
		logger1.setMaxBufferRecords(10);
		LoggerFactory.setThreadLogLevel(LogLevel.INFO);
		logger1.logMessage(LogLevel.DEBUG, "打印DEBUG日志信息：{1}",1);
		logger1.logMessage(LogLevel.INFO, "打印INFO日志信息：{1}",2);
		System.out.println("由于线程日志输出级别是INFO,只打印INFO日志");
		Thread thread2=new Thread(new Runnable() {
			public void run() {
				Logger logger1=LoggerFactory.getLogger("name1");
				LoggerFactory.setThreadLogLevel(LogLevel.INFO);
				logger1.startTransaction();
				logger1.logMessage(LogLevel.DEBUG, "打印事务日志信息：{1}",1);
				for (int i = 1; i <= 10; i++) {
					logger1.logMessage(LogLevel.DEBUG, "打印DEBUG日志信息：{1}",i);
					logger1.logMessage(LogLevel.INFO, "打印INFO日志信息：{1}",i);
				}
				System.out.println("由于线程日志输出级别是INFO,只打印INFO日志");
				for (int i = 0; i < 5; i++) {
					logger1.logMessage(LogLevel.DEBUG, "再次打印DEBUG日志信息：{1}",i+1);
					logger1.logMessage(LogLevel.INFO, "再次打印INFO日志信息：{1}",i+1);
				}
				logger1.endTransaction();
				System.out.println("INFO日志再次输出。。。");
			}
		});
		thread2.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}
