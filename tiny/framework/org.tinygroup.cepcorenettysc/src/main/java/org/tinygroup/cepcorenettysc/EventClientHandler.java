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
package org.tinygroup.cepcorenettysc;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.tinygroup.event.Event;
import org.tinygroup.net.ClientHandler;

public class EventClientHandler extends ClientHandler {

	
	protected void processObject(Object message, ChannelHandlerContext ctx) {
		Event event = (Event) message;
		// 更新事件
		String eventId = event.getEventId();
		EventClient eventClient = (EventClient) client;
		Event oldEvent = eventClient.getEvent(eventId);
		if (oldEvent != null) {// 如果事件已经不再存在，则直接忽略
			String oldEventId = oldEvent.getEventId();
			synchronized (oldEventId) {
				eventClient.updateEvent(oldEventId, event);
				// 提醒事件完成
				oldEventId.notify();
			}
		}
	}

}
