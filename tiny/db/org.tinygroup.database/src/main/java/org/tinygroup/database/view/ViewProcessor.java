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
package org.tinygroup.database.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.tinygroup.database.config.view.View;
import org.tinygroup.database.config.view.Views;

public interface ViewProcessor {

	void addViews(Views views);
	void removeViews(Views views);
	View getView(String name);
	View getViewById(String id);
	List<View> getViews();
	String getCreateSql(String name, String language);
	String getCreateSql(View view, String language);
	List<String> getCreateSql(String language);

	String getDropSql(String name, String language);
	String getDropSql(View view, String language);
	List<String> getDropSql(String language);

	/**
	 * 视图依赖初始化
	 */
	void dependencyInit();

	/**
     * 判断视图是否存在,根据不同的数据库类型调用对应的实现方法
     * @param view 视图元数据
     * @param conn 当前执行创建view的连接信息
     * @param language 当前数据类型
     * @return true表示视图存在，false反之
     * @throws SQLException
     */
	boolean checkViewExists(View view,Connection conn,String language) throws SQLException;
}
