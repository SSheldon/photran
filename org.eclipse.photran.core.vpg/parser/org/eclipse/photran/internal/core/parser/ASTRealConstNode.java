/*******************************************************************************
 * Copyright (c) 2007 University of Illinois at Urbana-Champaign and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     UIUC - Initial API and implementation
 *******************************************************************************/
package org.eclipse.photran.internal.core.parser;

import java.io.PrintStream;
import java.util.Iterator;

import java.util.List;

import org.eclipse.photran.internal.core.parser.ASTListNode;
import org.eclipse.photran.internal.core.parser.ASTNode;
import org.eclipse.photran.internal.core.parser.ASTNodeWithErrorRecoverySymbols;
import org.eclipse.photran.internal.core.parser.IASTListNode;
import org.eclipse.photran.internal.core.parser.IASTNode;
import org.eclipse.photran.internal.core.parser.IASTVisitor;
import org.eclipse.photran.internal.core.lexer.Token;

import org.eclipse.photran.internal.core.lexer.*;                   import org.eclipse.photran.internal.core.analysis.binding.ScopingNode;                   import org.eclipse.photran.internal.core.SyntaxException;                   import java.io.IOException;

@SuppressWarnings("all")
public class ASTRealConstNode extends ASTNode implements IExpr, ISelector, IUnsignedArithmeticConst
{
    org.eclipse.photran.internal.core.lexer.Token realConst; // in ASTRealConstNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTUnderscore; // in ASTRealConstNode
    org.eclipse.photran.internal.core.lexer.Token intKind; // in ASTRealConstNode
    ASTNamedConstantUseNode namedConstKind; // in ASTRealConstNode

    public org.eclipse.photran.internal.core.lexer.Token getRealConst()
    {
        return this.realConst;
    }

    public void setRealConst(org.eclipse.photran.internal.core.lexer.Token newValue)
    {
        this.realConst = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public org.eclipse.photran.internal.core.lexer.Token getIntKind()
    {
        return this.intKind;
    }

    public void setIntKind(org.eclipse.photran.internal.core.lexer.Token newValue)
    {
        this.intKind = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTNamedConstantUseNode getNamedConstKind()
    {
        return this.namedConstKind;
    }

    public void setNamedConstKind(ASTNamedConstantUseNode newValue)
    {
        this.namedConstKind = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTRealConstNode(this);
        visitor.visitIExpr(this);
        visitor.visitISelector(this);
        visitor.visitIUnsignedArithmeticConst(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 4;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.realConst;
        case 1:  return this.hiddenTUnderscore;
        case 2:  return this.intKind;
        case 3:  return this.namedConstKind;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.realConst = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTUnderscore = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 2:  this.intKind = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 3:  this.namedConstKind = (ASTNamedConstantUseNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

