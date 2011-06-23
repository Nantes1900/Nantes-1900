package CodeFinal;

import java.io.File;
import java.util.*;

import javax.vecmath.Vector3d;

/**
 * An ensemble of Triangle with a defined type
 */
public class EnsembleFaces extends HashSet<Triangle>{

	private static final long serialVersionUID = 1L;
	
	public EnsembleFaces() {
		super();
	}

	public EnsembleFaces(Collection<? extends Triangle> c) {
		super(c);
	}
	
	public EnsembleFaces orientesSelon(Vector3d normal, double error) {
		EnsembleFaces ret = new EnsembleFaces();
		for(Triangle f : this) {
			if(f.angularTolerance(normal, error))
				ret.add(f);
		}
		return ret;
	}
	
	public EnsembleFaces orientesPerpendiculairesA(Vector3d normal, double error) {
		EnsembleFaces ret = new EnsembleFaces();
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
		int n = 0;
		for(Triangle face : this){
			zAverage += face.zAverage();
		}
		return zAverage/n;
	}
	
	public double xAverage(){
		double xAverage = 0;
		int n = 0;
		for(Triangle face : this){
			xAverage += face.xAverage();
		}
		return xAverage/n;
	}
	
	public double yAverage(){
		double yAverage = 0;
		int n = 0;
		for(Triangle face : this){
			yAverage += face.yAverage();
		}
		return yAverage/n;
	}
	
	public double xMax() {
		double xMaxi = Double.MIN_VALUE;
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
		double yMaxi = Double.MIN_VALUE;
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
		double zMaxi = Double.MIN_VALUE;
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
			if (face.zMin()<zMini){
				zMini = face.zMin();
			}
		}
		return zMini;
	}
	
	public Triangle ZMinFace() {
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
	
	public Triangle ZMinFace(double ZMax) {
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
			return ZMinFace();
	}

	public EnsembleFaces changeBase(double[][] matrix) {
		EnsembleFaces ens = new EnsembleFaces();
		for(Triangle f : this) {
			ens.add(f.changeBase(matrix));
		}
		return ens;
	}
	
	public void suppress(EnsembleFaces aSuppr) {
		for(Triangle f : aSuppr) {
			this.remove(f);
		}
	}
	
	public EnsembleFaces zBetween(double min, double max) {
		EnsembleFaces ens = new EnsembleFaces();
		for(Triangle t : this) {
			if(t.zMax() < max && t.zMin() > min)
				ens.add(t);
		}
		return ens;
	}
	
	public boolean detectChimney(Triangle t, Vector3d normalSol, double errorNormalSol, double error) {
		EnsembleFaces memeHauteur = this.zBetween(t.zMin() - (t.zMin() + t.zMax()/2), t.zMax() + (t.zMin() + t.zMax()/2));
		Tuilage quad = new Tuilage(memeHauteur, 10, 10, 10);
		quad.findNeighbours();
		EnsembleFaces e = new EnsembleFaces();
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
	
	public boolean detectMuret(Triangle t, Vector3d normalSol, double errorNormalSol) {
		EnsembleFaces orientes = this.orientesPerpendiculairesA(normalSol, 20*errorNormalSol);
		
		Tuilage quad = new Tuilage(orientes, 10, 10, 10);
		quad.findNeighbours();
		
//		Writer.ecrireSurfaceA(new File("testdetectMuret.stl"), orientes);
		
		//Extraction batiments
		int taille = orientes.size();
		ArrayList<EnsembleFaces> blocs = new ArrayList<EnsembleFaces>();
		while(!orientes.isEmpty())
		{
			EnsembleFaces e = new EnsembleFaces();
			orientes.iterator().next().returnNeighbours(e);
			if(e.size() > 1)
				blocs.add(e);
			orientes.suppress(e);
		}
		
		//On compte le nombre de triangles par blocs
		//Si l'un des blocs fait 1/3 de la taille, on le retient
		ArrayList<EnsembleFaces> blocsGros = new ArrayList<EnsembleFaces>();
		for(EnsembleFaces e : blocs) {
			if(e.size() > taille/3)
				blocsGros.add(e);
		}
		if(blocsGros.size() >= 2) {
			Vector3d a = blocsGros.get(0).averageNormal();
			Vector3d b = blocsGros.get(1).averageNormal();
			b.negate();
			if(a.epsilonEquals(b, 0.1)) {
				return true;
				//Rajouter ici d'autres tests : e.g. : vérifier que la distance entre les murs est la distance caractéristique.
			}
			//Sinon, ce n'est pas un muret
		}
		//Sinon, ce n'est pas un muret
		return false;
	}
	
	public EnsembleFaces extractMuret(Triangle t, EnsembleFaces boule, Vector3d normalSol, double errorNormalSol) {
		EnsembleFaces muret = new EnsembleFaces();
		
		EnsembleFaces orientes = boule.orientesPerpendiculairesA(normalSol, 20*errorNormalSol);
		
		Tuilage quad = new Tuilage(orientes, 10, 10, 10);
		quad.findNeighbours();
		
		//Extraction batiments
		int taille = orientes.size();
		ArrayList<EnsembleFaces> blocs = new ArrayList<EnsembleFaces>();
		while(!orientes.isEmpty())
		{
			EnsembleFaces e = new EnsembleFaces();
			orientes.iterator().next().returnNeighbours(e);
			if(e.size() > 1)
				blocs.add(e);
			orientes.suppress(e);
		}
		ArrayList<EnsembleFaces> blocsGros = new ArrayList<EnsembleFaces>();
		for(EnsembleFaces e : blocs) {
			if(e.size() > taille/3)
				blocsGros.add(e);
		}
		
		EnsembleFaces ore0 = this.orientesSelon(blocs.get(0).averageNormal(), 10*errorNormalSol);
		EnsembleFaces ore1 = this.orientesSelon(blocs.get(1).averageNormal(), 10*errorNormalSol);
		Tuilage quad2 = new Tuilage(ore0, 1, 1, 1);
		quad2.findNeighbours();
		Tuilage quad3 = new Tuilage(ore1, 1, 1, 1);
		quad3.findNeighbours();
		
		EnsembleFaces neighbours0 = new EnsembleFaces();
		EnsembleFaces neighbours1 = new EnsembleFaces();
		ore0.iterator().next().returnNeighbours(neighbours0);
		ore1.iterator().next().returnNeighbours(neighbours1);
		
		muret.addAll(neighbours0);
		muret.addAll(neighbours1);
		
		return muret;
	}
	
	public EnsembleFaces getInBounds(Triangle t, double tailleBoule) {
		EnsembleFaces ens = new EnsembleFaces();
		for(Triangle tri : this) {
			Point p = t.getCentroid();
			if(p.distance3D(tri.getCentroid()) < tailleBoule)
				ens.add(tri);
		}
		return ens;
	}
	
	public Triangle getOutOfIndex(HashSet<Point> index, double tailleBoule) {
		boolean tag = true;
		for(Triangle t : this) {
			Point p = t.getCentroid();
			if(index.isEmpty())
				return t;
			for(Point point : index) {
				if(p.distance3D(point) < tailleBoule/(double)2) {
					tag = false;
					break;
				}
			}
			if(tag)
				return t;
			else
				tag = true;
		}
		return null;
	}
}
