package action.test;

import java.util.HashMap;
import java.util.Map;

import vo.Person;
import org.jessma.mvc.ActionSupport;

public class CheckBean1 extends ActionSupport
{
	@Override
	public String execute()
	{
		// 如果表单元素的名称和 Form Bean 属性名不一致则使用 keyMap 进行映射
		// key: 表单元素名称, value: Form Bean 属性名
		Map<String, String> keyMap = new HashMap<String, String>();
		keyMap.put("working-Age", "workingAge");
		keyMap.put("its", "interest");
		
		// 使用表单元素创建 Form Bean
		// 如果表单元素的名称和 Form Bean 属性名完全一致则不需使用 keyMap 进行映射
		Person p = createFormBean(Person.class, keyMap);
		
		/* 或者这样使用: 先创建 Form Bean 对象, 然后再填充它的属性 */
		//Person p = new Person();
		//fillFormBeanProperties(p, keyMap);
		
		// 设置 Request Attr
		setRequestAttribute("person", p);
		
		return SUCCESS;
	}
}
