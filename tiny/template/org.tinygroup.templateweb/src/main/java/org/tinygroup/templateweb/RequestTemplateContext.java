package org.tinygroup.templateweb;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.template.TemplateContext;

/**
 * request包装的上下文对象
 * 
 * @author renhui
 * 
 */
public class RequestTemplateContext extends ContextImpl implements
		TemplateContext {

	private HttpServletRequest request;

	public RequestTemplateContext(HttpServletRequest request) {
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		T value = (T) super.get(name);
		if (value != null) {
			return value;
		}
		return (T) findInRequset(name);
	}

	@SuppressWarnings("unchecked")
	private <T> T findInRequset(String name) {
		if (request != null) {
			T result = (T) request.getAttribute(name);
			if (!ObjectUtil.isEmptyObject(result))
				return result;
			result = (T) request.getParameterValues(name);
			if (!ObjectUtil.isEmptyObject(result)) {
				if (result.getClass().isArray()) {// 处理字符串数组的问题
					Object[] temp = (Object[]) result;
					if (temp.length == 1) {
						result = (T) temp[0];
					}
				}
			}
			if (!ObjectUtil.isEmptyObject(result))
				return result;

			result = (T) request.getSession().getAttribute(name);
			if (!ObjectUtil.isEmptyObject(result))
				return result;
			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if (cookie.getName().equals(name)) {
						result = (T) cookie.getValue();
						return result;
					}
				}
			}
			result = (T) request.getHeader(name);
			if (!ObjectUtil.isEmptyObject(result)) {
				return result;
			}
		}
		return null;
	}

	public boolean exist(String name) {
		boolean exist = super.exist(name);
		if (exist) {
			return true;
		}
		return existInRequset(name);
	}

	@SuppressWarnings("unchecked")
	private boolean existInRequset(String name) {
		if (request != null) {
			Enumeration<String> enumer = request.getAttributeNames();
			while (enumer.hasMoreElements()) {
				if (enumer.nextElement().equals(name)) {
					return true;
				}
			}
			Map parameterMap = request.getParameterMap();
			if (parameterMap.containsKey(name)) {
				return true;
			}
			enumer = request.getSession().getAttributeNames();
			while (enumer.hasMoreElements()) {
				if (enumer.nextElement().equals(name)) {
					return true;
				}
			}
			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if (cookie.getName().equals(name)) {
						return true;
					}
				}
			}
			enumer = request.getHeaderNames();
			while (enumer.hasMoreElements()) {
				if (enumer.nextElement().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

}
