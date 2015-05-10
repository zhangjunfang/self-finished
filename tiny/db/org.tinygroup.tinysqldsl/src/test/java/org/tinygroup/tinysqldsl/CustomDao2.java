package org.tinygroup.tinysqldsl;

import java.util.List;

import static org.tinygroup.tinysqldsl.CustomTable.CUSTOM;
import static org.tinygroup.tinysqldsl.Delete.delete;
import static org.tinygroup.tinysqldsl.Insert.insertInto;
import static org.tinygroup.tinysqldsl.Select.selectFrom;
import static org.tinygroup.tinysqldsl.base.StatementSqlBuilder.and;
import static org.tinygroup.tinysqldsl.Update.update;

public class CustomDao2 {
	private DslSession dslSession;

	public DslSession getDslSession() {
		return dslSession;
	}

	public void setDslSession(DslSession dslSession) {
		this.dslSession = dslSession;
	}

	public void insertCustom(Custom custom) {
		dslSession.execute(
			insertInto(CUSTOM).values(
				CUSTOM.ID.value(custom.getId()),
				CUSTOM.NAME.value(custom.getName()),
				CUSTOM.AGE.value(custom.getAge())
			)
		);
	}

	public void updateCustom(Custom custom) {// 以第一个字段作为条件
		dslSession.execute(
			update(CUSTOM).set(
				CUSTOM.NAME.value(custom.getName()),
				CUSTOM.AGE.value(custom.getAge())).where(
				CUSTOM.ID.eq(custom.getId())
			)
		);
	}

	public void deleteCustom(String id) {
		dslSession.execute(
				delete(CUSTOM).where(
						CUSTOM.ID.eq(id)
				)
		);
	}

	public Custom getCustomById(String id) {
		return dslSession.fetchOneResult(
			selectFrom(CUSTOM).where(
					CUSTOM.ID.eq(id)
			)
		, Custom.class);
	}

	public List<Custom> queryCustom(Custom custom) {//如果id作为主键字段，不作为条件出现。
		return dslSession.fetchList(
			selectFrom(CUSTOM).where(
				and(
						CUSTOM.ID.eq(custom.getId()),
						CUSTOM.NAME.equal(custom.getName()),
						CUSTOM.AGE.equal(custom.getAge())
				)
			)
		, Custom.class);
	}
}
