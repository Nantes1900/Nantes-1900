package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Writer;

//TODO : doc !
public class Mesh extends HashSet<Triangle>{

	private static final long serialVersionUID = 1L;

	public Mesh() {
		super();
	}

	public Mesh(Collection<? extends Triangle> c) {
		super(c);
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

	public Mesh zBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.zMax() < Math.max(m1, m2) && t.zMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	//TODO : take care of the fact that new Triangle without order, and without neighbours are created !
	//Caution !
	//FIXME : the actual key table is destroyed :)
	public Mesh xProjection(double x) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.xProjection(x));
		}
		return e;
	}

	public Mesh yProjection(double y) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.yProjection(y));
		}
		return e;
	}

	public Mesh zProjection(double z) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.zProjection(z));
		}
		return e;
	}


	public Triangle zMinFace() {
		Triangle t = null;
		if(this.isEmpty())
			throw new InvalidParameterException("Empty mesh !");
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				t = face;
				zMini = face.zMin();
			}
		}
		return t;
	}

	//TODO : improve the velocity of this method !

	public Triangle faceUnderZ(double zMax) {
		for (Triangle t : this) {
			if (t.zMax() < zMax) {
				return t;
			}
		}
		return null;
	}


	public Triangle getOne() {
		return this.iterator().next();
	}


	public void remove(Mesh m) {
		this.removeAll(m);
	}


	public void write(String fileName) {
		Writer.write(fileName, this);
	}


	public Mesh orientedAs(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.angularTolerance(normal, error))
				ret.add(f);
		}
		return ret;
	}


	//Caution : this is not in degrees
	//The factor is between 0 and 1.
	public Mesh orientedNormalTo(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.isNormalTo(normal, error))
				ret.add(f);
		}
		return ret;
	}


	public void changeBase(double[][] matrix) {
		if(matrix == null)
			throw new InvalidParameterException();

		HashSet<Point> set = new HashSet<Point>();
		HashSet<Triangle> mesh = new HashSet<Triangle>();

		for(Triangle f : this) {
			set.addAll(f.getPoints());
			MatrixMethod.changeBase(f.getNormal(), matrix);
			mesh.add(f);
		}

		for(Point p : set)
			p.changeBase(matrix);

		this.clear();
		this.addAll(mesh);
	}

	//FIXME !
	public Polyline returnUnsortedBounds() {
		Polyline bounds = new Polyline();
		for(Triangle tri : this) {
			ArrayList<Edge> edges = new ArrayList<Edge>(tri.getEdges());
			for(Edge e : edges) {
				int counter = 0;
				for(Triangle t : e.getTriangleList()) {
					if(this.contains(t))
						counter ++;
				}
				if(counter == 1)
					bounds.add(e);
			}
		}
		return bounds;
	}

	public ArrayList<Polyline> returnSortedBounds(Polyline bounds) {
		ArrayList<Polyline> boundList = new ArrayList<Polyline>();

		while(!bounds.isEmpty()) {
			Edge arete = bounds.getOne();
			Polyline p = new Polyline();

			arete.returnNeighbours(p, bounds);

			boundList.add(p);
			//FIXME : la méthode remove ne supprime pas les Points, seulement les Edges.
			bounds.remove(p);
		}

		return boundList;
	}

	public ArrayList<Polyline> separateBounds(ArrayList<Polyline> boundList) {
		ArrayList<Polyline> toAddList = new ArrayList<Polyline>();

		int counter = 0;
		Point weirdPoint = null;

		for(Polyline border : boundList) {
			for(Point p : border.getPointList()) {
				if(border.getNumNeighbours(p) != 2) {
					if(border.getNumNeighbours(p) == 4) {
						counter ++;
						weirdPoint = p;
					}
					else {
						System.err.println("Error !");
						//TODO : throw new Exception !
					}
				}
			}

			if (counter > 0) {
				if(counter > 1) {				
					System.err.println("Too much weird points !");
					//TODO : throw new Exception();
				}
				else {
					//Here are separated the two different Polylines : they still share one Point : caution !
					toAddList.add(this.treatWeirdCase(border, weirdPoint));
				}
			}
		}

		toAddList.addAll(boundList);

		return toAddList;
	}

	public Polyline treatWeirdCase(Polyline border, Point weirdPoint) {
		Polyline otherOne = new Polyline();

		ArrayList<Edge> weirdEdges = border.getNeighbours(weirdPoint);
		Edge weirdEdge = weirdEdges.get(0);

		Point p = weirdEdge.returnOther(weirdPoint);
		Edge e = weirdEdge.returnNeighbour(p, border);

		Edge temp;

		while(!weirdEdges.contains(e)) {
			temp = e;
			otherOne.add(temp);
			p = e.returnOther(p);
			e = e.returnNeighbour(p, border);
			border.remove(temp);
		}

		otherOne.add(e);
		border.remove(e);

		otherOne.add(weirdEdge);
		border.remove(weirdEdge);

		return otherOne;
	}

	public ArrayList<Polyline> returnBounds() {

		Polyline bounds = this.returnUnsortedBounds();

		ArrayList<Polyline> boundList = this.returnSortedBounds(bounds);

		boundList = this.separateBounds(boundList);

		return boundList;
	}

	public Polyline returnLongestBound() {
		ArrayList<Polyline> boundList = this.returnBounds();
		Polyline ret = new Polyline();

		double max = Double.MIN_VALUE;
		for(Polyline p : boundList) {
			if(p.distance() > max){
				max = p.distance();
				ret = p;
			}
		}

		return ret;
	}
}