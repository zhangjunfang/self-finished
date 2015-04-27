package global;

import org.jessma.mvc.ActionExecutor;
import org.jessma.mvc.ActionFilter;
import org.jessma.util.LogUtil;
import org.slf4j.Logger;

public class MyActionFilter3 implements ActionFilter
{
	Logger logger = LogUtil.getJessMALogger();
	
	@Override
	public void init()
	{
		logger.debug("> %s.init()", getClass().getName());
	}

	@Override
	public void destroy()
	{
		logger.debug("> %s.destroy()", getClass().getName());
	}

	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		logger.debug(">>> Filter-3");
		String result = executor.invoke();
		logger.debug("Filter-3 <<<");
		
		return result;
	}
}
