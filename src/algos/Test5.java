package algos;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Parser;
import utils.Writer;

import modeles.Mesh;
import modeles.Triangle;
import modeles.Grid;


public class Test5 {
	public static void main(String[] args) {
		try
		{
			Mesh sol = new Mesh(Parser.readSTLA(new File("sol.stl")));
			System.out.println("Lecture du fichier sol termin�e !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();
			double errorNormalSol = sol.quadricNormalError();
			
			//Changement de rep�re
			double[][] matrix = MatrixMethod.createOrthoBase(normalSolMalOrientee);
			Vector3d normalSol = MatrixMethod.changeBase(normalSolMalOrientee, matrix);
			
			//Parser
			int compt = 1;
			ArrayList<Mesh> vectBatiment = new ArrayList<Mesh>();
			
			while(new File("batiment - " + compt + ".stl").exists()) {
				Mesh batiment = new Mesh(Parser.readSTLA(new File("batiment - " + compt + ".stl")));
				vectBatiment.add(batiment);
				System.out.println("Lecture du fichier batiment - " + compt + " termin�e !");
				System.out.println("Nombre de triangles : " + batiment.size());
				
				Mesh orientes = batiment.orientesNormalA(normalSol, 50*errorNormalSol);
				ArrayList<Mesh> vectMorceaux = new ArrayList<Mesh>();
				ArrayList<Mesh> vectMurs = new ArrayList<Mesh>();
				
				//On d�coupe les murs
				int taille = 0;
				
				while(!orientes.isEmpty()) {
					Triangle tri = orientes.getOne();
					Mesh mur = orientes.orientedAs(tri.normale, 1);
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
						Writer.ecrireSurfaceA(new File("batiment - " + compt + " - mur - " + compteur + ".stl"), e);
					}
				}
				
				System.out.println("Murs tri�s !");
				
				//On d�coupe ce qui reste : les toits
				vectMorceaux.clear();
				ArrayList<Mesh> vectToits = new ArrayList<Mesh>();
				
				while(!batiment.isEmpty()) {
					Triangle tri = batiment.getOne();
					Mesh mur = batiment.orientedAs(tri.normale, 0.3);
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
						Writer.ecrireSurfaceA(new File("batiment - " + compt + " - toit - " + compteur + ".stl"), e);
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
