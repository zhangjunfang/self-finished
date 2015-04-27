package action.test;

import vo.Person;
import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

// CheckBean2 类中声明 @FormBean("person") 注解
// 表单将与 CheckBean2 的 person 属性自动装配
@FormBean("person")
public class CheckBean2 extends ActionSupport
{
	// 待装配的属性
	private Person person;
	
	// 结果页面中读取 person 属性时需要 getter 方法
	public Person getPerson()
	{
		return person;
	}

	// 执行装配时需要 setter 方法
	public void setPerson(Person person)
	{
		this.person = person;
	}

	// 进入 execute() 方法前，自动装配已完成
	@Override
	public String execute()
	{
		return SUCCESS;
	}
}
