package models;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import models.basis.Edge;
import models.basis.Point;
import models.basis.Triangle;

import utils.MatrixMethod;
import utils.WriterSTL;

/**
 * Implement a mesh as a HashSet of Triangle.
 * 
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Mesh extends HashSet<Triangle> {

	private static final long serialVersionUID = 1L;

	private ArrayList<Mesh> neighbours = new ArrayList<Mesh>();

	/**
	 * Void constructor
	 */
	public Mesh() {
		super();
	}

	/**
	 * Constructor from a collection of triangle.
	 * 
	 * @param c
	 *            the collection
	 */
	public Mesh(Collection<? extends Triangle> c) {
		super(c);
	}

	/**
	 * Add a neighbours to the attribute neighbours. Check if it is not
	 * contained.
	 * 
	 * @param p
	 *            the polyline as neighbour to add
	 */
	public void addNeighbour(Mesh m) {
		if (!this.neighbours.contains(m))
			this.neighbours.add(m);
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
		WriterSTL.write(fileName, this);
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
		// point considered is at the left of vect

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
	 * Search on two meshes to know if they have one common point.
	 * 
	 * @param mesh
	 *            the mesh to search in
	 * @return true if at least one point is shared, false otherwise
	 */
	public boolean isNeighbour(Mesh mesh) {
		if (mesh != this) {
			for (Triangle t1 : this) {
				for (Triangle t2 : mesh) {
					if (t1.isNeighboor(t2))
						return true;
				}
			}
		}
		return false;
	}

	public Edge findCommonEdge(Mesh m, double radiusError) {

		// We define the axe of the intersection
		Vector3d norm1 = this.averageNormal();
		Vector3d norm2 = m.averageNormal();

		// We define the axe of the intersection
		Edge axis = this.getAxis(m, norm1, norm2, radiusError);

		return axis;
	}

	private Edge getAxis(Mesh m, Vector3d norm1, Vector3d norm2,
			double radiusError) {
		Mesh p1 = this;
		Mesh p2 = m;

		Point p01 = p1.getOne().getP1();
		Point p02 = p2.getOne().getP1();

		double a1 = norm1.x, b1 = norm1.y, c1 = norm1.z;
		double a2 = norm2.x, b2 = norm2.y, c2 = norm2.z;
		double d1 = a1 * p01.getX() + b1 * p01.getY() + c1 * p01.getZ();
		double d2 = a2 * p02.getX() + b2 * p02.getY() + c2 * p02.getZ();

		double x = 0;
		double y = (c2 * d1 - c1 * d2) / (b1 * c2 - b2 * c1);
		double z = (b1 * d2 - b2 * d1) / (b1 * c2 - b2 * c1);

		Vector3d norm3 = new Vector3d();
		norm3.cross(norm1, norm2);

		Edge axis = new Edge(new Point(x, y, z), new Point(x + norm3.x, y
				+ norm3.y, z + norm3.z));

		// Get the points which are into the cylinder, and project them on
		// the axe.
		ArrayList<Point> cylinder1 = p1.getCylinderInfinite(axis, radiusError);
		ArrayList<Point> cylinder2 = p2.getCylinderInfinite(axis, radiusError);

		ArrayList<Point> cylinder = new ArrayList<Point>();
		cylinder.addAll(cylinder1);
		cylinder.addAll(cylinder2);

		// Select the max and min
		Point extremityMax = this.getCylinderMaxPoint(cylinder, axis);
		Point extremityMin = this.getCylinderMinPoint(cylinder, axis);

		// Adjust the two surfaces to have the same edge with the same max
		// and min
		Point extremityMax1 = this.getCylinderMaxPoint(cylinder1, axis);
		Point extremityMin1 = this.getCylinderMinPoint(cylinder1, axis);
		Point extremityMax2 = this.getCylinderMaxPoint(cylinder2, axis);
		Point extremityMin2 = this.getCylinderMinPoint(cylinder2, axis);

		if (extremityMax == extremityMax1) {
			extremityMax2.set(extremityMax1.getPointAsCoordinates());
		} else {
			extremityMax1.set(extremityMax2.getPointAsCoordinates());
		}

		if (extremityMin == extremityMin1) {
			extremityMin2.set(extremityMin1.getPointAsCoordinates());
		} else {
			extremityMin1.set(extremityMin2.getPointAsCoordinates());
		}

		return new Edge(extremityMin, extremityMax);
	}

	private Point getCylinderMaxPoint(ArrayList<Point> cylinder, Edge axe) {
		Point ref = null;
		double max = Double.NEGATIVE_INFINITY;

		for (Triangle t : this) {
			for (Point p : t.getPoints()) {
				if (Mesh.getAxisProjectionPoint(cylinder, axe, p).getX() > max) {
					ref = Mesh.getAxisProjectionPoint(cylinder, axe, p);
					max = ref.getX();
				}
			}
		}

		return ref;
	}

	private Point getCylinderMinPoint(ArrayList<Point> cylinder, Edge axe) {
		Point ref = null;
		double min = Double.POSITIVE_INFINITY;

		for (Triangle t : this) {
			for (Point p : t.getPoints()) {
				if (Mesh.getAxisProjectionPoint(cylinder, axe, p).getX() < min) {
					ref = Mesh.getAxisProjectionPoint(cylinder, axe, p);
					min = ref.getX();
				}
			}
		}

		return ref;
	}

	/**
	 * Build a infinite cylinder, with the edge e as central axe, with error as
	 * radius, and select only the points of this which are inside.
	 * 
	 * @param e
	 *            the edge which will be the axe
	 * @param error
	 *            the radius
	 * @return a list of points which are inside the cylinder
	 */
	public ArrayList<Point> getCylinderInfinite(Edge e, double error) {
		ArrayList<Point> ret = new ArrayList<Point>();

		for (Triangle t : this) {
			for (Point p : t.getPoints()) {
				if (e.isInInfiniteCylinder3D(p, error)) {
					ret.add(p);
				}
			}
		}

		return ret;
	}

	private static Point getAxisProjectionPoint(ArrayList<Point> cylinder,
			Edge axe, Point p) {
		Point p1 = axe.getP1(), p2 = axe.getP2(), p3 = p;

		double x1 = p1.getX(), x2 = p2.getX(), x3 = p3.getX();
		double y1 = p1.getY(), y2 = p2.getY(), y3 = p3.getY();
		double z1 = p1.getZ(), z2 = p2.getZ(), z3 = p3.getZ();

		double lambda = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1) + (z3 - z1)
				* (z2 - z1))
				/ ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1)
						* (z2 - z1));

		double x4 = lambda * (x2 - x1) + x1, y4 = lambda * (y2 - y1) + y1, z4 = lambda
				* (z2 - z1) + z1;

		return new Point(x4, y4, z4);
	}
}