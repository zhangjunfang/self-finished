package global;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jessma.mvc.Action.BaseType;
import org.jessma.mvc.Action.Constant;
import org.jessma.util.http.HttpHelper;

import freemarker.ext.servlet.FreemarkerServlet;

@SuppressWarnings("serial")
public class MyFreemarkerServlet extends FreemarkerServlet
{
	@Override
	protected boolean preprocessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		setBasePath(request);
		return false;
	}

	private void setBasePath(HttpServletRequest request)
	{
		BaseType baseType = (BaseType)HttpHelper.getApplicationAttribute(Constant.APP_ATTR_BASE_TYPE);

		if(baseType == BaseType.AUTO)
		{
			String __base = (String)request.getAttribute(Constant.REQ_ATTR_BASE_PATH);
			
			if(__base == null)
			{
				__base = HttpHelper.getRequestBasePath(request);
				request.setAttribute(Constant.REQ_ATTR_BASE_PATH, __base);
			}
		}
	}
}
