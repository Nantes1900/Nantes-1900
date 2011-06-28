package algos;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.Grid;
import utils.MatrixMethod;
import utils.Parser;

import modeles.Mesh;
import modeles.Triangle;

public class Test3 {
	public static void main(String[] args) {
		try
		{
			double errorNormalSol = 0.1;
			
			Mesh sol = new Mesh(Parser.readSTLA("sol.stl"));
			System.out.println("Lecture du fichier sol termin�e !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();
			
			//Changement de rep�re
			double[][] matrix = MatrixMethod.createOrthoBase(normalSolMalOrientee);
			Vector3d normalSol = MatrixMethod.changeBase(normalSolMalOrientee, matrix);
			
			//Parser
			int compt = 1;
			ArrayList<Mesh> vectBatiment = new ArrayList<Mesh>();
			
			while(new File("batiment - " + compt + ".stl").exists()) {
				Mesh batiment = new Mesh(Parser.readSTLA("batiment - " + compt + ".stl"));
				vectBatiment.add(batiment);
				System.out.println("Lecture du fichier batiment - " + compt + " termin�e !");
				System.out.println("Nombre de triangles : " + batiment.size());
				
				Mesh orientes = batiment.orientedNormalTo(normalSol, errorNormalSol);
				ArrayList<Mesh> vectMorceaux = new ArrayList<Mesh>();
				ArrayList<Mesh> vectMurs = new ArrayList<Mesh>();
				
				//On d�coupe les murs
				int taille = 0;
				
				while(!orientes.isEmpty()) {
					Triangle tri = orientes.getOne();
					Mesh mur = orientes.orientedAs(tri.getNormal(), 1);
					new Grid(mur, 10, 10, 10).findNeighbours();
					Mesh temp = new Mesh();
					tri.returnNeighbours(temp);
					orientes.remove(temp);
					batiment.remove(temp);
					vectMorceaux.add(temp);
					taille += temp.size();
				}
				
				int moyenne = taille/vectMorceaux.size();
				
				int compteur = 0;
				
				//On ne garde que les gros
				for(Mesh e : vectMorceaux) {
					if(e.size() > 15*moyenne) {
						vectMurs.add(e);
						compteur++;
						e.writeA("batiment - " + compt + " - mur - " + compteur + ".stl");
					}
				}
				
				System.out.println("Murs triés !");
				
				//On d�coupe ce qui reste : les toits
				vectMorceaux.clear();
				ArrayList<Mesh> vectToits = new ArrayList<Mesh>();
				
				while(!batiment.isEmpty()) {
					Triangle tri = batiment.getOne();
					Mesh mur = batiment.orientedAs(tri.getNormal(), 0.3);
					new Grid(mur, 10, 10, 10).findNeighbours();
					Mesh temp = new Mesh();
					tri.returnNeighbours(temp);
					batiment.remove(temp);
					vectMorceaux.add(temp);
					taille += temp.size();
				}
				
				moyenne = taille/vectMorceaux.size();
				
				compteur = 0;
				
				for(Mesh e : vectMorceaux) {
					if(e.size() > 15*moyenne) {
						vectToits.add(e);
						compteur++;
						e.writeA("batiment - " + compt + " - toit - " + compteur + ".stl");
					}
				}
				
				System.out.println("Toits tri�s !");

				compt ++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
