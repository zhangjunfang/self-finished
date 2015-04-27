/*
 * Copyright Bruce Liang (ldcsaa@gmail.com)
 *
 * Version	: JessMA 3.5.1
 * Author	: Bruce Liang
 * Website	: http://www.jessma.org
 * Project	: http://www.oschina.net/p/portal-basic
 * Blog		: http://www.cnblogs.com/ldcsaa
 * WeiBo	: http://weibo.com/u/1402935851
 * QQ Group	: 75375912
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jessma.dao.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jessma.util.GeneralHelper;


/**
 * 
 * JNDI Session 管理器
 *
 */
public class JndiSessionMgr extends AbstractJdbcSessionMgr
{
	/** JNDI 默认配置文件 */
	public static final String DEFAULT_CONFIG_FILE	= "jndi.cfg.xml";
	
	private static final String JNDI_NAME_KEY		= "jndi-name";
	
	private String jndiName;
	private DataSource dataSource;
	
	@Override
	protected String getDefaultConfigFile()
	{
		return DEFAULT_CONFIG_FILE;
	}
	
	/** 
	 * 初始化 
	 * 
	 * @param args <br>
	 * 			[0]	: configFile （默认：{@link JndiSessionMgr#DEFAULT_CONFIG_FILE}） <br>
	 * @throws InvalidParameterException
	 * @throws JdbcException
	 * 
	*/
	@Override
	public void initialize(String ... args)
	{
		if(args.length == 0)
			initialize();
		else if(args.length == 1)
			initialize(args[0]);
		else
			throw new InvalidParameterException("JndiSessionMgr initialize fail (invalid paramers)");
	}
	
	/** 初始化 */
	public void initialize()
	{
		initialize(DEFAULT_CONFIG_FILE);
	}
	
	/** 初始化 */
	public void initialize(String configFile)
	{
		Context ctx	= null;
		
		try
		{
			parseConfigFile(configFile);
			
			Properties props = new Properties();
			
			if(isXmlConfigFile())
    			loadXmlCfg(props);
    		else
    			loadPropCfg(props);
			
			props		= filtrateCfg(props);
			ctx			= new InitialContext(props);
			dataSource	= (DataSource)ctx.lookup(jndiName);
			
    		loadDefalutTransIsoLevel();
		}
		catch(Exception e)
		{
			try {unInitialize();} catch(Exception e2) {}
			throw new JdbcException(e);
		}
		finally
		{
			if(ctx != null) {try {ctx.close();} catch(Exception e) {}}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadXmlCfg(Properties props) throws Exception
	{
		SAXReader sr	= new SAXReader();
		Document doc	= sr.read(new File(configFile));
		Element root	= doc.getRootElement();
		
		jndiName = root.attributeValue(JNDI_NAME_KEY);
		if(GeneralHelper.isStrEmpty(jndiName))
			throw new RuntimeException(String.format("'%s' attribute must be set for 'root' element", JNDI_NAME_KEY));
		
		List<Element> list = root.elements("property");
		for(Element e : list)
		{
			String key		= e.attributeValue("name");
			String value	= e.getTextTrim();
			
			props.put(key, value);
		}
	}

	private void loadPropCfg(Properties props) throws IOException
	{
		props.load(new FileInputStream(configFile));
		
		jndiName = props.getProperty(JNDI_NAME_KEY);
		if(GeneralHelper.isStrEmpty(jndiName))
			throw new RuntimeException(String.format("'%s' protperty must be set", JNDI_NAME_KEY));
		
		props.remove(JNDI_NAME_KEY);
	}

	private Properties filtrateCfg(Properties props)
	{
		Properties result				= new Properties();
		Set<Entry<Object, Object>> set	= props.entrySet();
		
		for(Entry<Object, Object> e : set)
		{
			String value = GeneralHelper.safeTrimString((String)e.getValue());
			if(GeneralHelper.isStrNotEmpty(value))
				result.put(e.getKey(), value);
		}

		return result;
	}

	/** 注销 */
	@Override
	public void unInitialize()
	{
		dataSource = null;
		super.unInitialize();
	}
	
	/** 获取数据库连接对象 
	 * @throws SQLException */
	@Override
	protected Connection getInternalConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
}
