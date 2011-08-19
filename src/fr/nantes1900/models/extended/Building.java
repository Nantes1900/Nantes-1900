package fr.nantes1900.models.extended;

import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.constants.SeparationTreatmentWallsRoofs;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.Polyline.EmptyPolylineException;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.Surface.InvalidSurfaceException;
import fr.nantes1900.models.Surface.ImpossibleNeighboursOrderException;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.utils.Algos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

/**
 * Implements a building as a list of surfaces : walls and roofs.
 * 
 * @author Daniel Lefevre
 */
public class Building {

    /**
     * List of walls as polylines.
     */
    private final List<Polyline> walls = new ArrayList<Polyline>();

    /**
     * List of roofs as polylines.
     */
    private final List<Polyline> roofs = new ArrayList<Polyline>();

    /**
     * Constructor. Create the lists of walls and lists of roofs.
     */
    public Building() {
    }

    /**
     * Add a roof to the attribute list of roofs.
     * 
     * @param roof
     *            the roof to add
     */
    public final void addRoof(final Polyline roof) {
        this.roofs.add(roof);
    }

    /**
     * Add a list of roofs to the attribute list of roofs.
     * 
     * @param addRoofs
     *            the list of roofs to add
     */
    public final void addRoofs(final List<Polyline> addRoofs) {
        this.roofs.addAll(addRoofs);
    }

    /**
     * Add a wall to the attribute list of walls.
     * 
     * @param wall
     *            the wall to add
     */
    public final void addWall(final Polyline wall) {
        this.walls.add(wall);
    }

    /**
     * Add a list of walls to the attribute list of walls.
     * 
     * @param addWalls
     *            the wall to add
     */
    public final void addWalls(final List<Polyline> addWalls) {
        this.walls.addAll(addWalls);
    }

    /**
     * Build the building from a mesh, by computing the algorithms.
     * 
     * @param building
     *            the mesh to compute
     * @param normalFloor
     *            the normal to the floor
     * @param floors
     *            the mesh containing all the floors
     */
    public final void buildFromMesh(final Mesh building, final Mesh floors,
        final Vector3d normalFloor) {

        // Creates new meshes.
        final Mesh noise = new Mesh();

        // Applies the first algorithms : extract the walls, and after this,
        // extract the roofs.
        // FIXME : improve the speed of sortWalls and sortRoofs...
        final List<Mesh> wallList =
            this.sortWalls(building, normalFloor, noise);

        final List<Mesh> roofList =
            this.sortRoofs(building, normalFloor, noise);

        // Treats the noise, search deeply for new neighbours, find them, and
        // merge them if they belong to the same surface.
        this.treatNoise(wallList, roofList, noise, floors);

        final List<Surface> wallTreatedList = new ArrayList<Surface>();
        final List<Surface> roofTreatedList = new ArrayList<Surface>();
        this.vectorizeSurfaces(wallList, roofList, wallTreatedList,
            roofTreatedList);
        final Surface floorsTreated = new Surface(floors);

        // Finds the neighbours, searching deeply (add noise to the walls
        // and roofs to add every edge to one of these surface. Then find
        // the surfaces which share one edge.
        this.searchForNeighbours(wallTreatedList, roofTreatedList,
            floorsTreated, noise);

        // Treats the new neighbours.
        this.treatNewNeighbours(wallTreatedList, roofTreatedList);

        // Find the neighbours of each roof and wall (and the floors).
        this.determinateNeighbours(wallTreatedList, roofTreatedList,
            floorsTreated);

        // From all the neighbours, computes the wrap line and returns the
        // surfaces as polylines.
        this.findEdgesFromNeighbours(wallTreatedList, roofTreatedList,
            normalFloor, floorsTreated);

        this.writeSTL("files/St-Similien/m02/results/");
    }

    /**
     * Getter.
     * 
     * @return the list of roofs
     */
    public final List<Polyline> getRoofs() {
        return this.roofs;
    }

    /**
     * Getter.
     * 
     * @return the list of walls
     */
    public final List<Polyline> getWalls() {
        return this.walls;
    }

    public void findEdgesFromNeighbours(List<Surface> wallTreatedList,
        List<Surface> roofTreatedList, Vector3d normalFloor, Surface floors) {

        final Map<Point, Point> pointMap = new HashMap<Point, Point>();
        final Map<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

        final List<Surface> wholeTreatedList = new ArrayList<Surface>();
        wholeTreatedList.addAll(wallTreatedList);
        wholeTreatedList.addAll(roofTreatedList);

        int counterWellTreated = 0;
        int counterNotTreated = 0;

        for (Surface surface : wholeTreatedList) {
            try {

                // We order its neighbours in order to treat them.
                // If the neighbours of one surface are not 2 per 2 neighbours
                // each other, then it tries to correct it.
                surface.orderNeighbours(wholeTreatedList, floors);

                // When the neighbours are sorted, we find the intersection of
                // them to find the edges of this surface.
                Polyline p =
                    surface.findEdges(wallTreatedList, pointMap, edgeMap,
                        normalFloor);

                // If it is a wall, we add it to the wall list, otherwise to the
                // roof list (trivial...).
                if (wallTreatedList.contains(surface)) {
                    this.walls.add(p);
                } else {
                    this.roofs.add(p);
                }

                counterWellTreated++;

            } catch (ImpossibleNeighboursOrderException e) {
                // If there is a problem, we cannot continue the treatment.
                // LOOK : maybe return the unsorted bounds to have a little
                // result.
                counterNotTreated++;

            } catch (InvalidSurfaceException e) {
                // If there is a problem, we cannot continue the treatment.
                // LOOK : maybe return the unsorted bounds to have a little
                // result.
                counterNotTreated++;
            }
        }

        // FIXME : logger.
        System.out.println("Parts correctly treated : " + counterWellTreated);
        System.out.println("Parts not treated : " + counterNotTreated);
    }

    /**
     * Writes the walls and roofs in STL files. Used for debugging.
     * 
     * @param directoryName
     *            the directory to write in
     */
    public final void writeSTL(final String directoryName) {
        this.writeSTLWalls(directoryName);
        this.writeSTLRoofs(directoryName);
    }

    /**
     * Writes the roofs in STL files. Used for debugging.
     * 
     * @param directoryName
     *            the directory to write in
     */
    private final void writeSTLRoofs(final String directoryName) {
        int counterRoof = 0;
        for (Polyline p : this.roofs) {
            try {
                p.returnCentroidMesh().writeSTL(
                    directoryName + "computedRoof" + counterRoof
                        + FilesNames.EXTENSION);
                counterRoof = counterRoof + 1;
            } catch (EmptyPolylineException e) {
                System.out.println("Empty polyline : care !");
            }
        }
    }

    /**
     * Writes the walls in STL files. Used for debugging.
     * 
     * @param directoryName
     *            the directory to write in
     */
    private final void writeSTLWalls(final String directoryName) {
        int counterWall = 0;
        for (Polyline p : this.walls) {
            try {
                p.returnCentroidMesh().writeSTL(
                    directoryName + "computedWall" + counterWall
                        + FilesNames.EXTENSION);
                counterWall = counterWall + 1;
            } catch (EmptyPolylineException e) {
                System.out.println("Empty polyline : care !");
            }
        }
    }

    /**
     * Determinates the neighbours of each meshes. Two meshes are neighbours if
     * they share an edge.
     * 
     * @param wallTreatedList
     *            the list of walls as meshes
     * @param roofTreatedList
     *            the list of roofs as meshes
     * @param floors
     *            the floors as one mesh
     * @param noise
     */
    private void determinateNeighbours(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList, final Surface floors) {

        final List<Polyline> wallsBoundsList = new ArrayList<Polyline>();
        final List<Polyline> roofsBoundsList = new ArrayList<Polyline>();

        final Polyline floorsBounds = floors.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallTreatedList);
        wholeList.addAll(roofTreatedList);

        // LOOK : maybe clear those neighbours after the treatNewNeighbours,
        // because they still stay in memory otherwise...

        // First we clear the neighbours.
        for (Surface s : wholeList) {
            s.getNeighbours().clear();
        }

        // We compute the bounds to check if they share a common edge.
        for (Mesh w : wallTreatedList) {
            wallsBoundsList.add(w.returnUnsortedBounds());
        }
        for (Mesh r : roofTreatedList) {
            roofsBoundsList.add(r.returnUnsortedBounds());
        }

        final List<Polyline> wholeBoundsList = new ArrayList<Polyline>();
        wholeBoundsList.addAll(wallsBoundsList);
        wholeBoundsList.addAll(roofsBoundsList);

        // Then we check every edge of the bounds to see if some are shared by
        // two meshes. If they do, they are neighbours.
        for (int i = 0; i < wholeBoundsList.size(); i = i + 1) {
            final Polyline polyline1 = wholeBoundsList.get(i);
            // TODO : maybe go from j = i + 1 only ?
            for (int j = 0; j < wholeBoundsList.size(); j = j + 1) {
                final Polyline polyline2 = wholeBoundsList.get(j);
                if (polyline1.isNeighbour(polyline2)) {
                    wholeList.get(i).addNeighbour(wholeList.get(j));
                }
            }
            // TODO? Maybe no need to check if roofs are neighbours of floor.
            if (polyline1.isNeighbour(floorsBounds)) {
                wholeList.get(i).addNeighbour(floors);
            }
        }
    }

    /**
     * Determinates the neighbours of each meshes. Two meshes are neighbours if
     * they share an edge.
     * 
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param floors
     *            the floors as one mesh
     * @param noise
     */
    private void searchForNeighbours(final List<Surface> wallList,
        final List<Surface> roofList, final Surface floors, Mesh noise) {

        final Polyline floorsBounds = floors.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        // First we clear the neighbours.
        for (Surface m : wholeList) {
            m.getNeighbours().clear();
        }

        // FIXME : try this thing...
        // To find every neighbours, we complete every holes between roofs
        // and walls by adding all the noise.
        // List<Mesh> listFakesWalls = new ArrayList<Mesh>();
        // List<Mesh> listFakesRoofs = new ArrayList<Mesh>();
        //
        // for (Mesh m : wallList) {
        // Mesh fakeM = new Mesh(m);
        // listFakesWalls.add(fakeM);
        // }
        // for (Mesh m : roofList) {
        // Mesh fakeM = new Mesh(m);
        // listFakesRoofs.add(fakeM);
        // }
        // try {
        // // Add the neighbour noise to the walls.
        // Algos.blockTreatNoise(listFakesWalls, noise);
        //
        // // Add the neighbour noise to the roofs.
        // Algos.blockTreatNoise(listFakesRoofs, noise);
        //
        // } catch (MoreThanTwoTrianglesPerEdgeException e) {
        // e.printStackTrace();
        // }

        final List<Polyline> wallsBoundsList = new ArrayList<Polyline>();
        final List<Polyline> roofsBoundsList = new ArrayList<Polyline>();

        // We compute the bounds to check if they share a common edge.
        for (Mesh w : wallList) {
            wallsBoundsList.add(w.returnUnsortedBounds());
        }
        for (Mesh r : roofList) {
            roofsBoundsList.add(r.returnUnsortedBounds());
        }

        final List<Polyline> wholeBoundsList = new ArrayList<Polyline>();
        wholeBoundsList.addAll(wallsBoundsList);
        wholeBoundsList.addAll(roofsBoundsList);

        // Then we check every edge of the bounds to see if some are shared by
        // two meshes. If they do, they are neighbours.
        for (int i = 0; i < wholeBoundsList.size(); i = i + 1) {
            final Polyline polyline1 = wholeBoundsList.get(i);

            for (int j = i + 1; j < wholeBoundsList.size(); j = j + 1) {
                final Polyline polyline2 = wholeBoundsList.get(j);
                if (polyline1.isNeighbour(polyline2)) {
                    wholeList.get(i).addNeighbour(wholeList.get(j));
                }
            }

            if (polyline1.isNeighbour(floorsBounds)) {
                wholeList.get(i).addNeighbour(floors);
            }
        }
    }

    /**
     * Cut the rest (after the extraction of the walls) of the mesh in roofs
     * considering the orientation of the triangles.
     * 
     * @param building
     *            the building to carve
     * @param normalFloor
     *            the normal to the floor
     * @param wholeRoof
     *            the whole roof
     * @param noise
     *            the noise as mesh
     * @return the list of roofs
     */
    private List<Mesh> sortRoofs(final Mesh building,
        final Vector3d normalFloor, final Mesh noise) {
        final List<Mesh> roofList = new ArrayList<Mesh>();

        // Cut the mesh in parts, considering their orientation.
        try {

            final List<Mesh> thingsList =
                Algos.blockOrientedAndPlaneExtract(building,
                    SeparationTreatmentWallsRoofs.ROOF_ANGLE_ERROR);

            // Considering their size and their orientation, sort the blocks in
            // roofs or noise.
            for (Mesh e : thingsList) {

                if ((e.size() >= SeparationTreatmentWallsRoofs.ROOF_SIZE_ERROR)
                    && (e.averageNormal().dot(normalFloor) > 0)) {
                    roofList.add(e);

                } else {
                    noise.addAll(e);
                }
            }
        } catch (MoreThanTwoTrianglesPerEdgeException e1) {
            e1.printStackTrace();
        }
        return roofList;
    }

    /**
     * Cut the normal-to-the-floor mesh in walls considering the orientation of
     * the triangles.
     * 
     * @param building
     *            the building to carve
     * @param normalFloor
     *            the normal to the floor
     * @param wholeWall
     *            the whole wall
     * @param noise
     *            the noise as mesh
     * @return the list of walls
     */
    private List<Mesh> sortWalls(final Mesh building,
        final Vector3d normalFloor, final Mesh noise) {
        final List<Mesh> wallList = new ArrayList<Mesh>();

        // Select the triangles which are oriented normal to normalFloor.
        final Mesh wallOriented =
            building.orientedNormalTo(normalFloor,
                SeparationTreatmentWallsRoofs.NORMALTO_ERROR);

        // Cut the mesh in parts, considering their orientation.
        try {
            final List<Mesh> thingsList =
                Algos.blockOrientedAndPlaneExtract(wallOriented,
                    SeparationTreatmentWallsRoofs.WALL_ANGLE_ERROR);

            // Considering their size, sort the blocks in walls or noise.
            for (Mesh e : thingsList) {
                building.remove(e);
                if (e.size() >= SeparationTreatmentWallsRoofs.WALL_SIZE_ERROR) {
                    wallList.add(e);
                } else {
                    noise.addAll(e);
                }
            }
        } catch (MoreThanTwoTrianglesPerEdgeException e1) {
            e1.printStackTrace();
        }
        return wallList;
    }

    // FIXME : improve the velocity of this method.
    // Needs to call determinateNeighbours first...
    private void treatNewNeighbours(final List<Surface> wallList,
        final List<Surface> roofList) {

        // After the noise addition, if some of the walls or some of the roofs
        // are now neighbours (they share an edge) and have the same
        // orientation, then they are added to form only one wall or roof.

        // Wall is prioritary : it means that if a roof touch a wall, this roof
        // is added to the wall, and not the inverse.

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        for (int i = 0; i < wholeList.size(); i = i + 1) {
            final Surface surface = wholeList.get(i);

            final List<Surface> oriented = new ArrayList<Surface>();
            final List<Surface> ret = new ArrayList<Surface>();

            for (Surface m : wholeList) {
                if (m.isOrientedAs(surface,
                    SeparationTreatmentWallsRoofs.MIDDLE_ANGLE_ERROR)) {
                    oriented.add(m);
                }
            }

            surface.returnNeighbours(ret, oriented);

            for (Surface m : ret) {
                if (m != surface) {
                    surface.addAll(m);
                    wholeList.remove(m);
                    wallList.remove(m);
                    roofList.remove(m);
                }
            }
        }
    }

    /**
     * Add the noise to the surface which is its neighbour, and which has the
     * same orientation. The error orientation is determined by the
     * LARGE_ANGLE_ERROR.
     * 
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param noise
     *            the mesh containing the noise
     */
    private void treatNoise(final List<Mesh> wallList,
        final List<Mesh> roofList, final Mesh noise, final Mesh floors) {

        try {
            // Add the oriented and neighbour noise to the walls.
            Algos.blockTreatOrientedNoise(wallList, noise,
                SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

            // Add the oriented and neighbour noise to the roofs.
            Algos.blockTreatOrientedNoise(roofList, noise,
                SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

            // FIXME : if some roofs are neighbours to the floors, remove
            // them...
            // because it's noise. TODO ? Because if they are not noise ?
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            // TODO : this exception shouldn't happen. Maybe remove this try
            // catch.
            e.printStackTrace();
        }
    }

    /**
     * @param wallList
     * @param roofList
     * @param wallTreatedList
     * @param roofTreatedList
     */
    // TODO ; change this name !
    private void vectorizeSurfaces(List<Mesh> wallList, List<Mesh> roofList,
        List<Surface> wallTreatedList, List<Surface> roofTreatedList) {

        for (Mesh m : wallList) {
            wallTreatedList.add(new Surface(m));
        }
        for (Mesh m : roofList) {
            roofTreatedList.add(new Surface(m));
        }
    }
}
