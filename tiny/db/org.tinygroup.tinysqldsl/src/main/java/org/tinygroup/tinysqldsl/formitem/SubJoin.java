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
package org.tinygroup.tinysqldsl.formitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;
import org.tinygroup.tinysqldsl.select.Join;

/**
 * 用来支持join操作
 */
public class SubJoin implements FromItem {

	private FromItem left;
	private Join join;
	private Alias alias;

	public SubJoin(FromItem left, Join join, Alias alias) {
		super();
		this.left = left;
		this.join = join;
		this.alias = alias;
	}

	public FromItem getLeft() {
		return left;
	}

	public void setLeft(FromItem l) {
		left = l;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join j) {
		join = j;
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public String toString() {
		return "(" + left + " " + join + ")"
				+ ((alias != null) ? alias.toString() : "");
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append("(");
		getLeft().builder(builder);
		builder.deparseJoin(getJoin());
		buffer.append(")");
	}
}
