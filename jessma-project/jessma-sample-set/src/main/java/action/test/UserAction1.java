package action.test;

import java.util.List;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;
import org.jessma.dao.FacadeProxy;

import dao.mybatis.UserDao;
import vo.User;

@FormBean
public class UserAction1 extends ActionSupport
{
	// create() 入口方法自动装配以下属性
	private User user;
	
	// delete() 入口方法自动装配以下属性
	private int id;
	
	// findUsers() 入口方法自动装配以下属性
	private String name;
	private int experience;
	
	private List<User> users;
	
	/* 
	 * Action Entry:
	 * /test/entry/user.action			-> execute()
	 * /test/entry/user!create.action	-> create()
	 * /test/entry/user!delete.action	-> delete()
	 * /test/entry/user!query.action	-> findUsers()
	*/
	
	@FormBean("user")
	public String create() throws Exception
	{
		UserDao dao = FacadeProxy.getManualCommitProxy(UserDao.class);
		dao.createUser(user);
		
		return SUCCESS;
	}
	
	public String delete() throws Exception
	{
		UserDao dao = FacadeProxy.getManualCommitProxy(UserDao.class);
		dao.deleteUser(id);
		
		return SUCCESS;
	}
	
	public String findUsers() throws Exception
	{
		UserDao dao = FacadeProxy.getAutoCommitProxy(UserDao.class);
		users = dao.findUsers(name, experience);
		
		return SUCCESS;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getExperience()
	{
		return experience;
	}

	public void setExperience(int experience)
	{
		this.experience = experience;
	}

	public List<User> getUsers()
	{
		return users;
	}
}
