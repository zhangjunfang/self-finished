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
package org.tinygroup.database.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.config.table.ForeignReference;
import org.tinygroup.database.config.table.Table;

public class TableSort implements Comparator<Table> {

	public int compare(Table table1, Table table2) {
		List<String> dependTables1 = getDependList(table1);
		List<String> dependTables2 = getDependList(table2);
		boolean isEmpty1 = CollectionUtil.isEmpty(dependTables1);
		boolean isEmpty2 = CollectionUtil.isEmpty(dependTables2);
		if (isEmpty1||isEmpty2) {
			boolean contains1 = dependTables1.contains(table2.getName());
			boolean contains2 = dependTables2.contains(table1.getName());
			if (contains1 && contains2) {
				throw new RuntimeException(String.format(
						"表1[name:%s]与表2[name:%s]相互依赖", table1.getName(),
						table2.getName()));
			} else if (contains1) {
				return 1;
			} else if (contains2) {
				return -1;
			}
		}
		return 0;
	}

	private List<String> getDependList(Table table) {
		List<ForeignReference> foreigns = table.getForeignReferences();
		List<String> dependencies = new ArrayList<String>();
		for (ForeignReference foreignReference : foreigns) {
			dependencies.add(foreignReference.getMainTable());
		}
		return dependencies;
	}
	
}


