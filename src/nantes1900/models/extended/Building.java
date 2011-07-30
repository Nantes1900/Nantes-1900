package nantes1900.models.extended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Vector3d;

import nantes1900.coefficients.SeparationTreatmentWallsRoofs;
import nantes1900.models.Mesh;
import nantes1900.models.Mesh.InvalidSurfaceException;
import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Point;
import nantes1900.utils.Algos;

public class Building {

	private ArrayList<Polyline> walls = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofs = new ArrayList<Polyline>();

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

		ArrayList<Polyline> wallsBoundsList = new ArrayList<Polyline>();
		ArrayList<Polyline> roofsBoundsList = new ArrayList<Polyline>();

		for (Mesh w : wallList) {
			wallsBoundsList.add(w.returnUnsortedBounds());
		}

		for (Mesh r : roofList) {
			roofsBoundsList.add(r.returnUnsortedBounds());
		}

		Polyline floorsBounds = floors.returnUnsortedBounds();

		for (int i = 0; i < wallsBoundsList.size(); i++) {

			Polyline p1 = wallsBoundsList.get(i);

			for (int j = 0; j < wallsBoundsList.size(); j++) {
				Polyline p2 = wallsBoundsList.get(j);
				if (p1.isNeighbour(p2)) {
					wallList.get(i).addNeighbour(wallList.get(j));
				}
			}

			for (int j = 0; j < roofsBoundsList.size(); j++) {
				Polyline p2 = roofsBoundsList.get(j);
				if (p1.isNeighbour(p2)) {
					wallList.get(i).addNeighbour(roofList.get(j));
				}
			}

			if (p1.isNeighbour(floorsBounds)) {
				wallList.get(i).addNeighbour(floors);
			}
		}

		for (int i = 0; i < roofsBoundsList.size(); i++) {

			Polyline p1 = roofsBoundsList.get(i);

			for (int j = 0; j < roofsBoundsList.size(); j++) {
				Polyline p2 = roofsBoundsList.get(j);
				if (p1.isNeighbour(p2)) {
					roofList.get(i).addNeighbour(roofList.get(j));
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
			ArrayList<Mesh> roofList, Vector3d normalFloor) {

		HashMap<Point, Point> pointMap = new HashMap<Point, Point>();
		HashMap<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

		int counterError = 0;

		for (Mesh m : roofList) {
			Polyline p;
			try {
				p = m.findEdges(wallList, pointMap, edgeMap, normalFloor);
				roofs.add(p);
			} catch (InvalidSurfaceException e) {
				// If this exception is catched, do not treat the surface.
				// FIXME
				counterError++;
			}
		}

		for (Mesh m : wallList) {
			Polyline p;
			try {
				p = m.findEdges(wallList, pointMap, edgeMap, normalFloor);
				walls.add(p);
			} catch (InvalidSurfaceException e) {
				// If this exception is catched, do not treat the surface.
				// FIXME
				counterError++;
			}
		}

//		System.out.println("Nombre de surfaces non traitées : " + counterError);
	}

	/**
	 * 
	 * Cut the rest of the mesh in roofs considering the orientation of the
	 * triangles.
	 */
	private ArrayList<Mesh> sortRoofs(Mesh building, Vector3d normalFloor,
			Mesh wholeRoof, Mesh noise) {

		ArrayList<Mesh> roofList = new ArrayList<Mesh>();

		// Cut the mesh in parts, considering their orientation.
		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(building,
				SeparationTreatmentWallsRoofs.ANGLE_ROOF_ERROR);

		// Compute the average of triangles per block (roof)
		int size = 0;
		for (Mesh e : thingsList) {
			size += e.size();
		}

		// Considering their size and their orientation, sort the blocks in
		// roofs or noise.
		for (Mesh e : thingsList) {
			if ((e.size() >= SeparationTreatmentWallsRoofs.ROOF_BLOCK_SIZE_ERROR
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
				SeparationTreatmentWallsRoofs.NORMALTO_ERROR);

		// Cut the mesh in parts, considering their orientation.
		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(wallOriented,
				SeparationTreatmentWallsRoofs.ANGLE_WALL_ERROR);

		// Compute the average of triangles per block (wall)
		int size = 0;
		for (Mesh e : thingsList) {
			building.remove(e);
			size += e.size();
		}

		// Considering their size, sort the blocks in walls or noise.
		for (Mesh e : thingsList) {
			if (e.size() >= SeparationTreatmentWallsRoofs.WALL_BLOCK_SIZE_ERROR
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
			Mesh noise) {

		// Add the oriented and neighbour noise to the walls.
		Algos.blockTreatOrientedNoise(wallList, noise,
				SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

		// Add the oriented and neighbour noise to the roofs.
		Algos.blockTreatOrientedNoise(roofList, noise,
				SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

		// After the noise add, if some of the alls or some of the roofs are now
		// neighbours (they share an edge) and have the same orientation, then
		// they are added to form only one
		// wall or roof.
		for (int i = 0; i < wallList.size(); i++) {
			Mesh w1 = wallList.get(i);

			for (int j = i + 1; j < wallList.size(); j++) {
				Mesh w2 = wallList.get(j);

				if (w1.isNeighbour(w2)
						&& w1.isOrientedAs(
								w2,
								SeparationTreatmentWallsRoofs.ANGLE_WALL_ERROR)) {
					w1.addAll(w2);
					wallList.remove(w2);
				}
			}

			for (int j = 0; j < roofList.size(); j++) {
				Mesh r2 = roofList.get(j);

				if (w1.isNeighbour(r2)
						&& w1.isOrientedAs(
								r2,
								SeparationTreatmentWallsRoofs.ANGLE_WALL_ERROR)) {
					w1.addAll(r2);
					roofList.remove(r2);
				}
			}
		}

		// See above.
		for (int i = 0; i < roofList.size(); i++) {
			Mesh r1 = roofList.get(i);

			for (int j = i + 1; j < roofList.size(); j++) {
				Mesh r2 = roofList.get(j);

				if (r1.isNeighbour(r2)
						&& r1.isOrientedAs(
								r2,
								SeparationTreatmentWallsRoofs.ANGLE_ROOF_ERROR)) {
					r1.addAll(r2);
					roofList.remove(r2);
				}
			}
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
	 * 
	 * Build the building from a mesh, by computingthe algorithms.
	 * 
	 * @param building
	 *            the mesh to compute
	 * @param normalFloor
	 *            the normal to the floor
	 */
	public void buildFromMesh(Mesh building, Mesh floors, Vector3d normalFloor) {

		building.writeSTL("Tests/St-Similien/m01/results/building.stl");

		Mesh wholeWall = new Mesh();
		Mesh wholeRoof = new Mesh();
		Mesh noise = new Mesh();

		ArrayList<Mesh> wallList = this.sortWalls(building, normalFloor,
				wholeWall, noise);
		ArrayList<Mesh> roofList = this.sortRoofs(building, normalFloor,
				wholeRoof, noise);

		this.treatNoise(wallList, roofList, noise);

		// FIXME : if some roofs are neighbours to the floors, remove them...
		// because it's noise. TODO ? Because if they are not noise.

		// Sum all the walls to build the wholeWall.
		int counter = 0;
		for (Mesh w : wallList) {
			wholeWall.addAll(w);
			w.writeSTL("Tests/St-Similien/m01/results/wall - " + counter
					+ ".stl");
			counter++;
		}

		// Sum all the roofs to build the wholeRoof.
		counter = 0;
		for (Mesh r : roofList) {
			wholeRoof.addAll(r);
			r.writeSTL("Tests/St-Similien/m01/results/roof - " + counter
					+ ".stl");
			counter++;
		}

		this.determinateNeighbours(wallList, roofList, floors);

		this.findCommonEdges(wallList, roofList, normalFloor);
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
