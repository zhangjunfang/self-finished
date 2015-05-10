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
package org.tinygroup.jsqlparser.expression;

import java.util.ArrayList;
import java.util.List;
import org.tinygroup.jsqlparser.schema.Column;

/**
 *
 * @author toben
 */
public class JsonExpression  implements Expression {

    private Column column;
    
    private List<String> idents = new ArrayList<String>();
    
    
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public List<String> getIdents() {
        return idents;
    }

    public void setIdents(List<String> idents) {
        this.idents = idents;
    }
    
    public void addIdent(String ident) {
        idents.add(ident);
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(column.toString());
        for (String ident : idents) {
            b.append("->").append(ident);
        }
        return b.toString();
    }
}
