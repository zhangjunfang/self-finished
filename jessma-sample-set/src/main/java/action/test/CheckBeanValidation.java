package action.test;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;
import org.jessma.mvc.Result;
import org.jessma.mvc.Results;

import vo.Person2;

// CheckBean2 类中声明 @FormBean("person") 注解
// 表单将与 ValidateBean 的 person 属性自动装配
@FormBean("person")
@Results({
			@Result(value="success", path="/jsp/test/bean/result_vld.jsp"),
			@Result(value="input", path="/jsp/test/bean/test_bean_vld.jsp")
		})
public class CheckBeanValidation extends ActionSupport
{
	// 待装配的属性
	private Person2 person;
	
	// 结果页面中读取 person 属性时需要 getter 方法
	public Person2 getPerson()
	{
		return person;
	}

	// 执行装配时需要 setter 方法
	public void setPerson(Person2 person)
	{
		this.person = person;
	}

	// 进入 execute() 方法前，自动装配并验证 Form Bean
	@Override
	public String execute()
	{
		return SUCCESS;
	}
	
	// 进入 run() 方法前，自动装配但不验证 Form Bean
	// 此时，通过 validate() 方法手工验证 Form Bean
	@FormBean(value="person", validate=false)
	public String run()
	{
		return SUCCESS;
	}
	
	// 手工验证 Form Bean
	@Override
	public boolean validate()
	{
		if(!isAutoValidation())
		{		
			/*
			boolean valid = true;
			if(person.getFirstName() == null)
			{
				addError("firstName", "不能为空");
				valid = false;
			}
			...... ......
			...... ......
			*/
			
			// 调用 validateBeanAndAddErrors() 简化验证操作
			return validateBeanAndAddErrors(getFormBean());
		}
		
		return true;
	}
}
