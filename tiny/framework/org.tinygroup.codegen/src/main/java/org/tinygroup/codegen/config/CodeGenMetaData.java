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
package org.tinygroup.codegen.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("code-gen-metadata")
public class CodeGenMetaData {
    /**
     * 分类
     */
    @XStreamAsAttribute
    @XStreamAlias("category")
    private String category;
    /**
     * 图标
     */
    @XStreamAsAttribute
    @XStreamAlias("icon")
    private String icon;
    /**
     * 标题
     */
    @XStreamAsAttribute
    @XStreamAlias("title")
    private String title;
    
    @XStreamAsAttribute
    @XStreamAlias("name")
    private String name;
    /**
     * 长描述
     */
    private String description;
    
    @XStreamImplicit
    private List<TemplateDefine> templateDefines;
    @XStreamImplicit
    private List<MacroDefine> macroDefines;
    /**
     * 人机界面交互定义方件
     */
    @XStreamAsAttribute
    @XStreamAlias("ui-define-file")
    private String uiDefineFile;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TemplateDefine> getTemplateDefines() {
        if (templateDefines == null) {
            templateDefines = new ArrayList<TemplateDefine>();
        }
        return templateDefines;
    }

    public void setTemplateDefines(List<TemplateDefine> templateDefines) {
        this.templateDefines = templateDefines;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUiDefineFile() {
        return uiDefineFile;
    }

    public void setUiDefineFile(String uiDefineFile) {
        this.uiDefineFile = uiDefineFile;
    }

	public List<MacroDefine> getMacroDefines() {
		if(macroDefines==null){
			macroDefines=new ArrayList<MacroDefine>();
		}
		return macroDefines;
	}

	public void setMacroDefines(List<MacroDefine> macroDefines) {
		this.macroDefines = macroDefines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
    
}
