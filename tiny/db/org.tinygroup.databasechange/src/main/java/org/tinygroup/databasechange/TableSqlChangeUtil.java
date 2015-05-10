package org.tinygroup.databasechange;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.databasebuinstaller.DatabaseInstallerProcessor;
import org.tinygroup.tinytestutil.AbstractTestUtil;

/**
 * 表格结果变更信息记录的工具类
 * 
 * @author renhui
 * 
 */
public class TableSqlChangeUtil {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.exit(0);
		}
		String fileName = args[0];
		AbstractTestUtil.init(null, true);// 启动框架
		DatabaseInstallerProcessor processor = BeanContainerFactory
				.getBeanContainer(TableSqlChangeUtil.class.getClassLoader())
				.getBean("databaseInstallerProcessor");
		Map<Class, List<String>> processSqls = processor.getChangeSqls();
		StringBuilder builder = new StringBuilder();
		for (Class clazz : processSqls.keySet()) {
			builder.append("//-----").append(clazz.getSimpleName())
					.append("-----").append("\n\r");
			List<String> sqls = processSqls.get(clazz);
			for (String sql : sqls) {
				builder.append(sql).append("\n\r");
			}
			builder.append("//-----").append(clazz.getSimpleName())
					.append("-----").append("\n\r");
			builder.append("\n\r");
		}
		StreamUtil.writeText(builder, new FileWriter(new File(fileName)), true);
	}

}
