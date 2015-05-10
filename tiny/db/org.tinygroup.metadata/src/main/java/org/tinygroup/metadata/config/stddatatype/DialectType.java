/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.metadata.config.stddatatype;

import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.metadata.config.PlaceholderValue;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 方言类型
 * @author luoguo
 *
 */
@XStreamAlias("dialect-type")
public class DialectType {
	@XStreamAsAttribute
	private String language;// 语言
	@Deprecated
	@XStreamAsAttribute
	private String type; //带精度的类型，不建议使用。采用baseType和extType替代
	@XStreamAsAttribute
	@XStreamAlias("base-type")
	private String baseType;// 基础类型，必填。如varchar
	@XStreamAsAttribute
	@XStreamAlias("ext-type")
	private String extType;// 扩展类型，选填。用于描述基础类型的长度或精度。
	@XStreamAsAttribute
	@XStreamAlias("default-value")
	private String defaultValue;// 默认值
	@XStreamImplicit
	private List<PlaceholderValue> placeholderValueList;// 占位符列表

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 为了兼容以前配置
	 * @return
	 */
	public String getType() {
		if(StringUtil.isEmpty(type)){
		   return StringUtil.isEmpty(extType)?baseType:baseType+getExtType();
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<PlaceholderValue> getPlaceholderValueList() {
		return placeholderValueList;
	}

	public void setPlaceholderValueList(
			List<PlaceholderValue> placeholderValueList) {
		this.placeholderValueList = placeholderValueList;
	}

	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public String getExtType() {
		if(StringUtil.isEmpty(extType) || extType.startsWith("(") || extType.endsWith(")")){
			return extType;
		}else{
			return "("+extType+")";
		}
	}

	public void setExtType(String extType) {
		this.extType = extType;
	}

}
