package modeles;

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

	private ArrayList<Point> pointList;
	private ArrayList<Edge> edgeList;

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

		this.edgeList = new ArrayList<Edge>(a);
		this.pointList = new ArrayList<Point>();

		for (Edge e : a) {
			this.add(e.getP1());
			this.add(e.getP2());
		}

		this.ID = ++ID_current;
	}

	/**
	 * Void constructor
	 */
	public Polyline() {
		this.edgeList = new ArrayList<Edge>();
		this.pointList = new ArrayList<Point>();
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
	 * Returns the mesh composed all the triangles the edges belong to. If one
	 * edge in this doesn't have one triangle to return, this method returns the
	 * returnCentroidMesh() : FIXME
	 * 
	 * @return the mesh composed all the triangles the edges belong to
	 */
	public Mesh returnMesh() {
		// FIXME : si tous les Edge n'ont pas de triangles associés, renvoyer
		// vers l'autre méthode.
		Mesh ens = new Mesh();
		for (Edge e : this.edgeList) {
			if (!e.getTriangleList().isEmpty())
				ens.addAll(e.getTriangleList());
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
		Vector3d normal = new Vector3d(0, 0, 1);

		Point before = this.pointList.get(this.pointSize() - 1);

		for (Point p : this.pointList) {
			ens.add(new Triangle(centroid, before, p,
					new Edge(centroid, before), new Edge(before, p), new Edge(
							p, centroid), normal));
			before = p;
		}

		return ens;
	}

	/**
	 * Apply the base change to all the points contained, without changing the
	 * references. Caution : this method changes the hashCode, then be careful
	 * the hashTables which contains points
	 * 
	 * @param matrix
	 *            the change base matrix
	 */
	public void changeBase(double[][] matrix) {
		if (matrix == null) {
			throw new InvalidParameterException();
		}

		Polyline line = new Polyline(this.edgeList);
		for (Point p : line.pointList) {
			p.changeBase(matrix);
		}

		this.edgeList.clear();
		this.pointList.clear();
		this.addAll(line.edgeList);
	}

	/**
	 * Returns the number of edges contained in this that contain the point p
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

	// FIXME : @Test
	/**
	 * Order the polyline Each edge in the edge list must be surrounded by its
	 * neighbours
	 */
	public void order() {
		// TODO : vérification que chaque Edge a bien 2 voisins : ni plus, ni
		// moins
		// !
		Polyline ret = new Polyline();

		Edge first = this.getOne();
		Point p = first.getP1();
		Edge e = first.returnNeighbour(p, this);
		p = e.returnOther(p);

		while (e != first) {
			ret.add(e);
			e = e.returnNeighbour(p, this);
			p = e.returnOther(p);
		}
		ret.add(e);

		this.edgeList.clear();
		this.pointList.clear();
		this.addAll(ret.getEdgeList());
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
	public Polyline zProjection(double z) {
		Polyline line = new Polyline();
		line.addAll(this.edgeList);

		for (Point p : line.pointList) {
			p.setZ(z);
		}

		return line;
	}

	//
	/**
	 * Returns a polyline containing the importants points of this. Caution : we
	 * consider that we are in the plane (x,y)
	 * 
	 * @param error
	 *            the error of frame
	 * @return a polyline containing the importants points of this
	 */
	// FIXME : continue the explanation
	public Polyline determinateSingularPoints(double error) {
		Polyline singularPoints = new Polyline();

		// We take a point, and we follow the line until we find a segment with
		// angle change.
		Edge first = this.getOne();
		first = followTheFramedLine(first, first.getP2(), error, first);
		singularPoints.add(first);
		Edge e = first;

		// Then we record all the points where there is a angle change.
		do {
			// We determinate the point to know the direction to take...
			e = followTheFramedLine(e, this.toDelete, error, first);
			singularPoints.add(e);
		} while (e != first);

		return singularPoints;
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
		if (p1.getY() == p2.getY()) {
			a = 0;
			b = 1;
			c = -p1.getX();
		} else {
			a = 1;
			b = (p1.getX() - p2.getX()) / (p2.getY() - p1.getY());
			c = (-p1.getX() * p2.getY() + p2.getX() * p1.getY())
					/ (p2.getY() - p1.getY());
		}

		cPlus = -c + error;
		cMinus = -c - error;

		return (a * p3.getX() + b * p3.getY() < cPlus && a * p3.getX() + b
				* p3.getY() > cMinus);
	}

	// We still consider that we are in the plane (x,y)
	// FIXME : delete the Edge stop
	/**
	 * Return the next important point. Begins the search, and returns the next
	 * point which is not contained in the stripe : it's a point where the bound
	 * change its direction We still consider that we are in the plane (x,y)
	 * 
	 * @param first
	 *            the edge where the algorithm begins to search
	 * @param p2
	 *            the first point to search : it describes the direction
	 * @param error
	 *            the error of areWeInTheTwoLinesOrNot
	 * @param stop
	 *            the edge not to pass further
	 * @return the next important point
	 */
	public Edge followTheFramedLine(Edge first, Point p2, double error,
			Edge stop) {

		Point p1 = first.returnOther(p2);

		// LOOK : We can take the second segment in the list : it's supposed to
		// be ordered !
		Edge e2 = first.returnNeighbour(p2, this);
		Point p3 = e2.returnOther(p2);

		Edge eMain = first;
		Edge eNext = e2;

		while (areWeInTheTwoLinesOrNot(p1, p2, p3, error) && eNext != stop) {
			// Then the point p3 is almost aligned with the other points...
			// We add the two segments, and we do that with a third segment...
			eMain = eMain.compose(eNext, p2);
			eNext = eNext.returnNeighbour(p3, this);
			p1 = eMain.returnOther(p3);
			p2 = p3;
			p3 = eNext.returnOther(p2);
		}
		// When we're here, it means we found some Point which was not in the
		// frame...

		this.toDelete = p3;
		return eNext;
	}
}