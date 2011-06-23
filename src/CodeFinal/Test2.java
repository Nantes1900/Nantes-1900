package CodeFinal;

import java.io.File;
import java.util.ArrayList;

public class Test2 {
	
	public static void main(String[] args) {
		try
		{
			long time = System.nanoTime();
			
			System.out.println("Parsing ... ");
			
			//Parser
			EnsembleFaces batiments = new EnsembleFaces(Parser.readSTLA(new File("batimentsMesh.stl")));
			System.out.println("Lecture du fichier batiments terminée !");
			System.out.println("Nombre de triangles : " + batiments.size());
			
			EnsembleFaces sol = new EnsembleFaces(Parser.readSTLA(new File("solMesh.stl")));
			System.out.println("Lecture du fichier sol terminée !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Indexation
			System.out.println("Indexation des voisins ...");
			Tuilage quad = new Tuilage(batiments, 100, 100, 100);
			quad.findNeighbours();
			System.out.println("Indexation des voisins terminée !");
			
			//Extraction batiments
			System.out.println("Extraction des batiments ...");
			ArrayList<EnsembleFaces> vectBatiments = new ArrayList<EnsembleFaces>();
			
			int taille = batiments.size();
			
			while(!batiments.isEmpty())
			{
				System.out.println("Nombre de triangles restants : " + batiments.size() + " sur : " + taille);
				
				EnsembleFaces e = new EnsembleFaces();

				batiments.iterator().next().returnNeighbours(e);
				
				if(e.size() > 1)
					vectBatiments.add(e);
				
				batiments.suppress(e);
			}

			//Application de l'algorithme : moyenne des triangles
			System.out.println("Algorithme de moyenne des triangles par blocs ...");
			int nombre = 0;
			for(EnsembleFaces ens : vectBatiments) {
				nombre += ens.size();
			}

			double erreurNombreBlocs = (double)nombre/(double)vectBatiments.size();
			
			int compteur = 0;
			
			System.out.println("Ecriture des différents batiments ...");
			for(EnsembleFaces ens : vectBatiments) {
				if(ens.size() > erreurNombreBlocs) {
					compteur ++;
					Writer.ecrireSurfaceA(new File("batiment" + compteur + ".stl"), ens);
				}
			}
			System.out.println(compteur);
			System.out.println("Ecriture des différents batiments terminée !");
			
			System.out.println("Programme terminé !");
			System.out.println("Temps écoulé : " + (System.nanoTime() - time));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
