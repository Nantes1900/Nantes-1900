package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Mesh;
import modeles.Polyline;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.Parser;
import utils.Parser.BadFormedFileException;
import utils.Writer;

@SuppressWarnings("unused")

public class SeparationTraitementMursToits {

	private double angleNormalErrorFactor = 0.6;
	private double errorNumberTrianglesWall = 15;
	private double errorNumberTrianglesRoof = 3;

	private int WRITING_MODE = Writer.ASCII_MODE;	

	ArrayList<Mesh> buildingList = new ArrayList<Mesh>();

	ArrayList<Mesh> wallList = new ArrayList<Mesh>();
	ArrayList<Mesh> roofList = new ArrayList<Mesh>();

	ArrayList<Polyline> wallComputedList = new ArrayList<Polyline>();
	ArrayList<Polyline> roofComputedList = new ArrayList<Polyline>();	

	Mesh noise = new Mesh();
	Mesh wholeWall = new Mesh();		
	Mesh wholeRoof = new Mesh();
	Mesh floorBrut = new Mesh();

	Vector3d normalFloor = new Vector3d();

	Mesh currentBuilding = new Mesh();

	public SeparationTraitementMursToits() {
		//TODO : rajouter des options.

		//Options set
		Writer.setWriteMode(WRITING_MODE);

	}

	public void apply() {

		//TODO : peut-être vaudrait-il mieux parser un fichier à la fois et le traiter !
		this.parseFiles();
		this.obtainFloorNormal();

		for(Mesh building : buildingList) {
			this.currentBuilding = building;
			this.sortWalls();
			this.sortRoofs();
			this.treatNoiseWalls();
			this.treatNoiseRoofs();
			this.treatWalls();
			this.treatRoofs();
		}

	}

	private void parseFiles() {

		//Floor parser
		System.out.println("Parsing ...");

		try {

			this.floorBrut = new Mesh(Parser.readSTL("Files/floor.stl"));

			//Buildings parser
			int counterBuilding = 1;

			while(new File("Files/building - " + counterBuilding + ".stl").exists()) {
				Mesh building = new Mesh(Parser.readSTL("Files/building - " + counterBuilding + ".stl"));		
				buildingList.add(building);
				counterBuilding ++;
			}

		} catch (BadFormedFileException e) {
			System.err.println("Error in the file !");
		} catch (IOException e) {
			System.err.println("Error : file does not exist or is unreadable !");
		}
	}

	private void obtainFloorNormal() {

		//Floor normal
		normalFloor = floorBrut.averageNormal();

		try {
			//Base change
			double[][] matrix;

			matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);

		} catch (SingularMatrixException e) {
			System.err.println("Error in the matrix !");
		}

	}


	private void sortWalls() {

		//Wall sorting
		Mesh wallOriented = this.currentBuilding.orientedNormalTo(this.normalFloor, this.angleNormalErrorFactor);
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();

		thingsList = Algos.blockOrientedExtract(wallOriented, this.angleNormalErrorFactor);

		int size = 0;

		for (Mesh e : thingsList) {
			this.currentBuilding.remove(e);
			size += e.size();
		}

		for(Mesh e : thingsList) {
			if(e.size() > this.errorNumberTrianglesWall*(double)size/(double)thingsList.size()) {
				this.wallList.add(e);
			}
			else
				this.noise.addAll(e);
		}

		System.out.println("Walls sorted !");
		for(Mesh w : this.wallList) {
			this.wholeWall.addAll(w);
		}

		this.wholeWall.write("Files/wall.stl");
	}

	private void sortRoofs()	{
		//Roof sorting
		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(this.currentBuilding, this.angleNormalErrorFactor);

		int size = 0;

		for (Mesh e : thingsList) {
			size += e.size();
		}

		for(Mesh e : thingsList) {
			if((e.size() > this.errorNumberTrianglesRoof*(double)size/(double)thingsList.size()) && (e.averageNormal().dot(this.normalFloor) > 0)) {
				this.roofList.add(e);
			}
			else
				this.noise.addAll(e);
		}

		System.out.println("Roofs sorted !");

		for(Mesh r : this.roofList) {
			this.wholeRoof.addAll(r);
		}
		this.wholeRoof.write("Files/roof.stl");
	}

	private void treatNoiseWalls() {
		//Add to the walls
		ArrayList<Mesh> m = new ArrayList<Mesh>();

		for(Mesh e : wallList) {
			Mesh wallAndNoise = new Mesh(e);
			Mesh noiseOriented = noise.orientedAs(e.averageNormal(), 1.2);
			wallAndNoise.addAll(noiseOriented);
			Mesh mes = new Mesh();
			e.getOne().returnNeighbours(mes, wallAndNoise);
			m.add(mes);
			noise.remove(mes);
		}

		wallList = m;
	}

	private void treatNoiseRoofs() {
		//Add to the roofs
		ArrayList<Mesh> m = new ArrayList<Mesh>();

		for(Mesh e : roofList) {
			Mesh roofAndNoise = new Mesh(e);
			//FIXME : put this factor in the header !
			Mesh noiseOriented = noise.orientedAs(e.averageNormal(), 1.2);
			roofAndNoise.addAll(noiseOriented);
			Mesh mes = new Mesh();
			e.getOne().returnNeighbours(mes, roofAndNoise);
			m.add(mes);
			noise.remove(mes);
		}

		roofList = m;

		wholeWall = new Mesh();
		for(Mesh w : wallList) {
			wholeWall.addAll(w);
		}
	}

	private void treatWalls() {
		//Wall treatment
		int counterWall = 1;
		ArrayList<Polyline> contourList = new ArrayList<Polyline>();

		for(Mesh wall : wallList) {

			Polyline bound = new Polyline();

			Vector3d vector = new Vector3d();
			Mesh wallWellNormalToTheFloor = wall.orientedNormalTo(normalFloor, 0.2);

			//To get a wall normal correctly oriented normal to the floor, we select the good part of the wall.
			Vector3d normalWallBadOriented = wallWellNormalToTheFloor.averageNormal();
			vector.cross(normalFloor, normalWallBadOriented);

			double[][] matrixWall;
			try {
				matrixWall = MatrixMethod.createOrthoBase(normalWallBadOriented, vector, normalFloor);
			} catch (SingularMatrixException e) {
				System.err.println("Error in the matrix !");
			}
			//				double[][] matrixInv = MatrixMethod.getInversMatrix(matrixWall);

			//			wall.changeBase(matrixWall);
			//			wallWellNormalToTheFloor.changeBase(matrixWall);

			//We project on the plane at the x coordinate of the majority of triangle of the wall.
			//			Mesh projectedSurface = wall.xProjection(wallWellNormalToTheFloor.xAverage());

			//				projectedSurface.findNeighbours();
			//				
			//				ArrayList<Border> boundList = projectedSurface.returnBounds();
			//				bound = Algos.returnLongestBorder(boundList);

			//TODO : traiter les autres contour intérieurs : fenêtres, portes, etc...

			//				contourList.add(bound);

			//Treatment of each border to convert it into simple Polylines
			//				Border p = new Border();
			//				Point downLeft = new Point(bound.xAverage(), bound.yMin(), bound.zMin());
			//				Point downRight = new Point(bound.xAverage(), bound.yMax(), bound.zMin());
			//
			//				p.add(downRight);
			//				p.add(downLeft);
			//
			//				double pace = bound.yLengthAverage()*10;
			//
			//				//FIXME : supprimer la partie basse du contour, pour éviter que celle-ci soit sélectionnée.
			//				//Ou alors bien augmenter la taille du pas.
			//
			//				double leftLimit = bound.yMin();
			//				double rightLimit = leftLimit + pace;
			//
			//				while(rightLimit < bound.yMax() - pace) {
			//					Border part = bound.yBetween(leftLimit, rightLimit);
			//					rightLimit += pace;
			//					if(!part.isEmpty()) {
			//						leftLimit = rightLimit;
			//						p.add(part.zMaxPoint());
			//					}
			//				}
			//
			//				//When projectedSurface base is changed, then longestBorder points are base changed too, because it's a list
			//				//of references.
			//				Mesh m = p.buildWallMesh();
			//				Mesh mWellOriented = m.changeBase(matrixInv);

			//FIXME : on doit changeBase après être passé par le mesh seulement...
			////				wallComputedList.add(p.changeBase(matrixInv));
			//
			//				mWellOriented.write("Files/wallComputed - " + counterWall + " of building - " + counterBuilding + ".stl");

			counterWall ++;
		}
	}

	private void treatRoofs() {
		//Roof treatment

		int counterRoof = 1;
		ArrayList<Polyline> contourList = new ArrayList<Polyline>();

		for(Mesh roof : roofList) {

			Polyline bound = new Polyline();

			Vector3d normalRoofBadOriented = roof.averageNormal();

			double[][] matrixRoof;
			try {
				matrixRoof = MatrixMethod.createOrthoBase(normalRoofBadOriented);
			} catch (SingularMatrixException e) {
				System.err.println("Error in the matrix !");
			}
			//				double[][] matrixInv = MatrixMethod.getInversMatrix(matrixRoof);

			//			roof.changeBase(matrixRoof);
			//
			//			//We project on the plane at the z coordinate of the majority of triangle of the roof.
			//			Mesh projectedSurface = roof.zProjection(roof.zAverage());


			//				projectedSurface.findNeighbours();
			//				
			//				ArrayList<Border> boundList = projectedSurface.returnBounds();
			//				bound = Algos.returnLongestBorder(boundList);
			//
			//				contourList.add(bound);

			//				Mesh toWrite = bound.returnMesh().changeBase(matrixInv);
			//				toWrite.write("toit - " + counterRoof + ".stl");

			//TODO : traiter les autres contours : cheminées, etc...

			//				Border orderedBound = Algos.orderBorder(bound);

			//				Mesh toWrite2 = orderedBound.returnMesh().changeBase(matrixInv);
			//				toWrite2.write("toitOrdered - " + counterRoof + ".stl");

			//				int numberOfReduction = 10;

			//				Border reducedLine = Algos.reduce(orderedBound, numberOfReduction);

			//				Mesh mWellOriented = reducedLine.buildRoofMesh().changeBase(matrixInv);
			//				mWellOriented.write("roofComputed - " + counterRoof + " of building - " + counterBuilding + ".stl");

			counterRoof ++;
		}
	}
}
