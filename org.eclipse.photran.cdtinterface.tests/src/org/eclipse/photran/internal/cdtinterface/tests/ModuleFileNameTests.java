package org.eclipse.photran.internal.cdtinterface.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.photran.cdtinterface.makegen.DefaultFortranDependencyCalculator;
import org.eclipse.photran.internal.cdtinterface.CDTInterfacePlugin;
import org.eclipse.photran.internal.tests.PhotranWorkspaceTestCase;

public class ModuleFileNameTests extends PhotranWorkspaceTestCase {
	private static final String DIR = "dependency-test-code/module-file-name";

	private final DefaultFortranDependencyCalculator dependencyCalc = new DefaultFortranDependencyCalculator();

	private IFile program;
	private IFile module;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		program = importFile(CDTInterfacePlugin.getDefault(), DIR, "area.f90");
		module  = importFile(CDTInterfacePlugin.getDefault(), DIR, "shape.f90");
	}

	public void testFindDependencies() {
		IResource[] moduleDependencies = dependencyCalc.findDependencies(program, project);
		assertEquals(0, moduleDependencies.length);

		IResource[] programDependencies = dependencyCalc.findDependencies(program, project);
		assertEquals(1, programDependencies.length);
		assertEquals(module, programDependencies[0]);
	}
}
