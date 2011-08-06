package test.nantes1900;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.nantes1900.models.MeshTest;
import test.nantes1900.models.PolylineTest;
import test.nantes1900.models.basis.EdgeTest;
import test.nantes1900.models.basis.PointTest;
import test.nantes1900.models.basis.TriangleTest;
import test.nantes1900.models.extended.BuildingTest;
import test.nantes1900.models.extended.FloorTest;
import test.nantes1900.models.extended.SpecialBuildingTest;
import test.nantes1900.models.extended.TownTest;
import test.nantes1900.utils.AlgosTest;
import test.nantes1900.utils.MatrixMethodTest;
import test.nantes1900.utils.ParserSTLTest;

/**
 * Class to test every class tests of the project.
 * @author Daniel Lefevre
 */
@RunWith(Suite.class)
@SuiteClasses(
    value = {EdgeTest.class, PointTest.class, TriangleTest.class,
        PolylineTest.class, MeshTest.class,

        BuildingTest.class, FloorTest.class, SpecialBuildingTest.class,
        TownTest.class,

        AlgosTest.class, MatrixMethodTest.class, ParserSTLTest.class })
public final class AllTests {

    /**
     * Private constructor.
     */
    private AllTests() {
    }
}
