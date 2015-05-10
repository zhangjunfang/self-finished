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
package org.tinygroup.ini.impl;

import junit.framework.TestCase;
import org.tinygroup.ini.IniOperator;
import org.tinygroup.ini.Sections;
import org.tinygroup.ini.ValuePair;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-3-29.
 */
public class IniOperatorDefaultTestGetTMethod extends TestCase {
	IniOperator operator;

	public void setUp() throws Exception {
		super.setUp();
		operator = new IniOperatorDefault();
		Sections sections = new Sections();
		operator.setSections(sections);
	}

	public void testGetSections() throws Exception {
		assertNotNull(operator.getSections());
	}

	public void testInt() throws Exception {
		operator.put("aa", "abc", 456);
		assertEquals(true,456==operator.get(int.class, "aa", "abc", 1233));
	}
	
	public void testInteger() throws Exception {
		operator.put("aa", "abc", 456);
		assertEquals(Integer.valueOf(456),operator.get(Integer.class, "aa", "abc", 1233));
	}
	
	public void testBoolean() throws Exception {
		operator.put("aa", "abc", "true");
		assertEquals(true,Boolean.TRUE==operator.get(boolean.class, "aa", "abc", false));
	}
	
	public void testFloat() throws Exception {
		operator.put("aa","abc", 1.1f);
		System.out.println(operator.get(float.class, "aa", "abc", 1.2f));
		assertEquals(Float.valueOf(1.1f), operator.get(Float.class, "aa", "abc", 1.2f));
	}
	public void testfloat() throws Exception {
		operator.put("aa","abc", 1.1f);
		assertEquals(true,1.1f == operator.get(float.class, "aa", "abc", 1.2f));
	}

	public void testDouble() throws Exception {
		operator.put("aa","abc", 1.1d);
		assertEquals(Double.valueOf(1.1d), operator.get(Double.class, "aa", "abc", 1.2d));
	}
	
	public void testdouble() throws Exception {
		operator.put("aa","abc", 1.1d);
		assertEquals(true,1.1d == operator.get(double.class, "aa", "abc", 1.2d));
	}
}
