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

import org.tinygroup.cepcorenettysc.remote.EventClientDaemonRunnable;
import org.tinygroup.event.Event;

public class NettyCepCoreUtil {
	// 由AR发向SC的服务的服务ID
	public static String AR_TO_SC_SERVICE_KEY = "ar_regto_sc_services";
	public static String AR_TO_SC = "ar_to_sc";

	// 由SC发向AR的服务的服务ID
	public static String SC_TO_AR = "sc_to_ar";
	public static String SC_TO_AR_SERVICE_KEY = "sc_regto_ar_services";

	// 存放请求传递中的节点列表
	public static String NODES_KEY = "nodes_key";
	// 存放请求传递中的单个节点
	public static String NODE_KEY = "node_key";
	public static String NODE_PATH = "node_path";
	// 请求类型KEY，值为后面两者
	public static String TYPE_KEY = "type_key";
	public static String REG_KEY = "type_reg";
	public static String UNREG_KEY = "type_unreg";

	public static Event sendEvent(EventClient eventClient, Event event
			) {
		int i = 1;
		while (!eventClient.isReady()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// 此处无须处理
			}
			i++;
			if (i > 10) {
				break;
			}
		}
		return eventClient.sendObject(event);
	}
}
