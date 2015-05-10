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

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.cepcore.CEPCore;

public class NettyEventProcessorConatiner {
	private static Map<String, NettyEventProcessor> map = new HashMap<String, NettyEventProcessor>();
	private static Map<NettyEventProcessor, String> map2 = new HashMap<NettyEventProcessor, String>();

	public static void add(String name, NettyEventProcessor processor,
			CEPCore core) {
		if (map.containsKey(name)) {
			remove(name, core);
		}
		map.put(name, processor);
		map2.put(processor, name);
		core.registerEventProcessor(processor);
	}

	public static void remove(String name, CEPCore core) {
		if (map.containsKey(name)) {
			NettyEventProcessor processor = map.remove(name);
			map2.remove(processor);
			processor.stopConnect();
			core.unregisterEventProcessor(processor);
		}

	}
	
	public static void remove(NettyEventProcessor processor, CEPCore core) {
		if (map2.containsKey(processor)) {
			String s = map2.remove(processor);
			map.remove(s);
			core.unregisterEventProcessor(processor);
		}

	}
	
	public static void stop(){
		for(NettyEventProcessor processor:map.values()){
			processor.stopConnect();
		}
	}

}
