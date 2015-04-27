package test;

import org.jessma.util.archive.UnZipper;
import org.jessma.util.archive.Zipper;

public class TestZip
{
	private static final String BASE = "E:\\Downloads\\test\\";
	
	public static void main(String[] args)
	{
		testZip();
		testUnZip();
	}
	
	private static void testZip()
	{
		final String SRC = BASE + "测试文件夹";
		final String DEST = null;
		final String INC = null;
		final String EXC = "*Web*";
		final String ENC = Zipper.DEFAULT_ENCODING;
		final String COM = "ZIP 文件压缩测试！！";
		
		// 创建 Zipper 对象
		Zipper zip = new Zipper(SRC, DEST);
		
		// 设置参数
		zip.setIncludes(INC);
		zip.setExcludes(EXC);
		zip.setEncoding(ENC);
		zip.setComment(COM);
		
		// 执行压缩
		if(zip.execute())
			System.out.println("OK!");
		else
			System.err.println(zip.getCause());
	}
	
	private static void testUnZip()
	{
		final String SRC = BASE + "测试文件夹.zip";
		final String DEST = BASE + "解压/zip";
		final String INC = null;
		final String EXC = "**/*.h";
		final String ENC = UnZipper.DEFAULT_ENCODING;
		final boolean OVW = true;
		
		// 创建 UnZipper 对象
		UnZipper zip = new UnZipper(SRC, DEST);
		
		// 设置参数
		zip.setIncludes(INC);
		zip.setExcludes(EXC);
		zip.setEncoding(ENC);
		zip.setOverwrite(OVW);
		
		// 执行解压
		if(zip.execute())
			System.out.println("OK!");
		else
			System.err.println(zip.getCause());
		
	}
}
