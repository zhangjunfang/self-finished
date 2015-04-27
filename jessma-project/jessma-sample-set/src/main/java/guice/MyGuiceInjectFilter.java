package guice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jessma.dao.FacadeProxy;
import org.jessma.ext.guice.GuiceInjectFilter;

import com.google.inject.Binder;
import com.google.inject.Module;

public class MyGuiceInjectFilter extends GuiceInjectFilter
{
	@Override
	protected Collection<Module> configModules()
	{
		Set<Module> modules = new HashSet<Module>();
		
		// JDBC DAO Binding Module
		modules.add(new Module()
		{
			@Override
			public void configure(Binder binder)
			{
				// 配置绑定规则
				binder.bind(dao.jdbc.UserDao.class).toInstance(FacadeProxy.create(dao.jdbc.UserDao.class));
			}
		});
		
		// MyBatis DAO Binding Module
		modules.add(new Module()
		{
			@Override
			public void configure(Binder binder)
			{
				// 配置绑定规则
				binder.bind(dao.mybatis.UserDao.class).toInstance(FacadeProxy.create(dao.mybatis.UserDao.class));
			}
		});

		// Hibernate DAO Binding Module
		modules.add(new Module()
		{
			@Override
			public void configure(Binder binder)
			{
				// 配置绑定规则
				binder.bind(dao.hbn.UserDao.class).toInstance(FacadeProxy.create(dao.hbn.UserDao.class));
			}
		});

		return modules;
	}

}
