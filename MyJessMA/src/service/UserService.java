package service;

import java.util.List;

import vo.User;
import dao.mybatis.UserDao;

public class UserService
{
	/* 由 Spring 注入 */
	private UserDao userDao;
	
	public boolean createUser(User user)
	{
		return userDao.createUser(user);
	}
	
	public boolean deleteUser(int id)
	{
		return userDao.deleteUser(id);
	}
	
	public List<User> findUsers(String name, int experience)
	{
		return userDao.findUsers(name, experience);
	}
	
	public List<User> queryInterest(int gender, int experience)
	{
		return userDao.queryInterest(gender, experience);
	}

	public UserDao getUserDao()
	{
		return userDao;
	}

	public void setUserDao(UserDao userDao)
	{
		this.userDao = userDao;
	}
	
}
