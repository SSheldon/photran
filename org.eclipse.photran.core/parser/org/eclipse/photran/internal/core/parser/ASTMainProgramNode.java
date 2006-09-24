// Generated by Ludwig version 1.0 alpha 6

package org.eclipse.photran.internal.core.parser; import org.eclipse.photran.internal.core.lexer.*;


/**
 * <MainProgram> ::= MainRange:<MainRange>  :production8
 * <MainProgram> ::= ProgramStmt:<ProgramStmt> MainRange:<MainRange>  :production9
 */
public class ASTMainProgramNode extends ParseTreeNode
{
    public ASTMainProgramNode(Nonterminal nonterminal, Production production)
    {
        super(nonterminal, production);
    }

    public ASTMainRangeNode getASTMainRange()
    {
        return (ASTMainRangeNode)this.getChild("MainRange");
    }

    public ASTProgramStmtNode getASTProgramStmt()
    {
        return (ASTProgramStmtNode)this.getChild("ProgramStmt");
    }

    protected void visitThisNodeUsing(ASTVisitor visitor) { visitor.visitASTMainProgramNode(this); }
}