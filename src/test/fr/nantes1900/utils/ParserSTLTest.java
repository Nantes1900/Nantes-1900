package test.fr.nantes1900.utils;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.utils.ParserSTL.BadFormedFileException;
import fr.nantes1900.utils.WriterSTL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * A set of tests for the class Parser.
 * 
 * @author Daniel Lefevre
 */
public final class ParserSTLTest extends TestCase {

    /**
     * Private constructor.
     */
    private ParserSTLTest() {
    }

    /**
     * Test method for
     * {@link nantes1900pjct.utils.ParserSTL#read(java.lang.String)} and
     * {@link nantes1900.utils.WriteSTL#writeSTL(java.lang.String)} Same test as
     * write in WriterTest.
     */
    @Test
    public void testReadWriteSTL() {

        try {
            final Point p1 = new Point(1, 0, -1);
            final Point p2 = new Point(0, 1, 0);
            final Point p3 = new Point(-1, 2, 1);
            final Vector3d vect1 = new Vector3d(0, 0, 1);
            final Edge e1 = new Edge(p1, p2);
            final Edge e2 = new Edge(p2, p3);
            final Edge e3 = new Edge(p3, p1);
            Triangle t1;
            t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect1);

            final Point p4 = new Point(4, 5, 4);
            final Point p5 = new Point(2, -3, -3);
            final Point p6 = new Point(-2, 4, -5);
            final Vector3d vect2 = new Vector3d(1, 0, 0);
            final Edge e4 = new Edge(p4, p5);
            final Edge e5 = new Edge(p5, p6);
            final Edge e6 = new Edge(p6, p4);
            final Triangle t2 = new Triangle(p4, p5, p6, e4, e5, e6, vect2);

            final Mesh write = new Mesh();
            write.add(t1);
            write.add(t2);

            final WriterSTL writerA =
                new WriterSTL("WriterTestA.stl", WriterSTL.ASCII_MODE);
            writerA.setMesh(write);
            writerA.write();

            final WriterSTL writerB =
                new WriterSTL("WriterTestB.stl", WriterSTL.BINARY_MODE);
            writerB.setMesh(write);
            writerB.write();

            try {
                final ParserSTL parserA = new ParserSTL("WriterTestA.stl");
                final Mesh readA = parserA.read();
                Assert.assertTrue(readA.size() == 2);
                final List<Triangle> readListA = new ArrayList<Triangle>(readA);
                Assert.assertTrue(readListA.get(0).equals(t1)
                    || readListA.get(0).equals(t2));
                Assert.assertTrue(readListA.get(1).equals(t1)
                    || readListA.get(1).equals(t2));

                final ParserSTL parserB = new ParserSTL("WriterTestB.stl");
                final Mesh readB = parserB.read();
                Assert.assertTrue(readB.size() == 2);
                final List<Triangle> readListB = new ArrayList<Triangle>(readB);
                Assert.assertTrue(readListB.get(0).equals(t1)
                    || readListB.get(0).equals(t2));
                Assert.assertTrue(readListB.get(1).equals(t1)
                    || readListB.get(1).equals(t2));

            } catch (BadFormedFileException e) {
                Assert.fail("BadFormedFileException !");
            } catch (IOException e) {
                Assert.fail("IOException !");
            }
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            Assert.fail("More than two triangles per edge exception !");
        }

        Assert.assertTrue(new File("WriterTestA.stl").delete());
        Assert.assertTrue(new File("WriterTestB.stl").delete());
    }
}
