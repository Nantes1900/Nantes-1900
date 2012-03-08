package fr.nantes1900.models.basis;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector3d;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Implements a polyline : a suite of edges.
 * @author Daniel Lefevre
 */
public class Polygon {

    /**
     * ID counter.
     */
    private static int currentID;

    /**
     * List of the points of the polyline.
     */
    private final List<Point> pointList = new ArrayList<>();

    /**
     * List of the edges of the polyline.
     */
    private final List<Edge> edgeList = new ArrayList<>();

    /**
     * Normal of the polyline.
     */
    private Vector3d normal = new Vector3d();

    /**
     * ID of the polyline.
     */
    private final int iD;

    /**
     * Void constructor.
     */
    public Polygon() {
        this.iD = ++Polygon.currentID;
    }

    /**
     * Constructor from a list of edges.
     * @param collection
     *            the list of edges
     */
    public Polygon(final Collection<Edge> collection) {
        for (final Edge e : collection) {
            // Checks if the objects added are not already contained in the
            // list.
            if (!this.edgeList.contains(e)) {
                this.edgeList.add(e);
            }
            if (!this.pointList.contains(e.getP1())) {
                this.pointList.add(e.getP1());
            }
            if (!this.pointList.contains(e.getP2())) {
                this.pointList.add(e.getP2());
            }
        }

        this.iD = ++Polygon.currentID;
    }

    /**
     * Copy constructor. Caution : this constructor make a copy of the points,
     * then after this method will exist duplicates with same values and
     * different references.
     * @param p
     *            the polyline to copy
     */
    public Polygon(final Polygon p) {
        this(p.getEdgeList());

        // For each point, make the list of the edges which contain this point.
        for (final Point point : p.pointList) {
            final List<Edge> belongings = new ArrayList<>();

            for (final Edge e : this.edgeList) {
                if (e.contains(point)) {
                    belongings.add(e);
                }
            }

            final Point copy = new Point(point);
            this.pointList.remove(point);
            this.pointList.add(copy);

            // And then replace this point, with a copy of it, in every edges.
            for (final Edge e : belongings) {
                if (e.getP1() == point) {
                    e.setP1(copy);
                } else if (e.getP2() == point) {
                    e.setP2(copy);
                }
            }
        }
    }

    /**
     * Adds an edge. Adds the edge only if it is not already contained, and adds
     * the points with the method add(Point).
     * @param e
     *            the edge to add
     */
    public final void add(final Edge e) {
        if (!this.edgeList.contains(e)) {
            this.edgeList.add(e);
        }
        this.add(e.getP1());
        this.add(e.getP2());
    }

    /**
     * Adds a point. Add sthe point only if it is not already contained.
     * @param p
     *            the point to add
     */
    public final void add(final Point p) {
        if (!this.pointList.contains(p)) {
            this.pointList.add(p);
        }
    }

    /**
     * Adds all the edges of the list.
     * @param l
     *            the list
     */
    public final void addAll(final List<Edge> l) {
        for (final Edge e : l) {
            this.add(e);
        }
    }

    /**
     * Applies the base change to all the points contained, without changing the
     * references. Caution : this method changes the hashCode of the points,
     * then be careful with the HashSets which contains points.
     * @param matrix
     *            the change base matrix
     */
    public final void changeBase(final double[][] matrix) {
        if (matrix == null) {
            throw new InvalidParameterException();
        }

        // For each point of the list, base change it.
        for (final Point p : this.pointList) {
            p.changeBase(matrix);
        }
    }

    /**
     * Clears the two lists of the polyline.
     */
    public final void clear() {
        this.edgeList.clear();
        this.pointList.clear();
    }

    /**
     * Checks if the edge is contained in the polyline.
     * @param e
     *            the edge to check
     * @return true if it is contained and false otherwise
     */
    public final boolean contains(final Edge e) {
        return this.edgeList.contains(e);
    }

    /**
     * Check if the point is contained in the polyline.
     * @param p
     *            the point to check
     * @return true if it is contained and false otherwise
     */
    public final boolean contains(final Point p) {
        return this.pointList.contains(p);
    }

    /**
     * Returns the size of the edge list.
     * @return the size of the edge list
     */
    public final int edgeSize() {
        return this.edgeList.size();
    }

    /**
     * Builds a cylinder, with the edge e as central axe, with error as radius,
     * and framed into the two points of the edge, and selects only the points
     * of this which are inside.
     * @param e
     *            the edge which will be the axe, and the two points will close
     *            the cylinder
     * @param error
     *            the radius
     * @return a list of points which are inside the cylinder
     */
    public final List<Point> getCylinder(final Edge e, final double error) {
        final List<Point> ret = new ArrayList<>();

        for (final Point p : this.pointList) {
            if (e.isInCylinder3D(p, error)) {
                ret.add(p);
            }
        }

        return ret;
    }

    /**
     * Getter.
     * @return the list of edges
     */
    public final List<Edge> getEdgeList() {
        return this.edgeList;
    }

    /**
     * Getter.
     * @return the iD
     */
    public final int getID() {
        return this.iD;
    }

    /**
     * Returns the edges belonging to this polyline that contain the point p.
     * @param p
     *            the point considered
     * @return the edges contained in this that contain the point p
     */
    public final List<Edge> getNeighbours(final Point p) {
        if (p == null) {
            throw new InvalidParameterException();
        }

        final List<Edge> list = new ArrayList<>();
        for (final Edge e : this.edgeList) {
            if (e.contains(p)) {
                list.add(e);
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
     * Returns the number of edges belonging to this that contain the point p.
     * @param p
     *            the point considered
     * @return the number of edges contained in this that contain the point p
     */
    public final int getNumNeighbours(final Point p) {
        int counter = 0;
        for (final Edge e : this.edgeList) {
            if (e.contains(p)) {
                counter = counter + 1;
            }
        }
        return counter;
    }

    /**
     * Returns one edge of the list.
     * @return one edge of the list Use iterator().next()
     */
    public final Edge getOne() {
        return this.edgeList.iterator().next();
    }

    /**
     * Getter.
     * @return the list of points
     */
    public final List<Point> getPointList() {
        return this.pointList;
    }

    /**
     * Converts the list of points in a list of coordinates as doubles.
     * @return a list of double as coordinates.
     */
    public final List<Double> getPointsAsCoordinates() {
        final List<Double> list = new ArrayList<>();
        for (final Point p : this.pointList) {
            for (final double d : p.getPointAsCoordinates()) {
                list.add(new Double(d));
            }
        }
        return list;
    }

    /**
     * Checks if the edge list is empty.
     * @return true if it's empty, false otherwise
     */
    public final boolean isEmpty() {
        return this.edgeList.isEmpty();
    }

    /**
     * Checks if the two polylines have at least one point in common.
     * @param p
     *            the polyline to search in
     * @return true if one point belongs to the other polyline, false otherwise
     */
    public final boolean isNeighbour(final Polygon p) {
        if (p != this) {
            for (final Edge e1 : this.edgeList) {
                if (p.contains(e1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Computes the length of the polyline.
     * @return the sum of all the edges that compose the polyline
     */
    public final double length() {
        double length = 0;
        for (final Edge e : this.edgeList) {
            length += e.length();
        }
        return length;
    }

    /**
     * Returns the average of the length of all the edges.
     * @return the average of the length of all the edges
     */
    public final double lengthAverage() {
        return Math.sqrt(Math.pow(this.xLengthAverage(), 2)
                + Math.pow(this.yLengthAverage(), 2)
                + Math.pow(this.zLengthAverage(), 2));
    }

    /**
     * Returns the edges that are oriented as the edge e, with an orientation
     * error.
     * @param e
     *            the edge to compare with
     * @param error
     *            the orientation error
     * @return the polyline containing all those edges
     */
    public final Polygon orientedAs(final Edge e, final double error) {
        final Polygon ret = new Polygon();

        for (final Edge edge : this.edgeList) {
            if (edge.orientedAs(e, error)) {
                ret.add(edge);
            }
        }

        return ret;
    }

    /**
     * Returns the size of the point list.
     * @return the size of the point list
     */
    public final int pointSize() {
        return this.pointList.size();
    }

    /**
     * Refreshes the list of points, when some edges have disappeared and when
     * some points are still belonging to this but not belonging to one edge.
     */
    public final void refresh() {
        final List<Edge> edges = new ArrayList<>(this.getEdgeList());
        this.clear();
        this.addAll(edges);
    }

    /**
     * Removes the occurences of e contained in this.
     * @param e
     *            the edge to remove Caution : it doesn't remove the points
     */
    public final void remove(final Edge e) {
        final List<Edge> edges = new ArrayList<>(this.edgeList);
        edges.remove(e);
        this.clear();
        this.addAll(edges);
    }

    /**
     * Removes the edges of del contained in this. Caution : it doesn't remove
     * the points. This method uses the remove(Edge) method
     * @param p
     *            the list of edges to remove
     */
    public final void remove(final Polygon p) {
        final List<Edge> edges = new ArrayList<>(this.edgeList);
        for (final Edge e : p.edgeList) {
            edges.remove(e);
        }
        this.clear();
        this.addAll(edges);
    }

    /**
     * Returns a mesh composed of the triangles formed by each edges and the
     * point centroid of the polyline. This is not really beautiful at the end,
     * but it's enough for debugging. Used for debugging.
     * @return a mesh representing the surface of the polyline
     */
    public final Mesh returnCentroidMesh() {
        if (this.isEmpty()) {
            return new Mesh();
        }
        final Mesh ens = new Mesh();

        final Point centroid = new Point(this.xAverage(), this.yAverage(),
                this.zAverage());

        Point before = this.pointList.get(this.pointSize() - 1);
        Point first = this.pointList.get(0);

        Vector3d vector1 = new Vector3d(before.getX() - centroid.getX(),
                before.getY() - centroid.getY(), before.getZ()
                        - centroid.getZ());
        Vector3d vector2 = new Vector3d(first.getX() - centroid.getX(),
                first.getY() - centroid.getY(), first.getZ() - centroid.getZ());
        Vector3d cross = new Vector3d();
        cross.cross(vector1, vector2);

        if (cross.dot(this.normal) > 0) {
            before = this.pointList.get(0);

            for (int i = this.pointList.size() - 1; i >= 0; i--) {
                Point p = this.pointList.get(i);

                Edge e1 = new Edge(centroid, before);
                Edge e2 = new Edge(before, p);
                Edge e3 = new Edge(p, centroid);

                ens.add(new Triangle(e1, e2, e3, this.normal));

                before = p;
            }
        } else {
            for (int i = 0; i < this.pointList.size(); i++) {
                Point p = this.pointList.get(i);

                Edge e1 = new Edge(centroid, before);
                Edge e2 = new Edge(before, p);
                Edge e3 = new Edge(p, centroid);

                ens.add(new Triangle(e1, e2, e3, this.normal));

                before = p;
            }
        }

        return ens;
    }

    /**
     * Returns the mesh composed all the triangles the edges belong to and which
     * belong to the mesh m. If one edge in this doesn't have one triangle to
     * return, this method returns the returnCentroidMesh()
     * @param m
     *            the mesh where all the edges are expected to belong
     * @return the mesh composed all the triangles the edges belong to
     */
    public final Mesh returnExistingMesh(final Mesh m) {

        // If there is Edges which have not triangles associated, the method
        // calls the returnCentroidMesh.
        for (final Edge e : this.edgeList) {
            if (e.getNumberTriangles() == 0) {
                return this.returnCentroidMesh();
            }
        }

        // Find every triangles which belong to the edges, and which belong to m
        // too.
        final Mesh ens = new Mesh();
        for (final Edge e : this.edgeList) {
            for (final Triangle t : e.getTriangles()) {
                if (m.contains(t)) {
                    ens.add(t);
                }
            }
        }
        return ens;
    }

    /**
     * Setter.
     * @param normalNew
     *            the normal
     */
    public final void setNormal(final Vector3d normalNew) {
        this.normal = normalNew;
    }

    /**
     * Returns the average of the x coordinate of all the points.
     * @return the average of the x coordinate of all the points
     */
    public final double xAverage() {
        double xAverage = 0;
        for (final Point p : this.pointList) {
            xAverage += p.getX();
        }
        return xAverage / this.pointList.size();
    }

    /**
     * Returns the polyline composed by the edges contained in this which the x
     * coordinates are in the bounds m1 and m2.
     * @param m1
     *            one bound
     * @param m2
     *            the other bound
     * @return the polyline composed by the edges contained in this which the x
     *         coordinates are in the bounds m1 and m2
     */
    public final Polygon xBetween(final double m1, final double m2) {
        final Polygon b = new Polygon();
        for (final Edge e : this.edgeList) {
            if (e.getP1().getX() > Math.min(m1, m2)
                    && e.getP1().getX() < Math.max(m1, m2)
                    && e.getP2().getX() > Math.min(m1, m2)
                    && e.getP2().getX() < Math.max(m1, m2)) {
                b.add(e);
            }
        }
        return b;
    }

    /**
     * Returns the average of the length on the x axis of all the edges.
     * @return the average of the length on the x axis of all the edges
     */
    public final double xLengthAverage() {
        double xLengthAve = 0;
        for (final Edge e : this.edgeList) {
            xLengthAve += Math.abs(e.getP1().getX() - e.getP2().getX());
        }
        return xLengthAve / this.edgeList.size();
    }

    /**
     * Returns the maximum of the x coordinates.
     * @return the maximum of the x coordinates
     */
    public final double xMax() {
        double xMaxi = Double.NEGATIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getX() > xMaxi) {
                xMaxi = p.getX();
            }
        }
        return xMaxi;
    }

    /**
     * Returns the minimum of the x coordinates.
     * @return the minimum of the x coordinates
     */
    public final double xMin() {
        double xMini = Double.POSITIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getX() < xMini) {
                xMini = p.getX();
            }
        }
        return xMini;
    }

    /**
     * Returns the average of the y coordinate of all the points.
     * @return the average of the y coordinate of all the points
     */
    public final double yAverage() {
        double yAverage = 0;
        for (final Point p : this.pointList) {
            yAverage += p.getY();
        }
        return yAverage / this.pointList.size();
    }

    /**
     * Returns the polyline composed by the edges contained in this which the y
     * coordinates are in the bounds m1 and m2.
     * @param m1
     *            one bound
     * @param m2
     *            the other bound
     * @return the polyline composed by the edges contained in this which the y
     *         coordinates are in the bounds m1 and m2
     */
    public final Polygon yBetween(final double m1, final double m2) {
        final Polygon b = new Polygon();
        for (final Edge e : this.edgeList) {
            if (e.getP1().getY() > Math.min(m1, m2)
                    && e.getP1().getY() < Math.max(m1, m2)
                    && e.getP2().getY() > Math.min(m1, m2)
                    && e.getP2().getY() < Math.max(m1, m2)) {
                b.add(e);
            }
        }
        return b;
    }

    /**
     * Returns the average of the length on the y axis of all the edges.
     * @return the average of the length on the y axis of all the edges
     */
    public final double yLengthAverage() {
        double yLengthAve = 0;
        for (final Edge e : this.edgeList) {
            yLengthAve += Math.abs(e.getP1().getY() - e.getP2().getY());
        }
        return yLengthAve / this.edgeList.size();
    }

    /**
     * Returns the maximum of the y coordinates.
     * @return the maximum of the y coordinates
     */
    public final double yMax() {
        double yMaxi = Double.NEGATIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getY() > yMaxi) {
                yMaxi = p.getY();
            }
        }
        return yMaxi;
    }

    /**
     * Returns the minimum of the y coordinates.
     * @return the minimum of the y coordinates
     */
    public final double yMin() {
        double yMini = Double.POSITIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getY() < yMini) {
                yMini = p.getY();
            }
        }
        return yMini;
    }

    /**
     * Returns the average of the z coordinate of all the points.
     * @return the average of the z coordinate of all the points
     */
    public final double zAverage() {
        double zAverage = 0;
        for (final Point p : this.pointList) {
            zAverage += p.getZ();
        }
        return zAverage / this.pointList.size();
    }

    /**
     * Returns the polyline composed by the edges contained in this which the z
     * coordinates are in the bounds m1 and m2.
     * @param m1
     *            one bound
     * @param m2
     *            the other bound
     * @return the polyline composed by the edges contained in this which the z
     *         coordinates are in the bounds m1 and m2
     */
    public final Polygon zBetween(final double m1, final double m2) {
        final Polygon b = new Polygon();
        for (final Edge e : this.edgeList) {
            if (e.getP1().getZ() > Math.min(m1, m2)
                    && e.getP1().getZ() < Math.max(m1, m2)
                    && e.getP2().getZ() > Math.min(m1, m2)
                    && e.getP2().getZ() < Math.max(m1, m2)) {
                b.add(e);
            }
        }
        return b;
    }

    /**
     * Returns the average of the length on the z axis of all the edges.
     * @return the average of the length on the z axis of all the edges
     */
    public final double zLengthAverage() {
        double zLengthAve = 0;
        for (final Edge e : this.edgeList) {
            zLengthAve += Math.abs(e.getP1().getZ() - e.getP2().getZ());
        }
        return zLengthAve / this.edgeList.size();
    }

    /**
     * Returns the maximum of the z coordinates.
     * @return the maximum of the z coordinates
     */
    public final double zMax() {
        double zMaxi = Double.NEGATIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getZ() > zMaxi) {
                zMaxi = p.getZ();
            }
        }
        return zMaxi;
    }

    /**
     * Returns the point of this which has the maximum z coordinate.
     * @return the point of this which has the maximum z coordinate
     */
    public final Point zMaxPoint() {
        Point point = null;
        if (this.pointList.isEmpty()) {
            throw new InvalidParameterException("Empty border !");
        }
        double zMax = Double.NEGATIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getZ() > zMax) {
                zMax = p.getZ();
                point = p;
            }
        }
        return point;
    }

    /**
     * Returns the minimum of the z coordinates.
     * @return the minimum of the z coordinates
     */
    public final double zMin() {
        double zMini = Double.POSITIVE_INFINITY;
        for (final Point p : this.pointList) {
            if (p.getZ() < zMini) {
                zMini = p.getZ();
            }
        }
        return zMini;
    }

    /**
     * Returns a polyline that is the copy of this, but where all points have
     * the same z. Caution : it modifies the points, then it must be a copy of
     * the points. Otherwise, the mesh containing these points, and using the
     * hashCode will be lost (because the hashCode of the points would have been
     * modified without the hash table of the mesh refreshed).
     * @param z
     *            the value to project on
     */
    public final void zProjection(final double z) {
        for (final Point p : this.pointList) {
            p.setZ(z);
        }
    }

    // FIXME : put this in polygon
    public boolean containsAllWithJts(List<Polygon> borders) {
        for (Polygon otherBorder : borders) {
            if (otherBorder != this
                    && !this.containsWithJts(otherBorder.convertPolygonToJts())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the lowest edge of a wall.
     * @param w
     *            the wall
     * @return the lowest edge of the wall null if no edge is found
     */
    // FIXME : put this method in Polygon.
    // FIXME : this method bring bugs...
    public Edge getDownEdge() {
        // Finds the lowest edge of the polygon.
        Edge edge = this.edgeList.get(0);
        Point p1 = edge.getP1();
        Point p2 = edge.getP2();

        for (Edge e : this.edgeList) {
            if ((e.getP1().getZ() <= p1.getZ() && e.getP2().getZ() <= p2.getZ())
                    || (e.getP1().getZ() <= p2.getZ() && e.getP2().getZ() <= p1
                            .getZ())) {
                edge = e;
                p1 = edge.getP1();
                p2 = edge.getP2();
            }
        }

        return edge;
    }

    // FIXME : put this in Polygon.
    public boolean containsWithJts(
            com.vividsolutions.jts.geom.Polygon containedJts) {
        com.vividsolutions.jts.geom.Polygon containerJts = this
                .convertPolygonToJts();

        return containerJts.contains(containedJts);
    }

    // FIXME : put this in Polygon.
    public boolean containsWithJts(Polygon contained) {
        com.vividsolutions.jts.geom.Polygon containerJts = this
                .convertPolygonToJts();

        return containerJts.contains(contained.convertPolygonToJts());
    }

    // FIXME : put this in Polygon.
    public com.vividsolutions.jts.geom.Polygon convertPolygonToJts() {
        List<Coordinate> coords = new ArrayList<>();
        for (Point p : this.getPointList()) {
            coords.add(new Coordinate(p.getX(), p.getY()));
        }

        GeometryFactory geom = new GeometryFactory();
        // A linear ring coordinates first and last point should be the same
        Coordinate[] coordsJts = new Coordinate[coords.size() + 1];
        coords.toArray(coordsJts);
        coordsJts[coords.size()] = coordsJts[0];

        return new com.vividsolutions.jts.geom.Polygon(
                geom.createLinearRing(coordsJts), null, geom);
    }

    /**
     * Implements an exception used when the polyline is bad formed.
     * @author Daniel Lefevre
     */
    public static final class BadFormedPolylineException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public BadFormedPolylineException() {
        }
    }
}
