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
package org.tinygroup.cepcorenettysc.remote;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcorenettysc.EventClient;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.net.daemon.DaemonRunnable;

public class EventClientDaemonRunnable extends DaemonRunnable {
	static Logger logger = LoggerFactory.getLogger(EventClientDaemonRunnable.class);
	EventClient client;
	boolean reconnect = true;
	boolean flag = true;
	List<EventTrigger> preTriggers = new ArrayList<EventTrigger>();
	List<EventTrigger> postTriggers = new ArrayList<EventTrigger>();
	boolean triggered = false;
	int timeout = 1000 * 1;
	PreTriggerThread preTriggerThread = new PreTriggerThread();

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public EventClientDaemonRunnable(String hostName, int port,
			boolean reconnect) {
		this.reconnect = reconnect;
		client = new EventClient(hostName, port);
	}

	public EventClient getClient() {
		return client;
	}

	public void setClient(EventClient client) {
		this.client = client;
	}

	public void run() {
		if (preTriggers.size() > 0) {
//			(new PreTriggerThread()).start();
			preTriggerThread.setDaemon(true);
			preTriggerThread.start();
		}
		super.run();
	}

	protected void startAction() {
		if (flag) {
			flag = false;
			client.run();
			if (reconnect&&!isEnd()) {
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
				}
				flag = true;
				triggered = false;
				
//				if (preTriggerThread.getState()==Thread.State.NEW) {
//					logger.logMessage(LogLevel.INFO, "启动重注册触发器线程");
//					preTriggerThread.start();
//				}
			}

		} else {
			stop();
		}

	}

	public void addPreEventTrigger(EventTrigger trigger) {
		preTriggers.add(trigger);
		trigger.setEventClientDaemonRunnable(this);
	}

	public void addPostEventTrigger(EventTrigger trigger) {
		postTriggers.add(trigger);
		trigger.setEventClientDaemonRunnable(this);
	}

	protected void stopAction() {
		
		//flag主线程
		flag = false;
		client.stop();
		
		if(preTriggerThread!=null&&reconnect){
			//关闭触发器线程
			logger.logMessage(LogLevel.INFO, "关闭重注册触发器线程");
			triggered = true;
			
		}
		for (EventTrigger trigger : postTriggers) {
			trigger.execute();
		}
	}

	class PreTriggerThread extends Thread {
		public void run() {
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (client.isReady()&&!triggered) {
					for (EventTrigger trigger : preTriggers) {
						trigger.execute();
					}
					triggered = true;
				}

			}
		}
	}

}
