package algos;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Parser;
import utils.Writer;

import modeles.EnsembleFaces;
import modeles.Triangle;
import modeles.Tuilage;


public class Test5 {
	public static void main(String[] args) {
		try
		{
			EnsembleFaces sol = new EnsembleFaces(Parser.readSTLA(new File("sol.stl")));
			System.out.println("Lecture du fichier sol terminée !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();
			double errorNormalSol = sol.quadricNormalError();
			
			//Changement de repère
			double[][] matrix = MatrixMethod.createOrthoBase(normalSolMalOrientee);
			Vector3d normalSol = MatrixMethod.changeBase(normalSolMalOrientee, matrix);
			
			//Parser
			int compt = 1;
			ArrayList<EnsembleFaces> vectBatiment = new ArrayList<EnsembleFaces>();
			
			while(new File("batiment - " + compt + ".stl").exists()) {
				EnsembleFaces batiment = new EnsembleFaces(Parser.readSTLA(new File("batiment - " + compt + ".stl")));
				vectBatiment.add(batiment);
				System.out.println("Lecture du fichier batiment - " + compt + " terminée !");
				System.out.println("Nombre de triangles : " + batiment.size());
				
				EnsembleFaces orientes = batiment.orientesNormalA(normalSol, 50*errorNormalSol);
				ArrayList<EnsembleFaces> vectMorceaux = new ArrayList<EnsembleFaces>();
				ArrayList<EnsembleFaces> vectMurs = new ArrayList<EnsembleFaces>();
				
				//On découpe les murs
				int taille = 0;
				
				while(!orientes.isEmpty()) {
					Triangle tri = orientes.getOne();
					EnsembleFaces mur = orientes.orientesSelon(tri.normale, 1);
					new Tuilage(mur, 10, 10, 10).findNeighbours();
					EnsembleFaces temp = new EnsembleFaces();
					tri.returnNeighbours(temp);
					orientes.suppress(temp);
					batiment.suppress(temp);
					vectMorceaux.add(temp);
					taille += temp.size();
				}
				
				int moyenne = taille/vectMorceaux.size();
				
				int compteur = 0;
				
				//On ne garde que les gros
				for(EnsembleFaces e : vectMorceaux) {
					if(e.size() > 15*moyenne) {
						vectMurs.add(e);
						compteur++;
						Writer.ecrireSurfaceA(new File("batiment - " + compt + " - mur - " + compteur + ".stl"), e);
					}
				}
				
				System.out.println("Murs triés !");
				
				//On découpe ce qui reste : les toits
				vectMorceaux.clear();
				ArrayList<EnsembleFaces> vectToits = new ArrayList<EnsembleFaces>();
				
				while(!batiment.isEmpty()) {
					Triangle tri = batiment.getOne();
					EnsembleFaces mur = batiment.orientesSelon(tri.normale, 0.3);
					new Tuilage(mur, 10, 10, 10).findNeighbours();
					EnsembleFaces temp = new EnsembleFaces();
					tri.returnNeighbours(temp);
					batiment.suppress(temp);
					vectMorceaux.add(temp);
					taille += temp.size();
				}
				
				moyenne = taille/vectMorceaux.size();
				
				compteur = 0;
				
				for(EnsembleFaces e : vectMorceaux) {
					if(e.size() > 15*moyenne) {
						vectToits.add(e);
						compteur++;
						Writer.ecrireSurfaceA(new File("batiment - " + compt + " - toit - " + compteur + ".stl"), e);
					}
				}
				
				System.out.println("Toits triés !");

				compt ++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
