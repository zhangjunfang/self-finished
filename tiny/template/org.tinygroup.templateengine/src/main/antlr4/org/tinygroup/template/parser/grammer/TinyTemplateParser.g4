
 /*
 注意：此文档初始版本来自jetbrick-template，后经过修改成为Tiny模板引擎语法文件
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 */
parser grammar TinyTemplateParser;

options {
    tokenVocab = TinyTemplateLexer; // use tokens from JetTemplateLexer.g4
}

/*
@header {
package jetbrick.template.parser.grammer;
}
*/

// -------- rule ---------------------------------------
template    :   block
            ;

block       :   (comment | directive | text | value)*
            ;


text        :   TEXT_PLAIN
            |   TEXT_CDATA
            |   TEXT_SINGLE_CHAR
            |    COMMENT_LINE
            |   COMMENT_BLOCK1
            |   COMMENT_BLOCK2
            |   TEXT_ESCAPED_CHAR
            |   TEXT_DIRECTIVE_LIKE
            ;
comment        :     COMMENT_LINE
            |   COMMENT_BLOCK1
            |   COMMENT_BLOCK2
            ;
value       :   //VALUE_COMPACT_OPEN  identify_list
               VALUE_OPEN         expression '}'
            |   VALUE_ESCAPED_OPEN expression '}'
            |  I18N_OPEN  identify_list '}'
            ;


directive   :   set_directive
            |   if_directive
            |   for_directive
            |   break_directive
            |   import_directive
            |   continue_directive
            |   stop_directive
            |   include_directive
            |   macro_directive
            |   call_block_directive
            |   layout_directive
            |   layout_impl_directive
            |   call_directive
            |   endofline_directive
            |   blank_directive
            |   tabs_directive
            |   indent_directive
            |   dent_directive
            |   call_macro_directive
            |   call_macro_block_directive
            |   bodycontent_directive
            |   invalid_directive
            ;
identify_list
            :IDENTIFIER ('.' IDENTIFIER)*
            ;

define_expression_list
            :   define_expression (','? define_expression)*
            ;
para_expression_list
            :   para_expression (','? para_expression)*
            ;
para_expression
            :   IDENTIFIER '=' expression
            |   expression
            ;

define_expression
            :   IDENTIFIER ('=' expression)?
            ;

set_directive
            :   DIRECTIVE_OPEN_SET set_expression (','? set_expression)* ')'
            ;
set_expression
            :    IDENTIFIER '=' expression
            ;
endofline_directive
            :    DIRECTIVE_END_OF_LINE
            ;
tabs_directive
            :    DIRECTIVE_TABS
            ;
blank_directive
            :    DIRECTIVE_BLANK
            ;
indent_directive
            :    DIRECTIVE_TABS_INDENT
            ;
dent_directive
            :    DIRECTIVE_TABS_DENT
            ;

if_directive
            :   DIRECTIVE_OPEN_IF expression ')' block elseif_directive* else_directive? DIRECTIVE_END
            ;
elseif_directive
            :   DIRECTIVE_OPEN_ELSEIF expression ')' block
            ;
else_directive
            :   DIRECTIVE_ELSE block
            ;

for_directive
            :   DIRECTIVE_OPEN_FOR for_expression ')' block else_directive? DIRECTIVE_END
            ;
for_expression
            :    IDENTIFIER (':'|'in') expression
            ;

break_directive
            :   DIRECTIVE_OPEN_BREAK expression?')'
            |   DIRECTIVE_BREAK
            ;
import_directive
            :   DIRECTIVE_IMPORT expression ')'
            ;

continue_directive
            :   DIRECTIVE_OPEN_CONTINUE expression? ')'
            |   DIRECTIVE_CONTINUE
            ;
stop_directive
            :   DIRECTIVE_OPEN_STOP expression? ')'
            |   DIRECTIVE_STOP
            ;

include_directive
            :   DIRECTIVE_OPEN_INCLUDE expression (','? '{' hash_map_entry_list? '}')? ')'
            ;


macro_directive
            :   DIRECTIVE_OPEN_MACRO define_expression_list? ')' block DIRECTIVE_END
            ;

layout_directive
            :   DIRECTIVE_OPEN_LAYOUT IDENTIFIER ')' block DIRECTIVE_END
            ;

call_block_directive
            :   DIRECTIVE_OPEN_CALL  expression (','?  para_expression_list )? ')' block DIRECTIVE_END
            ;
layout_impl_directive
            :   DIRECTIVE_OPEN_LAYOUT_IMPL IDENTIFIER ')' block DIRECTIVE_END
            ;

call_directive
            :   DIRECTIVE_CALL  expression ( ','? para_expression_list )? ')'
            ;

call_macro_block_directive
            :  DIRECTIVE_OPEN_MACRO_INVOKE   para_expression_list? ')' block DIRECTIVE_END
            ;

bodycontent_directive
            :DIRECTIVE_BODYCONTENT
            ;
call_macro_directive
            :  DIRECTIVE_MACRO_INVOKE   para_expression_list? ')'
            ;
invalid_directive
            :   DIRECTIVE_SET
//            |   DIRECTIVE_PUT
            |   DIRECTIVE_IF
            |   DIRECTIVE_ELSEIF
            |   DIRECTIVE_FOR
            |   DIRECTIVE_INCLUDE
//            |   DIRECTIVE_OPEN_CALL_MACRO
//            |   DIRECTIVE_BODY_CALL
            |   DIRECTIVE_MACRO
            ;

expression  :   '(' expression ')'                                           # expr_group
            |   constant                                                     # expr_constant
            |   IDENTIFIER                                                   # expr_identifier
            |   '[' (expression_list |expression_range)?  ']'       # expr_array_list
            |   '{' hash_map_entry_list? '}'                                 # expr_hash_map
            |   expression ('.'|'?.') IDENTIFIER '(' expression_list? ')'    # expr_member_function_call
            |   expression ('.'|'?.') IDENTIFIER                             # expr_field_access
            |   IDENTIFIER '(' expression_list? ')'                          # expr_function_call

            |   expression ('?')? '[' expression ']'                         # expr_array_get
            |   expression ('++'|'--')                                       # expr_single_right
            |   ('+' <assoc=right> |'-' <assoc=right>)  expression           # expr_math_unary_prefix
            |   ('++'|'--')       expression                                 # expr_single_left
            |   '~' <assoc=right> expression                                 # expr_math_unary_prefix
            |   '!' <assoc=right> expression                                 # expr_math_unary_prefix
            |   expression ('*'|'/'|'%')  expression                         # expr_math_binary_basic
            |   expression ('+'|'-')      expression                         # expr_math_binary_basic
            |   expression ('<<'|'>>'|'>>>') expression             # expr_math_binary_shift
            |   expression ('>='|'<='|'>'|'<') expression                    # expr_compare_relational
            |   expression ('=='|'!=') expression                            # expr_compare_equality
            |   expression '&'  expression                                   # expr_math_binary_bitwise
            |   expression '^' <assoc=right> expression                      # expr_math_binary_bitwise
            |   expression '|'  expression                                   # expr_math_binary_bitwise
            |   expression '&&' expression                                   # expr_compare_condition
            |   expression '||' expression                                   # expr_compare_condition
            |   expression '?' <assoc=right> expression ':' expression       # expr_conditional_ternary
            |   expression '?:' expression       # expr_simple_condition_ternary
            ;

constant    :   STRING_DOUBLE
            |   STRING_SINGLE
            |   INTEGER
            |   INTEGER_HEX
            |   FLOATING_POINT
            |   KEYWORD_TRUE
            |   KEYWORD_FALSE
            |   KEYWORD_NULL
            ;

expression_list
            :   expression (',' expression)*
            ;

hash_map_entry_list
            :   expression ':' expression (',' expression ':' expression)*
            ;

expression_range
            :   expression '..' expression
            ;



