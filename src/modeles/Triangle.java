package modeles;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.Case;
import utils.MatrixMethod;


public class Triangle {
	private Point p0;
	private Point p1;
	private Point p2;
	
	private Vector3d normal;
	
	private Triangle neighbour1;
	private Triangle neighbour2;
	private Triangle neighbour3;
	
	private ArrayList<Case> belongedCases;

	public Triangle(Point p0, Point p1, Point p2, Vector3d normale) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.normal = normale;
		this.neighbour1 = null;
		this.neighbour2 = null;
		this.neighbour3 = null;
		belongedCases = new ArrayList<Case>();
	}
	
	public Triangle() {
		p0 = new Point();
		p1 = new Point();
		p2 = new Point();
		this.normal = new Vector3d();
		this.neighbour1 = null;
		this.neighbour2 = null;
		this.neighbour3 = null;
		belongedCases = new ArrayList<Case>();
	}	
	
	public Triangle(Triangle t) {
		p0 = t.p0;
		p1 = t.p1;
		p2 = t.p2;
		this.normal = t.normal;
		this.neighbour1 = t.neighbour1;
		this.neighbour2 = t.neighbour2;
		this.neighbour3 = t.neighbour3;
		belongedCases = new ArrayList<Case>(t.belongedCases);
	}

	public Point getP0() {
		return p0;
	}

	public void setP0(Point p0) {
		this.p0 = p0;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public Vector3d getNormal() {
		return normal;
	}

	public void setNormal(Vector3d normal) {
		this.normal = normal;
	}

	public void addBelongedCase(Case belongedCase) {
		this.belongedCases.add(belongedCase);
	}
	
	public void addNeighbour(Triangle f) {
		if(!this.isNeighboor(f)) {
			if(this.neighbour1 == null)
				neighbour1 = f;
			else if(this.neighbour2 == null)
				neighbour2 = f;
			else if(this.neighbour3 == null)
				neighbour3 = f;
			else {
				System.err.println("Errorr : more than trois voisins...");
			}
		}
	}
	
	public String toString() {
		return p0.toString() + "\n" + p1.toString() + "\n" + p2.toString() + "\n" + normal.toString() ;
	}
	
	public int getNumVoisins() {
		int i = 0;
		if(this.neighbour1 != null)
			i++;
		if(this.neighbour2 != null)
			i++;
		if(this.neighbour3 != null)
			i++;
		return i;
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
	
	public boolean isNeighboor(Triangle f) {
		return(f == neighbour1 || f == neighbour2 || f == neighbour3);
	}

	public boolean hasTwoEqualVertices(Triangle f) {
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
	
	/**
	 * Create a new Face, equal to this one in a new basis
	 * @param base The matrix (3*3)!
	 * @return The same Face in the new basis
	 */
	public Triangle changeBase(double[][] base){
		Vector3d newVect = MatrixMethod.changeBase(this.getNormal(), base);
		Point newp0 = this.p0.changeBase(base);
		Point newp1 = this.p1.changeBase(base);
		Point newp2 = this.p2.changeBase(base);
		return new Triangle(newp0, newp1, newp2, newVect);
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
		
		if(this.neighbour1 != null && !ret.contains(this.neighbour1))
			this.neighbour1.returnNeighbours(ret);
			
		if(this.neighbour2 != null && !ret.contains(this.neighbour2))
			this.neighbour2.returnNeighbours(ret);
			
		if(this.neighbour3 != null && !ret.contains(this.neighbour3))
			this.neighbour3.returnNeighbours(ret);
	}
	
	//TODO : refaire ça !
	public ArrayList<Edge> getFront() {
		if(this.getNumVoisins() == 2) {
			ArrayList<Point> list = new ArrayList<Point>();
			list.add(p0);
			list.add(p1);
			list.add(p2);
			if(		(neighbour1 != null && neighbour2 != null && neighbour1.contains(p0) && neighbour2.contains(p0))
					||	(neighbour2 != null && neighbour3 != null && neighbour2.contains(p0) && neighbour3.contains(p0))
					||	(neighbour1 != null && neighbour3 != null && neighbour1.contains(p0) && neighbour3.contains(p0)))
				list.remove(p0);
			if(		(neighbour1 != null && neighbour2 != null && neighbour1.contains(p1) && neighbour2.contains(p1))
					||	(neighbour2 != null && neighbour3 != null && neighbour2.contains(p1) && neighbour3.contains(p1))
					||	(neighbour1 != null && neighbour3 != null && neighbour1.contains(p1) && neighbour3.contains(p1)))
				list.remove(p1);
			if(		(neighbour1 != null && neighbour2 != null && neighbour1.contains(p2) && neighbour2.contains(p2))
					||	(neighbour2 != null && neighbour3 != null && neighbour2.contains(p2) && neighbour3.contains(p2))
					||	(neighbour1 != null && neighbour3 != null && neighbour1.contains(p2) && neighbour3.contains(p2)))
				list.remove(p2);
			ArrayList<Edge> l = new ArrayList<Edge>();
			l.add(new Edge(list.get(0), list.get(1), this));
			return l;
		}
		else if(this.getNumVoisins() == 1) {
			ArrayList<Edge> l = new ArrayList<Edge>();
			//TODO : normalement, il y aurait juste besoin de regarder voisin1, vu comment sont ajout�s les voisins...
			if(neighbour1 != null && neighbour1.contains(p0) && neighbour1.contains(p1)) {
				l.add(new Edge(p0, p2, this));
				l.add(new Edge(p1, p2, this));
				return l;
			}
			else if(neighbour1 != null && neighbour1.contains(p1) && neighbour1.contains(p2)) {
				l.add(new Edge(p0, p1, this));
				l.add(new Edge(p0, p2, this));
				return l;
			}
			else if(neighbour1 != null && neighbour1.contains(p0) && neighbour1.contains(p2)) {
				l.add(new Edge(p1, p0, this));
				l.add(new Edge(p1, p2, this));
				return l;
			}
			else
				return null;
		}
		else
			return null;
	}
	
	public void clearVoisins() {
		neighbour1 = null;
		neighbour2 = null;
		neighbour3 = null;
	}
}