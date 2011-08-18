package fr.nantes1900.models;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Building;
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

    public final void completeNeighbours(final List<Surface> wallList,
        final List<Surface> roofList) {

        // TODO : is this method useful ? Look at it ... Try and test on some
        // methods...

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        // Verification after the first search. If the neighbours of one surface
        // are not 2 per 2 neighbours each other, then correct it.
        for (Surface mesh : wholeList) {
            if (mesh.getNeighbours().size() == 0) {
                // FIXME : treat this case !
                System.out.println("No neighbours !");
            }
            for (Surface neighbour : mesh.getNeighbours()) {
                List<Mesh> list =
                    Building.listIntersection(mesh.getNeighbours(), neighbour
                        .getNeighbours());

                if (list.size() != 2) {
                    mesh.writeSTL("mesh.stl");
                    int counter = 0;
                    for (Mesh m : mesh.getNeighbours()) {
                        m.writeSTL("file" + counter + ".stl");
                        ++counter;
                    }
                    System.exit(1);
                }
            }

        }
    }

    /**
     * Find the edges of a mesh (which is a surface of a building : wall or
     * roof).
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
     *         perfectly fit to its neighbours.
     * @throws InvalidSurfaceException
     *             if anything bad happens in the algorithm
     */
    // TODO : comment !
    public final Polyline findEdges(final List<Surface> wallList,
        final Map<Point, Point> pointMap, final Map<Edge, Edge> edgeMap,
        final Vector3d normalFloor) throws InvalidSurfaceException {

        final Polyline edges = new Polyline();

        for (Surface m2 : this.neighbours) {
            final List<Point> points = new ArrayList<Point>();

            try {
                for (Surface m3 : this.neighbours) {

                    if (m2.neighbours.contains(m3)) {

                        Mesh plane1 = this;
                        Mesh plane2 = m2;
                        Mesh plane3 = m3;

                        // If this, or m2, or m3 is a wall, then treat it as a
                        // vertical plane first, and after find the edges.
                        if (wallList.contains(this)) {
                            plane1 = this.returnVerticalPlane(normalFloor);
                        }
                        if (wallList.contains(m2)) {
                            plane2 = m2.returnVerticalPlane(normalFloor);
                        }
                        if (wallList.contains(m3)) {
                            plane3 = m3.returnVerticalPlane(normalFloor);
                        }

                        // If there is two neighbours same-oriented, then
                        // don't try to cumpute the intersection of the two
                        // planes, and don't compute the edge.
                        final double isOrientedFactor = 10;

                        if (this.isOrientedAs(plane2, isOrientedFactor)) {
                            throw new ParallelPlanesException();
                        }
                        if (this.isOrientedAs(plane3, isOrientedFactor)) {
                            throw new ParallelPlanesException();
                        }
                        if (plane2.isOrientedAs(plane3, isOrientedFactor)) {
                            throw new ParallelPlanesException();
                        }

                        try {
                            Point p = plane1.intersection(plane2, plane3);

                            final Point mapP = pointMap.get(p);
                            if (mapP == null) {
                                pointMap.put(p, p);
                            } else {
                                p = mapP;
                            }

                            points.add(p);

                        } catch (SingularMatrixException e1) {
                            System.out.println("Singular matrix !");
                            throw new InvalidSurfaceException();
                        }
                    }
                }
            } catch (ParallelPlanesException e) {
                // this.writeSTL("Files/mesh.stl");
                // int counter = 0;
                // for (Mesh m : this.getNeighbours()) {
                // m.writeSTL("Files/error" + counter + ".stl");
                // ++counter;
                // }
                System.out.println("Parallel planes !");
                // System.exit(1);
                throw new InvalidSurfaceException();
            }

            if (points.size() == 2) {

                Edge e = new Edge(points.get(0), points.get(1));

                final Edge mapE = edgeMap.get(e);
                if (mapE == null) {
                    edgeMap.put(e, e);
                } else {
                    e = mapE;
                }

                edges.add(e);
            }
        }

        if (edges.edgeSize() == this.getNeighbours().size()) {
            edges.setNormal(this.averageNormal());
            edges.order();
            return edges;
        } else {
            System.out.println(edges.edgeSize() + " on "
                + this.getNeighbours().size());
            // this.writeSTL("mesh.stl");
            // int counter = 0;
            // for (Mesh m : this.getNeighbours()) {
            // m.writeSTL("file" + counter + ".stl");
            // ++counter;
            // }
            // System.exit(1);
            edges.setNormal(this.averageNormal());
            if (edges.canBeOrdered()) {
                edges.order();
                // } else {
                // edges.orderAsYouCan();
            }
            return edges;

            // System.out.println("Else !");
            // throw new InvalidSurfaceException();
        }
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
     * Returns the meshes belonging to contain which are neighbours of this in
     * the list ret. Recursive method.
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
