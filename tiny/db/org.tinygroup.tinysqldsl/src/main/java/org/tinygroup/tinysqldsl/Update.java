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
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.JdbcParameter;
import org.tinygroup.tinysqldsl.update.UpdateBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UPDATE语句
 * Created by luoguo on 2015/3/11.
 */
public class Update extends StatementSqlBuilder implements Statement {

    private UpdateBody updateBody;
    private String id;

    public String getId() {
        return id;
    }

    private Update() {
        updateBody = new UpdateBody();
    }

    public static Update update(Table table) {
        Update update = new Update();
        update.getUpdateBody().setTables(Collections.singletonList(table));
        return update;
    }

    public Update set(Value... values) {
        List<Column> columns = new ArrayList<Column>();
        List<Expression> expressions = new ArrayList<Expression>();
        for (Value value : values) {
            columns.add(value.getColumn());
            expressions
                    .add(new Condition(new JdbcParameter(), value.getValue()));
        }
        updateBody.setColumns(columns);
        updateBody.setExpressions(expressions);
        return this;
    }

    public Update where(Condition condition) {
        updateBody.setWhere(condition);
        return this;
    }

    public UpdateBody getUpdateBody() {
        return updateBody;
    }

    @Override
    protected void parserStatementBody() {
        build(updateBody);
    }

    @Override
    public String toString() {
        return sql();
    }

    public void id(String id) {
        this.id = id;
    }
}
