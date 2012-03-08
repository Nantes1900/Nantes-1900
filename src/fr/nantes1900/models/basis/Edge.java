package fr.nantes1900.models.basis;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3d;

/**
 * Implement an edge composed of two points, and the triangles containing this
 * edge.
 * @author Daniel Lefevre
 */
public class Edge {

    /**
     * Array of two points describing the edge.
     */
    private final Point[] points = new Point[2];

    /**
     * List of triangles containing this edge. The can be two triangles maximum.
     */
    private List<Triangle> triangles = new ArrayList<>(2);

    /**
     * The coefficient pi in degrees.
     */
    public static final double PI_DEGREES = 180;
    /**
     * The coefficients to convert pi to degrees.
     */
    public static final double CONVERSION_PI_DEGREES = Edge.PI_DEGREES
            / Math.PI;

    /**
     * Constructor.
     * @param point1
     *            the first point
     * @param point2
     *            the second point
     */
    public Edge(final Point point1, final Point point2) {
        this.points[0] = point1;
        this.points[1] = point2;
    }

    public Edge(final Edge edge) {
        this.points[0] = edge.points[0];
        this.points[1] = edge.points[1];
    }

    /**
     * Adds a triangle to the edge.
     * @param triangle
     *            the triangle to add
     */
    public final void addTriangle(final Triangle triangle) {
        if (!this.triangles.contains(triangle)) {
            if (this.triangles.size() <= 2) {
                this.triangles.add(triangle);
            }
        }
    }

    /**
     * Check if the edge contains that point.
     * @param point
     *            the point to check
     * @return true if p is contained, and false otherwise
     */
    public final boolean contains(final Point point) {
        return this.points[0] == point || this.points[1] == point;
    }

    /**
     * Converts the edge in a Vector3d. The coordinates of this vector are made
     * by substracting the coordinates of the second point by those of the first
     * point.
     * @return the vector
     */
    public final Vector3d convertToVector3d() {
        return new Vector3d(this.getP2().getX() - this.getP1().getX(), this
                .getP2().getY() - this.getP1().getY(), this.getP2().getZ()
                - this.getP1().getZ());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return ((Edge) obj).contains(this.points[0])
                && ((Edge) obj).contains(this.points[1]);
    }

    /**
     * Returns the number of triangles containing the edge.
     * @return the number of triangles
     */
    public final int getNumberTriangles() {
        return this.triangles.size();
    }

    /**
     * Getter.
     * @return the first point
     */
    public final Point getP1() {
        return this.points[0];
    }

    /**
     * Getter.
     * @return the second point
     */
    public final Point getP2() {
        return this.points[1];
    }

    /**
     * Getter.
     * @return a list of points containing the two points of the edge
     */
    public final List<Point> getPoints() {
        return Arrays.asList(this.points);
    }

    /**
     * Getter.
     * @return the list of triangles containing the edge
     */
    public final List<Triangle> getTriangles() {
        return this.triangles;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return this.points[0].hashCode() + this.points[1].hashCode();
    }

    /**
     * Checks if this edge is part of the border of the mesh m.
     * @param m
     *            the mesh
     * @return true if this edge contains only one triangle belonging to m,
     *         false otherwise.
     */
    public final boolean isBorder(final Mesh m) {
        int counter = 0;
        for (final Triangle triangle : this.triangles) {
            if (m.contains(triangle)) {
                ++counter;
            }
        }
        return counter == 1;
    }

    /**
     * Checks if the point is contained in the cylinder which axis is this edge,
     * which bounds are the two points of this edge, and which radius is the
     * error.
     * @param point
     *            the point to check
     * @param error
     *            the radius of the cylinder
     * @return true if p is contained in the cylinder and false otherwise
     */
    public final boolean isInCylinder3D(final Point point, final double error) {

        // Basic projection of a point on a line.
        final double x1 = this.getP1().getX();
        final double x2 = this.getP2().getX();
        final double x3 = point.getX();

        final double y1 = this.getP1().getY();
        final double y2 = this.getP2().getY();
        final double y3 = point.getY();

        final double z1 = this.getP1().getZ();
        final double z2 = this.getP2().getZ();
        final double z3 = point.getZ();

        final double lambda = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1) + (z3 - z1)
                * (z2 - z1))
                / ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1)
                        * (z2 - z1));

        final double x4 = lambda * (x2 - x1) + x1;
        final double y4 = lambda * (y2 - y1) + y1;
        final double z4 = lambda * (z2 - z1) + z1;

        return lambda >= 0 && lambda <= 1
                && point.distance(new Point(x4, y4, z4)) <= error;
    }

    /**
     * Checks if an edge is a neighboor of this edge.
     * @param e
     *            the edge to check
     * @return true if e shares one point with this edge, false if e is this
     *         edge, or if they are not neighboor
     */
    public final boolean isNeighboor(final Edge e) {
        return this != e
                && (this.contains(e.points[0]) || this.contains(e.points[1]));
    }

    /**
     * Calculates the length of the edge.
     * @return the distance between the two points of the edge
     */
    public final double length() {
        return this.points[0].distance(this.points[1]);
    }

    /**
     * Checks if this edge is oriented as another edge, with an orientation
     * error.
     * @param e
     *            the other edge
     * @param error
     *            the orientation error
     * @return true if they have the same orientation, false otherwise
     */
    public final boolean orientedAs(final Edge e, final double error) {

        // Builds two vectors and normalize them.
        final Vector3d vect1 = new Vector3d(
                e.getP2().getX() - e.getP1().getX(), e.getP2().getY()
                        - e.getP1().getY(), e.getP2().getZ() - e.getP1().getZ());
        vect1.normalize();

        final Vector3d vect2 = new Vector3d(this.getP2().getX()
                - this.getP1().getX(), this.getP2().getY()
                - this.getP1().getY(), this.getP2().getZ()
                - this.getP1().getZ());
        vect2.normalize();

        // Then calls the angle function to check their orientation.
        final double convertDegreeRadian = Edge.CONVERSION_PI_DEGREES;
        return (vect1.angle(vect2) < (error / convertDegreeRadian))
                || vect1.angle(vect2) > (((Edge.PI_DEGREES - error) / convertDegreeRadian));
    }

    /**
     * Returns the projection of a point on the edge. //FIXME : updates this
     * description.
     * @param point
     *            the point to project
     * @return the point projected
     */
    public final Point project(final Point point) {

        // Basic projection of a point on a line.
        final Point p1 = this.getP1();
        final Point p2 = this.getP2();
        final Point p3 = point;

        final double x1 = p1.getX();
        final double x2 = p2.getX();
        final double x3 = p3.getX();

        final double y1 = p1.getY();
        final double y2 = p2.getY();
        final double y3 = p3.getY();

        final double z1 = p1.getZ();
        final double z2 = p2.getZ();
        final double z3 = p3.getZ();

        final double lambda = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1) + (z3 - z1)
                * (z2 - z1))
                / ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1)
                        * (z2 - z1));

        if (lambda >= 1) {
            return new Point(p2);
        } else if (lambda <= 0) {
            return new Point(p1);
        }

        final double x4 = lambda * (x2 - x1) + x1;
        final double y4 = lambda * (y2 - y1) + y1;
        final double z4 = lambda * (z2 - z1) + z1;

        return new Point(x4, y4, z4);
    }

    /**
     * Returns the other point of the edge.
     * @param p
     *            one point of the edge
     * @return the other point which forms the edge
     */
    public final Point returnOther(final Point p) {
        if (this.getP1() == p) {
            return this.getP2();
        } else if (this.getP2() == p) {
            return this.getP1();
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * Returns the other triangle which contains this edge.
     * @param t
     *            one triangle which contains the edge
     * @return the other triangle which contains the edge
     */
    public final Triangle returnOther(final Triangle t) {

        Triangle triangle = null;

        if (this.triangles.size() == 2) {
            if (this.triangles.get(0) == t) {
                triangle = this.triangles.get(1);
            }
            if (this.triangles.get(1) == t) {
                triangle = this.triangles.get(0);
            }
        }

        return triangle;
    }

    /**
     * Setter.
     * @param point
     *            the first point
     */
    public final void setP1(final Point point) {
        this.synchronizeBeginning();
        this.points[0] = point;
        this.synchronizeEnd();
    }

    public final void synchronizeBeginning() {
        for (Triangle t : this.triangles) {
            t.synchronizeBeginning();
        }
    }

    public final void synchronizeEnd() {
        for (Triangle t : this.triangles) {
            t.synchronizeEnd();
        }
    }

    /**
     * Setter.
     * @param point
     *            the second point
     */
    public final void setP2(final Point point) {
        this.synchronizeBeginning();
        this.points[1] = point;
        this.synchronizeEnd();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return new String("(" + this.getP1() + ", " + this.getP2() + ")");
    }

    public boolean removeTriangle(Triangle triangle) {
        return this.triangles.remove(triangle);
    }

    public void replace(Point pOld, Point pNew) {
        if (this.points[0] == pOld) {
            this.synchronizeBeginning();
            this.points[0] = pNew;
            this.synchronizeEnd();
        } else if (this.points[1] == pOld) {
            this.synchronizeBeginning();
            this.points[1] = pNew;
            this.synchronizeEnd();
        }
    }

    public void removeTriangles(List<Triangle> list) {
        for (Triangle t : list) {
            this.removeTriangle(t);
        }
    }
}
