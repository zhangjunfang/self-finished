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
package org.tinygroup.template.loader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.compiler.MemorySource;
import org.tinygroup.template.compiler.MemorySourceCompiler;
import org.tinygroup.template.impl.ClassName;
import org.tinygroup.template.impl.ClassNameGetterDefault;
import org.tinygroup.template.parser.CodeBlock;
import org.tinygroup.template.parser.TinyTemplateCodeVisitor;
import org.tinygroup.template.parser.TinyTemplateErrorListener;
import org.tinygroup.template.parser.TinyTemplateErrorStrategy;
import org.tinygroup.template.parser.grammer.TinyTemplateLexer;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

/**
 * Created by luoguo on 2014/6/9.
 */
public final class ResourceCompilerUtils {
    private ResourceCompilerUtils() {
    }

    private static ClassNameGetter classNameGetter = new ClassNameGetterDefault();
    private static MemorySourceCompiler compiler = new MemorySourceCompiler();

    public static void setClassNameGetter(ClassNameGetter classNameGetter) {
        ResourceCompilerUtils.classNameGetter = classNameGetter;
    }

    public static ClassNameGetter getClassNameGetter() {
        return classNameGetter;
    }

    public static <T> T compileResource(ClassLoader classLoader, String content, String engineId, String path) throws TemplateException {
        ClassName className = classNameGetter.getClassName(path);
        CodeBlock codeBlock = preCompile(content, path);
        if (className.getPackageName() != null) {
            codeBlock.insertSubCode("package " + className.getPackageName() + ";");
        }
        MemorySource memorySource = new MemorySource(className.getClassName(), codeBlock.toString().replace("$TEMPLATE_PATH", path)
                .replace("$TEMPLATE_CLASS_NAME", className.getSimpleClassName()));
        if (compiler.isModified(className,engineId, memorySource.getContent())) {
            return (T) compiler.loadClass(classLoader,engineId, memorySource);
        } else {
            return (T) compiler.loadInstance(classLoader, engineId,className.getClassName());
        }

    }

    public static CodeBlock preCompile(String template, String sourceName) {
        char[] source = template.toCharArray();
        ANTLRInputStream is = new ANTLRInputStream(source, source.length);
        // set source file name, it will be displayed in error report.
        is.name = sourceName;
        TinyTemplateParser parser = new TinyTemplateParser(new CommonTokenStream(new TinyTemplateLexer(is)));
        // remove ConsoleErrorListener
        parser.removeErrorListeners();
        parser.addErrorListener(TinyTemplateErrorListener.getInstance());
        parser.setErrorHandler(new TinyTemplateErrorStrategy());
        TinyTemplateParser.TemplateContext templateParseTree = parser.template();
        TinyTemplateCodeVisitor visitor = new TinyTemplateCodeVisitor(parser);
        return templateParseTree.accept(visitor);
    }

}
