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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.operator.SetOperationInstanceCallBack;
import org.tinygroup.tinysqldsl.select.OrderByElement;
import org.tinygroup.tinysqldsl.select.PlainSelect;
import org.tinygroup.tinysqldsl.select.SetOperation;
import org.tinygroup.tinysqldsl.select.SetOperationList;
import org.tinygroup.tinysqldsl.select.UnionOperation;

/**
 * 复杂查询
 * 
 * @author renhui
 */
public class ComplexSelect<T extends ComplexSelect<T>> extends
		StatementSqlBuilder implements Statement {

	protected SetOperationList operationList;
	private String id;

	public String getId() {
		return id;
	}

	protected ComplexSelect() {
		super();
		operationList = new SetOperationList();
	}

	@SuppressWarnings("rawtypes")
	public static ComplexSelect union(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new UnionOperation();
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static ComplexSelect unionAll(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new UnionOperation(true);
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static ComplexSelect setOperation(
			SetOperationInstanceCallBack instance, Select... selects) {
		ComplexSelect complexSelect = new ComplexSelect();
		List<PlainSelect> plainSelects = new ArrayList<PlainSelect>();
		List<SetOperation> operations = new ArrayList<SetOperation>();
		for (int i = 0; i < selects.length; i++) {
			Select select = selects[0];
			plainSelects.add(select.getPlainSelect());
			if (i != 0) {
				operations.add(instance.instanceOperation());
			}
		}
		complexSelect.operationList.setOpsAndSelects(plainSelects, operations);
		return complexSelect;
	}

	@SuppressWarnings("unchecked")
	public T orderBy(OrderByElement... orderByElements) {
		operationList.addOrderByElements(orderByElements);
		return (T) this;
	}

	public SetOperationList getOperationList() {
		return operationList;
	}

	@Override
	public String toString() {
		return sql();
	}

	@Override
	protected void parserStatementBody() {
		build(operationList);
	}

	public void id(String id) {
		this.id = id;
	}
}
