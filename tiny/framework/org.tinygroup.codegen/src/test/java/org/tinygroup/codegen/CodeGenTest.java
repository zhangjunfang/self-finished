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
package org.tinygroup.codegen;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.tinygroup.codegen.config.CodeGenMetaData;
import org.tinygroup.codegen.impl.CodeGeneratorDefault;
import org.tinygroup.codegen.util.CodeGenUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class CodeGenTest extends TestCase {
	String testJavaPath;
	String testResourcePath;
	protected void setUp() throws Exception {
		super.setUp();
//		AbstractTestUtil.init(null, true);
	}

	protected void tearDown() throws Exception {
		File javaFile=new File(testJavaPath+CodeGenUtil.packageToPath("org.tinygroup.codegen")+"HelloWorld.java");
		assertTrue(javaFile.exists());
		javaFile.deleteOnExit();
		File xmlFile=new File(testResourcePath+"test"+File.separator+"helloworld.xml");
		assertTrue(xmlFile.exists());
		xmlFile.deleteOnExit();
	}

	
	public void testCodeGen() throws Exception {
		
		XStream xStream=XStreamFactory.getXStream();
		xStream.setClassLoader(getClass().getClassLoader());
		xStream.autodetectAnnotations(true);
		xStream.processAnnotations(CodeGenMetaData.class);
		CodeGenMetaData metaData=(CodeGenMetaData) xStream.fromXML(getClass().getResourceAsStream("/test.codegen.xml"));
		Context context=ContextFactory.getContext();
		String projectPath=System.getProperty("user.dir");
		testJavaPath=projectPath+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator;
		testResourcePath=projectPath+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator;
		context.put(CodeGenerator.JAVA_ROOT, projectPath+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator);
		context.put(CodeGenerator.JAVA_RES_ROOT, projectPath+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator);
		context.put(CodeGenerator.JAVA_TEST_ROOT, testJavaPath);
		context.put(CodeGenerator.JAVA_TEST_RES_ROOT,testResourcePath );
		context.put(CodeGenerator.CODE_META_DATA, metaData);
		context.put("beanPackageName", "org.tinygroup.codegen");
		context.put("className", "HelloWorld");
		context.put(CodeGenerator.ABSOLUTE_PATH, testResourcePath);
		CodeGenerator generator=new CodeGeneratorDefault();
		generator.generate(context);
	}

}
