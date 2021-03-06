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
package org.tinygroup.flow.exception;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.component.AbstractFlowComponent;

public class TestException  extends AbstractFlowComponent{
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	public void testException(){
		MyExceptionUtil.clear();
		Context c = new ContextImpl();
		flowExecutor.execute("myTestException", c);
		assertEquals(8, MyExceptionUtil.getValue());
	}
	
	public void testException2(){
		MyExceptionUtil.clear();
		Context c = new ContextImpl();
		flowExecutor.execute("myTestException2", c);
		assertEquals(4, MyExceptionUtil.getValue());
	}
}