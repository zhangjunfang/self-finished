/*
 * Copyright Bruce Liang (ldcsaa@gmail.com)
 *
 * Version	: JessMA 3.5.1
 * Author	: Bruce Liang
 * Website	: http://www.jessma.org
 * Project	: http://www.oschina.net/p/portal-basic
 * Blog		: http://www.cnblogs.com/ldcsaa
 * WeiBo	: http://weibo.com/u/1402935851
 * QQ Group	: 75375912
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jessma.mvc.validation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.jessma.mvc.BeanValidator;
import org.jessma.util.GeneralHelper;

/** 基于 Hibernate Validator 的 JSR 303 验证器 */
public class HibernateBeanValidator implements BeanValidator
{
	private Map<ValidatorKey, Validator> validatorMap;
	
	@Override
	public void init()
	{
		validatorMap = new HashMap<ValidatorKey, Validator>();
	}

	@Override
	public void destroy()
	{
		validatorMap.clear();
	}

	@Override
	public Set<ConstraintViolation<Object>> validate(final Object bean, final Class<?>[] groups, final String bundle, final Locale locale)
	{
		ValidatorKey key	= new ValidatorKey(groups, bundle, locale);		
		Validator validator	= validatorMap.get(key);
		
		if(validator == null)
			validator = tryCreateValidator(bundle, locale, key);
		
		return validator.validate(bean, groups);
	}

	private Validator tryCreateValidator(final String bundle, final Locale locale, ValidatorKey key)
	{
		
		HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();

		configure.messageInterpolator(new ResourceBundleMessageInterpolator(
			new PlatformResourceBundleLocator(bundle))
		{
			@Override
			public String interpolate(String message, Context context)
			{
				return super.interpolate(message, context, locale);
			}
		});
		
		ValidatorFactory vf = configure.buildValidatorFactory();
		Validator validator = vf.getValidator();
		
		GeneralHelper.syncTryPut(validatorMap, key, validator);
		return validatorMap.get(key);
	}
}
