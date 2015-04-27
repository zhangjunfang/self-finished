package global;

import org.jessma.mvc.AbstractActionFilter;
import org.jessma.mvc.ActionExecutor;
import org.jessma.util.LogUtil;
import org.slf4j.Logger;

public class UserActionFilter extends AbstractActionFilter
{
	Logger logger = LogUtil.getJessMALogger();
	
	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		logger.debug(">>> User Action Filter (%s#%s())", 
		executor.getAction().getClass().getName(), 
		executor.getEntryMethod().getName());
		String result = executor.invoke();
		logger.debug("User Action Filter <<<");
		
		return result;
	}
}
