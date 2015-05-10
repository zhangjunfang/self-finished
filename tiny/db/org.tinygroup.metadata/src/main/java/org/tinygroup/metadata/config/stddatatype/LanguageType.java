package org.tinygroup.metadata.config.stddatatype;

import java.util.List;

import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 数据库语言
 * @author yancheng11334
 *
 */
@XStreamAlias("language-type")
public class LanguageType extends BaseObject{

	@XStreamImplicit
	private List<LanguageField> languageFieldList;

	public List<LanguageField> getLanguageFieldList() {
		return languageFieldList;
	}

	public void setLanguageFieldList(List<LanguageField> languageFieldList) {
		this.languageFieldList = languageFieldList;
	}
}
