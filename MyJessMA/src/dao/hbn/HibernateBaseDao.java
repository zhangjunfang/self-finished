package dao.hbn;

import org.jessma.dao.hbn.HibernateFacade;
import org.jessma.dao.hbn.HibernateSessionMgr;

import global.MyConfigParser;

public class HibernateBaseDao extends HibernateFacade
{
	protected HibernateBaseDao()
	{
		this(MyConfigParser.getHibernateSessionMgr());
	}
	
	protected HibernateBaseDao(HibernateSessionMgr mgr)
	{
		super(mgr);
	}

}
