package fr.nantes1900.models.extended;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.coefficients.SimplificationSurfaces;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

/**
 * Implements a surface, extending a mesh, and containing a list of surfaces as
 * neighbours. Contains some algorithms for the separation between walls and
 * roofs.
 * @author Daniel Lefevre
 */
public class Surface {

    /**
     * ID counter.
     */
    private static int counterID = 0;

    /**
     * The String to display in a node to represent the object.
     */
    private String nodeString;

    /**
     * ID of the object.
     */
    private int iD = Surface.counterID++;

    /**
     * List of the neighbours of this surface.
     */
    private List<Surface> neighbours = new ArrayList<>();

    /**
     * The polygon representing this surface (after simplification).
     */
    private Polygon polygon;

    /**
     * The mesh representing this surface (before simplification).
     */
    private Mesh mesh;

    /**
     * Empty constructor.
     */
    public Surface() {
        this.mesh = new Mesh();
    }

    /**
     * Constructor from a Mesh.
     * @param m
     *            the mesh to build the surface
     */
    public Surface(final Mesh m) {
        this.setMesh(m);
    }

    /**
     * Constructor from a Polygon.
     * @param p
     *            the polygon to build the surface
     */
    public Surface(final Polygon p) {
        this.setPolygon(p);
    }

    /**
     * Copy constructor.
     * @param surface
     *            the surface to copy.
     */
    public Surface(final Surface surface) {
        this.mesh = surface.mesh;
        this.neighbours = surface.neighbours;
        this.polygon = surface.polygon;
    }

    /**
     * Adds a neighbour to the attribute neighbours. Checks if it is not
     * contained. Adds also this to the m neighbours, also checking if not
     * already contained.
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
     * With four planes (the three in parameters plus this), builds an edge.
     * Computes the intersection of the first three planes, and the three next.
     * If one plane is wall, rectifies its normal to be vertical. If one point
     * has already been created before, use the hashmap to find it.
     * @param s1
     *            the first plane
     * @param s2
     *            the second plane
     * @param s3
     *            the third plane
     * @param pointMap
     *            the map of existing points
     * @param wallList
     *            the list of the walls
     * @param normalGround
     *            the normal to the ground
     * @return the edge created by these four planes
     * @throws InvalidSurfaceException
     *             if the algorithm cannot comput the edge
     */
    private Edge createEdge(final Surface s1, final Surface s2,
            final Surface s3, final Map<Point, Point> pointMap,
            final List<Wall> wallList, final Vector3d normalGround)
            throws InvalidSurfaceException {
        final List<Surface> surfaces = new ArrayList<>();
        surfaces.add(s1);
        surfaces.add(s2);
        surfaces.add(s3);
        surfaces.add(this);

        try {
            // If there is two neighbours which have the same orientation, then
            // throw an exception.
            if (this.getMesh().isOrientedAs(s1.getMesh(),
                    SimplificationSurfaces.getIsOrientedFactor())
                    || this.getMesh().isOrientedAs(s2.getMesh(),
                            SimplificationSurfaces.getIsOrientedFactor())
                    || this.getMesh().isOrientedAs(s3.getMesh(),
                            SimplificationSurfaces.getIsOrientedFactor())) {
                throw new ParallelPlanesException();
            }
            if (s1.getMesh().isOrientedAs(s2.getMesh(),
                    SimplificationSurfaces.getIsOrientedFactor())
                    || s2.getMesh().isOrientedAs(s3.getMesh(),
                            SimplificationSurfaces.getIsOrientedFactor())) {
                throw new ParallelPlanesException();
            }

            // If one of the surfaces is a wall, rectifies its normal to be
            // vertical, and after finds the edges.
            final List<Surface> list = new ArrayList<>();
            for (final Surface s : surfaces) {
                if (s != this) {
                    if (wallList.contains(s)) {
                        list.add(s.returnVerticalPlane(normalGround));
                    } else {
                        list.add(s);
                    }
                }
            }

            Surface surface = this;
            if (wallList.contains(this)) {
                surface = this.returnVerticalPlane(normalGround);
            }

            // Finds the intersection of the three surfaces.
            Point p1 = surface.getMesh().intersection(list.get(0).getMesh(),
                    list.get(1).getMesh());
            Point p2 = surface.getMesh().intersection(list.get(1).getMesh(),
                    list.get(2).getMesh());

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

            final Edge e = new Edge(p1, p2);

            return e;

        } catch (final SingularMatrixException e) {
            throw new InvalidSurfaceException();
        } catch (final ParallelPlanesException e) {
            throw new InvalidSurfaceException();
        }
    }

    /**
     * Finds the edges of a surface. Caution : this method needs the neighbours
     * to be treated first (using orderNeighbours). Since the neighbours are
     * sorted, calls the method createEdge for each neighbours and builds the
     * polyline with the edges returned.
     * @param wallList
     *            the list of walls to check if the surface is a wall or not
     * @param pointMap
     *            the map of points
     * @param normalGround
     *            the normal to the ground
     * @return a polyline made from all the edges of this surface, and which
     *         perfectly fits to its neighbours.
     * @throws InvalidSurfaceException
     *             if a problem happened
     */
    public final Polygon findEdges(final List<Wall> wallList,
            final Map<Point, Point> pointMap, final Vector3d normalGround)
            throws InvalidSurfaceException {

        final Polygon edges = new Polygon();

        // The neighbours are sorted, then it's easy to make the edges and
        // points.
        for (int i = 0; i < this.getNeighbours().size() - 2; ++i) {

            edges.add(this.createEdge(this.getNeighbours().get(i), this
                    .getNeighbours().get(i + 1), this.getNeighbours()
                    .get(i + 2), pointMap, wallList, normalGround));
        }

        final int size = this.getNeighbours().size();

        try {
            // We add the last missing edges which where not treated in the
            // loop.
            edges.add(this.createEdge(this.getNeighbours().get(size - 2), this
                    .getNeighbours().get(size - 1),
                    this.getNeighbours().get(0), pointMap, wallList,
                    normalGround));

            edges.add(this.createEdge(this.getNeighbours().get(size - 1), this
                    .getNeighbours().get(0), this.getNeighbours().get(1),
                    pointMap, wallList, normalGround));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidSurfaceException();
        }

        edges.setNormal(this.getMesh().averageNormal());

        return edges;
    }

    /**
     * Finds in the list of neighbours of this except the list of surfaces the
     * closest to the surface.
     * @param current
     *            the surface to check
     * @param neighboursOrdered
     *            the list of surfaces not to search
     * @return the closest surface of current belonging to the neighbours of
     *         this, not belonging to the list neighboursOrdered
     */
    private Surface findPossibleNeighbour(final Surface current,
            final List<Surface> neighboursOrdered) {
        Surface possible = null;
        double distanceMin = Double.POSITIVE_INFINITY;

        // From all the neighbours which are not still ordered, select the
        // closest one.
        for (final Surface s : this.getNeighbours()) {
            if (!neighboursOrdered.contains(s)
                    && !current.getNeighbours().contains(s)) {
                if (current.getMesh().minimalDistance(s.getMesh()) < distanceMin) {
                    possible = s;
                    distanceMin = current.getMesh()
                            .minimalDistance(s.getMesh());
                }
            }
        }

        return possible;
    }

    /**
     * Returns the intersection of two lists of neighbours.
     * @param surface
     *            the other surface
     * @return a list containing the elements shared by the two lists
     */
    private List<Surface> getCommonNeighbours(final Surface surface) {
        final List<Surface> ret = new ArrayList<>();
        for (final Surface s : this.getNeighbours()) {
            if (surface.getNeighbours().contains(s)) {
                ret.add(s);
            }
        }
        return ret;
    }

    /**
     * Getter.
     * @return the ID of the object
     */
    public final int getID() {
        return this.iD;
    }

    /**
     * Getter.
     * @return the lesh contained in this
     */
    public final Mesh getMesh() {
        return this.mesh;
    }

    /**
     * Getter.
     * @return the neighbours of this surface
     */
    public final List<Surface> getNeighbours() {
        return this.neighbours;
    }

    /**
     * Getter.
     * @return the string displayed in a node to represent this surface
     */
    public final String getNodeString() {
        return this.nodeString;
    }

    /**
     * Getter.
     * @return the polygon contained in this
     */
    public final Polygon getPolygon() {
        return this.polygon;
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

        final List<Surface> neighboursOrdered = new ArrayList<>();

        final int neighboursNumber = this.getNeighbours().size();

        // If the ground is the neighbour of this surface, then we begin with
        // the ground, to avoid some problems in the future. Otherwise, we
        // begin where we want.
        Surface current = null;
        try {
            current = this.getNeighbours().get(0);
            if (this.getNeighbours().contains(grounds)) {
                current = this.getCommonNeighbours(grounds).get(0);
            }
        } catch (final IndexOutOfBoundsException e) {
            throw new ImpossibleNeighboursOrderException();
        }

        // We find the surfaces that are common to this surface and current.
        // There must be two surfaces, but there always are some problems...
        List<Surface> commonNeighbours = this.getCommonNeighbours(current);
        int counter = 0;

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

            // If there is too much neighbours, this step can resolve it.
            // The ground often causes that kind of problem, that's why we begin
            // by treating the ground first (see at the beginning of the
            // method).
            for (final Surface s : neighboursOrdered) {
                commonNeighbours.remove(s);
            }
            if (commonNeighbours.isEmpty()) {
                throw new ImpossibleNeighboursOrderException();
            }

            // Selects the next one (without retracing our steps).
            current = commonNeighbours.get(0);

            // Continues with this one.
            commonNeighbours = this.getCommonNeighbours(current);
            ++counter;
        }

        neighboursOrdered.add(current);

        // If the last one is not neighbour to the first one, there is a
        // problem.
        if (!current.getNeighbours().contains(neighboursOrdered.get(0))) {
            throw new ImpossibleNeighboursOrderException();
        }

        this.neighbours.clear();
        this.neighbours.addAll(neighboursOrdered);
    }

    /**
     * Returns the meshes belonging to the list contain which are neighbours of
     * this in the list ret. Recursive method.
     * @param ret
     *            the list returned which contains the neighbours
     * @param contain
     *            the list which must contain the neighbours
     */
    public final void returnNeighbours(final List<Surface> ret,
            final List<Surface> contain) {

        // Add this to the ret list.
        ret.add(this);

        // If this is not contained by ret and if this is contained in
        // contain,
        // then call this method.
        for (final Surface m : this.getNeighbours()) {
            if (!ret.contains(m) && contain.contains(m)) {
                m.returnNeighbours(ret, contain);
            }
        }
    }

    /**
     * Returns a node containing this surface.
     * @return the node
     */
    public final DefaultMutableTreeNode returnNode() {
        return new DefaultMutableTreeNode(this);
    }

    /**
     * Returns a mesh composed of only one triangle which represents a plane in
     * 3D space.
     * @param normalGround
     *            the normal to the ground
     * @return the mesh with only one triangle
     */
    public final Surface returnVerticalPlane(final Vector3d normalGround) {

        final Vector3d averageNormal = this.getMesh().averageNormal();

        final Surface computedWallPlane = new Surface();

        final Point centroid = new Point(this.getMesh().xAverage(), this
                .getMesh().yAverage(), this.getMesh().zAverage());

        final Point p1 = new Point(centroid.getX() + 1, centroid.getY()
                - averageNormal.getX() / averageNormal.getY(), centroid.getZ());

        final Point p3 = centroid;

        final Edge e1 = new Edge(p1, p1);
        final Edge e2 = new Edge(p1, p3);

        final Vector3d vect = new Vector3d();
        vect.cross(normalGround, e2.convertToVector3d());

        computedWallPlane.getMesh().add(
                new Triangle(p1, p1, p3, e1, e2, e2, vect));

        return computedWallPlane;
    }

    /**
     * Setter.
     * @param meshIn
     *            the new mesh
     */
    public final void setMesh(final Mesh meshIn) {
        this.mesh = meshIn;
    }

    /**
     * Setter.
     * @param newNeighbours
     *            the new list of neighbours
     */
    public final void setNeighbours(final List<Surface> newNeighbours) {
        this.neighbours = newNeighbours;
    }

    /**
     * Setter.
     * @param nodeStringIn
     *            the string displayed in a node to represent this surface
     */
    public final void setNodeString(final String nodeStringIn) {
        this.nodeString = nodeStringIn;
    }

    /**
     * Setter.
     * @param polygoneIn
     *            the new polygon
     */
    public final void setPolygon(final Polygon polygoneIn) {
        this.polygon = polygoneIn;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.nodeString;
    }

    /**
     * Implements an exception used in algorithms when the neighbours cannot be
     * ordered.
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
        public ImpossibleNeighboursOrderException() {
        }
    }

    /**
     * Implements an execption used in findEdges method when surfaces can not be
     * vectorized.
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
        public InvalidSurfaceException() {
        }
    }

    /**
     * Implements an exception used in algorithms when planes are parallel.
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
        public ParallelPlanesException() {
        }
    }
}
