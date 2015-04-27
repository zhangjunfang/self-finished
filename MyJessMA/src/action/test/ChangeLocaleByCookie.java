package action.test;

import java.util.Locale;

import org.jessma.mvc.ActionSupport;
import org.jessma.util.GeneralHelper;

public class ChangeLocaleByCookie extends ActionSupport
{
	private static final String LAN			= "lan";
	private static final String LAN_CHINESE	= "chinese";
	private static final String LAN_ENGLISH	= "english";
	private static final String LAN_DELETE	= "none";

	@Override
	public String execute()
	{
		String lan = getParam(LAN);
		
		if(!GeneralHelper.isStrEmpty(lan))
		{
    		if(LAN_CHINESE.compareToIgnoreCase(lan) == 0)
    			setLocaleByCookie(Locale.SIMPLIFIED_CHINESE);
    		else if(LAN_ENGLISH.compareToIgnoreCase(lan) == 0)
    			setLocaleByCookie(Locale.US);
    		else if(LAN_DELETE.compareToIgnoreCase(lan) == 0)
    			setLocaleByCookie(null);
		}
		
		return SUCCESS;
	}
	
	public int[] getSerial()
	{
		// 返回 List
		/*
		final String[] array = {"A", "B", "C", "D", "E", "J", "I", "H", "G", "F"};
		List<String> list = Arrays.asList(array);
		return list;
		*/
		
		// 返回数组
		return new int[] {0, 1, 2, 3, 4, 9, 8, 7, 6, 5};
	}
}
