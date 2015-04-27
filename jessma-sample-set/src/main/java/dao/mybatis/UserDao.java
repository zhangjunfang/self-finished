package dao.mybatis;

import global.Cache;

import java.util.ArrayList;
import java.util.List;

import org.jessma.dao.TransIsoLevel;
import org.jessma.dao.Transaction;
import org.jessma.dao.mybatis.MyBatisSessionMgr;

import vo.Interest;
import vo.User;
import vo.mapper.UserMapper;

public class UserDao extends MyBatisBaseDao
{
	protected UserDao()
	{
		super();
	}
	
	protected UserDao(MyBatisSessionMgr mgr)
	{
		super(mgr);
	}
	
	@Transaction(level=TransIsoLevel.REPEATABLE_READ)
	public boolean createUser(User user)
	{		
		UserMapper mapper = getMapper(UserMapper.class);
		int effect = mapper.createUser(user);
		
		if(effect > 0)
		{
			/*
			 * changeSessionExecutorTypeToXxx() 方法：
			 * 能在事务中动态修改 Session 的 Executor Type。
			 * 在执行批量更新等场合非常有用，使用得当能大大提升性能
			 * 
			 * 本例展示了一种典型的使用场合：Session 通过默认（SIMPLE）
			 * Executor Type 向主表插入一条记录并获得自增主键，然后把 
			 * Executor Type 动态修改为 BATCH，对从表执行批量更新操作
			 */
			changeSessionExecutorTypeToBatch();

			int userId = user.getId();
			List<Integer> interests = user.getInterests();
			
			if(interests != null)
				for(Integer interestId : interests)
					mapper.createUserInterest(userId, interestId);	
		}
		
		return (effect > 0);
	}

	public boolean deleteUser(int id)
	{
		UserMapper mapper = getMapper(UserMapper.class);
		mapper.deleteUserInterest(id);
		int effect = mapper.deleteUser(id);
		
		return (effect > 0);
	}

	@Transaction(false)
	public List<User> findUsers(String name, int experience)
	{
		Cache cache			= Cache.getInstance();
		UserMapper mapper	= getMapper(UserMapper.class);
		List<User> users	= mapper.findUsers(name, experience);
		
		for(User user : users)
		{			
			user.setGenderObj(cache.getGenderById(user.getGender()));
			user.setExperienceObj(cache.getExperenceById(user.getExperience()));
			
			List<Integer> ints = user.getInterests();
			
			if(ints != null)
			{
				List<Interest> intsObjs = new ArrayList<Interest>();
				
				for(Integer i : ints)
					intsObjs.add(cache.getInterestById(i));
				
				user.setInterestObjs(intsObjs);
			}
		}
		
		return users;
	}

	@Transaction(false)
	public List<User> queryInterest(int gender, int experience)
	{
		Cache cache			= Cache.getInstance();
		UserMapper mapper	= getMapper(UserMapper.class);
		List<User> users	= mapper.queryInterest(gender, experience);
		
		for(User user : users)
		{			
			user.setGenderObj(cache.getGenderById(user.getGender()));
			user.setExperienceObj(cache.getExperenceById(user.getExperience()));
			
			List<Integer> ints = user.getInterests();
			
			if(ints != null)
			{
				List<Interest> intsObjs = new ArrayList<Interest>();
				
				for(Integer i : ints)
					intsObjs.add(cache.getInterestById(i));
				
				user.setInterestObjs(intsObjs);
			}
		}
		
		return users;
	}
}
