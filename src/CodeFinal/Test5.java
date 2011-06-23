package CodeFinal;

public class Test5 {
	////int compteur = 0;
	//	boolean fin = false;
	//	
	//	ArrayList<EnsembleFaces> vectSurfaces = new ArrayList<EnsembleFaces>();
	//	
	//	while(!fin) {			
	//		//On choisit une face de mur au hasard
	//		int i = 0;
	//		while(!batiment.get(i).estNormalA(normalSol, errorNormalSol)) {
	//			i++;
	//			if(i == (batiment.size() - 1)) {
	//				fin = true;
	//				break;
	//			}
	//		}
	//		
	//		Triangle tri = batiment.get(i);
	//		
	//		//Calcul des orientés
	//		//TODO : permettre de changer le facteur de multiplication
	//		EnsembleFaces meshOrientes = batiment.orientesSelon(tri.getNormale(), 20*errorNormalSol);	
	//		System.out.println("Taille de meshOrientes : " + meshOrientes.size());
	//		
	//		//Création d'un pseudo-index : assignation de voisins à chaque triangle
	//		long time = System.nanoTime();
	//		Tuilage quad = new Tuilage(meshOrientes.xMin(), meshOrientes.xMax(), meshOrientes.yMin(), meshOrientes.yMax(), meshOrientes.zMin(), meshOrientes.zMax(), 50, 50, 50);
	//		quad.addEnsembleFaces(meshOrientes);
	//		quad.findNeighbours();
	//		System.out.println("Temps écoulé pour l'indexation : " + (System.nanoTime() - time));
	//		
	//		EnsembleFaces mur = new EnsembleFaces();
	//		tri.returnNeighbours(mur);
	//		batiment.suppress(mur);
	//		
	//		vectSurfaces.add(mur);
	//	}
	//	
	//	//Application de l'algorithme : moyenne des triangles
	//	int nombre = 0;
	//	for(EnsembleFaces ens : vectSurfaces) {
	//		nombre += ens.size();
	//	}
	//	
	//	double erreurNombreBlocs = (double)nombre/(double)vectSurfaces.size();
	//	ArrayList<EnsembleFaces> murs = new ArrayList<EnsembleFaces>();
	//	
	//	int compteur = 0;
	//	
	//	for(EnsembleFaces ens : vectSurfaces) {
	//		if(ens.size() > 10*erreurNombreBlocs) {
	//			murs.add(ens);
	//			Writer.ecrireSurfaceA(new File("mur" + compteur + ".stl"), ens);
	//			compteur++;
	//		}
	//	}
}
