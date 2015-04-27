package vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person2
{
	@Size(min=1, max=2)
	@Pattern(regexp="\\D*")
	private String firstName;
	@Size(min=1, max=5)
	@Pattern(regexp="\\D*")
	private String lastName;
	@NotNull
	@Past
	private Date birthday;
	private boolean gender;
	@Min(value=0, message="choose an item please")
	private int workingAge;
	@NotNull(message="至少需要勾选1项")
	private List<Integer> interest;

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
}
