package CodeFinal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import javax.vecmath.Vector3d;

public class Test4 {
	public static void main(String[] args) {
		try
		{
			System.out.println("Beginning ... ");
			
			//Parser
//			int compteur = 1;
			ArrayList<EnsembleFaces> vectBatiment = new ArrayList<EnsembleFaces>();
//			while(new File("batiment" + compteur + ".stl").exists()) {
//				vectBatiment.add(new EnsembleFaces(Parser.readSTLA(new File("batiment" + compteur + ".stl"))));
//				System.out.println("Lecture du fichier batiment" + compteur + " terminée !");
//				System.out.println("Nombre de triangles : " + vectBatiment.get(compteur - 1).size());
//				compteur ++;
//			}
			
			EnsembleFaces mesh = new EnsembleFaces(Parser.readSTLA(new File("batiment5.stl")));
			vectBatiment.add(mesh);
			System.out.println("Lecture du fichier batiment terminée !");
			System.out.println("Nombre de triangles : " + mesh.size());
			
			EnsembleFaces sol = new EnsembleFaces(Parser.readSTLA(new File("sol.stl")));
			System.out.println("Lecture du fichier sol terminée !");
			System.out.println("Nombre de triangles : " + sol.size());
			
			//Normale sol
			Vector3d normalSolMalOrientee = sol.averageNormal();
			double errorNormalSol = sol.quadricNormalError();
			
			//Changement de repère
			double[][] matrix = MatrixMethod.createOrthoBase(normalSolMalOrientee);
			Vector3d normalSol = MatrixMethod.changeBase(normalSolMalOrientee, matrix);
			
			//Détection murets
			for(EnsembleFaces bat : vectBatiment) {
				double tailleBoule = 10;
				HashSet<Point> index = new HashSet<Point>();
//				Prendre un triangle vertical
				Triangle t = bat.getOutOfIndex(index, tailleBoule);
				
				int compteur = 0;
				
				while(t != null) {
					compteur ++;

					//Détection cheminées
					EnsembleFaces boule = bat.getInBounds(t, tailleBoule);

//					Writer.ecrireSurfaceA(new File("test-50.stl"), boule);
//					if(boule.detectChimney(t, normalSol, errorNormalSol, 0.1)) {
//						System.out.println("Dectected chimney...");
//					}
					//Détection murets
					if(boule.detectMuret(t, normalSol, errorNormalSol)) {
						System.out.println("Dectected muret...");
						EnsembleFaces muret = bat.extractMuret(t, boule, normalSol, errorNormalSol);
						Writer.ecrireSurfaceA(new File("muret1.stl"), muret);
					}
						
					index.add(t.getCentroid());
					
					t = bat.getOutOfIndex(index, tailleBoule);
				}
				
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
