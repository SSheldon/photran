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
package org.eclipse.rephraserengine.internal.ui.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rephraserengine.core.vpg.eclipse.EclipseVPG;
import org.eclipse.rephraserengine.ui.actions.VPGWindowActionDelegate;

/**
 * Implements the Clear and Rebuild VPG Database action in the Refactor/(Debugging) menu
 *
 * @author Jeff Overbey
 *
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class ClearDBAction extends VPGWindowActionDelegate
{
    @Override
    protected void run(EclipseVPG vpg, IProgressMonitor progressMonitor) throws Exception
    {
        progressMonitor.beginTask(Messages.ClearDBAction_ClearingAndRebuildingDatabase, IProgressMonitor.UNKNOWN);

        vpg.clearDatabase();
        vpg.queueJobToEnsureVPGIsUpToDate();

        progressMonitor.done();
    }
}