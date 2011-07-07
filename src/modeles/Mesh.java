package modeles;

import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Writer;

public class Mesh extends HashSet<Triangle>{

	private static final long serialVersionUID = 1L;

	public Mesh() {
		super();
	}

	public Mesh(Collection<? extends Triangle> c) {
		super(c);
	}

	public Mesh orientedAs(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.angularTolerance(normal, error))
				ret.add(f);
		}
		return ret;
	}

	public Mesh orientedNormalTo(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.isNormalTo(normal, error))
				ret.add(f);
		}
		return ret;
	}

	/**
	 * Compute the average normal of all Faces composing this Ensemble
	 * @return average The average Vector3d normal.
	 */
	public Vector3d averageNormal(){
		int n = this.size();
		Vector3d average = new Vector3d();
		for(Triangle face : this){
			average.add(face.getNormal());
		}
		average.scale(1/(double)n);

		return average;
	}

	/**
	 * Compute the average z-coordinate of all points of all faces from this Ensemble
	 * @return the average z-coordinate of all points
	 */
	public double zAverage(){
		double zAverage = 0;
		for(Triangle face : this){
			zAverage += face.zAverage();
		}
		return zAverage/this.size();
	}

	public double xAverage(){
		double xAverage = 0;
		for(Triangle face : this){
			xAverage += face.xAverage();
		}
		return xAverage/this.size();
	}

	public double yAverage(){
		double yAverage = 0;
		for(Triangle face : this){
			yAverage += face.yAverage();
		}
		return yAverage/this.size();
	}

	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.xMax() > xMaxi){
				xMaxi = face.xMax();
			}
		}
		return xMaxi;
	}

	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.xMin() < xMini){
				xMini = face.xMin();
			}
		}
		return xMini;
	}

	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMax() > yMaxi){
				yMaxi = face.yMax();
			}
		}
		return yMaxi;
	}

	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMin() < yMini){
				yMini = face.yMin();
			}
		}
		return yMini;
	}

	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMax() > zMaxi){
				zMaxi = face.zMax();
			}
		}
		return zMaxi;
	}

	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				zMini = face.zMin();
			}
		}
		return zMini;
	}	

	public double xLengthAverage() {
		double xLengthAve = 0;
		for(Triangle t : this) {
			xLengthAve += (t.xMax() - t.xMin());
		}
		return xLengthAve/(double)this.size();
	}

	public double yLengthAverage() {
		double yLengthAve = 0;
		for(Triangle t : this) {
			yLengthAve += (t.yMax() - t.yMin());
		}
		return yLengthAve/(double)this.size();
	}

	public double zLengthAverage() {
		double zLengthAve = 0;
		for(Triangle t : this) {
			zLengthAve += (t.zMax() - t.zMin());
		}
		return zLengthAve/(double)this.size();
	}

	public Triangle zMinFace() {
		Triangle t = null;
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				t = face;
				zMini = face.zMin();
			}
		}
		return t;
	}
	
	public Triangle faceUnderZ(double zMax) {
		for (Triangle t : this) {
			if (t.zMin() < zMax) {
				return t;
			}
		}
		return null;
	}

	public Triangle getOne() {
		return this.iterator().next();
	}

	public void remove(Mesh aSuppr) {
		this.removeAll(aSuppr);
	}

	//FIXME : supprimer l'ancienne version, car elle est foutue.
	//FIXME : prévoir de faire une copie de l'ancienne version...
	public Mesh changeBase(double[][] matrix) {
		HashSet<Point> set = new HashSet<Point>();
		
		for(Triangle f : this) {
			set.addAll(f.getPoints());
			MatrixMethod.changeBase(f.getNormal(), matrix);
		}

		for(Point p : set)
			p.changeBase(matrix);

		return new Mesh(this);
	}

	public Mesh zBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.zMax() < Math.max(m1, m2) && t.zMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	public Mesh xBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.xMax() < Math.max(m1, m2) && t.xMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	public Mesh yBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.yMax() < Math.max(m1, m2) && t.yMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	public Mesh zProjection(double z) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.zProjection(z));
		}
		return e;
	}

	public Mesh xProjection(double x) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.xProjection(x));
		}
		return e;
	}

	//TODO : à refaire !
//	public ArrayList<Border> returnBounds() {
//		ArrayList<Border> e = new ArrayList<Border>();
//		//		ArrayList<Border> eFin = new ArrayList<Border>();
//
//		Border front = new Border();
//		for(Triangle tri : this) {
//			if(tri.getNumVoisins() < 3 && tri.getNumVoisins() > 0)
//				front.addAll(tri.getFront());
//		}
//
//		while(!front.getEdgeList().isEmpty()) {
//			Edge arete = front.getEdgeList().iterator().next();
//			Border ret = new Border();
//			front.returnNeighbours(ret, arete);
//			e.add(ret);
//			front.remove(ret);
//		}
//
//		//		//Si certains possède des frontières doubles, il faut créer deux frontières
//		//		for(Border b : e) {
//		////			System.out.println(b.edgeSize());
//		//			Border bFin = Algos.orderBorder(b);
//		////			System.out.println(bFin.edgeSize());
//		//			eFin.add(bFin);
//		//		}
//
//		return e;
//	}

	public void write(String fileName) {
		Writer.write(fileName, this);
	}
}