package algos;

import java.io.File;
import java.util.ArrayList;

import utils.Parser;
import utils.Writer;

import modeles.EnsembleFaces;
import modeles.Tuilage;


public class Test3 {
	public static void main(String[] args) {
		try
		{
			System.out.println("Parsing ... ");

			EnsembleFaces sol = new EnsembleFaces(Parser.readSTLA(new File("solMesh.stl")));
			System.out.println("Lecture du fichier sol terminée !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			EnsembleFaces bruits = new EnsembleFaces(Parser.readSTLA(new File("bruit - petits.stl")));
			System.out.println("Lecture du fichier bruits terminée !");
			System.out.println("Nombre de triangles : " + bruits.size());
			
			EnsembleFaces sol2 = new EnsembleFaces(sol);
			
			ArrayList<EnsembleFaces> vectSol = new ArrayList<EnsembleFaces>();
			new Tuilage(sol, 100, 100, 100).findNeighbours();
			
			while(!sol.isEmpty())
			{
				System.out.println(sol.size());
				EnsembleFaces e = new EnsembleFaces();
				sol.getOne().returnNeighbours(e);
				vectSol.add(e);
				sol.suppress(e);
			}

			//Si un bloc de bruit possède un voisin au sol, on le rajoute au sol.
			sol2.addAll(bruits);
			
			new Tuilage(sol2, 100, 100, 100).findNeighbours();
			System.out.println("Indexage terminé !");
			
			EnsembleFaces l = new EnsembleFaces();
			
			for(EnsembleFaces e : vectSol) {
				e.getOne().returnNeighbours(l);
			}
			
			Writer.ecrireSurfaceA(new File("sol_entier.stl"), l);
			System.out.println("Terminé ! ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}