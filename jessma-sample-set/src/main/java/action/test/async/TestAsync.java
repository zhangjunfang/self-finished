package action.test.async;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.jessma.mvc.ActionSupport;
import org.jessma.mvc.ActionTask;
import org.jessma.mvc.FormBean;
import org.jessma.mvc.Result;
import org.jessma.mvc.Results;
import org.jessma.util.GeneralHelper;

@FormBean
public class TestAsync extends ActionSupport
{
	long timeout 	= -1;
	long tasktime	= 0;
	boolean async	= true;
	String flag		= null;
	
	// Action 入口方法
	// 所有结果（'success'/'timeout'/'error'） 都跳转到 test-async.jsp
	// 其中，'success' 通过 Action Convention 机制自动跳转，无需声明
	@Override
	@Results({
		@Result(value=TIMEOUT, path="${jsp-path}/async/test-async.jsp"),
		@Result(value=ERROR, path="${jsp-path}/async/test-async.jsp")
	})
	public String execute() throws Exception
	{
		String result = NONE;

		if(async)
			sysPrint("##### async action entry #####");
		else
			sysPrint("##### normal action entry #####");
		
		sysPrint("$ begin execute() ...");
		
		if(async)	// 异步执行
			startAsync(new LongTask(tasktime), TimeUnit.SECONDS.toMillis(timeout), new TaskListener());
		else		// 同步执行
			result = new LongTask(tasktime).run();
		
		sysPrint("$ end execute() !");
		
		return result;
	}
	
	// 异步任务
	private class LongTask extends ActionTask
	{
		long tasktime;
		
		public LongTask(long tasktime)
		{
			this.tasktime = tasktime;
		}

		// 任务入口方法
		@Override
		@SuppressWarnings("unchecked")
		public String run()
		{
			sysPrint("  > begin run task ...");
			GeneralHelper.waitFor(tasktime, TimeUnit.SECONDS);
			sysPrint("  > end run task !");
			
			return SUCCESS;
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
