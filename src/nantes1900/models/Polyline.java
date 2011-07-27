package nantes1900.models;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Edge.BadFormedPolylineException;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;

/**
 * Implement a polyline : a closed suite of points.
 * 
 * @author Daniel Lefevre
 */
public class Polyline {

	private ArrayList<Point> pointList = new ArrayList<Point>();
	private ArrayList<Edge> edgeList = new ArrayList<Edge>();

	private Vector3d normal = new Vector3d();

	private ArrayList<Polyline> neighbours = new ArrayList<Polyline>();

	private final int ID;
	private static int ID_current = 0;

	/**
	 * Void constructor
	 */
	public Polyline() {
		this.ID = ++ID_current;
	}

	/**
	 * Constructor from a list of edges
	 * 
	 * @param the
	 *            list of edges
	 */
	public Polyline(List<Edge> a) {
		for (Edge e : a) {
			this.add(e);
			this.add(e.getP1());
			this.add(e.getP2());
		}

		this.ID = ++ID_current;
	}

	/**
	 * Copy constructor. Caution : this constructor make a copy of the points,
	 * then after this method will exist duplicates with same values and
	 * different references.
	 * 
	 * @param p
	 *            the polyline to copy
	 */
	public Polyline(Polyline p) {
		for (Edge e : p.edgeList) {
			this.add(new Edge(e));
			this.add(e.getP1());
			this.add(e.getP2());
		}

		for (Point point : p.pointList) {

			ArrayList<Edge> belongings = new ArrayList<Edge>();

			for (Edge e : this.edgeList) {
				if (e.contains(point))
					belongings.add(e);
			}

			Point copy = new Point(point);
			this.pointList.remove(point);
			this.pointList.add(copy);

			for (Edge e : belongings) {
				if (e.getP1() == point) {
					e.setP1(copy);
				} else if (e.getP2() == point) {
					e.setP2(copy);
				}
			}
		}

		this.ID = ++ID_current;
	}

	/**
	 * Add an edge. Add the edge only if it is not already contained, and add
	 * the points with the method add(Point)
	 * 
	 * @param e
	 *            the edge to add
	 */
	public void add(Edge e) {
		if (!this.edgeList.contains(e)) {
			this.edgeList.add(e);
		}
		this.add(e.getP1());
		this.add(e.getP2());
	}

	/**
	 * Add a point. Add the point only if it is not already contained
	 * 
	 * @param p
	 *            the point to add
	 */
	public void add(Point p) {
		if (!this.pointList.contains(p))
			this.pointList.add(p);
	}

	/**
	 * Add all the edges of the list.
	 * 
	 * @param l
	 *            the list
	 */
	public void addAll(List<Edge> l) {
		for (Edge e : l) {
			this.add(e);
		}
	}

	/**
	 * Add a neighbours to the attribute neighbours. Check if it is not
	 * contained.
	 * 
	 * @param p
	 *            the polyline as neighbour to add
	 */
	public void addNeighbour(Polyline p) {
		if (!this.neighbours.contains(p))
			this.neighbours.add(p);
	}

	/**
	 * Apply the base change to all the points contained, without changing the
	 * references. Caution : this method changes the hashCode, then be careful
	 * with the hashTables which contains points
	 * 
	 * @param matrix
	 *            the change base matrix
	 */
	public void changeBase(double[][] matrix) {
		if (matrix == null) {
			throw new InvalidParameterException();
		}

		for (Point p : this.pointList) {
			p.changeBase(matrix);
		}
	}

	/**
	 * Clear the edges and the points of the polyline.
	 */
	public void clear() {
		this.edgeList.clear();
		this.pointList.clear();
	}

	/**
	 * Check if e is contained in the polyline
	 * 
	 * @param e
	 *            the edge to check
	 * @return true if it is contained and false otherwise
	 */
	public boolean contains(Edge e) {
		return this.edgeList.contains(e);
	}

	/**
	 * Check if p is contained in the polyline
	 * 
	 * @param p
	 *            the point to check
	 * @return true if it is contained and false otherwise
	 */
	public boolean contains(Point p) {
		return this.pointList.contains(p);
	}

	/**
	 * Returns the size of the edge list
	 * 
	 * @return the size of the edge list
	 */
	public int edgeSize() {
		return this.edgeList.size();
	}

	/**
	 * Build a cylinder, with the edge e as central axe, with error as radius,
	 * and framed into the two points of the edge, and select only the points of
	 * this which are inside.
	 * 
	 * @param e
	 *            the edge which will be the axe, and the two points will close
	 *            the cylinder
	 * @param error
	 *            the radius
	 * @return a list of points which are inside the cylinder
	 */
	public ArrayList<Point> getCylinder(Edge e, double error) {
		ArrayList<Point> ret = new ArrayList<Point>();

		for (Point p : this.pointList) {
			if (e.isInCylinder3D(p, error)) {
				ret.add(p);
			}
		}

		return ret;
	}

	/**
	 * Getter
	 * 
	 * @return the list of edges
	 */
	public ArrayList<Edge> getEdgeList() {
		return this.edgeList;
	}

	/**
	 * Getter
	 * 
	 * @return the ID
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Getter
	 * 
	 * @return the neighbours
	 */
	public ArrayList<Polyline> getNeighbours() {
		return this.neighbours;
	}

	/**
	 * Returns the edges contained in this that contain the point p
	 * 
	 * @param p
	 *            the point considered
	 * @return the edges contained in this that contain the point p
	 */
	public ArrayList<Edge> getNeighbours(Point p) {
		if (p == null) {
			throw new InvalidParameterException();
		}
		ArrayList<Edge> list = new ArrayList<Edge>();
		for (Edge e : this.edgeList) {
			if (e.contains(p)) {
				list.add(e);
			}
		}
		return list;
	}

	/**
	 * Getter
	 * 
	 * @return the normal
	 */
	public Vector3d getNormal() {
		return normal;
	}

	/**
	 * Returns the number of edges contained in this that contain the point p.
	 * 
	 * @param p
	 *            the point considered
	 * @return the number of edges contained in this that contain the point p
	 */
	public int getNumNeighbours(Point p) {
		int counter = 0;
		for (Edge e : this.edgeList) {
			if (e.contains(p)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Return one edge of the list
	 * 
	 * @return one edge of the list Use iterator().next()
	 */
	public Edge getOne() {
		return this.edgeList.iterator().next();
	}

	/**
	 * Getter
	 * 
	 * @return the list of points
	 */
	public ArrayList<Point> getPointList() {
		return this.pointList;
	}

	/**
	 * Convert the list of points in a list of coordinates as doubles.
	 * 
	 * @return a list of double as coordinates.
	 */
	public List<Double> getPointsAsCoordinates() {
		ArrayList<Double> list = new ArrayList<Double>();
		for (Point p : this.pointList) {
			for (double d : p.getPointAsCoordinates()) {
				list.add(new Double(d));
			}
		}
		return list;
	}

	/**
	 * Check if the edge list is empty
	 * 
	 * @return true if it's empty, false otherwise
	 */
	public boolean isEmpty() {
		return edgeList.isEmpty();
	}

	/**
	 * Check if the two polylines has at least one point of one close to one
	 * point of the other.
	 * 
	 * @param p
	 *            the polyline to search in
	 * @return true if one point is find close to another of the other polyline,
	 *         false otherwise
	 */
	public boolean isNeighbour(Polyline p) {
		if (p == this) {
			return false;
		}
		for (Edge e1 : this.edgeList) {
			if (p.contains(e1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Compute the length of the polyline
	 * 
	 * @return the sum of all the edges that compose the polyline
	 */
	public double length() {
		double length = 0;
		for (Edge e : this.edgeList) {
			length += e.length();
		}
		return length;
	}

	/**
	 * Returns the average of the length of all the edges
	 * 
	 * @return the average of the length of all the edges
	 */
	public double lengthAverage() {
		return Math.pow(
				Math.pow(this.xLengthAverage(), 2)
						+ Math.pow(this.yLengthAverage(), 2)
						+ Math.pow(this.zLengthAverage(), 2), 0.5);
	}

	/**
	 * Order the polyline. Each edge in the edge list will be surrounded by its
	 * neighbours
	 */
	public void order() {
		if (!this.isEmpty()) {
			Polyline ret = new Polyline();

			try {

				Edge first = this.getOne();
				Point p = first.getP1();
				Edge e = first.returnNeighbour(this, p);
				p = e.returnOther(p);

				while (e != first) {
					ret.add(e);
					e = e.returnNeighbour(this, p);
					p = e.returnOther(p);
				}
				ret.add(e);

				this.edgeList.clear();
				this.pointList.clear();
				this.addAll(ret.getEdgeList());

			} catch (BadFormedPolylineException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Return the edges that are oriented as the edge e, with an orientation
	 * error.
	 * 
	 * @param e
	 *            the edge to compare with
	 * @param error
	 *            the orientation error
	 * @return the polyline containing all those edges
	 */
	public Polyline orientedAs(Edge e, double error) {
		Polyline ret = new Polyline();

		for (Edge edge : this.edgeList) {
			if (edge.orientedAs(e, error)) {
				ret.add(edge);
			}
		}

		return ret;
	}

	/**
	 * Returns the size of the point list
	 * 
	 * @return the size of the point list
	 */
	public int pointSize() {
		return this.pointList.size();
	}

	public void refresh() {
		ArrayList<Edge> edges = new ArrayList<Edge>(this.getEdgeList());
		this.clear();
		this.addAll(edges);
	}

	/**
	 * Remove the occurences of e contained in this
	 * 
	 * @param e
	 *            the edge to remove Caution : it doesn't remove the points
	 */
	public void remove(Edge e) {
		ArrayList<Edge> edges = new ArrayList<Edge>(this.edgeList);
		edges.remove(e);
		this.clear();
		this.addAll(edges);
	}

	/**
	 * Remove the edges of del contained in this. Caution : it doesn't remove
	 * the points. This method uses the remove(Edge) method
	 * 
	 * @param p
	 *            the list of edges to remove
	 */
	public void remove(Polyline p) {
		ArrayList<Edge> edges = new ArrayList<Edge>(this.edgeList);
		for (Edge e : p.edgeList) {
			edges.remove(e);
		}
		this.clear();
		this.addAll(edges);
	}

	/**
	 * Return a mesh composed of the triangles formed by each edges and the
	 * point centroid of the polyline. This is not really beautiful at the end,
	 * but it's enough for debugging.
	 * 
	 * @return a mesh representing the surface of the polyline
	 */
	public Mesh returnCentroidMesh() {
		Mesh ens = new Mesh();

		Point centroid = new Point(this.xAverage(), this.yAverage(),
				this.zAverage());
		Vector3d normal = new Vector3d(0, 0, -1);

		Point before = this.pointList.get(this.pointSize() - 1);

		for (Point p : this.pointList) {
			ens.add(new Triangle(before, centroid, p,
					new Edge(centroid, before), new Edge(before, p), new Edge(
							p, centroid), normal));
			before = p;
		}

		return ens;
	}

	/**
	 * Returns the mesh composed all the triangles the edges belong to and which
	 * belong to the mesh m. If one edge in this doesn't have one triangle to
	 * return, this method returns the returnCentroidMesh()
	 * 
	 * @return the mesh composed all the triangles the edges belong to
	 */
	public Mesh returnExistingMesh(Mesh m) {

		// If there is Edges which have not triangles associated, the method
		// calls the returnCentroidMesh.
		for (Edge e : this.edgeList) {
			if (e.getNumberTriangles() == 0) {
				return this.returnCentroidMesh();
			}
		}

		// Find every triangles which belong to the edges, and which belong to m
		// too.
		Mesh ens = new Mesh();
		for (Edge e : this.edgeList) {
			for (Triangle t : e.getTriangleList()) {
				if (m.contains(t)) {
					ens.add(t);
				}
			}
		}
		return ens;
	}

	/**
	 * Setter
	 * 
	 * @param normal
	 *            the normal
	 */
	public void setNormal(Vector3d normal) {
		this.normal = normal;
	}

	/**
	 * Write the mesh returned by returnCentroidMesh.
	 * 
	 * @param string
	 *            the name of the file to write in
	 */
	public void writeCentroidMesh(String string) {
		this.returnCentroidMesh().writeSTL(string);
	}

	/**
	 * Returns the average of the x coordinate of all the points
	 * 
	 * @return the average of the x coordinate of all the points
	 */
	public double xAverage() {
		double xAverage = 0;
		for (Point p : this.pointList) {
			xAverage += p.getX();
		}
		return xAverage / this.pointList.size();
	}

	/**
	 * Returns the polyline composed by the edges contained in this which the x
	 * coordinates are in the bounds m1 and m2
	 * 
	 * @param m1
	 *            one bound
	 * @param m2
	 *            the other bound
	 * @return the polyline composed by the edges contained in this which the x
	 *         coordinates are in the bounds m1 and m2
	 */
	public Polyline xBetween(double m1, double m2) {
		Polyline b = new Polyline();
		for (Edge e : this.edgeList) {
			if (e.getP1().getX() > Math.min(m1, m2)
					&& e.getP1().getX() < Math.max(m1, m2)
					&& e.getP2().getX() > Math.min(m1, m2)
					&& e.getP2().getX() < Math.max(m1, m2))
				b.add(e);
		}
		return b;
	}

	/**
	 * Returns the average of the length on the x axis of all the edges
	 * 
	 * @return the average of the length on the x axis of all the edges
	 */
	public double xLengthAverage() {
		double xLengthAve = 0;
		for (Edge e : this.edgeList) {
			xLengthAve += Math.abs(e.getP1().getX() - e.getP2().getX());
		}
		return xLengthAve / (double) this.edgeList.size();
	}

	/**
	 * Returns the maximum of the x coordinate
	 * 
	 * @return the maximum of the x coordinate
	 */
	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getX() > xMaxi) {
				xMaxi = p.getX();
			}
		}
		return xMaxi;
	}

	/**
	 * Returns the minimum of the x coordinate
	 * 
	 * @return the minimum of the x coordinate
	 */
	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getX() < xMini) {
				xMini = p.getX();
			}
		}
		return xMini;
	}

	/**
	 * Returns the average of the y coordinate all of the points
	 * 
	 * @return the average of the y coordinate all of the points
	 */
	public double yAverage() {
		double yAverage = 0;
		for (Point p : this.pointList) {
			yAverage += p.getY();
		}
		return yAverage / this.pointList.size();
	}

	/**
	 * Returns the polyline composed by the edges contained in this which the y
	 * coordinates are in the bounds m1 and m2
	 * 
	 * @param m1
	 *            one bound
	 * @param m2
	 *            the other bound
	 * @return the polyline composed by the edges contained in this which the y
	 *         coordinates are in the bounds m1 and m2
	 */
	public Polyline yBetween(double m1, double m2) {
		Polyline b = new Polyline();
		for (Edge e : this.edgeList) {
			if (e.getP1().getY() > Math.min(m1, m2)
					&& e.getP1().getY() < Math.max(m1, m2)
					&& e.getP2().getY() > Math.min(m1, m2)
					&& e.getP2().getY() < Math.max(m1, m2))
				b.add(e);
		}
		return b;
	}

	/**
	 * Returns the average of the length on the y axis of all the edges
	 * 
	 * @return the average of the length on the y axis of all the edges
	 */
	public double yLengthAverage() {
		double yLengthAve = 0;
		for (Edge e : this.edgeList) {
			yLengthAve += Math.abs(e.getP1().getY() - e.getP2().getY());
		}
		return yLengthAve / (double) this.edgeList.size();
	}

	/**
	 * Returns the maximum of the y coordinate
	 * 
	 * @return the maximum of the y coordinate
	 */
	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getY() > yMaxi) {
				yMaxi = p.getY();
			}
		}
		return yMaxi;
	}

	/**
	 * Returns the minimum of the y coordinate
	 * 
	 * @return the minimum of the y coordinate
	 */
	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getY() < yMini) {
				yMini = p.getY();
			}
		}
		return yMini;
	}

	/**
	 * Returns the average of the z coordinate all of the points
	 * 
	 * @return the average of the z coordinate all of the points
	 */
	public double zAverage() {
		double zAverage = 0;
		for (Point p : this.pointList) {
			zAverage += p.getZ();
		}
		return zAverage / this.pointList.size();
	}

	/**
	 * Returns the polyline composed by the edges contained in this which the z
	 * coordinates are in the bounds m1 and m2
	 * 
	 * @param m1
	 *            one bound
	 * @param m2
	 *            the other bound
	 * @return the polyline composed by the edges contained in this which the z
	 *         coordinates are in the bounds m1 and m2
	 */
	public Polyline zBetween(double m1, double m2) {
		Polyline b = new Polyline();
		for (Edge e : this.edgeList) {
			if (e.getP1().getZ() > Math.min(m1, m2)
					&& e.getP1().getZ() < Math.max(m1, m2)
					&& e.getP2().getZ() > Math.min(m1, m2)
					&& e.getP2().getZ() < Math.max(m1, m2))
				b.add(e);
		}
		return b;
	}

	/**
	 * Returns the average of the length on the z axis of all the edges
	 * 
	 * @return the average of the length on the z axis of all the edges
	 */
	public double zLengthAverage() {
		double zLengthAve = 0;
		for (Edge e : this.edgeList) {
			zLengthAve += Math.abs(e.getP1().getZ() - e.getP2().getZ());
		}
		return zLengthAve / (double) this.edgeList.size();
	}

	/**
	 * Returns the maximum of the z coordinate
	 * 
	 * @return the maximum of the z coordinate
	 */
	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getZ() > zMaxi) {
				zMaxi = p.getZ();
			}
		}
		return zMaxi;
	}

	/**
	 * Returns the point of this which has the maximum z coordinate
	 * 
	 * @return the point of this which has the maximum z coordinate
	 */
	public Point zMaxPoint() {
		Point point = null;
		if (this.pointList.isEmpty())
			throw new InvalidParameterException("Empty border !");
		double zMax = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getZ() > zMax) {
				zMax = p.getZ();
				point = p;
			}
		}
		return point;
	}

	/**
	 * Returns the minimum of the z coordinate
	 * 
	 * @return the minimum of the z coordinate
	 */
	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Point p : this.pointList) {
			if (p.getZ() < zMini) {
				zMini = p.getZ();
			}
		}
		return zMini;
	}

	/**
	 * Return a polyline that is the copy of this, but where all points have the
	 * same z. Caution : it modifies the points, then it must be a copy of the
	 * points. Otherwise, the Mesh containing these points, and using the
	 * hashCode will be lost (because the hashCode of the points would have been
	 * modified without the hash table of the mesh refreshed).
	 * 
	 * @param z
	 *            the value to project on
	 * @return a polyline that is the copy of this, but where all points have
	 *         the same z
	 */
	public void zProjection(double z) {
		for (Point p : this.pointList) {
			p.setZ(z);
		}
	}
}