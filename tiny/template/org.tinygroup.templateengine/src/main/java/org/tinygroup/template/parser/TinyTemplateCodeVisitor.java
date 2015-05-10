/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.template.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.tinygroup.template.loader.ResourceCompilerUtils;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.parser.grammer.TinyTemplateParserVisitor;

import java.util.List;
import java.util.Stack;

// Visitor 模式访问器，用来生成 Java 代码
public class TinyTemplateCodeVisitor extends AbstractParseTreeVisitor<CodeBlock> implements TinyTemplateParserVisitor<CodeBlock> {
    private static final String[] RESERVED_WORDS = {"set", "if","elseif","for","foreach","break","continue","stop","include","call","layout","macro","b","eol","t","bodyContent","import"};
    private TinyTemplateParser parser = null;
    private Stack<CodeBlock> codeBlocks = new Stack<CodeBlock>();
    private Stack<CodeLet> codeLets = new Stack<CodeLet>();
    private CodeBlock initCodeBlock = null;
    private CodeBlock macroCodeBlock = null;
    /**
     * 是否是严格格式，如果是严格格式会进行trim()
     */
    public static boolean strictFormat = false;

    public TinyTemplateCodeVisitor(TinyTemplateParser parser) {
        this.parser = parser;
    }

    public CodeBlock visitExpression_list(@NotNull TinyTemplateParser.Expression_listContext ctx) {

        List<TinyTemplateParser.ExpressionContext> expressionList = ctx.expression();
        int i = 0;
        for (TinyTemplateParser.ExpressionContext expression : expressionList) {
            CodeLet exp = pushPeekCodeLet();
            expression.accept(this);
            popCodeLet();
            if (i > 0) {
                peekCodeLet().code(",");
            }
            peekCodeLet().code(exp);
            i++;
        }
        return null;
    }

    public CodeBlock visitInvalid_directive(@NotNull TinyTemplateParser.Invalid_directiveContext ctx) {
        throw reportError("Missing arguments for " + ctx.getText() + " directive.", ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx);
    }


    public CodeBlock visitCall_directive(@NotNull TinyTemplateParser.Call_directiveContext ctx) {
        CodeBlock callMacro = processVisitCallHead(ctx.expression(), ctx.para_expression_list());
        callMacro.subCode("$macro.render($template,$context,$newContext,$writer);");
        return callMacro;
    }

    private CodeBlock processVisitCallHead(TinyTemplateParser.ExpressionContext macroName, TinyTemplateParser.Para_expression_listContext paraExpressionListContext) {
        CodeBlock callMacro = new CodeBlock();
        CodeLet nameCodeBlock = pushPeekCodeLet();
        macroName.accept(this);
        popCodeLet();

        String name = nameCodeBlock.toString();
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(%s,$template,$context);", name));
        callMacro.subCode("$newContext=new TemplateContextDefault();");
        callMacro.subCode("$paraList=new ArrayList();");
        callMacro.subCode("$newContext.put(" + name + "+\"ParameterList\",$paraList);");
        callMacro.subCode("$newContext.setParent($context);");
        if (paraExpressionListContext != null) {
            List<TinyTemplateParser.Para_expressionContext> expList = paraExpressionListContext.para_expression();
            if (expList != null) {
                pushCodeBlock(callMacro);
                int i = 0;
                for (TinyTemplateParser.Para_expressionContext visitParaExpression : expList) {
                    processVisitPara(i, visitParaExpression, name);
                    i++;
                }
                popCodeBlock();
            }
        }
        return callMacro;
    }

    public CodeBlock visitElse_directive(@NotNull TinyTemplateParser.Else_directiveContext ctx) {
        CodeBlock elseBlock = new CodeBlock().subCode(new CodeLet().lineCode("}else{")).tabIndent(-1);
        peekCodeBlock().subCode(elseBlock);
        ctx.block().accept(this);
        return null;
    }


    public CodeBlock visitExpr_hash_map(@NotNull TinyTemplateParser.Expr_hash_mapContext ctx) {
        TinyTemplateParser.Hash_map_entry_listContext hashMapEntryListContext = ctx.hash_map_entry_list();
        if (hashMapEntryListContext != null) {
            peekCodeLet().code("new TemplateMap()").code(hashMapEntryListContext.accept(this).toString()).code("");
        }
        return null;
    }

    public CodeBlock visitContinue_directive(@NotNull TinyTemplateParser.Continue_directiveContext ctx) {
        TinyTemplateParser.ExpressionContext expression = ctx.expression();
        processorConditionDirective(expression, "continue;");
        return null;
    }


    public CodeBlock visitExpr_field_access(@NotNull TinyTemplateParser.Expr_field_accessContext ctx) {
        CodeLet exp = pushPeekCodeLet();
        ctx.expression().accept(this);
        popCodeLet();
        peekCodeLet().code(ctx.getChild(1).getText().equals("?.") ? "U.sp(" : "U.p(").code(exp).code(",\"").code(ctx.IDENTIFIER().getText()).code("\")");
        return null;
    }

    public CodeBlock visitExpr_compare_condition(@NotNull TinyTemplateParser.Expr_compare_conditionContext ctx) {
        CodeLet left = pushPeekCodeLet();
        ctx.expression(0).accept(this);
        popCodeLet();
        left.codeBefore("U.b(").code(")");
        CodeLet right = pushPeekCodeLet();
        ctx.expression(1).accept(this);
        popCodeLet();
        right.codeBefore("U.b(").code(")");
        String op = ctx.getChild(1).getText();
        peekCodeLet().code(left).code(op).code(right);
        return null;
    }


    public CodeBlock visitExpr_function_call(@NotNull TinyTemplateParser.Expr_function_callContext ctx) {
        String functionName = ctx.getChild(0).getText();

        peekCodeLet().codeBefore("getTemplateEngine().executeFunction($template,$context,\"").code(functionName).code("\"");
        TinyTemplateParser.Expression_listContext list = ctx.expression_list();
        if (list != null) {
            peekCodeLet().code(",");
            list.accept(this);
        }
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitIndent_directive(@NotNull TinyTemplateParser.Indent_directiveContext ctx) {
        CodeBlock block = new CodeBlock();
        block.subCode("U.indent($context);");
        return block;
    }


    public CodeBlock visitMacro_directive(@NotNull TinyTemplateParser.Macro_directiveContext ctx) {
        String name = ctx.getChild(0).getText();
        name = name.substring(6, name.length() - 1).trim();
        //这里进行保留字检查
        boolean isReserve=false;
        for(String word :RESERVED_WORDS){
            if(name.equals(word)){
                TerminalNodeImpl terminalNode = (TerminalNodeImpl) ctx.getChild(0);
                throw new SyntaxErrorException("Macro name<"+name+"> is reserved word.",  terminalNode.getSymbol().getLine(), terminalNode.getSymbol().getStartIndex());
            }
        }

        name = ResourceCompilerUtils.getClassNameGetter().getClassName(name).getSimpleClassName();
        initCodeBlock.subCode(new CodeLet().lineCode("addMacro(new %s());", name));
        CodeBlock macro = new CodeBlock();
        TinyTemplateParser.Define_expression_listContext defineExpressionListContext = ctx.define_expression_list();
        pushPeekCodeLet();
        if (defineExpressionListContext != null) {
            defineExpressionListContext.accept(this);
        }
        macro.header(new CodeLet().lineCode("class %s extends AbstractMacro {", name));
        macro.footer(new CodeLet().lineCode("}"));
        macro.subCode(constructMethod(name));
        popCodeLet();
        CodeBlock render = getMacroRenderCodeBlock();
        pushCodeBlock(render);
        macro.subCode(render);
        ctx.block().accept(this);
        popCodeBlock();
        macroCodeBlock.subCode(macro);
        return null;
    }

    private CodeBlock constructMethod(String name) {
        CodeBlock block = new CodeBlock();
        block.header(CodeLet.lineCodeLet("public %s() {", name));
        block.subCode(String.format("super(\"%s\");", name));
        block.subCode(peekCodeLet());
        block.subCode(String.format("init(\"%s\");", name));
        block.footer(CodeLet.lineCodeLet("}"));
        return block;
    }

    public CodeBlock visitExpr_compare_equality(@NotNull TinyTemplateParser.Expr_compare_equalityContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitValue(@NotNull TinyTemplateParser.ValueContext ctx) {
        CodeBlock valueCodeBlock = new CodeBlock();
        pushCodeLet();
        if (ctx.getChild(0).getText().equals("$${")) {
            peekCodeLet().code("write($writer,U.getI18n($template.getTemplateEngine().getI18nVistor(),$context,\"").code(ctx.identify_list().getText()).lineCode("\"));");
        } else {
            ctx.expression().accept(this);
            Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
            if (token.getType() == TinyTemplateParser.VALUE_ESCAPED_OPEN) {
                peekCodeLet().codeBefore("U.escapeHtml((").code("))");
            }
            peekCodeLet().codeBefore("write($writer,").lineCode(");");
        }
        valueCodeBlock.subCode(peekCodeLet());
        popCodeLet();
        return valueCodeBlock;
    }

    public CodeBlock visitExpr_math_binary_bitwise(@NotNull TinyTemplateParser.Expr_math_binary_bitwiseContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitHash_map_entry_list(@NotNull TinyTemplateParser.Hash_map_entry_listContext ctx) {
        List<TinyTemplateParser.ExpressionContext> expressionContexts = ctx.expression();
        CodeLet keyPair = new CodeLet();
        CodeBlock result = new CodeBlock().subCode(keyPair);
        for (int i = 0; i < expressionContexts.size(); i += 2) {
            CodeBlock codeBlock = new CodeBlock();
            CodeLet keyCodeLet = pushPeekCodeLet();
            expressionContexts.get(i).accept(this);
            popCodeLet();
            CodeLet valueCodeLet = pushPeekCodeLet();
            expressionContexts.get(i + 1).accept(this);
            popCodeLet();
            codeBlock.subCode(new CodeLet().code(keyCodeLet).code(":").code(valueCodeLet));
            keyPair.code(".putItem(").code(keyCodeLet).code(",").code(valueCodeLet).code(")");
        }
        return result;
    }

    public CodeBlock visitDirective(@NotNull TinyTemplateParser.DirectiveContext ctx) {
        return ctx.getChild(0).accept(this);
    }


    public CodeBlock visitDent_directive(@NotNull TinyTemplateParser.Dent_directiveContext ctx) {
        CodeBlock block = new CodeBlock();
        block.subCode("U.dent($context);");
        return block;
    }

    public CodeBlock visitTemplate(@NotNull TinyTemplateParser.TemplateContext ctx) {
        CodeBlock templateCodeBlock = getTemplateCodeBlock();
        CodeBlock classCodeBlock = getClassCodeBlock();
        templateCodeBlock.subCode(classCodeBlock);
        CodeBlock renderMethodCodeBlock = getTemplateRenderCodeBlock();
        classCodeBlock.subCode(renderMethodCodeBlock);
        CodeBlock getTemplatePathMethod = getTemplatePathMethodCodeBlock();
        classCodeBlock.subCode(getTemplatePathMethod);
        pushCodeBlock(renderMethodCodeBlock);
        renderMethodCodeBlock.subCode(ctx.block().accept(this));
        popCodeBlock();
        return templateCodeBlock;
    }

    private CodeBlock getTemplateRenderCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("protected void renderContent(TemplateContext $context, Writer $writer) throws IOException, TemplateException{")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode("Macro $macro=null;");
        renderMethod.subCode("Macro $bodyMacro=null;");
        renderMethod.subCode("Template $template=this;");
        renderMethod.subCode("TemplateContext $pageContext=$context;");
        renderMethod.subCode("TemplateContext $newContext=null;");
        renderMethod.subCode("List $paraList=null;");

        return renderMethod;
    }

    private CodeBlock getMacroRenderCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("protected void renderMacro(Template $template,TemplateContext $pageContext, TemplateContext $context, Writer $writer) throws IOException, TemplateException{")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode("Macro $macro=null;");
        renderMethod.subCode("Macro $bodyMacro=null;");
        renderMethod.subCode("TemplateContext $newContext=null;");
        renderMethod.subCode("List $paraList=null;");

        return renderMethod;
    }

    private CodeBlock getTemplatePathMethodCodeBlock() {
        CodeBlock renderMethod = new CodeBlock();
        renderMethod.header(new CodeLet().lineCode("public String getPath(){")).footer(new CodeLet().lineCode("}"));
        renderMethod.subCode(new CodeLet().lineCode("return \"$TEMPLATE_PATH\";"));
        return renderMethod;
    }

    private CodeBlock getClassCodeBlock() {
        CodeBlock templateClass = new CodeBlock();
        initCodeBlock = new CodeBlock().header(new CodeLet("{").endLine()).footer(new CodeLet("}").endLine());
        templateClass.header(new CodeLet().lineCode("public class $TEMPLATE_CLASS_NAME extends AbstractTemplate{"));
        templateClass.subCode(initCodeBlock);
        macroCodeBlock = new CodeBlock();
        templateClass.subCode(macroCodeBlock);
        templateClass.footer(new CodeLet().lineCode("}"));
        return templateClass;
    }

    private CodeBlock getTemplateCodeBlock() {
        CodeBlock codeBlock = new CodeBlock();
        codeBlock.subCode(new CodeLet().lineCode("import java.io.IOException;"));
        codeBlock.subCode(new CodeLet().lineCode("import java.util.*;"));
        codeBlock.subCode(new CodeLet().lineCode("import org.tinygroup.template.rumtime.*;"));
        codeBlock.subCode(new CodeLet().lineCode("import org.tinygroup.template.*;"));
        codeBlock.subCode(new CodeLet().lineCode("import java.io.Writer;"));
        codeBlock.subCode(new CodeLet().lineCode("import org.tinygroup.template.impl.*;"));
        return codeBlock;
    }


    public CodeBlock visitIdentify_list(@NotNull TinyTemplateParser.Identify_listContext ctx) {

        return null;
    }

    public CodeBlock visitText(@NotNull TinyTemplateParser.TextContext ctx) {
        Token token = ((TerminalNode) ctx.getChild(0)).getSymbol();
        String text = token.getText();
        switch (token.getType()) {
            case TinyTemplateParser.TEXT_PLAIN:
                if (strictFormat) text = text.trim();
                break;
            case TinyTemplateParser.TEXT_CDATA:
                text = text.substring(3, text.length() - 3);
                break;
            case TinyTemplateParser.TEXT_ESCAPED_CHAR:
                text = text.substring(1);
                break;
            default:
                break;
        }
        if (text.length() == 0) {
            return null;
        }
        return new CodeBlock().header(new CodeLet().code("write($writer,\"").code(escapeJavaStyleString(text).toString()).lineCode("\");"));
    }

    private static StringBuffer escapeJavaStyleString(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            switch (ch) {
                case '\b':
                    escapeChar(stringBuffer, 'b');
                    break;
                case '\n':
                    escapeChar(stringBuffer, 'n');
                    break;
                case '\t':
                    escapeChar(stringBuffer, 't');
                    break;
                case '\f':
                    escapeChar(stringBuffer, 'f');
                    break;
                case '\r':
                    escapeChar(stringBuffer, 'r');
                    break;
                case '"':
                    escapeChar(stringBuffer, '"');
                    break;
                case '\\':
                    escapeChar(stringBuffer, '\\');
                    break;
                default:
                    stringBuffer.append(ch);
                    break;
            }
        }
        return stringBuffer;
    }

    private static void escapeChar(StringBuffer stringBuffer, char ch) {
        stringBuffer.append('\\');
        stringBuffer.append(ch);
    }

    public CodeBlock visitExpr_identifier(@NotNull TinyTemplateParser.Expr_identifierContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        peekCodeLet().code("U.v($context,\"" + name + "\")");
        return null;
    }


    public CodeBlock visitIf_directive(@NotNull TinyTemplateParser.If_directiveContext ctx) {
        CodeBlock ifCodeBlock = pushPeekCodeBlock();
        pushCodeLet();
        ctx.expression().accept(this);
        ifCodeBlock.header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){"));
        popCodeLet();
        ifCodeBlock.footer(new CodeLet().lineCode("}"));
        ctx.block().accept(this);
        List<TinyTemplateParser.Elseif_directiveContext> elseifDirectiveContexts = ctx.elseif_directive();
        for (TinyTemplateParser.Elseif_directiveContext elseifDirectiveContext : elseifDirectiveContexts) {
            elseifDirectiveContext.accept(this);
        }
        TinyTemplateParser.Else_directiveContext elseDirectiveContext = ctx.else_directive();
        if (elseDirectiveContext != null) {
            elseDirectiveContext.accept(this);
        }
        popCodeBlock();
        return ifCodeBlock;
    }

    public CodeBlock visitLayout_directive(@NotNull TinyTemplateParser.Layout_directiveContext ctx) {
        String name = "$" + ctx.IDENTIFIER().getText();
        CodeBlock positionCodeBlock = new CodeBlock();
        positionCodeBlock.subCode("if($context.exist(\"" + name + "\")){");
        positionCodeBlock.subCode("    write($writer,$context.get(\"" + name + "\"));");
        positionCodeBlock.subCode("}else {");
        positionCodeBlock.subCode("    new AbstractMacro(\"" + name + "\") {");
        positionCodeBlock.subCode("        protected void renderMacro(Template $template, TemplateContext $context,TemplateContext $newContext, Writer $writer) throws IOException, TemplateException {");
        CodeBlock subCodeBlock = pushPeekCodeBlock();
        ctx.block().accept(this);
        popCodeBlock();
        positionCodeBlock.subCode(subCodeBlock);
        positionCodeBlock.subCode("        }");
        positionCodeBlock.subCode("    }.renderMacro($template, $context, $newContext,$writer);");
        positionCodeBlock.subCode("}");
        return positionCodeBlock;
    }


//    public CodeBlock visitExpr_math_unary_suffix(@NotNull TinyTemplateParser.Expr_math_unary_suffixContext ctx) {
//        peekCodeLet().code("O.e(\"l").code(ctx.getChild(1).getText()).code("\",");
//        ctx.expression().accept(this);
//        peekCodeLet().code(")");
//        return null;
//    }


    public CodeBlock visitBodycontent_directive(@NotNull TinyTemplateParser.Bodycontent_directiveContext ctx) {
        CodeBlock codeBlock = new CodeBlock();
        codeBlock.subCode("$macro= getMacro($context);");
        codeBlock.subCode("if($macro!=null) {");
        codeBlock.subCode("    $newContext=new TemplateContextDefault();");
        codeBlock.subCode("    $newContext.setParent($context);");
        codeBlock.subCode("    $macro.render($template,$context,$newContext,$writer);");
        codeBlock.subCode("}");
        return codeBlock;
    }

    public CodeBlock visitDefine_expression_list(@NotNull TinyTemplateParser.Define_expression_listContext ctx) {
        for (TinyTemplateParser.Define_expressionContext exp : ctx.define_expression()) {
            if (exp.expression() == null) {
                peekCodeLet().code("addParameter(\"%s\",null);", exp.IDENTIFIER().getText());
            } else {
                peekCodeLet().code("addParameter(\"%s\",new EvaluateExpression() {public Object evaluate(TemplateContext $context)throws TemplateException{return ", exp.IDENTIFIER().getText());
                exp.expression().accept(this);
                peekCodeLet().code(";}});");
            }
        }
        peekCodeLet().code("\n");
        return null;
    }


    public CodeBlock visitCall_macro_directive(@NotNull TinyTemplateParser.Call_macro_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        String name = ctx.getChild(0).getText();
        name = name.substring(1, name.length());
        if (name.endsWith("(")) {
            name = name.substring(0, name.length() - 1).trim();
        }
        if (name.equals("macro")) {
            TerminalNodeImpl terminalNode = (TerminalNodeImpl) ctx.getChild(0);
            throw new SyntaxErrorException("Missing macro name for #macro directive." ,  terminalNode.getSymbol().getLine(), terminalNode.getSymbol().getStartIndex());
        }
        processCallMacro(ctx.para_expression_list(), callMacro, "\"" + name + "\"");
        callMacro.subCode(String.format("$macro.render($template,$context,$newContext,$writer);"));
        return callMacro;
    }


    public CodeBlock visitExpression_range(@NotNull TinyTemplateParser.Expression_rangeContext ctx) {
        return null;
    }

    public CodeBlock visitComment(@NotNull TinyTemplateParser.CommentContext ctx) {
        return null;
    }


    public CodeBlock visitCall_macro_block_directive(@NotNull TinyTemplateParser.Call_macro_block_directiveContext ctx) {
        CodeBlock callMacro = new CodeBlock();
        String name = ctx.getChild(0).getText();
        name = name.substring(2, name.length() - 1).trim();
        processCallMacro(ctx.para_expression_list(), callMacro, "\"" + name + "\"");
        CodeBlock bodyContentMacro = new CodeBlock();
        //callMacro.subCode("$bodyMacro= (Macro) $context.getItemMap().get(\"bodyContent\");");
        //callMacro.subCode("if($bodyMacro==null){");
        callMacro.subCode(bodyContentMacro);
        bodyContentMacro.header("$bodyMacro=new AbstractMacro(\"bodyContent\",(Macro)$context.getItemMap().get(\"bodyContent\")) {");
        CodeBlock render = getMacroRenderCodeBlock();
        bodyContentMacro.subCode(render);

        pushCodeBlock(render);
        ctx.block().accept(this);

        popCodeBlock();
        bodyContentMacro.footer("};");
        callMacro.subCode("$newContext.put(\"bodyContent\",$bodyMacro);");
        //callMacro.subCode("}");
        callMacro.subCode("$bodyMacro.setTemplateEngine(this.getTemplateEngine());");
        callMacro.subCode("$macro.render($template,$context,$newContext,$writer);");
        //Body部分创建新的类，然后传入要调用的宏
        return callMacro;
    }

    private void processCallMacro(TinyTemplateParser.Para_expression_listContext listContext, CodeBlock callMacro, String name) {
        callMacro.subCode(String.format("$macro=getTemplateEngine().findMacro(%s,$template,$context);", name));
        callMacro.subCode("$newContext=new TemplateContextDefault();");
        callMacro.subCode("$paraList=new ArrayList();");
        callMacro.subCode("$newContext.put(" + name + "+\"ParameterList\",$paraList);");
        callMacro.subCode("$newContext.setParent($context);");
        TinyTemplateParser.Para_expression_listContext expList = listContext;
        if (expList != null) {
            pushCodeBlock(callMacro);
            int i = 0;
            for (TinyTemplateParser.Para_expressionContext paraExpressionContext : expList.para_expression()) {
                processVisitPara(i, paraExpressionContext, name);
                i++;
            }
            popCodeBlock();
        }
    }

    private void processVisitPara(int i, TinyTemplateParser.Para_expressionContext visitParaExpression, String name) {
        CodeLet expression = new CodeLet();
        pushCodeLet(expression);
        if (visitParaExpression.getChildCount() == 3) {
            //如果是带参数的
            visitParaExpression.getChild(2).accept(this);
            peekCodeBlock().subCode(String.format("$newContext.put(\"%s\",%s);", visitParaExpression.getChild(0).getText(), expression));
        } else {
            visitParaExpression.getChild(0).accept(this);
            peekCodeBlock().subCode(String.format("$newContext.put($macro.getParameterName(%d),%s);", i, expression));
        }
        peekCodeBlock().subCode(String.format("((List)$newContext.get(" + name + "+\"ParameterList\")).add(%s);", expression));
        popCodeLet();
    }

    public CodeBlock visitInclude_directive(@NotNull TinyTemplateParser.Include_directiveContext ctx) {
        CodeBlock include = new CodeBlock();
        CodeLet path = pushPeekCodeLet();
        ctx.expression().accept(this);
        popCodeLet();
        CodeLet map = pushPeekCodeLet();
        if (ctx.hash_map_entry_list() != null) {
            peekCodeLet().code("new TemplateMap()").code(ctx.hash_map_entry_list().accept(this).toString()).code("");
        }
        popCodeLet();
        include.subCode("$newContext = new TemplateContextDefault();");
        if (map.length() > 0) {
            include.subCode(String.format("$newContext.putAll(%s);", map));
        }
        include.subCode("$context.putSubContext(\"$newContext\",$newContext);");
        include.subCode(String.format("getTemplateEngine().renderTemplateWithOutLayout(U.getPath(getPath(),%s),$newContext,$writer);", path));
        include.subCode("$context.removeSubContext(\"$newContext\");");
        return include;
    }

    public CodeBlock visitPara_expression(@NotNull TinyTemplateParser.Para_expressionContext ctx) {

        return null;
    }


    public CodeBlock visitTabs_directive(@NotNull TinyTemplateParser.Tabs_directiveContext ctx) {
        CodeBlock tab = new CodeBlock();
        tab.subCode("write($writer,U.getBlanks($context));");
        return tab;
    }


    public CodeBlock visitCall_block_directive(@NotNull TinyTemplateParser.Call_block_directiveContext ctx) {
        CodeBlock callMacro = processVisitCallHead(ctx.expression(), ctx.para_expression_list());

        CodeBlock bodyContentMacro = new CodeBlock();
        callMacro.subCode(bodyContentMacro);
        callMacro.subCode("$macro.render($template,$context,$newContext,$writer);");

        bodyContentMacro.header("$newContext.put(\"bodyContent\",new AbstractMacro(\"bodyContent\",(Macro)$context.getItemMap().get(\"bodyContent\")) {");
        CodeBlock render = getMacroRenderCodeBlock();
        bodyContentMacro.subCode(render);

        pushCodeBlock(render);
        ctx.block().accept(this);
        popCodeBlock();
        bodyContentMacro.footer("});");
        callMacro.subCode("$context.removeSubContext(\"$newContext\");");

        //Body部分创建新的类，然后传入要调用的宏
        return callMacro;
    }

    public CodeBlock visitExpr_array_get(@NotNull TinyTemplateParser.Expr_array_getContext ctx) {
        ctx.expression(0).accept(this);
        if (ctx.children.get(1).getText().equals("?")) {
            peekCodeLet().codeBefore("U.sa(").code(",");
        } else {
            peekCodeLet().codeBefore("U.a(").code(",");
        }
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitBlock(@NotNull TinyTemplateParser.BlockContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree node = ctx.children.get(i);
            CodeBlock codeBlock = node.accept(this);
            if (codeBlock != null) {
                peekCodeBlock().subCode(codeBlock);
            }
        }
        return null;
    }

    public CodeBlock visitExpr_compare_relational(@NotNull TinyTemplateParser.Expr_compare_relationalContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitExpr_math_binary_basic(@NotNull TinyTemplateParser.Expr_math_binary_basicContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitPara_expression_list(@NotNull TinyTemplateParser.Para_expression_listContext ctx) {

        return null;
    }

    public CodeBlock visitSet_expression(@NotNull TinyTemplateParser.Set_expressionContext ctx) {
        CodeBlock codeBlock = new CodeBlock();
        CodeLet codeLet = pushPeekCodeLet();
        codeBlock.header(codeLet);
        ctx.expression().accept(this);
        popCodeLet();
        codeLet.codeBefore("$context.put(\"" + ctx.getChild(0).getText() + "\",").lineCode(");");
        return codeBlock;
    }

    public CodeBlock visitTerminal(@org.antlr.v4.runtime.misc.NotNull org.antlr.v4.runtime.tree.TerminalNode node) {
        peekCodeLet().code(node.getText());
        return null;
    }


    public CodeBlock visitSet_directive(@NotNull TinyTemplateParser.Set_directiveContext ctx) {
        List<TinyTemplateParser.Set_expressionContext> setExpressionContexts = ctx.set_expression();
        for (TinyTemplateParser.Set_expressionContext node : setExpressionContexts) {
            CodeBlock codeBlock = new CodeBlock();
            CodeLet codeLet = pushPeekCodeLet();
            codeBlock.header(codeLet);
            node.getChild(2).accept(this);
            popCodeLet();
            if (ctx.getChild(0).getText().equals("#set(")) {
                codeLet.codeBefore("$context.put(\"" + node.getChild(0) + "\",").lineCode(");");
            } else {
                codeLet.codeBefore("$pageContext.put(\"" + node.getChild(0) + "\",").lineCode(");");
            }
            peekCodeBlock().subCode(codeBlock);
        }
        return null;
    }


    public CodeBlock visitImport_directive(@NotNull TinyTemplateParser.Import_directiveContext ctx) {
        pushCodeBlock(initCodeBlock);
        pushCodeLet();
        ctx.expression().accept(this);
        initCodeBlock.subCode(peekCodeLet().codeBefore("addImport(").lineCode(");"));
        popCodeLet();
        popCodeBlock();

        return null;
    }

    public CodeBlock visitConstant(@NotNull TinyTemplateParser.ConstantContext ctx) {
        return null;
    }


    public CodeBlock visitExpr_member_function_call(@NotNull TinyTemplateParser.Expr_member_function_callContext ctx) {
        CodeLet codeLet=new CodeLet();
        pushCodeLet(codeLet);
        ctx.expression().accept(this);
        String functionName = ctx.IDENTIFIER().getText();
        peekCodeLet().codeBefore(ctx.getChild(1).getText().equals(".") ? "U.c($template,$context," : "U.sc($template,$context,").code(",\"").code(functionName).code("\"");
        TinyTemplateParser.Expression_listContext list = ctx.expression_list();
        if (list != null) {
            peekCodeLet().code(",");
            list.accept(this);
        }
        peekCodeLet().code(")");
        popCodeLet();
        peekCodeLet().code(codeLet);
        return null;
    }

    public CodeBlock visitExpr_single_right(@NotNull TinyTemplateParser.Expr_single_rightContext ctx) {
        peekCodeLet().code("O.ce($context,\"").code(ctx.getChild(1).getText()).code("\",").code("\"" + ctx.getChild(0).getText() + "\",");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitBlank_directive(@NotNull TinyTemplateParser.Blank_directiveContext ctx) {
        CodeBlock blank = new CodeBlock();
        blank.subCode("write($writer,\" \");");
        return blank;
    }


    public CodeBlock visitExpr_array_list(@NotNull TinyTemplateParser.Expr_array_listContext ctx) {
        if (ctx.expression_range() != null) {
            CodeLet startExp = pushPeekCodeLet();
            ctx.expression_range().expression().get(0).accept(this);
            popCodeLet();
            CodeLet endExp = pushPeekCodeLet();
            ctx.expression_range().expression().get(1).accept(this);
            popCodeLet();
            peekCodeLet().code("new RangeList(%s,%s)", startExp.toString(), endExp.toString());
        } else {
            ParseTree items = ctx.getChild(1);

            for (int i = 0; i < items.getChildCount(); i++) {
                CodeLet tmp = pushPeekCodeLet();
                items.getChild(i).accept(this);
                popCodeLet();
                peekCodeLet().code(tmp);
            }
            peekCodeLet().codeBefore("new Object[]{").code("}");
        }
        return null;
    }

    public CodeBlock visitLayout_impl_directive(@NotNull TinyTemplateParser.Layout_impl_directiveContext ctx) {
        String name = "$" + ctx.IDENTIFIER().getText();
        CodeBlock positionCodeBlock = new CodeBlock();
        positionCodeBlock.subCode("if(!$context.exist(\"" + name + "\")){");
        positionCodeBlock.subCode("    Writer templateWriter = new java.io.CharArrayWriter();");
        positionCodeBlock.subCode("    new AbstractMacro(\"" + name + "\") {");
        positionCodeBlock.subCode("        protected void renderMacro(Template $template, TemplateContext $context, TemplateContext $newContext,Writer $writer) throws IOException, TemplateException {");
        CodeBlock subCodeBlock = pushPeekCodeBlock();
        ctx.block().accept(this);
        popCodeBlock();
        positionCodeBlock.subCode(subCodeBlock);
        positionCodeBlock.subCode("        }");
        positionCodeBlock.subCode("    }.renderMacro($template, $context, $newContext,templateWriter);");
        positionCodeBlock.subCode("    $context.put(\"" + name + "\",templateWriter);");
        positionCodeBlock.subCode("}");
        return positionCodeBlock;
    }


    public CodeBlock visitExpr_single_left(@NotNull TinyTemplateParser.Expr_single_leftContext ctx) {
        peekCodeLet().code("O.ce($context,\"l").code(ctx.getChild(0).getText()).code("\",").code("\"" + ctx.getChild(1).getText() + "\",");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }


    public CodeBlock visitExpr_conditional_ternary(@NotNull TinyTemplateParser.Expr_conditional_ternaryContext ctx) {
        CodeLet condition = new CodeLet();
        pushCodeLet(condition);
        ctx.expression(0).accept(this);
        popCodeLet();
        CodeLet left = new CodeLet();
        pushCodeLet(left);
        ctx.expression(1).accept(this);
        popCodeLet();
        CodeLet right = new CodeLet();
        pushCodeLet(right);
        ctx.expression(2).accept(this);
        popCodeLet();
        peekCodeLet().code("U.b(%s)?%s:%s", condition, left, right);
        return null;
    }


    private RuntimeException reportError(String message, int col, int rol, Object node) {
        if (node instanceof ParserRuleContext) {
            parser.notifyErrorListeners(((ParserRuleContext) node).getStart(), message, null);
        } else if (node instanceof TerminalNode) {
            parser.notifyErrorListeners(((TerminalNode) node).getSymbol(), message, null);
        } else if (node instanceof Token) {
            parser.notifyErrorListeners((Token) node, message, null);
        }
        return new SyntaxErrorException(message, col, rol);
    }

    public CodeBlock visitFor_expression(@NotNull TinyTemplateParser.For_expressionContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        pushCodeLet();
        ctx.expression().accept(this);
        peekCodeBlock().subCode(new CodeLet().code("$context.put(\"").code(name).code("For\",new ForIterator(").code(peekCodeLet()).lineCode("));"));
        peekCodeBlock().subCode(new CodeLet().code("while(((ForIterator)$context.get(\"").code(name).lineCode("For\")).hasNext()){"));
        CodeBlock assign = new CodeBlock().tabIndent(1);
        assign.footer(new CodeLet().code("$context.put(\"").code(name).code("\",((ForIterator)$context.get(\"").code(name).lineCode("For\")).next());")).tabIndent(1);
        peekCodeBlock().subCode(assign);
        popCodeLet();
        return null;
    }

    public CodeBlock visitDefine_expression(@NotNull TinyTemplateParser.Define_expressionContext ctx) {
        return null;
    }


    public CodeBlock visitExpr_math_binary_shift(@NotNull TinyTemplateParser.Expr_math_binary_shiftContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitEndofline_directive(@NotNull TinyTemplateParser.Endofline_directiveContext ctx) {
        peekCodeBlock().subCode("if(!getTemplateEngine().isCompactMode())write($writer,\"\\r\\n\");");
        return null;
    }


    public CodeBlock visitStop_directive(@NotNull TinyTemplateParser.Stop_directiveContext ctx) {
        TinyTemplateParser.ExpressionContext expression = ctx.expression();
        processorConditionDirective(expression, "return;");
        return null;
    }

    public CodeBlock visitBreak_directive(@NotNull TinyTemplateParser.Break_directiveContext ctx) {
        TinyTemplateParser.ExpressionContext expression = ctx.expression();
        processorConditionDirective(expression, "break;");
        return null;
    }

    private void processorConditionDirective(TinyTemplateParser.ExpressionContext expression, String directive) {
        if (expression != null) {
            pushCodeLet();
            expression.accept(this);
            CodeBlock ifCodeBlock = new CodeBlock().header(peekCodeLet().codeBefore("if(U.b(").lineCode(")){")).footer(new CodeLet().lineCode("}"));
            popCodeLet();
            ifCodeBlock.subCode(new CodeLet().lineCode(directive));
            peekCodeBlock().subCode(ifCodeBlock);
        } else {
            peekCodeBlock().subCode(new CodeLet().lineCode("if(true)" + directive + ";"));
        }
    }

    public CodeBlock visitFor_directive(@NotNull TinyTemplateParser.For_directiveContext ctx) {
        String varName = ctx.getChild(1).getChild(0).getText();
        ctx.for_expression().accept(this);
        CodeBlock forCodeBlock = new CodeBlock();
        peekCodeBlock().subCode(forCodeBlock);
        forCodeBlock.footer(new CodeLet().lineCode("}"));
        pushCodeBlock(forCodeBlock);
        ctx.block().accept(this);
        popCodeBlock();

        TinyTemplateParser.Else_directiveContext elseDirectiveContext = ctx.else_directive();
        if (elseDirectiveContext != null) {
            CodeBlock elseCodeBlock = pushPeekCodeBlock().header("if(U.b(((ForIterator)$context.get(\"" + varName + "For\")).getSize()==0)){").footer("}");
            elseDirectiveContext.block().accept(this);
            popCodeBlock();
            peekCodeBlock().subCode(elseCodeBlock);
        }
        //添加清理处理
        return null;
    }

    void pushCodeBlock(CodeBlock codeBlock) {
        codeBlocks.push(codeBlock);
    }

    void pushCodeBlock() {
        pushCodeBlock(new CodeBlock());
    }

    void popCodeBlock() {
        codeBlocks.pop();
    }


    void popCodeLet() {
        codeLets.pop();
    }

    void pushCodeLet(CodeLet codeLet) {
        codeLets.push(codeLet);
    }

    void pushCodeLet() {
        pushCodeLet(new CodeLet());
    }

    CodeLet peekCodeLet() {
        return codeLets.peek();
    }

    CodeLet pushPeekCodeLet() {
        pushCodeLet();
        return codeLets.peek();
    }

    CodeBlock peekCodeBlock() {
        return codeBlocks.peek();
    }

    CodeBlock pushPeekCodeBlock() {
        pushCodeBlock();
        return codeBlocks.peek();
    }

    public CodeBlock visitElseif_directive(@NotNull TinyTemplateParser.Elseif_directiveContext ctx) {
        pushCodeLet();
        ctx.expression().accept(this);
        CodeBlock elseifBlock = new CodeBlock().header(peekCodeLet().codeBefore("}else if(U.b(").lineCode(")){")).tabIndent(-1);
        popCodeLet();
        peekCodeBlock().subCode(elseifBlock);
        ctx.block().accept(this);
        return null;
    }

    public CodeBlock visitExpr_math_unary_prefix(@NotNull TinyTemplateParser.Expr_math_unary_prefixContext ctx) {
        peekCodeLet().code("O.e(\"l").code(ctx.getChild(0).getText()).code("\",");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitExpr_group(@NotNull TinyTemplateParser.Expr_groupContext ctx) {
        peekCodeLet().code("(");
        ctx.expression().accept(this);
        peekCodeLet().code(")");
        return null;
    }

    public CodeBlock visitExpr_constant(@NotNull TinyTemplateParser.Expr_constantContext ctx) {
        String text = ctx.getText();
        if (text.startsWith("\'")) {
            text = text.substring(1, text.length() - 1);
            text = text.replaceAll("\\\\'", "'");
            text = text.replaceAll("\\\\\"", "\"");
            text = text.replaceAll("\"", "\\\\\"");
            peekCodeLet().code("\"").code(text).code("\"");
        } else {
            peekCodeLet().code(text);
        }
        return null;
    }


    public CodeBlock visitExpr_simple_condition_ternary(@NotNull TinyTemplateParser.Expr_simple_condition_ternaryContext ctx) {
        peekCodeLet().code("O.e(\"").code(ctx.getChild(1).getText()).code("\",");
        ctx.expression(0).accept(this);
        peekCodeLet().code(",");
        ctx.expression(1).accept(this);
        peekCodeLet().code(")");
        return null;
    }
}
