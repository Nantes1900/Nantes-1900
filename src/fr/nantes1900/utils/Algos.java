package fr.nantes1900.utils;

import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.middle.TriangleMesh;
import fr.nantes1900.models.middle.Surface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains some algorithms used in the other classes.
 * 
 * @author Daniel Lefevre
 */
public final class Algos {

    /**
     * Private constructor.
     */
    private Algos() {
    }

    /**
     * Divides the mesh in block of neighbours. This method uses
     * returnNeighbours to find the neighbours of one triangle, and puts it into
     * a new mesh into the arraylist. Thus, it takes another triangle and make
     * again the same operation until there is no more triangle. This method
     * does not destroy the mesh in parameter.
     * 
     * @param m
     *            the mesh to divide
     * @return an array of the blocks-meshes
     */
    public static List<TriangleMesh> blockExtract(final TriangleMesh m) {
	final Set<TriangleMesh> thingsList = new HashSet<TriangleMesh>();
	final TriangleMesh triangleMesh = new TriangleMesh(m);

	while (!triangleMesh.isEmpty()) {

	    final TriangleMesh e = new TriangleMesh();
	    triangleMesh.getOne().returnNeighbours(e, triangleMesh);
	    triangleMesh.remove(e);
	    thingsList.add(e);

	}

	return new ArrayList<TriangleMesh>(thingsList);
    }

    /**
     * Divides the mesh in block of neighbours depending on their orientations.
     * This method takes one triangle and use returnNeighbours to find the
     * triangles which are oriented as the first one (with an error) finds into
     * them its neighbours, and puts it in a new mesh into the arraylist. Then
     * it takes another triangle and make again the same operation until there
     * is no more triangle. This method does not destroy the mesh in parameter.
     * 
     * @param m
     *            the mesh to divide
     * @param angleNormalErrorFactor
     *            the error on the orientation
     * @return an array of the blocks-meshs
     */
    public static List<TriangleMesh> blockOrientedExtract(final TriangleMesh m,
	    final double angleNormalErrorFactor) {

	final List<TriangleMesh> thingsList = new ArrayList<TriangleMesh>();
	final List<TriangleMesh> meshList = Algos.blockExtract(m);

	for (final TriangleMesh triangleMesh : meshList) {
	    while (!triangleMesh.isEmpty()) {
		final TriangleMesh e = new TriangleMesh();
		final Triangle tri = triangleMesh.getOne();

		final TriangleMesh oriented = triangleMesh.orientedAs(tri.getNormal(),
			angleNormalErrorFactor);

		tri.returnNeighbours(e, oriented);

		triangleMesh.remove(e);
		thingsList.add(e);
	    }
	}

	return thingsList;
    }

    /**
     * Treats a list of mesh to add the noise which is neighbour of the mesh.
     * This method tries to find a block of noise which completes the mesh (of
     * the list). It thus adds it to the mesh.
     * 
     * @param list
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     */
    public static void blockTreatNoise(final List<TriangleMesh> list, final TriangleMesh noise) {

	final List<TriangleMesh> m = new ArrayList<TriangleMesh>();

	for (final TriangleMesh e : list) {
	    final TriangleMesh meshAndNoise = new TriangleMesh(e);
	    meshAndNoise.addAll(noise);
	    final TriangleMesh mes = new TriangleMesh();
	    e.getOne().returnNeighbours(mes, meshAndNoise);
	    m.add(mes);
	    noise.remove(mes);
	}

	list.clear();
	list.addAll(m);
    }

    /**
     * Treats a list of mesh to add the noise which is neighbour of the mesh.
     * This method tries to find a block of noise which completes the mesh (of
     * the list) and which is contained between two planes parallel to the mesh.
     * It thus adds it to the mesh.
     * 
     * @param list
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @param errorPlanes
     *            the distance between the two planes
     */
    public static void blockTreatPlanedNoise(final List<TriangleMesh> list,
	    final TriangleMesh noise, final double errorPlanes) {

	final List<TriangleMesh> m = new ArrayList<TriangleMesh>();

	for (final TriangleMesh e : list) {
	    final TriangleMesh meshAndNoise = new TriangleMesh(e);
	    meshAndNoise.addAll(noise.inPlanes(e.averageNormal(),
		    e.getCentroid(), errorPlanes));
	    final TriangleMesh mes = new TriangleMesh();
	    e.getOne().returnNeighbours(mes, meshAndNoise);
	    m.add(mes);
	    noise.remove(mes);
	}

	list.clear();
	list.addAll(m);
    }

    /**
     * Treats a list of mesh to add the noise which is neighbour of the mesh.
     * This method tries to find a block of noise which completes the mesh (of
     * the list) and which have the same orientation. It thus adds it to the
     * mesh.
     * 
     * @param wallList
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @param largeAngleNormalErrorFactor
     *            the error on the orientation
     */
    public static void blockTreatOrientedNoise(final List<TriangleMesh> wallList,
	    final TriangleMesh noise, final double largeAngleNormalErrorFactor) {

	final List<TriangleMesh> list = new ArrayList<TriangleMesh>();

	for (final TriangleMesh e : wallList) {
	    final TriangleMesh meshAndNoise = new TriangleMesh(e);
	    meshAndNoise.addAll(noise.orientedAs(e.averageNormal(),
		    largeAngleNormalErrorFactor));
	    final TriangleMesh mes = new TriangleMesh();
	    e.getOne().returnNeighbours(mes, meshAndNoise);
	    list.add(mes);

	    noise.remove(mes);
	}

	wallList.clear();
	wallList.addAll(list);
    }
}
