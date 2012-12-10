package org.eclipse.photran.internal.cdtinterface.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.photran.cdtinterface.makegen.DefaultFortranDependencyCalculator;
import org.eclipse.photran.internal.cdtinterface.CDTInterfacePlugin;
import org.eclipse.photran.internal.tests.PhotranWorkspaceTestCase;

public class MultipleModulesPerFileTest extends PhotranWorkspaceTestCase {

	private static final String DIR = "dependency-test-code/multiple-modules";

	private final DefaultFortranDependencyCalculator dependencyCalc = new DefaultFortranDependencyCalculator();

	private IFile program;
	private IFile multipleModules;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		program = importFile(CDTInterfacePlugin.getDefault(), DIR, "main.f90");
		multipleModules = importFile(CDTInterfacePlugin.getDefault(), DIR, "hello.f90");
	}

	public void testMultipleModulesPerFile(){
		IResource[] multipleModulesDependencies = dependencyCalc.findDependencies(multipleModules, project, "Debug");
		assertEquals(1, multipleModulesDependencies.length);
		assertEquals(multipleModules, multipleModulesDependencies[0]);

		IResource[] mainProgramDependencies = dependencyCalc.findDependencies(program, project, "Debug");
		assertEquals(2, mainProgramDependencies.length);
		assertEquals(program, mainProgramDependencies[0]);
		assertEquals("hello.o", mainProgramDependencies[1].getName());
	}
}
