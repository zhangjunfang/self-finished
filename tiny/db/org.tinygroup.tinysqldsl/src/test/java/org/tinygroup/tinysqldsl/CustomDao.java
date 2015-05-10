package org.tinygroup.tinysqldsl;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Delete.delete;
import static org.tinygroup.tinysqldsl.Insert.insertInto;
import static org.tinygroup.tinysqldsl.Select.selectFrom;
import static org.tinygroup.tinysqldsl.Update.update;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;

import java.util.List;

public class CustomDao {
	private DslSession dslSession;

	public DslSession getDslSession() {
		return dslSession;
	}

	public void setDslSession(DslSession dslSession) {
		this.dslSession = dslSession;
	}

	public Custom insertCustom(Custom custom) {
		Insert insert = insertInto(CUSTOM).values(
				CUSTOM.ID.value(custom.getId()),
				CUSTOM.NAME.value(custom.getName()),
				CUSTOM.AGE.value(custom.getAge()));
		dslSession.execute(insert);
		return custom;
	}

	public void updateCustom(Custom custom) {// 以第一个字段作为条件
		Update update = update(CUSTOM).set(CUSTOM.NAME.value(custom.getName()),
				CUSTOM.AGE.value(custom.getAge())).where(
				CUSTOM.ID.eq(custom.getId()));
		dslSession.execute(update);
	}

	public void deleteCustom(String id) {
		Delete delete = delete(CUSTOM).where(CUSTOM.ID.eq(id));
		dslSession.execute(delete);
	}
	
	public void deleteCustoms(Object... ids) {
		Delete delete = delete(CUSTOM).where(CUSTOM.ID.in(ids));
		dslSession.execute(delete);
	}

	public Custom getCustomById(String id) {
		Select select = selectFrom(CUSTOM).where(CUSTOM.ID.eq(id));
		return dslSession.fetchOneResult(select, Custom.class);
	}

	public List<Custom> queryCustom(Custom custom) {//如果id作为主键字段，不作为条件出现。
		Select select = selectFrom(CUSTOM).where(
				and(CUSTOM.ID.eq(custom.getId()),
						CUSTOM.NAME.equal(custom.getName()),
						CUSTOM.AGE.equal(custom.getAge())));
		return dslSession.fetchList(select, Custom.class);
	}
}
