package CodeFinal;

import java.io.File;

import javax.vecmath.Vector3d;

public class Test1 {

	public static void main(String[] args) {
		try
		{
			double facteurErreurAltitude = 0.05;
			double facteurErreurNormalSol = 30;
			String nomFichier = "batiments 1 - binary.stl";
			
			long timeGlobal = System.nanoTime();
			
			System.out.println("Parsing ...");

			EnsembleFaces meshBrut = new EnsembleFaces(Parser.readSTLB(new File(nomFichier)));
			System.out.println("Lecture du fichier batiments terminée !");
			System.out.println("Nombre de triangles : " + meshBrut.size());
			
			EnsembleFaces sol = new EnsembleFaces(Parser.readSTLA(new File("sol.stl")));
			System.out.println("Lecture du fichier sol terminée !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();
			double _errorNormalSol = sol.quadricNormalError();
			
			//Changement de repère
			System.out.println("Changement de repère ...");
			double[][] matrix = MatrixMethod.createOrthoBase(normalSolMalOrientee);
			Vector3d _normalSol = MatrixMethod.changeBase(normalSolMalOrientee, matrix);

			EnsembleFaces mesh = meshBrut.changeBase(matrix);
			
			System.out.println("Changement de repère terminé !");
			
			//Calcul de l'erreur
			double erreurAltitude = (mesh.zMax() - mesh.zMin()) * facteurErreurAltitude;

			//Calcul des orientés sol
			System.out.println("Calcul des orientes ...");
			EnsembleFaces meshOrientes = mesh.orientesSelon(_normalSol, facteurErreurNormalSol * _errorNormalSol);
			System.out.println("Taille des orientés : " + meshOrientes.size());
			System.out.println("Calcul des orientés terminé !");

			//Création d'un pseudo-index : assignation de voisins à chaque triangle
			//FIXME : bien régler les paramètres de tuilage: cela crée des pbs parfois.
			System.out.println("Calcul des index ...");
			Tuilage quad = new Tuilage(meshOrientes, 50, 50, 50);
			quad.findNeighbours();
			System.out.println("Calcul des index terminé !");

			//Début de l'algorithme
			Triangle plusBasTriangle = meshOrientes.ZMinFace();
			double plusBasZ = plusBasTriangle.zMin();
			
			int taille = meshOrientes.size();
			
			EnsembleFaces e = new EnsembleFaces();
			EnsembleFaces surfaceSol = new EnsembleFaces();
			
			long time = System.nanoTime();
			while(plusBasTriangle.zMin() < plusBasZ + erreurAltitude)
			{
				System.out.println("Nombre de triangles restant : " + meshOrientes.size() + " sur : " + taille);
				e.clear();
							
				plusBasTriangle.returnNeighbours(e);
				surfaceSol.addAll(e);
				
				meshOrientes.suppress(e);
				mesh.suppress(e);
				plusBasTriangle = meshOrientes.ZMinFace(plusBasZ + erreurAltitude);
			}
			System.out.println("Temps écoulé durant l'algo : " + (System.nanoTime() - time));
			
			//Ecriture du sol	
			System.out.println("Extraction du sol ...");
			System.out.println("Taille du sol : " + surfaceSol.size());
			Writer.ecrireSurfaceA(new File("solMesh.stl"), surfaceSol);	
			System.out.println("Extraction du sol terminée !");
			
			System.out.println("Extraction des batiments ...");
			System.out.println("Taille de batiments : " + mesh.size());
			Writer.ecrireSurfaceA(new File("batimentsMesh.stl"), mesh);
			System.out.println("Extraction des batiments terminée !");
			
			System.out.println("Programme terminé !");
			
			System.out.println("Temps écoulé : " + (System.nanoTime() - timeGlobal));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
