package fr.nantes1900.models;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.WriterSTL;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;


/**
 * Implement a mesh as a HashSet of Triangle.
 * 
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Mesh extends HashSet<Triangle> {

	private static final long serialVersionUID = 1L;

	private List<Mesh> neighbours = new ArrayList<Mesh>();

	private final int ID;
	private static int currentID = 0;

	/**
	 * Void constructor
	 */
	public Mesh() {
		super();
		this.ID = currentID++;
	}

	/**
	 * Constructor from a collection of triangle.
	 * 
	 * @param c
	 *            the collection
	 */
	public Mesh(Collection<? extends Triangle> c) {
		super(c);
		this.ID = currentID++;
	}

	/**
	 * Add a neighbour to the attribute neighbours. Check if it is not
	 * contained. Add also this to the m neighbours, also checking if not
	 * already contained.
	 * 
	 * @param m
	 *            the mesh as neighbour to add
	 */
	public void addNeighbour(Mesh m) {
		if (!this.neighbours.contains(m))
			this.neighbours.add(m);
		if (!m.neighbours.contains(this))
			m.neighbours.add(this);
	}

	public int getID() {
		return this.ID;
	}

	/**
	 * Getter
	 * 
	 * @return the neighbours
	 */
	public List<Mesh> getNeighbours() {
		return this.neighbours;
	}

	/**
	 * Compute the average normal of all Faces composing this Ensemble
	 * 
	 * @return average The average Vector3d normal.
	 */
	public Vector3d averageNormal() {
		int n = this.size();
		Vector3d average = new Vector3d();
		for (Triangle face : this) {
			average.add(face.getNormal());
		}
		average.scale(1 / (double) n);

		return average;
	}

	/**
	 * Compute the average x-coordinate of all points of all faces from this
	 * mesh.
	 * 
	 * @return the average x-coordinate of all points
	 */
	public double xAverage() {
		double xAverage = 0;
		for (Triangle face : this) {
			xAverage += face.xAverage();
		}
		return xAverage / this.size();
	}

	/**
	 * Compute the average y-coordinate of all points of all faces from this
	 * mesh.
	 * 
	 * @return the average y-coordinate of all points
	 */
	public double yAverage() {
		double yAverage = 0;
		for (Triangle face : this) {
			yAverage += face.yAverage();
		}
		return yAverage / this.size();
	}

	/**
	 * Compute the average z-coordinate of all points of all faces from this
	 * mesh.
	 * 
	 * @return the average z-coordinate of all points
	 */
	public double zAverage() {
		double zAverage = 0;
		for (Triangle face : this) {
			zAverage += face.zAverage();
		}
		return zAverage / this.size();
	}

	/**
	 * Compute the x-maximum of all points of all faces from this mesh.
	 * 
	 * @return the x-maximum of all points of all faces from this mesh
	 */
	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this) {
			if (face.xMax() > xMaxi) {
				xMaxi = face.xMax();
			}
		}
		return xMaxi;
	}

	/**
	 * Compute the x-minimum of all points of all faces from this mesh.
	 * 
	 * @return the x-minimum of all points of all faces from this mesh
	 */
	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.xMin() < xMini) {
				xMini = face.xMin();
			}
		}
		return xMini;
	}

	/**
	 * Compute the y-maximum of all points of all faces from this mesh.
	 * 
	 * @return the y-maximum of all points of all faces from this mesh
	 */
	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this) {
			if (face.yMax() > yMaxi) {
				yMaxi = face.yMax();
			}
		}
		return yMaxi;
	}

	/**
	 * Compute the y-minimum of all points of all faces from this mesh.
	 * 
	 * @return the y-minimum of all points of all faces from this mesh
	 */
	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.yMin() < yMini) {
				yMini = face.yMin();
			}
		}
		return yMini;
	}

	/**
	 * Compute the z-maximum of all points of all faces from this mesh.
	 * 
	 * @return the z-maximum of all points of all faces from this mesh
	 */
	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this) {
			if (face.zMax() > zMaxi) {
				zMaxi = face.zMax();
			}
		}
		return zMaxi;
	}

	/**
	 * Compute the z-minimum of all points of all faces from this mesh.
	 * 
	 * @return the z-minimum of all points of all faces from this mesh
	 */
	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.zMin() < zMini) {
				zMini = face.zMin();
			}
		}
		return zMini;
	}

	/**
	 * Compute the average length on x of all points of all faces from this
	 * mesh.
	 * 
	 * @return the average length on x of all points of all faces from this mesh
	 */
	public double xLengthAverage() {
		double xLengthAve = 0;
		for (Triangle t : this) {
			xLengthAve += (t.xMax() - t.xMin());
		}
		return xLengthAve / (double) this.size();
	}

	/**
	 * Compute the average length on y of all points of all faces from this
	 * mesh.
	 * 
	 * @return the average length on y of all points of all faces from this mesh
	 */
	public double yLengthAverage() {
		double yLengthAve = 0;
		for (Triangle t : this) {
			yLengthAve += (t.yMax() - t.yMin());
		}
		return yLengthAve / (double) this.size();
	}

	/**
	 * Compute the average length on z of all points of all faces from this
	 * mesh.
	 * 
	 * @return the average length on z of all points of all faces from this mesh
	 */
	public double zLengthAverage() {
		double zLengthAve = 0;
		for (Triangle t : this) {
			zLengthAve += (t.zMax() - t.zMin());
		}
		return zLengthAve / (double) this.size();
	}

	/**
	 * Return a Mesh containing only the triangle whose x is between m1 and m2
	 * 
	 * @param m1
	 *            the first bound
	 * @param m2
	 *            the second bound
	 * @return the mesh containing the triangles
	 */
	public Mesh xBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for (Triangle t : this) {
			if (t.xMax() < Math.max(m1, m2) && t.xMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	/**
	 * Return a Mesh containing only the triangle whose y is between m1 and m2
	 * 
	 * @param m1
	 *            the first bound
	 * @param m2
	 *            the second bound
	 * @return the mesh containing the triangles
	 */
	public Mesh yBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for (Triangle t : this) {
			if (t.yMax() < Math.max(m1, m2) && t.yMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	/**
	 * Return a Mesh containing only the triangle whose z is between m1 and m2
	 * 
	 * @param m1
	 *            the first bound
	 * @param m2
	 *            the second bound
	 * @return the mesh containing the triangles
	 */
	public Mesh zBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for (Triangle t : this) {
			if (t.zMax() < Math.max(m1, m2) && t.zMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	/**
	 * Return the triangle which has the lowest z.
	 * 
	 * @return the triangle which has the lowest z
	 */
	public Triangle zMinFace() {
		Triangle t = null;
		if (this.isEmpty())
			throw new InvalidParameterException();
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.zMin() < zMini) {
				t = face;
				zMini = face.zMin();
			}
		}
		return t;
	}

	/**
	 * Search for one triangle which is under zMax. It means that all its points
	 * are under zMax.
	 * 
	 * @param zMax
	 *            the bound
	 * @return the first triangle found which is under zMax
	 */
	public Triangle faceUnderZ(double zMax) {
		for (Triangle t : this) {
			if (t.zMax() < zMax) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Get one triangle. It returns iterator().next(), it means the triangle
	 * which has the first hashCode. This method is just a confortable way to
	 * access a triangle without order.
	 * 
	 * @return one triangle
	 */
	public Triangle getOne() {
		return this.iterator().next();
	}

	/**
	 * Remove the triangles of m in this.
	 * 
	 * @param m
	 *            the mesh which contains the triangle to remove from this
	 */
	public void remove(Mesh m) {
		this.removeAll(m);
	}

	/**
	 * Write the mesh in a STL file using the class ParserSTL
	 * 
	 * @param fileName
	 *            the name of the file
	 */
	public void writeSTL(String fileName) {
		WriterSTL writer = new WriterSTL(fileName);
		writer.setMesh(this);
		writer.write();
	}

	/**
	 * Return the triangles which are oriented as normal, with an error.
	 * 
	 * @param normal
	 *            the vector to compare with
	 * @param error
	 *            the orientation error in degrees
	 * @return a mesh containing all those triangles
	 */
	public Mesh orientedAs(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for (Triangle f : this) {
			if (f.angularTolerance(normal, error))
				ret.add(f);
		}
		return ret;
	}

	/**
	 * Return the triangle which are normal to vect, with an error. The error is
	 * compared with the result of a dot product (vectors are normalized, then
	 * this result is between 0 and 1).
	 * 
	 * @param vect
	 *            the vector to compare with
	 * @param error
	 *            the orientation error
	 * @return the mesh containing all those triangles
	 */
	public Mesh orientedNormalTo(Vector3d vect, double error) {
		Mesh ret = new Mesh();
		for (Triangle f : this) {
			if (f.isNormalTo(vect, error))
				ret.add(f);
		}
		return ret;
	}

	/**
	 * Change the base of all the points contained in the mesh.
	 * 
	 * @param matrix
	 *            the change base matrix
	 */
	public void changeBase(double[][] matrix) {
		if (matrix == null)
			throw new InvalidParameterException();

		HashSet<Point> set = new HashSet<Point>();
		HashSet<Triangle> mesh = new HashSet<Triangle>();

		for (Triangle f : this) {
			set.addAll(f.getPoints());
			MatrixMethod.changeBase(f.getNormal(), matrix);
			mesh.add(f);
		}

		for (Point p : set)
			p.changeBase(matrix);

		this.clear();
		this.addAll(mesh);
	}

	/**
	 * Search for all the edges which belong to the bounds. If an edge contain
	 * only one triangle in this, then it is part of the bounds.
	 * 
	 * @return
	 */
	public Polyline returnUnsortedBounds() {
		Polyline bounds = new Polyline();
		HashSet<Edge> edges = new HashSet<Edge>();

		// Select every edges of the mesh.
		for (Triangle tri : this) {
			edges.addAll(tri.getEdges());
		}

		// On each edges, select the ones that have only one triangle associated
		// in this, ie they are part of the bounds.
		for (Edge e : edges) {
			int counter = 0;
			for (Triangle t : e.getTriangles()) {
				if (this.contains(t))
					counter++;
			}

			// Add the edges which belong to only one triangle in this are
			// selected as part of the bounds.
			if (counter == 1)
				bounds.add(e);
		}
		return bounds;
	}

	/**
	 * Check if two meshes share an edge.
	 * 
	 * @param mesh
	 *            the mesh to compare with
	 * @return tru if one edge at least is shared between this and mesh, and
	 *         false otherwise.
	 */
	// FIXME : too long method. Optimize it.
	public boolean isNeighbour(Mesh mesh) {
		if (mesh != this) {
			for (Edge e : this.returnUnsortedBounds().getEdgeList()) {
				if (mesh.contains(e))
					return true;
			}
		}
		return false;
	}

	/**
	 * Check if an edge is contained in this.
	 * 
	 * @param e
	 *            the edge to search
	 * @return true if one triangle at least owns this edge, false if no
	 *         triangle own it.
	 */
	public boolean contains(Edge e) {
		for (Triangle t : this) {
			if (t.contains(e))
				return true;
		}
		return false;
	}

	public Polyline findEdges(ArrayList<Mesh> wallList,
			HashMap<Point, Point> pointMap, HashMap<Edge, Edge> edgeMap,
			Vector3d normalFloor) throws InvalidSurfaceException {

		Polyline edges = new Polyline();

		for (Mesh m2 : this.neighbours) {
			ArrayList<Point> points = new ArrayList<Point>();

			try {
				for (Mesh m3 : this.neighbours) {

					if (m2.neighbours.contains(m3)) {
						Mesh plane1 = this;
						Mesh plane2 = m2;
						Mesh plane3 = m3;
						if (wallList.contains(this)) {
							plane1 = this.returnVerticalPlane(normalFloor);
						}
						if (wallList.contains(m2)) {
							plane2 = m2.returnVerticalPlane(normalFloor);
						}
						if (wallList.contains(m3)) {
							plane3 = m3.returnVerticalPlane(normalFloor);
						}

						// If there is two neighbours oriented the same, then
						// don't try to cumpute the intersection of the two
						// planes, but don't compute the edge.
						if (this.isOrientedAs(plane2, 10)) {
							this.writeSTL("erreur1.stl");
							m2.writeSTL("erreur2.stl");
							m3.writeSTL("erreur3.stl");
							// System.exit(1);
							throw new ParallelPlanesException();
						}
						if (this.isOrientedAs(plane3, 10)) {
							this.writeSTL("erreur1.stl");
							m2.writeSTL("erreur2.stl");
							m3.writeSTL("erreur3.stl");
							// System.exit(1);
							throw new ParallelPlanesException();
						}
						if (plane2.isOrientedAs(plane3, 10)) {
							this.writeSTL("erreur1.stl");
							m2.writeSTL("erreur2.stl");
							m3.writeSTL("erreur3.stl");
							// System.exit(1);
							throw new ParallelPlanesException();
						}

						try {
							Point p = plane1.intersection(plane2, plane3);

							Point mapP = pointMap.get(p);
							if (mapP == null)
								pointMap.put(p, p);
							else
								p = mapP;

							points.add(p);

						} catch (SingularMatrixException e1) {
							this.writeSTL("erreurMatrix1.stl");
							m2.writeSTL("erreurMatrix2.stl");
							m3.writeSTL("erreurMatrix3.stl");
							// System.err.println("Matrix exception");
							// System.exit(1);
							throw new InvalidSurfaceException();
						}
					}
				}
			} catch (ParallelPlanesException e) {
				// System.err.println("Parallel planes exception !");
				throw new InvalidSurfaceException();
			}

			if (points.size() == 2) {

				Edge e = new Edge(points.get(0), points.get(1));

				Edge mapE = edgeMap.get(e);
				if (mapE == null)
					edgeMap.put(e, e);
				else
					e = mapE;

				edges.add(e);
			} else {
				// We let the exception for below : if they can complete the
				// last edge, it's ok, if not, then they throw themselves the
				// exception.
			}
		}

		if (edges.edgeSize() == this.getNeighbours().size()) {
			edges.setNormal(this.averageNormal());
			edges.order();
			return edges;
		} else if (edges.edgeSize() == this.getNeighbours().size() - 1) {
			edges.setNormal(this.averageNormal());
			// Complete the last edge
			ArrayList<Point> list = new ArrayList<Point>();
			for (Point p : edges.getPointList()) {
				if (edges.getNumNeighbours(p) == 1) {
					list.add(p);
				}
			}
			if (list.size() == 2) {
				edges.add(new Edge(list.get(0), list.get(1)));
				// System.err.println("Yeah");
			} else {
				// System.err.println("Pas marché !");
				throw new InvalidSurfaceException();
			}
			edges.order();
			return edges;
		} else {
//			System.err.println("Il manquait plus d'un edge !");
			throw new InvalidSurfaceException();
		}
	}

	public static class InvalidSurfaceException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public static class ParallelPlanesException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	private Mesh returnVerticalPlane(Vector3d normalFloor) {

		Vector3d averageNormal = this.averageNormal();

		Mesh computedWallPlane = new Mesh();

		Point centroid = new Point(this.xAverage(), this.yAverage(),
				this.zAverage());

		// TODO : if normal.getY() == 0 ?
		Point p1 = new Point(centroid.getX() + 1, centroid.getY()
				- averageNormal.getX() / averageNormal.getY(), centroid.getZ());
		Point p2 = p1;
		Point p3 = centroid;

		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p1, p3);

		Vector3d vect = new Vector3d();
		vect.cross(normalFloor, e3.convertToVector3d());

		try {
			computedWallPlane.add(new Triangle(p1, p2, p3, e1, e2, e3, vect));
		} catch (MoreThanTwoTrianglesPerEdgeException e) {
			e.printStackTrace();
		}

		return computedWallPlane;
	}

	/**
	 * Compute the intersection of three planes in 3D. Create three planes with
	 * the average normal of eazch mesh, and the centroid of each mesh, and then
	 * solve a 3*3 system by inversing a matrix.
	 * 
	 * @param m2
	 *            the second plane
	 * @param m3
	 *            the third plane
	 * @return the point which is the intersection of the three planes
	 * @throws SingularMatrixException
	 *             //FIXME : treat it in the method !
	 */
	public Point intersection(Mesh m2, Mesh m3) throws SingularMatrixException {

		Vector3d vect1 = this.averageNormal();
		Vector3d vect2 = m2.averageNormal();
		Vector3d vect3 = m3.averageNormal();

		// FIXME : verifications : if they are colinear... ? This problem is
		// linked with the singular matrix problem !

		double a1 = vect1.x, b1 = vect1.y, c1 = vect1.z;
		double d1 = -this.xAverage() * a1 - this.yAverage() * b1
				- this.zAverage() * c1;

		double a2 = vect2.x, b2 = vect2.y, c2 = vect2.z;
		double d2 = -m2.xAverage() * a2 - m2.yAverage() * b2 - m2.zAverage()
				* c2;

		double a3 = vect3.x, b3 = vect3.y, c3 = vect3.z;
		double d3 = -m3.xAverage() * a3 - m3.yAverage() * b3 - m3.zAverage()
				* c3;

		double[][] matrix = null, matrixInv = null;
		matrix = MatrixMethod.createOrthoBase(vect1, vect2, vect3);
		matrixInv = MatrixMethod.getInversMatrix(matrix);

		double[] ds = { -d1, -d2, -d3 };
		double[] p = MatrixMethod.changeBase(ds, matrixInv);
		return new Point(p[0], p[1], p[2]);
	}

	public boolean isOrientedAs(Mesh w2, double littleAngleNormalErrorFactor) {
		return (this.averageNormal().angle(w2.averageNormal()) < littleAngleNormalErrorFactor
				/ 180 * Math.PI);
	}
}