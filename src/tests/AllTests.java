package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.models.basis.EdgeTest;
import tests.models.basis.MeshTest;
import tests.models.basis.PointTest;
import tests.models.basis.PolylineTest;
import tests.models.basis.TriangleTest;
import tests.utils.MatrixMethodTest;
import tests.utils.ParserSTLTest;
import tests.utils.WriterSTLTest;


/**
 * Class to test every test of the project
 * 
 * @author Daniel Lefevre
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { 
		EdgeTest.class, 
		MatrixMethodTest.class, 
		MeshTest.class,
		ParserSTLTest.class, 
		PointTest.class, 
		PolylineTest.class,
		TriangleTest.class, 
		WriterSTLTest.class, 
		})
public class AllTests {
}
