package nantes1900.models.extended;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import nantes1900.coefficients.SeparationTreatmentWallsRoofs;
import nantes1900.models.Mesh;
import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Point;
import nantes1900.utils.Algos;
import nantes1900.utils.MatrixMethod.SingularMatrixException;

public class Building {

	private ArrayList<Polyline> walls = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofs = new ArrayList<Polyline>();

	public Building() {
	}

	private void findCommonEdges(ArrayList<Mesh> wallList,
			ArrayList<Mesh> roofList) {

		int counter = 0;
		for (Mesh m1 : roofList) {

			ArrayList<Mesh> neighbours = new ArrayList<Mesh>();

			Polyline edges = new Polyline();

			for (Mesh m2 : roofList) {
				if (m1.isNeighbour(m2)) {
					neighbours.add(m2);
				}
			}
			for (Mesh m2 : wallList) {
				if (m1.isNeighbour(m2)) {
					neighbours.add(m2);
				}
			}

			int counterError;
			for (Mesh m2 : neighbours) {

				counterError = 0;
				ArrayList<Point> points = new ArrayList<Point>();

				for (Mesh m3 : neighbours) {

					if (m2.isNeighbour(m3)) {
						counterError++;
						if (counterError > 2) {
							System.err.println("Erreur 1 !");
						}
						try {
							points.add(m1.intersection(m2, m3));
						} catch (SingularMatrixException e1) {
							System.err.println("Erreur 2 !");
						}
					}
				}
				if (counterError == 2) {
					Edge e = new Edge(points.get(0), points.get(1));
					edges.add(e);
				} else {
					System.err.println("Erreur 3 !");
				}
			}

			if (edges.edgeSize() > 2) {

				edges.setNormal(m1.averageNormal());

				edges.writeCentroidMesh("Files/testFindCommonEdgesRoofs"
						+ counter + ".stl");
			}
			counter++;
		}

		counter = 0;
		for (Mesh m1 : wallList) {

			ArrayList<Mesh> neighbours = new ArrayList<Mesh>();

			Polyline edges = new Polyline();

			for (Mesh m2 : roofList) {
				if (m1.isNeighbour(m2)) {
					neighbours.add(m2);
				}
			}
			for (Mesh m2 : wallList) {
				if (m1.isNeighbour(m2)) {
					neighbours.add(m2);
				}
			}

			int counterError;
			for (Mesh m2 : neighbours) {

				counterError = 0;
				ArrayList<Point> points = new ArrayList<Point>();

				for (Mesh m3 : neighbours) {

					if (m2.isNeighbour(m3)) {
						counterError++;
						if (counterError > 2) {
							System.err.println("Erreur 1 !");
						}
						try {
							points.add(m1.intersection(m2, m3));
						} catch (SingularMatrixException e1) {
							System.err.println("Erreur 2 !");
						}
					}
				}
				if (counterError == 2) {
					Edge e = new Edge(points.get(0), points.get(1));
					edges.add(e);
				} else {
					System.err.println("Erreur 3 !");
				}
			}

			if (edges.edgeSize() > 2) {

				edges.setNormal(m1.averageNormal());

				edges.writeCentroidMesh("Files/testFindCommonEdgesWalls"
						+ counter + ".stl");
			}
			counter++;
		}
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

		// Sum all the walls to build the wholeWall.
		for (Mesh w : wallList) {
			wholeWall.addAll(w);
		}

		// Sum all the roofs to build the wholeRoof.
		for (Mesh r : roofList) {
			wholeRoof.addAll(r);
		}
	}

	public void addRoof(Polyline roof) {
		this.roofs.add(roof);
	}

	public void addRoofs(List<Polyline> roofs) {
		this.roofs.addAll(roofs);
	}

	public void addWall(Polyline wall) {
		this.walls.add(wall);
	}

	public void addWalls(List<Polyline> walls) {
		this.walls.addAll(walls);
	}

	public void buildFromMesh(Mesh building, Vector3d normalFloor) {

		Mesh wholeWall = new Mesh();
		Mesh wholeRoof = new Mesh();
		Mesh noise = new Mesh();

		ArrayList<Mesh> wallList = this.sortWalls(building, normalFloor,
				wholeWall, noise);
		ArrayList<Mesh> roofList = this.sortRoofs(building, normalFloor,
				wholeRoof, noise);

		this.treatNoise(wallList, roofList, wholeWall, wholeRoof, noise);

		// FIXME : some walls are contained in the others...

		int counterWall = 0;
		for (Mesh m : wallList) {
			m.writeSTL("Files/" + "wall - " + counterWall + ".stl");
			counterWall++;
		}

		int counterRoof = 0;
		for (Mesh m : roofList) {
			m.writeSTL("Files/" + "roof - " + counterRoof + ".stl");
			counterRoof++;
		}

		this.findCommonEdges(wallList, roofList);
	}

	public List<Polyline> getRoofs() {
		return this.roofs;
	}

	public List<Polyline> getWalls() {
		return this.walls;
	}

	public void writeSTL(String directoryName) {
		this.writeSTLWalls(directoryName);
		this.writeSTLRoofs(directoryName);
	}

	public void writeSTLRoofs(String directoryName) {
		int counterRoof = 0;

		for (Polyline p : this.roofs) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedRoof" + counterRoof + ".stl");

			counterRoof++;
		}
	}

	public void writeSTLWalls(String directoryName) {
		int counterWall = 0;

		for (Polyline p : this.walls) {

			p.returnCentroidMesh().writeSTL(
					directoryName + "computedWall" + counterWall + ".stl");

			counterWall++;
		}
	}
}
