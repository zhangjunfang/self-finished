package org.tinygroup.tinysqldsl.extend;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.operator.SetOperationInstanceCallBack;
import org.tinygroup.tinysqldsl.select.ExceptOperation;
import org.tinygroup.tinysqldsl.select.Fetch;
import org.tinygroup.tinysqldsl.select.IntersectOperation;
import org.tinygroup.tinysqldsl.select.Offset;
import org.tinygroup.tinysqldsl.select.PlainSelect;
import org.tinygroup.tinysqldsl.select.SetOperation;
import org.tinygroup.tinysqldsl.select.UnionOperation;

/**
 * sqlserver复杂查询操作对象
 * 
 * @author renhui
 * 
 */
public class SqlServerComplexSelect extends ComplexSelect<SqlServerComplexSelect> {

	private SqlServerComplexSelect() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public static SqlServerComplexSelect union(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new UnionOperation();
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static SqlServerComplexSelect unionAll(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new UnionOperation(true);
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static SqlServerComplexSelect setOperation(
			SetOperationInstanceCallBack instance, Select... selects) {
		SqlServerComplexSelect complexSelect = new SqlServerComplexSelect();
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

	@SuppressWarnings("rawtypes")
	public static SqlServerComplexSelect except(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new ExceptOperation();
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static SqlServerComplexSelect intersect(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new IntersectOperation();
			}
		}, selects);
	}

	public SqlServerComplexSelect offset(Offset offset) {
		operationList.setOffset(offset);
		return this;
	}

	public SqlServerComplexSelect fetch(Fetch fetch) {
		operationList.setFetch(fetch);
		return this;
	}

}
