package fr.nantes1900.models.islets;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationGrounds;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.middle.TriangleMesh;
import fr.nantes1900.utils.Algos;

//LOL for the name...

public abstract class AbstractEntialIslet extends AbstractIslet {
    private List<Building> buildings = new ArrayList<Building>();
    private Building initialBuilding = new Building();
    private Ground initialGround = new Ground();
    public int progression = 0;

    public AbstractEntialIslet(TriangleMesh m) {
	super(m);
    }

    public void launchNextAlgo() {
	switch (this.progression) {
	case 0:
	    this.launchAlgo1();
	    break;
	case 1:
	    this.launchAlgo2();
	    break;
	case 2:
	    this.launchAlgo3();
	    break;
	case 3:
	    this.launchAlgo4();
	    break;
	case 4:
	    this.launchAlgo5();
	    break;
	case 5:
	    this.launchAlgo6();
	    break;
	case 6:
	    this.launchAlgo7();
	    break;
	case 7:
	    this.launchAlgo8();
	    break;
	}
    }

    private void incProgression() {
	this.progression++;
    }

    /**
     * SeparationGroundBuilding
     */
    public void launchAlgo1() {
	if (this.initialTotalMesh == null) {
	    throw new InvalidParameterException();
	    // TODO
	}

	this.incProgression();
    }

    /**
     * SeparationBuildings
     */
    public void launchAlgo2() {

    }

    /**
     * SeparationWallRoof
     */
    public void launchAlgo3() {

    }

    /**
     * SeparationWalls
     */
    public void launchAlgo4() {

    }

    /**
     * SeparationRoofs
     */
    public void launchAlgo5() {

    }

    /**
     * DeterminateNeighbours
     */
    public void launchAlgo6() {

    }

    /**
     * SortNeighbours
     */
    public void launchAlgo7() {

    }

    /**
     * SimplificationSurfaces
     */
    public void launchAlgo8() {

    }

    /**
     * Extracts buildings by extracting the blocks after the ground extraction.
     * 
     * @param TriangleMesh
     *            the TriangleMesh containing the buildinfs
     * @param noise
     *            the TriangleMesh to stock the noise
     * @return a list of buildings as TriangleMeshes
     */
    private static List<TriangleMesh> buildingsExtraction(
	    final TriangleMesh TriangleMesh, final TriangleMesh noise) {

	final List<TriangleMesh> buildingList = new ArrayList<TriangleMesh>();

	List<TriangleMesh> thingsList;
	// Extraction of the buildings.
	thingsList = Algos.blockExtract(TriangleMesh);

	// Algorithm : detection of buildings considering their size.
	for (final TriangleMesh m : thingsList) {
	    if (m.size() >= SeparationBuildings.BLOCK_BUILDING_SIZE_ERROR) {

		buildingList.add(m);
	    } else {
		noise.addAll(m);
	    }
	}

	if (buildingList.size() == 0) {
	    System.out.println("Error : no building found !");
	}

	return buildingList;
    }

    /**
     * Cut in a building zone the forms which are not buildings : little walls,
     * chimneys. Method not implemented.
     * 
     * @param buildings
     *            the list of buildings to treat
     * @return the list of the forms
     */
    private static ArrayList<TriangleMesh> carveRealBuildings(
	    final List<TriangleMesh> buildings) {
	// TODO : implement this method.
	return null;
    }

    /**
     * Extracts the grounds, using the groundExtract method.
     * 
     * @param mesh
     *            the TriangleMesh to extract from
     * @param normalGround
     *            the normal to the ground (not the gravity-oriented normal, of
     *            course...)
     * @return a TriangleMesh containing the ground
     */
    private static TriangleMesh groundExtraction(final TriangleMesh mesh,
	    final Vector3d normalGround) {

	// Searches for ground-oriented triangles with an error.
	TriangleMesh TriangleMeshOriented = mesh.orientedAs(normalGround,
		SeparationGroundBuilding.ANGLE_GROUND_ERROR);

	List<TriangleMesh> thingsList;
	List<TriangleMesh> groundsList = new ArrayList<TriangleMesh>();
	// Extracts the blocks in the oriented triangles.
	thingsList = Algos.blockExtract(TriangleMeshOriented);

	// FIXME : use TriangleMeshOriented.
	TriangleMesh wholeGround = new TriangleMesh();
	for (final TriangleMesh f : thingsList) {
	    wholeGround.addAll(f);
	}

	// We consider the altitude of the blocks on an axis parallel to the
	// normal ground.
	final double highDiff = mesh.zMax() - mesh.zMin();

	// Builds an axis normal to the current ground.
	final Edge axisNormalGround = new Edge(new Point(0, 0, 0), new Point(
		normalGround.x, normalGround.y, normalGround.z));

	// Project the current whole ground centroid on this axis.
	final Point pAverage = axisNormalGround.project(wholeGround
		.getCentroid());

	// After this, for each block, consider the distance (on the
	// axisNormalGround) as an altitude distance. If it is greater than
	// the error, then it's not considered as ground.
	for (final TriangleMesh m : thingsList) {
	    final Point projectedPoint = axisNormalGround.project(m
		    .getCentroid());
	    if (projectedPoint.getZ() < pAverage.getZ()
		    || projectedPoint.distance(pAverage) < highDiff
			    * SeparationGroundBuilding.ALTITUDE_ERROR) {

		groundsList.add(m);
	    }
	}

	// We consider the size of the blocks : if they're big enough,
	// they're keeped. This is to avoid the parts of roofs, walls,
	// etc...
	thingsList = new ArrayList<TriangleMesh>(groundsList);
	groundsList = new ArrayList<TriangleMesh>();
	for (final TriangleMesh m : thingsList) {
	    if (m.size() > SeparationGrounds.BLOCK_GROUNDS_SIZE_ERROR) {
		groundsList.add(m);
	    }
	}

	// Now that we found the real grounds, we extract the other
	// triangles
	// which are almost ground-oriented to add them.
	TriangleMeshOriented = mesh.orientedAs(normalGround,
		SeparationGroundBuilding.LARGE_ANGLE_GROUND_ERROR);

	// If the new grounds are neighbours from the old ones, they are
	// added to the real grounds.
	thingsList = new ArrayList<TriangleMesh>();
	for (final TriangleMesh m : groundsList) {

	    final TriangleMesh temp = new TriangleMesh(m);
	    temp.addAll(TriangleMeshOriented);
	    final TriangleMesh ret = new TriangleMesh();
	    m.getOne().returnNeighbours(ret, temp);
	    TriangleMeshOriented.remove(ret);
	    thingsList.add(ret);
	}
	groundsList = thingsList;

	wholeGround = new TriangleMesh();
	for (final TriangleMesh f : groundsList) {
	    mesh.remove(f);
	    wholeGround.addAll(f);
	}

	return wholeGround;
    }

    /**
     * Treats the ground object. For the moment, it just changes base.
     * 
     * @param groundZone
     *            the ground to treat
     * @return the ground treated
     */
    private Ground treatGroundZone(final Ground groundZone) {

	// groundZone.changeBase(this.matrix);

	return groundZone;
    }

    /**
     * method in the Algos class. After the completion of the grounds, triangles
     * are removed from noise.
     * 
     * @param groundsTriangleMesh
     *            the ground
     * @param noise
     *            the noise TriangleMesh computed by former algorithms
     * @return a list of grounds completed with noise
     */
    private static List<TriangleMesh> noiseTreatment(
	    final TriangleMesh groundsTriangleMesh, final TriangleMesh noise) {

	List<TriangleMesh> list;

	list = Algos.blockExtract(groundsTriangleMesh);
	Algos.blockTreatNoise(list, noise);
	return list;
    }
}
