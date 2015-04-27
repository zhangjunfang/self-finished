package action.test;

import java.util.HashSet;
import java.util.Set;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

// 自动装配 Action 属性
@FormBean
public class CheckValidate extends ActionSupport
{
	// 被装配的属性
	private String nickName;
	
	// Request Attr
	private static final String ERR_NICK_ATTR = "nickname";
	private static final String ERR_DESC_ATTR = "errdesc";
	// 已存在的昵称
	static final Set<String> INVALID_NAMES;
	
	static
	{
		INVALID_NAMES = new HashSet<String>();
		INVALID_NAMES.add("bruce");
		INVALID_NAMES.add("kingfisher");
		INVALID_NAMES.add("怪兽");
	}
	
	// 输入校验方法
	@Override
	public boolean validate()
	{
		String errorDesc = null;
		
		if(nickName.isEmpty())
			errorDesc = "请输入昵称！";
		else if(INVALID_NAMES.contains(nickName.toLowerCase()))
			errorDesc = "昵称已存在，请重新输入！";
		
		// 设置 Request Attr
		setRequestAttribute(ERR_NICK_ATTR, nickName);
		setRequestAttribute(ERR_DESC_ATTR, errorDesc);
		
		return errorDesc == null;
	}
	
	// 这个简单例子并不需要改写 execute() 方法
	// @Override
	// public String execute() throws Exception
	// {
	// 	return super.execute();
	// }

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
}
