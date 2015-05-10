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
package org.tinygroup.flow.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.Context;
import org.tinygroup.el.EL;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class FlowElUtil {
	private static Logger logger = LoggerFactory.getLogger(FlowElUtil.class);
	

	public static boolean executeCondition(String condition, Context context,ClassLoader loader) {
		EL el = BeanContainerFactory.getBeanContainer(loader).getBean(EL.EL_BEAN);
		return (Boolean) el.execute(condition, context);
	}

	public static Object execute(String expression, Context context,ClassLoader loader) {
		try {
			EL el = BeanContainerFactory.getBeanContainer(loader).getBean(EL.EL_BEAN);
			return el.execute(expression, context);
		} catch (Exception e) {
			logger.errorMessage("执行el表达式时出错", e,expression);
			return null;
		}
	}
}
