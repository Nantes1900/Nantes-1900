package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value={
		//		AlgosTest.class,
		//		EdgeTest.class,
		//		MatrixMethodTest.class,
		//		MeshTest.class,
		//		ParserTest.class,
		PointTest.class,
		//		PolylineTest.class,
		//		SeparationFloorBuildingTest.class,
		//		SeparationTraitementMursToitsTest.class,
		TriangleTest.class,
		//		WriterTest.class,
})
public class AllTests {}
