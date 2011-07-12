package modeles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.vecmath.Vector3d;

/**
 * Implement a triangle, composed of three points, three edges, and one vector
 * as a normal
 * 
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Triangle {

	private Point[] points = new Point[3];
	private Vector3d normal;
	private Edge[] edges = new Edge[3];

	/**
	 * Constructor of the triangle.
	 * 
	 * @param p0
	 *            one point
	 * @param p1
	 *            one point
	 * @param p2
	 *            one point
	 * @param e1
	 *            one edge composed by two of the three points
	 * @param e2
	 *            one edge composed by two of the three points
	 * @param e3
	 *            one edge composed by two of the three points
	 * @param normale
	 *            the normal of the triangle
	 */
	public Triangle(Point p0, Point p1, Point p2, Edge e1, Edge e2, Edge e3,
			Vector3d normale) {
		this.points[0] = p0;
		this.points[1] = p1;
		this.points[2] = p2;
		this.normal = normale;
		this.edges[0] = e1;
		this.edges[1] = e2;
		this.edges[2] = e3;
		this.edges[0].addTriangle(this);
		this.edges[1].addTriangle(this);
		this.edges[2].addTriangle(this);
	}

	/**
	 * Copy constructor of the triangle. Caution : do not use this constructor
	 * to create double Triangle with equal values and different references !
	 * This method create new points so as the old ones are not modified.
	 * 
	 * @param t
	 *            the triangle to copy
	 */
	private Triangle(Triangle t) {
		this.points[0] = new Point(t.points[0]);
		this.points[1] = new Point(t.points[1]);
		this.points[2] = new Point(t.points[2]);
		this.normal = new Vector3d(t.normal);
		this.edges[0] = new Edge(this.points[0], this.points[1]);
		this.edges[1] = new Edge(this.points[1], this.points[2]);
		this.edges[2] = new Edge(this.points[2], this.points[0]);
		this.edges[0].addTriangle(this);
		this.edges[1].addTriangle(this);
		this.edges[2].addTriangle(this);
	}

	/**
	 * Getter.
	 * 
	 * @return the first point
	 */
	public Point getP1() {
		return points[0];
	}

	/**
	 * Getter.
	 * 
	 * @return the second point
	 */
	public Point getP2() {
		return points[1];
	}

	/**
	 * Getter.
	 * 
	 * @return the third point
	 */
	public Point getP3() {
		return points[2];
	}

	/**
	 * Getter.
	 * 
	 * @return a collection containing the three points
	 */
	public Collection<Point> getPoints() {
		return Arrays.asList(this.points);
	}

	/**
	 * Getter.
	 * 
	 * @return the normal
	 */
	public Vector3d getNormal() {
		return normal;
	}

	/**
	 * Getter.
	 * 
	 * @return the first edge
	 */
	public Edge getE1() {
		return edges[0];
	}

	/**
	 * Getter.
	 * 
	 * @return the second edge
	 */
	public Edge getE2() {
		return edges[1];
	}

	/**
	 * Getter.
	 * 
	 * @return the third edge
	 */
	public Edge getE3() {
		return edges[2];
	}

	/**
	 * Getter.
	 * 
	 * @return a collection containing the three edges
	 */
	public Collection<Edge> getEdges() {
		return Arrays.asList(this.edges);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return points[0].toString() + "\n" + points[1].toString() + "\n"
				+ points[2].toString() + "\n" + normal.toString();
	}

	/**
	 * Check if p is one of the three points of this. Use the method equals of
	 * this class
	 * 
	 * @param p
	 *            the point to check
	 * @return true is one point is equal with p
	 */
	public boolean contains(Point p) {
		for (Point point : points) {
			if (point.equals(p))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	// FIXME : revoir le hashCode !
	public int hashCode() {
		int result = 1;
		result = result + ((points[0] == null) ? 0 : points[0].hashCode());
		result = result + ((points[1] == null) ? 0 : points[1].hashCode());
		result = result + ((points[2] == null) ? 0 : points[2].hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	// TODO : do not depends on the normal : fix it !
	// Do not depend on the edges !
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triangle other = (Triangle) obj;

		if (this.contains(other.points[0]) && this.contains(other.points[1])
				&& this.contains(other.points[2]))
			return true;
		else
			return false;
	}

	/**
	 * @return
	 */
	public double xAverage() {
		double xAverage = points[0].getX() + points[1].getX()
				+ points[2].getX();
		return xAverage / 3;
	}

	/**
	 * @return
	 */
	public double zAverage() {
		double zAverage = points[0].getZ() + points[1].getZ()
				+ points[2].getZ();
		return zAverage / 3;
	}

	/**
	 * @return
	 */
	public double yAverage() {
		double yAverage = points[0].getY() + points[1].getY()
				+ points[2].getY();
		return yAverage / 3;
	}

	/**
	 * @return
	 */
	public double xMin() {
		return Math.min(points[0].getX(),
				Math.min(points[1].getX(), points[2].getX()));
	}

	/**
	 * @return
	 */
	public double xMax() {
		return Math.max(points[0].getX(),
				Math.max(points[1].getX(), points[2].getX()));
	}

	/**
	 * @return
	 */
	public double yMin() {
		return Math.min(points[0].getY(),
				Math.min(points[1].getY(), points[2].getY()));
	}

	/**
	 * @return
	 */
	public double yMax() {
		return Math.max(points[0].getY(),
				Math.max(points[1].getY(), points[2].getY()));
	}

	/**
	 * @return
	 */
	public double zMin() {
		return Math.min(points[0].getZ(),
				Math.min(points[1].getZ(), points[2].getZ()));
	}

	/**
	 * @return
	 */
	public double zMax() {
		return Math.max(points[0].getZ(),
				Math.max(points[1].getZ(), points[2].getZ()));
	}

	/**
	 * @return
	 */
	public Point xMinPoint() {
		double xMin = this.xMin();
		for (Point p : points) {
			if (p.getX() == xMin)
				return p;
		}
		return null;
	}

	/**
	 * @return
	 */
	public Point xMaxPoint() {
		double xMax = this.xMax();
		for (Point p : points) {
			if (p.getX() == xMax)
				return p;
		}
		return null;
	}

	/**
	 * @return
	 */
	public Point yMinPoint() {
		double yMin = this.yMin();
		for (Point p : points) {
			if (p.getY() == yMin)
				return p;
		}
		return null;
	}

	/**
	 * @return
	 */
	public Point yMaxPoint() {
		double yMay = this.yMax();
		for (Point p : points) {
			if (p.getY() == yMay)
				return p;
		}
		return null;
	}

	/**
	 * @return
	 */
	public Point zMinPoint() {
		double zMin = this.zMin();
		for (Point p : points) {
			if (p.getZ() == zMin)
				return p;
		}
		return null;
	}

	/**
	 * @return
	 */
	public Point zMaxPoint() {
		double zMax = this.zMax();
		for (Point p : points) {
			if (p.getZ() == zMax)
				return p;
		}
		return null;
	}

	/**
	 * @param x
	 * @return
	 */
	public Triangle xProjection(double x) {
		Triangle t = new Triangle(this);

		for (Point p : t.points)
			p.setX(x);

		return t;
	}

	/**
	 * @param y
	 * @return
	 */
	public Triangle yProjection(double y) {
		Triangle t = new Triangle(this);

		for (Point p : t.points)
			p.setY(y);

		return t;
	}

	// FIXME : caution to the Edges ..
	// FIXME : it destroy the former Triangle... and all the Points...
	/**
	 * @param z
	 * @return
	 */
	public Triangle zProjection(double z) {
		Triangle t = new Triangle(this);

		for (Point p : t.points)
			p.setZ(z);

		return t;
	}

	/**
	 * @param vector
	 * @param error
	 * @return
	 */
	public boolean angularTolerance(Vector3d vector, double error) {
		return (this.normal.angle(vector) * 180.0 / Math.PI < error);
	}

	/**
	 * @param face
	 * @param error
	 * @return
	 */
	public boolean angularTolerance(Triangle face, double error) {
		return (this.angularTolerance(face.normal, error));
	}

	// LOOK : Caution : this is not in degrees !
	// The factor is between 0 and 1.
	// TODO : normalize the vector before the compute.
	/**
	 * @param normal
	 * @param error
	 * @return
	 */
	public boolean isNormalTo(Vector3d normal, double error) {
		return (this.normal.dot(normal) < error && this.normal.dot(normal) > -error);
	}

	// 
	/**
	 * Returns in ret the neighbours of this which belongs to m.
	 * @param ret the returned mesh in which are the neighbours
	 * @param m the mesh which all neighbours have to belong to
	 */
	public void returnNeighbours(Mesh ret, Mesh m) {
		ret.add(this);

		Triangle other;

		for (Edge e : this.edges) {
			other = e.returnOther(this);
			if (other != null && m.contains(other) && !ret.contains(other))
				other.returnNeighbours(ret, m);
		}
	}

	/**
	 * @return
	 */
	public ArrayList<Triangle> getNeighbours() {
		ArrayList<Triangle> l = new ArrayList<Triangle>();
		Triangle other;

		for (Edge e : this.edges) {
			other = e.returnOther(this);
			if (other != null)
				l.add(other);
		}

		return l;
	}

	/**
	 * @param f
	 * @return
	 */
	public boolean isNeighboor(Triangle f) {
		return (this.getNeighbours().contains(f));
	}

	/**
	 * @return
	 */
	public int getNumNeighbours() {
		return this.getNeighbours().size();
	}
}