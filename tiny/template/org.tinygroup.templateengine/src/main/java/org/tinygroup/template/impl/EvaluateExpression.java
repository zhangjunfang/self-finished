package org.tinygroup.template.impl;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

/**
 * Created by luoguo on 2014/8/3.
 */
public interface EvaluateExpression {
    Object evaluate(TemplateContext context) throws TemplateException;
}
