package action.test.async;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.jessma.ext.rest.Get;
import org.jessma.ext.rest.RestActionSupport;
import org.jessma.ext.rest.RestActionTask;
import org.jessma.ext.rest.RestResult;
import org.jessma.mvc.Action;
import org.jessma.mvc.Result;
import org.jessma.mvc.Results;
import org.jessma.util.GeneralHelper;

// 所有结果（'success'/'timeout'/'error'） 都跳转到 test-rest.jsp
// 其中，'success' 通过 Action Convention 机制自动跳转，无需声明
@Results({
	@Result(value=Action.TIMEOUT, path="${jsp-path}/async/test-rest.jsp"),
	@Result(value=Action.ERROR, path="${jsp-path}/async/test-rest.jsp")
})
public class TestRest extends RestActionSupport
{
	long timeout;
	long tasktime;
	boolean async;
	String flag;

	// REST 入口方法
	@Get({"/", "/{0}/{1}/{2}"})
	public RestResult process(long timeout, long tasktime, boolean async)
	{
		this.timeout	= timeout;
		this.tasktime	= tasktime;
		this.async		= async;
		
		RestResult result = REST_NONE;

		if(async)
			sysPrint("##### async REST action entry #####");
		else
			sysPrint("##### normal REST action entry #####");
		
		sysPrint("$ begin execute() ...");
		
		if(async)	// 异步执行
			startAsync(new LongTask(tasktime), TimeUnit.SECONDS.toMillis(timeout), new TaskListener());
		else		// 同步执行
			result = new LongTask(tasktime).run();
		
		sysPrint("$ end execute() !");
		
		return result;
	}
	
	// 异步任务
	private class LongTask extends RestActionTask
	{
		long tasktime;
		
		public LongTask(long tasktime)
		{
			this.tasktime = tasktime;
		}

		// 任务入口方法
		@Override
		@SuppressWarnings("unchecked")
		public RestResult run()
		{
			sysPrint("  > begin run task ...");
			GeneralHelper.waitFor(tasktime, TimeUnit.SECONDS);
			sysPrint("  > end run task !");

			return new RestResult(SUCCESS, hashCode());
		}
	}
	
	// 异步任务监听器
	private class TaskListener implements AsyncListener
	{
		@Override
		public void onComplete(AsyncEvent event) throws IOException
		{
			sysPrint("    >> onComplete() !");
		}

		@Override
		public void onTimeout(AsyncEvent event) throws IOException
		{
			sysPrint("    >> onTimeout() !");
			setFlag("timeout");
		}

		@Override
		public void onError(AsyncEvent event) throws IOException
		{
			sysPrint("    >> onError() !");
			setFlag("error");
		}

		@Override
		public void onStartAsync(AsyncEvent event) throws IOException
		{
			sysPrint("    >> onStartAsync() !");			
		}
		
	}
	
	private void setFlag(String value)
	{
		if(GeneralHelper.isStrEmpty(flag))
		{
			flag = value;
		}
	}
	
	public String getFlag()
	{
		return flag;
	}
	
	public long getTasktime()
	{
		return tasktime;
	}
	
	public long getTimeout()
	{
		return timeout;
	}
	
	public boolean isAsync()
	{
		return async;
	}
	
	private void sysPrint(Object obj)
	{
		System.out.println(String.format("%8d: %s", hashCode(), obj));
	}
}
