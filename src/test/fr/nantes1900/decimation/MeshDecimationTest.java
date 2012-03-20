package test.fr.nantes1900.decimation;

import java.io.IOException;
import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.decimation.MeshDecimation;
import fr.nantes1900.utils.ParserSTL;

public class MeshDecimationTest extends TestCase {

    @Test
    public final static void testDecimation() {
        ParserSTL parser = new ParserSTL("files/tests/testDecim.stl");

        try {
            MeshDecimation mesh = new MeshDecimation(parser.read());

            // 1. Compute the Qi matrices for each vi.
            mesh.computeQiMatrices();

            // 2. Select all valid pairs.
            mesh.selectValidPairs();

            // 3. Compute errors for all valid pairs.
            mesh.computeErrors();

            // 4. Sort valid pairs.
            Edge edge = mesh.selectMinimalErrorPair();

            // Test part.
            List<Triangle> triangles = edge.getTriangles();
            // End test part.

            // 5. Collapse the pair with the less cost.
            List<Edge> edges = mesh.collapseMinusCostPair(edge,
                    mesh.computeNewVertex(edge));

            // 6. Recomputes every errors.
            mesh.updateMatricesAndErrors(edges);

            // Test part.
            Assert.assertTrue(mesh.size() == 8);
            Assert.assertFalse(mesh.contains(triangles.get(0)));
            Assert.assertFalse(mesh.contains(triangles.get(1)));
            // End test part.

        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public final static void testReplace() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(1, 1, 0); // To replace !

        Point p4 = new Point(1, 0, 0); // With this !

        Edge e1 = new Edge(p1, p2);
        Edge e2 = new Edge(p2, p3);
        Edge e3 = new Edge(p3, p1);

        Edge e4 = new Edge(p4, p1);

        Triangle t1 = new Triangle(e1, e2, e3, new Vector3d(0, 0, 1));

        Mesh m = new Mesh();
        m.add(t1);
        Mesh mesh = new MeshDecimation(m);

        // Test part.
        e1.replace(p3, p4);
        Assert.assertTrue(mesh.contains(t1));
        e2.replace(p3, p4);
        Assert.assertTrue(mesh.contains(t1));
        e3.replace(p3, p4);
        Assert.assertTrue(mesh.contains(t1));

        t1.replace(e3, e4);

        Assert.assertTrue(t1.contains(e1));
        Assert.assertTrue(t1.contains(e2));
        Assert.assertFalse(t1.contains(e3));
        Assert.assertTrue(t1.contains(e4));

        Assert.assertTrue(mesh.contains(t1));
        // End test part.
    }
}
