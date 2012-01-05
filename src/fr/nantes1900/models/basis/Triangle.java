package fr.nantes1900.models.basis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector3d;

/**
 * Implements a triangle, composed of three points, three edges, and one vector
 * as a normal.
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Triangle {

    /**
     * Number of vertices of a triangle.
     */
    private static final int NB_VERTICES = 3;

    /**
     * Array of three points of the triangle.
     */
    private final Point[] points = new Point[NB_VERTICES];

    /**
     * Normal of the triangle.
     */
    private final Vector3d normal = new Vector3d();

    /**
     * Array of three edges of the triangle.
     */
    private final Edge[] edges = new Edge[NB_VERTICES];

    /**
     * Constructor of the triangle.
     * @param point0
     *            one point
     * @param point1
     *            one point
     * @param point2
     *            one point
     * @param edge1
     *            one edge composed by two of the three points
     * @param edge2
     *            one edge composed by two of the three points
     * @param edge3
     *            one edge composed by two of the three points
     * @param normalNew
     *            the normal of the triangle
     */
    public Triangle(final Point point0, final Point point1, final Point point2,
            final Edge edge1, final Edge edge2, final Edge edge3,
            final Vector3d normalNew) {

        this.points[0] = point0;
        this.points[1] = point1;
        this.points[2] = point2;
        this.normal.set(normalNew);
        this.edges[0] = edge1;
        this.edges[1] = edge2;
        this.edges[2] = edge3;
        this.edges[0].addTriangle(this);
        this.edges[1].addTriangle(this);
        this.edges[2].addTriangle(this);
    }

    /**
     * Copy constructor of the triangle. Caution : do not use this constructor
     * to create double Triangle with equal values and different references !
     * This method create new points so as the old ones are not modified.
     * @param triangle
     *            the triangle to copy
     */
    public Triangle(final Triangle triangle) {
        this.points[0] = new Point(triangle.points[0]);
        this.points[1] = new Point(triangle.points[1]);
        this.points[2] = new Point(triangle.points[2]);
        this.normal.set(new Vector3d(triangle.normal));
        this.edges[0] = new Edge(this.points[0], this.points[1]);
        this.edges[1] = new Edge(this.points[1], this.points[2]);
        this.edges[2] = new Edge(this.points[2], this.points[0]);
        this.edges[0].addTriangle(this);
        this.edges[1].addTriangle(this);
        this.edges[2].addTriangle(this);
    }

    /**
     * Checks if the this triangle is oriented as the other triangle with an
     * angle error. The error is in degree.
     * @param face
     *            the other triangle to compare with
     * @param error
     *            the angle error
     * @return true if they have the same orientation, false otherwise
     */
    public final boolean angularTolerance(final Triangle face,
            final double error) {
        return this.angularTolerance(face.normal, error);
    }

    /**
     * Checks if this triangle is oriented as the vector with an error on the
     * angle. The error is in degree.
     * @param vector
     *            the vector to compare with
     * @param error
     *            the orientation error
     * @return true if its normal and the vector have the same orientation,
     *         false otherwise
     */
    public final boolean angularTolerance(final Vector3d vector,
            final double error) {
        return this.normal.angle(vector) * Edge.CONVERSION_PI_DEGREES < error;
    }

    /**
     * Checks if this triangle contains the edge e.
     * @param e
     *            the edge to check
     * @return true if the edge e is one of the edges of this triangle, false
     *         otherwise.
     */
    public final boolean contains(final Edge e) {
        return this.getEdges().contains(e);
    }

    /**
     * Checks if p is one of the three points of this. Use the method equals of
     * this class.
     * @param point
     *            the point to check
     * @return true is one point is equal with p
     */
    public final boolean contains(final Point point) {
        for (final Point point2 : this.points) {
            if (point.equals(point2)) {
                return true;
            }
        }
        return false;
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
        final Triangle other = (Triangle) obj;

        return this.contains(other.points[0]) && this.contains(other.points[1])
                && this.contains(other.points[2]);
    }

    /**
     * Getter.
     * @return the first edge
     */
    public final Edge getE1() {
        return this.edges[0];
    }

    /**
     * Getter.
     * @return the second edge
     */
    public final Edge getE2() {
        return this.edges[1];
    }

    /**
     * Getter.
     * @return the third edge
     */
    public final Edge getE3() {
        return this.edges[2];
    }

    /**
     * Getter.
     * @return a collection containing the three edges
     */
    public final Collection<Edge> getEdges() {
        return Arrays.asList(this.edges);
    }

    /**
     * Returns the neighbours of this triangle. Look in the edges to find the
     * other triangles which share those edges.
     * @return a list of the neighbours triangles
     */
    public final List<Triangle> getNeighbours() {
        final List<Triangle> list = new ArrayList<>();
        Triangle other;

        for (final Edge e : this.edges) {
            other = e.returnOther(this);
            if (other != null) {
                list.add(other);
            }
        }

        return list;
    }

    /**
     * Getter.
     * @return the normal
     */
    public final Vector3d getNormal() {
        return this.normal;
    }

    /**
     * Returns the number of neighbours of this triangle. Two triangles are
     * neighbours if they share one edge.
     * @return the number of neighbours
     */
    public final int getNumNeighbours() {

        int number = 0;
        for (final Edge e : this.edges) {
            number += e.getNumberTriangles() - 1;
        }

        return number;
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
     * @return the third point
     */
    public final Point getP3() {
        return this.points[2];
    }

    /**
     * Getter.
     * @return a collection containing the three points
     */
    public final Collection<Point> getPoints() {
        return Arrays.asList(this.points);
    }

    /**
     * Returns a collection of Double composed of the coordinates of the points.
     * @return a collection of the coordinates of the points
     */
    @SuppressWarnings("boxing")
    public final List<Double> getPointsAsCoordinates() {
        final List<Double> list = new ArrayList<>();
        for (final Point p : this.points) {
            for (final double d : p.getPointAsCoordinates()) {
                list.add(d);
            }
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return this.points[0].hashCode() + this.points[1].hashCode()
                + this.points[2].hashCode();
    }

    /**
     * Checks if this triangle is located between two planes. These planes are
     * normal to the vector, and are located from each side of the point with a
     * distance to the point equal to the error.
     * @param vect
     *            the vector normal of the two planes
     * @param p
     *            the point which locates the planes in space. It's at the
     *            middle of the two planes.
     * @param error
     *            the distance between the planes and the point
     * @return true if the first point of the triangle is located between those
     *         two planes, false otherwise.
     */
    public final boolean isInPlanes(final Vector3d vect, final Point p,
            final double error) {

        final Edge axisNormalGround = new Edge(new Point(0, 0, 0), new Point(
                vect.x, vect.y, vect.z));

        final Point pAverage = axisNormalGround.project(p);

        final Point projectedPoint = axisNormalGround.project(this.getP1());

        return projectedPoint.distance(pAverage) < error;
    }

    /**
     * Checks if a triangle is a neighbour of this triangle.
     * @param triangle
     *            the triangle to check
     * @return true if they share an edge, false otherwise.
     */
    public final boolean isNeighboor(final Triangle triangle) {

        Triangle other;
        for (final Edge e : this.edges) {
            other = e.returnOther(this);
            if (other == triangle) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the vector is normal to the normal of this triangle, with an
     * error. Caution : this error is not in degrees ! The error is compared
     * with the result of a dot product. Then it must be between 0 and 1.
     * @param norm
     *            the vector to compare with
     * @param error
     *            the orientation error
     * @return true if this triangle is normal to norm with an error, false
     *         otherwise.
     */
    public final boolean isNormalTo(final Vector3d norm, final double error) {
        return this.normal.dot(norm) < error && this.normal.dot(norm) > -error;
    }

    /**
     * Recursive method which returns in ret all the neighbours (and the
     * neighbours of these neighbours...) of this triangle which belong to the
     * container.
     * @param ret
     *            the mesh in which are returned the triangles
     * @param container
     *            the mesh which must contain all the triangles
     */
    public final void returnNeighbours(final Mesh ret, final Mesh container) {

        // Add this triangle.
        ret.add(this);

        Triangle other;

        // For each neighbour, ...
        for (final Edge e : this.edges) {
            other = e.returnOther(this);
            // ... Which is not yet in ret, and which belongs to container ...
            if (other != null && container.contains(other)
                    && !ret.contains(other)) {

                // ... Calls this method on this neighbour.
                other.returnNeighbours(ret, container);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return this.points[0].toString() + "\n" + this.points[1].toString()
                + "\n" + this.points[2].toString() + "\n"
                + this.normal.toString();
    }

    /**
     * Calculates the average x-coordinate of the three points.
     * @return the average x-coordinate of the three points
     */
    public final double xAverage() {
        return (this.points[0].getX() + this.points[1].getX() + this.points[2]
                .getX()) / Triangle.NB_VERTICES;
    }

    /**
     * Calculates the x-maximum of the three points.
     * @return the x-maximum of the three points
     */
    public final double xMax() {
        return Math.max(this.points[0].getX(),
                Math.max(this.points[1].getX(), this.points[2].getX()));
    }

    /**
     * Calculates the point at the x-maximum of the three points.
     * @return the point at the x-maximum of the three points.
     */
    public final Point xMaxPoint() {
        final double xMax = this.xMax();
        for (final Point p : this.points) {
            if (p.getX() == xMax) {
                return p;
            }
        }
        return null;
    }

    /**
     * Calculates the x-minimum of the three points.
     * @return the x-minimum of the three points
     */
    public final double xMin() {
        return Math.min(this.points[0].getX(),
                Math.min(this.points[1].getX(), this.points[2].getX()));
    }

    /**
     * Calculates the point at the x-minimum of the three points.
     * @return the point at the x-minimum of the three points.
     */
    public final Point xMinPoint() {
        final double xMin = this.xMin();
        for (final Point p : this.points) {
            if (p.getX() == xMin) {
                return p;
            }
        }
        return null;
    }

    /**
     * Calculates the average y-coordinate of the three points.
     * @return the average y-coordinate of the three points
     */
    public final double yAverage() {
        return (this.points[0].getY() + this.points[1].getY() + this.points[2]
                .getY()) / Triangle.NB_VERTICES;
    }

    /**
     * Calculates the y-maximum of the three points.
     * @return the y-maximum of the three points
     */
    public final double yMax() {
        return Math.max(this.points[0].getY(),
                Math.max(this.points[1].getY(), this.points[2].getY()));
    }

    /**
     * Calculates the point at the y-maximum of the three points.
     * @return the point at the y-maximum of the three points.
     */
    public final Point yMaxPoint() {
        final double yMax = this.yMax();
        for (final Point p : this.points) {
            if (p.getY() == yMax) {
                return p;
            }
        }
        return null;
    }

    /**
     * Calculates the y-minimum of the three points.
     * @return the y-minimum of the three points
     */
    public final double yMin() {
        return Math.min(this.points[0].getY(),
                Math.min(this.points[1].getY(), this.points[2].getY()));
    }

    /**
     * Calculates the point at the y-minimum of the three points.
     * @return the point at the y-minimum of the three points.
     */
    public final Point yMinPoint() {
        final double yMin = this.yMin();
        for (final Point p : this.points) {
            if (p.getY() == yMin) {
                return p;
            }
        }
        return null;
    }

    /**
     * Calculates the average z-coordinate of the three points.
     * @return the average z-coordinate of the three points
     */
    public final double zAverage() {
        final double zAverage = this.points[0].getZ() + this.points[1].getZ()
                + this.points[2].getZ();
        return zAverage / Triangle.NB_VERTICES;
    }

    /**
     * Calculates the z-maximum of the three points.
     * @return the z-maximum of the three points
     */
    public final double zMax() {
        return Math.max(this.points[0].getZ(),
                Math.max(this.points[1].getZ(), this.points[2].getZ()));
    }

    /**
     * Calculates the point at the z-maximum of the three points.
     * @return the point at the z-maximum of the three points.
     */
    public final Point zMaxPoint() {
        final double zMax = this.zMax();
        for (final Point p : this.points) {
            if (p.getZ() == zMax) {
                return p;
            }
        }
        return null;
    }

    /**
     * Calculates the z-minimum of the three points.
     * @return the z-minimum of the three points
     */
    public final double zMin() {
        return Math.min(this.points[0].getZ(),
                Math.min(this.points[1].getZ(), this.points[2].getZ()));
    }

    /**
     * Calculates the point at the z-minimum of the three points.
     * @return the point at the z-minimum of the three points.
     */
    public final Point zMinPoint() {
        final double zMin = this.zMin();
        for (final Point p : this.points) {
            if (p.getZ() == zMin) {
                return p;
            }
        }
        return null;
    }
}
