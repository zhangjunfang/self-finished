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

import junit.framework.TestCase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.internal.runners.statements.Fail;
import org.tinygroup.template.parser.grammer.TinyTemplateLexer;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

/**
 * Created by luoguo on 2014/6/4.
 */
public class TestParser extends TestCase {
    public String execute(String template) {
        // char[]
        // source="#--aaa--##*aaa*#abc#*aaa*#aa#[[#*aaaaa*#]]#${abc}".toCharArray();
        // char[] source="${abc.name}".toCharArray();
        char[] source = template.toCharArray();
        ANTLRInputStream is = new ANTLRInputStream(source, source.length);
        is.name = "testname"; // set source file name, it will be displayed in
        // error report.
        TinyTemplateParser parser = new TinyTemplateParser(
                new CommonTokenStream(new TinyTemplateLexer(is)));
        parser.removeErrorListeners(); // remove ConsoleErrorListener
        parser.addErrorListener(TinyTemplateErrorListener.getInstance());
        parser.setErrorHandler(new TinyTemplateErrorStrategy());

        TinyTemplateParser.TemplateContext templateParseTree = parser
                .template();
        TinyTemplateCodeVisitor visitor = new TinyTemplateCodeVisitor(parser);
        CodeBlock codeBlock = templateParseTree.accept(visitor);
        return codeBlock.toString();
    }

    public void testContent() throws Exception {
        String result = execute("abc");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"abc\");") > 0);

        result = execute("<a>b</c>");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"<a>b</c>\");") > 0);

        result = execute("${'abc\"def'}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"abc\\\"def\");") > 0);

        result = execute("$!{\"<abc\"}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,U.escapeHtml((\"<abc\")));") > 0);

        result = execute("abcd\n\taddd");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"abcd\\n\\taddd\");") > 0);

        result = execute("abcd\n\t${abc}\n\taddd");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"abcd") > 0);
    }

    public void testPosition() throws Exception {
        String result = execute("#layout(aaa)hello#end");
        System.out.println(result);
        assertTrue(result.indexOf("if($context.exist(\"$aaa\")){") > 0);

    }
    public void testMissMacro() throws Exception {
        try {
            String result = execute("\n#macro  (aaa)#end");
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void testReserveWordMacro() throws Exception {
        try {
            String result = execute("\n#macro  import(aaa)#end");
            fail();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void testImport() throws Exception {
        String result = execute("#import(\"aa/bb/cc/dd\");");
        System.out.println(result);
        assertTrue(result.indexOf("addImport(\"aa/bb/cc/dd\");") > 0);

    }
    public void testPositionImpl() throws Exception {
        String result = execute("#@layout(aaa)hello#end");
        System.out.println(result);
        assertTrue(result.indexOf("if(!$context.exist(\"$aaa\")){") > 0);

    }
    public void testPositionBlank() throws Exception {
        String result = execute("#@aaa(aaa bbb)hello#end");
        System.out.println(result);
        assertTrue(result.indexOf("$newContext.put($macro.getParameterName(1),U.v($context,\"bbb\"));") > 0);

    }

    public void testList() throws Exception {
        String result = execute("#set(a=[1,2,3,5])");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",new Object[]{1,2,3,5});") > 0);

    }

    public void testMacroDefaultValue() throws Exception {
        String result = execute("#macro abc(aa=def,b='abc')#end");
        System.out.println(result);
        assertTrue(result.indexOf("addParameter(\"aa\",new EvaluateExpression() {public Object evaluate(TemplateContext $context)throws TemplateException{return U.v($context,\"def\");}});addParameter(\"b\",new EvaluateExpression() {public Object evaluate(TemplateContext $context)throws TemplateException{return \"abc\";}});") > 0);

    }

    public void testRange() throws Exception {
        String result = execute("#set(a=[1..a])");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",new RangeList(1,U.v($context,\"a\")));") > 0);

    }

    public void testSet() throws Exception {
        String result = execute("#set(a=1)");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",1);") > 0);

        result = execute("#set(a=1>2)");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",O.e(\">\",1,2));") > 0);

        result = execute("#set(arraylist = [\"a\",\"b\",\"v\",\"d\"])");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"arraylist\",new Object[]{\"a\",\"b\",\"v\",\"d\"});") > 0);
        result = execute("#set(arraylist = [\"a\",aa,\"v\",\"d\"])");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"arraylist\",new Object[]{\"a\",U.v($context,\"aa\"),\"v\",\"d\"});") > 0);
        result = execute("#set(arraylist = \"124a\",a=3,b=4)");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"arraylist\",\"124a\");") > 0);
        assertTrue(result.indexOf("$context.put(\"a\",3);") > 0);
        assertTrue(result.indexOf("$context.put(\"b\",4);") > 0);

        result = execute("#set(arraylist = {\"a\":1,\"b\":1} )");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"arraylist\",new TemplateMap().putItem(\"a\",1).putItem(\"b\",1));") > 0);
        result = execute("#set(arraylist = {\"a\":[1,aa,3],\"b\":1} )");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"arraylist\",new TemplateMap().putItem(\"a\",new Object[]{1,U.v($context,\"aa\"),3}).putItem(\"b\",1));") > 0);

        result = execute("#set(a=null)");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",null);") > 0);
    }
    public void testMacroBlank() throws Exception {
        String result = execute("aaa\n   #abc()");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"aaa") > 0);

        result = execute("#macro test(aa bb cc)abc #end#macro test1(bb)def #end");
        System.out.println(result);
        assertTrue(result.indexOf("class test1 extends AbstractMacro {") > 0);
        assertTrue(result.indexOf("class test extends AbstractMacro {") > 0);
        assertTrue(result.indexOf("write($writer,\"abc") > 0);
        assertTrue(result.indexOf("write($writer,\"def") > 0);
    }
    public void testMacro() throws Exception {
        String result = execute("aaa\n   #abc()");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"aaa") > 0);

        result = execute("#macro test(aa,bb,cc)abc #end#macro test1(bb)def #end");
        System.out.println(result);
        assertTrue(result.indexOf("class test1 extends AbstractMacro {") > 0);
        assertTrue(result.indexOf("class test extends AbstractMacro {") > 0);
        assertTrue(result.indexOf("write($writer,\"abc") > 0);
        assertTrue(result.indexOf("write($writer,\"def") > 0);

        result = execute("#test(aa)");
        System.out.println(result);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(0),U.v($context,\"aa\"));") > 0);
        assertTrue(result
                .indexOf("$macro=getTemplateEngine().findMacro(\"test\",$template,$context);") > 0);

        result = execute("#test(aa=1,bb=2,3)");
        System.out.println(result);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(2),3);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"aa\",1);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"bb\",2);") > 0);
        assertTrue(result
                .indexOf("$macro.render($template,$context,$newContext,$writer);") > 0);
    }

    public void testEol() throws Exception {
        String result = execute("aa#{eol}bb#eol");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"\\r\\n\");") > 0);
        assertTrue(result.indexOf("write($writer,\"aa\");") > 0);
        assertTrue(result.indexOf("write($writer,\"bb\");") > 0);
    }

    public void testBodyContent() throws Exception {
        String result = execute("#bodyContent");
        System.out.println(result);
        assertTrue(result
                .indexOf("$macro.render($template,$context,$newContext,$writer);") > 0);
    }

    public void testExpressionIdentifier() throws Exception {
        String result = execute("$!{abc}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.escapeHtml((U.v($context,\"abc\"))));") > 0);
        result = execute("${1==2}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\"==\",1,2));") > 0);
    }

    public void testExprFieldAccess() throws Exception {
        String result = execute("${b?.c}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.sp(U.v($context,\"b\"),\"c\"));") > 0);
        result = execute("${aa.bb}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.p(U.v($context,\"aa\"),\"bb\"));") > 0);
        result = execute("${itemFor.index%2}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"%\",U.p(U.v($context,\"itemFor\"),\"index\"),2));") > 0);

        result = execute("${a1.b.v}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.p(U.p(U.v($context,\"a1\"),\"b\"),\"v\"));") > 0);
    }

    public void testExprMemberFunctionCall() throws Exception {
        String result = execute("${abc.aa(bb(1))}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.c($template,$context,U.v($context,\"abc\"),\"aa\",getTemplateEngine().executeFunction($template,$context,\"bb\",1)));") > 0);
        result = execute("${aa.bb(1,2,bb)}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.c($template,$context,U.v($context,\"aa\"),\"bb\",1,2,U.v($context,\"bb\")));") > 0);

        result = execute("${b.abc()}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.c($template,$context,U.v($context,\"b\"),\"abc\"));") > 0);
    }

    public void testExprMathUnarySuffix() throws Exception {
        String result = execute("${2++}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.ce($context,\"++\",\"2\",2));") > 0);

        result = execute("${2--}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.ce($context,\"--\",\"2\",2));") > 0);
    }

    public void testExprMathBinaryShift() throws Exception {
        String result = execute("${2>>1}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\">>\",2,1));") > 0);

        result = execute("${2<<1}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\"<<\",2,1));") > 0);

        result = execute("${2>>>1}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\">>>\",2,1));") > 0);
    }

    public void testExprSimpleConditionTernary() throws Exception {
        String result = execute("${2?:1}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\"?:\",2,1));") > 0);
    }

    public void testExprFunctionCall() throws Exception {
        String result = execute("${abc(2)}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,getTemplateEngine().executeFunction($template,$context,\"abc\",2));") > 0);

        result = execute("${aa()}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,getTemplateEngine().executeFunction($template,$context,\"aa\"));") > 0);

        result = execute("${format('this is %s',2)}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,getTemplateEngine().executeFunction($template,$context,\"format\",\"this is %s\",2));") > 0);
        ;

        result = execute("${a(b+1)}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,getTemplateEngine().executeFunction($template,$context,\"a\",O.e(\"+\",U.v($context,\"b\"),1)));") > 0);

    }

    public void testI18N() throws Exception {
        String result = execute("$${abc.bb}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.getI18n($template.getTemplateEngine().getI18nVistor(),$context,\"abc.bb\"));") > 0);
    }

    public void testExprArrayGet() throws Exception {
        String result = execute("${map[\"id\"]}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.a(U.v($context,\"map\"),\"id\"));") > 0);
    }

    public void testInclude() throws Exception {
        String result = execute("#include(\"aaa/bb/cc/dd\",{a:2,b:3,c:4})");
        System.out.println(result);
        assertTrue(result
                .indexOf("$newContext.putAll(new TemplateMap().putItem(U.v($context,\"a\"),2).putItem(U.v($context,\"b\"),3).putItem(U.v($context,\"c\"),4));") > 0);
        assertTrue(result
                .indexOf("$context.putSubContext(\"$newContext\",$newContext);") > 0);
        assertTrue(result
                .indexOf("getTemplateEngine().renderTemplateWithOutLayout(U.getPath(getPath(),\"aaa/bb/cc/dd\"),$newContext,$writer);") > 0);
        assertTrue(result
                .indexOf("$context.removeSubContext(\"$newContext\");") > 0);

        result = execute("#include(\"aaa/bb/cc/dd\")");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.putSubContext(\"$newContext\",$newContext);") > 0);
        assertTrue(result
                .indexOf("getTemplateEngine().renderTemplateWithOutLayout(U.getPath(getPath(),\"aaa/bb/cc/dd\"),$newContext,$writer);") > 0);
        assertTrue(result
                .indexOf("$context.removeSubContext(\"$newContext\");") > 0);
    }

    public void testCommentBlock1() throws Exception {
        String result = execute("a#--abc--#b");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"a\");") > 0);
        assertTrue(result.indexOf("write($writer,\"b\");") > 0);
    }

    public void testExprMathUnaryPrefix() throws Exception {
        String result = execute("${-123}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\"l-\",123));") > 0);
    }

    public void testIf() throws Exception {
        String result = execute("#if(true)aaa#end");
        System.out.println(result);
        assertTrue(result.indexOf("if(U.b(true)){") > 0);
        assertTrue(result.indexOf("write($writer,\"aaa\");") > 0);

        result = execute("#if(true)bbb#{else}aaa#end");
        System.out.println(result);
        assertTrue(result.indexOf("if(U.b(true)){") > 0);
        assertTrue(result.indexOf("write($writer,\"aaa\");") > 0);
        assertTrue(result.indexOf("write($writer,\"bbb\");") > 0);

        result = execute("#if(true)abc#if(false)def#end#end");
        System.out.println(result);
        assertTrue(result.indexOf("if(U.b(true)){") > 0);
        assertTrue(result.indexOf("if(U.b(false)){") > 0);
        assertTrue(result.indexOf("write($writer,\"abc\");") > 0);
        assertTrue(result.indexOf("write($writer,\"def\");") > 0);

        result = execute("#if(true)abc#{end}def");
        System.out.println(result);
        assertTrue(result.indexOf("if(U.b(true)){") > 0);
        assertTrue(result.indexOf("write($writer,\"abc\");") > 0);
        assertTrue(result.indexOf("write($writer,\"def\");") > 0);

        result = execute("#if(true)abc#elseif(true)ddd #{else}def#end");
        System.out.println(result);
        assertTrue(result.indexOf("if(U.b(true)){") > 0);
        assertTrue(result.indexOf("}else if(U.b(true)){") > 0);
        assertTrue(result.indexOf("}else{") > 0);
        assertTrue(result.indexOf("write($writer,\"abc\");") > 0);
        assertTrue(result.indexOf("write($writer,\"def\");") > 0);
    }

    public void testCommentBlock2() throws Exception {
        String result = execute("a#*abc*#b");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"a\");") > 0);
        assertTrue(result.indexOf("write($writer,\"b\");") > 0);
    }

    public void testCommentLine() throws Exception {
        String result = execute("ab##cd");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"ab\");") > 0);

        result = execute("##abc");
        System.out.println(result);
        assertTrue(result.indexOf("$writer.write") < 0);
    }

    public void testTransfer() throws Exception {
        String result = execute("${123}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,123);") > 0);

        result = execute("${\"123\"}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,\"123\");") > 0);
    }

    public void testExprMathBinaryBasic() throws Exception {
        String result = execute("${1+2}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\"+\",1,2));") > 0);

        result = execute("${a+1}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"+\",U.v($context,\"a\"),1));") > 0);
        result = execute("${a+b}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"+\",U.v($context,\"a\"),U.v($context,\"b\")));") > 0);
    }

    public void testExprCompareEquality() throws Exception {
        String result = execute("${1==2}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,O.e(\"==\",1,2));") > 0);

        result = execute("${1==2*3}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"==\",1,O.e(\"*\",2,3)));") > 0);
        result = execute("${aa==2*3}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"==\",U.v($context,\"aa\"),O.e(\"*\",2,3)));") > 0);
    }

    public void testExpressionRange() throws Exception {
        String result = execute("#set(a=[1..2])");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",new RangeList(1,2));") > 0);
    }

    public void testFor() throws Exception {
        String result = execute("#for(i: [1,2,3,4,5]) #if(true)abc${i}#break(true||false)#end #end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"iFor\",new ForIterator(new Object[]{1,2,3,4,5}));") > 0);
        assertTrue(result
                .indexOf("while(((ForIterator)$context.get(\"iFor\")).hasNext()){") > 0);
        assertTrue(result
                .indexOf("$context.put(\"i\",((ForIterator)$context.get(\"iFor\")).next());") > 0);

        result = execute("#for(i : [])ddd#end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"iFor\",new ForIterator(new Object[]{}));") > 0);
        assertTrue(result
                .indexOf("$context.put(\"i\",((ForIterator)$context.get(\"iFor\")).next());") > 0);

        result = execute("#for(i : {})ddd#end");
        System.out.println(result);
        assertTrue(result
                .indexOf("while(((ForIterator)$context.get(\"iFor\")).hasNext()){") > 0);
        assertTrue(result
                .indexOf("$context.put(\"i\",((ForIterator)$context.get(\"iFor\")).next());") > 0);

        result = execute("#for(i : [1,2,3,4,5])#{else}ddd#end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$context.put(\"iFor\",new ForIterator(new Object[]{1,2,3,4,5}));") > 0);
        assertTrue(result
                .indexOf("if(U.b(((ForIterator)$context.get(\"iFor\")).getSize()==0)){") > 0);
        assertTrue(result
                .indexOf("$context.put(\"i\",((ForIterator)$context.get(\"iFor\")).next());") > 0);
    }

    public void testExprCompareCondition() throws Exception {
        String result = execute("${true||false}");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,U.b(true)||U.b(false));") > 0);
    }

    public void testExprConditionalTernary() throws Exception {
        String result = execute("${true? 1 : 2 }");
        System.out.println(result);
        assertTrue(result.indexOf("write($writer,U.b(true)?1:2);") > 0);

        result = execute("#set(a=true? 1 : 2 )");
        System.out.println(result);
        assertTrue(result.indexOf("$context.put(\"a\",U.b(true)?1:2);") > 0);

        result = execute("${a?1:2}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,U.b(U.v($context,\"a\"))?1:2);") > 0);
    }

    public void testExprMathBinaryBitwise() throws Exception {
        String result = execute("${4|6|6}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"|\",O.e(\"|\",4,6),6));") > 0);

        result = execute("${4+6+6^3|3}");
        System.out.println(result);
        assertTrue(result
                .indexOf("write($writer,O.e(\"|\",O.e(\"^\",O.e(\"+\",O.e(\"+\",4,6),6),3),3));") > 0);
    }
    public void testCallBlankDirective() throws Exception {
        String result = execute("#call(\"test\" aa=1 bb=2,3)");
        System.out.println(result);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(2),3);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"aa\",1);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"bb\",2);") > 0);
        assertTrue(result
                .indexOf("$macro.render($template,$context,$newContext,$writer);") > 0);

        result = execute("#call(\"test\")#end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$macro.render($template,$context,$newContext,$writer);") > 0);

        result = execute("#@test(aa=1,bb=2,3) aaa #bbb(1)  #end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$macro=getTemplateEngine().findMacro(\"bbb\",$template,$context);") > 0);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(0),1);") > 0);
    }
    public void testCallDirective() throws Exception {
        String result = execute("#call(\"test\",aa=1,bb=2,3)");
        System.out.println(result);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(2),3);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"aa\",1);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"bb\",2);") > 0);
        assertTrue(result
                .indexOf("$macro.render($template,$context,$newContext,$writer);") > 0);

        result = execute("#call(\"test\")#end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$macro.render($template,$context,$newContext,$writer);") > 0);

        result = execute("#@test(aa=1,bb=2,3) aaa #bbb(1)  #end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$macro=getTemplateEngine().findMacro(\"bbb\",$template,$context);") > 0);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(0),1);") > 0);
    }

    public void testCallBlockDirective() throws Exception {
        String result = execute("#@call('test',aa=1,bb=2,3) aaa  #end");
        System.out.println(result);
        assertTrue(result.indexOf("$newContext.put(\"aa\",1);") > 0);
        assertTrue(result.indexOf("$newContext.put(\"bb\",2);") > 0);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(2),3);") > 0);
    }

    public void testCallMacroBlockDirective() throws Exception {
        String result = execute("#macro test(aaa)ddd#end #@test(aa=1,bb=2,3) aaa #@bbb(9)bb#end  #end");
        System.out.println(result);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(0),9);") > 0);
        assertTrue(result
                .indexOf("$newContext.put($macro.getParameterName(2),3);") > 0);
    }
}
