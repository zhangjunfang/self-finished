package org.tinygroup.weblayer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.weblayer.TinyProcessorConfig;
import org.tinygroup.weblayer.config.ServletMapping;
import org.tinygroup.weblayer.config.TinyProcessorConfigInfo;

/**
 * 默认实现
 * 
 * @author renhui
 * 
 */
public class DefaultTinyProcessorConfig extends SimpleBasicTinyConfig
		implements TinyProcessorConfig {

	// 存放映射正则表达式列表
	private List<Pattern> patterns = new ArrayList<Pattern>();
	// 存放正则表达式的字符串格式
	private List<String> patternStrs = new ArrayList<String>();

	public DefaultTinyProcessorConfig(TinyProcessorConfigInfo config) {
		super(config.getConfigName(),config);
		setProcessorConfig(config);
	}

	private void setProcessorConfig(TinyProcessorConfigInfo config) {
		this.parameterMap = config.getParameterMap();
		List<ServletMapping> filterMappings = config.getServletMappings();
		for (ServletMapping filterMapping : filterMappings) {
			String urlPattern = filterMapping.getUrlPattern();
			if (!patternStrs.contains(urlPattern)) {
				patterns.add(Pattern.compile(urlPattern));
				patternStrs.add(urlPattern);
			}
			logger.logMessage(LogLevel.DEBUG, "<{}>的url-pattern:'{}'",
					configName, urlPattern);
		}
	}

	public boolean isMatch(String url) {
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				logger.logMessage(LogLevel.DEBUG,
						"请求路径：<{}>,匹配的tiny-processor:<{}>", url, configName);
				return true;
			}
		}
		return false;
	}

}
