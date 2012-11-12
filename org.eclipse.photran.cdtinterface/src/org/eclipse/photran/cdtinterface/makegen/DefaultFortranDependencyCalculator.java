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
import org.eclipse.cdt.managedbuilder.core.IManagedOutputNameProvider;
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
import org.eclipse.photran.internal.cdtinterface.core.FortranLanguage;
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
public class DefaultFortranDependencyCalculator implements IManagedDependencyGenerator,
														   IManagedOutputNameProvider
{
	public static final String MODULE_EXTENSION = "o";	//$NON-NLS-1$
	
	/*
	 * Return a list of the names of all modules used by a file
	 */
	private String[] findUsedModuleNames(File file) {
		ArrayList names = new ArrayList();
		InputStream in = null;
		Reader r = null;
		try {
		/*
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			ILexer lexer = FortranProcessor.createLexerFor(in, file.getName());
			for (Token thisToken = lexer.yylex(), lastToken = null;
			     thisToken.getTerminal() != Terminal.END_OF_INPUT;
			     lastToken = thisToken, thisToken = lexer.yylex())
			{
				if (lastToken != null
						      && lastToken.getTerminal() == Terminal.T_USE
					          && thisToken.getTerminal() == Terminal.T_IDENT)
				{
					names.add(thisToken.getText());
				}
			}
		*/
			in = new BufferedInputStream(new FileInputStream(file));
			r = new BufferedReader(new InputStreamReader(in));
			StreamTokenizer st = new StreamTokenizer(r);
			st.commentChar('!');
			st.eolIsSignificant(false);
			st.slashSlashComments(false);
			st.slashStarComments(false);
			st.wordChars('_', '_');
			
			int token;
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				if (st.ttype == StreamTokenizer.TT_WORD) {
					if (st.sval.equalsIgnoreCase("use")) { //$NON-NLS-1$
						token = st.nextToken();
						if (st.ttype == StreamTokenizer.TT_WORD) {
							names.add(st.sval);
						} else {
							st.pushBack();
						}
					}
					/**
					 * This should be moved to separate include file list
					 */
					/*
					else if (st.sval.equalsIgnoreCase("include")) {
						token = st.nextToken();
						if (st.ttype == '\'' || st.ttype == '"') {
							names.add(st.sval);
						} else {
							st.pushBack();
						}
					}
					*/
				}
			}
		}
		catch (Exception e) {
			return new String[0];
		}
		finally {
			try {
				if (r != null)
					r.close();
				else if (in != null)
					in.close();
			}
			catch (IOException e) {
			}
		}
		return (String[]) names.toArray(new String[names.size()]);
	}
	
	/*
	 * Return a list of the names of all modules defined in a file
	 */
	private String[] findModuleNames(File file) {
		ArrayList names = new ArrayList();
		InputStream in = null;
		Reader r = null;
		try {
			/*
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			ILexer lexer = FortranProcessor.createLexerFor(in, file.getName());
			for (Token thisToken = lexer.yylex(), lastToken = null, tokenBeforeLast = null;
			     thisToken.getTerminal() != Terminal.END_OF_INPUT;
			     tokenBeforeLast = lastToken, lastToken = thisToken, thisToken = lexer.yylex())
			{
				if (lastToken != null
				    && lastToken.getTerminal() == Terminal.T_MODULE
				    && thisToken.getTerminal() == Terminal.T_IDENT)
				{
					if (tokenBeforeLast != null && tokenBeforeLast.getTerminal() == Terminal.T_END) {
						continue;
					}
					names.add(thisToken.getText());
				}
			}
			*/
			in = new BufferedInputStream(new FileInputStream(file));
			r = new BufferedReader(new InputStreamReader(in));
			StreamTokenizer st = new StreamTokenizer(r);
			st.commentChar('!');
			st.eolIsSignificant(false);
			st.slashSlashComments(false);
			st.slashStarComments(false);
			st.wordChars('_', '_');
			
			int token;
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				if (st.ttype == StreamTokenizer.TT_WORD) {
					if (st.sval.equalsIgnoreCase("module")) { //$NON-NLS-1$
						token = st.nextToken();
						if (st.ttype == StreamTokenizer.TT_WORD) {
							names.add(st.sval);
						} else {
							st.pushBack();
						}
					}
				}
			}
		}
		catch (Exception e) {
			return new String[0];
		}
		finally {
			try {
				if (r != null)
					r.close();
				else if (in != null)
					in.close();
			}
			catch (IOException e) {
			}
		}
		return (String[]) names.toArray(new String[names.size()]);
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
		ArrayList dependencies = new ArrayList();
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
			String[] usedNames = findUsedModuleNames(file);

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
		
		return (IResource[]) dependencies.toArray(new IResource[dependencies.size()]);
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

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.cdt.managedbuilder.core.IManagedOutputNameProvider#getOutputNames(org.eclipse.cdt.managedbuilder.core.ITool, org.eclipse.core.runtime.IPath[])
	 */
	public IPath[] getOutputNames(ITool tool, IPath[] primaryInputNames) {
		//  TODO:  This method should be passed the relative path of the top build directory?
		ArrayList outs = new ArrayList();
		if (primaryInputNames.length > 0) {
			// Get the names of modules created by this source file
			String[] modules = findModuleNames(primaryInputNames[0].toFile());
			// Add any generated modules
			if (modules != null) {
				for (int i = 0; i < modules.length; i++) {
					//  Return the path to the module file that will be created by the build.  By default, ifort appears
					//  to generate .mod files in the directory from which the compiler is run.  For MBS, this
					//  is the top-level build directory.  
					//  TODO: Support the /module:path option and use that in determining the path of the module file
					//  TODO: The nameProvider documentation should note that the returned path is relative to the top-level 
					//        build directory.  HOWEVER, if only a file name is returned, MBS will automatically add on the
					//        directory path relative to the top-level build directory.  The relative path comes from the source
					//        file location.  In order to specify that this output file is always in the top-level build 
					//        directory, regardless of the source file directory structure, return "./path".
					IPath modName = Path.fromOSString("." + Path.SEPARATOR + modules[i] + "." + MODULE_EXTENSION); //$NON-NLS-1$ //$NON-NLS-2$
					outs.add(modName);				
				}
			}
		}
		return (IPath[]) outs.toArray(new IPath[outs.size()]);
	}
}
