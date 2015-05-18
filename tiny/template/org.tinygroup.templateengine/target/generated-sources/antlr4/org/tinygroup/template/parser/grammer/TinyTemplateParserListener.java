// Generated from org\tinygroup\template\parser\grammer\TinyTemplateParser.g4 by ANTLR 4.2.2
package org.tinygroup.template.parser.grammer;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TinyTemplateParser}.
 */
public interface TinyTemplateParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#import_directive}.
	 * @param ctx the parse tree
	 */
	void enterImport_directive(@NotNull TinyTemplateParser.Import_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#import_directive}.
	 * @param ctx the parse tree
	 */
	void exitImport_directive(@NotNull TinyTemplateParser.Import_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(@NotNull TinyTemplateParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(@NotNull TinyTemplateParser.ConstantContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#layout_impl_directive}.
	 * @param ctx the parse tree
	 */
	void enterLayout_impl_directive(@NotNull TinyTemplateParser.Layout_impl_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#layout_impl_directive}.
	 * @param ctx the parse tree
	 */
	void exitLayout_impl_directive(@NotNull TinyTemplateParser.Layout_impl_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#para_expression_list}.
	 * @param ctx the parse tree
	 */
	void enterPara_expression_list(@NotNull TinyTemplateParser.Para_expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#para_expression_list}.
	 * @param ctx the parse tree
	 */
	void exitPara_expression_list(@NotNull TinyTemplateParser.Para_expression_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_member_function_call}.
	 * @param ctx the parse tree
	 */
	void enterExpr_member_function_call(@NotNull TinyTemplateParser.Expr_member_function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_member_function_call}.
	 * @param ctx the parse tree
	 */
	void exitExpr_member_function_call(@NotNull TinyTemplateParser.Expr_member_function_callContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#continue_directive}.
	 * @param ctx the parse tree
	 */
	void enterContinue_directive(@NotNull TinyTemplateParser.Continue_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#continue_directive}.
	 * @param ctx the parse tree
	 */
	void exitContinue_directive(@NotNull TinyTemplateParser.Continue_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#tabs_directive}.
	 * @param ctx the parse tree
	 */
	void enterTabs_directive(@NotNull TinyTemplateParser.Tabs_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#tabs_directive}.
	 * @param ctx the parse tree
	 */
	void exitTabs_directive(@NotNull TinyTemplateParser.Tabs_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_single_right}.
	 * @param ctx the parse tree
	 */
	void enterExpr_single_right(@NotNull TinyTemplateParser.Expr_single_rightContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_single_right}.
	 * @param ctx the parse tree
	 */
	void exitExpr_single_right(@NotNull TinyTemplateParser.Expr_single_rightContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_field_access}.
	 * @param ctx the parse tree
	 */
	void enterExpr_field_access(@NotNull TinyTemplateParser.Expr_field_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_field_access}.
	 * @param ctx the parse tree
	 */
	void exitExpr_field_access(@NotNull TinyTemplateParser.Expr_field_accessContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_compare_relational}.
	 * @param ctx the parse tree
	 */
	void enterExpr_compare_relational(@NotNull TinyTemplateParser.Expr_compare_relationalContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_compare_relational}.
	 * @param ctx the parse tree
	 */
	void exitExpr_compare_relational(@NotNull TinyTemplateParser.Expr_compare_relationalContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#define_expression}.
	 * @param ctx the parse tree
	 */
	void enterDefine_expression(@NotNull TinyTemplateParser.Define_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#define_expression}.
	 * @param ctx the parse tree
	 */
	void exitDefine_expression(@NotNull TinyTemplateParser.Define_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#call_directive}.
	 * @param ctx the parse tree
	 */
	void enterCall_directive(@NotNull TinyTemplateParser.Call_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#call_directive}.
	 * @param ctx the parse tree
	 */
	void exitCall_directive(@NotNull TinyTemplateParser.Call_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_math_unary_prefix}.
	 * @param ctx the parse tree
	 */
	void enterExpr_math_unary_prefix(@NotNull TinyTemplateParser.Expr_math_unary_prefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_math_unary_prefix}.
	 * @param ctx the parse tree
	 */
	void exitExpr_math_unary_prefix(@NotNull TinyTemplateParser.Expr_math_unary_prefixContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(@NotNull TinyTemplateParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(@NotNull TinyTemplateParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#text}.
	 * @param ctx the parse tree
	 */
	void enterText(@NotNull TinyTemplateParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#text}.
	 * @param ctx the parse tree
	 */
	void exitText(@NotNull TinyTemplateParser.TextContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#endofline_directive}.
	 * @param ctx the parse tree
	 */
	void enterEndofline_directive(@NotNull TinyTemplateParser.Endofline_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#endofline_directive}.
	 * @param ctx the parse tree
	 */
	void exitEndofline_directive(@NotNull TinyTemplateParser.Endofline_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#identify_list}.
	 * @param ctx the parse tree
	 */
	void enterIdentify_list(@NotNull TinyTemplateParser.Identify_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#identify_list}.
	 * @param ctx the parse tree
	 */
	void exitIdentify_list(@NotNull TinyTemplateParser.Identify_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expression_range}.
	 * @param ctx the parse tree
	 */
	void enterExpression_range(@NotNull TinyTemplateParser.Expression_rangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expression_range}.
	 * @param ctx the parse tree
	 */
	void exitExpression_range(@NotNull TinyTemplateParser.Expression_rangeContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_group}.
	 * @param ctx the parse tree
	 */
	void enterExpr_group(@NotNull TinyTemplateParser.Expr_groupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_group}.
	 * @param ctx the parse tree
	 */
	void exitExpr_group(@NotNull TinyTemplateParser.Expr_groupContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#hash_map_entry_list}.
	 * @param ctx the parse tree
	 */
	void enterHash_map_entry_list(@NotNull TinyTemplateParser.Hash_map_entry_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#hash_map_entry_list}.
	 * @param ctx the parse tree
	 */
	void exitHash_map_entry_list(@NotNull TinyTemplateParser.Hash_map_entry_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#stop_directive}.
	 * @param ctx the parse tree
	 */
	void enterStop_directive(@NotNull TinyTemplateParser.Stop_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#stop_directive}.
	 * @param ctx the parse tree
	 */
	void exitStop_directive(@NotNull TinyTemplateParser.Stop_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#call_macro_block_directive}.
	 * @param ctx the parse tree
	 */
	void enterCall_macro_block_directive(@NotNull TinyTemplateParser.Call_macro_block_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#call_macro_block_directive}.
	 * @param ctx the parse tree
	 */
	void exitCall_macro_block_directive(@NotNull TinyTemplateParser.Call_macro_block_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#blank_directive}.
	 * @param ctx the parse tree
	 */
	void enterBlank_directive(@NotNull TinyTemplateParser.Blank_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#blank_directive}.
	 * @param ctx the parse tree
	 */
	void exitBlank_directive(@NotNull TinyTemplateParser.Blank_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_compare_equality}.
	 * @param ctx the parse tree
	 */
	void enterExpr_compare_equality(@NotNull TinyTemplateParser.Expr_compare_equalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_compare_equality}.
	 * @param ctx the parse tree
	 */
	void exitExpr_compare_equality(@NotNull TinyTemplateParser.Expr_compare_equalityContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#for_directive}.
	 * @param ctx the parse tree
	 */
	void enterFor_directive(@NotNull TinyTemplateParser.For_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#for_directive}.
	 * @param ctx the parse tree
	 */
	void exitFor_directive(@NotNull TinyTemplateParser.For_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#elseif_directive}.
	 * @param ctx the parse tree
	 */
	void enterElseif_directive(@NotNull TinyTemplateParser.Elseif_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#elseif_directive}.
	 * @param ctx the parse tree
	 */
	void exitElseif_directive(@NotNull TinyTemplateParser.Elseif_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_single_left}.
	 * @param ctx the parse tree
	 */
	void enterExpr_single_left(@NotNull TinyTemplateParser.Expr_single_leftContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_single_left}.
	 * @param ctx the parse tree
	 */
	void exitExpr_single_left(@NotNull TinyTemplateParser.Expr_single_leftContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#break_directive}.
	 * @param ctx the parse tree
	 */
	void enterBreak_directive(@NotNull TinyTemplateParser.Break_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#break_directive}.
	 * @param ctx the parse tree
	 */
	void exitBreak_directive(@NotNull TinyTemplateParser.Break_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#call_block_directive}.
	 * @param ctx the parse tree
	 */
	void enterCall_block_directive(@NotNull TinyTemplateParser.Call_block_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#call_block_directive}.
	 * @param ctx the parse tree
	 */
	void exitCall_block_directive(@NotNull TinyTemplateParser.Call_block_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#indent_directive}.
	 * @param ctx the parse tree
	 */
	void enterIndent_directive(@NotNull TinyTemplateParser.Indent_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#indent_directive}.
	 * @param ctx the parse tree
	 */
	void exitIndent_directive(@NotNull TinyTemplateParser.Indent_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#dent_directive}.
	 * @param ctx the parse tree
	 */
	void enterDent_directive(@NotNull TinyTemplateParser.Dent_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#dent_directive}.
	 * @param ctx the parse tree
	 */
	void exitDent_directive(@NotNull TinyTemplateParser.Dent_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#set_expression}.
	 * @param ctx the parse tree
	 */
	void enterSet_expression(@NotNull TinyTemplateParser.Set_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#set_expression}.
	 * @param ctx the parse tree
	 */
	void exitSet_expression(@NotNull TinyTemplateParser.Set_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#template}.
	 * @param ctx the parse tree
	 */
	void enterTemplate(@NotNull TinyTemplateParser.TemplateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#template}.
	 * @param ctx the parse tree
	 */
	void exitTemplate(@NotNull TinyTemplateParser.TemplateContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_array_list}.
	 * @param ctx the parse tree
	 */
	void enterExpr_array_list(@NotNull TinyTemplateParser.Expr_array_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_array_list}.
	 * @param ctx the parse tree
	 */
	void exitExpr_array_list(@NotNull TinyTemplateParser.Expr_array_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#set_directive}.
	 * @param ctx the parse tree
	 */
	void enterSet_directive(@NotNull TinyTemplateParser.Set_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#set_directive}.
	 * @param ctx the parse tree
	 */
	void exitSet_directive(@NotNull TinyTemplateParser.Set_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_hash_map}.
	 * @param ctx the parse tree
	 */
	void enterExpr_hash_map(@NotNull TinyTemplateParser.Expr_hash_mapContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_hash_map}.
	 * @param ctx the parse tree
	 */
	void exitExpr_hash_map(@NotNull TinyTemplateParser.Expr_hash_mapContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_conditional_ternary}.
	 * @param ctx the parse tree
	 */
	void enterExpr_conditional_ternary(@NotNull TinyTemplateParser.Expr_conditional_ternaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_conditional_ternary}.
	 * @param ctx the parse tree
	 */
	void exitExpr_conditional_ternary(@NotNull TinyTemplateParser.Expr_conditional_ternaryContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_math_binary_shift}.
	 * @param ctx the parse tree
	 */
	void enterExpr_math_binary_shift(@NotNull TinyTemplateParser.Expr_math_binary_shiftContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_math_binary_shift}.
	 * @param ctx the parse tree
	 */
	void exitExpr_math_binary_shift(@NotNull TinyTemplateParser.Expr_math_binary_shiftContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void enterExpression_list(@NotNull TinyTemplateParser.Expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void exitExpression_list(@NotNull TinyTemplateParser.Expression_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#macro_directive}.
	 * @param ctx the parse tree
	 */
	void enterMacro_directive(@NotNull TinyTemplateParser.Macro_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#macro_directive}.
	 * @param ctx the parse tree
	 */
	void exitMacro_directive(@NotNull TinyTemplateParser.Macro_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#bodycontent_directive}.
	 * @param ctx the parse tree
	 */
	void enterBodycontent_directive(@NotNull TinyTemplateParser.Bodycontent_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#bodycontent_directive}.
	 * @param ctx the parse tree
	 */
	void exitBodycontent_directive(@NotNull TinyTemplateParser.Bodycontent_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(@NotNull TinyTemplateParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(@NotNull TinyTemplateParser.ValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#para_expression}.
	 * @param ctx the parse tree
	 */
	void enterPara_expression(@NotNull TinyTemplateParser.Para_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#para_expression}.
	 * @param ctx the parse tree
	 */
	void exitPara_expression(@NotNull TinyTemplateParser.Para_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#include_directive}.
	 * @param ctx the parse tree
	 */
	void enterInclude_directive(@NotNull TinyTemplateParser.Include_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#include_directive}.
	 * @param ctx the parse tree
	 */
	void exitInclude_directive(@NotNull TinyTemplateParser.Include_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#define_expression_list}.
	 * @param ctx the parse tree
	 */
	void enterDefine_expression_list(@NotNull TinyTemplateParser.Define_expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#define_expression_list}.
	 * @param ctx the parse tree
	 */
	void exitDefine_expression_list(@NotNull TinyTemplateParser.Define_expression_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#invalid_directive}.
	 * @param ctx the parse tree
	 */
	void enterInvalid_directive(@NotNull TinyTemplateParser.Invalid_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#invalid_directive}.
	 * @param ctx the parse tree
	 */
	void exitInvalid_directive(@NotNull TinyTemplateParser.Invalid_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_compare_condition}.
	 * @param ctx the parse tree
	 */
	void enterExpr_compare_condition(@NotNull TinyTemplateParser.Expr_compare_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_compare_condition}.
	 * @param ctx the parse tree
	 */
	void exitExpr_compare_condition(@NotNull TinyTemplateParser.Expr_compare_conditionContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_constant}.
	 * @param ctx the parse tree
	 */
	void enterExpr_constant(@NotNull TinyTemplateParser.Expr_constantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_constant}.
	 * @param ctx the parse tree
	 */
	void exitExpr_constant(@NotNull TinyTemplateParser.Expr_constantContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_math_binary_basic}.
	 * @param ctx the parse tree
	 */
	void enterExpr_math_binary_basic(@NotNull TinyTemplateParser.Expr_math_binary_basicContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_math_binary_basic}.
	 * @param ctx the parse tree
	 */
	void exitExpr_math_binary_basic(@NotNull TinyTemplateParser.Expr_math_binary_basicContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_math_binary_bitwise}.
	 * @param ctx the parse tree
	 */
	void enterExpr_math_binary_bitwise(@NotNull TinyTemplateParser.Expr_math_binary_bitwiseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_math_binary_bitwise}.
	 * @param ctx the parse tree
	 */
	void exitExpr_math_binary_bitwise(@NotNull TinyTemplateParser.Expr_math_binary_bitwiseContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#for_expression}.
	 * @param ctx the parse tree
	 */
	void enterFor_expression(@NotNull TinyTemplateParser.For_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#for_expression}.
	 * @param ctx the parse tree
	 */
	void exitFor_expression(@NotNull TinyTemplateParser.For_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#directive}.
	 * @param ctx the parse tree
	 */
	void enterDirective(@NotNull TinyTemplateParser.DirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#directive}.
	 * @param ctx the parse tree
	 */
	void exitDirective(@NotNull TinyTemplateParser.DirectiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_identifier}.
	 * @param ctx the parse tree
	 */
	void enterExpr_identifier(@NotNull TinyTemplateParser.Expr_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_identifier}.
	 * @param ctx the parse tree
	 */
	void exitExpr_identifier(@NotNull TinyTemplateParser.Expr_identifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#if_directive}.
	 * @param ctx the parse tree
	 */
	void enterIf_directive(@NotNull TinyTemplateParser.If_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#if_directive}.
	 * @param ctx the parse tree
	 */
	void exitIf_directive(@NotNull TinyTemplateParser.If_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_array_get}.
	 * @param ctx the parse tree
	 */
	void enterExpr_array_get(@NotNull TinyTemplateParser.Expr_array_getContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_array_get}.
	 * @param ctx the parse tree
	 */
	void exitExpr_array_get(@NotNull TinyTemplateParser.Expr_array_getContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#else_directive}.
	 * @param ctx the parse tree
	 */
	void enterElse_directive(@NotNull TinyTemplateParser.Else_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#else_directive}.
	 * @param ctx the parse tree
	 */
	void exitElse_directive(@NotNull TinyTemplateParser.Else_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#layout_directive}.
	 * @param ctx the parse tree
	 */
	void enterLayout_directive(@NotNull TinyTemplateParser.Layout_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#layout_directive}.
	 * @param ctx the parse tree
	 */
	void exitLayout_directive(@NotNull TinyTemplateParser.Layout_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#call_macro_directive}.
	 * @param ctx the parse tree
	 */
	void enterCall_macro_directive(@NotNull TinyTemplateParser.Call_macro_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#call_macro_directive}.
	 * @param ctx the parse tree
	 */
	void exitCall_macro_directive(@NotNull TinyTemplateParser.Call_macro_directiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_function_call}.
	 * @param ctx the parse tree
	 */
	void enterExpr_function_call(@NotNull TinyTemplateParser.Expr_function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_function_call}.
	 * @param ctx the parse tree
	 */
	void exitExpr_function_call(@NotNull TinyTemplateParser.Expr_function_callContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(@NotNull TinyTemplateParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(@NotNull TinyTemplateParser.CommentContext ctx);

	/**
	 * Enter a parse tree produced by {@link TinyTemplateParser#expr_simple_condition_ternary}.
	 * @param ctx the parse tree
	 */
	void enterExpr_simple_condition_ternary(@NotNull TinyTemplateParser.Expr_simple_condition_ternaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyTemplateParser#expr_simple_condition_ternary}.
	 * @param ctx the parse tree
	 */
	void exitExpr_simple_condition_ternary(@NotNull TinyTemplateParser.Expr_simple_condition_ternaryContext ctx);
}