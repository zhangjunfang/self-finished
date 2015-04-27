package action.test.conv;

import static org.jessma.mvc.Action.*;
import static org.jessma.mvc.Action.ResultType.*;

import java.util.List;

import vo.User;

import org.jessma.dao.DaoBean;
import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;
import org.jessma.mvc.Result;
import org.jessma.mvc.Results;

import dao.mybatis.UserDao;

@DaoBean
@FormBean
@Results
({
	@Result(value = QUERY, type = CHAIN, path = "./user!query")
})
public class UserAction extends ActionSupport
{
	// create() 入口方法自动装配以下属性
	private User user;

	// delete() 入口方法自动装配以下属性
	private int id;

	// findUsers() 入口方法自动装配以下属性
	private String name;
	private int experience;

	// 是否来自查询操作
	private boolean fromQuery;

	// DAO 对象
	UserDao dao;

	private List<User> users;

	/*
	 * Action Entry:
	 * ------------------------------
	 * /test/conv/user.action			-> execute() ->	dispatch : ${jsp-base}/test/conv/user.jsp
	 * /test/conv/user!create.action	-> create() ->	chain	 : ./user!query
	 * /test/conv/user!delete.action	-> delete() ->	chain	 : ./user!query
	 * /test/conv/user!query.action		-> query() ->	dispatch : ${jsp-path}/conv/user.jsp
	 */

	@FormBean("user")
	public String create() throws Exception
	{
		dao.createUser(user);

		return QUERY;
	}

	public String delete() throws Exception
	{
		dao.deleteUser(id);

		return QUERY;
	}

	@Result(value = LIST, path = "${jsp-path}/conv/user.jsp")
	public String query() throws Exception
	{
		users		= dao.findUsers(name, experience);
		fromQuery	= true;

		return LIST;
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

	public boolean isFromQuery()
	{
		return fromQuery;
	}
}
