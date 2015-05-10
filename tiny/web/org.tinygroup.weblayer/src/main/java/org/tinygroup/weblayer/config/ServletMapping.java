package org.tinygroup.weblayer.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("servlet-mapping")
public class ServletMapping {

	@XStreamAsAttribute
	@XStreamAlias("url-pattern")
	private String urlPattern;

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	@Override
	public int hashCode() {
		return urlPattern.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}
		if (obj instanceof ServletMapping) {
			ServletMapping other = (ServletMapping) obj;
			return other.urlPattern.equals(this.urlPattern);
		}
		return false;
	}
}
