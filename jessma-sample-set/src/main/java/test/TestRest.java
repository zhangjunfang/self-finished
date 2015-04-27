package test;

import java.util.Date;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import org.jessma.ext.rest.Get;
import org.jessma.ext.rest.Put;
import org.jessma.ext.rest.RequestType;
import org.jessma.ext.rest.Rest;
import org.jessma.ext.rest.RestActionSupport;
import org.jessma.ext.rest.RestContext;
import org.jessma.ext.rest.RestResult;
import org.jessma.ext.rest.renderer.RenderType;
import org.jessma.util.GeneralHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("unused")
public class TestRest
{
	public static class MyAction extends RestActionSupport
	{
		@Put({"/{3}/{0};{1},{2}", "/{2}/{1}"})
		public RestResult myMethod(int iv, List<Double> lv, String sv)
		{
			return new MyResult(sv, lv);
		}
		
		@Override
		public RestResult edit(int id) throws Exception
		{
			return new MyResult("edit_success", id);
		}
		
		/*
		@Get("/{0}/edit")
		public RestResult myEdit(Date dt) throws Exception
		{
			return new MyResult("edit_success", dt);
		}
		*/
	}
	
	@XStreamAlias("My-Rest")
	public static class MyResult extends RestResult
	{
		Date date;
		
		public MyResult(String result, Object model)
		{
			super(result, model);
			date = new Date();
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		MockServletContext servletContext	= new MockServletContext();
		MockHttpServletRequest request		= new MockHttpServletRequest();
		MockHttpServletResponse response	= new MockHttpServletResponse();
		
		response.setCharacterEncoding(GeneralHelper.DEFAULT_ENCODING);

		RestContext context = new RestContext(RequestType.PUT, "/aaa/111;12|34|56,伤神小怪兽", RenderType.XML);
		
		MyAction action = new MyAction();
		action.setServletContext(servletContext);
		action.setRequest(request);
		action.setResponse(response);
		
		action.setRequestAttribute(Rest.Constant.REQ_ATTR_REST_CONTEXT, context);
		action.execute();
		
		response.getWriter().println("\n-------------------------");
		
		context = new RestContext(RequestType.PUT, "/大家好/12|34|56", RenderType.JSON);
		action.setRequestAttribute(Rest.Constant.REQ_ATTR_REST_CONTEXT, context);
		action.execute();
		
		response.getWriter().println("\n-------------------------");
		
		context = new RestContext(RequestType.GET, "/173/edit", RenderType.XML);
		action.setRequestAttribute(Rest.Constant.REQ_ATTR_REST_CONTEXT, context);
		action.execute();
		
		String result = response.getContentAsString();
		System.out.println(result);
	}


}
