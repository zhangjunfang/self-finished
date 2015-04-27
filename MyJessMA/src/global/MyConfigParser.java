package global;

import org.dom4j.Element;
import org.jessma.app.AppConfig;
import org.jessma.app.UserConfigParser;
import org.jessma.dao.hbn.HibernateSessionMgr;
import org.jessma.dao.jdbc.AbstractJdbcSessionMgr;
import org.jessma.dao.mybatis.MyBatisSessionMgr;
import org.jessma.util.LogUtil;
import org.slf4j.Logger;

public class MyConfigParser implements UserConfigParser
{
	Logger logger = LogUtil.getJessMALogger();
	
	private static final String MY_HOME = "my-home";
	
	private static AbstractJdbcSessionMgr jdbcSessionMgr;
	private static MyBatisSessionMgr myBaitsSessionMgr;
	private static HibernateSessionMgr hibernateSessionMgr;
	
	@Override
	public void parse(Element user)
	{
		jdbcSessionMgr		= (AbstractJdbcSessionMgr)AppConfig.getSessionManager("session-mgr-1");
		myBaitsSessionMgr	= (MyBatisSessionMgr)AppConfig.getSessionManager("session-mgr-2");
		hibernateSessionMgr	= (HibernateSessionMgr)AppConfig.getSessionManager("session-mgr-3");
		
		Element mh = user.element(MY_HOME);
		if(mh != null)
		{
			String myHome = mh.getTextTrim();
			logger.info("My Home is: " + myHome);
		}
	}

	public static final AbstractJdbcSessionMgr getJdbcSessionMgr()
	{
		return jdbcSessionMgr;
	}

	public static MyBatisSessionMgr getMyBaitsSessionMgr()
	{
		return myBaitsSessionMgr;
	}

	public static HibernateSessionMgr getHibernateSessionMgr()
	{
		return hibernateSessionMgr;
	}

}
