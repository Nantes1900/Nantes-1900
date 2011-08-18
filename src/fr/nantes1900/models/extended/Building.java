package fr.nantes1900.models.extended;

import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.constants.SeparationTreatmentWallsRoofs;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.Polyline.EmptyPolylineException;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.Surface.InvalidSurfaceException;
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
     * Returns the intersection of two lists of meshes.
     * 
     * @param list
     *            the first list of meshes
     * @param list2
     *            the second list of meshes
     * @return a list containing the elements shared by the two lists
     */
    // FIXME : put that i don't know where but not here...
    public static List<Mesh> listIntersection(final List<Surface> list,
        final List<Surface> list2) {
        final List<Mesh> ret = new ArrayList<Mesh>();
        for (Mesh mesh : list) {
            if (list2.contains(mesh)) {
                ret.add(mesh);
            }
        }
        return ret;
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
        this.findCommonEdges(wallTreatedList, roofTreatedList, normalFloor);

        this.writeSTL("files/St-Similien/m02/results/");

        // FIXME
        System.exit(1);
    }

    /**
     * @param wallList
     * @param roofList
     * @param wallTreatedList
     * @param roofTreatedList
     */
    private void vectorizeSurfaces(List<Mesh> wallList, List<Mesh> roofList,
        List<Surface> wallTreatedList, List<Surface> roofTreatedList) {

        for (Mesh m : wallList) {
            wallTreatedList.add(new Surface(m));
        }
        for (Mesh m : roofList) {
            roofTreatedList.add(new Surface(m));
        }
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

    /**
     * Write the building in STL files. Use for debugging.
     * 
     * @param directoryName
     *            the directory name to write in
     */
    public final void writeSTL(final String directoryName) {
        this.writeSTLWalls(directoryName);
        this.writeSTLRoofs(directoryName);
    }

    /**
     * Write the roofs in STL files. Use for debugging.
     * 
     * @param directoryName
     *            the directory name to write in
     */
    public final void writeSTLRoofs(final String directoryName) {
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
     * Write the walls in STL files. Use for debugging.
     * 
     * @param directoryName
     *            the directory name to write in
     */
    public final void writeSTLWalls(final String directoryName) {
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
     * Determinate the neighbours of each meshes. Two meshes are neighbours if
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

        // FIXME
        // // First we clear the neighbours.
        // for (Mesh m : wholeList) {
        // m.getNeighbours().clear();
        // }

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
     * Determinate the neighbours of each meshes. Two meshes are neighbours if
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
    private void searchForNeighbours(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList, final Surface floors, Mesh noise) {

        final List<Polyline> wallsBoundsList = new ArrayList<Polyline>();
        final List<Polyline> roofsBoundsList = new ArrayList<Polyline>();

        final Polyline floorsBounds = floors.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallTreatedList);
        wholeList.addAll(roofTreatedList);

        // First we clear the neighbours.
        for (Surface m : wholeList) {
            m.getNeighbours().clear();
        }

        // FIXME :
        // To find every neighbours, we complete every holes between roofs
        // and walls by adding noise.
        List<Mesh> listFakesWalls = new ArrayList<Mesh>();
        List<Mesh> listFakesRoofs = new ArrayList<Mesh>();

        for (Mesh m : wallTreatedList) {
            Mesh fakeM = new Mesh(m);
            listFakesWalls.add(fakeM);
        }
        for (Mesh m : roofTreatedList) {
            Mesh fakeM = new Mesh(m);
            listFakesRoofs.add(fakeM);
        }
        try {
            // Add the neighbour noise to the walls.
            Algos.blockTreatNoise(listFakesWalls, noise);

            // Add the neighbour noise to the roofs.
            Algos.blockTreatNoise(listFakesRoofs, noise);

        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            e.printStackTrace();
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
     * Build the polyline. With all the neighbours, build the polyline by
     * computing the common edges between them, and put them into the list of
     * polylines walls and roofs.
     * 
     * @param wallTreatedList
     *            the list of walls as meshes
     * @param roofTreatedList
     *            the list of roofs as meshe
     * @param normalFloor
     *            the normal to the floor
     */
    private void findCommonEdges(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList, final Vector3d normalFloor) {

        final Map<Point, Point> pointMap = new HashMap<Point, Point>();
        final Map<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

        int counterError = 0;

        for (Surface s : roofTreatedList) {
            Polyline p;

            try {
                p =
                    s.findEdges(wallTreatedList, pointMap, edgeMap, normalFloor);
                this.roofs.add(p);
            } catch (InvalidSurfaceException e) {
                // If this exception is catched, do not treat the surface.
                // FIXME
                ++counterError;
            }
        }

        for (Surface s : wallTreatedList) {
            Polyline p;

            try {
                p =
                    s.findEdges(wallTreatedList, pointMap, edgeMap, normalFloor);
                this.walls.add(p);
            } catch (InvalidSurfaceException e) {
                // If this exception is catched, do not treat the surface.
                // FIXME
                ++counterError;
            }
        }
        // FIXME : logger.
        System.out.println("Nombre de surfaces non traitées : " + counterError);
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
}
