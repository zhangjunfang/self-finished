package test;

import java.util.Set;

import org.apache.tools.ant.filters.BaseParamFilterReader;
import org.jessma.util.PackageHelper;


@SuppressWarnings("unused")
public class TestPackage
{
	public static void main(String[] args) throws Exception
	{
		test3();
	}
	
	private static void test1() throws Exception
	{
		Set<String> files = PackageHelper.getResourceNames("", true, new PackageHelper.FileTypesFilter("*.xml", "xml", ".dtd", "xsd"));
		
		System.out.println(files.size());
		System.out.println(files);
	}
	
	private static void test2() throws Exception
	{
		Set<Class<?>> classes = PackageHelper.getClasses("org.jessma", false, org.jessma.mvc.Action.class);
		
		System.out.println(classes.size());
		System.out.println(classes);
	}
	
	private static void test3() throws Exception
	{
		Set<String> pkgs = 
			PackageHelper.getPackages("org", ".*\\.util");
			//PackageHelper.getPackages("", "org\\.jessma\\..*");
			//PackageHelper.getPackages("org.apache.tools.ant.filters", ".*");
			//PackageHelper.getPackages("org.*.ext.+");
		System.out.println(pkgs.size());
		System.out.println(pkgs);
	}
}
