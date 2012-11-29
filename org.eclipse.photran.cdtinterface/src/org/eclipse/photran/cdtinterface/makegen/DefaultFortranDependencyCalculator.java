/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.photran.cdtinterface.makegen;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.makegen.IManagedDependencyGenerator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.photran.core.IFortranAST;
import org.eclipse.photran.internal.cdtinterface.core.FortranLanguage;
import org.eclipse.photran.internal.core.analysis.binding.Definition;
import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTUseStmtNode;
import org.eclipse.photran.internal.core.vpg.PhotranVPG;

/**
 *  This class implements the Dependency Manager and Output Name Provider interfaces
 *  @author Unknown
 *  @author Timofey Yuvashev 2009
 *  @author Jeff Overbey -- files were not being closed (Bug 334796)
 *  
 *  @since 8.0
 */
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked", "unused" })
public class DefaultFortranDependencyCalculator implements IManagedDependencyGenerator
{
	public static final String MODULE_EXTENSION = "o";	//$NON-NLS-1$
	
	/*
	 * Return a list of the names of all modules used by a file
	 */
	private List<String> findUsedModuleNames(IFile file) {
		List<String> modules = new ArrayList<String>();
		IFortranAST ast = PhotranVPG.getInstance().acquireTransientAST(file);
		if (ast != null)
		{
			for (ASTUseStmtNode use : ast.getRoot().findAll(ASTUseStmtNode.class))
			{
				String moduleName = PhotranVPG.canonicalizeIdentifier(use.getName().getText());
				modules.add(moduleName);
			}
		}
		return modules;
	}

	/*
	 * Returns true if the resource is a Fortran source file
	 */
	private boolean isFortranFile(IProject project, File file, Collection fortranContentTypes) {
		try {
			IContentType ct = CCorePlugin.getContentType(project, file.getCanonicalPath());
			if (ct != null) {
				return fortranContentTypes.contains(ct.toString());
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.managedbuilder.makegen.IManagedBuilderDependencyCalculator#findDependencies(org.eclipse.core.resources.IResource)
	 */
	public IResource[] findDependencies(IResource resource, IProject project) {
		ArrayList<IResource> dependencies = new ArrayList<IResource>();
		Collection fortranContentTypes = new FortranLanguage().getRegisteredContentTypeIds();

		//  TODO:  This method should be passed the ITool and the relative path of the top build directory
		//         For now we'll figure this out from the project.
		IManagedBuildInfo mngInfo = ManagedBuildManager.getBuildInfo(project);
		IConfiguration config = mngInfo.getDefaultConfiguration();

		File file = resource.getLocation().toFile();
		try {
			if (!isFortranFile(project, file, fortranContentTypes)) {
				return new IResource[0];
			}
			
			// add dependency on self
			dependencies.add(resource);
	
			//  Get the names of the modules USE'd by the source file
			List<String> usedNames = findUsedModuleNames((IFile)resource);

			//  Search the project files for a Fortran source that creates the module.
			//  Compiling this source file is dependent upon first compiling the found source file.
			for (String usedName : usedNames) {
				List<IFile> exportingFiles = PhotranVPG.getInstance().findFilesThatExportModule(usedName);
				if (exportingFiles.size() == 1) {
					IFile exportingFile = exportingFiles.get(0);
					//  Get the path to the module file that will be created by the build.  By default, ifort appears
					//  to generate .mod files in the directory from which the compiler is run.  For MBS, this
					//  is the top-level build directory.
					//  TODO: Support the /module:path option and use that in determining the path of the module file
					String fileNameContainingModule = exportingFile.getProjectRelativePath().toString().replaceFirst("\\..+", ""); //$NON-NLS-1$ //$NON-NLS-2$;
					IPath modName = Path.fromOSString("./" + config.getName() + Path.SEPARATOR + fileNameContainingModule + "." + MODULE_EXTENSION); //$NON-NLS-1$ //$NON-NLS-2$
					dependencies.add(project.getFile(modName));
				}
			}
		}
		catch (Exception e)
		{
			return new IResource[0];
		}
		
		return dependencies.toArray(new IResource[dependencies.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.managedbuilder.makegen.IManagedBuilderDependencyCalculator#getCalculatorType()
	 */
	public int getCalculatorType() {
		return TYPE_EXTERNAL;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.managedbuilder.makegen.IManagedBuilderDependencyCalculator#getDependencyCommand()
	 */
	public String getDependencyCommand(IResource resource, IManagedBuildInfo info) {
		/* 
		 * The type of this IManagedDependencyGenerator is TYPE_EXTERNAL,
		 * so implement findDependencies() rather than getDependencyCommand().
		 * */
		return null;
	}
}
