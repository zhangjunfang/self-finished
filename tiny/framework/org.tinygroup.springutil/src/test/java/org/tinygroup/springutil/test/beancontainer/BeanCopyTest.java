package org.tinygroup.springutil.test.beancontainer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class BeanCopyTest extends TestCase {

	public void testContainer() {
		SpringBeanContainer sbc = new SpringBeanContainer();
		FileObject f = VFS.resolveURL(this.getClass().getClassLoader()
				.getResource("beancontainer.beans.xml"));
		List<FileObject> fl = new ArrayList<FileObject>();
		fl.add(f);
		sbc.regSpringConfigXml(fl);
		sbc.refresh();
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = (FileSystemXmlApplicationContext) sbc
				.getBeanContainerPrototype();
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) fileSystemXmlApplicationContext
				.getBeanFactory();
		BeanDefinition beanDefinition = defaultListableBeanFactory
				.getBeanDefinition("containerbean");
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(beanDefinition.getBeanClassName());
		MutablePropertyValues attributes = beanDefinition.getPropertyValues();
		builder.setScope("singleton");
		for (Object value : attributes.getPropertyValueList()) {
			PropertyValue propertyValue = (PropertyValue) value;
			builder.addPropertyValue(propertyValue.getName(),
					propertyValue.getValue());
		}
		assertNotNull(beanDefinition);
		System.out.println(defaultListableBeanFactory);
		defaultListableBeanFactory.registerBeanDefinition("aaa",builder.getBeanDefinition());
		assertNotSame( fileSystemXmlApplicationContext.getBean("aaa"), fileSystemXmlApplicationContext.getBean("containerbean"));
		
	}
}
