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
package org.tinygroup.tinydb.spring;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.Field;
import org.tinygroup.tinydb.util.TinyDBUtil;

public class BeanRowMapper implements RowMapper {
	private String beanType;
	private String schema;
	private List<Field> fields;

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	public BeanRowMapper(String beanType, String schema, List<Field> fields) {
		this.beanType = beanType;
		this.schema = schema;
		this.fields = fields;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Bean bean = TinyDBUtil.getBeanInstance(beanType, schema);
		bean.setFields(fields);
		for (Field field : fields) {
			String name=field.getName();
			int type = field.getType();
			int scale = field.getScale();
			int index = field.getIndex();
			switch (type) {
			case Types.DECIMAL:
			case Types.NUMERIC:
			case Types.DOUBLE:
				if (scale != 0 && scale != -127) {// 认为是double类型数据
					double data = round(rs.getDouble(index), scale);
                    bean.put(name, data);
				}
				break;
			default:
				bean.put(name, rs.getObject(index));
			}
		}
		bean.clearMark();
		return bean;
	}

	/**
	 * 
	 * 对于有精度的数据采用四舍五入
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	private static double round(double v, int scale) {

		if (scale > 0) {

			BigDecimal b = new BigDecimal(Double.toString(v));

			BigDecimal one = new BigDecimal("1");

			return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

		}
		return v;

	}

}
