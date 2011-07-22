package models.extended;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

import models.Mesh;
import models.Polyline;
import utils.Algos;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;
import coefficients.SeparationTreatmentWallsRoofs;

public class Edifice {

	private ArrayList<Polyline> walls = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofs = new ArrayList<Polyline>();

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

	public void buildFromMesh(Mesh building, Vector3d normalFloor) {

		building.writeSTL("Files/building.stl");

		Mesh wholeWall = new Mesh();
		Mesh wholeRoof = new Mesh();

		Mesh noise = new Mesh();

		ArrayList<Mesh> wallList = this.sortWalls(building, normalFloor,
				wholeWall, noise);
		ArrayList<Mesh> roofList = this.sortRoofs(building, normalFloor,
				wholeRoof, noise);

		this.treatNoiseWalls(wallList, wholeWall, noise);
		this.treatNoiseRoofs(roofList, wholeRoof, noise);

		int counterWall = 0;
		for (Mesh m : wallList) {
			m.writeSTL("Files/" + "computedWall" + counterWall + ".stl");
			counterWall++;
		}

		int counterRoof = 0;
		for (Mesh m : roofList) {
			m.writeSTL("Files/" + "computedRoof" + counterRoof + ".stl");
			counterRoof++;
		}

		this.determinateNeighbours(wallList, roofList);

		this.findCommonEdges(wallList, roofList);
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

//	/**
//	 * For each wall, treat the surface and add it to the list. See behind.
//	 */
//	private void treatWalls(ArrayList<Mesh> wallList) {
//
//		int counterWall = 0;
//
//		// For each wall
//		while (counterWall < wallList.size()) {
//
//			Polyline wallComputed = this
//					.treatSurface(wallList.get(counterWall));
//
//			walls.add(wallComputed);
//
//			counterWall++;
//		}
//	}
//
//	/**
//	 * For each roof, treat the surface and add it to the list. See behind.
//	 */
//	private void treatRoofs(ArrayList<Mesh> roofList) {
//
//		int counterRoof = 0;
//
//		// For each roof
//		while (counterRoof < roofList.size()) {
//
//			Polyline roofComputed = this
//					.treatSurface(roofList.get(counterRoof));
//
//			this.roofs.add(roofComputed);
//
//			counterRoof++;
//		}
//	}
//
//	/**
//	 * Treat the surface : find the contour, project it on a plan, delete
//	 * noises, and find the singular points.
//	 * 
//	 * @param surface
//	 *            the surface to treat
//	 * @return the polyline containing the singular points.
//	 */
//	private Polyline treatSurface(Mesh surface) {
//
//		// Compute the contour of the surface
//		// Create other points, edges, on the new polyline to avoid the risk
//		// of modifying the meshes
//		// Compute the contour by calculating the longest bound
//		Polyline longestBound = new Polyline(surface.returnLongestBound(surface
//				.averageNormal()));
//
//		// Compute the change base matrix
//		Vector3d normalSurface = surface.averageNormal();
//		double[][] matrixSurface = null, matrixSurfaceInv = null;
//
//		try {
//			matrixSurface = MatrixMethod.createOrthoBase(normalSurface);
//			matrixSurfaceInv = MatrixMethod.getInversMatrix(matrixSurface);
//		} catch (SingularMatrixException e) {
//			System.err.println("Error in the matrix !");
//			System.exit(1);
//		}
//
//		// Change base to have the surface in the plan (x, y)
//		longestBound.changeBase(matrixSurface);
//
//		// Project it on z = average z
//		longestBound.zProjection(longestBound.zAverage());
//
//		// Compute the singular points
//		Polyline singularPoints = null;
//		try {
//			// Remove the too little edges, and the edges with bad orientations
//			singularPoints = longestBound.reductNoise(5);
//
//			// Find the points where the angle change is important.
//			singularPoints = singularPoints
//					.determinateSingularPoints(SeparationTreatmentWallsRoofs.errorSingularPoints);
//
//			// Compute in one edge a suite of edges which have the same
//			// orientation.
//			singularPoints = singularPoints.refine(5);
//
//		} catch (InvalidActivityException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// Inverse change base.
//		singularPoints.changeBase(matrixSurfaceInv);
//
//		// Set the normal attribute of the surface for the next steps.
//		singularPoints.setNormal(surface.averageNormal());
//
//		return singularPoints;
//	}

	private void determinateNeighbours(ArrayList<Mesh> wallList,
			ArrayList<Mesh> roofList) {

		for (Mesh m1 : roofList) {
			for (Mesh m2 : roofList) {
				if (m1.isNeighbour(m2)) {
					m1.addNeighbour(m2);
					m2.addNeighbour(m1);
				}
			}

			for (Mesh m2 : wallList) {
				if (m1.isNeighbour(m2)) {
					m1.addNeighbour(m2);
					m2.addNeighbour(m1);
				}
			}
		}

		for (Mesh m1 : wallList) {
			for (Mesh m2 : wallList) {
				if (m1.isNeighbour(m2)) {
					m1.addNeighbour(m2);
					m2.addNeighbour(m1);
				}
			}
		}
	}

	private void findCommonEdges(ArrayList<Mesh> wallList,
			ArrayList<Mesh> roofList) {

		double radiusError = 5;

		Polyline edges = new Polyline();

		Mesh m1 = roofList.get(0);
		for (Mesh m2 : m1.getNeighbours()) {
			edges.add(m1.findCommonEdge(m2, radiusError));
		}

		edges.buildContour();

		edges.writeCentroidMesh("testFindCommonEdges.stl");
	}

	public void writeSTLWalls(String directoryName) {
		int counterWall = 0;

		for (Polyline p : this.walls) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedWall" + counterWall + ".stl");

			counterWall++;
		}
	}

	public void writeSTLRoofs(String directoryName) {
		int counterRoof = 0;

		for (Polyline p : this.roofs) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedRoof" + counterRoof + ".stl");

			counterRoof++;
		}
	}

	public void writeSTL(String directoryName) {
		this.writeSTLWalls(directoryName);
		this.writeSTLRoofs(directoryName);
	}
}
