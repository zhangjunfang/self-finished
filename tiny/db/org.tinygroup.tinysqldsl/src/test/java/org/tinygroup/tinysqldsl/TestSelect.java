package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.ComplexSelect.union;
import static org.tinygroup.tinysqldsl.ComplexSelect.unionAll;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.ScoreTable.TSCORE;
import static org.tinygroup.tinysqldsl.Select.customSelectItem;
import static org.tinygroup.tinysqldsl.Select.select;
import static org.tinygroup.tinysqldsl.Select.selectFrom;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.or;
import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentCondition;
import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentFrom;
import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentSelect;
import static org.tinygroup.tinysqldsl.formitem.SubSelect.subSelect;
import static org.tinygroup.tinysqldsl.select.Join.fullJoin;
import static org.tinygroup.tinysqldsl.select.Join.leftJoin;
import static org.tinygroup.tinysqldsl.select.Join.rightJoin;
import static org.tinygroup.tinysqldsl.select.OrderByElement.asc;
import static org.tinygroup.tinysqldsl.select.OrderByElement.desc;
import static org.tinygroup.tinysqldsl.selectitem.Top.top;

import org.tinygroup.tinysqldsl.expression.FragmentExpressionSql;
import org.tinygroup.tinysqldsl.formitem.SubSelect;
import org.tinygroup.tinysqldsl.selectitem.Top;

/**
 * Created by luoguo on 2015/3/11.
 */
public class TestSelect {
	public static void main(String[] args) {
		System.out.println(selectFrom(CUSTOM));

		System.out.println(select(
				customSelectItem("%s-%s", CUSTOM.NAME, CUSTOM.AGE))
				.from(CUSTOM));

		System.out.println(select(
				customSelectItem("upper(%s)-%s", CUSTOM.NAME, CUSTOM.AGE))
				.from(CUSTOM));

		System.out.println(selectFrom(CUSTOM).orderBy(desc(CUSTOM.NAME)));

		System.out.println(selectFrom(CUSTOM).orderBy(asc(CUSTOM.ID)));

		System.out.println(selectFrom(CUSTOM).where(CUSTOM.NAME.eq("abc")));

		System.out.println(selectFrom(CUSTOM).where(CUSTOM.NAME.like("abc")));

		System.out.println(selectFrom(CUSTOM).where(
				CUSTOM.NAME.rightLike("abc")));

		System.out.println(selectFrom(CUSTOM).where(CUSTOM.NAME.isNull()));

		System.out.println(selectFrom(CUSTOM).where(CUSTOM.NAME.isNotNull()));

		System.out.println(selectFrom(CUSTOM).where(
				or(CUSTOM.NAME.like("abc"), CUSTOM.AGE.gt(20))));

		System.out.println(selectFrom(CUSTOM).where(
				and(CUSTOM.NAME.like("abc"), CUSTOM.AGE.gt(20))));

		System.out.println(selectFrom(CUSTOM)
				.where(CUSTOM.NAME.leftLike("abc")));

		System.out
				.println(selectFrom(CUSTOM).where(CUSTOM.AGE.between(23, 25)));

		System.out.println(select(CUSTOM.AGE.max()).from(CUSTOM).groupBy(
				CUSTOM.NAME, CUSTOM.AGE));
		System.out.println(select(CUSTOM.AGE.min()).from(CUSTOM));
		System.out.println(select(CUSTOM.AGE.avg()).from(CUSTOM));
		System.out.println(select(CUSTOM.AGE.count()).from(CUSTOM));
		System.out.println(select(CUSTOM.AGE.sum()).from(CUSTOM));
		System.out.println(select(CUSTOM.NAME.distinct()).from(CUSTOM)
				.forUpdate());

		System.out.println(select(CUSTOM.NAME, CUSTOM.AGE, TSCORE.SCORE)
				.from(CUSTOM)
				.join(leftJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME))).sql());

		System.out.println(select(fragmentSelect("custom.name,custom.age"))
				.from(fragmentFrom("custom custom")).where(
						fragmentCondition("custom.name=?", "悠悠然然")));

		System.out.println(select(CUSTOM.NAME).from(
				subSelect(selectFrom(CUSTOM), "custom", true)).where(
				CUSTOM.ID.eq("2324")));
		System.out.println(select(CUSTOM.ID.count()).from(CUSTOM).groupBy(
				CUSTOM.AGE));
		System.out.println(select(CUSTOM.ID.count()).from(CUSTOM).groupBy(
				CUSTOM.NAME, CUSTOM.AGE));
		System.out.println(union(selectFrom(CUSTOM), selectFrom(CUSTOM)));

		System.out.println(unionAll(selectFrom(CUSTOM), selectFrom(CUSTOM)));

		System.out.println(select(CUSTOM.AGE, CUSTOM.NAME).from(CUSTOM).join(
				rightJoin(TSCORE, TSCORE.NAME.eq(CUSTOM.NAME))));

		System.out.println(select(CUSTOM.AGE, CUSTOM.NAME).from(CUSTOM).join(
				fullJoin(TSCORE, TSCORE.NAME.eq(CUSTOM.NAME))));

		System.out.println(select(TSCORE.SCORE.sum()).from(TSCORE)
				.groupBy(TSCORE.NAME).having(TSCORE.SCORE.sum().gt(300)));

		System.out.println(select(top(10), CUSTOM.NAME).from(CUSTOM));

		System.out.println(select(new Top(10, false, true, true), CUSTOM.NAME)
				.from(CUSTOM));

		System.out.println(selectFrom(CUSTOM).where(CUSTOM.AGE.gt(30))
				.forUpdate());

		System.out.println(select(TSCORE.SCORE.avg(), TSCORE.NAME).from(TSCORE)
				.groupBy(TSCORE.NAME));

		System.out.println(select(TSCORE.SCORE.sum(), TSCORE.NAME).from(TSCORE)
				.having(TSCORE.SCORE.sum().gt(200))
				.orderBy(asc(TSCORE.SCORE.sum())));

		System.out.println(select(fragmentSelect("sum(score.score) s"),
				TSCORE.NAME).from(TSCORE)
				.having(fragmentCondition("s>?", "200"))
				.orderBy(asc(fragmentCondition("s"))));

		System.out
				.println(select(fragmentSelect("r"), CUSTOM.NAME, CUSTOM.AGE)
						.from(fragmentFrom("(select ROWNUM r,custom.name,custom.age from custom where r>=1)"))
						.where(fragmentCondition("r<=?", 10)));

		System.out.println(select(fragmentSelect("cc.r,cc.name,cc.age")).from(
				subSelect(select(fragmentSelect("r"), CUSTOM.NAME, CUSTOM.AGE)
						.from(CUSTOM).where(fragmentCondition("r>?", 1)), "cc",
						true)).where(fragmentCondition("r<=?", 10)));

		System.out.println(select(fragmentSelect("c.name,c.age")).from(CUSTOM));

		SubSelect sub = subSelect(select(top(4), fragmentSelect("*")).from(
				CUSTOM).orderBy(desc(CUSTOM.ID)));
		System.out.println(select(top(2), fragmentSelect("*")).from(sub)
				.orderBy(asc(new FragmentExpressionSql("c.id"))));

		System.out.println(select(CUSTOM.NAME).from(CUSTOM).where(
				CUSTOM.NAME.isNull()));

		System.out.println(select(CUSTOM.NAME).from(CUSTOM).where(
				CUSTOM.NAME.equal(null)));

		System.out.println(select(CUSTOM.NAME).from(CUSTOM).where(
				and(CUSTOM.AGE.greaterThan(null), CUSTOM.NAME.equal(null))));
		
		System.out.println(select(CUSTOM.NAME).from(CUSTOM).where(
				and(CUSTOM.AGE.greaterThan(33), CUSTOM.NAME.isNotEmpty())));

		System.out.println(select(CUSTOM.NAME).from(CUSTOM).where(
				or(and(CUSTOM.AGE.greaterThan(33), CUSTOM.NAME.equal("")),
						CUSTOM.ID.eq("123"))));
		
		System.out.println(selectFrom(CUSTOM).where(CUSTOM.AGE.in(1,5,10)));
		
		System.out.println(selectFrom(CUSTOM).where(CUSTOM.AGE.notIn(1,5,10)));

	}
}
