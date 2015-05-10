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
package org.tinygroup.tinysqldsl.expression;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Column;

public class JsonExpression implements Expression {

	private Column column;

	private List<String> idents = new ArrayList<String>();

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

	public void builder(StatementSqlBuilder builder) {
		StringBuilder b = builder.getStringBuilder();
		b.append(column.toString());
		for (String ident : idents) {
			b.append("->").append(ident);
		}
	}
}
