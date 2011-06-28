package algos;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.Parser;

import modeles.Mesh;

public class Test2 {
	public static void main(String[] args) {
		try
		{
			//Parsing
			int buildingCounter = 1;

			ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
			while(new File("building - " + buildingCounter + ".stl").exists()) {
				buildingList.add(new Mesh(Parser.readSTLA("building - " + buildingCounter + ".stl")));
				buildingCounter ++;
			}

			Mesh floors = new Mesh(Parser.readSTLA("sol.stl"));

			//Floor normal
			Vector3d normalFloorBadOriented = floors.averageNormal();

			//Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloorBadOriented);
			Vector3d normalFloor = MatrixMethod.changeBase(normalFloorBadOriented, matrix);
			
			System.out.println(normalFloor);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}



//Archive : TODO : à supprimer
//Walls detection
//for(Mesh bat : buildingList) {
//	//Attention, ne pas trop descendre en-dessous de 8.
//	double tailleBoule = 8;
//
//	double xMin = bat.xMin();
//	double xMax = bat.xMax();
//	double yMin = bat.yMin();
//	double yMax = bat.yMax();
//	//				double zMin = bat.zMin();
//	//				double zMax = bat.zMax();
//
//	int nbX = (int)((xMax - xMin)/tailleBoule);
//	int nbY = (int)((yMax - yMin)/tailleBoule);
//	int nbZ = 5;
//
//	Grid quad = new Grid(bat, nbX, nbY, nbZ);
//
//	int compteur = 0;
//
//	for(int i = 0; i < nbX; i ++) {
//		for(int j = 0; j < nbY; j ++) {
//			for(int k = 0; k < nbZ; k ++) {
//				Mesh boule = new Mesh(quad.getQuad()[i][j][k]);
//				if(i > 1)
//					boule.addAll(quad.getQuad()[i-1][j][k]);				
//				if(j > 1)
//					boule.addAll(quad.getQuad()[i][j-1][k]);
//				if(i > 1 && j > 1)
//					boule.addAll(quad.getQuad()[i-1][j-1][k]);
//				if(i < nbX - 1)
//					boule.addAll(quad.getQuad()[i+1][j][k]);	
//				if(j < nbY - 1)
//					boule.addAll(quad.getQuad()[i][j+1][k]);	
//				if(i < nbX - 1 && j < nbY - 1)
//					boule.addAll(quad.getQuad()[i+1][j+1][k]);
//
//				//D�tection chemin�es
//				//Writer.ecrireSurfaceA(new File("test-50.stl"), boule);
//				//if(boule.detectChimney(t, normalSol, errorNormalSol, 0.1)) {
//				//	System.out.println("Dectected chimney...");
//				//}
//				//D�tection murets
//				if(!boule.isEmpty()) {
//					if(boule.detectMuret(normalSol, errorNormalSol)) {
//						System.out.println("Dectected muret...");
//						Mesh muret = bat.extractMuret(boule, normalSol, errorNormalSol);
//						Writer.writeSTLA("muret" + compteur + ".stl", muret);
//						Writer.writeSTLA("boule" + compteur + ".stl", boule);
//						bat.remove(muret);
//						quad.clear();
//						quad.addEnsembleFaces(bat);
//						System.out.println("Muret extracted ! ");
//						compteur ++;
//					}
//				}
//			}
//		}
//	}
//}
