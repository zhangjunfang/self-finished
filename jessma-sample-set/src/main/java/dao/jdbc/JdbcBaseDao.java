package dao.jdbc;

import global.MyConfigParser;

import org.jessma.dao.jdbc.AbstractJdbcSessionMgr;
import org.jessma.dao.jdbc.JdbcFacade;

public class JdbcBaseDao extends JdbcFacade
{
	protected JdbcBaseDao()
	{
		this(MyConfigParser.getJdbcSessionMgr());
	}
	
	protected JdbcBaseDao(AbstractJdbcSessionMgr mgr)
	{
		super(mgr);
	}
}
