package modeles;

import java.io.File;
import java.util.*;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Writer;


/**
 * An ensemble of Triangle with a defined type
 */
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
	
	public Mesh orientesNormalA(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.estNormalA(normal, error))
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
			average.add(face.getNormale());
		}
		average.scale(1/(double)n);
		return average;
	}
	
	public double quadricNormalError(){
		Vector3d normal = this.averageNormal();
		double error = 0;
		for(Triangle f : this)
			error += Math.pow(f.normale.angle(normal), 2);
		Math.pow(error, 0.5);
		return (error/this.size());
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
		double xMini = Double.MAX_VALUE;
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
		double yMini = Double.MAX_VALUE;
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
		double zMini = Double.MAX_VALUE;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				zMini = face.zMin();
			}
		}
		return zMini;
	}
	
	public Triangle zMinFace() {
		Triangle triangle = null;
		double zMini = Double.MAX_VALUE;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				zMini = face.zMin();
				triangle = face;
			}
		}
		return triangle;
	}
	
	public Point yMaxPoint() {
		Point p = null;
		double yMax = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMax() > yMax){
				yMax = face.yMax();
				p = face.yMaxPoint();
			}
		}
		return p;
	}
	
	public Point yMinPoint() {
		Point p = null;
		double yMin = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMin() < yMin){
				yMin = face.yMin();
				p = face.yMinPoint();
			}
		}
		return p;
	}
	
	public Point zMaxPoint() {
		Point p = null;
		double zMax = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMax() > zMax){
				zMax = face.zMax();
				p = face.zMaxPoint();
			}
		}
		return p;
	}
	
	public Triangle zMinFace(double ZMax) {
		Triangle triangle = null;
		for (Triangle face : this){
			if (face.zMin() < ZMax) {
				triangle = face;
				break;
			}
		}
		if(triangle != null)
			return triangle;
		else
			return zMinFace();
	}
	
	public Triangle getOne() {
		return this.iterator().next();
	}
	
	public void remove(Mesh aSuppr) {
		for(Triangle f : aSuppr) {
			this.remove(f);
		}
	}

	public Mesh changeBase(double[][] matrix) {
		Mesh ens = new Mesh();
		for(Triangle f : this) {
			ens.add(f.changeBase(matrix));
		}
		return ens;
	}
	
	public void lowest(Mesh aSuppr) {
		for(Triangle f : aSuppr) {
			this.remove(f);
		}
	}
	
	public Mesh zBetween(double min, double max) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.zMax() < max && t.zMin() > min)
				ens.add(t);
		}
		return ens;
	}
	
	public Mesh xBetween(double min, double max) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.xMax() < max && t.xMin() > min)
				ens.add(t);
		}
		return ens;
	}
	
	public Mesh yBetween(double min, double max) {
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.yMax() < max && t.yMin() > min)
				ens.add(t);
		}
		return ens;
	}
	
	public boolean detectChimney(Triangle t, Vector3d normalSol, double errorNormalSol, double error) {
		Mesh memeHauteur = this.zBetween(t.zMin() - (t.zMin() + t.zMax()/2), t.zMax() + (t.zMin() + t.zMax()/2));
		Grid quad = new Grid(memeHauteur, 10, 10, 10);
		quad.findNeighbours();
		Mesh e = new Mesh();
		t.returnNeighbours(e);
		
		int vrai = 0;
		
		int compteur = 0;
		for(Triangle tri : e) {
			if(tri.estNormalA(normalSol, errorNormalSol))
				compteur ++;
		}
		if(compteur == e.size())
			vrai ++;
			
		Point p = new Point(e.xAverage(), e.yAverage(), e.zAverage());
		compteur = 0;
		double moy = 0;
		for(Triangle tri : e) {
			moy += p.distance3D(tri.getCentroid());
		}
		moy /= e.size();
		for(Triangle tri : e) {
			if(p.distance3D(tri.getCentroid()) > moy - error && (p.distance3D(tri.getCentroid()) < moy + error))
				compteur ++;
		}
		if(compteur == e.size())
			vrai ++;	

		if(vrai == 2)
			return true;
		else
			return false;
	}
	
	public boolean detectMuret(Vector3d normalSol, double errorNormalSol) {
		Mesh orientes = this.orientesNormalA(normalSol, 0.2);
		
		new Grid(orientes, 5, 5, 5).findNeighbours();
		
		//Extraction batiments
		int taille = orientes.size();
		ArrayList<Mesh> blocs = new ArrayList<Mesh>();
		while(!orientes.isEmpty())
		{
			Mesh e = new Mesh();
			orientes.getOne().returnNeighbours(e);
			//TODO : Attention � la taille... Ca emp�che peut-�tre...
			if(e.size() > taille/3)
				blocs.add(e);
			orientes.remove(e);
		}
		
		//On compte le nombre de triangles par blocs
		//Si l'un des blocs fait 1/3 de la taille, on le retient
		
		if(blocs.size() == 2) {
			//Si les triangles sont tous orient�s dans le m�me sens
			Mesh temp0 = blocs.get(0).orientedAs(blocs.get(0).averageNormal(), 0.5);
			Mesh temp1 = blocs.get(1).orientedAs(blocs.get(1).averageNormal(), 0.5);
			if(temp0.size() > 75*blocs.get(0).size()/100 && temp1.size() > 75*blocs.get(1).size()/100) {
				Vector3d a = blocs.get(0).averageNormal();
				Vector3d b = blocs.get(1).averageNormal();
				b.negate();
				if(a.epsilonEquals(b, 0.2)) {
					return true;
					//Rajouter ici d'autres tests : e.g. : v�rifier que la distance entre les murs est la distance caract�ristique.
				}
			}
			//Sinon, ce n'est pas un muret
		}
		//Sinon, ce n'est pas un muret
		return false;
	}
	
	public Mesh extractMuret(Mesh boule, Vector3d normalSol, double errorNormalSol) {
		Mesh muret = new Mesh();
		
		Mesh orientes = boule.orientesNormalA(normalSol, 0.2);
		
		new Grid(orientes, 5, 5, 5).findNeighbours();
		
		//Extraction batiments
		int taille = orientes.size();
		ArrayList<Mesh> blocs = new ArrayList<Mesh>();
		while(!orientes.isEmpty())
		{
			Mesh e = new Mesh();
			orientes.getOne().returnNeighbours(e);
			if(e.size() > taille/3)
				blocs.add(e);
			orientes.remove(e);
		}
	
		Vector3d vector = new Vector3d();
		vector.cross(normalSol, blocs.get(0).averageNormal());
		
		double[][] matrix = MatrixMethod.createOrthoBase(blocs.get(0).averageNormal(), vector, normalSol);
		Mesh ore = this.changeBase(matrix);
		
		Mesh muretMalOriente = ore.xBetween(blocs.get(0).xMin(), blocs.get(0).xMax());
		
		double[][] matrixInv = MatrixMethod.getInversMatrix(matrix);
		muret = muretMalOriente.changeBase(matrixInv);
		
//		Writer.ecrireSurfaceA(new File("re0.stl"), blocs.get(0));
//		Writer.ecrireSurfaceA(new File("re1.stl"), blocs.get(1));
		
//		EnsembleFaces mesh = this.orientesNormalA(vector, 0.3);
//		new Tuilage(mesh, 50, 50, 50).findNeighbours();
//		blocs.get(0).getOne().returnNeighbours(muret);
//		blocs.get(1).getOne().returnNeighbours(muret);
		
		return muret;
	}
	
	public Mesh getInBounds(Point p, double tailleBoule) {
		Mesh ens = new Mesh();
		for(Triangle tri : this) {
			if(p.distance3D(tri.getCentroid()) < tailleBoule)
				ens.add(tri);
		}
		return ens;
	}
	
//	public Triangle getOutOfIndex(HashSet<Point> index, double tailleBoule) {
//		boolean tag = true;
//		for(Triangle t : this) {
//			Point p = t.getCentroid();
//			if(index.isEmpty())
//				return t;
//			for(Point point : index) {
//				if(p.distance3D(point) < tailleBoule/(double)2) {
//					tag = false;
//					break;
//				}
//			}
//			if(tag)
//				return t;
//			else
//				tag = true;
//		}
//		return null;
//	}
	
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
	
	public ArrayList<Frontiere> determinerFrontieres() {
		ArrayList<Frontiere> e = new ArrayList<Frontiere>();
//		boolean tag = true;

		Frontiere front = new Frontiere();
		for(Triangle tri : this) {
			if(tri.getNumVoisins() < 3)
				front.addAll(tri.getFront());
		}

//		Writer.ecrireSurfaceA(new File("front.stl"), front.returnMesh());
		
		while(!front.edgeSet.isEmpty()) {
			Edge arete = front.edgeSet.iterator().next();
			Frontiere ret = new Frontiere();
			front.returnNeighbours(ret, arete);
			e.add(ret);
			front.suppress(ret);
//			for(Frontiere f : e) {
//				if(f.containsPoint(arete.p1) || f.containsPoint(arete.p2)) {
//					f.add(arete);
//					tag = false;
//					break;
//				}
//			}
//			if(tag == true) {
//				Frontiere f2 = new Frontiere();
//				f2.add(arete);
//				e.add(f2);
//			}
//			tag = true;
		}
//		
//		System.out.println(e.size());
		
		return e;
	}
	
	public void clearNeighbours() {
		for(Triangle t : this) {
			t.clearVoisins();
		}
	}
	
	public void write(String fileName) {
		Writer.ecrireSurfaceA(new File(fileName), this);
		System.out.println(fileName + " written !");
	}
}
