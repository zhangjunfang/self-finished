package org.tinygroup.tinysqldsl;

import junit.framework.TestCase;

import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.operator.SetOperationInstanceCallBack;
import org.tinygroup.tinysqldsl.select.SetOperation;
import org.tinygroup.tinysqldsl.select.UnionOperation;

import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentCondition;
import static org.tinygroup.tinysqldsl.extend.OracleSelect.selectFrom;
import static org.tinygroup.tinysqldsl.extend.OracleComplexSelect.intersect;
import static org.tinygroup.tinysqldsl.extend.OracleComplexSelect.minus;
import static org.tinygroup.tinysqldsl.extend.OracleComplexSelect.setOperation;
import static org.tinygroup.tinysqldsl.extend.OracleComplexSelect.union;
import static org.tinygroup.tinysqldsl.extend.OracleComplexSelect.unionAll;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.ScoreTable.TSCORE;

public class JunitTestOracleSelect extends TestCase{

	public void testOracleSelect() {
		assertEquals(selectFrom(CUSTOM).page(0, 10).sql(), "select * from ( select row_.*, rownum db_rownum from ( SELECT * FROM custom ) row_ where rownum <=10) where db_rownum >=1");

		assertEquals(selectFrom(CUSTOM).into(new Table("test")).sql(), "SELECT * INTO test FROM custom");
		
		assertEquals(selectFrom(TSCORE).startWith(fragmentCondition("score.name=?", "aa"), fragmentCondition("score.custom_id=?", "bb"), false).sql(), "SELECT * FROM score START WITH score.name=? CONNECT BY score.custom_id=?");
	
		assertEquals(union(selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) UNION (SELECT * FROM custom)");
		
		assertEquals(unionAll(selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) UNION ALL (SELECT * FROM custom)");
		
		assertEquals(minus(selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) MINUS (SELECT * FROM custom)");
		
		assertEquals(intersect(selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) INTERSECT (SELECT * FROM custom)");
		
		
		assertEquals(setOperation(new SetOperationInstanceCallBack() {
			
			public SetOperation instanceOperation() {
				// TODO Auto-generated method stub
				return new UnionOperation();
			}
		},selectFrom(CUSTOM),selectFrom(TSCORE)).sql(), "(SELECT * FROM custom) UNION (SELECT * FROM custom)");
	}

}
