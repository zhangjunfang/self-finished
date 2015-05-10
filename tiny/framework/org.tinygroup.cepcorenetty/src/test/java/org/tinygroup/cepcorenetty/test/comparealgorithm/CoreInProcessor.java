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
import java.util.List;

/**
 * 通过每次遍历Processor，判断是否其包含某个服务id来获取Processor
 * @author chenjiao
 *
 */
public class CoreInProcessor implements Core{
	private List<Processor> processors = new ArrayList<Processor>();

	public void addProcessor(Processor processor) {
		processors.add(processor);

	}

	public Processor getProcessor(String name) {
		List<Processor> l = new ArrayList<Processor>();
		for (Processor t : processors) {
			if (t.contain(name)) {
				l.add(t);
			}
		}
		return l.get(0);
	}

	public String get(String name) {
		Processor t = getProcessor(name);
		if (t != null) {
			return t.get(name);
		}
		return null;
	}
}
