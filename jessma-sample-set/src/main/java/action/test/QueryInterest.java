package action.test;

import java.util.List;

import vo.User;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;
import org.jessma.dao.FacadeProxy;

import dao.mybatis.UserDao;

@FormBean
public class QueryInterest extends ActionSupport
{
	private int gender;
	private int experience;
	private List<User> users;
	
	@Override
	public String execute() throws Exception
	{
		UserDao dao = FacadeProxy.getAutoCommitProxy(UserDao.class);
		users = dao.queryInterest(gender, experience);
		
		return SUCCESS;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
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
