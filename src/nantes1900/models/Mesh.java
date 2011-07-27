package nantes1900.models;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;
import nantes1900.utils.MatrixMethod;
import nantes1900.utils.MatrixMethod.SingularMatrixException;
import nantes1900.utils.WriterSTL;

/**
 * Implement a mesh as a HashSet of Triangle.
 * 
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Mesh extends HashSet<Triangle> {

	private static final long serialVersionUID = 1L;

	private ArrayList<Mesh> neighbours = new ArrayList<Mesh>();

	private final int ID;
	private static int ID_current = 0;

	/**
	 * Void constructor
	 */
	public Mesh() {
		super();
		this.ID = ID_current++;
	}

	/**
	 * Constructor from a collection of triangle.
	 * 
	 * @param c
	 *            the collection
	 */
	public Mesh(Collection<? extends Triangle> c) {
		super(c);
		this.ID = ID_current++;
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
	public ArrayList<Mesh> getNeighbours() {
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
			for (Triangle t : e.getTriangleList()) {
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
	 * Sort the bounds.
	 * 
	 * @return a list of sorted bounds.
	 */
	public ArrayList<Polyline> returnBounds(Vector3d normal) {
		Polyline bounds = this.returnUnsortedBounds();

		ArrayList<Polyline> boundList = new ArrayList<Polyline>();

		Polyline ret = new Polyline();

		while (!bounds.isEmpty()) {
			ret = new Polyline();

			Edge arete = bounds.getOne();

			ret = arete.returnOneBound(bounds,
					this.getLeftPoint(arete, normal), normal);

			if (ret != null)
				boundList.add(ret);

			bounds.remove(ret);
		}

		return boundList;
	}

	/**
	 * Compute some calculations to obtain the point (ie the direction) to
	 * follow to have the inside mesh at the right, and the hole at the left.
	 * 
	 * @param edge
	 *            an edge which is a bound for this
	 * @param normalFloor
	 *            the normal to the floor
	 * @return the point which represent the direction
	 */
	public Point getLeftPoint(Edge edge, Vector3d normalFloor) {

		Point p31 = null, p32 = null;

		// FIXME : if edge doesn't have 2 triangles ?

		// Get the third point of each triangle of the edge
		for (Point p : edge.getTriangleList().get(0).getPoints()) {
			if (!edge.contains(p)) {
				p31 = p;
			}
		}
		for (Point p : edge.getTriangleList().get(1).getPoints()) {
			if (!edge.contains(p)) {
				p32 = p;
			}
		}

		Vector3d vect = new Vector3d();
		Vector3d v31 = new Vector3d();
		Vector3d v32 = new Vector3d();

		vect.x = edge.getP2().getX() - edge.getP1().getX();
		vect.y = edge.getP2().getY() - edge.getP1().getY();
		vect.z = edge.getP2().getZ() - edge.getP1().getZ();

		v31.x = p31.getX() - edge.getP1().getX();
		v31.y = p31.getY() - edge.getP1().getY();
		v31.z = p31.getZ() - edge.getP1().getZ();

		v32.x = p32.getX() - edge.getP1().getX();
		v32.y = p32.getY() - edge.getP1().getY();
		v32.z = p32.getZ() - edge.getP1().getZ();

		Vector3d cross = new Vector3d();
		cross.cross(normalFloor, vect);

		// If (normalFloor cross vect) dot a vector is positive, then the vector
		// point considered is at the left of vect.

		// We then try to determine on which side is the inside of the mesh, and
		// on which side is the outside, to know the direction : we want to
		// begin with the outside at our left.
		if (!this.contains(edge.getTriangleList().get(0))) {
			if (cross.dot(v31) > 0) {
				return edge.getP2();
			} else {
				return edge.getP1();
			}
		} else {
			if (cross.dot(v32) > 0) {
				return edge.getP2();
			} else {
				return edge.getP1();
			}
		}
	}

	/**
	 * Calculate the max of the bounds returned by the returnBounds.
	 * 
	 * @return the longest bound which is supposed to be the contour of the
	 *         surface
	 */
	public Polyline returnLongestBound(Vector3d normal) {

		ArrayList<Polyline> boundList = this.returnBounds(normal);
		Polyline ret = null;

		double max = Double.MIN_VALUE;
		for (Polyline p : boundList) {
			if (p.length() > max) {
				max = p.length();
				ret = p;
			}
		}

		return ret;
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
			Vector3d normalFloor) {

		ArrayList<Mesh> neighbours = this.getNeighbours();
		Polyline edges = new Polyline();

		try {
			for (Mesh m2 : neighbours) {
				ArrayList<Point> points = new ArrayList<Point>();

				for (Mesh m3 : neighbours) {
					if (m2.getNeighbours().contains(m3)) {
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

						Point p = plane1.intersection(plane2, plane3);

						Point mapP = pointMap.get(p);
						if (mapP == null)
							pointMap.put(p, p);
						else
							p = mapP;

						points.add(p);
					}
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
					System.err.println("Erreur!");
				}
			}

			if (edges.edgeSize() > 2) {
				edges.setNormal(this.averageNormal());
				edges.order();
				return edges;
			} else {
				return null;
			}
		} catch (SingularMatrixException e1) {
			System.err.println("Matrix error ! Program shutting down !");
			System.exit(1);
			return null;
		}
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

		computedWallPlane.add(new Triangle(p1, p2, p3, e1, e2, e3, vect));

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