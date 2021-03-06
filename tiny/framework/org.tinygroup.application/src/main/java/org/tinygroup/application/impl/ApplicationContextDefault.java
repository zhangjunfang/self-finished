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
package org.tinygroup.application.impl;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationContext;
import org.tinygroup.context.impl.ContextImpl;

/**
 * 默认的应用环境实现类
 * Created by luoguo on 2014/5/5.
 */
public class ApplicationContextDefault extends ContextImpl implements ApplicationContext {
   
	private static final long serialVersionUID = -392641758600719141L;

	public ApplicationContextDefault() {

    }

    public ApplicationContextDefault(Application application) {
        this.application = application;
    }

    private Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
