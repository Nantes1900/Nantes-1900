package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

/**
 * Implement an edge : two points, and the triangles it belongs to.
 * 
 * @author Daniel Lefevre
 */
public class Edge {

	private ArrayList<Triangle> triangleList = new ArrayList<Triangle>(2);

	private Point[] points = new Point[2];

	/**
	 * Constructor
	 * 
	 * @param p1
	 *            the first point
	 * @param p2
	 *            the second point
	 */
	public Edge(Point p1, Point p2) {
		this.points[0] = p1;
		this.points[1] = p2;
	}

	// FIXME
	public Edge(Edge e) {
		this.setP1(e.getP1());
		this.setP2(e.getP2());
		this.triangleList = new ArrayList<Triangle>(e.triangleList);
	}

	/**
	 * Getter
	 * 
	 * @return the list of triangles the edge belongs to
	 */
	public ArrayList<Triangle> getTriangleList() {
		return this.triangleList;
	}

	/**
	 * Return the other triangle this belongs to
	 * 
	 * @param t
	 *            one triangle which contains the edge
	 * @return the other triangle which contains the edge Return an exception if
	 *         the edge is bad-formed : ie it contains more than two triangles
	 */
	public Triangle returnOther(Triangle t) {
		if (this.triangleList.size() > 2)
			// TODO : renvoyer une exception
			System.err.println("Error : more than two triangles per edge !");
		if (!this.triangleList.contains(t))
			throw new InvalidParameterException("t is not part of the Edge !");
		if (this.triangleList.size() < 2)
			return null;
		if (this.triangleList.get(0) == t)
			return this.triangleList.get(1);
		else if (this.triangleList.get(1) == t)
			return this.triangleList.get(0);
		else
			return null;
	}

	/**
	 * Getter
	 * 
	 * @return the first point
	 */
	public Point getP1() {
		return this.points[0];
	}

	/**
	 * Getter
	 * 
	 * @return the second point
	 */
	public Point getP2() {
		return this.points[1];
	}

	/**
	 * Getter
	 * 
	 * @return an ArrayList<Point> with the two points of the edge
	 */
	public ArrayList<Point> getPoints() {
		return new ArrayList<Point>(Arrays.asList(this.points));
	}

	/**
	 * Compute the length of the edge
	 * 
	 * @return the length of the edge
	 */
	public double length() {
		return this.points[0].distance(this.points[1]);
	}

	/**
	 * Contains method
	 * 
	 * @param p
	 *            the point to check
	 * @return true if p is contained, and false otherwise
	 */
	public boolean contains(Point p) {
		return (this.points[0] == p || this.points[1] == p);
	}

	/**
	 * Add a triangle to the edge
	 * 
	 * @param t
	 *            the triangle to add
	 * @throws Exception
	 *             (LOOK) if the edge already contains 2 triangles
	 * 
	 */
	public void addTriangle(Triangle t) {
		if (!this.triangleList.contains(t)) {
			this.triangleList.add(t);
			if (this.triangleList.size() > 2) {
				// Throw an exception and treat it !
				System.err
						.println("Problem in the mesh : more than two triangles for one edge !");
			}
		}
	}

	/**
	 * Return the number of triangles
	 * 
	 * @return the number of triangles
	 */
	public int getNumberTriangles() {
		return this.triangleList.size();
	}

	/**
	 * Check if an edge is a neighboor of this
	 * 
	 * @param e
	 *            the edge to check
	 * @return true if e shares one point with this, false if e is this, and if
	 *         it is not neighboor
	 */
	public boolean isNeighboor(Edge e) {
		if (this == e)
			return false;
		else
			return (this.contains(e.points[0]) || this.contains(e.points[1]));
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
		result = result
				+ ((this.points[0] == null) ? 0 : this.points[0].hashCode());
		result = result
				+ ((this.points[1] == null) ? 0 : this.points[1].hashCode());
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
		Edge e = (Edge) obj;
		return (e.contains(this.points[0]) && e.contains(this.points[1]));
	}

	/**
	 * Recursing method returning a polyline containing all the neighbours which
	 * are contained in p
	 * 
	 * @param ret
	 *            the polyline in which are returned the neighbours
	 * @param p
	 *            the polyline in which the edges have to be //LOOK : fix
	 *            this...
	 */
	public void returnNeighbours(Polyline ret, Polyline p) {
		if (!ret.contains(this)) {
			ret.add(this);
			for (Edge e : p.getEdgeList()) {
				if (this.isNeighboor(e)) {
					e.returnNeighbours(ret, p);
				}
			}
		}
	}

	public Polyline returnOneBound(Mesh m, Polyline p, Point point,
			Vector3d normalFloor) {

		Polyline bound = new Polyline();

		try {
			ArrayList<Edge> edgeList = p.getNeighbours(point);

			if (edgeList.size() < 2) {
				System.err.println("Error in returnOneBound 1 !");
			}

			Edge e;
			e = this.returnNeighbour(point, p, normalFloor);
			e.setP2(e.returnOther(point));
			e.setP1(point);

			bound.add(e);
			point = e.returnOther(point);

			int counter = 1;

			while (e != this) {
				edgeList = p.getNeighbours(point);
				if (!edgeList.contains(e)) {
					System.err.println("Error in returnOneBound 2 !"); // LOOK
					throw new InvalidActivityException();
				} else if (edgeList.size() < 2) {
					System.err.println("Error in returnOneBound 3 !"); // LOOK
					throw new InvalidActivityException();
				} else {
					e = e.returnNeighbour(point, p, normalFloor);

					e.setP2(e.returnOther(point));
					e.setP1(point);

					bound.add(e);
					point = e.returnOther(point);
				}
				if (counter > p.edgeSize()) {
					System.err.println("too much loops !");
					break;
				}
				counter++;
			}
		} catch (InvalidActivityException e1) {
			e1.printStackTrace();
		}
		return bound;
	}

	public void setP1(Point point) {
		this.points[0] = point;

	}

	public void setP2(Point point) {
		this.points[1] = point;

	}

	private Edge returnTheLeftOne(ArrayList<Edge> weirdEdges, Point weirdPoint,
			Vector3d normalFloor) throws InvalidActivityException {

		if (!weirdEdges.contains(this)) {
			// LOOK
			System.err.println("Error in returnTheLeftOne !");
		}

		Vector3d v = new Vector3d();

		v.x = -this.returnOther(weirdPoint).getX() + weirdPoint.getX();
		v.y = -this.returnOther(weirdPoint).getY() + weirdPoint.getY();
		v.z = -this.returnOther(weirdPoint).getZ() + weirdPoint.getZ();

		Vector3d cross = new Vector3d();
		cross.cross(normalFloor, v);

		cross.normalize();

		Edge ref = null;
		double max = Double.NEGATIVE_INFINITY;

		for (Edge edge : weirdEdges) {
			if (edge != this) {
				Vector3d vect = new Vector3d();

				vect.x = edge.returnOther(weirdPoint).getX()
						- weirdPoint.getX();
				vect.y = edge.returnOther(weirdPoint).getY()
						- weirdPoint.getY();
				vect.z = edge.returnOther(weirdPoint).getZ()
						- weirdPoint.getZ();

				vect.normalize();

				if (cross.dot(vect) > max) {
					max = cross.dot(vect);
					ref = edge;
				}
			}
		}

		return ref;
	}

	public String toString() {
		return new String("(" + this.getP1() + ", " + this.getP2() + ")");
	}

	/**
	 * Returns the number of neighbours of this contained in the polyline p
	 * 
	 * @param p
	 *            the polyline in which the edges have to be
	 * @return the number of neighbours //TODO : maybe for this method and
	 *         returnNeighbours, it should be interesting to make an index and
	 *         to search first, or to create references in the class Point...
	 */
	public int getNumNeighbours(Polyline p) {
		if (!p.contains(this)) {
			throw new InvalidParameterException();
		}
		int counter = 0;
		for (Edge e : p.getEdgeList()) {
			if (this.isNeighboor(e)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Return the other point of the edge
	 * 
	 * @param p
	 *            one point of the edge
	 * @return the other point which forms the edge
	 * @throws Exception
	 */
	public Point returnOther(Point p) {
		if (this.getP1() == p)
			return this.getP2();
		else if (this.getP2() == p)
			return this.getP1();
		else {
			throw new InvalidParameterException();
		}
	}

	/**
	 * Returns the edge neighbour of this which contains p and which belongs to
	 * b
	 * 
	 * @param p
	 *            the point shared by the two edges
	 * @param b
	 *            the poliyline in which must be the edge returned
	 * @return the edge belonging to b which contains p
	 * @throws InvalidActivityException
	 */
	// TODO : @Test
	public Edge returnNeighbour(Point p, Polyline b, Vector3d normalFloor)
			throws InvalidActivityException {

		if (!b.contains(this)) {
			throw new InvalidParameterException();
		}

		ArrayList<Edge> list = b.getNeighbours(p);

		if (list.size() == 4) {
			return this.returnTheLeftOne(b.getNeighbours(p), p, normalFloor);
		} else if (list.size() < 2 || list.size() > 4 || list.size() == 3) {
			System.err.println("Error in returnNeighbour !");
			// FIXME : throw exc !
			throw new InvalidActivityException();
		} else {
			list.remove(this);
			return list.get(0);
		}
	}

	/**
	 * Create another edge with the opposite points of the two parameters
	 * 
	 * @param eAdd
	 *            - the edge to compose with
	 * @param p
	 *            the point the two edges share
	 * @return a new edge formed with the opposite points of the two parameters
	 */
	// TODO : remove the parameter p : it's easy to find
	public Edge compose(Edge eAdd, Point p) {
		return new Edge(this.returnOther(p), eAdd.returnOther(p));
	}

	// Error in degrees !
	public boolean orientedAs(Edge e, double error) {
		Vector3d vect1 = new Vector3d(e.getP2().getX() - e.getP1().getX(), e
				.getP2().getY() - e.getP1().getY(), e.getP2().getZ()
				- e.getP1().getZ());
		vect1.normalize();
		Vector3d vect2 = new Vector3d(
				this.getP2().getX() - this.getP1().getX(), this.getP2().getY()
						- this.getP1().getY(), this.getP2().getZ()
						- this.getP1().getZ());
		vect2.normalize();
		return (vect1.angle(vect2) < ((error / 180) * Math.PI) || vect1
				.angle(vect2) > (((180 - error) / 180) * Math.PI));
	}

	public double minDistance(Edge edge) {
		double min = Double.POSITIVE_INFINITY;

		for (Point p1 : edge.points) {
			for (Point p2 : this.points) {
				if (p1.distance(p2) < min) {
					min = p1.distance(p2);
				}
			}
		}

		return min;
	}

	public Point sharedPoint(Edge e) throws Exception {
		if (this.contains(e.getP1())) {
			return e.getP1();
		} else if (this.contains(e.getP2())) {
			return e.getP2();
		} else {
			throw new Exception();
		}
	}

	public Point project(Point point) {

		Point p1 = this.getP1(), p2 = this.getP2();
		Point p3 = point;

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
