
  /*
  注意：此文档初始版本来自jetbrick-template，后经过修改成为Tiny模板引擎词法文件
  * jetbrick-template
  * http://subchen.github.io/jetbrick-template/
  *
  * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
  * Email: subchen@gmail.com
  */
lexer grammar TinyTemplateLexer;
/*
@header {
package jetbrick.template.parser.grammer;
}
*/
// *******************************************************************
// ------- DEFAULT mode for Plain Text -------------------------------
COMMENT_LINE           : '##' ~[\r\n]* NEWLINE          ;
COMMENT_BLOCK2            : '#*' .*? '*#'                 ;
COMMENT_BLOCK1            : '#--' .*? '--#'                  ;
fragment NEWLINE        : ('\r'? '\n' | EOF)              ;

TEXT_PLAIN              : ~('$'|'#'|'\\')+                ;
TEXT_CDATA              : '#[[' .*? ']]#'                 ;
TEXT_ESCAPED_CHAR       : ('\\#'|'\\$'|'\\\\')            ;
TEXT_SINGLE_CHAR        : ('#'|'$'|'\\')                  ;
PARA_SPLITER           :[ \t]* (',')?[ \t]*    ;
I18N_OPEN              : '$${'                            -> pushMode(INSIDE) ;

//VALUE_COMPACT_OPEN              : '$'                    ;
VALUE_OPEN              : '${'                            -> pushMode(INSIDE) ;
VALUE_ESCAPED_OPEN      : '$!{'                           -> pushMode(INSIDE) ;

DIRECTIVE_OPEN_SET      : ('#set'|'#!set' )     ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_IF       : '#if'       ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_ELSEIF   : '#elseif'   ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_FOR      : ('#for'|'#foreach')      ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_BREAK    : '#break'    ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_CONTINUE : '#continue' ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_STOP     : '#stop'     ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_INCLUDE  : '#include'  ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_CALL  : '#call'  ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_CALL  : '#@call'  ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_LAYOUT_IMPL  : '#@layout'  ARGUMENT_START      -> pushMode(INSIDE) ;

DIRECTIVE_OPEN_MACRO    : '#macro'    [ \t]+ ID ARGUMENT_START -> pushMode(INSIDE) ;

DIRECTIVE_OPEN_LAYOUT   : '#layout' ARGUMENT_START -> pushMode(INSIDE) ;

fragment ID             : [_a-zA-Z$][_a-zA-Z$0-9]*        ;
fragment ARGUMENT_START : [ \t]* '('                      ;

DIRECTIVE_SET           : '#set'|'#!set'                          ;
DIRECTIVE_IF            : '#if'                           ;
DIRECTIVE_ELSEIF        : '#elseif'                       ;
DIRECTIVE_FOR           : '#for'                          ;
DIRECTIVE_INCLUDE       : '#include'                      ;
DIRECTIVE_BREAK         : '#break'                        ;
DIRECTIVE_CONTINUE      : '#continue'                     ;
DIRECTIVE_STOP          : '#stop'                         ;
DIRECTIVE_MACRO         : '#macro'                        ;

DIRECTIVE_ELSE          : '#else'|'#{else}'                   ;
DIRECTIVE_END           : '#end'|'#{end}'                 ;
DIRECTIVE_BLANK         : '#b'|'#{b}'                       ;
DIRECTIVE_END_OF_LINE           : '#eol'|'#{eol}'                 ;
DIRECTIVE_TABS           : '#t'|'#{t}'                 ;
DIRECTIVE_TABS_INDENT           : '#]'|'#{]}'                 ;
DIRECTIVE_TABS_DENT           : '#['|'#{[}'                 ;
DIRECTIVE_BODYCONTENT  : '#bodyContent'|'#{bodyContent}' ;
DIRECTIVE_IMPORT  : '#import' ARGUMENT_START      -> pushMode(INSIDE) ;

//DIRECTIVE_CALL    : '#' ID      ;
DIRECTIVE_MACRO_INVOKE    : '#' ID     ARGUMENT_START      -> pushMode(INSIDE) ;
DIRECTIVE_OPEN_MACRO_INVOKE    : '#@' ID    ARGUMENT_START -> pushMode(INSIDE) ;

// It is a text which like a directive.
// It must be put after directive defination to avoid confliction.
TEXT_DIRECTIVE_LIKE     : '#' [a-zA-Z0-9]+                ;


// *******************************************************************
// -------- INSIDE mode for directive --------------------------------
mode INSIDE;

WHITESPACE              : [ \t\r\n]+                       -> skip ;

LEFT_PARENTHESE         : '('                              -> pushMode(INSIDE) ;
RIGHT_PARENTHESE        : ')'                              -> popMode ;
LEFT_BRACKET            : '['                              ;
RIGHT_BRACKET           : ']'                              ;
LEFT_BRACE              : '{'                              -> pushMode(INSIDE) ;
RIGHT_BRACE             : '}'                              -> popMode ;
IN                       : 'in'                             ;
OP_ASSIGNMENT           : '='                              ;

OP_DOT_DOT              :  '..'                              ;
OP_DOT_INVOCATION       : '.'                              ;
OP_DOT_INVOCATION_SAFE  : '?.'                             ;

OP_EQUALITY_EQ          : '=='                             ;
OP_EQUALITY_NE          : '!='                             ;

OP_RELATIONAL_GT        : '>'                              ;
OP_RELATIONAL_LT        : '<'                              ;
OP_RELATIONAL_GE        : '>='                             ;
OP_RELATIONAL_LE        : '<='                             ;

OP_CONDITIONAL_AND      : '&&'                             ;
OP_CONDITIONAL_OR       : '||'                             ;
OP_CONDITIONAL_NOT      : '!'                              ;

OP_MATH_PLUS            : '+'                              ;
OP_MATH_MINUS           : '-'                              ;
OP_MATH_MULTIPLICATION  : '*'                              ;
OP_MATH_DIVISION        : '/'                              ;
OP_MATH_REMAINDER       : '%'                              ;
OP_MATH_INCREMENT       : '++'                             ;
OP_MATH_DECREMENT       : '--'                             ;

OP_BITWISE_AND          : '&'                              ;
OP_BITWISE_OR           : '|'                              ;
OP_BITWISE_NOT          : '~'                              ;
OP_BITWISE_XOR          : '^'                              ;
OP_BITWISE_SHL          : '<<'                             ;

OP_BITWISE_SHR        : '>>'                             ;
OP_BITWISE_SHR_2      : '>>>'                            ;

OP_CONDITIONAL_TERNARY  : '?'                              ;

OP_SIMPLE_CONDITION_TERNARY      : '?:'                           ;

COMMA                   : ','                              ;
COLON                   : ':'                              ;
AT                      : '@'                              ;

KEYWORD_TRUE            : 'true'                           ;
KEYWORD_FALSE           : 'false'                          ;
KEYWORD_NULL            : 'null'                           ;


IDENTIFIER              : [_a-zA-Z][_a-zA-Z0-9]*         ;

INTEGER                 : INT [lLfFdD]?                    ;
INTEGER_HEX             : '0x' HEX+ [lL]?                  ;
FLOATING_POINT          : INT ('.' FRAC)? EXP? [fFdD]?     ;
fragment INT            : '0' | [1-9] [0-9]*               ;
fragment FRAC           : [0-9]+                           ;
fragment EXP            : [Ee] [+\-]? INT                  ;

STRING_DOUBLE           : '"'  (ESC|.)*? '"'               ;
STRING_SINGLE           : '\'' (ESC|.)*? '\''              ;
fragment ESC            : '\\' ([btnfr"'\\]|UNICODE)       ;
fragment UNICODE        : 'u' HEX HEX HEX HEX              ;
fragment HEX            : [0-9a-fA-F]                      ;
