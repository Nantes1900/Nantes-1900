package algos;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.Grid;
import utils.MatrixMethod;
import utils.Parser;

import modeles.Mesh;

public class Test3 {

	public static boolean DEBUG = true;

	public static void main(String[] args) {
		try
		{
			double angleNormalErrorFactor = 0.6;
			double errorNumberTrianglesWall = 10;
			double errorNumberTrianglesRoof = 3;

			//Floor parser
			System.out.println("Parsing ...");
			Mesh floor = new Mesh(Parser.readSTLA("Files\\floor.stl"));

			//Floor normal
			Vector3d normalFloorBadOriented = floor.averageNormal();

			//Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloorBadOriented);
			Vector3d normalFloor = MatrixMethod.changeBase(normalFloorBadOriented, matrix);

			//Buildings parser
			int counterBuilding = 4;
			ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

			while(new File("Files\\building - " + counterBuilding + ".stl").exists()) {
				Mesh building = new Mesh(Parser.readSTLA("Files\\building - " + counterBuilding + ".stl"));
				buildingList.add(building);

				//Wall sorting
				Mesh wallOriented = building.orientedNormalTo(normalFloor, angleNormalErrorFactor);
				ArrayList<Mesh> thingsList = new ArrayList<Mesh>();
				ArrayList<Mesh> wallList = new ArrayList<Mesh>();
				Mesh noise = new Mesh();

				thingsList = Algos.blockOrientedExtract(wallOriented, angleNormalErrorFactor);

				int size = 0;

				for (Mesh e : thingsList) {
					building.remove(e);
					size += e.size();
				}

				for(Mesh e : thingsList) {
					if(e.size() > errorNumberTrianglesWall*(double)size/(double)thingsList.size()) {
						wallList.add(e);
					}
					else
						noise.addAll(e);
				}

				System.out.println("Walls sorted !");
				Mesh wholeWall = new Mesh();
				for(Mesh w : wallList) {
					wholeWall.addAll(w);
				}

				if(DEBUG) {
					wholeWall.writeA("Files\\wall.stl");
					System.out.println("Wall written !");
				}


				//Roof sorting
				ArrayList<Mesh> roofList = new ArrayList<Mesh>();

				thingsList = Algos.blockOrientedExtract(building, angleNormalErrorFactor);

				size = 0;

				for (Mesh e : thingsList) {
					size += e.size();
				}

				for(Mesh e : thingsList) {
					if((e.size() > errorNumberTrianglesRoof*(double)size/(double)thingsList.size()) && (e.averageNormal().dot(normalFloor) > 0)) {
						roofList.add(e);
					}
					else
						noise.addAll(e);
				}

				System.out.println("Roofs sorted !");

				if(DEBUG) {				
					Mesh wholeRoof = new Mesh();
					for(Mesh r : roofList) {
						wholeRoof.addAll(r);
					}
					wholeRoof.writeA("Files\\roof.stl");
					System.out.println("Roof written !");
				}

				//Noise treatment
				Mesh wallsAndNoise = new Mesh(wholeWall);
				wallsAndNoise.addAll(noise);

				new Grid(wallsAndNoise).findNeighbours();

				ArrayList<Mesh> m = new ArrayList<Mesh>();

				for(Mesh e : wallList) {
					Mesh me = new Mesh();
					e.getOne().returnNeighbours(me);
					m.add(me);
				}

				System.out.println("Noise treated !");
				
				if(DEBUG) {
					wholeWall = new Mesh();
					for(Mesh w : m) {
						wholeWall.addAll(w);
					}
					wholeWall.writeA("Files\\entireWall.stl");
				}

				counterBuilding ++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
