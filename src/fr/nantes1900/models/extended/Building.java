package fr.nantes1900.models.extended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.models.middle.Polygone;
import fr.nantes1900.models.middle.Surface;
import fr.nantes1900.models.middle.Surface.ImpossibleNeighboursOrderException;
import fr.nantes1900.models.middle.Surface.InvalidSurfaceException;
import fr.nantes1900.utils.Algos;

/**
 * Implements a building as two lists of surfaces : walls and roofs.
 * 
 * @author Daniel Lefevre
 */
public class Building {

    private Mesh initialTotalMesh;
    private Mesh initialWall;
    private Mesh initialRoof;
    private Mesh noise;
    private final List<Surface> walls = new ArrayList<Surface>();
    private final List<Surface> roofs = new ArrayList<Surface>();

    public Building(Mesh mesh) {
	this.initialTotalMesh = mesh;
    }

    public void separateWallRoof(Vector3d gravityNormal) {
	// Select the triangles which are oriented normal to normalGround.
	this.initialWall = this.initialTotalMesh.orientedNormalTo(
		gravityNormal, SeparationWallRoof.NORMALTO_ERROR);

	this.initialRoof = new Mesh(this.initialTotalMesh);
	this.initialRoof.remove(this.initialWall);
    }

    public void cutWalls() {
	Mesh building = new Mesh(this.initialTotalMesh);

	// Cut the mesh in parts, considering their orientation.
	final List<Mesh> thingsList = Algos.blockOrientedExtract(
		this.initialWall,
		SeparationWallsSeparationRoofs.WALL_ANGLE_ERROR);

	// Considering their size, sort the blocks in walls or noise.
	for (final Mesh e : thingsList) {
	    building.remove(e);
	    if (e.size() >= SeparationWallsSeparationRoofs.WALL_SIZE_ERROR) {
		this.walls.add(new Wall(e));
	    } else {
		this.noise.addAll(e);
	    }
	}
    }

    public void cutRoofs(Vector3d groundNormal) {
	// Cut the mesh in parts, considering their orientation.
	final List<Mesh> thingsList = Algos.blockOrientedExtract(
		this.initialRoof,
		SeparationWallsSeparationRoofs.ROOF_ANGLE_ERROR);

	// Considering their size and their orientation, sort the blocks in
	// roofs or noise. If a wall is oriented in direction of the ground,
	// it is not keeped.
	for (final Mesh e : thingsList) {
	    if ((e.size() >= SeparationWallsSeparationRoofs.ROOF_SIZE_ERROR)
		    && (e.averageNormal().dot(groundNormal) > 0)) {
		this.roofs.add(new Roof(e));
	    } else {
		this.noise.addAll(e);
	    }
	}
    }

    public void sortSurfaces() {

	int counter = 0;
	for (int i = 0; i < this.walls.size(); i++) {
	    final Surface s = this.walls.get(i);
	    if (s.getNeighbours().size() < 3) {
		this.walls.remove(s);
		for (final Surface neighbour : s.getNeighbours()) {
		    neighbour.getNeighbours().remove(s);
		}
		counter++;
	    }
	}
	for (int i = 0; i < this.roofs.size(); i++) {
	    final Surface s = this.roofs.get(i);
	    if (s.getNeighbours().size() < 3) {
		this.roofs.remove(s);
		for (final Surface neighbour : s.getNeighbours()) {
		    neighbour.getNeighbours().remove(s);
		}
		counter++;
	    }
	}
	System.out.println(" Isolated surfaces (not treated) : " + counter);
    }

    public void treatNewNeighbours(final Surface grounds) {

	this.searchForNeighbours(grounds);

	// After the noise addition, if some of the walls or some of the
	// roofs are now neighbours (they share an edge) and have the same
	// orientation, then they are added to form only one wall or roof.

	// Wall is prioritary : it means that if a roof touch a wall, this
	// roof is added to the wall, and not the inverse.

	final List<Surface> wholeList = new ArrayList<Surface>();
	wholeList.addAll(this.walls);
	wholeList.addAll(this.roofs);

	for (int i = 0; i < wholeList.size(); i = i + 1) {
	    final Surface surface = wholeList.get(i);

	    final List<Surface> oriented = new ArrayList<Surface>();
	    final List<Surface> ret = new ArrayList<Surface>();

	    for (final Surface m : wholeList) {
		if (m.getMesh().isOrientedAs(surface.getMesh(),
			SeparationWallsSeparationRoofs.MIDDLE_ANGLE_ERROR)) {
		    oriented.add(m);
		}
	    }

	    surface.returnNeighbours(ret, oriented);

	    for (final Surface m : ret) {
		if (m != surface) {
		    surface.getMesh().addAll(m.getMesh());
		    wholeList.remove(m);
		    this.walls.remove(m);
		    this.roofs.remove(m);
		}
	    }
	}
    }

    public void treatNoise() {
	// Adds the oriented and neighbour noise to the walls.
	Algos.blockTreatOrientedNoise(this.walls, this.noise,
		SeparationWallsSeparationRoofs.LARGE_ANGLE_ERROR);

	// Adds the oriented and neighbour noise to the roofs.
	Algos.blockTreatOrientedNoise(this.roofs, this.noise,
		SeparationWallsSeparationRoofs.LARGE_ANGLE_ERROR);
    }

    public void orderNeighbours(final Surface grounds) {
	// Adds all the surfaces
	final List<Surface> wholeList = new ArrayList<Surface>();
	wholeList.addAll(this.walls);
	wholeList.addAll(this.roofs);

	for (final Surface surface : wholeList) {
	    try {
		// Orders its neighbours in order to treat them.
		// If the neighbours of one surface are not 2 per 2 neighbours
		// each other, then it tries to correct it.
		surface.orderNeighbours(wholeList, grounds);

	    } catch (final ImpossibleNeighboursOrderException e) {
		// If there is a problem, the treatment cannot continue.
	    }
	}
    }

    public final void determinateContours(final Vector3d normalGround) {
	// Creates the map where the points and edges will be put : if one
	// point is created a second time, it will be given the same
	// reference as the other one having the same values.
	final Map<Point, Point> pointMap = new HashMap<Point, Point>();

	// Adds all the surfaces
	final List<Surface> wholeList = new ArrayList<Surface>();
	wholeList.addAll(this.walls);
	wholeList.addAll(this.roofs);

	for (final Surface surface : wholeList) {
	    try {
		// When the neighbours are sorted, finds the intersection of
		// them to find the edges of this surface.
		final Polygone p = surface.findEdges(this.walls, pointMap,
			normalGround);

		surface.setPolygone(p);
	    } catch (final InvalidSurfaceException e) {
		// If there is a problem, we cannot continue the treatment.
	    }
	}
    }

    public final List<Surface> getRoofs() {
	return this.roofs;
    }

    public final List<Surface> getWalls() {
	return this.walls;
    }

    public void reComputeGroundBounds() {
	// TODO : implement this method.
    }

    public void determinateNeighbours(final Surface grounds) {

	final Polygone groundsBounds = grounds.getMesh().returnUnsortedBounds();

	final List<Surface> wholeList = new ArrayList<Surface>();
	wholeList.addAll(this.walls);
	wholeList.addAll(this.roofs);

	// To find every neighbours, we complete every holes between roofs
	// and walls by adding all the noise.
	final List<Mesh> wholeListFakes = new ArrayList<Mesh>();
	for (final Surface m : wholeList) {
	    final Mesh fake = new Mesh(m.getMesh());
	    wholeListFakes.add(fake);
	}
	Algos.blockTreatPlanedNoise(wholeListFakes, this.noise,
		SeparationWallsSeparationRoofs.PLANES_ERROR);

	// First we clear the neighbours.
	for (final Surface s : wholeList) {
	    s.getNeighbours().clear();
	}
	// And we clear the neighbours of the grounds.
	grounds.getNeighbours().clear();

	// We compute the bounds to check if they share a common edge.
	final List<Polygone> wholeBoundsList = new ArrayList<Polygone>();
	for (final Mesh m : wholeListFakes) {
	    wholeBoundsList.add(m.returnUnsortedBounds());
	}

	// Then we check every edge of the bounds to see if some are shared by
	// two meshes. If they do, they are neighbours.
	for (int i = 0; i < wholeBoundsList.size(); i = i + 1) {
	    final Polygone polygone1 = wholeBoundsList.get(i);

	    for (int j = i + 1; j < wholeBoundsList.size(); j = j + 1) {
		final Polygone polygone2 = wholeBoundsList.get(j);

		if (polygone1.isNeighbour(polygone2)) {
		    wholeList.get(i).addNeighbour(wholeList.get(j));
		}
	    }

	    if (polygone1.isNeighbour(groundsBounds)) {
		wholeList.get(i).addNeighbour(grounds);
	    }
	}
    }

    // FIXME : what is the difference between searchForNeighbours and
    // determinateNeighbours.
    private void searchForNeighbours(final Surface grounds) {
	final Polygone groundsBounds = grounds.getMesh().returnUnsortedBounds();

	final List<Surface> wholeList = new ArrayList<Surface>();
	wholeList.addAll(this.walls);
	wholeList.addAll(this.roofs);

	// First we clear the neighbours.
	for (final Surface m : wholeList) {
	    m.getNeighbours().clear();
	}
	// And we clear the neighbours of the grounds.
	grounds.getNeighbours().clear();

	final List<Polygone> wholeBoundsList = new ArrayList<Polygone>();

	// We compute the bounds to check if they share a common edge.
	for (final Surface m : wholeList) {
	    wholeBoundsList.add(m.getMesh().returnUnsortedBounds());
	}

	// Then we check every edge of the bounds to see if some are shared
	// by two meshes. If they do, they are neighbours.
	for (int i = 0; i < wholeBoundsList.size(); i = i + 1) {
	    final Polygone polygone1 = wholeBoundsList.get(i);

	    for (int j = i + 1; j < wholeBoundsList.size(); j = j + 1) {
		final Polygone polygone2 = wholeBoundsList.get(j);
		if (polygone1.isNeighbour(polygone2)) {
		    wholeList.get(i).addNeighbour(wholeList.get(j));
		}
	    }

	    if (polygone1.isNeighbour(groundsBounds)) {
		wholeList.get(i).addNeighbour(grounds);
	    }
	}
    }
}
