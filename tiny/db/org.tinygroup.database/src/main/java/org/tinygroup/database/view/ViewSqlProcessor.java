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

import org.tinygroup.database.config.view.View;

public interface ViewSqlProcessor {

	/**
	 * 获取构建视图的完整sql语句
	 * @param view 视图配置信息元数据
	 * @return
	 */
	String getCreateSql(View view);

	/**
	 * 获取删除视图的语句
	 * @param view 视图配置信息元数据
	 * @return
	 */
	String getDropSql(View view);

	/**
	 * 判断视图是否存在
	 * @param view 视图元数据
	 * @param conn 当前执行创建view的连接信息
	 * @return true表示视图存在，false反之
	 * @throws SQLException
	 */
	boolean checkViewExists(View view,Connection conn) throws SQLException;
}
