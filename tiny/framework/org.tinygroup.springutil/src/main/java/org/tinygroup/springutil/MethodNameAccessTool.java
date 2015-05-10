package org.tinygroup.springutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * 获取方法、构造函数的参数名称
 * @author renhui
 *
 */
public class MethodNameAccessTool {
	
	private static ParameterNameDiscoverer discoverer=new LocalVariableTableParameterNameDiscoverer();
	
	public static String[] getMethodParameterName(Method method){
		return discoverer.getParameterNames(method);
	}
	
	public static String[] getConstructorParameterName(Constructor constructor){
		return discoverer.getParameterNames(constructor);
	}

}
