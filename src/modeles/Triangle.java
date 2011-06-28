package modeles;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;


public class Triangle {
	protected Point p0;
	protected Point p1;
	protected Point p2;
	
	protected Vector3d normale;
	
	protected Triangle voisin1;
	protected Triangle voisin2;
	protected Triangle voisin3;
	
	protected ArrayList<Case> belongedCases;

	public Triangle(Point p0, Point p1, Point p2, Vector3d normale) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.normale = normale;
		this.voisin1 = null;
		this.voisin2 = null;
		this.voisin3 = null;
		belongedCases = new ArrayList<Case>();
	}
	
	public Triangle() {
		p0 = new Point();
		p1 = new Point();
		p2 = new Point();
		this.normale = new Vector3d();
		this.voisin1 = null;
		this.voisin2 = null;
		this.voisin3 = null;
		belongedCases = new ArrayList<Case>();
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

	/**
	 * Get the vector3d containing the normale of the face.
	 * @return the vector3d
	 */
	public Vector3d getNormale() {
		return normale;
	}
	
	/**
	 * Set the normale of the face.
	 * @param normale the vector3d we want to set as normale
	 */
	public void setNormale(Vector3d normale) {
		this.normale = normale;
	}

	public void addBelongedCase(Case belongedCase) {
		this.belongedCases.add(belongedCase);
	}
	
	public void addVoisin(Triangle f) {
		if(!this.isNeighboor(f)) {
			if(this.voisin1 == null)
				voisin1 = f;
			else if(this.voisin2 == null)
				voisin2 = f;
			else if(this.voisin3 == null)
				voisin3 = f;
			else {
				System.err.println("Erreur : plus de trois voisins...");
				System.err.println(this);
				System.err.println(this.voisin1);
				System.err.println(this.voisin2);
				System.err.println(this.voisin3);
				System.err.println(f);
			}
		}
	}
	
	public String toString() {
		return p0.toString() + "\n" + p1.toString() + "\n" + p2.toString() + "\n" + normale.toString() ;
	}
	
	public int getNumVoisins() {
		int i = 0;
		if(this.voisin1 != null)
			i++;
		if(this.voisin2 != null)
			i++;
		if(this.voisin3 != null)
			i++;
		return i;
	}
	
	/**
	 * Check if the angle between the normal of this face and a vector is less or equal than the error
	 * @param face The Vector3d we want to compare to.
	 * @param erreur The double containing the tolerance.
	 * @return boolean if true or not
	 */
	public boolean angularTolerance(Vector3d vector, double error) {
		return(this.normale.angle(vector) < error);
	}
	
	/**
	 * Check if the angle between two face's normale is less than erreur.
	 * @param face The face we want to compare to.
	 * @param erreur The double containing the tolerance.
	 * @return boolean if true or not
	 */
	public boolean angularTolerance(Triangle face, double error) {
		return(this.angularTolerance(face.normale, error));
	}
	
	//TODO : A TESTER !!
	public boolean estNormalA(Vector3d normal, double error) {
		return((this.normale.dot(normal) < error) && (this.normale.dot(normal) > -error));
	}

	public boolean contains(Point p) {
		return(p0.equals(p) || p1.equals(p) || p2.equals(p));
	}
	
	public boolean isNeighboor(Triangle f) {
		return(f == voisin1 || f == voisin2 || f == voisin3);
	}

	public boolean hasTwoEqualVertices(Triangle f) {
		return(		(this.contains(f.p0) && this.contains(f.p1)) 
				|| 	(this.contains(f.p1) && this.contains(f.p2)) 
				|| 	(this.contains(f.p0) && this.contains(f.p2))
			   );
	}
	
	public void returnNeighbours(Mesh ret) {
		ret.add(this);
		
		if(this.voisin1 != null && !ret.contains(this.voisin1))
			this.voisin1.returnNeighbours(ret);
			
		if(this.voisin2 != null && !ret.contains(this.voisin2))
			this.voisin2.returnNeighbours(ret);
			
		if(this.voisin3 != null && !ret.contains(this.voisin3))
			this.voisin3.returnNeighbours(ret);
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
		Point mid = new Point((p0._x + p1._x)/(double)2, (p0._y + p1._y)/(double)2, (p0._z + p1._z)/(double)2);
		return new Point((mid._x + 2*p2._x)/(double)3, (mid._y + 2*p2._y)/(double)3, (mid._z + 2*p2._z)/(double)3);
	}
	
	public Point yMaxPoint() {
		Point p = p0;
		double max = p0._y;
		if(p1._y > max) {
			p = p1;
			max = p1._y;
		}
		if(p2._y > max) {
			p = p2;
		}
		return p;
	}
	
	public Point yMinPoint() {
		Point p = p0;
		double min = p0._y;
		if(p1._y < min) {
			p = p1;
			min = p1._y;
		}
		if(p2._y < min) {
			p = p2;
		}
		return p;
	}
	
	public Point zMaxPoint() {
		Point p = p0;
		double max = p0._z;
		if(p1._z > max) {
			p = p1;
			max = p1._z;
		}
		if(p2._z > max) {
			p = p2;
		}
		return p;
	}
	
	/**
	 * Create a new Face, equal to this one in a new basis
	 * @param base The matrix (3*3)!
	 * @return The same Face in the new basis
	 */
	public Triangle changeBase(double[][] base){
		Vector3d newVect = MatrixMethod.changeBase(this.getNormale(), base);
		Point newp0 = this.p0.changeBase(base);
		Point newp1 = this.p1.changeBase(base);
		Point newp2 = this.p2.changeBase(base);
		return new Triangle(newp0, newp1, newp2, newVect);
	}
	
	public Triangle zProjection(double z) {
		Point np0 = this.p0;
		Point np1 = this.p1;
		Point np2 = this.p2;
		np0.setZ(z);
		np1.setZ(z);
		np2.setZ(z);
		return new Triangle(np0, np1, np2, this.normale);
	}
	
	public Triangle xProjection(double x) {
		Point np0 = this.p0;
		Point np1 = this.p1;
		Point np2 = this.p2;
		np0.setX(x);
		np1.setX(x);
		np2.setX(x);
		return new Triangle(np0, np1, np2, this.normale);
	}
	
	//A utiliser seulement si NumVoisins == 2 !
	public ArrayList<Edge> getFront() {
		if(this.getNumVoisins() == 2) {
			ArrayList<Point> list = new ArrayList<Point>();
			list.add(p0);
			list.add(p1);
			list.add(p2);
			if(		(voisin1 != null && voisin2 != null && voisin1.contains(p0) && voisin2.contains(p0))
					||	(voisin2 != null && voisin3 != null && voisin2.contains(p0) && voisin3.contains(p0))
					||	(voisin1 != null && voisin3 != null && voisin1.contains(p0) && voisin3.contains(p0)))
				list.remove(p0);
			if(		(voisin1 != null && voisin2 != null && voisin1.contains(p1) && voisin2.contains(p1))
					||	(voisin2 != null && voisin3 != null && voisin2.contains(p1) && voisin3.contains(p1))
					||	(voisin1 != null && voisin3 != null && voisin1.contains(p1) && voisin3.contains(p1)))
				list.remove(p1);
			if(		(voisin1 != null && voisin2 != null && voisin1.contains(p2) && voisin2.contains(p2))
					||	(voisin2 != null && voisin3 != null && voisin2.contains(p2) && voisin3.contains(p2))
					||	(voisin1 != null && voisin3 != null && voisin1.contains(p2) && voisin3.contains(p2)))
				list.remove(p2);
			ArrayList<Edge> l = new ArrayList<Edge>();
			l.add(new Edge(list.get(0), list.get(1), this));
			return l;
		}
		else if(this.getNumVoisins() == 1) {
			ArrayList<Edge> l = new ArrayList<Edge>();
			//TODO : normalement, il y aurait juste besoin de regarder voisin1, vu comment sont ajout�s les voisins...
			if(voisin1 != null && voisin1.contains(p0) && voisin1.contains(p1)) {
				l.add(new Edge(p0, p2, this));
				l.add(new Edge(p1, p2, this));
				return l;
			}
			else if(voisin1 != null && voisin1.contains(p1) && voisin1.contains(p2)) {
				l.add(new Edge(p0, p1, this));
				l.add(new Edge(p0, p2, this));
				return l;
			}
			else if(voisin1 != null && voisin1.contains(p0) && voisin1.contains(p2)) {
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
		voisin1 = null;
		voisin2 = null;
		voisin3 = null;
	}
}