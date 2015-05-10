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
package org.tinygroup.webservice.test.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.Context;
import org.tinygroup.service.ServiceProviderInterface;

public class WebServiceTestUtil {

	public static void init() {
		// if (!init) {
		// AbstractTestUtil.init(null, true);
		// init = true;
		// ServiceProviderInterface provider = SpringUtil.getBean("service");
		// ServiceProcessor processor = new ServiceProcessorImpl();
		// processor.addServiceProvider(provider);
		// CEPCore core = SpringUtil.getBean(CEPCore.CEP_CORE_BEAN);
		// core.registerEventProcessor(processor);
		// }
	}

	public static void execute(String serviceId, Context context) {
		init();
		ServiceProviderInterface provider = BeanContainerFactory
				.getBeanContainer(WebServiceTestUtil.class.getClassLoader())
				.getBean("service");
		provider.execute(serviceId, context);
	}

}
