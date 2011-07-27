package nantes1900.tests;

import nantes1900.tests.models.MeshTest;
import nantes1900.tests.models.PolylineTest;
import nantes1900.tests.models.basis.EdgeTest;
import nantes1900.tests.models.basis.PointTest;
import nantes1900.tests.models.basis.TriangleTest;
import nantes1900.tests.models.extended.EdificeTest;
import nantes1900.tests.models.extended.FloorTest;
import nantes1900.tests.models.extended.SpecialBuildingTest;
import nantes1900.tests.models.extended.TownTest;
import nantes1900.tests.utils.MatrixMethodTest;
import nantes1900.tests.utils.ParserSTLTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Class to test every class tests of the project
 * 
 * @author Daniel Lefevre
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { EdgeTest.class, MeshTest.class, PointTest.class,
		PolylineTest.class, TriangleTest.class,

		EdificeTest.class, FloorTest.class, SpecialBuildingTest.class,
		TownTest.class,

		MatrixMethodTest.class, ParserSTLTest.class })
public class AllTests {
}
