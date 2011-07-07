package modeles;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;


public class Triangle {
	private Point p0;
	private Point p1;
	private Point p2;

	private Vector3d normal;

	private ArrayList<Triangle> neighbours;

	private Edge edge1 = null;
	private Edge edge2 = null;
	private Edge edge3 = null;

	public Triangle(Point p0, Point p1, Point p2, Edge e1, Edge e2, Edge e3, Vector3d normale) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.normal = normale;
		this.edge1 = e1;
		this.edge2 = e2;
		this.edge3 = e3;
		e1.addTriangle(this);
		e2.addTriangle(this);
		e3.addTriangle(this);
		neighbours = new ArrayList<Triangle>();
	}

	public Triangle() {
		p0 = new Point();
		p1 = new Point();
		p2 = new Point();
		this.normal = new Vector3d();
		neighbours = new ArrayList<Triangle>();
	}	

	public Triangle(Triangle t) {
		this.p0 = t.p0;
		this.p1 = t.p1;
		this.p2 = t.p2;
		this.normal = t.normal;
		this.neighbours = new ArrayList<Triangle>(t.neighbours);
		this.edge1 = t.edge1;
		this.edge2 = t.edge2;
		this.edge3 = t.edge3;
		edge1.addTriangle(this);
		edge2.addTriangle(this);
		edge3.addTriangle(this);
	}

	public Point getP0() {
		return p0;
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public Vector3d getNormal() {
		return normal;
	}

	public Edge getEdge1() {
		return edge1;
	}

	public void setEdge1(Edge edge1) {
		this.edge1 = edge1;
	}

	public Edge getEdge2() {
		return edge2;
	}

	public void setEdge2(Edge edge2) {
		this.edge2 = edge2;
	}

	public Edge getEdge3() {
		return edge3;
	}

	public void setEdge3(Edge edge3) {
		this.edge3 = edge3;
	}

	public ArrayList<Triangle> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<Triangle> neighbours) {
		this.neighbours = neighbours;
	}

	public void addNeighbour(Triangle f) {
		if(this != f && !this.isNeighboor(f)) {
			this.neighbours.add(f);
			if(this.neighbours.size() > 3) {
				System.err.println("Error : more than trois voisins...");
			}
		}
	}

	public void addNeighbours(List<Triangle> l) {
		for(Triangle t : l) {
			addNeighbour(t);
		}
	}

	public String toString() {
		return p0.toString() + "\n" + p1.toString() + "\n" + p2.toString() + "\n" + normal.toString() ;
	}

	public int getNumVoisins() {
		return neighbours.size();
	}

	/**
	 * Check if the angle between the normal of this face and a vector is less or equal than the error
	 * @param face The Vector3d we want to compare to.
	 * @param erreur The double containing the tolerance.
	 * @return boolean if true or not
	 */
	//TODO : passer ça en degrés !
	public boolean angularTolerance(Vector3d vector, double error) {
		return(this.normal.angle(vector) < error);
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
		return(p0.equals(p) || p1.equals(p) || p2.equals(p));
	}

	@Override
	public int hashCode() {
//		final int prime = 31;
		int result = 1;
//		result = prime * result + ((normal == null) ? 0 : normal.hashCode());
		result = result + ((p0 == null) ? 0 : p0.hashCode());
		result = result + ((p1 == null) ? 0 : p1.hashCode());
		result = result + ((p2 == null) ? 0 : p2.hashCode());
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
		if (normal == null) {
			if (other.normal != null)
				return false;
		} 
		else if (!normal.equals(other.normal))
			return false;
		
		if(this.contains(other.p0) && this.contains(other.p1) && this.contains(other.p2))
			return true;
		else
			return false;
	}

	public boolean isNeighboor(Triangle f) {
		return(neighbours.contains(f));
	}

	//TODO : créer une deuxième méthode qui compare les références et non les points : plus rapide !
	public boolean hasTwoEqualVerticesWith(Triangle f) {
		return(		(this.contains(f.p0) && this.contains(f.p1)) 
				|| 	(this.contains(f.p1) && this.contains(f.p2)) 
				|| 	(this.contains(f.p0) && this.contains(f.p2))
		);
	}

	/**
	 * Compute the average z-coordinate of the points of this face
	 * @return the average z-coordinate of the points of this face
	 */
	public double zAverage(){
		double zAverage = p0.getZ() + p1.getZ() + p2.getZ();
		return zAverage/3;
	}

	public double xAverage(){
		double xAverage = p0.getX() + p1.getX() + p2.getX();
		return xAverage/3;
	}

	public double yAverage(){
		double yAverage = p0.getY() + p1.getY() + p2.getY();
		return yAverage/3;
	}

	public double xMin(){
		return Math.min(p0.getX(), Math.min(p1.getX(), p2.getX()));
	}

	public double xMax(){
		return Math.max(p0.getX(), Math.max(p1.getX(), p2.getX()));
	}

	public double yMin(){
		return Math.min(p0.getY(), Math.min(p1.getY(), p2.getY()));
	}

	public double yMax(){
		return Math.max(p0.getY(), Math.max(p1.getY(), p2.getY()));
	}

	public double zMin(){
		return Math.min(p0.getZ(), Math.min(p1.getZ(), p2.getZ()));
	}

	public double zMax(){
		return Math.max(p0.getZ(), Math.max(p1.getZ(), p2.getZ()));
	}

	public Point getCentroid() {
		Point mid = new Point((p0.getX() + p1.getX())/(double)2, (p0.getY() + p1.getY())/(double)2, (p0.getZ() + p1.getZ())/(double)2);
		return new Point((mid.getX() + 2*p2.getX())/(double)3, (mid.getY() + 2*p2.getY())/(double)3, (mid.getZ() + 2*p2.getZ())/(double)3);
	}

	public Point yMaxPoint() {
		if(p0.getY() == this.yMax())
			return p0;
		else if(p1.getY() == this.yMax())
			return p1;
		else
			return p2;
	}

	public Point yMinPoint() {
		if(p0.getY() == this.yMin())
			return p0;
		else if(p1.getY() == this.yMin())
			return p1;
		else
			return p2;
	}

	public Point zMaxPoint() {
		if(p0.getZ() == this.zMax())
			return p0;
		else if(p1.getZ() == this.zMax())
			return p1;
		else
			return p2;
	}

	//Attention en l'utilisant, à ce qu'elle n'ait pas déjà été effectuée !
	public void changeBase(double[][] base){
		normal = MatrixMethod.changeBase(this.getNormal(), base);
		this.p0.changeBase(base);
		this.p1.changeBase(base);
		this.p2.changeBase(base);
	}

	public Triangle zProjection(double z) {
		Triangle t = new Triangle(this);
		t.p0.setZ(z);
		t.p1.setZ(z);
		t.p2.setZ(z);
		return t;
	}

	public Triangle xProjection(double x) {
		Triangle t = new Triangle(this);
		t.p0.setX(x);
		t.p1.setX(x);
		t.p2.setX(x);
		return t;
	}

	public void returnNeighbours(Mesh ret) {
		ret.add(this);

		Triangle t;
		for(int i = 0; i < neighbours.size(); i ++) {
			t = neighbours.get(i);
			if(!ret.contains(t))
				t.returnNeighbours(ret);
		}
	}

	public ArrayList<Edge> getFront() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		if(this.edge1.getTriangleList().size() == 1)
			list.add(this.edge1);
		if(this.edge2.getTriangleList().size() == 1)
			list.add(this.edge2);
		if(this.edge3.getTriangleList().size() == 1)
			list.add(this.edge3);
		return list;
	}

	public void clearVoisins() {
		neighbours.clear();
	}
}