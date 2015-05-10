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
package org.tinygroup.database.config.table;

import org.tinygroup.commons.tools.EqualsUtil;
import org.tinygroup.commons.tools.HashCodeUtil;
import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 外键引用关系
 * @author renhui
 *
 */
@XStreamAlias("foreign-reference")
public class ForeignReference extends BaseObject{

	/**
	 * 外键引用的主表
	 */
	@XStreamAlias("main-table")
	private String mainTable;
	/**
	 * 外键引用的主表字段
	 */
	@XStreamAlias("reference-field")
	private String referenceField;
	/**
	 * 外键字段
	 */
	@XStreamAlias("foreign-field")
	private String foreignField;
	
	public ForeignReference() {
		super();
	}
	public ForeignReference(String name,String mainTable, String referenceField,
			String foreignField) {
		super();
		setName(name);
		this.mainTable = mainTable;
		this.referenceField = referenceField;
		this.foreignField = foreignField;
	}
	public String getMainTable() {
		return mainTable;
	}
	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}
	public String getReferenceField() {
		return referenceField;
	}
	public void setReferenceField(String referenceField) {
		this.referenceField = referenceField;
	}
	public String getForeignField() {
		return foreignField;
	}
	public void setForeignField(String foreignField) {
		this.foreignField = foreignField;
	}
	@Override
	public int hashCode() {
		return HashCodeUtil.reflectionCompareHashCode(this, new String[]{"mainTable","referenceField","foreignField","name"});
	}
	@Override
	public boolean equals(Object obj) {
		return EqualsUtil.reflectionCompareEquals(this, obj, new String[]{"mainTable","referenceField","foreignField","name"});
	}
	
}