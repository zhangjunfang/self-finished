package org.tinygroup.jdbctemplatedslsession.execute;

import static org.tinygroup.jdbctemplatedslsession.CustomTable.CUSTOM;
import static org.tinygroup.jdbctemplatedslsession.ScoreTable.TSCORE;
import static org.tinygroup.tinysqldsl.Delete.delete;
import static org.tinygroup.tinysqldsl.Insert.insertInto;
import static org.tinygroup.tinysqldsl.Select.select;
import static org.tinygroup.tinysqldsl.Select.selectFrom;
import static org.tinygroup.tinysqldsl.Update.update;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;
import static org.tinygroup.tinysqldsl.select.Join.leftJoin;

import java.sql.SQLException;

import org.tinygroup.jdbctemplatedslsession.Custom;
import org.tinygroup.jdbctemplatedslsession.CustomScore;
import org.tinygroup.jdbctemplatedslsession.SimpleDslSession;
import org.tinygroup.tinysqldsl.Delete;
import org.tinygroup.tinysqldsl.DslSession;
import org.tinygroup.tinysqldsl.Insert;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.Update;

public class DslSqlTest extends BaseTest {

	public void testDsl() throws SQLException {

		Delete delete = delete(CUSTOM);
		DslSession session = new SimpleDslSession(dataSource);
		int affect = session.execute(delete);

		delete = delete(TSCORE);
		affect = session.execute(delete);

		Insert customInsert = insertInto(CUSTOM).values(
				CUSTOM.ID.value("10001"), CUSTOM.NAME.value("悠悠然然"),
				CUSTOM.AGE.value(22));
		affect = session.execute(customInsert);
		assertEquals(1, affect);

		Insert scoreInsert = insertInto(TSCORE).values(
				TSCORE.ID.value("10002"), TSCORE.NAME.value("悠悠然然"),
				TSCORE.SCORE.value(98), TSCORE.COURSE.value("shuxue"));
		affect = session.execute(scoreInsert);
		assertEquals(1, affect);

		Select select = selectFrom(CUSTOM).where(CUSTOM.NAME.like("悠"));
		Custom custom = session.fetchOneResult(select, Custom.class);
		assertEquals("悠悠然然", custom.getName());

		select = select(CUSTOM.NAME, CUSTOM.AGE, TSCORE.SCORE, TSCORE.COURSE)
				.from(CUSTOM).join(
						leftJoin(TSCORE, CUSTOM.NAME.eq(TSCORE.NAME)));
		CustomScore customScore = session.fetchOneResult(select,
				CustomScore.class);
		assertEquals("悠悠然然", customScore.getName());
		assertEquals(98, customScore.getScore());
		assertEquals(22, customScore.getAge());
		assertEquals("shuxue", customScore.getCourse());
		
		select=select(CUSTOM.AGE.max()).from(CUSTOM);
        int max =session.fetchOneResult(select, Integer.class);
        assertEquals(22, max);
        
        select=selectFrom(CUSTOM).where(CUSTOM.AGE.in(1,5,10,22,25));
        custom=session.fetchOneResult(select,Custom.class);
        assertEquals("悠悠然然", custom.getName());

		Update update = update(CUSTOM).set(CUSTOM.NAME.value("flank"),
				CUSTOM.AGE.value(30)).where(CUSTOM.NAME.eq("悠悠然然"));
		affect = session.execute(update);
		assertEquals(1, affect);
		
		
		delete=delete(CUSTOM).where(and(CUSTOM.NAME.leftLike("a"), CUSTOM.AGE.between(1, 10)));
		affect = session.execute(delete);
		assertEquals(0, affect);
		
		
		delete=delete(CUSTOM).where(and(CUSTOM.NAME.leftLike(null), CUSTOM.AGE.between(1, 10)));
		affect = session.execute(delete);
		assertEquals(0, affect);

		delete = delete(CUSTOM).where(CUSTOM.NAME.eq("flank"));
		affect = session.execute(delete);
		assertEquals(1, affect);
		delete = delete(TSCORE).where(TSCORE.NAME.eq("悠悠然然"));
		affect = session.execute(delete);
	
	}
}
