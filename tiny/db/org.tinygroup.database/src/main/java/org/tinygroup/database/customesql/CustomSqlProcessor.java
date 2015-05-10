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
package org.tinygroup.database.customesql;

import java.util.List;

import org.tinygroup.database.config.SqlBody;
import org.tinygroup.database.config.customsql.CustomSqls;

public interface CustomSqlProcessor {

    /**
     * 通用的标准sql类型,适用于各种数据库，未定义{@link SqlBody#getDialectTypeName()}的自定义sql将默认为此类型
     */
    String STANDARD_SQL_TYPE = "STANDARD_SQL_TYPE";

    /**
     * 优先执行的sql
     */
    String BEFORE = "before";

    /**
     * 后续执行的sql
     */
    String AFTER = "after";

    List<String> getCustomSqls(String type,String language);

    void addCustomSqls(CustomSqls customsqls);

    void removeCustomSqls(CustomSqls customsqls);
}
