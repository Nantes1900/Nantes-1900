package modeles.basis;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

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
	private static final long serialVersionUID = 1L;

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

	public Vector3d getNormal() {
		return normal;
	}

	public void setNormal(Vector3d normal) {
		this.normal = normal;
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
	public void addAll(List<Edge> l) {
		for (Edge e : l) {
			this.add(e);
		}
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
	 * Remove the edges of del contained in this. Caution : it doesn't remove
	 * the points. This method uses the remove(Edge) method
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

	// TODO : this method is maybe not useful.
	// With reductNoise and refine, we maybe have enough things...
	/**
	 * Returns a polyline containing the importants points of this. Caution : we
	 * consider that we are in the plane (x,y)
	 * 
	 * @param error
	 *            the error of frame
	 * @return a polyline containing the importants points of this
	 * @throws Exception
	 */
	public Polyline determinateSingularPoints(double error) throws Exception {

		Polyline singularPoints = new Polyline();

		// We take the first edge, and we follow the line until we find a
		// segment with an angle change.
		int numb = 0;
		Edge first = this.getEdgeList().get(numb);

		numb = this.followTheFramedLine(error, numb);

		// We add it to the list of singular points
		singularPoints.add(first);

		// Then we record all the points where there is an angle change.
		do {
			// When we find a point of angle change,
			numb = followTheFramedLine(error, numb);
			// We add it,
			singularPoints.add(this.getEdgeList().get(numb));

			// And we continue, until we've reached the first one.
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

			// If this edge is not oriented as the former and the next
			if (!e.orientedAs(before, error) && !e.orientedAs(next, error)) {
				// Then it is a noise. We must remove it.
				Point shared = null;
				shared = e.sharedPoint(next);
				ret.add(new Edge(e.returnOther(shared), next
						.returnOther(shared)));
				counter++;
			}

			// If the edge is little
			else if (e.length() < averageLength) {
				Point shared = null;
				shared = e.sharedPoint(next);
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

	// TODO : Almost static method... Maybe to put in Edge ?
	/**
	 * Check if p3 is contained in the frame constitued by two segments
	 * parallels to [p1 p2] with a coefficient. Caution : this method expects to
	 * be in the plane (x,y). Thus a change base must be made before.
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
	public boolean isInCylinder2D(Point p1, Point p2, Point p3, double error) {
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

	public boolean isInCylinder3D(Point p1, Point p2, Point p3, double error) {

		double x1 = p1.getX(), x2 = p2.getX(), x3 = p3.getX();
		double y1 = p1.getY(), y2 = p2.getY(), y3 = p3.getY();
		double z1 = p1.getZ(), z2 = p2.getZ(), z3 = p3.getZ();

		double lambda = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1) + (z3 - z1)
				* (z2 - z1))
				/ ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1)
						* (z2 - z1));

		double x4 = lambda * (x2 - x1) + x1, y4 = lambda * (y2 - y1) + y1, z4 = lambda
				* (z2 - z1) + z1;

		Point p4 = new Point(x4, y4, z4);

		return (lambda > 0 && lambda < 1 && p3.distance(p4) < error);
	}

	public boolean isInInfiniteCylinder3D(Point p1, Point p2, Point p3,
			double error) {

		double x1 = p1.getX(), x2 = p2.getX(), x3 = p3.getX();
		double y1 = p1.getY(), y2 = p2.getY(), y3 = p3.getY();
		double z1 = p1.getZ(), z2 = p2.getZ(), z3 = p3.getZ();

		double lambda = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1) + (z3 - z1)
				* (z2 - z1))
				/ ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1)
						* (z2 - z1));

		double x4 = lambda * (x2 - x1) + x1, y4 = lambda * (y2 - y1) + y1, z4 = lambda
				* (z2 - z1) + z1;

		Point p4 = new Point(x4, y4, z4);

		return (p3.distance(p4) < error);
	}

	/**
	 * Return the next important point. Begins the search, and returns the next
	 * point which is not contained in the stripe : it's a point where the bound
	 * change its direction. Caution : this method expects to be in the plane
	 * (x,y)
	 * 
	 * @param error
	 *            the radius of the cylinder 2D where the points must be
	 *            contained
	 * @param numb
	 *            the number of the current edge to begin the algorithm with
	 * @return the number of the current edge where the algorithm stopped
	 */
	public int followTheFramedLine(double error, int numb) {

		if (numb == this.edgeSize() - 1)
			return 0;

		if (numb == this.edgeSize() - 2)
			return this.edgeSize();

		Edge eMain = this.getEdgeList().get(numb);
		Edge eNext = this.getEdgeList().get(++numb);

		Point p2 = eMain.sharedPoint(eNext);

		Point p1 = eMain.returnOther(p2);
		Point p3 = eNext.returnOther(p2);
		Point pbis = p2;

		while (isInCylinder2D(p1, p2, p3, error) && numb < this.edgeSize() - 1) {

			// If we are the loop, it means that the point p3 is almost aligned
			// with the other points.
			eNext = this.getEdgeList().get(++numb);

			// Then we continue to the next edge.
			pbis = p3;
			p3 = eNext.returnOther(pbis);
		}

		// If we have arrived to the end of the polyline, we return 0 so that
		// the upper method understand it is the end.
		if (numb == this.edgeSize() - 1) {
			return 0;
		}

		// When we're here, it means we found some Point which was not in the
		// cylinder, then which means an angle change.
		return numb;
	}

	// The error is in degrees
	/**
	 * Refine the polyline by mergind a list of edges which follow each other
	 * and which are almost same-oriented.
	 * 
	 * @param angleError
	 *            the error of orientation
	 * @return a new polyline containing the refined polyline
	 */
	public Polyline refine(double angleError) {

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
	public ArrayList<Point> getCylinderInfinite(Edge e, double error) {
		ArrayList<Point> ret = new ArrayList<Point>();

		for (Point p : this.pointList) {
			if (isInInfiniteCylinder3D(e.getP1(), e.getP2(), p, error)) {
				ret.add(p);
			}
		}

		return ret;
	}

	public ArrayList<Point> getCylinder(Edge e, double error) {
		ArrayList<Point> ret = new ArrayList<Point>();

		for (Point p : this.pointList) {
			if (isInCylinder3D(e.getP1(), e.getP2(), p, error)) {
				ret.add(p);
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

	public void addNeighbour(Polyline p) {
		if (!this.neighbours.contains(p))
			this.neighbours.add(p);
	}

	public void findCommonEdge(Polyline p, double radiusError) {
		Polyline p1 = this;
		Polyline p2 = p;

		// We define the axe of the intersection
		Vector3d norm1 = p1.getNormal();
		Vector3d norm2 = p2.getNormal();

		if (norm1.angle(norm2) > 0.3 && norm1.angle(norm2) < 2.8) {

			// We define the axe of the intersection
			Edge axis = this.getAxis(p, norm1, norm2, radiusError);

			// Select only the points which are into these bounds
			ArrayList<Point> cylinder1 = p1.getCylinder(axis, radiusError);
			ArrayList<Point> cylinder2 = p2.getCylinder(axis, radiusError);

			ArrayList<Point> cylinder = new ArrayList<Point>();
			cylinder.addAll(cylinder1);
			cylinder.addAll(cylinder2);

			if (cylinder1.size() != 0 && cylinder2.size() != 0) {
				for (Point point : cylinder) {
					point.set(axis.project(point).getPointAsCoordinates());
				}
			}
		}
	}

	private Edge getAxis(Polyline p, Vector3d norm1, Vector3d norm2,
			double radiusError) {
		Polyline p1 = this;
		Polyline p2 = p;

		Point p01 = p1.getPointList().get(0);
		Point p02 = p2.getPointList().get(0);

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

	public void findCommonWallEdges(Polyline p, Vector3d normalFloor,
			double radiusError) {
		Polyline p1 = this;
		Polyline p2 = p;

		if (p1 != p2 && p1.isNeighbour(p2)) {

			// We define the axe of the intersection

			Vector3d norm1 = p1.getNormal();
			Vector3d norm2 = p2.getNormal();

			if (norm1.angle(norm2) > 0.3 && norm1.angle(norm2) < 2.8) {

				Point p01 = p1.getPointList().get(0);
				Point p02 = p2.getPointList().get(0);

				double a1 = norm1.x, b1 = norm1.y, c1 = norm1.z;
				double a2 = norm2.x, b2 = norm2.y, c2 = norm2.z;
				double d1 = a1 * p01.getX() + b1 * p01.getY() + c1 * p01.getZ();
				double d2 = a2 * p02.getX() + b2 * p02.getY() + c2 * p02.getZ();

				double x = 0;
				double y = (c2 * d1 - c1 * d2) / (b1 * c2 - b2 * c1);
				double z = (b1 * d2 - b2 * d1) / (b1 * c2 - b2 * c1);

				Vector3d norm3 = new Vector3d();
				norm3.cross(norm1, norm2);

				Edge axis = new Edge(new Point(x, y, z), new Point(x + norm3.x,
						y + norm3.y, z + norm3.z));

				// Get the points which are into the cylinder, and project them
				// on
				// the axe.
				ArrayList<Point> cylinder1 = p1.getCylinderInfinite(axis,
						radiusError);
				ArrayList<Point> cylinder2 = p2.getCylinderInfinite(axis,
						radiusError);

				ArrayList<Point> cylinder = new ArrayList<Point>();
				cylinder.addAll(cylinder1);
				cylinder.addAll(cylinder2);

				// Select the max and min
				Point extremityMax = this.getCylinderMaxPoint(cylinder, axis);
				Point extremityMin = this.getCylinderMinPoint(cylinder, axis);

				// Adjust the two surfaces to have the same edge with the same
				// max
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

				// Select only the points which are into these bounds
				axis = new Edge(extremityMin, extremityMax);
				cylinder1 = p1.getCylinder(axis, radiusError);
				cylinder2 = p2.getCylinder(axis, radiusError);

				cylinder = new ArrayList<Point>();
				cylinder.addAll(cylinder1);
				cylinder.addAll(cylinder2);

				if (cylinder1.size() != 0 && cylinder2.size() != 0) {
					for (Point point : cylinder) {
						point.set(axis.project(point).getPointAsCoordinates());
					}
				}

				// On the axe, select the min, max in x (or y, or z, no
				// matter), they are the extremities. Thus make an edge, and
				// remove all the other ones between.
			}
		}
	}

	private Point getCylinderMaxPoint(ArrayList<Point> cylinder, Edge axe) {
		Point ref = null;
		double max = Double.NEGATIVE_INFINITY;

		for (Point p : this.pointList) {
			if (getAxisProjectionPoint(cylinder, axe, p).getX() > max) {
				ref = getAxisProjectionPoint(cylinder, axe, p);
				max = ref.getX();
			}
		}

		return ref;
	}

	private Point getCylinderMinPoint(ArrayList<Point> cylinder, Edge axe) {
		Point ref = null;
		double min = Double.POSITIVE_INFINITY;

		for (Point p : this.pointList) {
			if (getAxisProjectionPoint(cylinder, axe, p).getX() < min) {
				ref = getAxisProjectionPoint(cylinder, axe, p);
				min = ref.getX();
			}
		}

		return ref;
	}

	private Point getAxisProjectionPoint(ArrayList<Point> cylinder, Edge axe,
			Point p) {
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

	public ArrayList<Polyline> getNeighbours() {
		return this.neighbours;
	}

	public boolean isNeighbour(Polyline p) {
		if (p == this) {
			return false;
		}
		for (Point p1 : this.pointList) {
			for (Point p2 : p.pointList) {
				if (p1.distance(p2) < 2)
					return true;
			}
		}
		return false;
	}

	public void writeCentroidMesh(String string) {
		this.returnCentroidMesh().writeSTL(string);
	}
}