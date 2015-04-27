package dao.mybatis;

import org.jessma.dao.mybatis.MyBatisFacade;
import org.jessma.dao.mybatis.MyBatisSessionMgr;

import global.MyConfigParser;

public class MyBatisBaseDao extends MyBatisFacade
{
	protected MyBatisBaseDao()
	{
		this(MyConfigParser.getMyBaitsSessionMgr());
	}
	
	protected MyBatisBaseDao(MyBatisSessionMgr mgr)
	{
		super(mgr);
	}

}
