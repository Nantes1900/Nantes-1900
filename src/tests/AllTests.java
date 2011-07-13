package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


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
		ParserTest.class, 
		PointTest.class, 
		PolylineTest.class,
		TriangleTest.class, 
		WriterTest.class, })
public class AllTests {
}
