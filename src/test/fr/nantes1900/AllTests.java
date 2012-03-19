package test.fr.nantes1900;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.fr.nantes1900.decimation.MeshDecimationTest;
import test.fr.nantes1900.models.EdgeTest;
import test.fr.nantes1900.models.MeshTest;
import test.fr.nantes1900.models.PointTest;
import test.fr.nantes1900.models.PolygonTest;
import test.fr.nantes1900.models.TriangleTest;
import test.fr.nantes1900.utils.MatrixMethodTest;
import test.fr.nantes1900.utils.ParserSTLTest;

/**
 * Class to test every class tests of the project.
 * @author Daniel Lefevre
 */
@RunWith(Suite.class)
@SuiteClasses(value = {EdgeTest.class, PointTest.class, TriangleTest.class,
        PolygonTest.class, MeshTest.class, MatrixMethodTest.class,
        ParserSTLTest.class, MeshDecimationTest.class}
)

public final class AllTests {

    /**
     * Private constructor.
     */
    private AllTests() {
    }
}
