package global;

import org.jessma.mvc.AbstractActionFilter;
import org.jessma.mvc.ActionExecutor;
import org.jessma.util.LogUtil;
import org.slf4j.Logger;

public class MyActionFilter1 extends AbstractActionFilter
{
	Logger logger = LogUtil.getJessMALogger();
	
	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		logger.debug(">>> Filter-1");
		String result = executor.invoke();
		logger.debug("Filter-1 <<<");
		
		return result;
	}
}
