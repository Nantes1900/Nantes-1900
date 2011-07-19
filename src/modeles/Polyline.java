package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

/**
 * Implement a polyline : a closed suite of points.
 * 
 * @author Daniel Lefevre
 */
public class Polyline {

	private ArrayList<Point> pointList = new ArrayList<Point>();
	private ArrayList<Edge> edgeList = new ArrayList<Edge>();

	private final int ID;

	private static int ID_current = 0;
	private static final long serialVersionUID = 1L;

	// FIXME : :)
	public Point toDelete;

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
	 * Void constructor
	 */
	public Polyline() {
		this.ID = ++ID_current;
	}

	// FIXME : NEVER USE IT
	public Polyline(Polyline p) throws InvalidActivityException {
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

			// if(belongings.size() > 2) {
			// System.err.println("Error !");
			// throw new InvalidActivityException();
			// }

			Point copy = new Point(point);
			this.pointList.remove(point);
			this.pointList.add(copy);
			for (Edge e : belongings) {
				if (e.getP1() == point) {
					e.setP1(copy);
				} else if (e.getP2() == point) {
					e.setP2(copy);
				} else {
					System.err.println("Error !");
					throw new InvalidActivityException();
				}
			}
		}

		this.ID = ++ID_current;
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
	 * @return the list of points
	 */
	public ArrayList<Point> getPointList() {
		return this.pointList;
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
	 * Add all the edges of the list
	 * 
	 * @param l
	 *            the list
	 */
	// DOESNT WORK ! FIXME !
	// public void addAll(List<Edge> l) {
	// for (Edge e : l) {
	// this.add(e);
	// }
	// }

	/**
	 * Return one edge of the list
	 * 
	 * @return one edge of the list Use iterator().next()
	 */
	public Edge getOne() {
		return this.edgeList.iterator().next();
	}

	// FIXME : it doesn't remove the points !
	/**
	 * Remove the edges of del contained in this. Caution : it doesn't remove
	 * the points Use the remove(Edge) method
	 * 
	 * @param del
	 *            the list of edges to remove
	 */
	public void remove(Polyline del) {
		for (Edge e : del.edgeList) {
			this.edgeList.remove(e);
		}
	}

	// FIXME : it doesn't remove the points !
	/**
	 * Remove the occurences of e contained in this
	 * 
	 * @param e
	 *            the edge to remove Caution : it doesn't remove the points
	 */
	public void remove(Edge e) {
		this.edgeList.remove(e);
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
	 * Returns the size of the edge list
	 * 
	 * @return the size of the edge list
	 */
	public int edgeSize() {
		return this.edgeList.size();
	}

	/**
	 * Returns the size of the point list
	 * 
	 * @return the size of the point list
	 */
	public int pointSize() {
		return this.pointList.size();
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
	 * Returns the mesh composed all the triangles the edges belong to and which
	 * belong to the mesh m. If one edge in this doesn't have one triangle to
	 * return, this method returns the returnCentroidMesh() : FIXME
	 * 
	 * @return the mesh composed all the triangles the edges belong to
	 */
	public Mesh returnMesh(Mesh m) {
		// FIXME : si tous les Edge n'ont pas de triangles associés, renvoyer
		// vers l'autre méthode.
		Mesh ens = new Mesh();
		for (Edge e : this.edgeList) {
			// if (!e.getTriangleList().isEmpty()) {
			for (Triangle t : e.getTriangleList()) {
				if (m.contains(t)) {
					ens.add(t);
				}
			}
			// }
		}
		return ens;
	}

	// FIXME : make a better method
	/**
	 * @return
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

	// FIXME : it destroys the former Polyline and all the Points...
	/**
	 * Return a polyline that is the copy of this, but where all points have the
	 * same z
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

	/**
	 * Returns a polyline containing the importants points of this. Caution : we
	 * consider that we are in the plane (x,y)
	 * 
	 * @param error
	 *            the error of frame
	 * @return a polyline containing the importants points of this
	 * @throws Exception
	 */
	// FIXME : continue the explanation
	public Polyline determinateSingularPoints(double error) throws Exception {

		Polyline singularPoints = new Polyline();

		// We take the first edge, and we follow the line until we find a
		// segment with an angle change.
		int numb = 0;
		Edge first = this.getEdgeList().get(numb);

		numb = this.followTheFramedLine(error, numb);

		singularPoints.add(first);

		// Then we record all the points where there is an angle change.
		do {
			numb = followTheFramedLine(error, numb);
			singularPoints.add(this.getEdgeList().get(numb));

		} while (this.getEdgeList().get(numb) != first);

		return singularPoints;
	}

	public Polyline reductNoise(double error) {
		Polyline ret = new Polyline();

		int counter = 1;
		double averageLength = this.lengthAverage();

		while (counter < this.edgeSize() - 2) {

			Edge e = this.edgeList.get(counter);
			Edge before = this.edgeList.get(counter - 1);
			Edge next = this.edgeList.get(counter + 1);

			// If this edge is not oriented as the last and the next
			if (!e.orientedAs(before, error) && !e.orientedAs(next, error)) {
				// Then it is a noise. We must remove it.
				Point shared = null;
				try {
					shared = e.sharedPoint(next);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
				ret.add(new Edge(e.returnOther(shared), next
						.returnOther(shared)));
				counter++;
			}
			// If the edge is little
			else if (e.length() < averageLength) {
				Point shared = null;
				try {
					shared = e.sharedPoint(next);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
				ret.add(new Edge(e.returnOther(shared), next
						.returnOther(shared)));
				counter++;
			} else {
				ret.add(e);
			}

			counter++;
		}

		return ret;
	}

	// We still consider that we are in the plane (x,y)
	// Almost static method... Maybe to put in Edge ?
	/**
	 * Check if p3 is contained in the frame constitued by two segments
	 * parallels to [p1 p2] with a coefficient
	 * 
	 * @param p1
	 *            the first point of the segment
	 * @param p2
	 *            the second point of the segment
	 * @param p3
	 *            the point to check
	 * @param error
	 *            the distance between the segment [p1 p2] and its two parallel
	 *            segments in which p3 must be
	 * @return true if p3 is contained between those segments and false
	 *         otherwise
	 */
	public boolean areWeInTheTwoLinesOrNot(Point p1, Point p2, Point p3,
			double error) {
		double a, b, c, cPlus, cMinus;

		// We calculate the equation of the segment, and of the two lines
		// parallels to it and which frame the line
		if (p1.getX() == p2.getX()) {
			a = 1;
			b = 0;
			c = -p1.getX();
		} else {
			a = -(p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
			b = 1;
			c = -p1.getY() - a * p1.getX();
		}

		cPlus = -c + error;
		cMinus = -c - error;

		return (a * p3.getX() + b * p3.getY() < cPlus && a * p3.getX() + b
				* p3.getY() > cMinus);
	}

	// We still consider that we are in the plane (x,y)
	/**
	 * Return the next important point. Begins the search, and returns the next
	 * point which is not contained in the stripe : it's a point where the bound
	 * change its direction We still consider that we are in the plane (x,y)
	 * 
	 */
	// FIXME : doc !
	public int followTheFramedLine(double error, int numb)
			throws InvalidActivityException {

		if (numb == this.edgeSize() - 1)
			return 0;

		// FIXME if numb = this.size() - 2 !

		Point p2 = null;

		Edge eMain = this.getEdgeList().get(numb);
		Edge eNext = this.getEdgeList().get(++numb);

		// TODO : make a method to return the point common to the both edges.
		if (eMain.contains(eNext.getP1())) {
			p2 = eNext.getP1();
		} else if (eMain.contains(eNext.getP2())) {
			p2 = eNext.getP2();
		} else {
			System.err.println("Error !");
		}

		Point p1 = eMain.returnOther(p2);
		Point p3 = eNext.returnOther(p2);
		Point pbis = p2;

		if (p3 == null) {
			System.err.println("Error !");
		}

		while (areWeInTheTwoLinesOrNot(p1, p2, p3, error)
				&& numb < this.edgeSize() - 1) {

			// Then the point p3 is almost aligned with the other points...
			// We add the two segments, and we do that with a third segment...

			// eMain = eMain.compose(eNext, p2);
			eNext = this.getEdgeList().get(++numb);

			pbis = p3;
			p3 = eNext.returnOther(pbis);

			if (p3 == null) {
				System.err.println("Error !");
			}
		}

		// We are after the angle change, but we certainly have missed the exact
		// angle point. Then we turn back to try to find it.

		if (numb == this.edgeSize() - 1) {
			return 0;
		}

		// When we're here, it means we found some Point which was not in the
		// frame...

		return numb;
	}

	// Error in degrees !
	public Polyline refine(double angleError) throws Exception {

		Polyline refined = new Polyline();

		int numb = 0;

		while (numb < this.edgeSize() - 1) {
			Edge e = this.edgeList.get(numb);

			while (this.edgeList.get(numb).orientedAs(e, angleError)
					&& numb < this.edgeSize() - 1) {
				// We continue.
				numb++;
			}

			// When it's stopped...
			refined.add(new Edge(e.getP1(), this.edgeList.get(numb - 1).getP2()));
		}

		return refined;
	}

	public Polyline getNeighbourLines(Edge e, double error) {
		Polyline ret = new Polyline();

		for (Edge edge : this.edgeList) {
			if (areWeInTheTwoLinesOrNot(e.getP1(), e.getP2(), edge.getP1(),
					error)
			// && areWeInTheTwoLinesOrNot(e.getP1(), e.getP2(),
			// edge.getP2(), error)
			) {
				ret.add(edge);
			}
		}

		return ret;
	}

	public Polyline orientedAs2D(Edge e, double error) {
		Polyline ret = new Polyline();

		for (Edge edge : this.edgeList) {
			if (edge.orientedAs(e, error)) {
				ret.add(edge);
			}
		}

		return ret;
	}
}