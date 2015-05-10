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
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.commons.order.OrderUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
//TODO 仔细想一下，程序的启动过程,文件搜索，SpringBean,配置之间的关系

/**
 * 默认的应用实现<br>
 * Tiny框架认为，应用都是一个Application，无论是控制台App还是Web App。<br>
 * 只不过这个应用的复杂程度不同而已。
 */
public class ApplicationDefault implements Application {

    private static Logger logger = LoggerFactory
            .getLogger(ApplicationDefault.class);
    private List<ApplicationProcessor> applicationProcessors = new ArrayList<ApplicationProcessor>();
    private ApplicationContext applicationContext;

    public void init() {
    	OrderUtil.order(applicationProcessors);
        for (ApplicationProcessor applicationProcessor : applicationProcessors) {
            applicationProcessor.init();
        }
    }

    public void start() {
        if (applicationProcessors != null) {
            // 对ApplicationProcessor列表进行排序
            for (ApplicationProcessor applicationProcessor : applicationProcessors) {
                logger.logMessage(LogLevel.INFO, "应用处理器{}正在启动...",
                        applicationProcessor.getClass());
                applicationProcessor.start();
                logger.logMessage(LogLevel.INFO, "应用处理器{}启动完毕。",
                        applicationProcessor.getClass());
            }
        }
    }

    public void stop() {
        for (int i = applicationProcessors.size() - 1; i >= 0; i--) {
            ApplicationProcessor processorLoader = applicationProcessors.get(i);
            logger.logMessage(LogLevel.INFO, "应用处理器{}正在停止...",
                    processorLoader.getClass());
            processorLoader.stop();
            logger.logMessage(LogLevel.INFO, "应用处理器{}停止完毕。",
                    processorLoader.getClass());
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void addApplicationProcessor(ApplicationProcessor applicationProcessor) {
        applicationProcessors.add(applicationProcessor);
        applicationProcessor.setApplication(this);
    }

    public List<ApplicationProcessor> getApplicationProcessors() {
        return applicationProcessors;
    }
}
