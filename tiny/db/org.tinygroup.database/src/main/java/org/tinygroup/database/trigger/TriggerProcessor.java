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
package org.tinygroup.database.trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.tinygroup.database.config.trigger.Trigger;
import org.tinygroup.database.config.trigger.Triggers;

/**
 * 触发器处理
 * @author renhui
 *
 */
public interface TriggerProcessor {
	
	public void addTriggers(Triggers triggers);
	
	public void removeTriggers(Triggers triggers);
	
	public Trigger getTrigger(String triggerName);
	
	public String getCreateSql(String triggerName,String language);
	
	public String getDropSql(String triggerName,String language);
	
	public List<String> getCreateSql(String language);
	
	public List<String> getDropSql(String language);
	
	public List<Trigger> getTriggers(String language);
	
	public  boolean checkTriggerExist(String language,Trigger trigger,Connection connection)throws SQLException;
	
	
}
