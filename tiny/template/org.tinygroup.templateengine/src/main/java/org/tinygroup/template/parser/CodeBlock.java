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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 2014/6/3.
 */
public class CodeBlock {
    private CodeBlock parentCodeBlock;
    private CodeLet headerCodeLet;
    private CodeLet footerCodeLet;
    private List<CodeBlock> subCodeBlocks;
    private int tabIndent = 0;

    public CodeBlock getParentCodeBlock() {
        return parentCodeBlock;
    }

    public int getTabIndent() {
        return tabIndent;
    }

    public CodeBlock tabIndent(int tabIndent) {
        this.tabIndent = tabIndent;
        return this;
    }

    public CodeBlock setTabIndent(int tabIndent) {
        this.tabIndent = tabIndent;
        return this;
    }

    public CodeBlock setParentCodeBlock(CodeBlock parentCodeBlock) {
        this.parentCodeBlock = parentCodeBlock;
        return this;
    }

    public CodeBlock insertSubCode(String code) {
        CodeBlock newCodeBlock = new CodeBlock().header(code);
        getSubCodeBlocks().add(0, newCodeBlock);
        return this;
    }

    public CodeBlock header(CodeLet headerCodeLet) {
        this.headerCodeLet = headerCodeLet;
        return this;
    }

    public CodeBlock header(String string) {
        this.headerCodeLet = new CodeLet(string).endLine();
        return this;
    }

    public CodeBlock footer(String string) {
        this.footerCodeLet = new CodeLet(string).endLine();
        return this;
    }

    public CodeBlock footer(CodeLet footerCodeLet) {
        this.footerCodeLet = footerCodeLet;
        return this;
    }

    public CodeBlock setSubCodeBlocks(List<CodeBlock> subCodeBlocks) {
        this.subCodeBlocks = subCodeBlocks;
        for (CodeBlock subCodeBlock : subCodeBlocks) {
            subCodeBlock.setParentCodeBlock(this);
        }
        return this;
    }

    public List<CodeBlock> getSubCodeBlocks() {
        return subCodeBlocks;
    }

    public CodeBlock subCode(String code) {
        if (code != null) {
            subCode(new CodeLet(code).endLine());
        }
        return this;
    }

    public CodeBlock subCode(CodeBlock codeBlock) {
        if (codeBlock != null) {
            if (subCodeBlocks == null) {
                subCodeBlocks = new ArrayList<CodeBlock>();
            }
            subCodeBlocks.add(codeBlock);
            codeBlock.setParentCodeBlock(this);
        }
        return this;
    }

    public CodeBlock subCode(CodeLet codeLet) {
        if (codeLet != null) {
            CodeBlock codeBlock = new CodeBlock();
            codeBlock.header(codeLet);
            return subCode(codeBlock);
        }
        return this;
    }

    public CodeLet getHeaderCodeLet() {
        return headerCodeLet;
    }

    public CodeLet getFooterCodeLet() {
        return footerCodeLet;
    }

    public CodeBlock write(Writer writer) throws IOException {
        return write(writer, 0);
    }

    public CodeBlock write(Writer writer, int tab) throws IOException {
        int blockIndent = tab + tabIndent;
        if (headerCodeLet != null) {
            writePreBlank(writer, blockIndent);
            headerCodeLet.write(writer);
        }
        if (subCodeBlocks != null) {
            for (CodeBlock codeBlock : subCodeBlocks) {
                if (headerCodeLet == null && footerCodeLet == null) {
                    codeBlock.write(writer, blockIndent);
                } else {
                    codeBlock.write(writer, blockIndent + 1);
                }
            }
        }
        if (footerCodeLet != null) {
            writePreBlank(writer, blockIndent);
            footerCodeLet.write(writer);
        }
        return this;
    }

    private void writePreBlank(Writer writer, int tab) throws IOException {
        if (tab > 0) {
            writer.write(String.format("%" + tab * 4 + "s", ""));
        }
    }

    public String toString() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);
        try {
            write(writer);
            writer.close();
        } catch (IOException e) {
            //DoNothing
        }
        return outputStream.toString();
    }

}
