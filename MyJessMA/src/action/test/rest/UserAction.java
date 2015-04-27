package action.test.rest;

import java.util.List;

import vo.User;

import org.jessma.dao.DaoBean;
import org.jessma.ext.rest.Get;
import org.jessma.ext.rest.RestActionSupport;
import org.jessma.ext.rest.RestResult;
import org.jessma.ext.rest.renderer.RenderType;

import dao.mybatis.UserDao;

@DaoBean
public class UserAction extends RestActionSupport
{
	// HTML 视图页面需要的属性
	private String name;
	private int experience;
	private boolean fromQuery;
	
	// DAO 对象
	UserDao dao;

	/*
	 * 
	 * REST Entry:
	 *   -> /user
	 * --------------------
	 * Action Entry:
	 *   -> /test/rest/user.action -> execute() -> dispatch : ${jsp-base}/test/rest/user.jsp
	 * --------------------
	 * REST Mapping:
	 *   -> GET	  : ""			-> index()
	 *   -> POST  : ""			-> create()
	 *   -> DELETE: "/{0}"		-> delete(int)
	 *   -> GET	  : "/{0},{1}"	-> query(String, int)
	 *   -> GET	  : "/{1}"		-> query(String, int)
	 * 
	 */

	@Override
	// Rails-style REST 标准方法，不需要 REST 注解
	public RestResult index()
	{
		return new RestResult(SUCCESS);
	}

	@Override
	// Rails-style REST 标准方法，不需要 REST 注解
	public RestResult create() throws Exception
	{
		// 注入 Form Bean
		User user = createFormBean(User.class);
		
		dao.createUser(user);

		if(getRestContext().getRenderType() == RenderType.HTML)
			return doQuery(user.getName(), user.getExperience());
		else
			return new RestResult(SUCCESS);
	}

	@Override
	// Rails-style REST 标准方法，不需要 REST 注解
	public RestResult delete(int id) throws Exception
	{
		dao.deleteUser(id);

		if(getRestContext().getRenderType() == RenderType.HTML)
			return doQuery(null, 0);
		else
			return new RestResult(SUCCESS);
	}
	
	// 非 Rails-style REST 标准方法，需要 REST 注解
	@Get({"/{0},{1}", "/{1}"})
	public RestResult query(String name, int experience) throws Exception
	{
		return doQuery(name, experience);
	}

	private RestResult doQuery(String name, int experience)
	{
		this.name		 = name;
		this.experience	 = experience;
		this.fromQuery	 = true;
		List<User> model = dao.findUsers(name, experience);

		return new RestResult(SUCCESS, model);
	}

	public String getName()
	{
		return name;
	}

	public int getExperience()
	{
		return experience;
	}

	public boolean isFromQuery()
	{
		return fromQuery;
	}
}
