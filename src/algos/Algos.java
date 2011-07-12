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

		while(!mesh.isEmpty())
		{
			//LOOK : redo this syso : the numbers are false !
			System.out.println("Number of triangles left : " + mesh.size() + " on " + size);
			Mesh e = new Mesh();
			mesh.getOne().returnNeighbours(e, mesh);
			mesh.remove(e);
			thingsList.add(e);
		}

		return new ArrayList<Mesh>(thingsList);
	}

	public static ArrayList<Mesh> blockOrientedExtract(Mesh m, double angleNormalErrorFactor) {
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();
		Mesh mesh = new Mesh(m);

		int size = mesh.size();

		while(!mesh.isEmpty()) {
			//LOOK : redo this syso : the numbers are false !
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

	public static ArrayList<Mesh> blockTreatOrientedNoise(ArrayList<Mesh> list, Mesh noise, double largeAngleNormalErrorFactor) {

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
		
		Mesh stripe = meshOriented.zBetween(lowestZ, lowestZ + altitudeErrorFactor);
		int size = meshOriented.size();
		
		Mesh temp = new Mesh();

		while(!stripe.isEmpty())
		{
			lowestTriangle = stripe.getOne();
			//LOOK : redo this syso : the numbers are false !
			System.out.println("Number of triangles left : " + meshOriented.size() + " on " + size);
			temp.clear();
			lowestTriangle.returnNeighbours(temp, meshOriented);
			floors.addAll(temp);
			meshOriented.remove(temp);
			stripe.remove(temp);
		}

		return floors;
	}
}