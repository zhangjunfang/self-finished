package org.tinygroup.weblayer.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("filter-mapping")
public class FilterMapping {

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
		if (obj instanceof FilterMapping) {
			FilterMapping other = (FilterMapping) obj;
			return other.urlPattern.equals(this.urlPattern);
		}
		return false;
	}

}
