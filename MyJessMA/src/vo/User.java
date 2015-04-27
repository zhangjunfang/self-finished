package vo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="user_hbn")
public class User
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(length=50)
	private String name;
	private int age;
	private int gender;
	private int experience;
	
	@ElementCollection
	@JoinTable(
		name="user_interest_hbn", 
		joinColumns={@JoinColumn(name="user_id", nullable=false)}
	)
	@OrderColumn(name="list_order")
	@Column(name="interest_id")
	private List<Integer> interests;
	
	@Transient
	private Gender genderObj;
	@Transient
	private Experence experienceObj;
	@Transient
	private List<Interest> interestObjs;

	public int getId()
	{
		return id;
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

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
	}

	public Gender getGenderObj()
	{
		return genderObj;
	}

	public void setGenderObj(Gender genderObj)
	{
		this.genderObj = genderObj;
	}

	public int getExperience()
	{
		return experience;
	}

	public void setExperience(int experience)
	{
		this.experience = experience;
	}

	public Experence getExperienceObj()
	{
		return experienceObj;
	}

	public void setExperienceObj(Experence experienceObj)
	{
		this.experienceObj = experienceObj;
	}

	public List<Integer> getInterests()
	{
		return interests;
	}

	public void setInterests(List<Integer> interests)
	{
		this.interests = interests;
	}

	public List<Interest> getInterestObjs()
	{
		return interestObjs;
	}

	public void setInterestObjs(List<Interest> interestObjs)
	{
		this.interestObjs = interestObjs;
	}
}
