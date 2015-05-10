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
package org.tinygroup.cepcorenetty.test.comparealgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直接通过 服务和Processor的Map 根据服务id获取Processor
 * @author chenjiao
 *
 */
public class CoreInCore implements Core{
	private List<Processor> processors = new ArrayList<Processor>();
	private Map<String, List<Processor>> map = new HashMap<String, List<Processor>>();

	public void addProcessor(Processor processor) {
		processors.add(processor);
		List<Item> list = processor.get();
		for (Item i : list) {
			String name = i.getName();
			if (map.containsKey(name)) {
				map.get(name).add(processor);
			} else {
				List<Processor> pl = new ArrayList<Processor>();
				pl.add(processor);
				map.put(name, pl);
			}
		}
	}

	public Processor getProcessor(String name) {
		if (map.containsKey(name))
			return map.get(name).get(0);
		return null;
	}
	
	public String get(String name){
		Processor t = getProcessor(name);
		if(t!=null){
			return t.get(name);
		}
		return null;
	}
}
