package dao.jdbc;

import global.Cache;

import java.util.ArrayList;
import java.util.List;

import org.jessma.util.GeneralHelper;
import org.jessma.util.KV;

import vo.Interest;
import vo.User;

public class UserDao extends JdbcBaseDao
{
	public boolean createUser(User user)
	{
		String sql = "INSERT INTO user (name, age, gender, experience) VALUES (?, ?, ?, ?)";
		
		KV<Integer, List<Object>> result = updateAndGenerateKeys(
																	sql, 
																	user.getName(), 
																	user.getAge(), 
																	user.getGender(), 
																	user.getExperience()
																);
		int effect = result.getKey();
		
		if(effect > 0)
		{
			List<Integer> interests	= user.getInterests();
			
			if(interests != null && interests.size() > 0)
			{
				sql = "INSERT INTO user_interest (user_id, interest_id) VALUES (?, ?)";
				
				List<Object[]> params = new ArrayList<Object[]>();
				List<Object> userIdList	= result.getValue();
				Long userId				= (Long)userIdList.get(0);
				
				for(Integer interest : interests)
					params.add(new Object[] {userId, interest});
				
				updateBatch(sql, params);
			}
		}
		
		return (effect > 0);
	}

	public boolean deleteUser(int id)
	{
		String sql = "DELETE FROM user_interest WHERE user_id = ?";
		update(sql, id);
		
		sql = "DELETE FROM user WHERE id = ?";
		int effect = update(sql, id);
		
		return (effect > 0);
	}

	public List<User> findUsers(String name, int experience)
	{
		List<User> result	= new ArrayList<User>();
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sqlBuilder = new StringBuilder("SELECT id, name, age, gender, experience FROM user ");
		
		if(GeneralHelper.isStrNotEmpty(name))
		{
			sqlBuilder.append("WHERE name LIKE ? ");
			params.add("%" + name + "%");
		}
		else
			sqlBuilder.append("WHERE 1 = 1 ");
		
		if(experience != 0)
		{
			sqlBuilder.append("AND experience = ? ");
			params.add(experience);
		}
		
		sqlBuilder.append("ORDER BY id DESC");
		
		String sql			= sqlBuilder.toString();
		List<Object[]> list	= query(sql, params.toArray());
		Cache cache			= Cache.getInstance();
		
		for(Object[] objs : list)
		{
			User user = new User();
			user.setId((Integer)objs[0]);
			user.setName((String)objs[1]);
			user.setAge((Integer)objs[2]);
			
			Integer genderId = (Integer)objs[3];
			user.setGender(genderId);
			user.setGenderObj(cache.getGenderById(genderId));
			
			Integer experienceId = (Integer)objs[4];
			user.setExperience(experienceId);
			user.setExperienceObj(cache.getExperenceById(experienceId));
			
			sql = "SELECT interest_id FROM user_interest WHERE user_id = ? ORDER BY id";
			
			List<Object[]> interests = query(sql, user.getId());
			List<Integer> interestList = new ArrayList<Integer>();
			List<Interest> interestObjList = new ArrayList<Interest>();

			for(Object[] ints : interests)
			{
				Integer interestId = (Integer)ints[0];
				interestList.add(interestId);
				interestObjList.add(cache.getInterestById(interestId));
			}
			
			user.setInterests(interestList);
			user.setInterestObjs(interestObjList);
			
			result.add(user);
		}
			
		return result;
	}
}
