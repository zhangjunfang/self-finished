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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;
import org.tinygroup.tinysqldsl.util.DslUtil;

/**
 * FromItem列表
 * 
 * @author renhui
 */
public class FromItemList implements FromItem {
	/**
	 * from对象列表
	 */
	private List<FromItem> fromItems;

	public FromItemList() {
		super();
		fromItems = new ArrayList<FromItem>();
	}

	public FromItemList(List<FromItem> fromItems) {
		this.fromItems = fromItems;
	}

	public FromItemList(FromItem... fromItems) {
		this.fromItems = new ArrayList<FromItem>();
		Collections.addAll(this.fromItems, fromItems);
	}

	public List<FromItem> getFromItems() {
		return fromItems;
	}

	public void setFromItems(List<FromItem> fromItems) {
		this.fromItems = fromItems;
	}

	public Alias getAlias() {
		return new Alias();
	}

	public void setAlias(Alias alias) {

	}

	public String toString() {
		return DslUtil.getStringList(fromItems, true, false);
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(toString());
	}

}
