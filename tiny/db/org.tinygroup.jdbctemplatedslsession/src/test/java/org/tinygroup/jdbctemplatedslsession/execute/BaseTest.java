package org.tinygroup.jdbctemplatedslsession.execute;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.Resources;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.tinytestutil.script.ScriptRunner;

public abstract class BaseTest extends TestCase {
	private boolean hasExcuted;
	protected DataSource dataSource;
	void init() {
		AbstractTestUtil.init(null, true);
	}

	protected void setUp() throws Exception {
		super.setUp();
		if (!hasExcuted) {
			init();
			dataSource = BeanContainerFactory.getBeanContainer(
					getClass().getClassLoader()).getBean("dataSource");
			initTable();
			hasExcuted = true;
		}
	}

	private void initTable() throws IOException, Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			ScriptRunner runner = new ScriptRunner(conn, false, false);
			// 设置字符集
			Resources.setCharset(Charset.forName("utf-8"));
			// 加载sql脚本并执行
			runner.runScript(Resources.getResourceAsReader("table_derby.sql"));
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					fail(e.getMessage());
				}
			}
		}

	}

}
