package fr.nantes1900.models.basis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.nantes1900.utils.MatrixMethod;

/**
 * Implements a point, composed of three double coordinates.
 * @author Daniel Lefèvre, Elsa Arroud-Vignod
 */
public class Point {

    /**
     * x coordinate.
     */
    private double x;

    /**
     * y coordinate.
     */
    private double y;

    /**
     * z coordinate.
     */
    private double z;

    /**
     * The list of the edges which contain this point.
     */
    private List<Edge> edges = new ArrayList<>();

    /**
     * Constructor.
     * @param xNew
     *            coordinate
     * @param yNew
     *            coordinate
     * @param zNew
     *            coordinate
     */
    public Point(final double xNew, final double yNew, final double zNew) {
        this.x = xNew;
        this.y = yNew;
        this.z = zNew;
    }

    /**
     * Copy constructor. To use very cautiously : it can create two Points with
     * equal values and different references !
     * @param point
     *            the point to copy
     */
    public Point(final Point point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
        this.edges = new ArrayList<>(point.edges);
    }

    /**
     * Operates a change base on the point. Be careful : the point doesn't have
     * the same hashCode after this operation...
     * @param matrix
     *            of base change
     */
    public final void changeBase(final double[][] matrix) {
        final double[] coords = {this.x, this.y, this.z,};
        this.set(MatrixMethod.changeBase(coords, matrix));
    }

    /**
     * Calculates the distance between two points.
     * @param p
     *            the other point
     * @return the distance
     */
    public final double distance(final Point p) {
        return Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2)
                + Math.pow(p.z - this.z, 2));
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
        final Point other = (Point) obj;
        if (Double.doubleToLongBits((float) this.x) != Double
                .doubleToLongBits((float) other.x)) {
            return false;
        }
        if (Double.doubleToLongBits((float) this.y) != Double
                .doubleToLongBits((float) other.y)) {
            return false;
        }
        if (Double.doubleToLongBits((float) this.z) != Double
                .doubleToLongBits((float) other.z)) {
            return false;
        }
        return true;
    }

    /**
     * Getter of the coordinates of the point.
     * @return an array of doubles
     */
    public final double[] getPointAsCoordinates() {
        return new double[]{this.x, this.y, this.z,};
    }

    /**
     * Getter.
     * @return the x coordinate
     */
    public final double getX() {
        return this.x;
    }

    /**
     * Getter.
     * @return the y coordinate
     */
    public final double getY() {
        return this.y;
    }

    /**
     * Getter.
     * @return the z coordinate
     */
    public final double getZ() {
        return this.z;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        final int hashTemp = 32;
        long temp;
        temp = Double.doubleToLongBits((float) this.x);
        result = prime * result + (int) (temp ^ (temp >>> hashTemp));
        temp = Double.doubleToLongBits((float) this.y);
        result = prime * result + (int) (temp ^ (temp >>> hashTemp));
        temp = Double.doubleToLongBits((float) this.z);
        result = prime * result + (int) (temp ^ (temp >>> hashTemp));
        return result;
    }

    /**
     * Setter.
     * @param xNew
     *            the new x coordinate
     * @param yNew
     *            the new y coordinate
     * @param zNew
     *            the new z coordinate
     */
    public final void set(final double xNew, final double yNew,
            final double zNew) {
        this.x = xNew;
        this.y = yNew;
        this.z = zNew;
    }

    /**
     * Setter.
     * @param coords
     *            the three coordinates
     */
    public final void set(final double[] coords) {
        this.setX(coords[0]);
        this.setY(coords[1]);
        this.setZ(coords[2]);
    }

    /**
     * Setter.
     * @param xNew
     *            : the x coordinate
     */
    public final void setX(final double xNew) {
        this.x = xNew;
    }

    /**
     * Setter.
     * @param yNew
     *            coordinate
     */
    public final void setY(final double yNew) {
        this.y = yNew;
    }

    /**
     * Setter.
     * @param zNew
     *            coordinate
     */
    public final void setZ(final double zNew) {
        this.z = zNew;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return new String("(" + this.x + ", " + this.y + ", " + this.z + ")");
    }

    /**
     * Searches the closest point of this in the list points.
     * @param points
     *            the list of points
     * @return the point from the list points which is the closest
     */
    public final Point getCloser(final List<Point> points) {
        Point pClose = points.get(0);
        double distance = this.distance(pClose);
        for (Point point : points) {
            if (point != this && this.distance(point) < distance) {
                distance = this.distance(point);
                pClose = point;
            }
        }

        return pClose;
    }

    /**
     * Adds an edge to the attribute list.
     * @param edge
     *            the edge
     */
    public final void addEdge(final Edge edge) {
        this.edges.add(edge);
    }

    /**
     * Returns the list of the edges.
     * @return the list of the edges
     */
    public final List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Returns the list of all the triangles which contains this point.
     * @return the list of all triangles which contains the edges of this point
     */
    public final List<Triangle> getTriangles() {
        Set<Triangle> triangles = new HashSet<>();
        for (Edge e : this.edges) {
            triangles.addAll(e.getTriangles());
        }
        return new ArrayList<>(triangles);
    }
}
