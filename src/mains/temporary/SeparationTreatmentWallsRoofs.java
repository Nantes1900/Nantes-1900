package mains.temporary;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.vecmath.Vector3d;

import modeles.basis.Mesh;
import modeles.extended.Edifice;
import modeles.extended.Town;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;
import utils.WriterSTL;

/**
 * Algorithm of separation between between walls and roofs and treatment.
 * 
 * @author Daniel Lefevre
 */

// LOOK : this class must be deleted until the end of the projec

public class SeparationTreatmentWallsRoofs {

	public static double angleNormalErrorFactor = 20;
	public static double largeAngleNormalErrorFactor = 40;
	public static double errorNormalToFactor = 0.2;
	public static double errorNumberTrianglesWall = 4;
	public static double errorNumberTrianglesRoof = 6;
	public static double errorSingularPoints = 1;

	private int counterBuilding = 1;

	public Mesh floorBrut = new Mesh();
	public Vector3d normalFloor = new Vector3d();

	private Logger log = Logger.getLogger("logger");

	/**
	 * Create the algorithm. Create the logger, and put the writing mode in
	 * default BINARY_MODE.
	 */
	public SeparationTreatmentWallsRoofs() {

		// Options set
		WriterSTL.setWriteMode(WriterSTL.BINARY_MODE);

		log.setLevel(Level.INFO);
		log.setUseParentHandlers(false);
		log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	/**
	 * Execute for each building the algorithm.
	 */
	public void apply() {

		Town town = new Town();

		this.parseFloor();
		this.extractFloorNormal();

		while (new File("Originals/building - " + this.counterBuilding + ".stl")
				.exists()) {

			Edifice building = new Edifice();

			building.buildFromMesh("Originals/building - "
					+ this.counterBuilding + ".stl", normalFloor);

			town.addBuilding(building);

			this.counterBuilding++;
		}

		town.writeCityGML("town");
	}

	/**
	 * Parse the floor brut. Put it in this.floorBrut. Catch and treat the
	 * errors.
	 */
	private void parseFloor() {
		try {
			this.floorBrut = new Mesh(ParserSTL.readSTL("Originals/floor.stl"));
		} catch (BadFormedFileException e) {
			log.severe("Error in the file !");
		} catch (IOException e) {
			log.severe("Error : file does not exist or is unreadable !");
		}
	}

	/**
	 * Compute the average of the normals of the floor brut to obtain the normal
	 * to the floor. Catch and treat matrix exceptions.
	 */
	private void extractFloorNormal() {
		// The floor normal is the average of all the normals fo the triangles
		// in the brut floor file
		normalFloor = floorBrut.averageNormal();

		// Try a base creation if the matrix is not singular
		try {
			double[][] matrix;

			matrix = MatrixMethod.createOrthoBase(normalFloor);

			// Change the normalFloor : now it is z-oriented : (0, 0, 1)
			MatrixMethod.changeBase(normalFloor, matrix);

		} catch (SingularMatrixException e) {
			log.severe("Error in the matrix !");
			System.exit(1);
		}
	}
}
