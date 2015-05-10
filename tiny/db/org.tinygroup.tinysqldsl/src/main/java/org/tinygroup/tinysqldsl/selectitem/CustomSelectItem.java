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
package org.tinygroup.tinysqldsl.selectitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;

/**
 * 格式化的selectItem
 * 
 * @author renhui
 * 
 */
public class CustomSelectItem implements SelectItem {

	private SelectItem[] items;

	private String format;

	public CustomSelectItem(String format, SelectItem... items) {
		super();
		this.format = format;
		this.items = items;
	}

	public SelectItem[] getItems() {
		return items;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return segment();
	}

	private String segment() {
		Object[] args = new Object[items.length];
		for (int i = 0; i < args.length; i++) {
			args[i] = items[i];
		}
		return String.format(format, args);
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(segment());
	}

}
