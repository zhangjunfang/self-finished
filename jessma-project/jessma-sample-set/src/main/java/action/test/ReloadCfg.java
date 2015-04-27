package action.test;

import java.util.ResourceBundle;

import org.jessma.app.AppConfig;
import org.jessma.ext.rest.RestDispatcher;
import org.jessma.mvc.ActionDispatcher;
import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.Result;

public class ReloadCfg extends ActionSupport
{
	@Override
	@Result(path="${index}")
	public String execute() throws Exception
	{
		// 获取 ActionDispatcher 实例
		ActionDispatcher mvcDisp = ActionDispatcher.instance();
		// 获取 RestDispatcher 实例
		RestDispatcher restDisp	 = RestDispatcher.instance();
		// 获取 AppConfig 实例
		AppConfig appCfg		 = AppConfig.instance();
		
		final long DELAY = 3000L;
		String type = getParam("type");
		
		// 更新 MVC 配置
		if(type.equals("mvc"))
		{
			try
			{
				mvcDisp.pause();
				mvcDisp.reload(DELAY);
			}
			finally
			{
				mvcDisp.resume();
			}
		}
		// 更新 REST 配置
		else if(type.equals("rest"))
		{
			try
			{
				restDisp.pause();
				restDisp.reload(DELAY);
			}
			finally
			{
				restDisp.resume();
			}
		}
		// 更新用户自定义配置
		else if(type.equals("user"))
		{
			try
			{
				mvcDisp.pause();
				restDisp.pause();
				appCfg.reloadUserConfig(DELAY);
			}
			finally
			{
				mvcDisp.resume();
				restDisp.resume();
			}
		}
		// 更新国际化资源文件
		else if(type.equals("res"))
		{
			ResourceBundle.clearCache();
		}
		// 更新所有配置
		else if(type.equals("all"))
		{
			try
			{
				mvcDisp.pause();
				restDisp.pause();
				appCfg.reloadUserConfig(DELAY);
				restDisp.reload(0L);
				mvcDisp.reload(0L);
				ResourceBundle.clearCache();
			}
			finally
			{
				mvcDisp.resume();
				restDisp.resume();
			}
		}

		return SUCCESS;
	}
}
