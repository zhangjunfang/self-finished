package org.tinygroup.validate;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.validate.impl.ValidateResultImpl;

import junit.framework.TestCase;

public class ValidateTest  extends TestCase{
    static{
        AbstractTestUtil.init(null, true);
        annotationValidatorManager = BeanContainerFactory.getBeanContainer(ValidateTest.class.getClassLoader()).
        getBean("annotationValidatorManager");
    }

    private static ValidatorManager annotationValidatorManager;

    
    public void testAnnotationValidate(){
        UserBean bean = new UserBean();
        bean.setUserName("12");
        bean.setPasswd("1234");
        bean.setObid("123456");
        ValidateResult result = new ValidateResultImpl();
        annotationValidatorManager.validate(bean, result);

        for(ErrorDescription errorMsg : result.getErrorList()){
            System.out.println(errorMsg.getDescription());
        }

        assertFalse(result.hasError());
    }
}
