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


public class CoreTestCase {
	public static int ProcessorNum = 6;
	public static int ServiceNum = 1000;

	public static void main(String[] args) {
		core(new CoreInCore());
		core(new CoreInProcessor());
	}
	
	
	public static void core(Core core) {
		int j = 0;
		for (int i = 0; i < ProcessorNum; i++, j = j - 10 + ServiceNum) {
			Processor p = new Processor(ServiceNum, j, "v");
			core.addProcessor(p);
		}
		long begin = System.currentTimeMillis();
		for (int i = 0; i < j; i++) {
			core.get("v" + i);
		}
		long end = System.currentTimeMillis();
		System.out.print(core.getClass().getName()+" user:");
		System.out.println(end-begin);
		System.out.println(j);
	}
}
