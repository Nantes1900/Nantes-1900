package CodeFinal;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

public class Test5 {
	public static void main(String[] args) {
		try
		{
			//Parser
			int compteur = 1;
			ArrayList<EnsembleFaces> vectBatiment = new ArrayList<EnsembleFaces>();
			while(new File("batiment - " + compteur + ".stl").exists()) {
				vectBatiment.add(new EnsembleFaces(Parser.readSTLB(new File("batiment - " + compteur + ".stl"))));
				System.out.println("Lecture du fichier batiment - " + compteur + " terminée !");
				System.out.println("Nombre de triangles : " + vectBatiment.get(compteur - 1).size());
				compteur ++;
			}
			
			EnsembleFaces sol = new EnsembleFaces(Parser.readSTLA(new File("sol.stl")));
			System.out.println("Lecture du fichier sol terminée !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();
			double errorNormalSol = sol.quadricNormalError();
			
			//Changement de repère
			double[][] matrix = MatrixMethod.createOrthoBase(normalSolMalOrientee);
			Vector3d normalSol = MatrixMethod.changeBase(normalSolMalOrientee, matrix);
			
			for(EnsembleFaces batiment : vectBatiment) {
				EnsembleFaces orientes = batiment.orientesNormalA(normalSol, 40*errorNormalSol);
				ArrayList<EnsembleFaces> vectMorceaux = new ArrayList<EnsembleFaces>();
				ArrayList<EnsembleFaces> vectMurs = new ArrayList<EnsembleFaces>();
				
				//On découpe les murs
				int taille = 0;
				
				while(!orientes.isEmpty()) {
					Triangle tri = orientes.getOne();
					EnsembleFaces mur = orientes.orientesSelon(tri.normale, 0.3);
					new Tuilage(mur, 10, 10, 10).findNeighbours();
					EnsembleFaces temp = new EnsembleFaces();
					tri.returnNeighbours(temp);
					orientes.suppress(temp);
					batiment.suppress(temp);
					vectMorceaux.add(temp);
					taille += temp.size();
				}
				
				int moyenne = taille/vectMorceaux.size();
				
				compteur = 0;
				
				//On ne garde que les gros
				for(EnsembleFaces e : vectMorceaux) {
					if(e.size() > 15*moyenne) {
						vectMurs.add(e);
						compteur++;
						Writer.ecrireSurfaceA(new File("bat" + compteur + ".stl"), e);
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
						Writer.ecrireSurfaceA(new File("toit" + compteur + ".stl"), e);
					}
				}				
				
				System.out.println("Toits triés !");

			}
			
			
			
	//		ArrayList<EnsembleFaces> vectSurfaces = new ArrayList<EnsembleFaces>();
			 
	//		while(!fin) {			
	//			//On choisit une face de mur au hasard
	//			int i = 0;
	//			while(!batiment.get(i).estNormalA(normalSol, errorNormalSol)) {
	//				i++;
	//				if(i == (batiment.size() - 1)) {
	//					fin = true;
	//					break;
	//				}
	//			}
	//			
	//			Triangle tri = batiment.get(i);
	//			
	//			//Calcul des orientés
	//			//TODO : permettre de changer le facteur de multiplication
	//			EnsembleFaces meshOrientes = batiment.orientesSelon(tri.getNormale(), 20*errorNormalSol);	
	//			System.out.println("Taille de meshOrientes : " + meshOrientes.size());
	//			
	//			//Création d'un pseudo-index : assignation de voisins à chaque triangle
	//			long time = System.nanoTime();
	//			Tuilage quad = new Tuilage(meshOrientes.xMin(), meshOrientes.xMax(), meshOrientes.yMin(), meshOrientes.yMax(), meshOrientes.zMin(), meshOrientes.zMax(), 50, 50, 50);
	//			quad.addEnsembleFaces(meshOrientes);
	//			quad.findNeighbours();
	//			System.out.println("Temps écoulé pour l'indexation : " + (System.nanoTime() - time));
	//			
	//			EnsembleFaces mur = new EnsembleFaces();
	//			tri.returnNeighbours(mur);
	//			batiment.suppress(mur);
	//			
	//			vectSurfaces.add(mur);
	//		}
			
	//		//Application de l'algorithme : moyenne des triangles
	//		int nombre = 0;
	//		for(EnsembleFaces ens : vectSurfaces) {
	//			nombre += ens.size();
	//		}
	//		
	//		double erreurNombreBlocs = (double)nombre/(double)vectSurfaces.size();
	//		ArrayList<EnsembleFaces> murs = new ArrayList<EnsembleFaces>();
	//		
	//		int compteur = 0;
	//		
	//		for(EnsembleFaces ens : vectSurfaces) {
	//			if(ens.size() > 10*erreurNombreBlocs) {
	//				murs.add(ens);
	//				Writer.ecrireSurfaceA(new File("mur" + compteur + ".stl"), ens);
	//				compteur++;
	//			}
	//		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
