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
package org.tinygroup.template.parser;

import java.io.IOException;
import java.io.Writer;

/**
 * 代码段
 * Created by luoguo on 2014/6/4.
 */
public class CodeLet {
    private StringBuffer codeBuffer = new StringBuffer();
    public int length(){
        return codeBuffer.length();
    }
    public CodeLet() {

    }

    public CodeLet(String code) {
        codeBuffer.append(code);
    }

    public CodeLet lineCode(String code) {
        return code(code).endLine();
    }

    public CodeLet lineCode(String code, Object... args) {
        return code(code, args).endLine();
    }

    public static CodeLet codeLet(String code, Object... args) {
        return new CodeLet().code(code, args);
    }

    public static CodeLet lineCodeLet(String code, Object... args) {
        return new CodeLet().lineCode(code, args);
    }

    public CodeLet code(String code) {
        codeBuffer.append(code);
        return this;
    }

    public CodeLet codeBefore(String code) {
        codeBuffer.insert(0, code);
        return this;
    }

    public CodeLet code(String code, Object... args) {
        codeBuffer.append(String.format(code, args));
        return this;
    }

    public StringBuffer getCodeBuffer() {
        return codeBuffer;
    }

    public String toString() {
        return codeBuffer.toString();
    }

    public CodeLet write(Writer writer) throws IOException {
        writer.write(codeBuffer.toString());
        return this;
    }

    public CodeLet endLine() {
        return code("\r\n");
    }


    public CodeLet code(CodeLet codeLet) {
        return this.code(codeLet.toString());
    }

    public CodeLet lineCode(CodeLet codeLet) {
        return this.lineCode(codeLet.toString());
    }

}
