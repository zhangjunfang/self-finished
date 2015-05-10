package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.ComplexSelect.union;
import static org.tinygroup.tinysqldsl.ComplexSelect.unionAll;
import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.ScoreTable.TSCORE;
import static org.tinygroup.tinysqldsl.Select.customSelectItem;
import static org.tinygroup.tinysqldsl.Select.select;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.or;
import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentCondition;
import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentFrom;
import static org.tinygroup.tinysqldsl.base.FragmentSql.fragmentSelect;
import static org.tinygroup.tinysqldsl.extend.MysqlSelect.selectFrom;
import static org.tinygroup.tinysqldsl.select.Join.fullJoin;
import static org.tinygroup.tinysqldsl.select.Join.leftJoin;
import static org.tinygroup.tinysqldsl.select.Join.newJoin;
import static org.tinygroup.tinysqldsl.select.Join.rightJoin;
import static org.tinygroup.tinysqldsl.select.OrderByElement.asc;
import static org.tinygroup.tinysqldsl.select.OrderByElement.desc;
import static org.tinygroup.tinysqldsl.selectitem.Top.top;
import junit.framework.TestCase;

public class JunitTestSelect extends TestCase {

	public void testSelect() {
		assertEquals(selectFrom(CUSTOM).sql(), "SELECT * FROM custom");

		assertEquals(select(customSelectItem("%s,%s", CUSTOM.NAME, CUSTOM.AGE))
				.from(CUSTOM).sql(),
				"SELECT custom.name,custom.age FROM custom");

		assertEquals(
				select(
						customSelectItem("upper(%s),%s", CUSTOM.NAME,
								CUSTOM.AGE)).from(CUSTOM).sql(),
				"SELECT upper(custom.name),custom.age FROM custom");

		assertEquals(selectFrom(CUSTOM).orderBy(desc(CUSTOM.NAME)).sql(),
				"SELECT * FROM custom ORDER BY custom.name DESC");

		assertEquals(selectFrom(CUSTOM).orderBy(asc(CUSTOM.ID)).sql(),
				"SELECT * FROM custom ORDER BY custom.id");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.NAME.eq("abc")).sql(),
				"SELECT * FROM custom WHERE custom.name = ?");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.NAME.like("abc")).sql(),
				"SELECT * FROM custom WHERE custom.name LIKE ?");

		assertEquals(
				selectFrom(CUSTOM).where(
						or(CUSTOM.NAME.like("abc"), CUSTOM.AGE.gt(20))).sql(),
				"SELECT * FROM custom WHERE custom.name LIKE ? or custom.age > ?");

		assertEquals(
				selectFrom(CUSTOM).where(
						and(CUSTOM.NAME.like("abc"), CUSTOM.AGE.gt(20))).sql(),
				"SELECT * FROM custom WHERE custom.name LIKE ? and custom.age > ?");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.NAME.leftLike("abc"))
				.sql(), "SELECT * FROM custom WHERE custom.name LIKE ?");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.NAME.rightLike("abc"))
				.sql(), "SELECT * FROM custom WHERE custom.name LIKE ?");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.NAME.isNull()).sql(),
				"SELECT * FROM custom WHERE custom.name IS NULL");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.NAME.isNotNull()).sql(),
				"SELECT * FROM custom WHERE custom.name IS NOT NULL");

		assertEquals(
				selectFrom(CUSTOM).where(CUSTOM.AGE.between(23, 25)).sql(),
				"SELECT * FROM custom WHERE custom.age BETWEEN ? AND ?");

		assertEquals(select(CUSTOM.AGE.max()).from(CUSTOM).sql(),
				"SELECT max(custom.age) FROM custom");

		assertEquals(select(CUSTOM.AGE.min()).from(CUSTOM).sql(),
				"SELECT min(custom.age) FROM custom");

		assertEquals(select(CUSTOM.AGE.avg()).from(CUSTOM).sql(),
				"SELECT avg(custom.age) FROM custom");

		assertEquals(select(CUSTOM.AGE.count()).from(CUSTOM).sql(),
				"SELECT count(custom.age) FROM custom");

		assertEquals(select(CUSTOM.AGE.sum()).from(CUSTOM).sql(),
				"SELECT sum(custom.age) FROM custom");

		assertEquals(select(CUSTOM.NAME.distinct()).from(CUSTOM).forUpdate()
				.sql(), "SELECT DISTINCT (custom.name)  FROM custom FOR UPDATE");

		assertEquals(
				select(CUSTOM.NAME, CUSTOM.AGE, TSCORE.SCORE).from(CUSTOM)
						.join(leftJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME)))
						.sql(),
				"SELECT custom.name,custom.age,score.score FROM custom LEFT JOIN score ON custom.name = score.name");

		assertEquals(
				select(fragmentSelect("custom.name,custom.age"))
						.from(fragmentFrom("custom custom"))
						.where(fragmentCondition("custom.name=?", "悠悠然然"))
						.sql(),
				"SELECT custom.name,custom.age FROM custom custom WHERE custom.name=?");

		assertEquals(select(CUSTOM.ID.count()).from(CUSTOM).groupBy(CUSTOM.AGE)
				.sql(),
				"SELECT count(custom.id) FROM custom GROUP BY custom.age");

		assertEquals(
				select(CUSTOM.ID.count()).from(CUSTOM)
						.groupBy(CUSTOM.NAME, CUSTOM.AGE).sql(),
				"SELECT count(custom.id) FROM custom GROUP BY custom.name,custom.age");

		assertEquals(union(selectFrom(CUSTOM), selectFrom(CUSTOM)).sql(),
				"(SELECT * FROM custom) UNION (SELECT * FROM custom)");

		assertEquals(unionAll(selectFrom(CUSTOM), selectFrom(CUSTOM)).sql(),
				"(SELECT * FROM custom) UNION ALL (SELECT * FROM custom)");

		assertEquals(
				select(CUSTOM.AGE, CUSTOM.NAME).from(CUSTOM)
						.join(rightJoin(TSCORE, TSCORE.NAME.eq(CUSTOM.NAME)))
						.sql(),
				"SELECT custom.age,custom.name FROM custom RIGHT JOIN score ON score.name = custom.name");

		assertEquals(
				select(CUSTOM.AGE, CUSTOM.NAME).from(CUSTOM)
						.join(fullJoin(TSCORE, TSCORE.NAME.eq(CUSTOM.NAME)))
						.sql(),
				"SELECT custom.age,custom.name FROM custom FULL JOIN score ON score.name = custom.name");

		assertEquals(
				select(TSCORE.SCORE.sum()).from(TSCORE).groupBy(TSCORE.NAME)
						.having(TSCORE.SCORE.sum().gt(300)).sql(),
				"SELECT sum(score.score) FROM score GROUP BY score.name HAVING sum(score.score) > ?");

		assertEquals(select(top(10), CUSTOM.NAME).from(CUSTOM).sql(),
				"SELECT TOP (10),custom.name FROM custom");

		assertEquals(selectFrom(CUSTOM).where(CUSTOM.AGE.gt(30)).forUpdate()
				.sql(), "SELECT * FROM custom WHERE custom.age > ? FOR UPDATE");

		assertEquals(select(TSCORE.SCORE.avg(), TSCORE.NAME).from(TSCORE)
				.groupBy(TSCORE.NAME).sql(),
				"SELECT avg(score.score),score.name FROM score GROUP BY score.name");

		assertEquals(
				select(TSCORE.SCORE.sum(), TSCORE.NAME).from(TSCORE)
						.having(TSCORE.SCORE.sum().gt(200))
						.orderBy(asc(TSCORE.SCORE.sum())).sql(),
				"SELECT sum(score.score),score.name FROM score HAVING sum(score.score) > ? ORDER BY sum(score.score)");

		assertEquals(
				select(fragmentSelect("sum(score.score) s"), TSCORE.NAME)
						.from(TSCORE).having(fragmentCondition("s>?", "200"))
						.orderBy(asc(fragmentCondition("s"))).sql(),
				"SELECT sum(score.score) s,score.name FROM score HAVING s>? ORDER BY s");

		assertEquals(
				select(CUSTOM.NAME, TSCORE.SCORE).from(CUSTOM)
						.join(newJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME)))
						.sql(),
				"SELECT custom.name,score.score FROM custom JOIN score ON custom.name = score.name");

		assertEquals(
				select(fragmentSelect("r"), CUSTOM.NAME, CUSTOM.AGE)
						.from(fragmentFrom("(select ROWNUM r,custom.name,custom.age from custom where r>=1)"))
						.where(fragmentCondition("r<=?", 10)).sql(),
				"SELECT r,custom.name,custom.age FROM (select ROWNUM r,custom.name,custom.age from custom where r>=1) WHERE r<=?");

		assertEquals(
				selectFrom(CUSTOM).where(
						CUSTOM.NAME.eq("abc").and(CUSTOM.AGE.greaterThan(30))
								.or(CUSTOM.ID.in(1, 3, 6))).sql(),
				"SELECT * FROM custom WHERE custom.name = ? AND custom.age > ? OR custom.id IN (?, ?, ?)");
	}

}
