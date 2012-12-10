package org.eclipse.photran.internal.cdtinterface.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.photran.cdtinterface.makegen.DefaultFortranDependencyCalculator;
import org.eclipse.photran.internal.cdtinterface.CDTInterfacePlugin;
import org.eclipse.photran.internal.tests.PhotranWorkspaceTestCase;

public class NoDependenciesTests extends PhotranWorkspaceTestCase {
	private static final String DIR = "dependency-test-code/no-dependencies";

	private final DefaultFortranDependencyCalculator dependencyCalc = new DefaultFortranDependencyCalculator();

	private IFile program;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		program = importFile(CDTInterfacePlugin.getDefault(), DIR, "hello.f90");
	}

	public void testFindDependencies() {
		IResource[] moduleDependencies = dependencyCalc.findDependencies(program, project, "Debug");
		assertEquals(1, moduleDependencies.length);
		assertEquals(program, moduleDependencies[0]);
	}
}
