package fr.nantes1900.models.islets;

import java.util.List;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.middle.Mesh;

public class IndustrialIslet extends AbstractEntialIslet {

    public IndustrialIslet(Mesh m) {
	super(m);
    }

    /**
     * Treats the files of industrial zones which are in the directory.
     * Separates grounds and buildings, builds a Ground object and calls
     * buildFromMesh method, builds Building objects, puts the buildings in and
     * calls the buildFromMesh methods.
     * 
     * @param directoryName
     *            the name of the working directory
     * @param normalGravityOriented
     *            the normal gravity-oriented
     */
    public void treatIndustrials(final String directoryName,
	    final Vector3d normalGravityOriented) {
	int counterIndustrials = 1;

	// Declarations.
	List<Mesh> buildings;
	Mesh wholeGround;
	List<Mesh> groundsMesh;
	Vector3d realNormalToTheGround;

	// realNormalToTheGround = normalGravityOriented;
	//
	// // Base change in the gravity-oriented base.
	// mesh.changeBase(this.matrix);
	//
	// // Extraction of the ground.
	// wholeGround = Town
	// .groundExtraction(mesh, realNormalToTheGround);
	//
	// // Extraction of the buildings : the blocks which are left after
	// // the ground extraction.
	// final Mesh noise = new Mesh();
	// buildings = Town.buildingsExtraction(mesh, noise);
	//
	// // Treatment of the noise : other blocks are added to the
	// // grounds if possible.
	// groundsMesh = Town.noiseTreatment(wholeGround, noise);
	//
	//
	//
	// // Cuts the little walls, and other things that are not buildings.
	// // ArrayList<Mesh> formsList = this.carveRealBuildings(buildings);
	//
	// // Foreach building, create an building object, call the algorithm
	// // to build it and add it to the list of this town.
	// final List<Building> listBuildings = new ArrayList<Building>();
	//
	// int counterBuilding = 1;
	// for (final Mesh m : buildings) {
	// final Building e = new Building();
	// e.buildFromMesh(m, wholeGround, realNormalToTheGround,
	// directoryName, counterIndustrials, counterBuilding);
	// listBuildings.add(e);
	// counterBuilding++;
	// }
	//
	// // Foreach ground found, call the algorithm of ground treatment, and
	// // add it to the list of this town with an attribute : street.
	// for (final Mesh m : groundsMesh) {
	// final Ground ground = new Ground("street");
	// ground.buildFromMesh(m);
	// this.addGround(ground);
	// }
	//
	// this.addIndustrials(listBuildings);
	//
	// ++counterIndustrials;
	// }
    }
}
