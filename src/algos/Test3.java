package algos;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Border;
import modeles.Mesh;
import utils.MatrixMethod;
import utils.Parser;
import utils.Writer;

public class Test3 {

	public static boolean DEBUG = false;
	public static boolean WALLS = true;
	public static boolean ROOFS = true;	

	public static void main(String[] args) {
		try {
			double angleNormalErrorFactor = 0.6;
			double errorNumberTrianglesWall = 15;
			double errorNumberTrianglesRoof = 3;
			
			//Options set
			Writer.setWriteMode(Writer.ASCII_MODE);

			//Floor parser
			System.out.println("Parsing ...");
			Mesh floor = new Mesh(Parser.readSTL("Files/floor.stl"));

			//Floor normal
			Vector3d normalFloorBadOriented = floor.averageNormal();

			//Base change
			double[][] matrix = MatrixMethod.createOrthoBase(normalFloorBadOriented);
			Vector3d normalFloor = MatrixMethod.changeBase(normalFloorBadOriented, matrix);

			//Buildings parser
			int counterBuilding = 4;
			ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

			//			while(new File("Files/building - " + counterBuilding + ".stl").exists()) {
			Mesh building = new Mesh(Parser.readSTL("Files/building - " + counterBuilding + ".stl"));
			buildingList.add(building);

			ArrayList<Mesh> wallList = new ArrayList<Mesh>();
			ArrayList<Mesh> roofList = new ArrayList<Mesh>();

//			ArrayList<Polyline> wallComputedList = new ArrayList<Polyline>();
//			ArrayList<Polyline> roofComputedList = new ArrayList<Polyline>();

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
					wholeWall.write("Files/wall.stl");
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
					wholeRoof.write("Files/roof.stl");
				}
			}

			{
				//Noise treatment
				{
					//Add to the walls
					ArrayList<Mesh> m = new ArrayList<Mesh>();

					for(Mesh e : wallList) {
						Mesh wallAndNoise = new Mesh(e);
						Mesh noiseOriented = noise.orientedAs(e.averageNormal(), 1.2);
						wallAndNoise.addAll(noiseOriented);
						wallAndNoise.findNeighbours();
						Mesh mes = new Mesh();
						e.getOne().returnNeighbours(mes);
						m.add(mes);
						noise.remove(mes);
					}

					wallList = m;
				}

				{
					//Add to the roofs
					ArrayList<Mesh> m = new ArrayList<Mesh>();

					for(Mesh e : roofList) {
						Mesh roofAndNoise = new Mesh(e);
						//TODO : put this factor in the header !
						Mesh noiseOriented = noise.orientedAs(e.averageNormal(), 1.2);
						roofAndNoise.addAll(noiseOriented);
						roofAndNoise.findNeighbours();
						Mesh mes = new Mesh();
						e.getOne().returnNeighbours(mes);
						m.add(mes);
						noise.remove(mes);
					}

					roofList = m;
				}

				System.out.println("Noise treated !");

				if(DEBUG) {
					wholeWall = new Mesh();
					for(Mesh w : wallList) {
						wholeWall.addAll(w);
					}
					wholeWall.write("Files/entireWall.stl");
				}
			}

			if(WALLS)
			{
				//Wall treatment
				int counterWall = 1;
				ArrayList<Border> contourList = new ArrayList<Border>();

				for(Mesh wall : wallList) {

					Border bound = new Border();

					Vector3d vector = new Vector3d();
					Mesh wallWellNormalToTheFloor = wall.orientedNormalTo(normalFloor, 0.2);

					//To get a wall normal correctly oriented normal to the floor, we select the good part of the wall.
					Vector3d normalWallBadOriented = wallWellNormalToTheFloor.averageNormal();
					vector.cross(normalFloor, normalWallBadOriented);

					double[][] matrixWall = MatrixMethod.createOrthoBase(normalWallBadOriented, vector, normalFloor);
//					double[][] matrixInv = MatrixMethod.getInversMatrix(matrixWall);

					wall.changeBase(matrixWall);
					wallWellNormalToTheFloor.changeBase(matrixWall);

					//We project on the plane at the x coordinate of the majority of triangle of the wall.
					Mesh projectedSurface = wall.xProjection(wallWellNormalToTheFloor.xAverage());

					projectedSurface.findNeighbours();
					
					ArrayList<Border> boundList = projectedSurface.returnBounds();
					bound = Algos.returnLongestBorder(boundList);

					//TODO : traiter les autres contour intérieurs : fenêtres, portes, etc...

					contourList.add(bound);

					//Treatment of each border to convert it into simple Polylines
//					Border p = new Border();
//					Point downLeft = new Point(bound.xAverage(), bound.yMin(), bound.zMin());
//					Point downRight = new Point(bound.xAverage(), bound.yMax(), bound.zMin());
//
//					p.add(downRight);
//					p.add(downLeft);
//
//					double pace = bound.yLengthAverage()*10;
//
//					//FIXME : supprimer la partie basse du contour, pour éviter que celle-ci soit sélectionnée.
//					//Ou alors bien augmenter la taille du pas.
//
//					double leftLimit = bound.yMin();
//					double rightLimit = leftLimit + pace;
//
//					while(rightLimit < bound.yMax() - pace) {
//						Border part = bound.yBetween(leftLimit, rightLimit);
//						rightLimit += pace;
//						if(!part.isEmpty()) {
//							leftLimit = rightLimit;
//							p.add(part.zMaxPoint());
//						}
//					}
//
//					//When projectedSurface base is changed, then longestBorder points are base changed too, because it's a list
//					//of references.
//					Mesh m = p.buildWallMesh();
//					Mesh mWellOriented = m.changeBase(matrixInv);

					//FIXME : on doit changeBase après être passé par le mesh seulement...
////					wallComputedList.add(p.changeBase(matrixInv));
//
//					mWellOriented.write("Files/wallComputed - " + counterWall + " of building - " + counterBuilding + ".stl");

					counterWall ++;
				}
			}

			if(ROOFS)
			{
				//Roof treatment

				int counterRoof = 1;
				ArrayList<Border> contourList = new ArrayList<Border>();
				
				for(Mesh roof : roofList) {

					Border bound = new Border();

					Vector3d normalRoofBadOriented = roof.averageNormal();

					double[][] matrixRoof = MatrixMethod.createOrthoBase(normalRoofBadOriented);
//					double[][] matrixInv = MatrixMethod.getInversMatrix(matrixRoof);

					roof.changeBase(matrixRoof);

					//We project on the plane at the z coordinate of the majority of triangle of the roof.
					Mesh projectedSurface = roof.zProjection(roof.zAverage());

					
					projectedSurface.findNeighbours();
					
					ArrayList<Border> boundList = projectedSurface.returnBounds();
					bound = Algos.returnLongestBorder(boundList);

					contourList.add(bound);

//					Mesh toWrite = bound.returnMesh().changeBase(matrixInv);
//					toWrite.write("toit - " + counterRoof + ".stl");

					//TODO : traiter les autres contours : cheminées, etc...

//					Border orderedBound = Algos.orderBorder(bound);

//					Mesh toWrite2 = orderedBound.returnMesh().changeBase(matrixInv);
//					toWrite2.write("toitOrdered - " + counterRoof + ".stl");

//					int numberOfReduction = 10;

//					Border reducedLine = Algos.reduce(orderedBound, numberOfReduction);

//					Mesh mWellOriented = reducedLine.buildRoofMesh().changeBase(matrixInv);
//					mWellOriented.write("roofComputed - " + counterRoof + " of building - " + counterBuilding + ".stl");

					counterRoof ++;
				}
			}
			//				counterBuilding ++;
			//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
