package org.tinygroup.jsqlparser.util;

import java.io.StringReader;

import junit.framework.TestCase;

import org.tinygroup.jsqlparser.extend.ParameterFinder;
import org.tinygroup.jsqlparser.extend.ParameterFinder.ParameterEntry;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.select.Select;

public class ParameterFinderTest extends TestCase {
	
	CCJSqlParserManager pm = new CCJSqlParserManager();
	
	public void testParameter() throws Exception{
		String sql="select * from user where name='test'";
		Select select=(Select) pm.parse(new StringReader(sql));
		ParameterFinder finder=new ParameterFinder();
		ParameterEntry parameterEntry= finder.getParameterEntry(select);
		assertEquals(0, parameterEntry.getParamLength());
		sql="SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 3, ?";
		select=(Select) pm.parse(new StringReader(sql));
		parameterEntry= finder.getParameterEntry(select);
		assertEquals(1, parameterEntry.getParamLength());
		assertEquals(1, parameterEntry.getPositionMap().get("limit").intValue());
		sql="SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?,?";
		select=(Select) pm.parse(new StringReader(sql));
		parameterEntry= finder.getParameterEntry(select);
		assertEquals(2, parameterEntry.getParamLength());
		assertEquals(1, parameterEntry.getPositionMap().get("start").intValue());
		assertEquals(2, parameterEntry.getPositionMap().get("limit").intValue());
		sql="SELECT * FROM mytable WHERE mytable.col = ? LIMIT ? OFFSET ?";
		select=(Select) pm.parse(new StringReader(sql));
		parameterEntry= finder.getParameterEntry(select);
		assertEquals(3, parameterEntry.getParamLength());
		assertEquals(3, parameterEntry.getPositionMap().get("offset").intValue());
		assertEquals(2, parameterEntry.getPositionMap().get("limit").intValue());
		sql="(SELECT * FROM mytable WHERE mytable.col = 9 OFFSET ?) UNION "
            + "(SELECT * FROM mytable2 WHERE mytable2.col = 9 OFFSET ?) LIMIT ?, ?";
		select=(Select) pm.parse(new StringReader(sql));
		parameterEntry= finder.getParameterEntry(select);
		assertEquals(4, parameterEntry.getParamLength());
		assertEquals(3, parameterEntry.getPositionMap().get("start").intValue());
		assertEquals(4, parameterEntry.getPositionMap().get("limit").intValue());
		
		
	}
	

}
