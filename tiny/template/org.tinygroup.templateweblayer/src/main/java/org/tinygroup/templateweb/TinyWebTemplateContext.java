package org.tinygroup.templateweb;

import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.weblayer.WebContext;

/**
 * tinywebcontext包装的模板上下文
 * 
 * @author renhui
 * 
 */
public class TinyWebTemplateContext extends ContextImpl implements
		TemplateContext {

	private WebContext webContext;

	public TinyWebTemplateContext(WebContext webContext) {
		this.webContext = webContext;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		T value=(T)super.get(name);
		if(value!=null){
			return value;
		}
		return (T)webContext.get(name);
	}

	public WebContext getWebContext() {
		return webContext;
	}

	@Override
	public boolean exist(String name) {
		boolean exist=super.exist(name);
		if(exist){
			return true;
		}
		return webContext.exist(name);
	}
	
	
}
