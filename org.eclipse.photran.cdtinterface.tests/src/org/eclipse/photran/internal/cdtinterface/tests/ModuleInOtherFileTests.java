package org.eclipse.photran.internal.cdtinterface.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.photran.cdtinterface.makegen.DefaultFortranDependencyCalculator;
import org.eclipse.photran.internal.cdtinterface.CDTInterfacePlugin;
import org.eclipse.photran.internal.tests.PhotranWorkspaceTestCase;

public class ModuleInOtherFileTests extends PhotranWorkspaceTestCase {
	private static final String DIR = "dependency-test-code/other-file-module";

	private final DefaultFortranDependencyCalculator dependencyCalc = new DefaultFortranDependencyCalculator();

	private IFile program;
	private IFile module;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		program = importFile(CDTInterfacePlugin.getDefault(), DIR, "area.f90");
		module  = importFile(CDTInterfacePlugin.getDefault(), DIR, "circle.f90");
	}

	public void testFindDependencies() {
		IResource[] moduleDependencies = dependencyCalc.findDependencies(module, project, "Debug");
		assertEquals(1, moduleDependencies.length);
		assertEquals(module, moduleDependencies[0]);

		IResource[] programDependencies = dependencyCalc.findDependencies(program, project, "Debug");
		assertEquals(2, programDependencies.length);
		assertEquals(program, programDependencies[0]);
		assertEquals("circle.o", programDependencies[1].getName());
	}
}
