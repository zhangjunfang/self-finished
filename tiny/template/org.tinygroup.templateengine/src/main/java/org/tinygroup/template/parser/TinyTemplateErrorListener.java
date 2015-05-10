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

import org.antlr.v4.runtime.*;
import org.tinygroup.template.compiler.MemorySourceCompiler;

public final class TinyTemplateErrorListener extends BaseErrorListener {
    private static TinyTemplateErrorListener instance = new TinyTemplateErrorListener();

    public static TinyTemplateErrorListener getInstance() {
        return instance;
    }

    private TinyTemplateErrorListener() {
    }


    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {

        CommonTokenStream tokens = (CommonTokenStream) recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        String[] sourceLines = input.split("\r?\n", -1);
        Token offendingToken = (Token) offendingSymbol;

        StringBuilder sb = new StringBuilder(128);
        sb.append("Template parse failed.\n");
        sb.append(recognizer.getInputStream().getSourceName());
        sb.append(':');
        sb.append(line);
        sb.append(':');
        sb.append(charPositionInLine);
        sb.append("\nmessage: ");
        sb.append(msg);
        sb.append('\n');
        sb.append(MemorySourceCompiler.getPrettyError(sourceLines, line, charPositionInLine + 1, offendingToken.getStartIndex(), offendingToken.getStopIndex(), 5));

        if (e != null) {
            throw new SyntaxErrorException(sb.toString(), line, charPositionInLine, e);
        } else {
            throw new SyntaxErrorException(sb.toString(), line, charPositionInLine);
        }
    }
}
