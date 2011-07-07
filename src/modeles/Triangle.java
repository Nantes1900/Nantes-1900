package modeles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Vector3d;


public class Triangle {
	
	private Point[] points = new Point[3];
	private Vector3d normal;
	private Edge[] edges = new Edge[3];

	public Triangle(Point p0, Point p1, Point p2, Edge e1, Edge e2, Edge e3, Vector3d normale) {
		this.points[0] = p0;
		this.points[1] = p1;
		this.points[2] = p2;
		this.normal = normale;
		this.edges[0] = e1;
		this.edges[1] = e2;
		this.edges[2] = e3;
		e1.addTriangle(this);
		e2.addTriangle(this);
		e3.addTriangle(this);
	}
	
	public Triangle(Triangle t) {
		this.points[0] = t.points[0];
		this.points[1] = t.points[1];
		this.points[2] = t.points[2];
		this.normal = t.normal;
		this.edges[0] = t.edges[0];
		this.edges[1] = t.edges[1];
		this.edges[2] = t.edges[2];
		this.edges[0].addTriangle(this);
		this.edges[1].addTriangle(this);
		this.edges[2].addTriangle(this);
	}

	public Point getP0() {
		return points[0];
	}

	public Point getP1() {
		return points[1];
	}

	public Point getP2() {
		return points[2];
	}

	public Vector3d getNormal() {
		return normal;
	}

	public Edge getEdge1() {
		return edges[0];
	}

	public Edge getEdge2() {
		return edges[1];
	}

	public Edge getEdge3() {
		return edges[2];
	}

	
	//FIXME : oh oh !
	public ArrayList<Triangle> getNeighbours() {
		ArrayList<Triangle> l = new ArrayList<Triangle>();
		for(Edge e : this.edges) {
			l.addAll(e.getTriangleList());
		}
		for(Triangle t : l) {
			if(t == this)
				l.remove(t);
		}
		return l;
	}

	public boolean isNeighboor(Triangle f) {
		return(this.getNeighbours().contains(f));
	}
	
	public String toString() {
		return points[0].toString() + "\n" + points[1].toString() + "\n" + points[2].toString() + "\n" + normal.toString() ;
	}

	/**
	 * Check if the angle between the normal of this face and a vector is less or equal than the error
	 * @param face The Vector3d we want to compare to.
	 * @param erreur The double containing the tolerance.
	 * @return boolean if true or not
	 */
	public boolean angularTolerance(Vector3d vector, double error) {
		return(this.normal.angle(vector)*180/Math.PI < error);
	}

	/**
	 * Check if the angle between two face's normale is less than erreur.
	 * @param face The face we want to compare to.
	 * @param erreur The double containing the tolerance.
	 * @return boolean if true or not
	 */
	public boolean angularTolerance(Triangle face, double error) {
		return(this.angularTolerance(face.normal, error));
	}

	public boolean isNormalTo(Vector3d normal, double error) {
		return((this.normal.dot(normal) < error) && (this.normal.dot(normal) > -error));
	}

	public boolean contains(Point p) {
		for(Point point : points) {
			if(point.equals(p))
				return true;
		}
		return false;
	}

	@Override
	//FIXME : revoir le hashCode !
	public int hashCode() {
		int result = 1;
		result = result + ((points[0] == null) ? 0 : points[0].hashCode());
		result = result + ((points[1] == null) ? 0 : points[1].hashCode());
		result = result + ((points[2] == null) ? 0 : points[2].hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triangle other = (Triangle) obj;
		
		if (this.contains(other.points[0]) && this.contains(other.points[1]) && this.contains(other.points[2]))
			return true;
		else
			return false;
	}

	/**
	 * Compute the average z-coordinate of the points of this face
	 * @return the average z-coordinate of the points of this face
	 */
	public double zAverage(){
		double zAverage = points[0].getZ() + points[1].getZ() + points[2].getZ();
		return zAverage/3;
	}

	public double xAverage(){
		double xAverage = points[0].getX() + points[1].getX() + points[2].getX();
		return xAverage/3;
	}

	public double yAverage(){
		double yAverage = points[0].getY() + points[1].getY() + points[2].getY();
		return yAverage/3;
	}

	public double xMin(){
		return Math.min(points[0].getX(), Math.min(points[1].getX(), points[2].getX()));
	}

	public double xMax(){
		return Math.max(points[0].getX(), Math.max(points[1].getX(), points[2].getX()));
	}

	public double yMin(){
		return Math.min(points[0].getY(), Math.min(points[1].getY(), points[2].getY()));
	}

	public double yMax(){
		return Math.max(points[0].getY(), Math.max(points[1].getY(), points[2].getY()));
	}

	public double zMin(){
		return Math.min(points[0].getZ(), Math.min(points[1].getZ(), points[2].getZ()));
	}

	public double zMax(){
		return Math.max(points[0].getZ(), Math.max(points[1].getZ(), points[2].getZ()));
	}

	public Point yMaxPoint() {
		double yMax = this.yMax();
		for(Point p : points) {
			if(p.getY() == yMax)
				return p;
		}
		return null;
	}

	public Triangle zProjection(double z) {
		Triangle t = new Triangle(this);
		t.points[0].setZ(z);
		t.points[1].setZ(z);
		t.points[2].setZ(z);
		return t;
	}

	public Triangle xProjection(double x) {
		Triangle t = new Triangle(this);
		t.points[0].setX(x);
		t.points[1].setX(x);
		t.points[2].setX(x);
		return t;
	}

	//Returns in ret the neighbours of this which belongs to m
	//TODO : essayer de remettre 3 Triangle neighbour1, 2, et 3.
	//Ici, on aurait plus besoin de la HashSet...
	public void returnNeighbours(Mesh ret, Mesh m) {
		ret.add(this);
		
		HashSet<Triangle> neighbours = new HashSet<Triangle>();
		
		neighbours.addAll(this.getEdge1().getTriangleList());
		neighbours.addAll(this.getEdge2().getTriangleList());
		neighbours.addAll(this.getEdge3().getTriangleList());
		
		for(Triangle t : neighbours) {
			if(t != this && m.contains(t) && !ret.contains(t))
				t.returnNeighbours(ret, m);
		}
	}

//	//FIXME !
//	public ArrayList<Edge> getFront() {
//		ArrayList<Edge> list = new ArrayList<Edge>();
//		
//		for(Edge e : this.edges) {
//			if(e.getTriangleList().size() == 1)
//				list.add(e);
//		}
//		
//		return list;
//	}

	public Collection<Point> getPoints() {
		return Arrays.asList(this.points);
	}
}