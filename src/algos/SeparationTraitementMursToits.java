package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

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

	private double angleNormalErrorFactor = 60;
	private double largeAngleNormalErrorFactor = 80;
	private double errorNumberTrianglesWall = 15;
	private double errorNumberTrianglesRoof = 3;


	private Mesh floorBrut = new Mesh();
	private Vector3d normalFloor = new Vector3d();	

	private Mesh currentBuilding = new Mesh();

	private ArrayList<Mesh> wallList = new ArrayList<Mesh>();
	private ArrayList<Mesh> roofList = new ArrayList<Mesh>();

	private Mesh wholeWall = new Mesh();
	private Mesh wholeRoof = new Mesh();

	private Mesh noise = new Mesh();

	private ArrayList<Polyline> wallComputedList = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofComputedList = new ArrayList<Polyline>();


	private int counterBuilding = 1;
	private int counterWall = 1;
	private int counterRoof = 1;


	private int WRITING_MODE = Writer.BINARY_MODE;
	private boolean DEBUG_MODE = false;

	private Logger log = Logger.getLogger("logger");


	public SeparationTraitementMursToits() {
		//TODO : rajouter des options.

		//Options set
		Writer.setWriteMode(WRITING_MODE);

		log.setLevel(Level.INFO);
		log.setUseParentHandlers(false);
		log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	public void setDebugMode() {
		log.setLevel(Level.FINEST);
		this.DEBUG_MODE = true;

		this.WRITING_MODE = Writer.ASCII_MODE;
		Writer.setWriteMode(WRITING_MODE);
	}

	public void apply() {

		this.parseFloor();

		while(new File("Files/building - " + this.counterBuilding + ".stl").exists()) {

			this.parseBuilding("Files/building - " + this.counterBuilding + ".stl");
			this.extractFloorNormal();

			this.sortWalls();
			this.sortRoofs();

			this.treatNoiseWalls();
			this.treatNoiseRoofs();

			for(Mesh wall : wallList) {
				wallComputedList.add(this.treatWall(wall));
				counterWall ++;
			}
			for(Mesh roof : roofList) {
				roofComputedList.add(this.treatRoof(roof));
				counterRoof ++;
			}

			this.counterBuilding ++;
		}
	}

	private void parseFloor() {
		try {
			this.floorBrut = new Mesh(Parser.readSTL("Files/floor.stl"));
		} catch (BadFormedFileException e) {
			log.severe("Error in the file !");
		} catch (IOException e) {
			log.severe("Error : file does not exist or is unreadable !");
		}
	}

	private void parseBuilding(String fileName) {
		try {
			this.currentBuilding = new Mesh(Parser.readSTL(fileName));
		}
		catch (BadFormedFileException e) {
			log.severe("Error in the file !");
			System.exit(1);
		} catch (IOException e) {
			log.severe("Error : file does not exist or is unreadable !");
			System.exit(1);
		}
	}

	private void extractFloorNormal() {
		//The floor normal is the average of all the normals fo the triangles in the brut floor file
		normalFloor = floorBrut.averageNormal();

		//Try a base change if the matrix is not singular
		try {
			double[][] matrix;

			matrix = MatrixMethod.createOrthoBase(normalFloor);
			MatrixMethod.changeBase(normalFloor, matrix);

		} catch (SingularMatrixException e) {
			log.severe("Error in the matrix !");
			System.exit(1);
		}
	}


	private void sortWalls() {

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

		for(Mesh w : this.wallList) {
			this.wholeWall.addAll(w);
		}

		if(DEBUG_MODE) {
			this.wholeWall.write("Files/wall.stl");
		}
	}

	private void sortRoofs()	{

		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(this.currentBuilding, this.angleNormalErrorFactor);

		int size = 0;

		for (Mesh e : thingsList) {
			size += e.size();
		}

		for(Mesh e : thingsList) {
			if((e.size() > this.errorNumberTrianglesRoof*(double)size/(double)thingsList.size()) 
					&& (e.averageNormal().dot(this.normalFloor) > 0)) {
				this.roofList.add(e);
			}
			else
				this.noise.addAll(e);
		}

		for(Mesh r : this.roofList) {
			this.wholeRoof.addAll(r);
		}

		if(DEBUG_MODE) {
			this.wholeRoof.write("Files/roof.stl");
		}
	}

	private void treatNoiseWalls() {

		//Add to the walls
		wallList = Algos.blockTreatNoise(wallList, noise, largeAngleNormalErrorFactor);

		wholeWall = new Mesh();
		for(Mesh w : wallList) {
			wholeWall.addAll(w);
		}
	}

	private void treatNoiseRoofs() {

		//Add to the roofs
		roofList = Algos.blockTreatNoise(roofList, noise, largeAngleNormalErrorFactor);

		wholeRoof = new Mesh();
		for(Mesh w : roofList) {
			wholeRoof.addAll(w);
		}
	}

	private Polyline treatWall(Mesh wall) {

//		ArrayList<Polyline> contourList = new ArrayList<Polyline>();
//
		Polyline bound = new Polyline();
//
//		Vector3d vector = new Vector3d();
//		Mesh wallWellNormalToTheFloor = wall.orientedNormalTo(normalFloor, 0.2);
//
//		//To get a wall normal correctly oriented normal to the floor, we select the good part of the wall.
//		Vector3d normalWallBadOriented = wallWellNormalToTheFloor.averageNormal();
//		vector.cross(normalFloor, normalWallBadOriented);
//
//		double[][] matrixWall = null;
//		try {
//			matrixWall = MatrixMethod.createOrthoBase(normalWallBadOriented, vector, normalFloor);
//		} catch (SingularMatrixException e) {
//			System.err.println("Error in the matrix !");
//		}
//		//				double[][] matrixInv = MatrixMethod.getInversMatrix(matrixWall);
//
//		//			wall.changeBase(matrixWall);
//		//			wallWellNormalToTheFloor.changeBase(matrixWall);
//
//		//We project on the plane at the x coordinate of the majority of triangle of the wall.
//		//			Mesh projectedSurface = wall.xProjection(wallWellNormalToTheFloor.xAverage());
//
//		//				projectedSurface.findNeighbours();
//		//				
//		//				ArrayList<Border> boundList = projectedSurface.returnBounds();
//		//				bound = Algos.returnLongestBorder(boundList);
//
//		//TODO : traiter les autres contour intérieurs : fenêtres, portes, etc...
//
//		//				contourList.add(bound);
//
//		//Treatment of each border to convert it into simple Polylines
//		//				Border p = new Border();
//		//				Point downLeft = new Point(bound.xAverage(), bound.yMin(), bound.zMin());
//		//				Point downRight = new Point(bound.xAverage(), bound.yMax(), bound.zMin());
//		//
//		//				p.add(downRight);
//		//				p.add(downLeft);
//		//
//		//				double pace = bound.yLengthAverage()*10;
//		//
//		//				//FIXME : supprimer la partie basse du contour, pour éviter que celle-ci soit sélectionnée.
//		//				//Ou alors bien augmenter la taille du pas.
//		//
//		//				double leftLimit = bound.yMin();
//		//				double rightLimit = leftLimit + pace;
//		//
//		//				while(rightLimit < bound.yMax() - pace) {
//		//					Border part = bound.yBetween(leftLimit, rightLimit);
//		//					rightLimit += pace;
//		//					if(!part.isEmpty()) {
//		//						leftLimit = rightLimit;
//		//						p.add(part.zMaxPoint());
//		//					}
//		//				}
//		//
//		//				//When projectedSurface base is changed, then longestBorder points are base changed too, because it's a list
//		//				//of references.
//		//				Mesh m = p.buildWallMesh();
//		//				Mesh mWellOriented = m.changeBase(matrixInv);
//
//		//FIXME : on doit changeBase après être passé par le mesh seulement...
//		////				wallComputedList.add(p.changeBase(matrixInv));
//		//
//		//				mWellOriented.write("Files/wallComputed - " + counterWall + " of building - " + counterBuilding + ".stl");
		return bound;
	}

	private Polyline treatRoof(Mesh roof) {

		Polyline bound = new Polyline();

		Vector3d normalRoofBadOriented = roof.averageNormal();
		double[][] matrixRoof = null, matrixRoofInv;

		try {
			matrixRoof = MatrixMethod.createOrthoBase(normalRoofBadOriented);
			matrixRoofInv = MatrixMethod.getInversMatrix(matrixRoof);
		} catch (SingularMatrixException e) {
			System.err.println("Error in the matrix !");
			System.exit(1);
		}

		roof.changeBase(matrixRoof);

		//Projection on the plane at the z coordinate of the z average of all triangles of the roof.
		Mesh projectedSurface = roof.zProjection(roof.zAverage());

//		ArrayList<Polyline> boundList = projectedSurface.returnBounds();
//		bound = Algos.returnLongestBorder(boundList);
//
//		contourList.add(bound);
//
//		//TODO : traiter les autres contours : cheminées, etc...
//		Polyline orderedBound = Algos.orderBorder(bound);
//
//		int numberOfReduction = 10;
//
//		Polyline reducedLine = Algos.reduce(orderedBound, numberOfReduction);
		return bound;
	}
}
