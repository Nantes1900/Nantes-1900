package algos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import modeles.Mesh;
import modeles.Triangle;

public class Algos {

	static Logger log = Logger.getLogger("logger");

	public static ArrayList<Mesh> blockExtract(Mesh m) {
		HashSet<Mesh> thingsList = new HashSet<Mesh>();
		Mesh mesh = new Mesh(m);

		int size = mesh.size();

		long time;

		while(!mesh.isEmpty())
		{
			System.out.println("Number of triangles left : " + mesh.size() + " on " + size);

			time = System.nanoTime();
			Mesh e = new Mesh();
			System.out.println("Temps pour le new Mesh : " + (System.nanoTime() - time));

			time = System.nanoTime();
			mesh.getOne().returnNeighbours(e, mesh);
			System.out.println("Temps pour le returnNeighbours : " + (System.nanoTime() - time));

			time = System.nanoTime();
			mesh.remove(e);
			System.out.println("Temps pour le remove : " + (System.nanoTime() - time));

			time = System.nanoTime();
			thingsList.add(e);
			System.out.println("Temps pour le add : " + (System.nanoTime() - time));
		}

		return new ArrayList<Mesh>(thingsList);
	}

	public static ArrayList<Mesh> blockOrientedExtract(Mesh m, double angleNormalErrorFactor) {
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();
		Mesh mesh = new Mesh(m);

		int size = mesh.size();

		while(!mesh.isEmpty()) {
			System.out.println("Number of triangles left : " + mesh.size() + " on " + size);
			Mesh e = new Mesh();
			Triangle tri = mesh.getOne();
			Mesh oriented = mesh.orientedAs(tri.getNormal(), angleNormalErrorFactor);
			tri.returnNeighbours(e, oriented);
			mesh.remove(e);
			thingsList.add(e);
		}

		return thingsList;
	}

	public static ArrayList<Mesh> blockTreatNoise(ArrayList<Mesh> list, Mesh noise, double largeAngleNormalErrorFactor) {

		ArrayList<Mesh> m = new ArrayList<Mesh>();

		for(Mesh e : list) {
			Mesh meshAndNoise = new Mesh(e);
			Mesh noiseOriented = noise.orientedAs(e.averageNormal(), largeAngleNormalErrorFactor);
			meshAndNoise.addAll(noiseOriented);
			Mesh mes = new Mesh();
			e.getOne().returnNeighbours(mes, meshAndNoise);
			m.add(mes);
			noise.remove(mes);
		}

		return m;
	}

	public static Mesh floorExtract(Mesh meshOriented, double altitudeErrorFactor) {
		Mesh floors = new Mesh();

		Triangle lowestTriangle = meshOriented.zMinFace();
		double lowestZ = lowestTriangle.zMin();
		
		Mesh temp = meshOriented.zBetween(lowestZ, lowestZ + altitudeErrorFactor);
		int size = temp.size();

		long time;
		
		int taillemoyennedesbouts = 0;
		int counter = 0;

		while(lowestTriangle != null)
		{
			System.out.println("Number of triangles left : " + meshOriented.size() + " on " + size);
			time = System.nanoTime();
			temp.clear();
			System.out.println("Temps pour le clear : " + (System.nanoTime() - time));

			time = System.nanoTime();
			lowestTriangle.returnNeighbours(temp, meshOriented);
			System.out.println("Temps pour le returnNeighbours : " + (System.nanoTime() - time));

			counter ++;
			taillemoyennedesbouts += temp.size();
			System.out.println("Taille moyenne des bouts : " + taillemoyennedesbouts/counter);
			
			time = System.nanoTime();
			floors.addAll(temp);
			System.out.println("Temps pour le addAll : " + (System.nanoTime() - time));

			time = System.nanoTime();
			meshOriented.remove(temp);
			System.out.println("Temps pour le remove : " + (System.nanoTime() - time));

			time = System.nanoTime();
			lowestTriangle = meshOriented.faceUnderZ(lowestZ + altitudeErrorFactor);
			System.out.println("Temps pour le faceUnderZ : " + (System.nanoTime() - time));
		}

		return floors;
	}
}