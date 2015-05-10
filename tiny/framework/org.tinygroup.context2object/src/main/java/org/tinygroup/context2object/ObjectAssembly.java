package org.tinygroup.context2object;

import org.tinygroup.context.Context;

/**
 * 对象组装接口
 * @author renhui
 *
 */
public interface ObjectAssembly<T> {
	
	/**
	 * 匹配type类型的组装
	 * @param type
	 * @return
	 */
	public boolean isMatch(Class<?> type);

	/**
	 * 根据上下文参数信息进行对象组装
	 * @param varName
	 * @param object
	 * @param context
	 */
	public void assemble(String varName,T object,Context context);
	
}
