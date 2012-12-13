package org.eclipse.photran.internal.cdtinterface.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.photran.cdtinterface.makegen.DefaultFortranDependencyCalculator;
import org.eclipse.photran.internal.cdtinterface.CDTInterfacePlugin;
import org.eclipse.photran.internal.tests.PhotranWorkspaceTestCase;

public class ModuleAcrossMultipleFilesTest extends PhotranWorkspaceTestCase {
	private static final String DIR = "dependency-test-code/multiple-files";

	private final DefaultFortranDependencyCalculator dependencyCalc = new DefaultFortranDependencyCalculator();

	private IFile program;
	private IFile module1;
	private IFile module2;

	@Override
	public void setUp() throws Exception{
		super.setUp();

		program = importFile(CDTInterfacePlugin.getDefault(), DIR, "main.f90");
		module1 = importFile(CDTInterfacePlugin.getDefault(), DIR, "hello.f90");
		module2 = importFile(CDTInterfacePlugin.getDefault(), DIR, "sub.f90");

	}

	public void testModulesAcrossMultipleFiles() {
		IResource [] module1Dependencies = dependencyCalc.findDependencies(module1, project, "Debug");
		assertEquals(module1, module1Dependencies[0]);
		assertEquals(1, module1Dependencies.length);

		IResource [] module2Dependencies = dependencyCalc.findDependencies(module2, project, "Debug");
		assertEquals(1, module2Dependencies.length);
		assertEquals(module2, module2Dependencies[0]);

		IResource [] programDependencies = dependencyCalc.findDependencies(program, project, "Debug");
		assertEquals(3, programDependencies.length);
		assertEquals("hello.o", programDependencies[1].getName());
		assertEquals("sub.o", programDependencies[2].getName());
	}
}
