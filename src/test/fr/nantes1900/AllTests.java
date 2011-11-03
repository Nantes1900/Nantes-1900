package test.fr.nantes1900;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.fr.nantes1900.models.MeshTest;
import test.fr.nantes1900.models.PolylineTest;
import test.fr.nantes1900.models.basis.EdgeTest;
import test.fr.nantes1900.models.basis.PointTest;
import test.fr.nantes1900.models.basis.TriangleTest;
import test.fr.nantes1900.utils.MatrixMethodTest;
import test.fr.nantes1900.utils.ParserSTLTest;

/**
 * Class to test every class tests of the project.
 * 
 * @author Daniel Lefevre
 */
@RunWith(Suite.class)
@SuiteClasses(value = { EdgeTest.class, PointTest.class, TriangleTest.class,
	PolylineTest.class, MeshTest.class,

	MatrixMethodTest.class, ParserSTLTest.class })
public final class AllTests {

    /**
     * Private constructor.
     */
    private AllTests() {
    }
}
