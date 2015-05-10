package org.tinygroup.exception.impl;

import org.tinygroup.exception.CommonError;
import org.tinygroup.exception.ErrorCodeTranslator;

public class SimpleErrorCodeTranslator implements ErrorCodeTranslator {


    public String translate(CommonError commonError) {
        return commonError.getErrorMsg();
    }

    public String getErrorCode(CommonError commonError) {
        return commonError.getErrorCode().toString();
    }

}
