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

	public void returnOneBound(Mesh m, Polyline ret, Polyline p, Point point,
			Vector3d normalFloor) throws InvalidActivityException {
		if (!ret.contains(this)) {
			ret.add(this);

			ArrayList<Edge> edgeList = p.getNeighbours(point);
			if (!edgeList.contains(this)) {
				System.err.println("Enormous error 1 !");
			} else if (edgeList.size() == 4) {
				Edge e = this.returnTheGoodOne(m, p.getNeighbours(point),
						point, normalFloor);
				e.returnOneBound(m, ret, p, e.returnOther(point), normalFloor);
			} else if (edgeList.size() == 2) {
				Edge e = this.returnNeighbour(point, p);
				e.returnOneBound(m, ret, p, e.returnOther(point), normalFloor);
			} else {
				System.err.println("pb !");
			}
		}
	}

	private Edge returnTheGoodOne(Mesh m, ArrayList<Edge> weirdEdges,
			Point weirdPoint, Vector3d normalFloor)
			throws InvalidActivityException {

		if (!weirdEdges.contains(this)) {
			System.err.println("Big mistake 2!");
		}

		// TODO : si un triangle de ces 4 là possède un triangle qui n'est pas
		// dans m en commun avec un autre, alors il sont de la même polyline.

		ArrayList<Edge> weirdEdges1 = new ArrayList<Edge>(weirdEdges);
		ArrayList<Edge> weirdEdges2 = new ArrayList<Edge>();

		if (weirdEdges1.size() != 4) {
			System.err.println("Error in the number of weirdEdges !");
		} else {
			Edge e1 = weirdEdges1.get(0);
			Edge e2 = weirdEdges1.get(1);
			Edge e3 = weirdEdges1.get(2);
			Edge e4 = weirdEdges1.get(3);

			Triangle t1 = null;
			int counter = 0;
			for (Triangle t : e1.getTriangleList()) {
				if (m.contains(t)) {
					t1 = t;
					counter++;
				}
			}
			if (counter != 1)
				System.err.println("Error 32!");

			weirdEdges2.add(e1);
			weirdEdges1.remove(e1);

			if (e2.getTriangleList().contains(t1)) {
				weirdEdges2.add(e2);
				weirdEdges1.remove(e2);
			} else if (e3.getTriangleList().contains(t1)) {
				weirdEdges2.add(e3);
				weirdEdges1.remove(e3);
			} else if (e4.getTriangleList().contains(t1)) {
				weirdEdges2.add(e4);
				weirdEdges1.remove(e4);
			} else {
				System.err.println("Re big !");
			}
		}

		// We know that in weirdEdges1 are two edges which share one triangle,
		// and the same in weirdEdges2. Now we have to find which one is with
		// which other...

		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();
		Vector3d v3 = new Vector3d();
		Vector3d v4 = new Vector3d();

		Edge e1 = weirdEdges1.get(0);
		Edge e2 = weirdEdges1.get(1);

		v1.x = e1.returnOther(weirdPoint).getX() - weirdPoint.getX();
		v1.y = e1.returnOther(weirdPoint).getY() - weirdPoint.getY();
		v1.z = e1.returnOther(weirdPoint).getZ() - weirdPoint.getZ();

		v2.x = e2.returnOther(weirdPoint).getX() - weirdPoint.getX();
		v2.y = e2.returnOther(weirdPoint).getY() - weirdPoint.getY();
		v2.z = e2.returnOther(weirdPoint).getZ() - weirdPoint.getZ();

		Edge e3 = weirdEdges2.get(0);
		Edge e4 = weirdEdges2.get(1);

		v3.x = e3.returnOther(weirdPoint).getX() - weirdPoint.getX();
		v3.y = e3.returnOther(weirdPoint).getY() - weirdPoint.getY();
		v3.z = e3.returnOther(weirdPoint).getZ() - weirdPoint.getZ();

		v4.x = e4.returnOther(weirdPoint).getX() - weirdPoint.getX();
		v4.y = e4.returnOther(weirdPoint).getY() - weirdPoint.getY();
		v4.z = e4.returnOther(weirdPoint).getZ() - weirdPoint.getZ();

		v1.normalize();
		v2.normalize();
		v3.normalize();
		v4.normalize();

		weirdEdges1.clear();
		weirdEdges1.add(e1);

		weirdEdges2.clear();
		weirdEdges2.add(e2);

		Vector3d cross = new Vector3d();
		cross.cross(normalFloor, v1);

		if (cross.dot(v2) > 0) {
			// It means that seeing from the weirdPoint (middle point), e2 is at
			// the left of e1.
			// Thus we try to find the first edge at the right of e1.
			if (cross.dot(v3) < cross.dot(v4)) {
				weirdEdges1.add(e3);
				weirdEdges2.add(e4);
			} else {
				weirdEdges1.add(e4);
				weirdEdges2.add(e3);
			}

		} else {
			// e2 is at the right. Find the first at the left of e1.
			if (cross.dot(v3) > cross.dot(v4)) {
				weirdEdges1.add(e3);
				weirdEdges2.add(e4);
			} else {
				weirdEdges1.add(e4);
				weirdEdges2.add(e3);
			}
		}

		if (weirdEdges1.contains(this)) {
			weirdEdges1.remove(this);
			return weirdEdges1.get(0);
		} else if (weirdEdges2.contains(this)) {
			weirdEdges2.remove(this);
			return weirdEdges2.get(0);
		} else {
			throw new InvalidActivityException();
		}
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
	// LOOK
	public Point returnOther(Point p) {
		if (this.getP1() == p)
			return this.getP2();
		else if (this.getP2() == p)
			return this.getP1();
		else {
			return null;
			// TODO : throw Ex !
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
	 * @throws Exception
	 *             if there is more than two edges containing the same point
	 */
	// TODO : @Test
	public Edge returnNeighbour(Point p, Polyline b) {
		if (!b.contains(this)) {
			throw new InvalidParameterException();
		}
		ArrayList<Edge> list = b.getNeighbours(p);
		if (list.size() != 2) {
			System.err.println("Error in returnNeighbours!");
			// throw new InvalidParameterException();
		}
		// else {
		list.remove(this);
		return list.get(0);
		// }
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

	// /**
	// * Recursing method returning a polyline containing all the neighbours
	// which
	// * are contained in p
	// *
	// * @param ret
	// * the polyline in which are returned the neighbours
	// * @param p
	// * the polyline in which the edges have to be //LOOK : fix
	// * this...
	// */
	// public Edge returnNeighbours(Polyline bound, ArrayList<Edge> stop) {
	// Polyline ret = new Polyline();
	//
	// for (Edge e : bound.getEdgeList()) {
	// this.returnNeighbours(ret, bound);
	// for (Edge edge : ret.getEdgeList()) {
	// if (stop.contains(e)) {
	// return e;
	// } else {
	// return edge.returnNeighbours(bound, stop);
	// }
	// }
	// }
	// }
}
