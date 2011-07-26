package nantes1900.utils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;

import nantes1900.models.Mesh;
import nantes1900.models.basis.Triangle;


/**
 * @author Daniel Lefevre
 * 
 */
public final class Algos {

	/**
	 * Implement an exception when the floor is empty.
	 * 
	 * @author Daniel Lefevre
	 */
	public static class NoFloorException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Divide the mesh in block of neighbours. This method use returnNeighbours
	 * to find the neighbours of one triangle, and put it into a new mesh into
	 * the arraylist. Thus, it takes another triangle and redo the same
	 * operation until there is no more triangle. This method does not destroy
	 * the mesh in parameter.
	 * 
	 * @param m
	 *            the mesh to divide
	 * @return an array of the blocks-meshs
	 */
	public static ArrayList<Mesh> blockExtract(Mesh m) {
		HashSet<Mesh> thingsList = new HashSet<Mesh>();
		Mesh mesh = new Mesh(m);

		while (!mesh.isEmpty()) {

			Mesh e = new Mesh();
			mesh.getOne().returnNeighbours(e, mesh);
			mesh.remove(e);
			thingsList.add(e);

		}

		return new ArrayList<Mesh>(thingsList);
	}

	/**
	 * Divide the mesh in block of neighbours depending on their orientations.
	 * This method takes one triangle and use returnNeighbours to find the
	 * triangles which are oriented as the first one (with an error) and find
	 * into them its neighbours, and put it in a new mesh into the arraylist.
	 * Thus, it takes another triangle and redo the same operation until there
	 * is no more triangle. This method does not destroy the mesh in parameter.
	 * 
	 * @param m
	 *            the mesh to divide
	 * @param angleNormalErrorFactor
	 *            the error on the orientation
	 * @return an array of the blocks-meshs
	 */
	public static ArrayList<Mesh> blockOrientedExtract(Mesh m,
			double angleNormalErrorFactor) {
		ArrayList<Mesh> thingsList = new ArrayList<Mesh>();
		Mesh mesh = new Mesh(m);

		while (!mesh.isEmpty()) {

			Mesh e = new Mesh();
			Triangle tri = mesh.getOne();
			Mesh oriented = mesh.orientedAs(tri.getNormal(),
					angleNormalErrorFactor);
			tri.returnNeighbours(e, oriented);
			mesh.remove(e);
			thingsList.add(e);

		}

		return thingsList;
	}

	/**
	 * Treat a list of mesh to add the noise which is part of the mesh. This
	 * method try to find a block of noise which complete the mesh (of the list)
	 * and which have the same orientation. It thus adds it to the mesh.
	 * 
	 * @param list
	 *            the list of mesh to complete with noise
	 * @param noise
	 *            the whole noise
	 * @param largeAngleNormalErrorFactor
	 *            the error on the orientation
	 * @return the list of mesh updated
	 */
	public static void blockTreatOrientedNoise(ArrayList<Mesh> list,
			Mesh noise, double largeAngleNormalErrorFactor) {

		ArrayList<Mesh> m = new ArrayList<Mesh>();

		for (Mesh e : list) {
			Mesh meshAndNoise = new Mesh(e);
			Mesh noiseOriented = noise.orientedAs(e.averageNormal(),
					largeAngleNormalErrorFactor);
			meshAndNoise.addAll(noiseOriented);
			Mesh mes = new Mesh();
			e.getOne().returnNeighbours(mes, meshAndNoise);
			m.add(mes);
			noise.remove(mes);
		}

		list.clear();
		list.addAll(m);
	}

	/**
	 * Extract the floor in a mesh. Receiving a mesh of triangles oriented as
	 * the floor, tt searches the lowest altitude as the lowest z, and take a
	 * stripe of triangles that are contained in the lowest altitude and an
	 * error. In this stripe, it takes one triangle, and returns all its
	 * neighbours. FIXME : find the altitude factor automatically
	 * 
	 * @param meshOriented
	 *            the floor-oriented triangles that will be treated
	 * @param altitudeErrorFactor
	 *            the error on the altitude
	 * @return the mesh containing the floor extracted
	 * @throws NoFloorException
	 *             if there is no floor
	 */
	public static Mesh floorExtract(Mesh meshOriented,
			double altitudeErrorFactor) throws NoFloorException {
		Mesh floors = new Mesh();

		try {
			Triangle lowestTriangle = meshOriented.zMinFace();
			double lowestZ = lowestTriangle.zMin();

			Mesh stripe = meshOriented.zBetween(lowestZ, lowestZ
					+ altitudeErrorFactor);

			Mesh temp = new Mesh();

			while (!stripe.isEmpty()) {

				lowestTriangle = stripe.getOne();
				temp.clear();
				lowestTriangle.returnNeighbours(temp, meshOriented);
				floors.addAll(temp);
				meshOriented.remove(temp);
				stripe.remove(temp);

			}

			return floors;
		} catch (InvalidParameterException e) {
			throw new NoFloorException();
		}
	}

	private Algos() {
	}
}