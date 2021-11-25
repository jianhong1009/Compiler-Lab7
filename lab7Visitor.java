// Generated from lab7.g4 by ANTLR 4.9
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link lab7Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface lab7Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link lab7Parser#compUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompUnit(lab7Parser.CompUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(lab7Parser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#funcType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncType(lab7Parser.FuncTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(lab7Parser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#blockItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockItem(lab7Parser.BlockItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(lab7Parser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#lVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLVal(lab7Parser.LValContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(lab7Parser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#constDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDecl(lab7Parser.ConstDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#bType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBType(lab7Parser.BTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#constDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDef(lab7Parser.ConstDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#constInitVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstInitVal(lab7Parser.ConstInitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#constExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExp(lab7Parser.ConstExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(lab7Parser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(lab7Parser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#initVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitVal(lab7Parser.InitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(lab7Parser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(lab7Parser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#lOrExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLOrExp(lab7Parser.LOrExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#lAndExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLAndExp(lab7Parser.LAndExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#eqExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(lab7Parser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#eqNeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqNeq(lab7Parser.EqNeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#relExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExp(lab7Parser.RelExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#compare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompare(lab7Parser.CompareContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#addExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(lab7Parser.AddExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#addSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(lab7Parser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#mulExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp(lab7Parser.MulExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#mulDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(lab7Parser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#unaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExp(lab7Parser.UnaryExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#funcRParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncRParams(lab7Parser.FuncRParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp(lab7Parser.PrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#unaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(lab7Parser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(lab7Parser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(lab7Parser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#if_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_(lab7Parser.If_Context ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#while_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_(lab7Parser.While_Context ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#break_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_(lab7Parser.Break_Context ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#continue_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_(lab7Parser.Continue_Context ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#return_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_(lab7Parser.Return_Context ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#comma}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComma(lab7Parser.CommaContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#openBracket}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpenBracket(lab7Parser.OpenBracketContext ctx);
	/**
	 * Visit a parse tree produced by {@link lab7Parser#closeBracket}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloseBracket(lab7Parser.CloseBracketContext ctx);
}