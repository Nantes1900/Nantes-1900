package algos;

import java.util.ArrayList;

import modeles.Mesh;

public class Algos {

	public static ArrayList<Mesh> blockExtract(Mesh m) {
		ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
		Mesh buildings = new Mesh(m);
		
		int originalSize = buildings.size();
		
		while(!buildings.isEmpty())
		{
			System.out.println("Number of triangles left : " + buildings.size() + " sur : " + originalSize);
			Mesh e = new Mesh();
			buildings.getOne().returnNeighbours(e);			
			buildings.remove(e);
			buildingList.add(e);
		}
		
		return buildingList;
	}
	
//	public boolean detectChimney(Triangle t, Vector3d normalSol, double errorNormalSol, double error) {
//		Mesh memeHauteur = this.zBetween(t.zMin() - (t.zMin() + t.zMax()/2), t.zMax() + (t.zMin() + t.zMax()/2));
//		Grid quad = new Grid(memeHauteur, 10, 10, 10);
//		quad.findNeighbours();
//		Mesh e = new Mesh();
//		t.returnNeighbours(e);
//		
//		int vrai = 0;
//		
//		int compteur = 0;
//		for(Triangle tri : e) {
//			if(tri.isNormalTo(normalSol, errorNormalSol))
//				compteur ++;
//		}
//		if(compteur == e.size())
//			vrai ++;
//			
//		Point p = new Point(e.xAverage(), e.yAverage(), e.zAverage());
//		compteur = 0;
//		double moy = 0;
//		for(Triangle tri : e) {
//			moy += p.distance(tri.getCentroid());
//		}
//		moy /= e.size();
//		for(Triangle tri : e) {
//			if(p.distance(tri.getCentroid()) > moy - error && (p.distance(tri.getCentroid()) < moy + error))
//				compteur ++;
//		}
//		if(compteur == e.size())
//			vrai ++;	
//
//		if(vrai == 2)
//			return true;
//		else
//			return false;
//	}
//	
//	public boolean detectMuret(Vector3d normalSol, double errorNormalSol) {
//		Mesh orientes = this.orientedNormalTo(normalSol, 0.2);
//		
//		new Grid(orientes, 5, 5, 5).findNeighbours();
//		
//		//Extraction batiments
//		int taille = orientes.size();
//		ArrayList<Mesh> blocs = new ArrayList<Mesh>();
//		while(!orientes.isEmpty())
//		{
//			Mesh e = new Mesh();
//			orientes.getOne().returnNeighbours(e);
//			//TODO : Attention � la taille... Ca emp�che peut-�tre...
//			if(e.size() > taille/3)
//				blocs.add(e);
//			orientes.remove(e);
//		}
//		
//		//On compte le nombre de triangles par blocs
//		//Si l'un des blocs fait 1/3 de la taille, on le retient
//		
//		if(blocs.size() == 2) {
//			//Si les triangles sont tous orient�s dans le m�me sens
//			Mesh temp0 = blocs.get(0).orientedAs(blocs.get(0).averageNormal(), 0.5);
//			Mesh temp1 = blocs.get(1).orientedAs(blocs.get(1).averageNormal(), 0.5);
//			if(temp0.size() > 75*blocs.get(0).size()/100 && temp1.size() > 75*blocs.get(1).size()/100) {
//				Vector3d a = blocs.get(0).averageNormal();
//				Vector3d b = blocs.get(1).averageNormal();
//				b.negate();
//				if(a.epsilonEquals(b, 0.2)) {
//					return true;
//					//Rajouter ici d'autres tests : e.g. : v�rifier que la distance entre les murs est la distance caract�ristique.
//				}
//			}
//			//Sinon, ce n'est pas un muret
//		}
//		//Sinon, ce n'est pas un muret
//		return false;
//	}
//	
//	public Mesh extractMuret(Mesh boule, Vector3d normalSol, double errorNormalSol) {
//		Mesh muret = new Mesh();
//		
//		Mesh orientes = boule.orientedNormalTo(normalSol, 0.2);
//		
//		new Grid(orientes, 5, 5, 5).findNeighbours();
//		
//		//Extraction batiments
//		int taille = orientes.size();
//		ArrayList<Mesh> blocs = new ArrayList<Mesh>();
//		while(!orientes.isEmpty())
//		{
//			Mesh e = new Mesh();
//			orientes.getOne().returnNeighbours(e);
//			if(e.size() > taille/3)
//				blocs.add(e);
//			orientes.remove(e);
//		}
//	
//		Vector3d vector = new Vector3d();
//		vector.cross(normalSol, blocs.get(0).averageNormal());
//		
//		double[][] matrix = MatrixMethod.createOrthoBase(blocs.get(0).averageNormal(), vector, normalSol);
//		Mesh ore = this.changeBase(matrix);
//		
//		Mesh muretMalOriente = ore.xBetween(blocs.get(0).xMin(), blocs.get(0).xMax());
//		
//		double[][] matrixInv = MatrixMethod.getInversMatrix(matrix);
//		muret = muretMalOriente.changeBase(matrixInv);
//		
////		Writer.ecrireSurfaceA(new File("re0.stl"), blocs.get(0));
////		Writer.ecrireSurfaceA(new File("re1.stl"), blocs.get(1));
//		
////		EnsembleFaces mesh = this.orientesNormalA(vector, 0.3);
////		new Tuilage(mesh, 50, 50, 50).findNeighbours();
////		blocs.get(0).getOne().returnNeighbours(muret);
////		blocs.get(1).getOne().returnNeighbours(muret);
//		
//		return muret;
//	}
}
