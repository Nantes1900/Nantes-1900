package nantes1900.models.basis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector3d;

import nantes1900.models.Mesh;


/**
 * Implement a triangle, composed of three points, three edges, and one vector
 * as a normal
 * 
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 */
public class Triangle {

	private Point[] points = new Point[3];
	private Vector3d normal = new Vector3d();;
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
	public Triangle(Triangle t) {
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
	 * Returns a collection of the coordinates of the points.
	 * 
	 * @return a collection of the coordinates of the points
	 */
	public List<Double> getPointsAsCoordinates() {
		ArrayList<Double> list = new ArrayList<Double>();
		for (Point p : points) {
			for (double d : p.getPointAsCoordinates()) {
				list.add(new Double(d));
			}
		}
		return list;
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
	 * Compute the average x-coordinate of the three points.
	 * 
	 * @return the average x-coordinate of the three points
	 */
	public double xAverage() {
		double xAverage = points[0].getX() + points[1].getX()
				+ points[2].getX();
		return xAverage / 3;
	}

	/**
	 * Compute the average y-coordinate of the three points.
	 * 
	 * @return the average y-coordinate of the three points
	 */
	public double yAverage() {
		double yAverage = points[0].getY() + points[1].getY()
				+ points[2].getY();
		return yAverage / 3;
	}

	/**
	 * Compute the average z-coordinate of the three points.
	 * 
	 * @return the average z-coordinate of the three points
	 */
	public double zAverage() {
		double zAverage = points[0].getZ() + points[1].getZ()
				+ points[2].getZ();
		return zAverage / 3;
	}

	/**
	 * Compute the x-minimum of the three points.
	 * 
	 * @return the x-minimum of the three points
	 */
	public double xMin() {
		return Math.min(points[0].getX(),
				Math.min(points[1].getX(), points[2].getX()));
	}

	/**
	 * Compute the x-maximum of the three points.
	 * 
	 * @return the x-maximum of the three points
	 */
	public double xMax() {
		return Math.max(points[0].getX(),
				Math.max(points[1].getX(), points[2].getX()));
	}

	/**
	 * Compute the y-minimum of the three points.
	 * 
	 * @return the y-minimum of the three points
	 */
	public double yMin() {
		return Math.min(points[0].getY(),
				Math.min(points[1].getY(), points[2].getY()));
	}

	/**
	 * Compute the y-maximum of the three points.
	 * 
	 * @return the y-maximum of the three points
	 */
	public double yMax() {
		return Math.max(points[0].getY(),
				Math.max(points[1].getY(), points[2].getY()));
	}

	/**
	 * Compute the z-minimum of the three points.
	 * 
	 * @return the z-minimum of the three points
	 */
	public double zMin() {
		return Math.min(points[0].getZ(),
				Math.min(points[1].getZ(), points[2].getZ()));
	}

	/**
	 * Compute the z-maximum of the three points.
	 * 
	 * @return the z-maximum of the three points
	 */
	public double zMax() {
		return Math.max(points[0].getZ(),
				Math.max(points[1].getZ(), points[2].getZ()));
	}

	/**
	 * Compute the point at the x-minimum of the three points.
	 * 
	 * @return the point at the x-minimum of the three points.
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
	 * Compute the point at the x-maximum of the three points.
	 * 
	 * @return the point at the x-maximum of the three points.
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
	 * Compute the point at the y-minimum of the three points.
	 * 
	 * @return the point at the y-minimum of the three points.
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
	 * Compute the point at the y-maximum of the three points.
	 * 
	 * @return the point at the y-maximum of the three points.
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
	 * Compute the point at the z-minimum of the three points.
	 * 
	 * @return the point at the z-minimum of the three points.
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
	 * Compute the point at the z-maximum of the three points.
	 * 
	 * @return the point at the z-maximum of the three points.
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
	 * Check if the this face is oriented as vector with an error on the angle.
	 * The error is in degree.
	 * 
	 * @param vector
	 *            the vector to compare with
	 * @param error
	 *            the orientation error
	 * @return true if it is oriented as vector, false otherwise
	 */
	public boolean angularTolerance(Vector3d vector, double error) {
		return (this.normal.angle(vector) * 180.0 / Math.PI < error);
	}

	/**
	 * Check if the this face is oriented as face with an error on the angle.
	 * The error is in degree.
	 * 
	 * @param face
	 *            the other triangle to compare with
	 * @param error
	 *            the orientation error
	 * @return true if it is oriented as face, false otherwise
	 */
	public boolean angularTolerance(Triangle face, double error) {
		return (this.angularTolerance(face.normal, error));
	}

	/**
	 * Check if norm is normal to the normal of this triangle, with an error.
	 * Caution : this error is not in degrees ! The error is compared with the
	 * result of a dot product. Then this error must be between 0 and 1.
	 * 
	 * @param norm
	 *            the vector to compare
	 * @param error
	 *            the error of orientation
	 * @return true if this triangle is oriented normal to norm with an error,
	 *         false otherwise.
	 */
	public boolean isNormalTo(Vector3d norm, double error) {
		return (this.normal.dot(norm) < error && this.normal.dot(norm) > -error);
	}

	/**
	 * Returns in ret the neighbours of this which belongs to m.
	 * 
	 * @param ret
	 *            the returned mesh in which are the neighbours
	 * @param m
	 *            the mesh which all neighbours have to belong to
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
	 * Return the neighbours of this triangle. Look in the edges to find the
	 * other triangles which share those edges.
	 * 
	 * @return a list of the neighbours triangles
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
	 * Check if a triangle is a neighbours of this. This method calls the
	 * getNeighbours method.
	 * 
	 * @param f
	 *            the triangle to check
	 * @return true if it is neighbours, false otherwise.
	 */
	public boolean isNeighboor(Triangle f) {
		return (this.getNeighbours().contains(f));
	}

	/**
	 * Return the number of neighbours of this triangle.
	 * 
	 * @return the number of neighbours
	 */
	public int getNumNeighbours() {
		return this.getNeighbours().size();
	}

	public boolean contains(Edge e) {
		if (this.getEdges().contains(e))
			return true;
		else
			return false;
	}
}