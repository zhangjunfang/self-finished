package org.tinygroup.weblayer.config;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tiny-wrapper-filter")
public class TinyWrapperFilterConfigInfo extends TinyFilterConfigInfo {

	private static String FILTER_BEAN_NAME = "filter_beans";

	public String getFilterBeanName() {
		String filterBean = getParameterMap().get(FILTER_BEAN_NAME);
		Assert.assertTrue(!StringUtil.isBlank(filterBean),
				"the filter_beans property of value must not null or empty");
		return filterBean;
	}

}
