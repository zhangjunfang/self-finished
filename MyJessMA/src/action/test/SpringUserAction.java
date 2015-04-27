package action.test;

import java.util.List;

import service.UserService;
import vo.User;

import org.jessma.ext.spring.SpringBean;
import org.jessma.ext.spring.SpringBeans;
import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

@FormBean
@SpringBeans
public class SpringUserAction extends ActionSupport
{
	// create() 入口方法自动装配以下属性
	private User user;
	
	// delete() 入口方法自动装配以下属性
	private int id;
	
	// findUsers() 入口方法自动装配以下属性
	private String name;
	private int experience;
	
	/* 由 SpringInjectFilter 注入 */
	private UserService userService;
	
	private List<User> users;
	
	/* 
	 * Action Entry:
	 * /test/spring/user.action			-> execute()	: 不注入 Spring Bean
	 * /test/spring/user!create.action	-> create()		: 通过 Bean 名称和类型注入 Spring Bean
	 * /test/spring/user!delete.action	-> delete()		: 通过 Bean 名称注入 Spring Bean
	 * /test/spring/user!query.action	-> findUsers()	: 通过 Bean 类型注入 Spring Bean
	*/
	
	// 通过 Bean 名称和类型注入
	@FormBean("user")
	@SpringBean(value="userService", name="userService", type=service.UserService.class)
	public String create() throws Exception
	{
		userService.createUser(user);
		
		return SUCCESS;
	}
	
	// 通过 Bean 名称注入，由于没有指定 Bean 名称（没有指定 name 参数），
	// Bean 名称会默认为被注入的属性或成员变量名称（value 参数）
	@SpringBean("userService")
	public String delete() throws Exception
	{
		userService.deleteUser(id);
		
		return SUCCESS;
	}
	
	// 通过 Bean 类型注入
	@SpringBean(value="userService", type=service.UserService.class)
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
