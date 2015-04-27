package action.test;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.FormBean;

// 自动装配 Action 属性
@FormBean
public class CheckNickName extends ActionSupport
{
	// 被装配的属性
	private String nickName;
	
	// 处理 Ajax 请求
	@Override
	public String execute() throws Exception
	{
		int code = 1;
		String desc = "昵称可用";
		
		if(nickName.isEmpty())
		{
			code	= -1;
			desc	= "请输入昵称";
		}
		else if(CheckValidate.INVALID_NAMES.contains(nickName.toLowerCase()))
		{
			code	= 0;
			desc	= "昵称已存在";
		}
		
		// 输出 JSON 格式的处理结果
		String result = String.format("{\"code\" : \"%d\", \"desc\" : \"%s\"}", code, desc);
		getResponse().getWriter().write(result);
		
		// 返回 "none" Result Name
		return NONE;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
}
