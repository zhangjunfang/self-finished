package vo;

import java.util.Date;
import java.util.List;

public class Person
{
	private String firstName;
	private String lastName;
	private Date birthday;
	private boolean gender;
	private int workingAge;
	private List<Integer> interest;
	private List<String> photos;

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	public boolean isGender()
	{
		return gender;
	}

	public void setGender(boolean gender)
	{
		this.gender = gender;
	}

	public int getWorkingAge()
	{
		return workingAge;
	}

	public void setWorkingAge(int workingAge)
	{
		this.workingAge = workingAge;
	}

	public List<Integer> getInterest()
	{
		return interest;
	}

	public void setInterest(List<Integer> interest)
	{
		this.interest = interest;
	}

	public List<String> getPhotos()
	{
		return photos;
	}

	public void setPhotos(List<String> photos)
	{
		this.photos = photos;
	}

}
