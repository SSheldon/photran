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
public class ASTIntentStmtNode extends ASTNode implements ISpecificationStmt
{
    org.eclipse.photran.internal.core.lexer.Token label; // in ASTIntentStmtNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTIntent; // in ASTIntentStmtNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTLparen; // in ASTIntentStmtNode
    ASTIntentSpecNode intentSpec; // in ASTIntentStmtNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTRparen; // in ASTIntentStmtNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTColon; // in ASTIntentStmtNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTColon2; // in ASTIntentStmtNode
    IASTListNode<ASTIntentParListNode> variableList; // in ASTIntentStmtNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTEos; // in ASTIntentStmtNode

    public org.eclipse.photran.internal.core.lexer.Token getLabel()
    {
        return this.label;
    }

    public void setLabel(org.eclipse.photran.internal.core.lexer.Token newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTIntentSpecNode getIntentSpec()
    {
        return this.intentSpec;
    }

    public void setIntentSpec(ASTIntentSpecNode newValue)
    {
        this.intentSpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTIntentParListNode> getVariableList()
    {
        return this.variableList;
    }

    public void setVariableList(IASTListNode<ASTIntentParListNode> newValue)
    {
        this.variableList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTIntentStmtNode(this);
        visitor.visitISpecificationStmt(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 9;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.hiddenTIntent;
        case 2:  return this.hiddenTLparen;
        case 3:  return this.intentSpec;
        case 4:  return this.hiddenTRparen;
        case 5:  return this.hiddenTColon;
        case 6:  return this.hiddenTColon2;
        case 7:  return this.variableList;
        case 8:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTIntent = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 3:  this.intentSpec = (ASTIntentSpecNode)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTRparen = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTColon = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 6:  this.hiddenTColon2 = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 7:  this.variableList = (IASTListNode<ASTIntentParListNode>)value; if (value != null) value.setParent(this); return;
        case 8:  this.hiddenTEos = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

