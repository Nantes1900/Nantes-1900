package fr.nantes1900.models;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

/**
 * Implement a polyline : a closed suite of points.
 * 
 * @author Daniel Lefevre
 */
public class Polyline {

    /**
     * ID counter.
     */
    private static int currentID;
    /**
     * List of point of the polyline.
     */
    private List<Point> pointList = new ArrayList<Point>();
    /**
     * List of edges of the polyline.
     */
    private List<Edge> edgeList = new ArrayList<Edge>();

    /**
     * Normal vector to the polyline.
     */
    private Vector3d normal = new Vector3d();

    /**
     * Other Polyline which are neighbours of this.
     */
    private List<Polyline> neighbours = new ArrayList<Polyline>();

    /**
     * ID of the polyline.
     */
    private final int iD;

    /**
     * Void constructor.
     */
    public Polyline() {
        this.iD = ++Polyline.currentID;
    }

    /**
     * Constructor from a list of edges.
     * 
     * @param a
     *            the list of edges
     */
    public Polyline(final List<Edge> a) {
        for (Edge e : a) {
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

        this.iD = ++Polyline.currentID;
    }

    /**
     * Copy constructor. Caution : this constructor make a copy of the points,
     * then after this method will exist duplicates with same values and
     * different references.
     * 
     * @param p
     *            the polyline to copy
     */
    public Polyline(final Polyline p) {
        this(p.getEdgeList());

        // For each point, make the list of the edges which contain this point.
        for (Point point : p.pointList) {
            final List<Edge> belongings = new ArrayList<Edge>();

            for (Edge e : this.edgeList) {
                if (e.contains(point)) {
                    belongings.add(e);
                }
            }

            final Point copy = new Point(point);
            this.pointList.remove(point);
            this.pointList.add(copy);

            // And then replace this point, with a copy of it, in every edges.
            for (Edge e : belongings) {
                if (e.getP1() == point) {
                    e.setP1(copy);
                } else if (e.getP2() == point) {
                    e.setP2(copy);
                }
            }
        }
    }

    /**
     * Add an edge. Add the edge only if it is not already contained, and add
     * the points with the method add(Point)
     * 
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
     * Add a point. Add the point only if it is not already contained
     * 
     * @param p
     *            the point to add
     */
    public final void add(final Point p) {
        if (!this.pointList.contains(p)) {
            this.pointList.add(p);
        }
    }

    /**
     * Add all the edges of the list.
     * 
     * @param l
     *            the list
     */
    public final void addAll(final List<Edge> l) {
        for (Edge e : l) {
            this.add(e);
        }
    }

    /**
     * Add a neighbours to the attribute neighbours. Check if it is not
     * contained.
     * 
     * @param p
     *            the polyline as neighbour to add
     */
    public final void addNeighbour(final Polyline p) {
        if (!this.neighbours.contains(p)) {
            this.neighbours.add(p);
        }
    }

    /**
     * Apply the base change to all the points contained, without changing the
     * references. Caution : this method changes the hashCode, then be careful
     * with the hashTables which contains points
     * 
     * @param matrix
     *            the change base matrix
     */
    public final void changeBase(final double[][] matrix) {
        if (matrix == null) {
            throw new InvalidParameterException();
        }

        // For each point of the list, base change it.
        for (Point p : this.pointList) {
            p.changeBase(matrix);
        }
    }

    /**
     * Clear the edges and the points of the polyline.
     */
    public final void clear() {
        this.edgeList.clear();
        this.pointList.clear();
    }

    /**
     * Check if e is contained in the polyline.
     * 
     * @param e
     *            the edge to check
     * @return true if it is contained and false otherwise
     */
    public final boolean contains(final Edge e) {
        return this.edgeList.contains(e);
    }

    /**
     * Check if p is contained in the polyline.
     * 
     * @param p
     *            the point to check
     * @return true if it is contained and false otherwise
     */
    public final boolean contains(final Point p) {
        return this.pointList.contains(p);
    }

    /**
     * Returns the size of the edge list.
     * 
     * @return the size of the edge list
     */
    public final int edgeSize() {
        return this.edgeList.size();
    }

    /**
     * Build a cylinder, with the edge e as central axe, with error as radius,
     * and framed into the two points of the edge, and select only the points of
     * this which are inside.
     * 
     * @param e
     *            the edge which will be the axe, and the two points will close
     *            the cylinder
     * @param error
     *            the radius
     * @return a list of points which are inside the cylinder
     */
    public final List<Point> getCylinder(final Edge e, final double error) {
        final List<Point> ret = new ArrayList<Point>();

        for (Point p : this.pointList) {
            if (e.isInCylinder3D(p, error)) {
                ret.add(p);
            }
        }

        return ret;
    }

    /**
     * Getter.
     * 
     * @return the list of edges
     */
    public final List<Edge> getEdgeList() {
        return this.edgeList;
    }

    /**
     * Getter.
     * 
     * @return the iD
     */
    public final int getID() {
        return this.iD;
    }

    /**
     * Getter.
     * 
     * @return the neighbours
     */
    public final List<Polyline> getNeighbours() {
        return this.neighbours;
    }

    /**
     * Returns the edges contained in this that contain the point p.
     * 
     * @param p
     *            the point considered
     * @return the edges contained in this that contain the point p
     */
    public final List<Edge> getNeighbours(final Point p) {
        if (p == null) {
            throw new InvalidParameterException();
        }

        final List<Edge> list = new ArrayList<Edge>();
        for (Edge e : this.edgeList) {
            if (e.contains(p)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Getter.
     * 
     * @return the normal
     */
    public final Vector3d getNormal() {
        return this.normal;
    }

    /**
     * Returns the number of edges contained in this that contain the point p.
     * 
     * @param p
     *            the point considered
     * @return the number of edges contained in this that contain the point p
     */
    public final int getNumNeighbours(final Point p) {
        int counter = 0;
        for (Edge e : this.edgeList) {
            if (e.contains(p)) {
                counter = counter + 1;
            }
        }
        return counter;
    }

    /**
     * Return one edge of the list.
     * 
     * @return one edge of the list Use iterator().next()
     */
    public final Edge getOne() {
        return this.edgeList.iterator().next();
    }

    /**
     * Getter.
     * 
     * @return the list of points
     */
    public final List<Point> getPointList() {
        return this.pointList;
    }

    /**
     * Convert the list of points in a list of coordinates as doubles.
     * 
     * @return a list of double as coordinates.
     */
    public final List<Double> getPointsAsCoordinates() {
        final List<Double> list = new ArrayList<Double>();
        for (Point p : this.pointList) {
            for (double d : p.getPointAsCoordinates()) {
                list.add(new Double(d));
            }
        }
        return list;
    }

    /**
     * Check if the edge list is empty.
     * 
     * @return true if it's empty, false otherwise
     */
    public final boolean isEmpty() {
        return this.edgeList.isEmpty();
    }

    /**
     * Check if the two polylines has at least one point of one close to one
     * point of the other.
     * 
     * @param p
     *            the polyline to search in
     * @return true if one point is find close to another of the other polyline,
     *         false otherwise
     */
    public final boolean isNeighbour(final Polyline p) {
        if (p != this) {
            for (Edge e1 : this.edgeList) {
                if (p.contains(e1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compute the length of the polyline.
     * 
     * @return the sum of all the edges that compose the polyline
     */
    public final double length() {
        double length = 0;
        for (Edge e : this.edgeList) {
            length += e.length();
        }
        return length;
    }

    /**
     * Returns the average of the length of all the edges.
     * 
     * @return the average of the length of all the edges
     */
    public final double lengthAverage() {
        return Math.sqrt(Math.pow(this.xLengthAverage(), 2)
            + Math.pow(this.yLengthAverage(), 2)
            + Math.pow(this.zLengthAverage(), 2));
    }

    /**
     * Order the polyline. Each edge in the edge list will be surrounded by its
     * neighbours. The polyline must be well formed and must be able to be
     * ordered.
     */
    public final void order() {
        if (!this.isEmpty()) {
            final Polyline ret = new Polyline();

            try {

                final Edge first = this.getEdgeList().get(0);
                Point p = first.getP1();
                Edge e = first.returnNeighbour(this, p);
                p = e.returnOther(p);

                // While we are not back to the beginning, add it to ret.
                while (e != first) {
                    ret.add(e);
                    e = e.returnNeighbour(this, p);
                    p = e.returnOther(p);
                }
                ret.add(e);

                this.edgeList.clear();
                this.pointList.clear();
                this.addAll(ret.getEdgeList());

            } catch (BadFormedPolylineException e1) {
                e1.printStackTrace();
            }
        }
    }

    public final void orderAsYouCan() {
        if (!this.isEmpty()) {
            final Polyline ret = new Polyline();

            try {
                final Edge first = this.getEdgeList().get(0);
                Point p = first.getP1();
                Edge e = first.returnNeighbour(this, p);
                p = e.returnOther(p);

                // While we are not back to the beginning, add it to ret.
                while (e != first) {
                    ret.add(e);
                    e = e.returnNeighbour(this, p);
                    p = e.returnOther(p);
                }
                ret.add(e);

                this.edgeList.clear();
                this.pointList.clear();
                this.addAll(ret.getEdgeList());

            } catch (BadFormedPolylineException e1) {
                e1.printStackTrace();
            }
        }
    }

    public final boolean canBeOrdered() {
        if (!this.isEmpty()) {
            for (Edge e1 : this.edgeList) {
                int counter = 0;
                for (Edge e2 : this.edgeList) {
                    if (e1.isNeighboor(e2)) {
                        ++counter;
                    }
                }
                if (counter != 2) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the edges that are oriented as the edge e, with an orientation
     * error.
     * 
     * @param e
     *            the edge to compare with
     * @param error
     *            the orientation error
     * @return the polyline containing all those edges
     */
    public final Polyline orientedAs(final Edge e, final double error) {
        final Polyline ret = new Polyline();

        for (Edge edge : this.edgeList) {
            if (edge.orientedAs(e, error)) {
                ret.add(edge);
            }
        }

        return ret;
    }

    /**
     * Returns the size of the point list.
     * 
     * @return the size of the point list
     */
    public final int pointSize() {
        return this.pointList.size();
    }

    /**
     * Refresh the list of points, when some edges have disappeared and when
     * some points are still belonging to this but not belonging to one edge.
     */
    public final void refresh() {
        final List<Edge> edges = new ArrayList<Edge>(this.getEdgeList());
        this.clear();
        this.addAll(edges);
    }

    /**
     * Remove the occurences of e contained in this.
     * 
     * @param e
     *            the edge to remove Caution : it doesn't remove the points
     */
    public final void remove(final Edge e) {
        final List<Edge> edges = new ArrayList<Edge>(this.edgeList);
        edges.remove(e);
        this.clear();
        this.addAll(edges);
    }

    /**
     * Remove the edges of del contained in this. Caution : it doesn't remove
     * the points. This method uses the remove(Edge) method
     * 
     * @param p
     *            the list of edges to remove
     */
    public final void remove(final Polyline p) {
        final List<Edge> edges = new ArrayList<Edge>(this.edgeList);
        for (Edge e : p.edgeList) {
            edges.remove(e);
        }
        this.clear();
        this.addAll(edges);
    }

    /**
     * Return a mesh composed of the triangles formed by each edges and the
     * point centroid of the polyline. This is not really beautiful at the end,
     * but it's enough for debugging.
     * 
     * @return a mesh representing the surface of the polyline
     * @throws EmptyPolylineException
     */
    // TODO : replace by something better.
    public final Mesh returnCentroidMesh() throws EmptyPolylineException {
        if (this.isEmpty()) {
            throw new EmptyPolylineException();
        }
        final Mesh ens = new Mesh();

        final Point centroid =
            new Point(this.xAverage(), this.yAverage(), this.zAverage());
        final Vector3d normalVect = new Vector3d(0, 0, -1);

        Point before = this.pointList.get(this.pointSize() - 1);

        for (Point p : this.pointList) {
            try {
                ens.add(new Triangle(before, centroid, p, new Edge(centroid,
                    before), new Edge(before, p), new Edge(p, centroid),
                    normalVect));
            } catch (MoreThanTwoTrianglesPerEdgeException e) {
                e.printStackTrace();
            }
            before = p;
        }

        return ens;
    }

    /**
     * Returns the mesh composed all the triangles the edges belong to and which
     * belong to the mesh m. If one edge in this doesn't have one triangle to
     * return, this method returns the returnCentroidMesh()
     * 
     * @param m
     *            the mesh where all the edges are expected to belong
     * @return the mesh composed all the triangles the edges belong to
     * @throws EmptyPolylineException
     */
    public final Mesh returnExistingMesh(final Mesh m)
        throws EmptyPolylineException {

        // If there is Edges which have not triangles associated, the method
        // calls the returnCentroidMesh.
        for (Edge e : this.edgeList) {
            if (e.getNumberTriangles() == 0) {
                return this.returnCentroidMesh();
            }
        }

        // Find every triangles which belong to the edges, and which belong to m
        // too.
        final Mesh ens = new Mesh();
        for (Edge e : this.edgeList) {
            for (Triangle t : e.getTriangles()) {
                if (m.contains(t)) {
                    ens.add(t);
                }
            }
        }
        return ens;
    }

    /**
     * Setter.
     * 
     * @param normalNew
     *            the normal
     */
    public final void setNormal(final Vector3d normalNew) {
        this.normal = normalNew;
    }

    /**
     * Write the mesh returned by returnCentroidMesh.
     * 
     * @param string
     *            the name of the file to write in
     * @throws EmptyPolylineException
     */
    public final void writeCentroidMesh(final String string)
        throws EmptyPolylineException {
        this.returnCentroidMesh().writeSTL(string);
    }

    /**
     * Returns the average of the x coordinate of all the points.
     * 
     * @return the average of the x coordinate of all the points
     */
    public final double xAverage() {
        double xAverage = 0;
        for (Point p : this.pointList) {
            xAverage += p.getX();
        }
        return xAverage / this.pointList.size();
    }

    /**
     * Returns the polyline composed by the edges contained in this which the x
     * coordinates are in the bounds m1 and m2.
     * 
     * @param m1
     *            one bound
     * @param m2
     *            the other bound
     * @return the polyline composed by the edges contained in this which the x
     *         coordinates are in the bounds m1 and m2
     */
    public final Polyline xBetween(final double m1, final double m2) {
        final Polyline b = new Polyline();
        for (Edge e : this.edgeList) {
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
     * 
     * @return the average of the length on the x axis of all the edges
     */
    public final double xLengthAverage() {
        double xLengthAve = 0;
        for (Edge e : this.edgeList) {
            xLengthAve += Math.abs(e.getP1().getX() - e.getP2().getX());
        }
        return xLengthAve / (double) this.edgeList.size();
    }

    /**
     * Returns the maximum of the x coordinate.
     * 
     * @return the maximum of the x coordinate
     */
    public final double xMax() {
        double xMaxi = Double.NEGATIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getX() > xMaxi) {
                xMaxi = p.getX();
            }
        }
        return xMaxi;
    }

    /**
     * Returns the minimum of the x coordinate.
     * 
     * @return the minimum of the x coordinate
     */
    public final double xMin() {
        double xMini = Double.POSITIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getX() < xMini) {
                xMini = p.getX();
            }
        }
        return xMini;
    }

    /**
     * Returns the average of the y coordinate all of the points.
     * 
     * @return the average of the y coordinate all of the points
     */
    public final double yAverage() {
        double yAverage = 0;
        for (Point p : this.pointList) {
            yAverage += p.getY();
        }
        return yAverage / this.pointList.size();
    }

    /**
     * Returns the polyline composed by the edges contained in this which the y
     * coordinates are in the bounds m1 and m2.
     * 
     * @param m1
     *            one bound
     * @param m2
     *            the other bound
     * @return the polyline composed by the edges contained in this which the y
     *         coordinates are in the bounds m1 and m2
     */
    public final Polyline yBetween(final double m1, final double m2) {
        final Polyline b = new Polyline();
        for (Edge e : this.edgeList) {
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
     * 
     * @return the average of the length on the y axis of all the edges
     */
    public final double yLengthAverage() {
        double yLengthAve = 0;
        for (Edge e : this.edgeList) {
            yLengthAve += Math.abs(e.getP1().getY() - e.getP2().getY());
        }
        return yLengthAve / (double) this.edgeList.size();
    }

    /**
     * Returns the maximum of the y coordinate.
     * 
     * @return the maximum of the y coordinate
     */
    public final double yMax() {
        double yMaxi = Double.NEGATIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getY() > yMaxi) {
                yMaxi = p.getY();
            }
        }
        return yMaxi;
    }

    /**
     * Returns the minimum of the y coordinate.
     * 
     * @return the minimum of the y coordinate
     */
    public final double yMin() {
        double yMini = Double.POSITIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getY() < yMini) {
                yMini = p.getY();
            }
        }
        return yMini;
    }

    /**
     * Returns the average of the z coordinate all of the points.
     * 
     * @return the average of the z coordinate all of the points
     */
    public final double zAverage() {
        double zAverage = 0;
        for (Point p : this.pointList) {
            zAverage += p.getZ();
        }
        return zAverage / this.pointList.size();
    }

    /**
     * Returns the polyline composed by the edges contained in this which the z
     * coordinates are in the bounds m1 and m2.
     * 
     * @param m1
     *            one bound
     * @param m2
     *            the other bound
     * @return the polyline composed by the edges contained in this which the z
     *         coordinates are in the bounds m1 and m2
     */
    public final Polyline zBetween(final double m1, final double m2) {
        final Polyline b = new Polyline();
        for (Edge e : this.edgeList) {
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
     * 
     * @return the average of the length on the z axis of all the edges
     */
    public final double zLengthAverage() {
        double zLengthAve = 0;
        for (Edge e : this.edgeList) {
            zLengthAve += Math.abs(e.getP1().getZ() - e.getP2().getZ());
        }
        return zLengthAve / (double) this.edgeList.size();
    }

    /**
     * Returns the maximum of the z coordinate.
     * 
     * @return the maximum of the z coordinate
     */
    public final double zMax() {
        double zMaxi = Double.NEGATIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getZ() > zMaxi) {
                zMaxi = p.getZ();
            }
        }
        return zMaxi;
    }

    /**
     * Returns the point of this which has the maximum z coordinate.
     * 
     * @return the point of this which has the maximum z coordinate
     */
    public final Point zMaxPoint() {
        Point point = null;
        if (this.pointList.isEmpty()) {
            throw new InvalidParameterException("Empty border !");
        }
        double zMax = Double.NEGATIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getZ() > zMax) {
                zMax = p.getZ();
                point = p;
            }
        }
        return point;
    }

    /**
     * Returns the minimum of the z coordinate.
     * 
     * @return the minimum of the z coordinate
     */
    public final double zMin() {
        double zMini = Double.POSITIVE_INFINITY;
        for (Point p : this.pointList) {
            if (p.getZ() < zMini) {
                zMini = p.getZ();
            }
        }
        return zMini;
    }

    /**
     * Return a polyline that is the copy of this, but where all points have the
     * same z. Caution : it modifies the points, then it must be a copy of the
     * points. Otherwise, the Mesh containing these points, and using the
     * hashCode will be lost (because the hashCode of the points would have been
     * modified without the hash table of the mesh refreshed).
     * 
     * @param z
     *            the value to project on
     */
    public final void zProjection(final double z) {
        for (Point p : this.pointList) {
            p.setZ(z);
        }
    }

    /**
     * Implement a exception used when the polyline is bad formed.
     * 
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

    public static class EmptyPolylineException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public EmptyPolylineException() {
        }
    }
}
