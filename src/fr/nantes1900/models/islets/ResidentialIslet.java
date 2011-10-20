package fr.nantes1900.models.islets;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.middle.TriangleMesh;

public class ResidentialIslet extends AbstractEntialIslet {

    public ResidentialIslet(TriangleMesh m) {
	super(m);
    }

    /**
     * Treats the files of residential zones which are in the directory.
     * Separates grounds and buildings, builds a Ground object and calls
     * buildFromTriangleMesh method, builds Building objects, puts the buildings
     * in and calls the buildFromTriangleMesh methods.
     * 
     * @param directoryName
     *            the name of the working directory
     * @param normalGravityOriented
     *            the normal oriented as the gravity vector oriented to the sky
     */
    public void treatResidentials(final String directoryName,
	    final Vector3d normalGravityOriented) {
	// int counterResidentials = 1;
	//
	// // Declarations.
	// List<TriangleMesh> buildings;
	// TriangleMesh wholeGround;
	// List<TriangleMesh> groundsTriangleMesh;
	// Vector3d realNormalToTheGround;
	//
	// // ...Parse the TriangleMeshes of these files.
	// this.parseFile(directoryName + FilesNames.RESIDENTIAL_FILENAME
	// + FilesNames.SEPARATOR + counterResidentials
	// + FilesNames.EXTENSION);
	//
	// // If another ground normal is available, extract it. Otherwise,
	// // keep the normal gravity-oriented as the normal to the ground.
	// if (new File(directoryName + FilesNames.GROUND_FILENAME
	// + FilesNames.SEPARATOR + counterResidentials
	// + FilesNames.EXTENSION).exists()) {
	//
	// realNormalToTheGround = this.extractGroundNormal(directoryName
	// + FilesNames.GROUND_FILENAME + FilesNames.SEPARATOR
	// + counterResidentials + FilesNames.EXTENSION);
	//
	// MatrixMethod.changeBase(realNormalToTheGround, this.matrix);
	//
	// } else {
	// realNormalToTheGround = normalGravityOriented;
	// }
	//
	// // Base change in the gravity-oriented base.
	// this.changeBase();
	//
	// // Extraction of the ground.
	// wholeGround = groundExtraction(mesh, realNormalToTheGround);
	//
	// // Extraction of the buildings : the blocks which are left after
	// // the ground extraction.
	// final TriangleMesh noise = new TriangleMesh();
	// buildings = buildingsExtraction(TriangleMesh, noise);
	//
	// //Treatment of the noise : other blocks are added to the
	// //grounds if possible.
	// groundsTriangleMesh = noiseTreatment(wholeGround, noise);
	//
	// // Cuts the little walls, and other things that are not buildings.
	// // ArrayList<TriangleMesh> formsList =
	// // this.carveRealBuildings(buildings);
	//
	// // Foreach building, create an building object, call the algorithm
	// // to build it and add it to the list of this town.
	// final List<Building> listBuildings = new ArrayList<Building>();
	//
	// int counterBuilding = 1;
	// for (final TriangleMesh m : buildings) {
	// final Building e = new Building();
	// e.buildFromTriangleMesh(m, wholeGround, realNormalToTheGround,
	// directoryName, counterResidentials, counterBuilding);
	// listBuildings.add(e);
	// counterBuilding++;
	// }
	//
	// // Foreach ground found, call the algorithm of ground treatment, and
	// // add it to the list of this town with an attribute : street.
	// for (final TriangleMesh m : groundsTriangleMesh) {
	// final Ground ground = new Ground("street");
	// ground.buildFromTriangleMesh(m);
	// this.addGround(ground);
	// }
    }

}
