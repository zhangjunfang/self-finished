package org.tinygroup.beanconverter;

import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context2object.ObjectAssembly;
import org.tinygroup.database.util.DataSourceInfo;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.util.TinyDBUtil;

/**
 * bean对象的组装实现
 * 
 * @author renhui
 * 
 */
public class BeanAssembly implements ObjectAssembly<Bean> {

	private static final String SPLIT = ",";
	private static final String BEAN_TYPE_KEY = "@beantype";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BeanAssembly.class);

	public void assemble(String varName,Bean object, Context context) {
		String beanType = context.get(BEAN_TYPE_KEY);
		if (StringUtil.isBlank(beanType)) {
			LOGGER.errorMessage("未设置参数名称为@beantype的参数");
			throw new RuntimeException("未设置参数名称为@beantype的参数");
		}
		String[] types = beanType.split(SPLIT);
		String type=findType(varName,types);
		object.setType(type);
		String schema = DataSourceInfo.getDataSource();
		List<String> properties = TinyDBUtil.getBeanProperties(
				beanType, schema, this.getClass().getClassLoader());
		TinyDBUtil.context2Bean(context, object, properties);
	}

	private String findType(String varName,String[] types) {
		if(types.length==1){
			return types[0];
		}
        return varName;
	}

	public boolean isMatch(Class<?> type) {
		return Bean.class.equals(type);
	}
}
