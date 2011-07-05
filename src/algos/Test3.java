package algos;

import java.awt.Polygon;
import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import utils.Grid;
import utils.MatrixMethod;
import utils.Parser;

import modeles.Border;
import modeles.Edge;
import modeles.Mesh;
import modeles.Point;
import modeles.Polyline;

@SuppressWarnings("unused")
public class Test3 {

	public static boolean DEBUG = false;

	public static void main(String[] args) {
		try {
			double angleNormalErrorFactor = 0.6;
			double errorNumberTrianglesWall = 15;
			double errorNumberTrianglesRoof = 3;

			//Floor parser
			System.out.println("Parsing ...");
			Mesh floor = new Mesh(Parser.readSTLA("Files/floor.stl"));

			//Floor normal
			Vector3d normalFloorBadOriented = floor.averageNormal();

			//Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloorBadOriented);
			Vector3d normalFloor = MatrixMethod.changeBase(normalFloorBadOriented, matrix);

			//Buildings parser
			int counterBuilding = 4;
			ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

			//			while(new File("Files/building - " + counterBuilding + ".stl").exists()) {
			Mesh building = new Mesh(Parser.readSTLA("Files/building - " + counterBuilding + ".stl"));
			buildingList.add(building);

			ArrayList<Mesh> wallList = new ArrayList<Mesh>();
			ArrayList<Mesh> roofList = new ArrayList<Mesh>();
			ArrayList<Border> contourList = new ArrayList<Border>();
			Mesh noise = new Mesh();
			Mesh wholeWall = new Mesh();

			{
				//Wall sorting
				Mesh wallOriented = building.orientedNormalTo(normalFloor, angleNormalErrorFactor);
				ArrayList<Mesh> thingsList = new ArrayList<Mesh>();

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
				for(Mesh w : wallList) {
					wholeWall.addAll(w);
				}

				if(DEBUG) {
					wholeWall.writeA("Files/wall.stl");
				}
			}

			{
				//Roof sorting
				ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(building, angleNormalErrorFactor);

				int size = 0;

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
					wholeRoof.writeA("Files/roof.stl");
				}
			}

			{
				//Noise treatment
				ArrayList<Mesh> m = new ArrayList<Mesh>();

				for(Mesh e : wallList) {
					Mesh wallAndNoise = new Mesh(e);
					Mesh noiseOriented = noise.orientedAs(e.averageNormal(), 1.2);
					wallAndNoise.addAll(noiseOriented);
					new Grid(wallAndNoise).findNeighbours();
					Mesh mes = new Mesh();
					e.getOne().returnNeighbours(mes);
					m.add(mes);
					noise.remove(mes);
				}

				wallList = m;

				System.out.println("Noise treated !");

				if(DEBUG) {
					wholeWall = new Mesh();
					for(Mesh w : wallList) {
						wholeWall.addAll(w);
					}
					wholeWall.writeA("Files/entireWall.stl");
				}
			}

			{
				//Wall treatment
				int counterWall = 1;

				for(Mesh wall : wallList) {

					Border bound = new Border();

					{
						Vector3d vector = new Vector3d();
						Mesh wallWellNormalToTheFloor = wall.orientedNormalTo(normalFloor, 0.2);
						
						//To get a wall normal correctly oriented normal to the floor, we select the good part of the wall.
						Vector3d normalWallBadOriented = wallWellNormalToTheFloor.averageNormal();
						vector.cross(normalFloor, normalWallBadOriented);

						double[][] matrixWall = MatrixMethod.createOrthoBase(normalWallBadOriented, vector, normalFloor);
						double[][] matrixInv = MatrixMethod.getInversMatrix(matrixWall);

						Mesh surface = wall.changeBase(matrixWall);
						Mesh surfaceWellNormalToTheFloor = wallWellNormalToTheFloor.changeBase(matrixWall);

						//We project on the plane at the x coordinate of the majority of triangle of the wall.
						Mesh projectedSurface = surface.xProjection(surfaceWellNormalToTheFloor.xAverage());

						new Grid(projectedSurface).findNeighbours();
						ArrayList<Border> boundsList = projectedSurface.returnBounds();

						double max = Double.MIN_VALUE;

						for(Border f : boundsList) {
							if(f.distance() > max) {
								max = f.distance();
								bound = f;
							}
						}

						contourList.add(bound);

						//Treatment of each border to convert it into simple Polylines
						Polyline p = new Polyline();
						Point downLeft = new Point(bound.xAverage(), bound.yMin(), bound.zMin());
						Point downRight = new Point(bound.xAverage(), bound.yMax(), bound.zMin());

						p.add(downRight);
						p.add(downLeft);

						double pace = bound.yLengthAverage()*10;

						//FIXME : supprimer la partie basse du contour, pour éviter que celle-ci soit sélectionnée.
						//Ou alors bien augmenter la taille du pas.

						double leftLimit = bound.yMin();
						double rightLimit = leftLimit + pace;

						while(rightLimit < bound.yMax() - pace) {
							Border part = bound.yBetween(leftLimit, rightLimit);
							rightLimit += pace;
							if(!part.isEmpty()) {
								leftLimit = rightLimit;
								p.add(part.zMaxPoint());
							}
						}

						//When projectedSurface base is changed, then longestBorder points are base changed too, because it's a list
						//of references.
						Mesh m = p.buildMesh();
						Mesh mWellOriented = m.changeBase(matrixInv);

						mWellOriented.writeA("wallComputed - " + counterWall + ".stl");

						System.out.println("Files/Wall - " + counterWall + " of building - " + counterBuilding + " computed !");
					}
					counterWall ++;
				}
			}

			{
				//Roof treatment

				int counterRoof = 1;


			}
			//				counterBuilding ++;
			//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
