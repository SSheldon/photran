// Generated by Ludwig version 1.0 alpha 6

package org.eclipse.photran.internal.core.parser; import org.eclipse.photran.internal.core.lexer.*;


/**
 * <FunctionSubprogram> ::= FunctionStmt:<FunctionStmt> FunctionRange:<FunctionRange>  :production17
 */
public class ASTFunctionSubprogramNode extends ParseTreeNode
{
    public ASTFunctionSubprogramNode(Nonterminal nonterminal, Production production)
    {
        super(nonterminal, production);
    }

    public ASTFunctionStmtNode getASTFunctionStmt()
    {
        return (ASTFunctionStmtNode)this.getChild("FunctionStmt");
    }

    public ASTFunctionRangeNode getASTFunctionRange()
    {
        return (ASTFunctionRangeNode)this.getChild("FunctionRange");
    }

    protected void visitThisNodeUsing(ASTVisitor visitor) { visitor.visitASTFunctionSubprogramNode(this); }
}