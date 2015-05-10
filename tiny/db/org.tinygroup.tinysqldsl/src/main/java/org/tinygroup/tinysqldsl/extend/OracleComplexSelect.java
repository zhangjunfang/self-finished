package org.tinygroup.tinysqldsl.extend;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.operator.SetOperationInstanceCallBack;
import org.tinygroup.tinysqldsl.select.IntersectOperation;
import org.tinygroup.tinysqldsl.select.MinusOperation;
import org.tinygroup.tinysqldsl.select.PlainSelect;
import org.tinygroup.tinysqldsl.select.SetOperation;
import org.tinygroup.tinysqldsl.select.UnionOperation;

/**
 * oracle复杂查询操作对象
 * 
 * @author renhui
 * 
 */
public class OracleComplexSelect extends ComplexSelect<OracleComplexSelect> {

	private OracleComplexSelect() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public static OracleComplexSelect union(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new UnionOperation();
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static OracleComplexSelect unionAll(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new UnionOperation(true);
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static OracleComplexSelect setOperation(
			SetOperationInstanceCallBack instance, Select... selects) {
		OracleComplexSelect complexSelect = new OracleComplexSelect();
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
	public static OracleComplexSelect minus(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new MinusOperation();
			}
		}, selects);
	}

	@SuppressWarnings("rawtypes")
	public static OracleComplexSelect intersect(Select... selects) {
		return setOperation(new SetOperationInstanceCallBack() {
			public SetOperation instanceOperation() {
				return new IntersectOperation();
			}
		}, selects);
	}

	public OracleComplexSelect page(int start, int limit) {
		StringBuilder pagingSelect = new StringBuilder();
		if (start == 0) {
			start = 1;
		}
		pagingSelect
				.append("select * from ( select row_.*, rownum db_rownum from ( ");
		pagingSelect.append(sql());
		pagingSelect.append(" ) row_ where rownum <=" + (start + limit - 1)
				+ ") where db_rownum >=" + start);
		this.stringBuilder = pagingSelect;
		return this;
	}
}
