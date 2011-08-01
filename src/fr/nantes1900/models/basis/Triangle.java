package fr.nantes1900.models.basis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;

/**
 * Implement a triangle, composed of three points, three edges, and one vector
 * as a normal.
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Triangle {

    /**
     * List of 3 points of the triangle.
     */
    private final Point[] points = new Point[3];
    /**
     * Normal of the triangle.
     */
    private final Vector3d normal = new Vector3d();
    /**
     * List of 3 edges of the triangle.
     */
    private final Edge[] edges = new Edge[3];

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
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if one edge already contains 2 triangles
     */
    // TODO : reduce this constructor by making the edge inside.
    public Triangle(final Point point0, final Point point1,
            final Point point2, final Edge edge1,
            final Edge edge2, final Edge edge3,
            final Vector3d normalNew)
            throws MoreThanTwoTrianglesPerEdgeException {
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
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if one edge already contains 2 triangles
     */
    public Triangle(final Triangle triangle)
            throws MoreThanTwoTrianglesPerEdgeException {
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
     * Check if the this face is oriented as face with an error on the angle.
     * The error is in degree.
     * @param face
     *            the other triangle to compare with
     * @param error
     *            the orientation error
     * @return true if it is oriented as face, false otherwise
     */
    public final boolean angularTolerance(final Triangle face,
            final double error) {
        return this.angularTolerance(face.normal, error);
    }

    /**
     * Check if the this face is oriented as vector with an error on the angle.
     * The error is in degree.
     * @param vector
     *            the vector to compare with
     * @param error
     *            the orientation error
     * @return true if it is oriented as vector, false otherwise
     */
    public final boolean angularTolerance(final Vector3d vector,
            final double error) {
        return (this.normal.angle(vector) * 180.0 / Math.PI < error);
    }

    /**
     * Check if the triangle contains the edge e.
     * @param e
     *            the edge to check
     * @return true if the edge e is one of the edges of this, false otherwise.
     */
    public final boolean contains(final Edge e) {
        return this.getEdges().contains(e);
    }

    /**
     * Check if p is one of the three points of this. Use the method equals of
     * this class.
     * @param point
     *            the point to check
     * @return true is one point is equal with p
     */
    public final boolean contains(final Point point) {
        for (Point point2 : points) {
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        Triangle other = (Triangle) obj;

        return (this.contains(other.points[0])
                && this.contains(other.points[1]) && this
                    .contains(other.points[2]));
    }

    /**
     * Getter.
     * @return the first edge
     */
    public final Edge getE1() {
        return edges[0];
    }

    /**
     * Getter.
     * @return the second edge
     */
    public final Edge getE2() {
        return edges[1];
    }

    /**
     * Getter.
     * @return the third edge
     */
    public final Edge getE3() {
        return edges[2];
    }

    /**
     * Getter.
     * @return a collection containing the three edges
     */
    public final Collection<Edge> getEdges() {
        return Arrays.asList(this.edges);
    }

    /**
     * Return the neighbours of this triangle. Look in the edges to find the
     * other triangles which share those edges.
     * @return a list of the neighbours triangles
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if an edge is bad formed
     */
    public final List<Triangle> getNeighbours()
            throws MoreThanTwoTrianglesPerEdgeException {
        final List<Triangle> list = new ArrayList<Triangle>();
        Triangle other;

        for (Edge e : this.edges) {
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
    public Vector3d getNormal() {
        return normal;
    }

    /**
     * Return the number of neighbours of this triangle.
     * @return the number of neighbours
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    public int getNumNeighbours()
            throws MoreThanTwoTrianglesPerEdgeException {
        return this.getNeighbours().size();
    }

    /**
     * Getter.
     * @return the first point
     */
    public Point getP1() {
        return points[0];
    }

    /**
     * Getter.
     * @return the second point
     */
    public Point getP2() {
        return points[1];
    }

    /**
     * Getter.
     * @return the third point
     */
    public Point getP3() {
        return points[2];
    }

    /**
     * Getter.
     * @return a collection containing the three points
     */
    public Collection<Point> getPoints() {
        return Arrays.asList(this.points);
    }

    /**
     * Returns a collection of the coordinates of the points.
     * @return a collection of the coordinates of the points
     */
    public List<Double> getPointsAsCoordinates() {
        final List<Double> list = new ArrayList<Double>();
        for (Point p : points) {
            for (double d : p.getPointAsCoordinates()) {
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
    public int hashCode() {
        return ((points[0] == null) ? 0 : points[0].hashCode())
                + ((points[1] == null) ? 0 : points[1]
                        .hashCode())
                + ((points[2] == null) ? 0 : points[2]
                        .hashCode());
    }

    /**
     * Check if a triangle is a neighbours of this. This method calls the
     * getNeighbours method.
     * @param triangle
     *            the triangle to check
     * @return true if it is neighbours, false otherwise.
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    // TODO : code again this method : you don't need to call the big
    // getNeighbours to check that little thing.
    public boolean isNeighboor(Triangle triangle)
            throws MoreThanTwoTrianglesPerEdgeException {
        return this.getNeighbours().contains(triangle);
    }

    /**
     * Check if norm is normal to the normal of this triangle, with an error.
     * Caution : this error is not in degrees ! The error is compared with the
     * result of a dot product. Then this error must be between 0 and 1.
     * @param norm
     *            the vector to compare
     * @param error
     *            the error of orientation
     * @return true if this triangle is oriented normal to norm with an error,
     *         false otherwise.
     */
    public boolean isNormalTo(Vector3d norm, double error) {
        return (this.normal.dot(norm) < error && this.normal
                .dot(norm) > -error);
    }

    /**
     * Returns in ret the neighbours of this which belongs to m.
     * @param ret
     *            the returned mesh in which are the neighbours
     * @param container
     *            the mesh which all neighbours have to belong to
     * @throws MoreThanTwoTrianglesPerEdgeException
     */
    public void returnNeighbours(Mesh ret, Mesh container)
            throws MoreThanTwoTrianglesPerEdgeException {
        ret.add(this);

        Triangle other;

        for (Edge e : this.edges) {
            other = e.returnOther(this);
            if (other != null && container.contains(other)
                    && !ret.contains(other)) {
                other.returnNeighbours(ret, container);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return points[0].toString() + "\n"
                + points[1].toString() + "\n"
                + points[2].toString() + "\n"
                + normal.toString();
    }

    /**
     * Compute the average x-coordinate of the three points.
     * @return the average x-coordinate of the three points
     */
    public double xAverage() {
        return (points[0].getX() + points[1].getX() + points[2]
                .getX()) / 3;
    }

    /**
     * Compute the x-maximum of the three points.
     * @return the x-maximum of the three points
     */
    public double xMax() {
        return Math.max(points[0].getX(),
                Math.max(points[1].getX(), points[2].getX()));
    }

    /**
     * Compute the point at the x-maximum of the three points.
     * @return the point at the x-maximum of the three points.
     */
    public Point xMaxPoint() {
        final double xMax = this.xMax();
        for (Point p : points) {
            if (p.getX() == xMax) {
                return p;
            }
        }
        return null;
    }

    /**
     * Compute the x-minimum of the three points.
     * @return the x-minimum of the three points
     */
    public double xMin() {
        return Math.min(points[0].getX(),
                Math.min(points[1].getX(), points[2].getX()));
    }

    /**
     * Compute the point at the x-minimum of the three points.
     * @return the point at the x-minimum of the three points.
     */
    public Point xMinPoint() {
        double xMin = this.xMin();
        for (Point p : points) {
            if (p.getX() == xMin) {
                return p;
            }
        }
        return null;
    }

    /**
     * Compute the average y-coordinate of the three points.
     * @return the average y-coordinate of the three points
     */
    public double yAverage() {
        return (points[0].getY() + points[1].getY() + points[2]
                .getY()) / 3;
    }

    /**
     * Compute the y-maximum of the three points.
     * @return the y-maximum of the three points
     */
    public double yMax() {
        return Math.max(points[0].getY(),
                Math.max(points[1].getY(), points[2].getY()));
    }

    /**
     * Compute the point at the y-maximum of the three points.
     * @return the point at the y-maximum of the three points.
     */
    public Point yMaxPoint() {
        double yMax = this.yMax();
        for (Point p : points) {
            if (p.getY() == yMax) {
                return p;
            }
        }
        return null;
    }

    /**
     * Compute the y-minimum of the three points.
     * @return the y-minimum of the three points
     */
    public double yMin() {
        return Math.min(points[0].getY(),
                Math.min(points[1].getY(), points[2].getY()));
    }

    /**
     * Compute the point at the y-minimum of the three points.
     * @return the point at the y-minimum of the three points.
     */
    public Point yMinPoint() {
        double yMin = this.yMin();
        for (Point p : points) {
            if (p.getY() == yMin) {
                return p;
            }
        }
        return null;
    }

    /**
     * Compute the average z-coordinate of the three points.
     * @return the average z-coordinate of the three points
     */
    public double zAverage() {
        double zAverage = points[0].getZ() + points[1].getZ()
                + points[2].getZ();
        return zAverage / 3;
    }

    /**
     * Compute the z-maximum of the three points.
     * @return the z-maximum of the three points
     */
    public double zMax() {
        return Math.max(points[0].getZ(),
                Math.max(points[1].getZ(), points[2].getZ()));
    }

    /**
     * Compute the point at the z-maximum of the three points.
     * @return the point at the z-maximum of the three points.
     */
    public Point zMaxPoint() {
        double zMax = this.zMax();
        for (Point p : points) {
            if (p.getZ() == zMax) {
                return p;
            }
        }
        return null;
    }

    /**
     * Compute the z-minimum of the three points.
     * @return the z-minimum of the three points
     */
    public double zMin() {
        return Math.min(points[0].getZ(),
                Math.min(points[1].getZ(), points[2].getZ()));
    }

    /**
     * Compute the point at the z-minimum of the three points.
     * @return the point at the z-minimum of the three points.
     */
    public Point zMinPoint() {
        final double zMin = this.zMin();
        for (Point p : points) {
            if (p.getZ() == zMin) {
                return p;
            }
        }
        return null;
    }
}
