package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.models.MeshTest;
import tests.models.PolylineTest;
import tests.models.basis.EdgeTest;
import tests.models.basis.PointTest;
import tests.models.basis.TriangleTest;
import tests.models.extended.EdificeTest;
import tests.models.extended.FloorTest;
import tests.models.extended.SpecialBuildingTest;
import tests.models.extended.TownTest;
import tests.utils.MatrixMethodTest;
import tests.utils.ParserSTLTest;
import tests.utils.WriterSTLTest;

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

		MatrixMethodTest.class, ParserSTLTest.class, WriterSTLTest.class })
public class AllTests {
}
