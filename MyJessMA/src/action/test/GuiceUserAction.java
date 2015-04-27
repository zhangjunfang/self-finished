package action.test;

import java.util.List;

import org.jessma.ext.guice.GuiceBean;
import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

import guice.UserService;
import vo.User;

@FormBean
@GuiceBean("userService")
public class GuiceUserAction extends ActionSupport
{
	// create() 入口方法自动装配以下属性
	private User user;
	
	// delete() 入口方法自动装配以下属性
	private int id;
	
	// findUsers() 入口方法自动装配以下属性
	private String name;
	private int experience;
	
	/* 由 MyGuiceInjectFilter 注入 */
	private UserService userService;
	
	private List<User> users;
	
	/* 
	 * Action Entry (通过 类中声明的 @GuiceBean 注入 userService)
	 * 
	 * /test/guice/user.action			-> execute()
	 * /test/guice/user!create.action	-> create()
	 * /test/guice/user!delete.action	-> delete()
	 * /test/guice/user!query.action	-> findUsers()
	*/
	
	@FormBean("user")
	public String create() throws Exception
	{
		userService.createUser(user);
		
		return SUCCESS;
	}
	
	public String delete() throws Exception
	{
		userService.deleteUser(id);
		
		return SUCCESS;
	}
	
	public String findUsers() throws Exception
	{	
		users = userService.findUsers(name, experience);
		
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
