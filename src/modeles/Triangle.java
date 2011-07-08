package modeles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

	public Point getP1() {
		return points[0];
	}

	public Point getP2() {
		return points[1];
	}

	public Point getP3() {
		return points[2];
	}

	public Collection<Point> getPoints() {
		return Arrays.asList(this.points);
	}

	public Vector3d getNormal() {
		return normal;
	}

	public Edge getE1() {
		return edges[0];
	}

	public Edge getE2() {
		return edges[1];
	}

	public Edge getE3() {
		return edges[2];
	}
	
	public String toString() {
		return points[0].toString() + "\n" + points[1].toString() + "\n" + points[2].toString() + "\n" + normal.toString();
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
	//TODO : do not depends on the normal : fix it !
	//Do not depend on the edges !
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

	public double xAverage(){
		double xAverage = points[0].getX() + points[1].getX() + points[2].getX();
		return xAverage/3;
	}
	
	public double zAverage(){
		double zAverage = points[0].getZ() + points[1].getZ() + points[2].getZ();
		return zAverage/3;
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

	public Point xMinPoint() {
		double xMin = this.xMin();
		for(Point p : points) {
			if(p.getX() == xMin)
				return p;
		}
		return null;
	}

	public Point xMaxPoint() {
		double xMax = this.xMax();
		for(Point p : points) {
			if(p.getX() == xMax)
				return p;
		}
		return null;
	}

	public Point yMinPoint() {
		double yMin = this.yMin();
		for(Point p : points) {
			if(p.getY() == yMin)
				return p;
		}
		return null;
	}

	public Point yMaxPoint() {
		double yMay = this.yMax();
		for(Point p : points) {
			if(p.getY() == yMay)
				return p;
		}
		return null;
	}

	public Point zMinPoint() {
		double zMin = this.zMin();
		for(Point p : points) {
			if(p.getZ() == zMin)
				return p;
		}
		return null;
	}

	public Point zMaxPoint() {
		double zMax = this.zMax();
		for(Point p : points) {
			if(p.getZ() == zMax)
				return p;
		}
		return null;
	}	
	
	public Triangle xProjection(double x) {
		Triangle t = new Triangle(this);
		
		for(Point p : t.points)
			p.setX(x);
		
		return t;
	}	
	
	public Triangle yProjection(double y) {
		Triangle t = new Triangle(this);
		
		for(Point p : t.points)
			p.setY(y);
		
		return t;
	}	
	
	public Triangle zProjection(double z) {
		Triangle t = new Triangle(this);
		
		for(Point p : t.points)
			p.setZ(z);
		
		return t;
	}

	public boolean angularTolerance(Vector3d vector, double error) {
		return(this.normal.angle(vector)*180/Math.PI < error);
	}
	
	public boolean angularTolerance(Triangle face, double error) {
		return(this.angularTolerance(face.normal, error));
	}

	public boolean isNormalTo(Vector3d normal, double error) {
		return((this.normal.dot(normal) < error) && (this.normal.dot(normal) > -error));
	}

	//Returns in ret the neighbours of this which belongs to m
	public void returnNeighbours(Mesh ret, Mesh m) {
		ret.add(this);
		
		Triangle other;
		
		for(Edge e : this.edges) {
			other = e.returnOther(this);
			if(other != null && m.contains(other) && !ret.contains(other))
				other.returnNeighbours(ret, m);
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

	public ArrayList<Triangle> getNeighbours() {
		ArrayList<Triangle> l = new ArrayList<Triangle>();
		Triangle other;
		
		for(Edge e : this.edges) {
			other = e.returnOther(this);
			if(other != null)
				l.add(other);
		}
		
		return l;
	}

	public boolean isNeighboor(Triangle f) {
		return(this.getNeighbours().contains(f));
	}
}