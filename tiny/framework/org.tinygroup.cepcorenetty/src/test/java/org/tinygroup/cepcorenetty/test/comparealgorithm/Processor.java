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

public class Processor {
	private List<Item> list = new ArrayList<Item>();

	public void add(Item i){
		list.add(i);
	}
	
	public boolean contain(String name){
		for(Item i:list){
			if(i.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public List<Item> get(){
		return list;
	}
	
	public String get(String name){
		for(Item i:list){
			if(i.getName().equals(name)){
				return i.getValue();
			}
		}
		return null;
	}

	public Processor(int itemNum,int begin,String value){
		for(int i = 0; i < itemNum ; i ++,begin++){
			Item item = new Item();
			item.setName(value+begin);
			item.setValue(value+begin);
			this.add(item);
		}
	}
}
