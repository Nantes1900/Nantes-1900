package algos;

import java.util.ArrayList;
import java.util.HashSet;

import modeles.Mesh;
import modeles.Triangle;

public class Algos {

	public static ArrayList<Mesh> blockExtract(Mesh m) {
		HashSet<Mesh> thingsList = new HashSet<Mesh>();
		Mesh mesh = new Mesh(m);
		
		int size = mesh.size();
		
//		long time;

		while(!mesh.isEmpty())
		{
			System.out.println("Number of triangles left : " + mesh.size() + " on " + size);
			
//			time = System.nanoTime();
			Mesh e = new Mesh();
//			System.out.println("Temps pour le new Mesh : " + (System.nanoTime() - time));
			
//			time = System.nanoTime();
			mesh.getOne().returnNeighbours(e, mesh);
//			System.out.println("Temps pour le returnNeighbours : " + (System.nanoTime() - time));

//			time = System.nanoTime();
			mesh.remove(e);
//			System.out.println("Temps pour le remove : " + (System.nanoTime() - time));

//			time = System.nanoTime();
			thingsList.add(e);
//			System.out.println("Temps pour le add : " + (System.nanoTime() - time));
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

//	public static Point average(List<Point> points) {
//		double xAverage = 0, yAverage = 0, zAverage = 0;
//
//		for(Point p : points) {
//			xAverage += p.getX();
//			yAverage += p.getY();
//			zAverage += p.getZ();
//		}
//
//		return new Point(xAverage, yAverage, zAverage);
//	}
//
//	public static Border returnLongestBorder(ArrayList<Border> boundList) {
//		Border bound = new Border();
//
//		double max = Double.MIN_VALUE;
//
//		for(Border f : boundList) {
//			if(f.distance() > max) {
//				max = f.distance();
//				bound = f;
//			}
//		}
//
//		return bound;
//	}
}