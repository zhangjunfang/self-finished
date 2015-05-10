package org.tinygroup.codegen;

import java.io.IOException;
import java.util.List;

import org.tinygroup.context.Context;

/**
 * 代码生成器
 * @author yancheng11334
 *
 */
public interface CodeGenerator {

	public static final String JAVA_ROOT="JAVA_ROOT";
	public static final String JAVA_TEST_ROOT="JAVA_TEST_ROOT";
	public static final String JAVA_RES_ROOT="JAVA_RES_ROOT";
	public static final String JAVA_TEST_RES_ROOT="JAVA_TEST_RES_ROOT";
	public static final String CODE_META_DATA="CODE_META_DATA";
	public static final String TEMPLATE_FILE="TEMPLATE_FILE";
	public static final String XSTEAM_PACKAGE_NAME="codegen";
	public static final String ABSOLUTE_PATH="ABSOLUTE_PATH";
	
	/**
	 * 代码生成处理方法
	 * @param context 上下文
	 * @throws IOException 
	 */
	public  List<String> generate(Context context) throws IOException;
}
