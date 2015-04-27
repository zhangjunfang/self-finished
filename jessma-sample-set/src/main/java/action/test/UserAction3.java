package action.test;

import java.util.List;

import vo.User;

import org.jessma.dao.DaoBean;
import org.jessma.dao.DaoBeans;
import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

import dao.mybatis.UserDao;

@DaoBeans
@FormBean
public class UserAction3 extends ActionSupport
{
	// create() 入口方法自动装配以下属性
	private User user;
	
	// delete() 入口方法自动装配以下属性
	private int id;
	
	// findUsers() 入口方法自动装配以下属性
	private String name;
	private int experience;
	
	UserDao dao;
	
	private List<User> users;
	
	/* 
	 * Action Entry:
	 * /test/entry/user.action			-> execute()
	 * /test/entry/user!create.action	-> create()
	 * /test/entry/user!delete.action	-> delete()
	 * /test/entry/user!query.action	-> findUsers()
	*/
	
	@DaoBean
	@FormBean("user")
	public String create() throws Exception
	{
		dao.createUser(user);
		
		return SUCCESS;
	}
	
	@DaoBean(value="dao", daoClass=UserDao.class, mgrName="session-mgr-2")
	public String delete() throws Exception
	{
		dao.deleteUser(id);
		
		return SUCCESS;
	}
	
	public String findUsers() throws Exception
	{
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
