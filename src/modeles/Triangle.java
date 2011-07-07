package modeles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;


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

	public void setEdge1(Edge e) {
		this.edges[0] = e;
	}

	public Edge getEdge2() {
		return edges[1];
	}

	public void setEdge2(Edge e) {
		this.edges[1] = e;
	}

	public Edge getEdge3() {
		return edges[2];
	}

	public void setEdge3(Edge e) {
		this.edges[2] = e;
	}

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

	//TODO : A TESTER !!
	public boolean isNormalTo(Vector3d normal, double error) {
		return((this.normal.dot(normal) < error) && (this.normal.dot(normal) > -error));
	}

	public boolean contains(Point p) {
		return (points[0].equals(p) || points[1].equals(p) || points[2].equals(p));
	}

	@Override
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

	public boolean isNeighboor(Triangle f) {
		return(this.getNeighbours().contains(f));
	}

	//TODO : créer une deuxième méthode qui compare les références et non les points : plus rapide !
	public boolean hasTwoEqualVerticesWith(Triangle f) {
		return(		(this.contains(f.points[0]) && this.contains(f.points[1])) 
				|| 	(this.contains(f.points[1]) && this.contains(f.points[2])) 
				|| 	(this.contains(f.points[0]) && this.contains(f.points[2]))
		);
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

	public Point getCentroid() {
		Point mid = new Point((points[0].getX() + points[1].getX())/(double)2, (points[0].getY() + points[1].getY())/(double)2, (points[0].getZ() + points[1].getZ())/(double)2);
		return new Point((mid.getX() + 2*points[2].getX())/(double)3, (mid.getY() + 2*points[2].getY())/(double)3, (mid.getZ() + 2*points[2].getZ())/(double)3);
	}

	public Point yMaxPoint() {
		if(points[0].getY() == this.yMax())
			return points[0];
		else if(points[1].getY() == this.yMax())
			return points[1];
		else
			return points[2];
	}

	public Point yMinPoint() {
		if(points[0].getY() == this.yMin())
			return points[0];
		else if(points[1].getY() == this.yMin())
			return points[1];
		else
			return points[2];
	}

	public Point zMaxPoint() {
		if(points[0].getZ() == this.zMax())
			return points[0];
		else if(points[1].getZ() == this.zMax())
			return points[1];
		else
			return points[2];
	}

	//Attention en l'utilisant, à ce qu'elle n'ait pas déjà été effectuée !
	public void changeBase(double[][] base){
		MatrixMethod.changeBase(this.getNormal(), base);
		this.points[0].changeBase(base);
		this.points[1].changeBase(base);
		this.points[2].changeBase(base);
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

	
	//Returns int ret the neighbours of this which belongs to m
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

	//FIXME !
	public ArrayList<Edge> getFront() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		
		for(Edge e : this.edges) {
			if(e.getTriangleList().size() == 1)
				list.add(e);
		}
		
		return list;
	}

	public Collection<Point> getPoints() {
		return Arrays.asList(this.points);
	}
}