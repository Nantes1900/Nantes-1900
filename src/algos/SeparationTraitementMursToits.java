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

public class SeparationTraitementMursToits {

	private double angleNormalErrorFactor = 40;
	private double largeAngleNormalErrorFactor = 80;
	private double errorNormalToFactor = 0.2;
	private double errorNumberTrianglesWall = 5;
	private double errorNumberTrianglesRoof = 5;


	private Mesh floorBrut = new Mesh();
	private Vector3d normalFloor = new Vector3d();	

	private Mesh currentBuilding = new Mesh();

	private ArrayList<Mesh> wallList = new ArrayList<Mesh>();
	private ArrayList<Mesh> roofList = new ArrayList<Mesh>();

	private Mesh wholeWall = new Mesh();
	private Mesh wholeRoof = new Mesh();

	private Mesh noise = new Mesh();

	//	private ArrayList<Polyline> wallComputedList = new ArrayList<Polyline>();
	//	private ArrayList<Polyline> roofComputedList = new ArrayList<Polyline>();


	private int counterBuilding = 1;
	private int counterWall = 1;
	private int counterRoof = 1;


	private int WRITING_MODE = Writer.BINARY_MODE;

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
		//		this.DEBUG_MODE = true;

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

			//			this.wallList.get(0).write("wall1.stl");
			//			this.wallList.get(1).write("wall2.stl");
			//			this.wallList.get(2).write("wall3.stl");
			//			this.wallList.get(3).write("wall4.stl");

			this.treatWall();

			//			for(int i = 0; i < 1; i ++) {
			//				this.wallList.get(i).write("wallsse.stl");
			//				Mesh w = this.wallList.get(i);
			//				w.write("waladfafal.stl");
			//				this.treatWall(w);
			//			}
			
			this.treatRoof();

			//			for(Mesh roof : this.roofList) {
			//				roof.write("roof avant - " + counterRoof + ".stl");
			//				Polyline p = this.treatRoof(roof);
			//				p.returnMeshShit().write(counterWall + "wall-shit.stl");
			//			}

			this.counterBuilding ++;
		}
	}

	private void parseFloor() {
		try {
			this.floorBrut = new Mesh(Parser.readSTL("Originals/floor.stl"));
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

		//Try a base creation if the matrix is not singular
		try {
			double[][] matrix;

			matrix = MatrixMethod.createOrthoBase(normalFloor);

			//Change the normalFloor : now it is z-oriented : (0, 0, 1)
			MatrixMethod.changeBase(normalFloor, matrix);

		} catch (SingularMatrixException e) {
			log.severe("Error in the matrix !");
			System.exit(1);
		}
	}


	private void sortWalls() {

		Mesh wallOriented = this.currentBuilding.orientedNormalTo(this.normalFloor, this.errorNormalToFactor);

		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(wallOriented, this.angleNormalErrorFactor);

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
	}

	private void treatNoiseWalls() {

		this.wallList = Algos.blockTreatOrientedNoise(this.wallList, this.noise, this.largeAngleNormalErrorFactor);

		this.wholeWall = new Mesh();
		for(Mesh w : this.wallList) {
			this.wholeWall.addAll(w);
		}
	}

	private void treatNoiseRoofs() {

		this.roofList = Algos.blockTreatOrientedNoise(this.roofList, this.noise, this.largeAngleNormalErrorFactor);

		this.wholeRoof = new Mesh();
		for(Mesh w : this.roofList) {
			this.wholeRoof.addAll(w);
		}
	}

	private void treatWall() {

		counterWall = 0;

		while(counterWall < wallList.size()) {

			Mesh wall = wallList.get(counterWall);

			wall.write("wallBrut - " + counterWall + ".stl");

			//			Vector3d normalWall = wall.averageNormal();

			//			double[][] matrixWall = null, matrixWallInv = null;
			//			try {
			//				matrixWall = MatrixMethod.createOrthoBase(normalWall);
			//				matrixWallInv = MatrixMethod.getInversMatrix(matrixWall);
			//			} catch (SingularMatrixException e) {
			//				System.err.println("Error in the matrix !");
			//				System.exit(1);
			//			}

			Polyline unsortedBounds = wall.returnUnsortedBounds();
			unsortedBounds.returnMesh().write("wallUnsortedBounds - " + counterWall + ".stl");
			
			Polyline longestBound = wall.returnLongestBound();
			longestBound.returnMeshShit().write("wallLongestBound - " + counterWall + ".stl");

			//			bound.order();

			//Projection on the plane at the z coordinate of the z average of all triangles of the roof.
			//			bound.changeBase(matrixWall);
			//			bound = bound.zProjection(wall.zAverage());
			//			bound.changeBase(matrixWallInv);
			//			bound.returnMesh().write("wallUnsortedBounds - " + counterWall + ".stl");

			//FIXME : put error in the header
			//			double error = 0.1;
			//			Polyline singularPoints = bound.determinateSingularPoints(error);

			//			singularPoints.changeBase(matrixWallInv);

			counterWall ++;
		}

		//		return singularPoints;
	}

	private void treatRoof() {

		counterRoof = 0;

		while(counterRoof < roofList.size()) {

			Mesh roof = roofList.get(counterRoof);

			roof.write("roofBrut - " + counterRoof + ".stl");

			//		Vector3d normalRoofBadOriented = roof.averageNormal();
			//		double[][] matrixRoof = null, matrixRoofInv = null;
			//
			//		try {
			//			matrixRoof = MatrixMethod.createOrthoBase(normalRoofBadOriented);
			//			matrixRoofInv = MatrixMethod.getInversMatrix(matrixRoof);
			//		} catch (SingularMatrixException e) {
			//			System.err.println("Error in the matrix !");
			//			System.exit(1);
			//		}
			//
			//		roof.changeBase(matrixRoof);
			
			Polyline unsortedBounds = roof.returnUnsortedBounds();
			unsortedBounds.returnMesh().write("roofUnsortedBounds - " + counterRoof + ".stl");
			
			Polyline longestBound = roof.returnLongestBound();
			longestBound.returnMeshShit().write("roofLongestBound - " + counterRoof + ".stl");
					
			//		//TODO : traiter les autres contours : cheminées, etc...
			//		bound.order();
			//
			//		//Projection on the plane at the z coordinate of the z average of all triangles of the roof.
			//		bound = bound.zProjection(roof.zAverage());
			//
			//		//FIXME : put error in the header
			//		double error = 0.1;
			//		Polyline singularPoints = bound.determinateSingularPoints(error);
			//
			//		singularPoints.changeBase(matrixRoofInv);
			//
			//		return singularPoints;
			//	}
			counterRoof ++;
		}
	}
}
