package action.test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jessma.mvc.ActionSupport;
import org.jessma.util.BeanHelper;
import org.jessma.util.LogUtil;
import org.jessma.util.http.FileUploader;
import org.jessma.util.http.FileUploader.FileInfo;
import org.jessma.util.http.FileUploader.Result;

import vo.Person;

public class CheckUpload extends ActionSupport
{
	// 上传路径
	private static final String UPLOAD_PATH		= "upload";
	// 可接受的文件类型
	private static final String[] ACCEPT_TYPES	= {"pdf", ".Jpg", "PNG", "*.BMP"};
	// 总上传文件大小限制 ：10M
	private static final long MAX_SIZE			= 1024 * 1024 * 10;
	// 单个传文件大小限制：5M
	private static final long MAX_FILE_SIZE		= 1024 * 1024 * 5;
	
	private Person person;
	private String cause;
	
	public Person getPerson()
	{
		return person;
	}

	public String getCause()
	{
		return cause;
	}

	@Override
	public String execute()
	{
		// 创建 FileUploader 对象
		FileUploader fu = new FileUploader(UPLOAD_PATH, ACCEPT_TYPES, MAX_SIZE, MAX_FILE_SIZE);
		
		// 根据实际情况设置对象属性（可选）
		/*
		// 自定义 FileNameGenerator
		fu.setFileNameGenerator(new FileNameGenerator()
		{
			@Override
			public String generate(FileItem item, String suffix)
			{
				return String.format("%d_%d", item.hashCode(), item.get().hashCode());
			}
		});
		
		// 设置上传进度监听器
		fu.setServletProgressListener(new ProgressListener()
		{
			@Override
			public void update(long pBytesRead, long pContentLength, int pItems)
			{
				System.out.printf("%d: length -> %d, read -> %d.\n", pItems, pContentLength, pBytesRead);
			}
		});
		*/
		
		// 执行上传并获取操作结果
		Result result = fu.upload(getRequest(), getResponse());
		
		// 检查操作结果
		if(result != FileUploader.Result.SUCCESS)
		{
			// 获取失败描述
			cause = fu.getCause().toString();
			// 记录日志
			LogUtil.exception(fu.getCause(), "upload file fail", false);
			
			return FAIL;
		}
		
		// 通过非文件表单域创建 VO
		person	= BeanHelper.createBean(Person.class, fu.getParamFields());
		
		// 附件名称列表
		List<String> photos	= new ArrayList<String>();
		/* 轮询文件表单域，填充 photos 列表 */
		Set<String> keys = fu.getFileFields().keySet();
		for(String key : keys)
		{
			FileInfo[] ffs = fu.getFileFields().get(key);
			for(FileInfo ff : ffs)
			{
				photos.add(String.format("[%s] %s%s%s", key, fu.getSavePath(), File.separator, ff.getSaveFile().getName()));
			}
		}
		
		// 设置 VO 的 photos 属性
		person.setPhotos(photos);
		
		return SUCCESS;
	}
}
