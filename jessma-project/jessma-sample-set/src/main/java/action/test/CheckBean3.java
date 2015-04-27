package action.test;

import java.util.Date;
import java.util.List;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

public class CheckBean3 extends ActionSupport
{
	// 待装配的属性
	private String firstName;
	private String lastName;
	private Date birthday;
	private boolean gender;
	private int workingAge;
	private List<Integer> interest;
	private List<String> photos;
	
	// 必须提供待装配属性的 get / set 方法
	// getters & setters
	// （略）......
	
	/* **************************************** */
	/* **** 进入 execute() 方法前，自动装配已完成 ***** */
	
	// CheckBean3 的入口方法 'execute()' 中声明 @FormBean 注解
	// 注意：没有注解参数，表单将与 CheckBean3 的相应属性自动装配
	@FormBean
	@Override
	public String execute()
	{
		return SUCCESS;
	}

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
