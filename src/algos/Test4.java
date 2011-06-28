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

			EnsembleFaces mesh = new EnsembleFaces(Parser.readSTLA(new File("batiment5" + ".stl")));
			vectBatiment.add(mesh);
			System.out.println("Lecture du fichier batiment terminée !");
			System.out.println("Nombre de triangles : " + mesh.size());
			
			for(Triangle t : mesh) {
				t.normale.normalize();
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

			//Détection murets
			for(EnsembleFaces bat : vectBatiment) {
				//Attention, ne pas trop descendre en-dessous de 8.
				double tailleBoule = 8;

				double xMin = bat.xMin();
				double xMax = bat.xMax();
				double yMin = bat.yMin();
				double yMax = bat.yMax();
//				double zMin = bat.zMin();
//				double zMax = bat.zMax();

				int nbX = (int)((xMax - xMin)/tailleBoule);
				int nbY = (int)((yMax - yMin)/tailleBoule);
				int nbZ = 5;

				Tuilage quad = new Tuilage(bat, nbX, nbY, nbZ);

				int compteur = 0;

				for(int i = 0; i < nbX; i ++) {
					for(int j = 0; j < nbY; j ++) {
						for(int k = 0; k < nbZ; k ++) {
							EnsembleFaces boule = new EnsembleFaces(quad._quad[i][j][k]);
							if(i > 1)
								boule.addAll(quad._quad[i-1][j][k]);				
							if(j > 1)
								boule.addAll(quad._quad[i][j-1][k]);
							if(i > 1 && j > 1)
								boule.addAll(quad._quad[i-1][j-1][k]);
							if(i < nbX - 1)
								boule.addAll(quad._quad[i+1][j][k]);	
							if(j < nbY - 1)
								boule.addAll(quad._quad[i][j+1][k]);	
							if(i < nbX - 1 && j < nbY - 1)
								boule.addAll(quad._quad[i+1][j+1][k]);
							
							//Détection cheminées
							//Writer.ecrireSurfaceA(new File("test-50.stl"), boule);
							//if(boule.detectChimney(t, normalSol, errorNormalSol, 0.1)) {
							//	System.out.println("Dectected chimney...");
							//}
							//Détection murets
							if(!boule.isEmpty()) {
								if(boule.detectMuret(normalSol, errorNormalSol)) {
									System.out.println("Dectected muret...");
									EnsembleFaces muret = bat.extractMuret(boule, normalSol, errorNormalSol);
									Writer.ecrireSurfaceA(new File("muret" + compteur + ".stl"), muret);
									Writer.ecrireSurfaceA(new File("boule" + compteur + ".stl"), boule);
									bat.suppress(muret);
									quad.clear();
									quad.addEnsembleFaces(bat);
									System.out.println("Muret extracted ! ");
									compteur ++;
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
