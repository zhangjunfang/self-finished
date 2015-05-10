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
package org.tinygroup.webservicetest;

import java.io.Serializable;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ServiceABC implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2335830028613954176L;
	private static Logger logger = LoggerFactory.getLogger(ServiceABC.class);
	public boolean read(boolean a) {
		logger.logMessage(LogLevel.INFO, "read");
		return true;
	}
	
	public String write(int i, String s) {
		logger.logMessage(LogLevel.INFO, "write {} {}",i,s);
		return s+""+i;
	}

	public void write1(User abc) {
		logger.logMessage(LogLevel.INFO, "write1");
	}
}
