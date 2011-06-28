package algos;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Parser;
import utils.Writer;

import modeles.Batiment;
import modeles.Mesh;
import modeles.Frontiere;
import modeles.Point;
import modeles.Triangle;
import modeles.Grid;


public class Test7 {
	public static void main(String[] args) {
		try {
			int compt = 1;
			int compteurBat = 1;
			int compteurToit = 1;

			Mesh sol = new Mesh(Parser.readSTLA(new File("sol.stl")));
			System.out.println("Lecture du fichier sol termin�e !");
			System.out.println("Nombre de triangles : " + sol.size());

			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();

			ArrayList<Mesh> toits = new ArrayList<Mesh>();

			while(new File("batiment - " + compt + " - toit - " + compteurToit + ".stl").exists()) {
				Mesh toit = new Mesh(Parser.readSTLA(new File("batiment - " + compt + " - toit - " + compteurToit + ".stl")));
				toits.add(toit);
				compteurToit++;
			}

			while(new File("batiment - " + compt + " - mur - " + compteurBat + ".stl").exists()) {
				ArrayList<Mesh> batiment = new ArrayList<Mesh>();
				Mesh mur = new Mesh(Parser.readSTLA(new File("batiment - " + compt + " - mur - " + compteurBat + ".stl")));
				batiment.add(mur);
				
				Batiment structBatiment = new Batiment();

				Vector3d vector = new Vector3d();

				Vector3d normaleMalOrientee = mur.averageNormal();

				vector.cross(normalSolMalOrientee, normaleMalOrientee);

				double matrix[][] = MatrixMethod.createOrthoBase(normaleMalOrientee, vector, normalSolMalOrientee);
				Mesh surface = mur.changeBase(matrix);

				Mesh surfaceProj = surface.xProjection(surface.xAverage());

				double[][] matrixInv = MatrixMethod.getInversMatrix(matrix);
				Mesh surfaceBienOrientee = surface.changeBase(matrixInv);

				//				Writer.ecrireSurfaceA(new File("surfaceBienOrientee.stl"), surfaceBienOrientee);

				new Grid(surfaceProj, 100, 100, 100).findNeighbours();

				ArrayList<Frontiere> listeFrontieres = surfaceProj.determinerFrontieres();
				double max = Double.MIN_VALUE;
				Frontiere laPlusLongue = new Frontiere();

				for(Frontiere f : listeFrontieres) {
					if(f.distance() > max) {
						max = f.distance();
						laPlusLongue = f;
					}
				}
				
				surfaceProj.clearNeighbours();

				//La fa�ade de mur est maintenant orient�e dans le plan (y,z)
				Mesh contour = laPlusLongue.returnMesh();
				
				Writer.ecrireSurfaceA(new File("contour" + compteurBat + ".stl"), contour);

				Point droiteBas = new Point(contour.xAverage(), contour.yMax(), contour.zMin());
				Point gaucheBas = new Point(contour.xAverage(), contour.yMin(), contour.zMin());

				double erreur = contour.yMax() - contour.yMin();
				erreur /= 25;

				Mesh bordDroit = contour.yBetween(contour.yMax() - erreur, contour.yMax());
				Point droiteHaut = bordDroit.zMaxPoint();
				Mesh bordGauche = contour.yBetween(contour.yMin(), contour.yMin() + erreur);
				Point gaucheHaut = bordGauche.zMaxPoint();

				Triangle t1 = new Triangle(droiteHaut, gaucheBas, droiteBas, normaleMalOrientee);
				Triangle t2 = new Triangle(droiteHaut, gaucheHaut, gaucheBas, normaleMalOrientee);

				Mesh face = new Mesh();
				face.add(t1);
				face.add(t2);

				String type = new String();

				//Rep�rage des murs trap�zo�daux
				double erreurVerticale = contour.zMax() - contour.zMin();
				erreurVerticale /= 5;

				double zHaut = (droiteHaut.getZ() + gaucheHaut.getZ())/2;

				if((droiteHaut.getZ() < gaucheHaut.getZ() - erreurVerticale) || (droiteHaut.getZ() > gaucheHaut.getZ() + erreurVerticale))
				{	//C'est un trap�ze
					//TODO : un trap�ze n'a qu'un seul toit...
					type = "trap�ze";
					
				}
				else 
				{					
					if(contour.zMax() > zHaut + erreurVerticale) 
					{	//Ce n'est pas un rectangle
						type = "multisommets";
					}
					else 
					{
						type = "rectangle";
					}
				}

				System.out.println(type);

				if (type.equals("multisommets")) {					
					ArrayList<Point> pointTops = new ArrayList<Point>();
					ArrayList<Point> pointBots = new ArrayList<Point>();

					while(pointTops.size() < toits.size()) {
						pointTops.add(contour.zMaxPoint());
						contour.remove(contour.getInBounds(contour.zMaxPoint(), erreurVerticale));
					}

					//A REFLECHIR : Si le nombre est diff�rent de celui esp�r�, on garde seulement les plus haut points
					//On r�cup�re les autres points en prenant la partie entre deux sommets.
					for(int i = 0; i < pointTops.size() - 1; i ++) {
						Point p1 = pointTops.get(i);
						Point p2 = pointTops.get(i + 1);
						Mesh temp = contour.yBetween(Math.min(p1.getY(), p2.getY()), Math.max(p1.getY(), p2.getY()));
						Mesh e = temp.zBetween(contour.zMax()/2 + contour.zMin()/2, contour.zMax());
						pointBots.add(e.yMinPoint());
					}
					
					Triangle t3 = new Triangle(pointBots.get(0), droiteHaut, pointTops.get(0), normaleMalOrientee);
					Triangle t4 = new Triangle(gaucheHaut, pointBots.get(0), pointTops.get(1), normaleMalOrientee);
					face.add(t3);
					face.add(t4);
				}
				
				Mesh faceBienOrientee = face.changeBase(matrixInv);

				Writer.ecrireSurfaceA(new File("faceBienOrientee" + compteurBat + ".stl"), faceBienOrientee);

				compteurBat ++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
