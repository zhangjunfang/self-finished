package action.test;

import org.jessma.mvc.ActionSupport;

public class IndexAction extends ActionSupport
{
	@Override
	public String execute() throws Exception
	{
		/*
		Locale locale = getLocale();
		if(locale != Locale.US)
			setLocale(Locale.US);
		*/
		
		Integer times	= getSessionAttribute("times");
		int ts			= times == null ? 1 : times + 1;
		
		setSessionAttribute("times", ts);
		
		return SUCCESS;
	}
}
