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

import org.eclipse.photran.internal.core.parser.Parser.ASTNode;
import org.eclipse.photran.internal.core.parser.Parser.ASTNodeWithErrorRecoverySymbols;
import org.eclipse.photran.internal.core.parser.Parser.IASTListNode;
import org.eclipse.photran.internal.core.parser.Parser.IASTNode;
import org.eclipse.photran.internal.core.parser.Parser.IASTVisitor;
import org.eclipse.photran.internal.core.lexer.Token;

import org.eclipse.photran.internal.core.lexer.*;                   import org.eclipse.photran.internal.core.analysis.binding.ScopingNode;

public class ASTAssociationNode extends ASTNode
{
    org.eclipse.photran.internal.core.lexer.Token associateName; // in ASTAssociationNode
    org.eclipse.photran.internal.core.lexer.Token hiddenTEqgreaterthan; // in ASTAssociationNode
    ISelector selector; // in ASTAssociationNode

    public org.eclipse.photran.internal.core.lexer.Token getAssociateName()
    {
        return this.associateName;
    }

    public void setAssociateName(org.eclipse.photran.internal.core.lexer.Token newValue)
    {
        this.associateName = newValue;
    }


    public ISelector getSelector()
    {
        return this.selector;
    }

    public void setSelector(ISelector newValue)
    {
        this.selector = newValue;
    }


    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAssociationNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 3;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.associateName;
        case 1:  return this.hiddenTEqgreaterthan;
        case 2:  return this.selector;
        default: return null;
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.associateName = (org.eclipse.photran.internal.core.lexer.Token)value; return;
        case 1:  this.hiddenTEqgreaterthan = (org.eclipse.photran.internal.core.lexer.Token)value; return;
        case 2:  this.selector = (ISelector)value; return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
