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
package org.tinygroup.bundlejar.component;

import org.tinygroup.bundlejar.BundleTestObject;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

public class BundleComponent implements ComponentInterface{
	private BundleTestObject object;
	private int i;
	public void execute(Context context) {
		System.out.println(i);
		System.out.println(object);
		System.out.println(object.getName());
		System.out.println(object.getNum());
	}
	public BundleTestObject getObject() {
		return object;
	}
	public void setObject(BundleTestObject object) {
		this.object = object;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}

}
