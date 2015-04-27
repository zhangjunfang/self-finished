package dao.hbn;

import global.Cache;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.jessma.util.GeneralHelper;

import vo.Interest;
import vo.User;

public class UserDao extends HibernateBaseDao
{
	public boolean createUser(User user)
	{
		Integer id = (Integer)save(user);
		
		return id != null;
	}

	public boolean deleteUser(int id)
	{
		User user = get(User.class, id);
		if(user != null)
			delete(user);
		
		return user != null;
	}
	
	public List<User> findUsers(String name, int experience)
	{
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if(GeneralHelper.isStrNotEmpty(name))
			criterions.add(Restrictions.like("name", "%" + name + "%"));
		if(experience != 0)
			criterions.add(Restrictions.eq("experience", experience));
		
		Criterion[] cs		= criterions.toArray(new Criterion[criterions.size()]);
		Order[] orders		= new Order[] {Order.desc("id")};
		List<User> users	= qbcQuery(User.class, orders, cs);
		Cache cache			= Cache.getInstance();
		
		/*
		 * 下面注释中的代码与上面获取 users 的代码效果一样
		 * -----------------------------------------------------------
		Criteria criteria = getSession().createCriteria(User.class);
		if(GeneralHelper.isStrNotEmpty(name))
			criteria.add(Restrictions.like("name", "%" + name + "%"));
		if(experience != 0)
			criteria.add(Restrictions.eq("experience", experience));
		criteria.addOrder(Order.desc("id"));
		List<User> users = (List<User>)criteria.list();
		* ------------------------------------------------------------
		*/
		
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
