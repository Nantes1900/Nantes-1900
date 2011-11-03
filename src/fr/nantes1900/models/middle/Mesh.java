package fr.nantes1900.models.middle;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;
import fr.nantes1900.utils.WriterSTL;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Vector3d;

/**
 * Implements a mesh as extending a HashSet of Triangle.
 * 
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Mesh extends HashSet<Triangle> {

    /**
     * Static integer to create new ID objects.
     */
    private static int currentID;

    /**
     * Version attribute.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Object ID.
     */
    private final int iD;

    /**
     * Void constructor.
     */
    public Mesh() {
	super();
	this.iD = ++Mesh.currentID;
    }

    /**
     * Constructor from a collection of triangle.
     * 
     * @param c
     *            the collection
     */
    public Mesh(final Collection<? extends Triangle> c) {
	super(c);
	this.iD = ++Mesh.currentID;
    }

    /**
     * Computes the average normal of all triangles composing this mesh.
     * 
     * @return average The average Vector3d normal.
     */
    public final Vector3d averageNormal() {
	final int n = this.size();
	final Vector3d average = new Vector3d();
	for (final Triangle face : this) {
	    average.add(face.getNormal());
	}
	average.scale(1 / (double) n);

	return average;
    }

    /**
     * Changes the base of all the points contained in the mesh.
     * 
     * @param matrix
     *            the change base matrix
     */
    public final void changeBase(final double[][] matrix) {
	if (matrix == null) {
	    throw new InvalidParameterException();
	}

	final Set<Point> set = new HashSet<Point>();
	final Set<Triangle> mesh = new HashSet<Triangle>();

	// Make a list of all the points, and base change them.
	for (final Triangle f : this) {
	    set.addAll(f.getPoints());
	    MatrixMethod.changeBase(f.getNormal(), matrix);
	    mesh.add(f);
	}

	for (final Point p : set) {
	    p.changeBase(matrix);
	}

	this.clear();
	this.addAll(mesh);
    }

    /**
     * Checks if an edge is contained in this.
     * 
     * @param e
     *            the edge to search
     * @return true if one triangle at least owns this edge, false if no
     *         triangle own it.
     */
    public final boolean contains(final Edge e) {
	for (final Triangle t : this) {
	    if (t.contains(e)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Searchs for one triangle which is under zMax. It means that all its
     * points are under zMax.
     * 
     * @param zMax
     *            the bound
     * @return the first triangle found which is under zMax
     */
    public final Triangle faceUnderZ(final double zMax) {
	for (final Triangle t : this) {
	    if (t.zMax() < zMax) {
		return t;
	    }
	}
	return null;
    }

    /**
     * Computes the point centroid : average of x, y, and z.
     * 
     * @return this point
     */
    public final Point getCentroid() {
	return new Point(this.xAverage(), this.yAverage(), this.zAverage());
    }

    /**
     * Getter.
     * 
     * @return the ID of the object
     */
    public final int getID() {
	return this.iD;
    }

    /**
     * Gets one triangle. It returns iterator().next() : it means the triangle
     * which has the first hashCode. This method is just a confortable way to
     * access a triangle without order.
     * 
     * @return one triangle
     */
    public final Triangle getOne() {
	return this.iterator().next();
    }

    /**
     * Returns the triangles of this meshes which are contained in two meshes.
     * The two meshes have the same normal : vect, and are spaced on each side
     * of the point p, distants of the error from p.
     * 
     * @param vect
     *            the normal of the two planes
     * @param p
     *            the location in space for the two planes
     * @param error
     *            the distance between the two planes and p
     * @return the triangles which are between those two planes
     */
    public final Mesh inPlanes(final Vector3d vect, final Point p,
	    final double error) {
	final Mesh ret = new Mesh();

	for (final Triangle triangle : this) {
	    if (triangle.isInPlanes(vect, p, error)) {
		ret.add(triangle);
	    }
	}

	return ret;
    }

    /**
     * Computes the intersection of three planes in 3D. Creates three planes
     * with the average normal and the centroid of each mesh, and then solve a
     * 3*3 system by inversing a matrix.
     * 
     * @param m2
     *            the second plane
     * @param m3
     *            the third plane
     * @return the point which is the intersection of the three planes
     * @throws SingularMatrixException
     *             if the planes are not well oriented
     */
    public final Point intersection(final Mesh m2, final Mesh m3)
	    throws SingularMatrixException {

	final Vector3d vect1 = this.averageNormal();
	final Vector3d vect2 = m2.averageNormal();
	final Vector3d vect3 = m3.averageNormal();

	// Creates three planes from the three meshes.
	final double a1 = vect1.x;
	final double b1 = vect1.y;
	final double c1 = vect1.z;
	final double d1 = -this.xAverage() * a1 - this.yAverage() * b1
		- this.zAverage() * c1;

	final double a2 = vect2.x;
	final double b2 = vect2.y;
	final double c2 = vect2.z;
	final double d2 = -m2.xAverage() * a2 - m2.yAverage() * b2
		- m2.zAverage() * c2;

	final double a3 = vect3.x;
	final double b3 = vect3.y;
	final double c3 = vect3.z;
	final double d3 = -m3.xAverage() * a3 - m3.yAverage() * b3
		- m3.zAverage() * c3;

	// Inverses the matrix to find the intersection point.
	final double[][] matrix = MatrixMethod.createOrthoBase(vect1, vect2,
		vect3);
	final double[][] matrixInv = MatrixMethod.getInversMatrix(matrix);

	final double[] ds = { -d1, -d2, -d3 };
	final double[] p = MatrixMethod.changeBase(ds, matrixInv);
	return new Point(p[0], p[1], p[2]);
    }

    /**
     * Checks if two meshes share an edge.
     * 
     * @param mesh
     *            the mesh to compare with
     * @return true if one edge at least is shared between this and mesh, and
     *         false otherwise.
     */
    public final boolean isNeighbour(final Mesh mesh) {
	if (mesh != this) {
	    for (final Edge e : this.returnUnsortedBounds().getEdgeList()) {
		if (mesh.contains(e)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Checks if this mesh is oriented as the other one, with an error. Compares
     * the average normals of the two meshes.
     * 
     * @param w2
     *            the other mesh to compare with
     * @param littleAngleNormalErrorFactor
     *            the error in degrees
     * @return the mesh composed of all the triangles
     */
    public final boolean isOrientedAs(final Mesh w2,
	    final double littleAngleNormalErrorFactor) {

	final double convertDegreesToRadian = 180 / Math.PI;
	return this.averageNormal().angle(w2.averageNormal()) < littleAngleNormalErrorFactor
		/ convertDegreesToRadian;
    }

    /**
     * Returns the minimal distance between two meshes. Searches for all the
     * points the one which are the closest and returns their distance.
     * 
     * @param mesh
     *            the other mesh
     * @return the minimal distance between those two meshes
     */
    public final double minimalDistance(final Mesh mesh) {

	final Set<Point> hash1 = new HashSet<Point>();
	final Polygon poly1 = this.returnUnsortedBounds();
	hash1.addAll(poly1.getPointList());

	final Set<Point> hash2 = new HashSet<Point>();
	final Polygon poly2 = mesh.returnUnsortedBounds();
	hash2.addAll(poly2.getPointList());

	double minDistance = Double.POSITIVE_INFINITY;

	for (final Point p1 : hash1) {
	    for (final Point p2 : hash2) {
		if (p1.distance(p2) < minDistance) {
		    minDistance = p1.distance(p2);
		}
	    }
	}

	return minDistance;
    }

    /**
     * Returns the triangles of this mesh which are oriented as normal, with an
     * error.
     * 
     * @param normal
     *            the vector to compare with
     * @param error
     *            the orientation error in degrees
     * @return a mesh containing all those triangles
     */
    public final Mesh orientedAs(final Vector3d normal, final double error) {
	final Mesh ret = new Mesh();
	for (final Triangle f : this) {
	    if (f.angularTolerance(normal, error)) {
		ret.add(f);
	    }
	}
	return ret;
    }

    /**
     * Returns the triangles of this mesh which are normal to vect, with an
     * error. The error is compared with the result of a dot product (vectors
     * are normalized, then this result is between 0 and 1).
     * 
     * @param vect
     *            the vector to compare with
     * @param error
     *            the orientation error
     * @return the mesh containing all those triangles
     */
    public final Mesh orientedNormalTo(final Vector3d vect, final double error) {
	final Mesh ret = new Mesh();
	for (final Triangle f : this) {
	    if (f.isNormalTo(vect, error)) {
		ret.add(f);
	    }
	}
	return ret;
    }

    /**
     * Removes from this mesh the triangles contained in the mesh.
     * 
     * @param m
     *            the mesh containing the triangles to remove
     */
    public final void remove(final Mesh m) {
	this.removeAll(m);
    }

    /**
     * Searches for all the edges which belong to the bounds. If an edge
     * contains only one triangle in this mesh, then it is part of the bounds.
     * 
     * @return the polyline containing these edges
     */
    public final Polygon returnUnsortedBounds() {
	final Polygon bounds = new Polygon();

	// Select every edges of the mesh.
	for (final Triangle tri : this) {
	    for (final Edge edge : tri.getEdges()) {
		if (edge.isBound(this)) {
		    bounds.add(edge);
		}
	    }
	}

	return bounds;
    }

    /**
     * Writes the mesh in a STL file using the ParserSTL class.
     * 
     * @param fileName
     *            the name of the file
     */
    public final void writeSTL(final String fileName) {
	final WriterSTL writer = new WriterSTL(fileName);
	writer.setMesh(this);
	writer.write();
    }

    /**
     * Computes the average x-coordinate of all points of all faces from this
     * mesh.
     * 
     * @return the average x-coordinate of all points
     */
    public final double xAverage() {
	double xAverage = 0;
	for (final Triangle face : this) {
	    xAverage += face.xAverage();
	}
	return xAverage / this.size();
    }

    /**
     * Returns a mesh containing only the triangles whose x is between m1 and
     * m2.
     * 
     * @param m1
     *            the first bound
     * @param m2
     *            the second bound
     * @return the mesh containing the triangles
     */
    public final Mesh xBetween(final double m1, final double m2) {
	final Mesh ens = new Mesh();
	for (final Triangle t : this) {
	    if (t.xMax() < Math.max(m1, m2) && t.xMin() > Math.min(m1, m2)) {
		ens.add(t);
	    }
	}
	return ens;
    }

    /**
     * Computes the x-maximum of all points of all faces from this mesh.
     * 
     * @return the x-maximum of all points of all faces from this mesh
     */
    public final double xMax() {
	double xMaxi = Double.NEGATIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.xMax() > xMaxi) {
		xMaxi = face.xMax();
	    }
	}
	return xMaxi;
    }

    /**
     * Computes the x-minimum of all points of all faces from this mesh.
     * 
     * @return the x-minimum of all points of all faces from this mesh
     */
    public final double xMin() {
	double xMini = Double.POSITIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.xMin() < xMini) {
		xMini = face.xMin();
	    }
	}
	return xMini;
    }

    /**
     * Computes the average y-coordinate of all points of all faces from this
     * mesh.
     * 
     * @return the average y-coordinate of all points
     */
    public final double yAverage() {
	double yAverage = 0;
	for (final Triangle face : this) {
	    yAverage += face.yAverage();
	}
	return yAverage / this.size();
    }

    /**
     * Returns a mesh containing only the triangles whose y is between m1 and
     * m2.
     * 
     * @param m1
     *            the first bound
     * @param m2
     *            the second bound
     * @return the mesh containing the triangles
     */
    public final Mesh yBetween(final double m1, final double m2) {
	final Mesh ens = new Mesh();
	for (final Triangle t : this) {
	    if (t.yMax() < Math.max(m1, m2) && t.yMin() > Math.min(m1, m2)) {
		ens.add(t);
	    }
	}
	return ens;
    }

    /**
     * Computes the y-maximum of all points of all faces from this mesh.
     * 
     * @return the y-maximum of all points of all faces from this mesh
     */
    public final double yMax() {
	double yMaxi = Double.NEGATIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.yMax() > yMaxi) {
		yMaxi = face.yMax();
	    }
	}
	return yMaxi;
    }

    /**
     * Computes the y-minimum of all points of all faces from this mesh.
     * 
     * @return the y-minimum of all points of all faces from this mesh
     */
    public final double yMin() {
	double yMini = Double.POSITIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.yMin() < yMini) {
		yMini = face.yMin();
	    }
	}
	return yMini;
    }

    /**
     * Computes the average z-coordinate of all points of all faces from this
     * mesh.
     * 
     * @return the average z-coordinate of all points
     */
    public final double zAverage() {
	double zAverage = 0;
	for (final Triangle face : this) {
	    zAverage += face.zAverage();
	}
	return zAverage / this.size();
    }

    /**
     * Returns a mesh containing only the triangles whose z is between m1 and
     * m2.
     * 
     * @param m1
     *            the first bound
     * @param m2
     *            the second bound
     * @return the mesh containing the triangles
     */
    public final Mesh zBetween(final double m1, final double m2) {
	final Mesh ens = new Mesh();
	for (final Triangle t : this) {
	    if (t.zMax() < Math.max(m1, m2) && t.zMin() > Math.min(m1, m2)) {
		ens.add(t);
	    }
	}
	return ens;
    }

    /**
     * Computes the z-maximum of all points of all faces from this mesh.
     * 
     * @return the z-maximum of all points of all faces from this mesh
     */
    public final double zMax() {
	double zMaxi = Double.NEGATIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.zMax() > zMaxi) {
		zMaxi = face.zMax();
	    }
	}
	return zMaxi;
    }

    /**
     * Computes the z-minimum of all points of all faces from this mesh.
     * 
     * @return the z-minimum of all points of all faces from this mesh
     */
    public final double zMin() {
	double zMini = Double.POSITIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.zMin() < zMini) {
		zMini = face.zMin();
	    }
	}
	return zMini;
    }

    /**
     * Returns the triangle which has the lowest z.
     * 
     * @return the triangle which has the lowest z
     */
    public final Triangle zMinFace() {
	Triangle t = null;
	if (this.isEmpty()) {
	    throw new InvalidParameterException();
	}
	double zMini = Double.POSITIVE_INFINITY;
	for (final Triangle face : this) {
	    if (face.zMin() < zMini) {
		t = face;
		zMini = face.zMin();
	    }
	}
	return t;
    }
}
