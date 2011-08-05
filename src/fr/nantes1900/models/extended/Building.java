package fr.nantes1900.models.extended;

import fr.nantes1900.constants.SeparationTreatmentWallsRoofs;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Mesh.InvalidSurfaceException;
import fr.nantes1900.models.Polyline;
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
 * @author Daniel Lefevre
 */
public class Building {

    /**
     * Returns the intersection of two lists of meshes.
     * @param list1
     *            the first list of meshes
     * @param list2
     *            the second list of meshes
     * @return a list containing the elements shared by the two lists
     */
    private static List<Mesh> listIntersection(
        final List<Mesh> list1, final List<Mesh> list2) {
        final List<Mesh> ret = new ArrayList<Mesh>();
        for (Mesh mesh : list1) {
            if (list2.contains(mesh)) {
                ret.add(mesh);
            }
        }
        return ret;
    }

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
     * Determinate the neighbours of each meshes. Two meshes are neighbours if
     * they share an edge.
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param floors
     *            the floors as one mesh
     */
    private void determinateNeighbours(
        final List<Mesh> wallList,
        final List<Mesh> roofList, final Mesh floors) {

        final List<Polyline> wallsBoundsList = new ArrayList<Polyline>();
        final List<Polyline> roofsBoundsList = new ArrayList<Polyline>();
        final Polyline floorsBounds = floors
            .returnUnsortedBounds();
        final List<Polyline> wholeBoundsList = new ArrayList<Polyline>(
            wallsBoundsList);
        wholeBoundsList.addAll(roofsBoundsList);
        final List<Mesh> wholeList = new ArrayList<Mesh>(
            wallList);
        wholeList.addAll(roofList);
        for (Mesh w : wallList) {
            wallsBoundsList.add(w.returnUnsortedBounds());
        }
        for (Mesh r : roofList) {
            roofsBoundsList.add(r.returnUnsortedBounds());
        }
        for (int i = 0; i < wholeBoundsList.size(); i = i + 1) {
            final Polyline polyline1 = wholeBoundsList.get(i);
            for (int j = 0; j < wholeBoundsList.size(); j = j + 1) {
                final Polyline polyline2 = wholeBoundsList.get(j);
                if (polyline1.isNeighbour(polyline2)) {
                    wholeList.get(i).addNeighbour(
                        wholeList.get(j));
                }
            }
            // TODO? Maybe no need to check if roofs are neighbours of floor.
            if (polyline1.isNeighbour(floorsBounds)) {
                wallList.get(i).addNeighbour(floors);
            }
        }

        // Verification after the first search. If the neighbours of one surface
        // are not 2 per 2 neighbours each other, then correct it.
        for (Mesh mesh : wholeList) {
            final List<Mesh> list = new ArrayList<Mesh>();
            for (Mesh surface : mesh.getNeighbours()) {
                if (Building.listIntersection(
                    mesh.getNeighbours(),
                    surface.getNeighbours()).size() == 1) {
                    list.add(surface);
                }
            }
            // S'il y a deux surfaces qui n'ont pas 2 voisins chacune, alors ces
            // deux-là doivent forcément être voisines. On les ajoute donc.
            if (list.size() == 2) {
                list.get(0).addNeighbour(list.get(1));
                System.out.println("Case well treated !");
            } else if (list.size() == 1) {
                System.out.println("Error !");
            } else if (list.size() == 0) {
                System.out.println("There was no problem !");
            } else {
                System.out
                    .println("Impossible to treat for now !");
            }
        }
    }

    /**
     * Build the polyline. With all the neighbours, build the polyline by
     * computing the common edges between them, and put them into the list of
     * polylines walls and roofs.
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshe
     * @param normalFloor
     *            the normal to the floor
     */
    private void findCommonEdges(final List<Mesh> wallList,
        final List<Mesh> roofList,
        final Vector3d normalFloor) {

        final Map<Point, Point> pointMap = new HashMap<Point, Point>();
        final Map<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

        int counterError = 0;

        for (Mesh m : roofList) {
            Polyline p;

            try {
                p = m.findEdges(wallList, pointMap, edgeMap,
                    normalFloor);
                if (!p.isEmpty()) {
                    this.roofs.add(p);
                } else {
                    counterError++;
                }
            } catch (InvalidSurfaceException e) {
                // If this exception is catched, do not treat the surface.
                // FIXME
                counterError++;
            }
        }

        for (Mesh m : wallList) {
            Polyline p;

            try {
                p = m.findEdges(wallList, pointMap, edgeMap,
                    normalFloor);
                if (!p.isEmpty()) {
                    walls.add(p);
                } else {
                    counterError++;
                }
            } catch (InvalidSurfaceException e) {
                // If this exception is catched, do not treat the surface.
                // FIXME
                counterError++;
            }
        }
        System.out.println("Nombre de surfaces non traitées : " +
            counterError);
    }

    /**
     * Cut the rest of the mesh in roofs considering the orientation of the
     * triangles.
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
    private ArrayList<Mesh> sortRoofs(final Mesh building,
        final Vector3d normalFloor, final Mesh wholeRoof,
        final Mesh noise) {
        ArrayList<Mesh> roofList = new ArrayList<Mesh>();
        // Cut the mesh in parts, considering their orientation.
        ArrayList<Mesh> thingsList;
        try {
            thingsList = Algos
                .blockOrientedExtract(
                    building,
                    SeparationTreatmentWallsRoofs.ANGLE_ROOF_ERROR);
            // Compute the average of triangles per block (roof)
            int size = 0;
            for (Mesh e : thingsList) {
                size += e.size();
            }
            // Considering their size and their orientation, sort the blocks in
            // roofs or noise.
            for (Mesh e : thingsList) {

                if ((e.size() >= SeparationTreatmentWallsRoofs.ROOF_SIZE_ERROR
                    * (double) size
                    / (double) thingsList.size())
                    && (e.averageNormal().dot(normalFloor) > 0)) {
                    roofList.add(e);

                } else {
                    noise.addAll(e);
                }
            }
            // TODO put it after
            // Add the roofs to the wholeRoof mesh.
            for (Mesh r : roofList) {
                wholeRoof.addAll(r);
            }
        } catch (MoreThanTwoTrianglesPerEdgeException e1) {
            e1.printStackTrace();
        }
        return roofList;
    }

    /**
     * Cut the normal-to-the-floor mesh in walls considering the orientation of
     * the triangles.
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
    private ArrayList<Mesh> sortWalls(final Mesh building,
        final Vector3d normalFloor, final Mesh wholeWall, final Mesh noise) {
        ArrayList<Mesh> wallList = new ArrayList<Mesh>();
        // Select the triangles which are oriented normal to normalFloor.
        Mesh wallOriented = building.orientedNormalTo(
            normalFloor,
            SeparationTreatmentWallsRoofs.NORMALTO_ERROR);
        // Cut the mesh in parts, considering their orientation.
        ArrayList<Mesh> thingsList;
        try {
            thingsList = Algos
                .blockOrientedExtract(
                    wallOriented,
                    SeparationTreatmentWallsRoofs.ANGLE_WALL_ERROR);
            // Compute the average of triangles per block (wall)
            int size = 0;
            for (Mesh e : thingsList) {
                building.remove(e);
                size += e.size();
            }
            // Considering their size, sort the blocks in walls or noise.
            for (Mesh e : thingsList) {
                if (e.size() >= SeparationTreatmentWallsRoofs.WALL_SIZE_ERROR
                    * (double) size
                    / (double) thingsList.size()) {
                    wallList.add(e);
                } else {
                    noise.addAll(e);
                }
            }
            // TODO put it after
            // Add the walls to the wholeWall mesh.
            for (Mesh w : wallList) {
                wholeWall.addAll(w);
            }
        } catch (MoreThanTwoTrianglesPerEdgeException e1) {
            e1.printStackTrace();
        }
        return wallList;
    }

    /**
     * Add the noise to the surface which is its neighbour, and which has the
     * same orientation. The error orientation is determined by the
     * LARGE_ANGLE_ERROR.
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param noise
     *            the mesh containing the noise
     */
    private void treatNoise(final ArrayList<Mesh> wallList,
        final ArrayList<Mesh> roofList, final Mesh noise) {
        // Add the oriented and neighbour noise to the walls.
        try {
            Algos.blockTreatOrientedNoise(
                wallList,
                noise,
                SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);
            // Add the oriented and neighbour noise to the roofs.
            Algos.blockTreatOrientedNoise(
                roofList,
                noise,
                SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            e.printStackTrace();
        }
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
     * @param roof
     *            the roof to add
     */
    public final void addRoof(final Polyline roof) {
        this.roofs.add(roof);
    }

    /**
     * Add a list of roofs to the attribute list of roofs.
     * @param addRoofs
     *            the list of roofs to add
     */
    public final void addRoofs(final List<Polyline> addRoofs) {
        this.roofs.addAll(addRoofs);
    }

    /**
     * Add a wall to the attribute list of walls.
     * @param wall
     *            the wall to add
     */
    public final void addWall(final Polyline wall) {
        this.walls.add(wall);
    }

    /**
     * Add a list of walls to the attribute list of walls.
     * @param addWalls
     *            the wall to add
     */
    public final void addWalls(final List<Polyline> addWalls) {
        this.walls.addAll(addWalls);
    }

    /**
     * Build the building from a mesh, by computingthe algorithms.
     * @param building
     *            the mesh to compute
     * @param normalFloor
     *            the normal to the floor
     * @param floors
     *            the mesh containing all the floors
     */
    public final void buildFromMesh(final Mesh building,
        final Mesh floors, final Vector3d normalFloor) {

        building.writeSTL("Tests/St-Similien/m01/results/building.stl");

        Mesh wholeWall = new Mesh();
        Mesh wholeRoof = new Mesh();
        Mesh noise = new Mesh();

        ArrayList<Mesh> wallList = this.sortWalls(building,
            normalFloor, wholeWall, noise);
        ArrayList<Mesh> roofList = this.sortRoofs(building,
            normalFloor, wholeRoof, noise);

        this.treatNoise(wallList, roofList, noise);
        // FIXME : if some roofs are neighbours to the floors, remove them...
        // because it's noise. TODO ? Because if they are not noise ?

        // Sum all the walls to build the wholeWall.
        int counter = 0;
        for (Mesh w : wallList) {
            wholeWall.addAll(w);
            w.writeSTL("Tests/St-Similien/m01/results/wall - "
                + counter + ".stl");
            counter++;
        }
        // Sum all the roofs to build the wholeRoof.
        counter = 0;
        for (Mesh r : roofList) {
            wholeRoof.addAll(r);
            r.writeSTL("Tests/St-Similien/m01/results/roof - "
                + counter + ".stl");
            counter++;
        }
        this.determinateNeighbours(wallList, roofList, floors);
        this.findCommonEdges(wallList, roofList, normalFloor);

        this.writeSTL("Tests/St-Similien/m01/results/");
    }

    /**
     * Getter.
     * @return the list of roofs
     */
    public final List<Polyline> getRoofs() {
        return this.roofs;
    }

    /**
     * Getter.
     * @return the list of walls
     */
    public final List<Polyline> getWalls() {
        return this.walls;
    }

    /**
     * Write the building in STL files. Use for debugging.
     * @param directoryName
     *            the directory name to write in
     */
    public final void writeSTL(final String directoryName) {
        this.writeSTLWalls(directoryName);
        this.writeSTLRoofs(directoryName);
    }

    /**
     * Write the roofs in STL files. Use for debugging.
     * @param directoryName
     *            the directory name to write in
     */
    public final void writeSTLRoofs(final String directoryName) {
        int counterRoof = 0;
        for (Polyline p : this.roofs) {
            p.returnCentroidMesh().writeSTL(
                directoryName + "computedRoof" + counterRoof
                    + ".stl");
            counterRoof++;
        }
    }

    /**
     * Write the walls in STL files. Use for debugging.
     * @param directoryName
     *            the directory name to write in
     */
    public final void writeSTLWalls(final String directoryName) {
        int counterWall = 0;
        for (Polyline p : this.walls) {
            p.returnCentroidMesh().writeSTL(
                directoryName + "computedWall" + counterWall
                    + ".stl");
            counterWall++;
        }
    }
}
