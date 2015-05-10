package org.tinygroup.docgen.function;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.function.AbstractTemplateFunction;
import org.tinygroup.template.rumtime.U;

/**
 * 调用静态类的方法
 * @author yancheng11334
 *
 */
public class StaticClassFunction extends AbstractTemplateFunction {
    private Class<?> clazz;
	//静态类的配置名
	public StaticClassFunction(String names,String className) {
		super(names);
		try {
			clazz = this.getClass().getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Object execute(Template template, TemplateContext context,
			Object... parameters) throws TemplateException {

		if (parameters.length == 0 || !(parameters[0] instanceof String)) {
	        notSupported(parameters);
	    }
		
		String methodName = parameters[0].toString();
		try {
			Method method = clazz.getMethod(methodName, U.getParameterTypes(clazz, methodName));
			Object[] objects = Arrays.copyOfRange(parameters, 1, parameters.length);
			return method.invoke(null, objects);
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}
	

}
