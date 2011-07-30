package nantes1900.tests.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import junit.framework.TestCase;

import nantes1900.models.Mesh;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;
import nantes1900.utils.ParserSTL;
import nantes1900.utils.ParserSTL.BadFormedFileException;
import nantes1900.utils.WriterSTL;

import org.junit.Test;

/**
 * A set of tests for the class Parser.
 * 
 * @author Daniel Lefevre
 */
public class ParserSTLTest extends TestCase {

	/**
	 * Test method for
	 * {@link nantes1900.utils.ParserSTL#readSTL(java.lang.String)} and
	 * {@link nantes1900.utils.WriteSTL#writeSTL(java.lang.String)} Same test as
	 * write in WriterTest.
	 * 
	 * @throws MoreThanTwoTrianglesPerEdgeException
	 */
	@Test
	public void testReadWriteSTL() throws MoreThanTwoTrianglesPerEdgeException {

		Point p1 = new Point(1, 0, -1);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 1);
		Vector3d vect1 = new Vector3d(0, 0, 1);
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Triangle t1;
		t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect1);

		Point p4 = new Point(4, 5, 4);
		Point p5 = new Point(2, -3, -3);
		Point p6 = new Point(-2, 4, -5);
		Vector3d vect2 = new Vector3d(1, 0, 0);
		Edge e4 = new Edge(p4, p5);
		Edge e5 = new Edge(p5, p6);
		Edge e6 = new Edge(p6, p4);
		Triangle t2 = new Triangle(p4, p5, p6, e4, e5, e6, vect2);

		Mesh write = new Mesh();
		write.add(t1);
		write.add(t2);

		WriterSTL writerA = new WriterSTL("WriterTestA.stl",
				WriterSTL.ASCII_MODE);
		writerA.setMesh(write);
		writerA.write();

		WriterSTL writerB = new WriterSTL("WriterTestB.stl",
				WriterSTL.BINARY_MODE);
		writerB.setMesh(write);
		writerB.write();

		try {
			Mesh readA = new Mesh(ParserSTL.readSTL("WriterTestA.stl"));
			assertTrue(readA.size() == 2);
			ArrayList<Triangle> readListA = new ArrayList<Triangle>(readA);
			assertTrue(readListA.get(0).equals(t1)
					|| readListA.get(0).equals(t2));
			assertTrue(readListA.get(1).equals(t1)
					|| readListA.get(1).equals(t2));

			Mesh readB = new Mesh(ParserSTL.readSTL("WriterTestB.stl"));
			assertTrue(readB.size() == 2);
			ArrayList<Triangle> readListB = new ArrayList<Triangle>(readB);
			assertTrue(readListB.get(0).equals(t1)
					|| readListB.get(0).equals(t2));
			assertTrue(readListB.get(1).equals(t1)
					|| readListB.get(1).equals(t2));
		} catch (BadFormedFileException e) {
			fail("BadFormedFileException !");
		} catch (IOException e) {
			fail("IOException !");
		}

		assertTrue(new File("WriterTestA.stl").delete());
		assertTrue(new File("WriterTestB.stl").delete());
	}
}
