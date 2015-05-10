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

import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.database.config.view.View;
import org.tinygroup.database.config.view.ViewField;


public class SqlserverViewSqlCreator extends ViewSqlCreator{

    public SqlserverViewSqlCreator(View view) {
        super(view);
    }

    @Override
    protected void appendHead(String viewName, StringBuffer buffer) {
        buffer.append("CREATE VIEW ").append(view.getName()).append(" AS ");
    }

    @Override
    protected void appendFields(StringBuffer buffer) {
        buffer.append(" SELECT TOP 100 PERCENT ");
        List<ViewField> fields = view.getFieldList();
        for (int i = 0; i < fields.size(); i++) {
            ViewField field = fields.get(i);
            String fieldAlias = field.getAlias();
            String tableFieldName = fieldNames.get(field.getId());
            String fieldName = "";
            if (StringUtil.isBlank(fieldAlias)) {
                fieldName = tableFieldName;
            } else {
                fieldName = tableFieldName + " AS " + fieldAlias;
            }
            buffer.append(fieldName);
            if (i < fields.size() - 1) {
                buffer.append(",");
            }
        }
    }
}
