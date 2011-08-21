package fr.nantes1900.models;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

/**
 * Implements a surface, extending a mesh, and containing a list of surfaces as
 * neighbours. Contains some algorithms for the separation between walls and
 * roofs.
 * 
 * @author Daniel Lefevre
 */
public class Surface extends Mesh {

    /**
     * Version attribute.
     */
    private static final long serialVersionUID = 1L;

    /**
     * List of the neighbours of this surface.
     */
    private List<Surface> neighbours = new ArrayList<Surface>();

    /**
     * Void constructor.
     */
    public Surface() {
        super();
    }

    /**
     * Constructor from a collection of triangles. See the HashSet constructor.
     * 
     * @param c
     *            a collection of triangles
     */
    public Surface(final Collection<? extends Triangle> c) {
        super(c);
    }

    /**
     * Adds a neighbour to the attribute neighbours. Checks if it is not
     * contained. Adds also this to the m neighbours, also checking if not
     * already contained.
     * 
     * @param m
     *            the mesh as neighbour to add
     */
    public final void addNeighbour(final Surface m) {
        if (!this.neighbours.contains(m)) {
            this.neighbours.add(m);
        }
        if (!m.neighbours.contains(this)) {
            m.neighbours.add(this);
        }
    }

    /**
     * Finds the edges of a surface. Caution : this method needs the neighbours
     * to be treated first (using orderNeighbours). Since the neighbours are
     * sorted, calls the method createEdge for each neighbours and builds the
     * polyline with the edges returned.
     * 
     * @param wallList
     *            the list of walls to check if the surface is a wall or not
     * @param pointMap
     *            the map of points
     * @param edgeMap
     *            the map of edges
     * @param normalFloor
     *            the normal to the ground
     * @return a polyline made from all the edges of this surface, and which
     *         perfectly fits to its neighbours.
     * @throws InvalidSurfaceException
     *             if a problem happened
     */
    public final Polyline findEdges(final List<Surface> wallList,
        final Map<Point, Point> pointMap, final Map<Edge, Edge> edgeMap,
        final Vector3d normalFloor) throws InvalidSurfaceException {

        final Polyline edges = new Polyline();

        // The neighbours are sorted, then it's easy to make the edges and
        // points.
        for (int i = 0; i < this.getNeighbours().size() - 2; i++) {
            // try {
            edges.add(this.createEdge(this.getNeighbours().get(i), this
                .getNeighbours().get(i + 1), this.getNeighbours().get(i + 2),
                pointMap, edgeMap, wallList, normalFloor));
            // } catch (BadNeighbourException e) {
            // this.getNeighbours().remove(e.getNeighbourError());
            // }
        }

        final int size = this.getNeighbours().size();

        // We add the last missing edges which where not treated in the loop.
        // try {
        edges.add(this.createEdge(this.getNeighbours().get(size - 2), this
            .getNeighbours().get(size - 1), this.getNeighbours().get(0),
            pointMap, edgeMap, wallList, normalFloor));
        // } catch (BadNeighbourException e) {
        // // If this error happens, we don't add the edge. FIXME.
        // }

        // try {
        edges.add(this.createEdge(this.getNeighbours().get(size - 1), this
            .getNeighbours().get(0), this.getNeighbours().get(1), pointMap,
            edgeMap, wallList, normalFloor));
        // } catch (BadNeighbourException e) {
        // // If this error happens, we don't add the edge. FIXME.
        // }

        return edges;
    }

    /**
     * Getter.
     * 
     * @return the neighbours
     */
    public final List<Surface> getNeighbours() {
        return this.neighbours;
    }

    /**
     * Orders the neighbours of this surface. The list of neighbours of this
     * surface will then be sorted such as two surfaces neighbours in the list
     * are neigbhours in the mesh each other. The algorithm searches for each
     * neighbour N the two neighbours that N shares with this surface. Then, it
     * goes on the not-still-treated direction. There can be problems such as :
     * there is less that two common neighbours. Then the algorithm selects as
     * neighbour the closest (in terms of distance) neighbour not still treated.
     * If there is too much common neighbours, then the algorithm removes the
     * still-treated neighbours. It often resolves the problem.
     * 
     * @param wholeList
     *            the list of every surfaces
     * @param grounds
     *            the grounds
     * @throws ImpossibleNeighboursOrderException
     *             if a problem happens in the algorithm that can not be treated
     *             as said in the description
     */
    public final void orderNeighbours(final List<Surface> wholeList,
        final Surface grounds) throws ImpossibleNeighboursOrderException {

        final List<Surface> neighboursOrdered = new ArrayList<Surface>();

        final int neighboursNumber = this.getNeighbours().size();
        int counter = 0;

        // If the ground is the neighbour of this surface, then we begin with the
        // ground, to avoid some problems in the future. Otherwise, we begin
        // where we want.
        Surface current = this.getNeighbours().get(0);
        if (this.getNeighbours().contains(grounds)) {

            try {
                current = this.getCommonNeighbours(grounds).get(0);
            } catch (IndexOutOfBoundsException e) {
                throw new ImpossibleNeighboursOrderException();
            }
        }

        // We find the surfaces that are common to this surface and current.
        // There must be two surfaces, but there always are some problems...
        List<Surface> commonNeighbours = this.getCommonNeighbours(current);

        while (counter < neighboursNumber - 1) {

            // Adds the current surface to the ordered list.
            neighboursOrdered.add(current);

            // Resolving problems : not enough common neighbours or too much...
            if (commonNeighbours.size() < 2) {

                // If there is not enough, it probably means that two surfaces
                // are effectively neighbours, but don't touch each other.
                // We will find which one our current surface is the closest to,
                // and it will be our next neighbour !
                commonNeighbours.add(this.findPossibleNeighbour(current,
                    neighboursOrdered));

            }

            // Selects the next one. If this one has already been treated, take
            // another one, not to retrace our steps.

            // If there is too much neighbours, this step can resolve it.
            // The ground often causes that kind of problem, that's why we begin
            // by treating the ground first (see at the beginning of the method).
            for (Surface s : neighboursOrdered) {
                commonNeighbours.remove(s);
            }
            if (commonNeighbours.isEmpty()) {
                throw new ImpossibleNeighboursOrderException();
            }

            current = commonNeighbours.get(0);

            // Continues with this one.
            commonNeighbours = this.getCommonNeighbours(current);
            ++counter;
        }

        neighboursOrdered.add(current);

        this.neighbours.clear();
        this.neighbours.addAll(neighboursOrdered);
    }

    /**
     * Returns the meshes belonging to the list contain which are neighbours of
     * this in the list ret. Recursive method.
     * 
     * @param ret
     *            the list returned which contains the neighbours
     * @param contain
     *            the list which must contain the neighbours
     */
    public final void returnNeighbours(final List<Surface> ret,
        final List<Surface> contain) {

        // Add this to the ret list.
        ret.add(this);

        // If this is not contained by ret and if this is contained in contain,
        // then call this method.
        for (Surface m : this.getNeighbours()) {
            if (!ret.contains(m) && contain.contains(m)) {
                m.returnNeighbours(ret, contain);
            }
        }
    }

    /**
     * With four planes (the three in parameters plus this), builds an edge.
     * Computes the intersection of the first three planes, and the three next.
     * If one plane is wall, rectifies its normal to be vertical. If one point
     * or one edge has already been created before, use the two hashmaps to find
     * it.
     * 
     * @param s1
     *            the first plane
     * @param s2
     *            the second plane
     * @param s3
     *            the third plane
     * @param pointMap
     *            the map of existing points
     * @param edgeMap
     *            the map of existing edges
     * @param wallList
     *            the list of the walls
     * @param normalFloor
     *            the normal to the ground
     * @return the edge created by these four planes
     * @throws InvalidSurfaceException
     *             if the algorithm cannot comput the edge
     */
    private final Edge createEdge(final Surface s1, final Surface s2,
        final Surface s3, final Map<Point, Point> pointMap,
        final Map<Edge, Edge> edgeMap, final List<Surface> wallList,
        final Vector3d normalFloor) throws InvalidSurfaceException {

        // LOOK : maybe remove that list : it is not really useful.
        final List<Surface> surfaces = new ArrayList<Surface>();
        surfaces.add(s1);
        surfaces.add(s2);
        surfaces.add(s3);
        surfaces.add(this);

        try {
            // LOOK : put that factor in the constants package.
            final double isOrientedFactor = 5;

            // If there is two neighbours which have the same orientation, then
            // throw an exception.
            // FIXME : try to improve that mess ?
            if (this.isOrientedAs(s1, isOrientedFactor)) {
                throw new ParallelPlanesException();
            }
            if (this.isOrientedAs(s2, isOrientedFactor)) {
                throw new ParallelPlanesException();
            }
            if (this.isOrientedAs(s3, isOrientedFactor)) {
                throw new ParallelPlanesException();
            }
            if (s1.isOrientedAs(s2, isOrientedFactor)) {
                throw new ParallelPlanesException();
            }
            if (s2.isOrientedAs(s3, isOrientedFactor)) {
                throw new ParallelPlanesException();
            }

            // If one of the surfaces is a wall, rectifies its normal to be
            // vertical, and after finds the edges.
            final List<Mesh> list = new ArrayList<Mesh>();
            for (Surface s : surfaces) {
                if (s != this) {
                    if (wallList.contains(s)) {
                        list.add(s.returnVerticalPlane(normalFloor));
                    } else {
                        list.add(s);
                    }
                }
            }

            Mesh mesh = this;
            if (wallList.contains(this)) {
                mesh = this.returnVerticalPlane(normalFloor);
            }

            // Finds the intersection of the three surfaces.
            Point p1 = mesh.intersection(list.get(0), list.get(1));
            Point p2 = mesh.intersection(list.get(1), list.get(2));

            // Searches in the map to find if another point with the same value
            // doesn't already exist.
            Point pTemp = pointMap.get(p1);
            if (pTemp == null) {
                pointMap.put(p1, p1);
            } else {
                p1 = pTemp;
            }

            // Idem.
            pTemp = pointMap.get(p2);
            if (pTemp == null) {
                pointMap.put(p2, p2);
            } else {
                p2 = pTemp;
            }

            Edge e = new Edge(p1, p2);

            // Idem with the map of edges.
            final Edge eTemp = edgeMap.get(e);
            if (eTemp == null) {
                edgeMap.put(e, e);
            } else {
                e = eTemp;
            }

            return e;

        } catch (SingularMatrixException e) {
            // System.out.println("Singular matrix !");
            // FIXME : try this new exception...
            // throw new BadNeighbourException();
            throw new InvalidSurfaceException();
        } catch (ParallelPlanesException e) {
            throw new InvalidSurfaceException();
        }
    }

    /**
     * Finds in the list of neighbours of this except the list of surfaces the
     * closest to the surface.
     * 
     * @param current
     *            the surface to check
     * @param neighboursOrdered
     *            the list of surfaces not to search
     * @return the closest surface of current belonging to the neighbours of
     *         this, not belonging to the list neighboursOrdered
     */
    // FIXME : refactor this method...
    private Surface findPossibleNeighbour(final Surface current,
        final List<Surface> neighboursOrdered) {
        // FIXME : improve the speed...
        Surface possible = null;
        double distanceMin = Double.POSITIVE_INFINITY;

        // From all the neighbours which are not still ordered, select the
        // closest one.
        for (Surface s : this.getNeighbours()) {
            if (!neighboursOrdered.contains(s)) {
                if (current.minimalDistance(s) < distanceMin) {
                    possible = s;
                    distanceMin = current.minimalDistance(s);
                }
            }
        }

        return possible;
    }

    /**
     * Returns the intersection of two lists of neighbours.
     * 
     * @param list
     *            the first list of meshes
     * @param list2
     *            the second list of meshes
     * @return a list containing the elements shared by the two lists
     */
    private List<Surface> getCommonNeighbours(final Surface surface) {
        final List<Surface> ret = new ArrayList<Surface>();
        for (Surface s : this.getNeighbours()) {
            if (surface.getNeighbours().contains(s)) {
                ret.add(s);
            }
        }
        return ret;
    }

    /**
     * Implements an execption used in findEdges method when surfaces can not be
     * vectorized.
     * 
     * @author Daniel Lefevre
     */
    public static final class InvalidSurfaceException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private InvalidSurfaceException() {
        }
    }

    /**
     * Implements an exception used in algorithms when the neighbours cannot be
     * ordered.
     * 
     * @author Daniel Lefevre
     */
    public static final class ImpossibleNeighboursOrderException extends
        Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private ImpossibleNeighboursOrderException() {
        }
    }

    /**
     * Implements an exception used in algorithms when a neighbour provoques
     * errors and must be removed of the list of neighbours. Used when a
     * SingularMatrixException happens. Contains the neighbours surface which
     * caused the error.
     * 
     * @author Daniel Lefevre
     */
    public static final class BadNeighbourException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The neighbour which caused the error.
         */
        private Surface errorNeighbour;

        /**
         * Private constructor.
         * 
         * @param s
         *            the neighbour which caused the error.
         */
        private BadNeighbourException(Surface s) {
            this.errorNeighbour = s;
        }

        /**
         * Returns the neighbour which caused the error.
         * 
         * @return one of the neighbour
         */
        public Surface getNeighbourError() {
            return this.errorNeighbour;
        }
    }

    /**
     * Implements an exception used in algorithms when planes are parallel.
     * 
     * @author Daniel Lefevre
     */
    public static final class ParallelPlanesException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private ParallelPlanesException() {
        }
    }
}
