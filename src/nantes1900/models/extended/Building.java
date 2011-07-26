package nantes1900.models.extended;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.vecmath.Vector3d;

import nantes1900.coefficients.SeparationTreatmentWallsRoofs;
import nantes1900.models.Mesh;
import nantes1900.models.Polyline;
import nantes1900.utils.Algos;

public class Building {

	private ArrayList<Polyline> walls = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofs = new ArrayList<Polyline>();

	private Logger log = Logger.getLogger("logger");

	/**
	 * Constructor. Create the lists of walls and lists of roofs.
	 */
	public Building() {
	}

	/**
	 * Determinate the neighbours of each meshes. Two meshes are neighbours if
	 * they share an edge.
	 * 
	 * @param wallList
	 *            the list of walls as meshes
	 * @param roofList
	 *            the list of roofs as meshes
	 */
	private void determinateNeighbours(ArrayList<Mesh> wallList,
			ArrayList<Mesh> roofList, Mesh floors) {

		for (Mesh w1 : wallList) {
			for (Mesh w2 : wallList) {
				if (w1.isNeighbour(w2)) {
					w1.addNeighbour(w2);
				}
			}
			for (Mesh r2 : roofList) {
				if (w1.isNeighbour(r2)) {
					w1.addNeighbour(r2);
				}
			}
			if (w1.isNeighbour(floors)) {
				w1.addNeighbour(floors);
			}
		}

		for (Mesh r1 : roofList) {
			for (Mesh r2 : roofList) {
				if (r1.isNeighbour(r2)) {
					r1.addNeighbour(r2);
				}
			}
		}
	}

	/**
	 * Build the polyline. With all the neighbours, build the polyline by
	 * computing the common edges between them, and put them into the list of
	 * polylines walls and roofs.
	 * 
	 * @param wallList
	 *            the list of walls as meshes
	 * @param roofList
	 *            the list of roofs as meshes
	 */
	private void findCommonEdges(ArrayList<Mesh> wallList,
			ArrayList<Mesh> roofList) {

		for (Mesh m : roofList) {
			Polyline p = m.findEdgesRoof();
			if (p != null && !p.isEmpty()) {
				roofs.add(p);
			}
		}

		for (Mesh m : wallList) {
			Polyline p = m.findEdgesWall();
			if (p != null && !p.isEmpty()) {
				walls.add(p);
			}
		}
	}

	private void recollerLesmMorceaux(ArrayList<Mesh> wallList, Mesh floors) {

	}

	/**
	 * Cut the rest of the mesh in roofs considering the orientation of the
	 * triangles.
	 */
	private ArrayList<Mesh> sortRoofs(Mesh building, Vector3d normalFloor,
			Mesh wholeRoof, Mesh noise) {

		ArrayList<Mesh> roofList = new ArrayList<Mesh>();

		// Cut the mesh in parts, considering their orientation.
		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(building,
				SeparationTreatmentWallsRoofs.angleNormalErrorFactor);

		// Compute the average of triangles per block (roof)
		int size = 0;
		for (Mesh e : thingsList) {
			size += e.size();
		}

		// Considering their size and their orientation, sort the blocks in
		// roofs or noise.
		for (Mesh e : thingsList) {
			if ((e.size() >= SeparationTreatmentWallsRoofs.errorNumberTrianglesRoof
					* (double) size / (double) thingsList.size())
					&& (e.averageNormal().dot(normalFloor) > 0)) {
				roofList.add(e);
			} else
				noise.addAll(e);
		}

		// Add the roofs to the wholeRoof mesh.
		for (Mesh r : roofList) {
			wholeRoof.addAll(r);
		}

		return roofList;
	}

	/**
	 * Cut the normal-to-the-floor mesh in walls considering the orientation of
	 * the triangles.
	 */
	private ArrayList<Mesh> sortWalls(Mesh building, Vector3d normalFloor,
			Mesh wholeWall, Mesh noise) {

		ArrayList<Mesh> wallList = new ArrayList<Mesh>();

		// Select the triangles which are oriented normal to normalFloor.
		Mesh wallOriented = building.orientedNormalTo(normalFloor,
				SeparationTreatmentWallsRoofs.errorNormalToFactor);

		// Cut the mesh in parts, considering their orientation.
		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(wallOriented,
				SeparationTreatmentWallsRoofs.angleNormalErrorFactor);

		// Compute the average of triangles per block (wall)
		int size = 0;
		for (Mesh e : thingsList) {
			building.remove(e);
			size += e.size();
		}

		// Considering their size, sort the blocks in walls or noise.
		for (Mesh e : thingsList) {
			if (e.size() >= SeparationTreatmentWallsRoofs.errorNumberTrianglesWall
					* (double) size / (double) thingsList.size()) {
				wallList.add(e);
			} else
				noise.addAll(e);
		}

		// Add the walls to the wholeWall mesh.
		for (Mesh w : wallList) {
			wholeWall.addAll(w);
		}

		return wallList;
	}

	/**
	 * Add the noise to the surface which is its neighbour, and which has the
	 * same orientation. The error orientation is determined by the
	 * largeAngleNormalErrorFactor.
	 */
	private void treatNoise(ArrayList<Mesh> wallList, ArrayList<Mesh> roofList,
			Mesh wholeWall, Mesh wholeRoof, Mesh noise) {

		// Add the oriented and neighbour noise to the walls.
		Algos.blockTreatOrientedNoise(wallList, noise,
				SeparationTreatmentWallsRoofs.largeAngleNormalErrorFactor);

		// Add the oriented and neighbour noise to the roofs.
		Algos.blockTreatOrientedNoise(roofList, noise,
				SeparationTreatmentWallsRoofs.largeAngleNormalErrorFactor);

		for (int i = 0; i < wallList.size(); i++) {
			Mesh w1 = wallList.get(i);

			for (int j = i + 1; j < wallList.size(); j++) {
				Mesh w2 = wallList.get(j);

				if (w1.isNeighbour(w2)
						&& w1.isOrientedAs(
								w2,
								SeparationTreatmentWallsRoofs.angleNormalErrorFactor)) {
					w1.addAll(w2);
					wallList.remove(w2);
				}

			}
		}

		// Sum all the walls to build the wholeWall.
		for (Mesh w : wallList) {
			wholeWall.addAll(w);
		}

		// Sum all the roofs to build the wholeRoof.
		for (Mesh r : roofList) {
			wholeRoof.addAll(r);
		}
	}

	/**
	 * Add a roof to the attribute list of roofs.
	 * 
	 * @param roof
	 *            the roof to add
	 */
	public void addRoof(Polyline roof) {
		this.roofs.add(roof);
	}

	/**
	 * Add a list of roofs to the attribute list of roofs.
	 * 
	 * @param roofs
	 *            the list of roofs to add
	 */
	public void addRoofs(List<Polyline> roofs) {
		this.roofs.addAll(roofs);
	}

	/**
	 * Add a wall to the attribute list of walls.
	 * 
	 * @param wall
	 *            the wall to add
	 */
	public void addWall(Polyline wall) {
		this.walls.add(wall);
	}

	/**
	 * Add a list of walls to the attribute list of walls.
	 * 
	 * @param wall
	 *            the wall to add
	 */
	public void addWalls(List<Polyline> walls) {
		this.walls.addAll(walls);
	}

	/**
	 * Build the building from a mesh, by computingthe algorithms.
	 * 
	 * @param building
	 *            the mesh to compute
	 * @param normalFloor
	 *            the normal to the floor
	 */
	public void buildFromMesh(Mesh building, Mesh floors, Vector3d normalFloor) {

		Mesh wholeWall = new Mesh();
		Mesh wholeRoof = new Mesh();
		Mesh noise = new Mesh();

		long time = System.nanoTime();
		ArrayList<Mesh> wallList = this.sortWalls(building, normalFloor,
				wholeWall, noise);
		log.finest("Sort walls : " + (System.nanoTime() - time));

		time = System.nanoTime();
		ArrayList<Mesh> roofList = this.sortRoofs(building, normalFloor,
				wholeRoof, noise);
		log.finest("Sort roofs : " + (System.nanoTime() - time));

		time = System.nanoTime();
		// TODO : compute wholeWall and wholeRoof in the method, and not in
		// sortRoofs or sortWalls.
		this.treatNoise(wallList, roofList, wholeWall, wholeRoof, noise);
		log.finest("Treat noise : " + (System.nanoTime() - time));

		time = System.nanoTime();
		this.determinateNeighbours(wallList, roofList, floors);
		log.finest("Determinate neighbours : " + (System.nanoTime() - time));

		time = System.nanoTime();
		this.findCommonEdges(wallList, roofList);
		log.finest("Find common edges : " + (System.nanoTime() - time));

		this.recollerLesmMorceaux(wallList, floors);
	}

	/**
	 * Getter.
	 * 
	 * @return the list of roofs
	 */
	public List<Polyline> getRoofs() {
		return this.roofs;
	}

	/**
	 * Getter.
	 * 
	 * @return the list of walls
	 */
	public List<Polyline> getWalls() {
		return this.walls;
	}

	/**
	 * Write the building in STL files. Use for debugging.
	 * 
	 * @param directoryName
	 *            the directory name to write in
	 */
	public void writeSTL(String directoryName) {
		this.writeSTLWalls(directoryName);
		this.writeSTLRoofs(directoryName);
	}

	/**
	 * Write the roofs in STL files. Use for debugging.
	 * 
	 * @param directoryName
	 *            the directory name to write in
	 */
	public void writeSTLRoofs(String directoryName) {
		int counterRoof = 0;

		for (Polyline p : this.roofs) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedRoof" + counterRoof + ".stl");

			counterRoof++;
		}
	}

	/**
	 * Write the walls in STL files. Use for debugging.
	 * 
	 * @param directoryName
	 *            the directory name to write in
	 */
	public void writeSTLWalls(String directoryName) {
		int counterWall = 0;

		for (Polyline p : this.walls) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedWall" + counterWall + ".stl");

			counterWall++;
		}
	}
}
