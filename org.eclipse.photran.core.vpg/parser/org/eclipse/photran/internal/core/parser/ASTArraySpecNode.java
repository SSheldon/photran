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

import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.Parser.*;
import java.util.List;

public class ASTArraySpecNode extends InteriorNode
{
    ASTArraySpecNode(Production production, List<CSTNode> childNodes, List<CSTNode> discardedSymbols)
    {
         super(production);
         
         for (Object o : childNodes)
             addChild((CSTNode)o);
         constructionFinished();
    }
    
    @Override protected void visitThisNodeUsing(ASTVisitor visitor)
    {
        visitor.visitASTArraySpecNode(this);
    }

    public ASTExplicitShapeSpecListNode getExplicitShapeSpecList()
    {
        if (treeHasBeenModified()) throw new IllegalStateException("Accessor methods cannot be called on the nodes of a CST after it has been modified");

        if (getProduction() == Production.ARRAY_SPEC_292)
            return (ASTExplicitShapeSpecListNode)getChild(0);
        else
            return null;
    }

    public ASTAssumedSizeSpecNode getAssumedSizeSpec()
    {
        if (treeHasBeenModified()) throw new IllegalStateException("Accessor methods cannot be called on the nodes of a CST after it has been modified");

        if (getProduction() == Production.ARRAY_SPEC_293)
            return (ASTAssumedSizeSpecNode)getChild(0);
        else
            return null;
    }

    public ASTAssumedShapeSpecListNode getAssumedShapeSpecList()
    {
        if (treeHasBeenModified()) throw new IllegalStateException("Accessor methods cannot be called on the nodes of a CST after it has been modified");

        if (getProduction() == Production.ARRAY_SPEC_294)
            return (ASTAssumedShapeSpecListNode)getChild(0);
        else
            return null;
    }

    public ASTDeferredShapeSpecListNode getDeferredShapeSpecList()
    {
        if (treeHasBeenModified()) throw new IllegalStateException("Accessor methods cannot be called on the nodes of a CST after it has been modified");

        if (getProduction() == Production.ARRAY_SPEC_295)
            return (ASTDeferredShapeSpecListNode)getChild(0);
        else
            return null;
    }
}