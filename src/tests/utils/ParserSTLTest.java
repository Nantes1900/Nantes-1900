package tests.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import models.Mesh;
import models.basis.Edge;
import models.basis.Point;
import models.basis.Triangle;

import org.junit.Test;

import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;

/**
 * A set of tests for the class Parser.
 * 
 * @author Daniel Lefevre
 */
public class ParserSTLTest {

	/**
	 * Test method for {@link utils.ParserSTL#readSTL(java.lang.String)}. Same
	 * test as write in WriterTest.
	 */
	@Test
	public void testReadSTL() {

		Point p1 = new Point(1, 0, -1);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 1);
		Vector3d vect1 = new Vector3d(0, 0, 1);
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect1);

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

		write.writeSTL("WriterTest.stl");
		try {
			Mesh read = new Mesh(ParserSTL.readSTL("WriterTest.stl"));
			assertTrue(read.size() == 2);
			ArrayList<Triangle> readList = new ArrayList<Triangle>(read);
			assertTrue(readList.get(0).equals(t1) || readList.get(0).equals(t2));
			assertTrue(readList.get(1).equals(t1) || readList.get(1).equals(t2));
		} catch (BadFormedFileException e) {
			fail("BadFormedFileException !");
		} catch (IOException e) {
			fail("IOException !");
		}

		new File("WriterTest.stl").deleteOnExit();
	}

}
