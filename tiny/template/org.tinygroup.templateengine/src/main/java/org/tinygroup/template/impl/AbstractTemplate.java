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
package org.tinygroup.template.impl;

import org.tinygroup.template.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractTemplate implements Template {
    private Map<String, Macro> macroMap = new HashMap<String, Macro>();
    private TemplateEngine templateEngine;
    private TemplateContext templateContext = new TemplateContextDefault();
    private List<String> importPathList = new ArrayList<String>();

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    protected void write(Writer writer, Object object) throws IOException {
        if (object != null) {
            writer.write(object.toString());
        }
    }

    public void addImport(Object importPath) {
        importPathList.add(importPath.toString());
    }

    public List<String> getImportPathList() {
        return importPathList;
    }

    protected void addMacro(Macro macro) {
        macroMap.put(macro.getName(), macro);
        macro.setTemplateEngine(getTemplateEngine());
    }

    public Map<String, Macro> getMacroMap() {
        return macroMap;
    }

    public void render(TemplateContext context, Writer writer) throws TemplateException {
        try {
            context.putSubContext("$currentTemplateContext", getTemplateContext());
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            renderContent(context, bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new TemplateException(e);
        } finally {
            context.removeSubContext("$currentTemplateContext");
        }
    }
    public void render(TemplateContext context) throws TemplateException {
        render(context, new OutputStreamWriter(System.out));
    }

    public void render() throws TemplateException {
        render(new TemplateContextDefault(), new OutputStreamWriter(System.out));
    }

    protected abstract void renderContent(TemplateContext context, Writer writer) throws IOException, TemplateException;

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        for (Macro macro : macroMap.values()) {
            macro.setTemplateEngine(templateEngine);
        }
    }

    public TemplateContext getTemplateContext() {
        return templateContext;
    }


    public Object put(String key, Object value) {
        return templateContext.put(key, value);
    }
}
