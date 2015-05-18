// Generated from org\tinygroup\template\parser\grammer\TinyTemplateParser.g4 by ANTLR 4.2.2
package org.tinygroup.template.parser.grammer;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TinyTemplateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TinyTemplateParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#import_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_directive(@NotNull TinyTemplateParser.Import_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(@NotNull TinyTemplateParser.ConstantContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#layout_impl_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLayout_impl_directive(@NotNull TinyTemplateParser.Layout_impl_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#para_expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPara_expression_list(@NotNull TinyTemplateParser.Para_expression_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_member_function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_member_function_call(@NotNull TinyTemplateParser.Expr_member_function_callContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#continue_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_directive(@NotNull TinyTemplateParser.Continue_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#tabs_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTabs_directive(@NotNull TinyTemplateParser.Tabs_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_single_right}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_single_right(@NotNull TinyTemplateParser.Expr_single_rightContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_field_access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_field_access(@NotNull TinyTemplateParser.Expr_field_accessContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_compare_relational}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_relational(@NotNull TinyTemplateParser.Expr_compare_relationalContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#define_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefine_expression(@NotNull TinyTemplateParser.Define_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#call_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_directive(@NotNull TinyTemplateParser.Call_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_math_unary_prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_unary_prefix(@NotNull TinyTemplateParser.Expr_math_unary_prefixContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(@NotNull TinyTemplateParser.BlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(@NotNull TinyTemplateParser.TextContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#endofline_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndofline_directive(@NotNull TinyTemplateParser.Endofline_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#identify_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentify_list(@NotNull TinyTemplateParser.Identify_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expression_range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_range(@NotNull TinyTemplateParser.Expression_rangeContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_group(@NotNull TinyTemplateParser.Expr_groupContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#hash_map_entry_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_map_entry_list(@NotNull TinyTemplateParser.Hash_map_entry_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#stop_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStop_directive(@NotNull TinyTemplateParser.Stop_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#call_macro_block_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_macro_block_directive(@NotNull TinyTemplateParser.Call_macro_block_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#blank_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlank_directive(@NotNull TinyTemplateParser.Blank_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_compare_equality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_equality(@NotNull TinyTemplateParser.Expr_compare_equalityContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#for_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_directive(@NotNull TinyTemplateParser.For_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#elseif_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif_directive(@NotNull TinyTemplateParser.Elseif_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_single_left}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_single_left(@NotNull TinyTemplateParser.Expr_single_leftContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#break_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_directive(@NotNull TinyTemplateParser.Break_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#call_block_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_block_directive(@NotNull TinyTemplateParser.Call_block_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#indent_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndent_directive(@NotNull TinyTemplateParser.Indent_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#dent_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDent_directive(@NotNull TinyTemplateParser.Dent_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#set_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_expression(@NotNull TinyTemplateParser.Set_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(@NotNull TinyTemplateParser.TemplateContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_array_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_array_list(@NotNull TinyTemplateParser.Expr_array_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#set_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_directive(@NotNull TinyTemplateParser.Set_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_hash_map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_hash_map(@NotNull TinyTemplateParser.Expr_hash_mapContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_conditional_ternary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_conditional_ternary(@NotNull TinyTemplateParser.Expr_conditional_ternaryContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_math_binary_shift}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_binary_shift(@NotNull TinyTemplateParser.Expr_math_binary_shiftContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_list(@NotNull TinyTemplateParser.Expression_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#macro_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_directive(@NotNull TinyTemplateParser.Macro_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#bodycontent_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodycontent_directive(@NotNull TinyTemplateParser.Bodycontent_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(@NotNull TinyTemplateParser.ValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#para_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPara_expression(@NotNull TinyTemplateParser.Para_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#include_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_directive(@NotNull TinyTemplateParser.Include_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#define_expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefine_expression_list(@NotNull TinyTemplateParser.Define_expression_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#invalid_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalid_directive(@NotNull TinyTemplateParser.Invalid_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_compare_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_condition(@NotNull TinyTemplateParser.Expr_compare_conditionContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_constant(@NotNull TinyTemplateParser.Expr_constantContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_math_binary_basic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_binary_basic(@NotNull TinyTemplateParser.Expr_math_binary_basicContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_math_binary_bitwise}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_binary_bitwise(@NotNull TinyTemplateParser.Expr_math_binary_bitwiseContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#for_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_expression(@NotNull TinyTemplateParser.For_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirective(@NotNull TinyTemplateParser.DirectiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_identifier(@NotNull TinyTemplateParser.Expr_identifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#if_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_directive(@NotNull TinyTemplateParser.If_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_array_get}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_array_get(@NotNull TinyTemplateParser.Expr_array_getContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#else_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_directive(@NotNull TinyTemplateParser.Else_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#layout_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLayout_directive(@NotNull TinyTemplateParser.Layout_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#call_macro_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_macro_directive(@NotNull TinyTemplateParser.Call_macro_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_function_call(@NotNull TinyTemplateParser.Expr_function_callContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(@NotNull TinyTemplateParser.CommentContext ctx);

	/**
	 * Visit a parse tree produced by {@link TinyTemplateParser#expr_simple_condition_ternary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_simple_condition_ternary(@NotNull TinyTemplateParser.Expr_simple_condition_ternaryContext ctx);
}