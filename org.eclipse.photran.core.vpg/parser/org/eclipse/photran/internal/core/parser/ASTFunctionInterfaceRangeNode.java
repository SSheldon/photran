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
public class ASTFunctionInterfaceRangeNode extends ASTNode
{
    IASTListNode<ISpecificationPartConstruct> subprogramInterfaceBody; // in ASTFunctionInterfaceRangeNode
    ASTEndFunctionStmtNode endFunctionStmt; // in ASTFunctionInterfaceRangeNode

    public IASTListNode<ISpecificationPartConstruct> getSubprogramInterfaceBody()
    {
        return this.subprogramInterfaceBody;
    }

    public void setSubprogramInterfaceBody(IASTListNode<ISpecificationPartConstruct> newValue)
    {
        this.subprogramInterfaceBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTEndFunctionStmtNode getEndFunctionStmt()
    {
        return this.endFunctionStmt;
    }

    public void setEndFunctionStmt(ASTEndFunctionStmtNode newValue)
    {
        this.endFunctionStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTFunctionInterfaceRangeNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 2;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.subprogramInterfaceBody;
        case 1:  return this.endFunctionStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.subprogramInterfaceBody = (IASTListNode<ISpecificationPartConstruct>)value; if (value != null) value.setParent(this); return;
        case 1:  this.endFunctionStmt = (ASTEndFunctionStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

