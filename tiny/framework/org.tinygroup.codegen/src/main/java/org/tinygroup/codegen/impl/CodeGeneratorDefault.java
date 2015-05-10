package org.tinygroup.codegen.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.codegen.CodeGenerator;
import org.tinygroup.codegen.config.CodeGenMetaData;
import org.tinygroup.codegen.config.MacroDefine;
import org.tinygroup.codegen.config.TemplateDefine;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.docgen.DocumentGenerater;
import org.tinygroup.docgen.config.GenUtilConfig;
import org.tinygroup.docgen.config.StaticClass;
import org.tinygroup.docgen.function.StaticClassFunction;
import org.tinygroup.docgen.impl.DocumentGeneraterImpl;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import com.thoughtworks.xstream.XStream;

public class CodeGeneratorDefault implements CodeGenerator{

private static Logger logger = LoggerFactory.getLogger(CodeGeneratorDefault.class);

	//public static FullContextFileRepository repository;//暂时定为静态
	private DocumentGenerater<TemplateEngine> generater;
	
	
	public CodeGeneratorDefault(){
		generater=new DocumentGeneraterImpl();
		TemplateEngine templateEngine = new TemplateEngineDefault();
		templateEngine.setEngineId("tiny_code");
		generater.setTemplateGenerater(templateEngine);
	}
	

	/**
	 * @param context
	 *            默认文件需要用到的上下文环境
	 * @throws IOException 
	 */
	public List<String> generate(Context context) throws IOException{
		CodeGenMetaData metaData=context.get(CODE_META_DATA);
		if(metaData==null){
			throw new RuntimeException("代码生成器的元数据不存在");
		}
		addUtilClass(generater.getTemplateGenerater());
		List<MacroDefine> macroDefines=metaData.getMacroDefines();
		for (MacroDefine macroDefine : macroDefines) {
		   logger.logMessage(LogLevel.INFO, "开始加载宏文件路径：{0}",macroDefine.getMacroPath());
		   String macroPath=macroDefine.getMacroPath();
		   if (!StringUtils.startsWith(macroPath, "/")) {
			   macroPath = "/"+macroPath;
		   }
		   FileObject fileObject=VFS.resolveFile(String.valueOf(context.get(ABSOLUTE_PATH)));
		   generater.addMacroFile(fileObject.getFileObject(macroPath));
		   logger.logMessage(LogLevel.INFO, "宏文件路径：{0}，加载完毕",macroDefine.getMacroPath());
		}
		List<TemplateDefine> templateDefines=metaData.getTemplateDefines();
		List<String> fileList=new ArrayList<String>();
		for (TemplateDefine templateDefine : templateDefines) {
			Context newContext=createNewContext(context, templateDefine);
			String templatePath=templateDefine.getTemplatePath();
			logger.logMessage(LogLevel.INFO, "开始加载模板文件路径：{0}",templatePath);
//			if(templatePath.startsWith("/")){
//				templatePath=CodeGenerator.class.getResource(templatePath).getPath();
//			}
			FileObject templateDirObject=VFS.resolveFile(String.valueOf(context.get(ABSOLUTE_PATH)));
			logger.logMessage(LogLevel.INFO, "模板文件路径：{0}，加载完毕",templatePath);
			String filePath = templatePath;
			if (!StringUtils.startsWith(templatePath, "/")) {
				filePath = "/"+templatePath;
			}
			//repository.addFileObject(templatePath, templateFileObject.getFileObject(filePath));
			FileObject templateFileObject = templateDirObject.getFileObject(templatePath);
			String generateFile=generater.evaluteString(newContext, templateDefine.getFileNameTemplate());
			File file=new File(generateFile);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			try {
				generater.generate(templateFileObject, newContext,writer);
			}finally{
				writer.close();
			}
			fileList.add(file.getPath());
		}
		return fileList;
		
	}
	
	private void addUtilClass(TemplateEngine templateEngine) {
		XStream stream = new XStream();
		stream.setClassLoader(getClass().getClassLoader());
		stream.autodetectAnnotations(true);
		stream.processAnnotations(GenUtilConfig.class);
		GenUtilConfig config=(GenUtilConfig)stream.fromXML(getClass().getResourceAsStream("/codegen.util.xml"));
		if(config.getStaticClasses()!=null){
		   for(StaticClass staticClass:config.getStaticClasses()){
			   try {
				   StaticClassFunction function = new StaticClassFunction(staticClass.getName(),staticClass.getClassName());
				   generater.getTemplateGenerater().addTemplateFunction(function);
			   } catch (Exception e) {
				   logger.logMessage(LogLevel.ERROR, "静态方法类：{0}，实例化失败",staticClass.getClassName());
			   }
		   }
		}
	}

	private Context createNewContext(Context context,TemplateDefine templateDefine){
		Context newContext=ContextFactory.getContext();
		newContext.setParent(context);
		newContext.put(TEMPLATE_FILE, templateDefine);
		String templatePath=templateDefine.getTemplatePath();
		String templateFilePath = templatePath.replaceAll("/", "\\"+File.separator);//把/../..路径转化成系统认知的路径格式
		String path = StringUtil.substringBeforeLast(templateFilePath, File.separator);
		if(path.startsWith(File.separator)){
			path=path.substring(1);
		}
		if(path.trim().length()>0&&!path.endsWith(File.separator)){
			path=path+File.separator;
		}
		newContext.put("templateFilePath", path);
		String fileName=StringUtil.substringAfterLast(templateFilePath, File.separator);
		newContext.put("templateFileName", StringUtil.substringBeforeLast(fileName, "."));
		return newContext;
	}
}
