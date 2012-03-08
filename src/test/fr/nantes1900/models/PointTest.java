package test.fr.nantes1900.models;

import javax.vecmath.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import fr.nantes1900.models.basis.Point;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

/**
 * A set of tests for the class Point.
 * @author Daniel Lefevre
 */
public final class PointTest extends TestCase {

    /**
     * Constructor.
     */
    public PointTest() {
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Point#changeBase(double[][])}.
     */
    @Test
    public static void testChangeBase() {
        final Vector3d vect = new Vector3d(0, 0, 1);

        try {

            final double[][] matrix = MatrixMethod.createOrthoBase(vect);

            final double x = 1.2366646772;
            final double y = 435.23134144;
            final double z = -210.35681944;
            final Point p = new Point(x, y, z);

            p.changeBase(matrix);

            final Point pChanged = new Point(0, 0, 0);
            final double[] coords = { x, y, z
            };
            pChanged.set(MatrixMethod.changeBase(coords, matrix));

            Assert.assertTrue(p.equals(pChanged));

        } catch (final SingularMatrixException e) {
            Assert.fail();
        }
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Point#distance(fr.nantes1900.models.basis.Point)}
     * .
     */
    @Test
    public static void testDistance() {
        final Point p1 = new Point(0, 0, 0);
        final double x = 1.2366646772;
        final double y = 435.23134144;
        final double z = -210.35681944;
        final Point p2 = new Point(x, y, z);
        Assert.assertTrue(p1.distance(p2) == Math.sqrt(Math.pow(
                p2.getX() - p1.getX(), 2)
                + Math.pow(p2.getY() - p1.getY(), 2)
                + Math.pow(p2.getZ() - p1.getZ(), 2)));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Point#equals(java.lang.Object)}.
     */
    @Test
    public static void testEquals() {
        final Point p1 = new Point(0, 0, 0);
        Point p2 = p1;
        Assert.assertTrue(p2.equals(p1));

        p2 = new Point(p1);
        Assert.assertTrue(p2.equals(p1));

        p2.setX(1.0);
        Assert.assertFalse(p2.equals(p1));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Point#equals(java.lang.Object)}.
     */
    @Test
    public static void testEqualsObject() {
        final Point p1 = new Point(0, 0, 0);
        final Point p2 = new Point(0, 0, 0);

        Assert.assertTrue(p1.equals(p2));
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Point#getPointAsCoordinates()}.
     */
    @Test
    public static void testGetPointAsCoordinates() {
        final Point p1 = new Point(0.5, 0.5, 0.5);
        final double[] coords = p1.getPointAsCoordinates();
        Assert.assertTrue(coords[0] == 0.5 && coords[1] == 0.5
                && coords[2] == 0.5);
    }

    /**
     * Test method for {@link fr.nantes1900.models.basis.Point#hashCode()}.
     */
    @Test
    public static void testHashCode() {
        final Point p1 = new Point(0.0242515242412, 0, 0);
        final Point p2 = new Point(0.0242515244450, 0, 0);
        Assert.assertTrue(p1.hashCode() == p2.hashCode());
    }

    /**
     * Test method for {@link fr.nantes1900.models.basis.Point#set(double[])}.
     */
    @Test
    public static void testSetDoubleArray() {
        final double[] a = { 0.1, 0.2, 0.4
        };
        final Point p1 = new Point(0, 0, 0);
        p1.set(a);

        Assert.assertTrue(p1.getX() == 0.1);
        Assert.assertTrue(p1.getY() == 0.2);
        Assert.assertTrue(p1.getZ() == 0.4);
    }

    /**
     * Test method for
     * {@link fr.nantes1900.models.basis.Point#set(double, double, double)}.
     */
    @Test
    public static void testSetDoubleDoubleDouble() {
        final double a = 0.1;
        final double b = 0.2;
        final double c = 0.4;
        final Point p1 = new Point(0, 0, 0);
        p1.set(a, b, c);

        Assert.assertTrue(p1.getX() == 0.1);
        Assert.assertTrue(p1.getY() == 0.2);
        Assert.assertTrue(p1.getZ() == 0.4);
    }
}
