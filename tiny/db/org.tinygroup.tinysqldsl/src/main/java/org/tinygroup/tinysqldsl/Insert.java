/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinysqldsl;

import org.tinygroup.tinysqldsl.base.*;
import org.tinygroup.tinysqldsl.expression.JdbcParameter;
import org.tinygroup.tinysqldsl.expression.relational.ExpressionList;
import org.tinygroup.tinysqldsl.insert.InsertBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert语句
 * Created by luoguo on 2015/3/11.
 */
public class Insert extends StatementSqlBuilder implements Statement {

    private InsertBody insertBody;
    /**
     * SQL语句的标识，只是用于方便的识别是哪个SQL，没有其它意义，可以不设置
     */
    private String id;

    public String getId() {
        return id;
    }

    private Insert() {
        insertBody = new InsertBody();
    }

    public static Insert insertInto(Table table) {
        Insert insert = new Insert();
        insert.getInsertBody().setTable(table);
        return insert;
    }

    public Insert values(Value... values) {
        List<Column> columns = new ArrayList<Column>();
        ExpressionList itemsList = new ExpressionList();
        for (Value value : values) {
            columns.add(value.getColumn());
            itemsList.addExpression(new Condition(new JdbcParameter(), value
                    .getValue()));
        }
        insertBody.setColumns(columns);
        insertBody.setItemsList(itemsList);
        return this;
    }

    public InsertBody getInsertBody() {
        return insertBody;
    }

    @Override
    public String toString() {
        return sql();
    }

    @Override
    protected void parserStatementBody() {
        build(insertBody);
    }

    public void id(String id) {
        this.id = id;
    }
}
