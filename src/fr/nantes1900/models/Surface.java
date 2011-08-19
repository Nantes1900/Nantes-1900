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
 * @author Daniel Lefevre
 */
public class Surface extends Mesh {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * List of the neighbours of this surface. Used in the algorithms of
     * separation between walls and roofs.
     */
    private List<Surface> neighbours = new ArrayList<Surface>();

    /**
     * Void constructor.
     */
    public Surface() {
        super();
    }

    public Surface(final Collection<? extends Triangle> c) {
        super(c);
    }

    /**
     * Add a neighbour to the attribute neighbours. Check if it is not
     * contained. Add also this to the m neighbours, also checking if not
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
     * Finds the edges of a surface. Create these edges by calculating the
     * intersection of its neighbours two by two.
     * 
     * @param wallList
     *            the list of walls to check if the surface is a wall or not
     * @param pointMap
     *            the map of points
     * @param edgeMap
     *            the map of edges
     * @param normalFloor
     *            the normal to the floor
     * @return a polyline made from all the edges of this surface, and which
     *         perfectly fits to its neighbours.
     * @throws InvalidSurfaceException
     *             if a surface has not been treated
     */
    // FIXME : needs the neighbours to be sorted before...
    public final Polyline findEdges(final List<Surface> wallList,
        final Map<Point, Point> pointMap, final Map<Edge, Edge> edgeMap,
        final Vector3d normalFloor) throws InvalidSurfaceException {

        final Polyline edges = new Polyline();

        // FIXME : treat walls to have them verticals !

        // The neighbours are sorted, then it's easy to make the edges and
        // points.
        for (int i = 0; i < this.getNeighbours().size() - 2; i++) {
            edges.add(this.createEdge(this.getNeighbours().get(i), this
                .getNeighbours().get(i + 1), this.getNeighbours().get(i + 2),
                pointMap, edgeMap, wallList, normalFloor));
        }

        final int size = this.getNeighbours().size();

        // We add the last missing edges which where not treated in the loop.
        try {
            edges.add(this.createEdge(this.getNeighbours().get(size - 2), this
                .getNeighbours().get(size - 1), this.getNeighbours().get(0),
                pointMap, edgeMap, wallList, normalFloor));

            edges.add(this.createEdge(this.getNeighbours().get(size - 1), this
                .getNeighbours().get(0), this.getNeighbours().get(1), pointMap,
                edgeMap, wallList, normalFloor));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(this.getNeighbours().size());
            System.out.println(size);
        }

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

    // FIXME : treat the case where the surface has no or one or two
    // neighbours...

    /**
     * @param wallList
     * @param roofList
     * @throws MissingNeighbourException
     */
    public final void orderMyNeighbour(final List<Surface> wholeList,
        final Surface floors) throws MissingNeighbourException {

        final int neighboursNumber = this.getNeighbours().size();
        int counter = 0;

        final List<Surface> neighboursOrdered = new ArrayList<Surface>();

        Surface current = this.getNeighbours().get(0);

        List<Surface> commonNeighbours = this.getCommonNeighbours(current);

        while (counter < neighboursNumber - 1) {

            // Adds the current surface to the ordered list.
            neighboursOrdered.add(current);

            // Resolving problems : not enough neighbours or too much...
            if (commonNeighbours.size() < 2) {

                // If there is not enough, it probably means that two surfaces
                // which effectively are neighbours don't touch each other. We
                // will find which one our current surface is the closest to,
                // and it will be our next neighbour !
                commonNeighbours.add(this.findPossibleNeighbour(current,
                    neighboursOrdered));

            } else if (commonNeighbours.size() > 2) {

                // If there is too much neighbour, it probably means that two of
                // these neighbours touch themselves at another point that on
                // this surface.
                // It is often the floor... :(
                if (commonNeighbours.contains(floors)) {

                    // If the floors is a neighbour, then it can be a third
                    // common
                    // neighbour...
                    // FIXME : look if the solution found up can be applied
                    // here...
                    commonNeighbours.remove(floors);

                } else {
                    // System.out.println("More than 3 neighbours problem !");
                    // if (commonNeighbours.size() == 4) {
                    // FIXME : enlever ce bazar et traiter le cas !
                    // this.writeSTL("files/St-Similien/m02/results/mesh.stl");
                    // int counter1 = 0;
                    // for (Surface s : this.getNeighbours()) {
                    // s.writeSTL("files/St-Similien/m02/results/neigh"
                    // + counter1 + ".stl");
                    // counter1++;
                    // }
                    // counter1 = 0;
                    // for (Surface s : commonNeighbours) {
                    // s.writeSTL("files/St-Similien/m02/results/common"
                    // + counter1 + ".stl");
                    // counter1++;
                    // }
                    // current
                    // .writeSTL("files/St-Similien/m02/results/current.stl");
                    // System.exit(1);
                    // }
                    throw new MissingNeighbourException();
                }
            }

            // Takes the next one. If this one has already been treated, take
            // another one, not to retrace our steps.
            int i = 0;
            while (neighboursOrdered.contains(commonNeighbours.get(i))) {
                ++i;
                if (i == commonNeighbours.size()) {
                    // FIXME
                    System.out.println("Impossible problem !");
                    throw new MissingNeighbourException();
                }
            }

            // Selects this one.
            current = commonNeighbours.get(i);

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

    private final Edge createEdge(final Surface s1, final Surface s2,
        final Surface s3, final Map<Point, Point> pointMap,
        final Map<Edge, Edge> edgeMap, final List<Surface> wallList,
        final Vector3d normalFloor) throws InvalidSurfaceException {

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
            for (Surface surface1 : surfaces) {
                for (Surface surface2 : surfaces) {
                    if (surface1 != surface2) {
                        if (surface1.isOrientedAs(surface2, isOrientedFactor)) {
                            throw new ParallelPlanesException();
                        }
                    }
                }
            }

            // If one of the surfaces is a wall then rectifies its normal to be
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

            // Find the intersection of the three surfaces.
            Point p1 = mesh.intersection(list.get(0), list.get(1));
            Point p2 = mesh.intersection(list.get(1), list.get(2));

            // We search in the map to find if another point with the same value
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
            throw new InvalidSurfaceException();
        } catch (ParallelPlanesException e) {
            // System.out.println("Parallel planes !");
            throw new InvalidSurfaceException();
        }
    }

    /**
     * @param current
     * @param wholeList
     * @return
     */
    // FIXME : improve the speed...
    private Surface findPossibleNeighbour(final Surface current,
        final List<Surface> neighboursOrdered) {

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
     * Implements an exception used in algorithms when one neigbhour is missing.
     * 
     * @author Daniel Lefevre
     */
    public static final class MissingNeighbourException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private MissingNeighbourException() {
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
