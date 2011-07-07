package algos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import modeles.Border;
import modeles.Edge;
import modeles.Mesh;
import modeles.Point;
import modeles.Triangle;

public class Algos {

	public static ArrayList<Mesh> blockExtract(Mesh m) {
		LinkedList<Mesh> thingsList = new LinkedList<Mesh>();
		Mesh mesh = new Mesh(m);
		
		int size = mesh.size();

		while(!mesh.isEmpty())
		{
			System.out.println("Number of triangles left : " + mesh.size() + " on " + size);
			Mesh e = new Mesh();
			mesh.getOne().returnNeighbours(e, mesh);
			mesh.remove(e);
			if(e.size()>1)
				System.err.println("Yeah !");
			thingsList.add(e);
		}

		return new ArrayList<Mesh>(thingsList);
	}


	public static ArrayList<Mesh> blockOrientedExtract(Mesh m, double angleNormalErrorFactor) {
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();
		Mesh mesh = new Mesh(m);
		
		int size = mesh.size();

		while(!mesh.isEmpty()) {
			System.out.println("Number of triangles left : " + mesh.size() + " on " + size);
			Mesh e = new Mesh();
			Triangle tri = mesh.getOne();
			Mesh oriented = mesh.orientedAs(tri.getNormal(), angleNormalErrorFactor);
			tri.returnNeighbours(e, oriented);
			mesh.remove(e);
			thingsList.add(e);
		}

		return thingsList;
	}

	public static Border orderBorder(Border bound) {
		Border oriented = new Border();

		//		if(bound.edgeSize() < 2)
		//			throw new InvalidParameterException("Border too short !");

		if(bound.edgeSize() > 1)
		{
			Edge e = bound.getEdgeList().get(0);
			Point first = e.getP1();
			oriented.getEdgeList().add(e);
			oriented.getPointList().add(e.getP1());
			oriented.getPointList().add(e.getP2());
			bound.remove(e);

			Point p = e.getP2();

			int counter = 0;

			do {
				Iterator<Edge> i = bound.getEdgeList().iterator();
				do { e = i.next(); }
				while(i.hasNext() && !e.contains(p));
				//FIXME : si on arrive à la fin de la liste sans avoir trouvé ?
				if(e.getP1() != p) {
					p = e.getP1();
				}
				else {
					p = e.getP2();
				}
				oriented.getEdgeList().add(e);
				oriented.getPointList().add(p);

				counter ++;
			}
			while(p != first && counter <= bound.getPointList().size());

			if(counter == bound.getPointList().size()) {
				System.err.println("Erreur : boucle infinie");
				return null;
			}

			return oriented;
		}
		else
			return bound;
	}

	//TODO : put it in the Polyline class
	public static Border reduce(Border line, int numberOfReduction) {
		Border ret = new Border();
		ArrayList<Point> pointList = line.getPointList();
		int position = 0;

		while(position < pointList.size() - numberOfReduction) {
			//On en prend 1 sur numberOfReduction
			ret.getPointList().add(pointList.get(position));
			position += numberOfReduction;
			//Mettre à jour les Edge
		}

		//		while(position < pointList.size() - numberOfReduction) {
		//			ArrayList<Point> l = new ArrayList<Point>();
		//			for(int i = 0; i < numberOfReduction; i ++) {
		//				l.add(pointList.get(position + i));
		//			}
		//
		//			position += numberOfReduction;
		//
		//			ret.getPointList().add(Algos.average(l));
		//		}

		//		while(position < pointList.size() - 1) {
		//			ArrayList<Point> l = new ArrayList<Point>();
		//			l.add(pointList.get(position));
		//			double length = 0;
		//			
		//			while(length < pace && position < pointList.size() - 1) {
		//				l.add(pointList.get(position + 1));
		//				length += pointList.get(position + 1).distance(pointList.get(position));
		//				position ++;
		//			}
		//			
		//			ret.getPointList().add(Algos.average(l));
		//			//FIXME : ajouter l'edge aussi !
		//		}

		return ret;
	}

	public static Point average(List<Point> points) {
		double xAverage = 0, yAverage = 0, zAverage = 0;

		for(Point p : points) {
			xAverage += p.getX();
			yAverage += p.getY();
			zAverage += p.getZ();
		}

		return new Point(xAverage, yAverage, zAverage);
	}


	public static Border returnLongestBorder(ArrayList<Border> boundList) {
		Border bound = new Border();

		double max = Double.MIN_VALUE;

		for(Border f : boundList) {
			if(f.distance() > max) {
				max = f.distance();
				bound = f;
			}
		}

		return bound;
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
