package modeles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

import algos.SeparationTreatmentWallsRoofs;

import utils.Algos;
import utils.MatrixMethod;
import utils.ParserSTL;
import utils.MatrixMethod.SingularMatrixException;
import utils.ParserSTL.BadFormedFileException;

public class Edifice {

	private ArrayList<Polyline> walls = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofs = new ArrayList<Polyline>();

	private int counterWall = 1;
	private int counterRoof = 1;

	private Logger log = Logger.getLogger("logger");

	public Edifice() {
	}

	public void addWall(Polyline wall) {
		this.walls.add(wall);
	}

	public void addRoof(Polyline roof) {
		this.roofs.add(roof);
	}

	public void addWalls(List<Polyline> walls) {
		this.walls.addAll(walls);
	}

	public void addRoofs(List<Polyline> roofs) {
		this.roofs.addAll(roofs);
	}

	public List<Polyline> getWalls() {
		return this.walls;
	}

	public List<Polyline> getRoofs() {
		return this.roofs;
	}

	public void buildFromMesh(String fileName, Vector3d normalFloor) {

		Mesh building = this.parseBuilding(fileName);

		Mesh wholeWall = new Mesh();
		Mesh wholeRoof = new Mesh();

		Mesh noise = new Mesh();

		ArrayList<Mesh> wallList = this.sortWalls(building, normalFloor,
				wholeWall, noise);
		ArrayList<Mesh> roofList = this.sortRoofs(building, normalFloor,
				wholeRoof, noise);

		this.treatNoiseWalls(wallList, wholeWall, noise);
		this.treatNoiseRoofs(roofList, wholeRoof, noise);

		this.treatWalls(wallList);
		this.treatRoofs(roofList);

		this.determinateNeighbours(wallList, roofList);

		this.findCommonEdges();
	}

	/**
	 * Parse the building. Catch and treat the errors.
	 * 
	 * @param fileName
	 *            the name of the file containing the building
	 */
	private Mesh parseBuilding(String fileName) {
		try {

			return new Mesh(ParserSTL.readSTL(fileName));

		} catch (BadFormedFileException e) {
			log.severe("Error in the file !");
			System.exit(1);
			return null;
		} catch (IOException e) {
			log.severe("Error : file does not exist or is unreadable !");
			System.exit(1);
			return null;
		}
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
	 * Add the noise to the wall which is its neighbour, and which has the same
	 * orientation. The error orientation is determined by the
	 * largeAngleNormalErrorFactor.
	 */
	private void treatNoiseWalls(ArrayList<Mesh> wallList, Mesh wholeWall,
			Mesh noise) {

		// Add the oriented and neighbour noise to the walls.
		wallList = Algos.blockTreatOrientedNoise(wallList, noise,
				SeparationTreatmentWallsRoofs.largeAngleNormalErrorFactor);

		// Add all the walls to build the wholeWall.
		wholeWall = new Mesh();
		for (Mesh w : wallList) {
			wholeWall.addAll(w);
		}
	}

	/**
	 * Add the noise to the floor which is its neighbour, and which has the same
	 * orientation. The error orientation is determined by the
	 * largeAngleNormalErrorFactor.
	 */
	private void treatNoiseRoofs(ArrayList<Mesh> roofList, Mesh wholeRoof,
			Mesh noise) {

		// Add the oriented and neighbour noise to the roofs.
		roofList = Algos.blockTreatOrientedNoise(roofList, noise,
				SeparationTreatmentWallsRoofs.largeAngleNormalErrorFactor);

		// Add all the roofs to build the wholeRoof.
		wholeRoof = new Mesh();
		for (Mesh w : roofList) {
			wholeRoof.addAll(w);
		}
	}

	/**
	 * For each wall, treat the surface and add it to the list. See behind.
	 */
	private void treatWalls(ArrayList<Mesh> wallList) {

		this.counterWall = 0;

		// For each wall
		while (this.counterWall < wallList.size()) {

			Polyline wallComputed = this.treatSurface(wallList
					.get(this.counterWall));

			walls.add(wallComputed);

			this.counterWall++;
		}
	}

	/**
	 * For each roof, treat the surface and add it to the list. See behind.
	 */
	private void treatRoofs(ArrayList<Mesh> roofList) {

		this.counterRoof = 0;

		// For each roof
		while (this.counterRoof < roofList.size()) {

			Polyline roofComputed = this.treatSurface(roofList
					.get(this.counterRoof));

			this.roofs.add(roofComputed);

			this.counterRoof++;
		}
	}

	/**
	 * Treat the surface : find the contour, project it on a plan, delete
	 * noises, and find the singular points.
	 * 
	 * @param surface
	 *            the surface to treat
	 * @return the polyline containing the singular points.
	 */
	private Polyline treatSurface(Mesh surface) {

		// Compute the contour of the surface
		// Create other points, edges, on the new polyline to avoid the risk
		// of modifying the meshes
		// Compute the contour by calculating the longest bound
		Polyline longestBound = new Polyline(surface.returnLongestBound(surface
				.averageNormal()));

		// Compute the change base matrix
		Vector3d normalSurface = surface.averageNormal();
		double[][] matrixSurface = null, matrixSurfaceInv = null;

		try {
			matrixSurface = MatrixMethod.createOrthoBase(normalSurface);
			matrixSurfaceInv = MatrixMethod.getInversMatrix(matrixSurface);
		} catch (SingularMatrixException e) {
			System.err.println("Error in the matrix !");
			System.exit(1);
		}

		// Change base to have the surface in the plan (x, y)
		longestBound.changeBase(matrixSurface);

		// Project it on z = average z
		longestBound.zProjection(longestBound.zAverage());

		// Compute the singular points
		Polyline singularPoints = null;
		try {
			// Remove the too little edges, and the edges with bad orientations
			singularPoints = longestBound.reductNoise(5);

			// Find the points where the angle change is important.
			singularPoints = singularPoints
					.determinateSingularPoints(SeparationTreatmentWallsRoofs.errorSingularPoints);

			// Compute in one edge a suite of edges which have the same
			// orientation.
			singularPoints = singularPoints.refine(5);

		} catch (InvalidActivityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Inverse change base.
		singularPoints.changeBase(matrixSurfaceInv);

		// Set the normal attribute of the surface for the next steps.
		singularPoints.setNormal(surface.averageNormal());

		return singularPoints;
	}

	private void determinateNeighbours(ArrayList<Mesh> wallList,
			ArrayList<Mesh> roofList) {

		for (int i = 0; i < this.roofs.size(); i++) {
			Polyline p = this.roofs.get(i);

			for (int k = 0; k < this.roofs.size(); k++) {
				Polyline line = this.roofs.get(k);

				if (roofList.get(i).isNeighbour(roofList.get(k))) {
					p.addNeighbour(line);
					line.addNeighbour(p);
				}
			}

			for (int k = 0; k < this.walls.size(); k++) {
				Polyline line = this.walls.get(k);

				if (roofList.get(i).isNeighbour(wallList.get(k))) {
					p.addNeighbour(line);
					line.addNeighbour(p);
				}
			}
		}

		for (int i = 0; i < this.walls.size(); i++) {
			Polyline p = this.walls.get(i);

			for (int k = 0; k < this.roofs.size(); k++) {
				Polyline line = this.roofs.get(k);

				if (wallList.get(i).isNeighbour(roofList.get(k))) {
					p.addNeighbour(line);
					line.addNeighbour(p);
				}
			}

			for (int k = 0; k < this.walls.size(); k++) {
				Polyline line = this.walls.get(k);

				if (wallList.get(i).isNeighbour(wallList.get(k))) {
					p.addNeighbour(line);
					line.addNeighbour(p);
				}
			}
		}
	}

	private void findCommonEdges() {

		double radiusError = 5;

		// FIXME !
		for (Polyline p1 : this.roofs) {
			for (Polyline p2 : p1.getNeighbours()) {
				p1.findCommonEdge(p2, radiusError);
			}
		}
	}

	public void writeSTLWalls(String directoryName) {
		this.counterWall = 0;

		for (Polyline p : this.walls) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedWall" + this.counterWall + ".stl");

			this.counterWall++;
		}
	}

	public void writeSTLRoofs(String directoryName) {
		this.counterRoof = 0;

		for (Polyline p : this.roofs) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedRoof" + this.counterRoof + ".stl");

			this.counterRoof++;
		}
	}

	// TODO : modify it !
	public void writeSTL(String directoryName) {
		this.writeSTLWalls(directoryName);
		this.writeSTLRoofs(directoryName);
	}
}
