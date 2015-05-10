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
package org.tinygroup.database.view.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.tinygroup.database.config.view.View;

public class SqlserverViewSqlProcessorImpl extends ViewSqlProcessorImpl {

	public String getCreateSql(View view) {
	    SqlserverViewSqlCreator creator = new SqlserverViewSqlCreator(view);

		return creator.getCreateSql();
	}

    public boolean checkViewExists(View view, Connection conn) throws SQLException {
        boolean checkResult = false;
        String checkSql = buildCheckViewSql(view);

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(checkSql);

            checkResult = resultSet.next();
        } finally{
            if(null != resultSet){
                resultSet.close();
            }
            if(null != statement){
                statement.close();
            }
        }
        return checkResult;
    }

    /**
     * 获取sqlserver中检测视图是否存在的sql语句
     * @param view 视图构建元数据
     * @return 检测视图是否存在的sql语句
     */
    private String buildCheckViewSql(View view){
        StringBuffer checkSql = new StringBuffer();
        checkSql.append("SELECT * FROM SYSOBJECTS WHERE NAME= '");
        checkSql.append(view.getName()).append("'");

        return checkSql.toString();
    }
}
