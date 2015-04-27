package global;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jessma.util.LogUtil;

import vo.Experence;
import vo.Gender;
import vo.Interest;

public class Cache
{
	/** 保存在 Servlet Context 中的缓存对象的 Key */
	public static final String CACHE_KEY	= "__cache";
	/** 全局唯一缓存对象 */
	private static final Cache instance		= new Cache();
	
	/** 性别列表 */
	private List<Gender> genders					= new ArrayList<Gender>();
	/** 性别 Map */
	private Map<Integer, Gender> gendersMap			= new HashMap<Integer, Gender>();
	/** 兴趣列表 */
	private List<Interest> interests				= new ArrayList<Interest>();
	/** 兴趣 Map */
	private Map<Integer, Interest> interestsMap		= new HashMap<Integer, Interest>();
	/** 工作年限列表 */
	private List<Experence> experences				= new ArrayList<Experence>();
	/** 工作年限 Map */
	private Map<Integer, Experence> experencesMap	= new HashMap<Integer, Experence>();
	
	/** 私有构造函数 */
	private Cache()
	{
		
	}
	
	/** 缓存对象获取方法 */
	public static final Cache getInstance()
	{
		return instance;
	}

	/** 加载基础数据缓存 */
	synchronized void loadBasicData()
	{
		genders.add(new Gender(1, "男"));
		genders.add(new Gender(2, "女"));
		
		for(Gender o : genders)
			gendersMap.put(o.getId(), o);
			
		interests.add(new Interest(1, "游泳"));
		interests.add(new Interest(2, "打球"));
		interests.add(new Interest(3, "下棋"));
		interests.add(new Interest(4, "打麻将"));
		interests.add(new Interest(5, "看书"));
		
		for(Interest o : interests)
			interestsMap.put(o.getId(), o);
			
		experences.add(new Experence(1, "3 年以下"));
		experences.add(new Experence(2, "3-5 年"));
		experences.add(new Experence(3, "5-10 年"));
		experences.add(new Experence(4, "10 年以上"));
		
		for(Experence o : experences)
			experencesMap.put(o.getId(), o);
	}
	
	/** 卸载基础数据缓存 */
	synchronized void unloadBasicData()
	{
		Field[] fields = this.getClass().getDeclaredFields();

		for(Field f : fields)
		{
			Class<?> type = f.getType();
			
			if(type.isAssignableFrom(List.class) || type.isAssignableFrom(Map.class))
			{
    			try
    			{
    				f.set(this, null);
    			}
    			catch(Exception e)
    			{
    				LogUtil.exception(e, String.format("unload basic data '%s'", f), true);
    			}
			}
		}
	}
	
	/** 通过 ID 查找 Gender 对象 */
	public Gender getGenderById(Integer id)
	{
		return gendersMap.get(id);
	}

	/** 通过 ID 查找 Interest 对象 */
	public Interest getInterestById(Integer id)
	{
		return interestsMap.get(id);
	}

	/** 通过 ID 查找 Experence 对象 */
	public Experence getExperenceById(Integer id)
	{
		return experencesMap.get(id);
	}

	public List<Gender> getGenders()
	{
		return genders;
	}

	public Map<Integer, Gender> getGendersMap()
	{
		return gendersMap;
	}

	public List<Interest> getInterests()
	{
		return interests;
	}

	public Map<Integer, Interest> getInterestsMap()
	{
		return interestsMap;
	}

	public List<Experence> getExperences()
	{
		return experences;
	}

	public Map<Integer, Experence> getExperencesMap()
	{
		return experencesMap;
	}
}
