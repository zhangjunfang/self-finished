package org.tinygroup.context2object.test.testcase;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.ObjectGenerator;
import org.tinygroup.context2object.User;
import org.tinygroup.context2object.impl.ClassNameObjectGenerator;
import org.tinygroup.context2object.impl.DateTypeConverter;
import org.tinygroup.context2object.impl.DetectDateTypeConverter;
import org.tinygroup.tinytestutil.AbstractTestUtil;

import junit.framework.TestCase;

/**
 * 功能说明：<br/>
 * DetectDateTypeConverter 测试类
 * </br>
 * 开发人员：zhengkk(zhengkk@strongit.com.cn)<br/>
 * 开发时间：2015年3月5日<br/>
 */
public class testDetectDateTypeConverter extends TestCase {
    protected ObjectGenerator generator = new ClassNameObjectGenerator();

    protected void setUp() {
        generator.addTypeCreator(new ListCreator());
        generator.addTypeConverter(new DetectDateTypeConverter());
        AbstractTestUtil.init(null, true);
    }

    public void testGetObjectWithDateCn() throws ParseException {
        Context context = new ContextImpl();
        context.put("birthday", "1999-03-03");
        User user = (User) generator.getObject(null,null,User.class.getName(),this.getClass().getClassLoader(), context);
        assertEquals(user.getBirthday(),new SimpleDateFormat("yyyy-MM-dd").parse("1999-03-03"));
    }
    public void testGetObjectWithDateZh() throws ParseException {
        Context context = new ContextImpl();
        context.put("birthday", "1999-03-03 12:06:52");
        User user = (User) generator.getObject(null,null,User.class.getName(),this.getClass().getClassLoader(), context);
        assertEquals(user.getBirthday(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1999-03-03 12:06:52"));
    }
    public void testGetObjectWithDateCnTime() throws ParseException {
        Context context = new ContextImpl();
        context.put("birthday", "1999年03月03日");
        User user = (User) generator.getObject(null,null,User.class.getName(),this.getClass().getClassLoader(), context);
        assertEquals(user.getBirthday(),new SimpleDateFormat("yyyy年MM月dd日").parse("1999年03月03日"));
    }
    public void testGetObjectWithDateZhTime() throws ParseException {
        Context context = new ContextImpl();
        context.put("birthday", "1999年03月03日  12:06:52");
        User user = (User) generator.getObject(null,null,User.class.getName(),this.getClass().getClassLoader(), context);
        assertEquals(user.getBirthday(),new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").parse("1999年03月03日 12:06:52"));
    }

}
