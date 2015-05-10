package org.tinygroup.dbrouter.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 主键生成表配置信息
 * @author yancheng11334
 *
 */
@XStreamAlias("key-table")
public class KeyTable {

	@XStreamAsAttribute
	private String language;
	
	@XStreamAlias("class")
	@XStreamAsAttribute
	private String className;
	
	private String sql;
	
    private static final String TABLE_TAG = "@tablename";

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getRealSql(String tableName){
		return sql==null?"":sql.replaceAll(TABLE_TAG, tableName);
	}
	
}
