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
package org.tinygroup.tinydb.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.beanwrapper.BeanWrapperHolder;
import org.tinygroup.beanwrapper.BeanWrapperImpl;
import org.tinygroup.context.Context;
import org.tinygroup.database.util.DataSourceInfo;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.DbOperatorFactory;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;

/**
 * 工具方法
 * 
 * @author luoguo
 * 
 */
public final class TinyDBUtil {

	// private static BeanOperatorManager manager;
	//
	// static {
	// BeanOperatorManager manager =
	// BeanContainerFactory.getBeanContainer(loader).getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
	// }

	private TinyDBUtil() {
	}

	public static String getSeqName(String param) {
		return "seq_" + param;
	}

	public static <T extends Object> T[] listToArray(List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		T[] array = (T[]) Array
				.newInstance(list.get(0).getClass(), list.size());
		int i = 0;
		for (Object obj : list) {
			array[i++] = (T) obj;
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T[] collectionToArray(
			Collection<T> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}

		T[] array = (T[]) Array.newInstance(collection.iterator().next()
				.getClass(), collection.size());
		int i = 0;
		for (Object obj : collection) {
			array[i++] = (T) obj;
		}
		return array;
	}

	public static TableConfiguration getTableConfig(String tableName,
			String schema, ClassLoader loader) {
		DbOperatorFactory factory = BeanContainerFactory.getBeanContainer(
				loader).getBean(DbOperatorFactory.class);
		BeanOperatorManager manager = factory.getBeanOperatorManager();
		String beanType = manager.getBeanDbNameConverter()
				.dbTableNameToTypeName(tableName);
		return manager.getTableConfiguration(beanType, schema);
	}

	public static TableConfiguration getTableConfigByBean(String beanType,
			String schema, ClassLoader loader) {
		DbOperatorFactory factory = BeanContainerFactory.getBeanContainer(
				loader).getBean(DbOperatorFactory.class);
		BeanOperatorManager manager = factory.getBeanOperatorManager();
		return manager.getTableConfiguration(beanType, schema);
	}

	/**
	 * 获取表信息
	 * 
	 * 表schema
	 * 
	 * @param columnType
	 *            表名
	 * @return 表信息
	 */

	public static String getColumnJavaType(String columnType) {
		return null;
	}

	public static List<String> getBeanProperties(String beanType,
			String schema, ClassLoader loader) {
		DbOperatorFactory factory = BeanContainerFactory.getBeanContainer(
				loader).getBean(DbOperatorFactory.class);
		BeanOperatorManager manager = factory.getBeanOperatorManager();
		TableConfiguration tableConfig = manager.getTableConfiguration(
				beanType, schema);
		List<String> properties = new ArrayList<String>();
		if (tableConfig != null) {
			for (ColumnConfiguration c : tableConfig.getColumns()) {
				String columnName = c.getColumnName();
				String propertyName = manager.getBeanDbNameConverter()
						.dbFieldNameToPropertyName(columnName);
				properties.add(propertyName);
			}
		}
		return properties;
	}

	public static Bean getBeanInstance(String beanType, String schame) {
		return new Bean(beanType);
	}

	public static Bean context2Bean(Context c, String beanType, String schame,
			ClassLoader loader) {
		List<String> properties = getBeanProperties(beanType, schame, loader);
		return context2Bean(c, beanType, properties, schame);
	}

	public static Bean context2Bean(Context c, String beanType,
			ClassLoader loader) {
		String schema = DataSourceInfo.getDataSource();
		List<String> properties = getBeanProperties(beanType, schema, loader);
		return context2Bean(c, beanType, properties, schema);
	}

	public static Bean context2Bean(Context c, String beanType,
			List<String> properties, String schame) {
		Bean bean = getBeanInstance(beanType, schame);
		return context2Bean(c, bean, properties);
	}

	public static Bean context2Bean(Context c, Bean bean,
			List<String> properties) {
		for (String property : properties) {
			if (c.exist(property)) {
				bean.put(property, c.get(property));
			}
		}
		bean.put(Bean.SELECT_ITEMS_KEY, c.get(Bean.SELECT_ITEMS_KEY));
		bean.put(Bean.CONDITION_FIELD_KEY, c.get(Bean.CONDITION_FIELD_KEY));
		bean.put(Bean.CONDITION_MODE_KEY, c.get(Bean.CONDITION_MODE_KEY));
		bean.put(Bean.ORDER_BY_KEY, c.get(Bean.ORDER_BY_KEY));
		bean.put(Bean.SORT_DIRECTION_KEY, c.get(Bean.SORT_DIRECTION_KEY));
		bean.put(Bean.GROUP_BY_KEY, c.get(Bean.GROUP_BY_KEY));
		return bean;
	}

	public static void setProperties(Object object,
			Map<String, String> properties) {
		BeanWrapperHolder holder = new BeanWrapperHolder();
		BeanWrapperImpl wrapperImpl = holder.getBeanWrapper();
		wrapperImpl.setWrappedInstance(object);
		for (String attribute : properties.keySet()) {
			try {
				String value = properties.get(attribute);
				wrapperImpl.setPropertyValue(attribute, value);
			} catch (Exception e) {
				throw new RuntimeException("设置对象属性出现异常", e);
			}
		}
	}
}
