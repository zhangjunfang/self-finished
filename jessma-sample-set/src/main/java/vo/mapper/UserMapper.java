package vo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import vo.User;

public interface UserMapper
{
	@Insert("INSERT INTO user (name, age, gender, experience) VALUES (#{name}, #{age}, #{gender}, #{experience})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int createUser(User user);
	
	@Insert("INSERT INTO user_interest (user_id, interest_id) VALUES (#{userId}, #{interestId})")
	int createUserInterest(@Param("userId") int userId, @Param("interestId") int interestId);
	
	@Delete("DELETE FROM user WHERE id = #{id}")
	int deleteUser(int id);
	
	@Delete("DELETE FROM user_interest WHERE user_id = #{userId}")
	int deleteUserInterest(int userId);
	
	// findUsers(String, int) 的执行策略定义在 UserMapper.xml 中
	List<User> findUsers(@Param("name") String name, @Param("experience") int experience);
	
	@Select("SELECT interest_id FROM user_interest WHERE user_id = #{userId} ORDER BY id")
	List<Integer> findUserInterests(int userId);

	// queryInterest(int, int) 的执行策略定义在 UserMapper.xml 中
	List<User> queryInterest(@Param("gender") int gender, @Param("experience") int experience);
}
