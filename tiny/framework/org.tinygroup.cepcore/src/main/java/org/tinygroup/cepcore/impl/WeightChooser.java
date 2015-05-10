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
package org.tinygroup.cepcore.impl;

import java.util.List;

import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.EventProcessorChoose;
import org.tinygroup.xmlparser.node.XmlNode;

public class WeightChooser implements EventProcessorChoose {

	public EventProcessor choose(List<EventProcessor> processors) {
		int totalWeight = 0;
		for (EventProcessor eventProcessor : processors) {
			int wight = eventProcessor.getWeight();
			if (wight == 0) {
				wight = DEFAULT_WEIGHT;
			}
			totalWeight +=  wight;
		}
		int random = (int) (Math.random() * totalWeight);
		for (EventProcessor eventProcessor : processors) {
			int wight = eventProcessor.getWeight();
			if (wight == 0) {
				wight = DEFAULT_WEIGHT;
			}
			random -= wight;
			if(random<=0){
				return eventProcessor;
			}
		}
		//不会到达
		return processors.get(0);
	}

	public void setParam(XmlNode param) {
		
	}

}
