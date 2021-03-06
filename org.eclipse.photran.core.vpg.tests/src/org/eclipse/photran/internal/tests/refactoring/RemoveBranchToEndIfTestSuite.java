/*******************************************************************************
 * Copyright (c) 2010 Rita Chow, Nicola Hall, Jerry Hsiao, Mark Mozolewski, Chamil Wijenayaka
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Rita Chow - Initial Implementation
 *    Nicola Hall - Initial Implementation
 *    Jerry Hsiao - Initial Implementation
 *    Mark Mozolewski - Initial Implementation
 *    Chamil Wijenayaka - Initial Implementation
 *******************************************************************************/
package org.eclipse.photran.internal.tests.refactoring;

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.photran.internal.core.refactoring.RemoveBranchToEndIfRefactoring;
import org.eclipse.photran.internal.tests.Activator;
import org.eclipse.photran.internal.tests.PhotranRefactoringTestSuiteFromMarkers;

/**
 * Unit tests for the Remove Branch To End If Refactoring.
 * 
 * @author Rita Chow (chow15), Jerry Hsiao (jhsiao2), Mark Mozolewski (mozolews), Chamil Wijenayaka
 *         (wijenay2), Nicola Hall (nfhall2)
 * @author Jeff Overbey - added {@link #shouldCompile(IFile)}
 */
public class RemoveBranchToEndIfTestSuite extends
    PhotranRefactoringTestSuiteFromMarkers<RemoveBranchToEndIfRefactoring>
{
    private static final String DIR = "refactoring-test-code/remove-branch-to-end-if";

    public static Test suite() throws Exception
    {
        return new RemoveBranchToEndIfTestSuite();
    }

    public RemoveBranchToEndIfTestSuite() throws Exception
    {
        super(Activator.getDefault(), "Running Remove Branch To End If Refactoring in", DIR,
            RemoveBranchToEndIfRefactoring.class);
    }

    @Override protected boolean shouldCompile(IFile fileContainingMarker)
    {
        String name = fileContainingMarker.getName();
        return
            !name.equalsIgnoreCase("branch_from_inside_and_outside_if_block_no_do_loop.f90") &&
            !name.equalsIgnoreCase("nested_if_block_basic.f90") &&
            !name.equalsIgnoreCase("test-branch_end_if_label.f90") &&
            !name.equalsIgnoreCase("test-end_if_label_between_branch_to_end_if.f90");
    }
}
