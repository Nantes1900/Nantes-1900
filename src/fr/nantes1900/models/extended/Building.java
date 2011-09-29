package fr.nantes1900.models.extended;

import fr.nantes1900.constants.Configuration;
import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.constants.SeparationTreatmentWallsRoofs;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.Surface.ImpossibleNeighboursOrderException;
import fr.nantes1900.models.Surface.InvalidSurfaceException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.utils.Algos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector3d;

/**
 * Implements a building as two lists of surfaces : walls and roofs.
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
     * Constructor. Creates the lists of walls and roofs.
     */
    public Building() {
    }

    /**
     * Adds a roof to the attribute list of roofs.
     * 
     * @param roof
     *            the roof to add
     */
    public final void addRoof(final Polyline roof) {
        this.roofs.add(roof);
    }

    /**
     * Adds a list of roofs to the attribute list of roofs.
     * 
     * @param addRoofs
     *            the list of roofs to add
     */
    public final void addRoofs(final List<Polyline> addRoofs) {
        this.roofs.addAll(addRoofs);
    }

    /**
     * Adds a wall to the attribute list of walls.
     * 
     * @param wall
     *            the wall to add
     */
    public final void addWall(final Polyline wall) {
        this.walls.add(wall);
    }

    /**
     * Adds a list of walls to the attribute list of walls.
     * 
     * @param addWalls
     *            the wall to add
     */
    public final void addWalls(final List<Polyline> addWalls) {
        this.walls.addAll(addWalls);
    }

    /**
     * Builds the building from a mesh, by computing the algorithms. Sort first
     * the walls, then the roofs, and treatNoise. It then converts the meshes to
     * surfaces, and search and treat the new neighbours. After that, it
     * determinates neighbours, sort the uncomputable surfaces, and finally
     * order the neighbours and compute the edges.
     * 
     * @param building
     *            the mesh to compute
     * @param normalGround
     *            the normal to the ground
     * @param grounds
     *            the mesh containing all the grounds
     * @param directoryName
     *            the name of the residentials directory
     * @param counter
     *            the number of the current building
     */
    public final void buildFromMesh(final Mesh building, final Mesh grounds,
        final Vector3d normalGround, final String directoryName,
        final int counterZone, final int counterBuilding) {

        if (Town.stepByStep) {
            building
                .writeSTL(directoryName + FilesNames.TEMPORARY_DIRECTORY
                    + FilesNames.RESIDENTIAL_FILENAME + FilesNames.SEPARATOR
                    + counterZone + FilesNames.SEPARATOR
                    + FilesNames.BUILDING_NAME + FilesNames.SEPARATOR
                    + counterBuilding + FilesNames.EXTENSION);
        }

        final Surface groundsTreated = new Surface(grounds);
        List<Surface> wallList;
        List<Surface> roofList;
        Mesh noise;
        Mesh tempBuilding;

        do {
            tempBuilding = new Mesh(building);
            
            // Loads the new coefficients from the config file.
            Configuration.loadCoefficients();

            if (Town.stepByStep) {
                // Removes the files if they exist, to avoid confusion between
                // old and new files.
                int counterWall = 1;
                while (new File(directoryName + FilesNames.TEMPORARY_DIRECTORY
                    + FilesNames.RESIDENTIAL_FILENAME + FilesNames.SEPARATOR
                    + counterZone + FilesNames.SEPARATOR
                    + FilesNames.BUILDING_NAME + FilesNames.SEPARATOR
                    + counterBuilding + FilesNames.SEPARATOR
                    + FilesNames.WALL_NAME + FilesNames.SEPARATOR + counterWall
                    + FilesNames.EXTENSION).exists()) {
                    new File(directoryName + FilesNames.TEMPORARY_DIRECTORY
                        + FilesNames.RESIDENTIAL_FILENAME
                        + FilesNames.SEPARATOR + counterZone
                        + FilesNames.SEPARATOR + FilesNames.BUILDING_NAME
                        + FilesNames.SEPARATOR + counterBuilding
                        + FilesNames.SEPARATOR + FilesNames.WALL_NAME
                        + FilesNames.SEPARATOR + counterWall
                        + FilesNames.EXTENSION).delete();
                    counterWall++;
                }
                int counterRoof = 1;
                while (new File(directoryName + FilesNames.TEMPORARY_DIRECTORY
                    + FilesNames.RESIDENTIAL_FILENAME + FilesNames.SEPARATOR
                    + counterZone + FilesNames.SEPARATOR
                    + FilesNames.BUILDING_NAME + FilesNames.SEPARATOR
                    + counterBuilding + FilesNames.SEPARATOR
                    + FilesNames.ROOF_NAME + FilesNames.SEPARATOR + counterRoof
                    + FilesNames.EXTENSION).exists()) {
                    new File(directoryName + FilesNames.TEMPORARY_DIRECTORY
                        + FilesNames.RESIDENTIAL_FILENAME
                        + FilesNames.SEPARATOR + counterZone
                        + FilesNames.SEPARATOR + FilesNames.BUILDING_NAME
                        + FilesNames.SEPARATOR + counterBuilding
                        + FilesNames.SEPARATOR + FilesNames.ROOF_NAME
                        + FilesNames.SEPARATOR + counterRoof
                        + FilesNames.EXTENSION).delete();
                    counterRoof++;
                }
            }

            // Creates a new mesh.
            noise = new Mesh();

            // Applies the first algorithms : extract the walls, and after this,
            // extract the roofs.
            wallList = Building.sortWalls(tempBuilding, normalGround, noise);
            roofList = Building.sortRoofs(tempBuilding, normalGround, noise);

            // Treats the noise.
            Building.treatNoise(wallList, roofList, noise, groundsTreated);

            // Finds the neighbours, searching deeply (add noise to the walls
            // and roofs to add every edge to one of these surface. Then find
            // the surfaces which share one edge, and treat the neighbours.
            Building.treatNewNeighbours(wallList, roofList, groundsTreated);

            if (Town.stepByStep) {
                System.out
                    .println("2nd step executed : you will find the result in : "
                        + directoryName
                        + FilesNames.TEMPORARY_DIRECTORY
                        + FilesNames.RESIDENTIAL_FILENAME
                        + FilesNames.SEPARATOR
                        + counterZone
                        + FilesNames.SEPARATOR
                        + FilesNames.BUILDING_NAME
                        + FilesNames.SEPARATOR + counterBuilding);

                int counterWall = 1;
                for (final Mesh m : wallList) {
                    m.writeSTL(directoryName + FilesNames.TEMPORARY_DIRECTORY
                        + FilesNames.RESIDENTIAL_FILENAME
                        + FilesNames.SEPARATOR + counterZone
                        + FilesNames.SEPARATOR + FilesNames.BUILDING_NAME
                        + FilesNames.SEPARATOR + counterBuilding
                        + FilesNames.SEPARATOR + FilesNames.WALL_NAME
                        + FilesNames.SEPARATOR + counterWall
                        + FilesNames.EXTENSION);
                    counterWall++;
                }
                int counterRoof = 1;
                for (final Mesh m : roofList) {
                    m.writeSTL(directoryName + FilesNames.TEMPORARY_DIRECTORY
                        + FilesNames.RESIDENTIAL_FILENAME
                        + FilesNames.SEPARATOR + counterZone
                        + FilesNames.SEPARATOR + FilesNames.BUILDING_NAME
                        + FilesNames.SEPARATOR + counterBuilding
                        + FilesNames.SEPARATOR + FilesNames.ROOF_NAME
                        + FilesNames.SEPARATOR + counterRoof
                        + FilesNames.EXTENSION);
                    counterRoof++;
                }
            }
        } while (Town.stepByStep && !Town.askForAnswer());

        do {
            if (Town.stepByStep) {
                // Loads the new coefficients from the config file.
                Configuration.loadCoefficients();

                this.walls.clear();
                this.roofs.clear();
            }

            // From all the neighbours, computes the wrap line and returns the
            // surfaces as polylines.
            this.findEdgesFromNeighbours(wallList, roofList, normalGround,
                groundsTreated, noise);

            if (Town.stepByStep) {
                System.out
                    .println("3rd step executed : you will find the result in : "
                        + directoryName
                        + FilesNames.TEMPORARY_DIRECTORY
                        + FilesNames.RESIDENTIAL_FILENAME
                        + FilesNames.SEPARATOR
                        + counterZone
                        + FilesNames.SEPARATOR
                        + FilesNames.BUILDING_NAME
                        + FilesNames.SEPARATOR + counterBuilding);

                this.writeSTL(directoryName + FilesNames.TEMPORARY_DIRECTORY
                    + FilesNames.RESIDENTIAL_FILENAME + FilesNames.SEPARATOR
                    + counterZone + FilesNames.SEPARATOR
                    + FilesNames.BUILDING_NAME + FilesNames.SEPARATOR
                    + counterBuilding);
            }
        } while (Town.stepByStep && !Town.askForAnswer());

        // Recompute the bounds between the grounds and the buildings, to avoid
        // holes.
        // this.reComputeBounds(grounds);
    }

    /**
     * For each surface (wall or roof), orders the neighbours, and after this,
     * computes the edges. If an exception is throwed by these two methods, the
     * surface is not treated at all.
     * 
     * @param wallList
     *            the list of walls as surfaces
     * @param roofList
     *            the list of roofs as surfaces
     * @param normalGround
     *            the normal to the ground
     * @param grounds
     *            the mesh containing the grounds
     * @param noise
     *            the mesh containing the noise
     */
    public final void findEdgesFromNeighbours(final List<Surface> wallList,
        final List<Surface> roofList, final Vector3d normalGround,
        final Surface grounds, final Mesh noise) {

        // Determinates the neighbours.
        Building.determinateNeighbours(wallList, roofList, grounds, noise);

        // Find all the surface which have 0, or 1, or 2 neigbhours and then
        // cannot be treated.
        Building.sortSurfaces(wallList, roofList);

        // Creates the map where the points and edges will be put : if one point
        // is created a second time, it will be given the same
        // reference as the other one having the same values.
        final Map<Point, Point> pointMap = new HashMap<Point, Point>();

        // Adds all the surfaces
        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        int counterWellTreated = 0;

        for (final Surface surface : wholeList) {
            try {
                // Orders its neighbours in order to treat them.
                // If the neighbours of one surface are not 2 per 2 neighbours
                // each other, then it tries to correct it.
                surface.orderNeighbours(wholeList, grounds);

                // When the neighbours are sorted, finds the intersection of
                // them to find the edges of this surface.
                final Polyline p =
                    surface.findEdges(wallList, pointMap, normalGround);

                // If it is a wall, adds it to the wall list, otherwise to the
                // roof list (obvious...).
                if (wallList.contains(surface)) {
                    this.walls.add(p);
                } else {
                    this.roofs.add(p);
                }

                ++counterWellTreated;

            } catch (final ImpossibleNeighboursOrderException e) {
                // If there is a problem, the treatment cannot continue.
            } catch (final InvalidSurfaceException e) {
                // If there is a problem, we cannot continue the treatment.
            }
        }

        Town.LOG.finest("Surfaces correctly treated : " + counterWellTreated);
        Town.LOG.finest("Surfaces not treated : "
            + (wholeList.size() - counterWellTreated));
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
     * Determinates the neighbours of each meshes. Two meshes are neighbours if
     * they share an edge.
     * 
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param grounds
     *            the grounds as one mesh
     * @param noise
     *            the mesh containing the noise
     */
    private static void determinateNeighbours(final List<Surface> wallList,
        final List<Surface> roofList, final Surface grounds, final Mesh noise) {

        final Polyline groundsBounds = grounds.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        // To find every neighbours, we complete every holes between roofs
        // and walls by adding all the noise.
        final List<Mesh> wholeListFakes = new ArrayList<Mesh>();
        for (final Mesh m : wholeList) {
            final Mesh fake = new Mesh(m);
            wholeListFakes.add(fake);
        }
        Algos.blockTreatPlanedNoise(wholeListFakes, noise,
            SeparationTreatmentWallsRoofs.PLANES_ERROR);

        // First we clear the neighbours.
        for (final Surface s : wholeList) {
            s.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        grounds.getNeighbours().clear();

        // We compute the bounds to check if they share a common edge.
        final List<Polyline> wholeBoundsList = new ArrayList<Polyline>();
        for (final Mesh m : wholeListFakes) {
            wholeBoundsList.add(m.returnUnsortedBounds());
        }

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

            if (polyline1.isNeighbour(groundsBounds)) {
                wholeList.get(i).addNeighbour(grounds);
            }
        }
    }

    /**
     * Computes the grounds to fill holes between grounds and buildings. Not
     * implemented.
     * 
     * @param grounds
     *            the surface containing the grounds
     */
    @SuppressWarnings("unused")
    private void reComputeBounds(final Mesh grounds) {
        // TODO : implement this method.
    }

    /**
     * Determinates the neighbours of each meshes. Two meshes are neighbours if
     * they share an edge.
     * 
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param grounds
     *            the grounds as one mesh
     */
    private static void searchForNeighbours(final List<Surface> wallList,
        final List<Surface> roofList, final Surface grounds) {

        final Polyline groundsBounds = grounds.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        // First we clear the neighbours.
        for (final Surface m : wholeList) {
            m.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        grounds.getNeighbours().clear();

        final List<Polyline> wholeBoundsList = new ArrayList<Polyline>();

        // We compute the bounds to check if they share a common edge.
        for (final Mesh m : wholeList) {
            wholeBoundsList.add(m.returnUnsortedBounds());
        }

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

            if (polyline1.isNeighbour(groundsBounds)) {
                wholeList.get(i).addNeighbour(grounds);
            }
        }
    }

    /**
     * Cuts the rest (after the extraction of the walls) of the mesh in roofs
     * considering the orientation of the triangles.
     * 
     * @param building
     *            the building to carve
     * @param normalGround
     *            the normal to the ground
     * @param noise
     *            the noise as mesh
     * @return the list of roofs
     */
    private static List<Surface> sortRoofs(final Mesh building,
        final Vector3d normalGround, final Mesh noise) {
        final List<Surface> roofList = new ArrayList<Surface>();

        // Cut the mesh in parts, considering their orientation.
        final List<Mesh> thingsList =
            Algos.blockOrientedExtract(building,
                SeparationTreatmentWallsRoofs.ROOF_ANGLE_ERROR);

        // Considering their size and their orientation, sort the blocks in
        // roofs or noise. If a wall is oriented in direction of the ground,
        // it is not keeped.
        for (final Mesh e : thingsList) {
            if ((e.size() >= SeparationTreatmentWallsRoofs.ROOF_SIZE_ERROR)
                && (e.averageNormal().dot(normalGround) > 0)) {
                roofList.add(new Surface(e));
            } else {
                noise.addAll(e);
            }
        }
        return roofList;
    }

    /**
     * Removes the surfaces which have two neighbours or less : they can't be
     * treated. It's isolated surfaces for example.
     * 
     * @param wallList
     *            the list of walls as surfaces
     * @param roofList
     *            the list of roofs as surfaces
     */
    private static void sortSurfaces(final List<Surface> wallList,
        final List<Surface> roofList) {

        int counter = 0;
        for (int i = 0; i < wallList.size(); i++) {
            final Surface s = wallList.get(i);
            if (s.getNeighbours().size() < 3) {
                wallList.remove(s);
                for (final Surface neighbour : s.getNeighbours()) {
                    neighbour.getNeighbours().remove(s);
                }
                counter++;
            }
        }
        for (int i = 0; i < roofList.size(); i++) {
            final Surface s = roofList.get(i);
            if (s.getNeighbours().size() < 3) {
                roofList.remove(s);
                for (final Surface neighbour : s.getNeighbours()) {
                    neighbour.getNeighbours().remove(s);
                }
                counter++;
            }
        }
        Town.LOG.finest(" Isolated surfaces (not treated) : " + counter);
    }

    /**
     * Cuts the normal-to-the-ground mesh in walls considering the orientation
     * of the triangles.
     * 
     * @param building
     *            the building to carve
     * @param normalGround
     *            the normal to the ground
     * @param noise
     *            the noise as mesh
     * @return the list of walls
     */
    private static List<Surface> sortWalls(final Mesh building,
        final Vector3d normalGround, final Mesh noise) {
        final List<Surface> wallList = new ArrayList<Surface>();

        // Select the triangles which are oriented normal to normalGround.
        final Mesh wallOriented =
            building.orientedNormalTo(normalGround,
                SeparationTreatmentWallsRoofs.NORMALTO_ERROR);

        // Cut the mesh in parts, considering their orientation.
        final List<Mesh> thingsList =
            Algos.blockOrientedExtract(wallOriented,
                SeparationTreatmentWallsRoofs.WALL_ANGLE_ERROR);

        // Considering their size, sort the blocks in walls or noise.
        for (final Mesh e : thingsList) {
            building.remove(e);
            if (e.size() >= SeparationTreatmentWallsRoofs.WALL_SIZE_ERROR) {
                wallList.add(new Surface(e));
            } else {
                noise.addAll(e);
            }
        }
        return wallList;
    }

    /**
     * Treats the new neighbours : search for the new neighbours. After this, if
     * some surfaces are neighbours, and have the same orientation, then merges
     * it to form only one surface.
     * 
     * @param wallTreatedList
     *            the list of walls as surfaces
     * @param roofTreatedList
     *            the list of roofs as surfaces
     * @param grounds
     *            the grounds
     */
    private static void treatNewNeighbours(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList, final Surface grounds) {

        Building.searchForNeighbours(wallTreatedList, roofTreatedList, grounds);

        // After the noise addition, if some of the walls or some of the roofs
        // are now neighbours (they share an edge) and have the same
        // orientation, then they are added to form only one wall or roof.

        // Wall is prioritary : it means that if a roof touch a wall, this roof
        // is added to the wall, and not the inverse.

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallTreatedList);
        wholeList.addAll(roofTreatedList);

        for (int i = 0; i < wholeList.size(); i = i + 1) {
            final Surface surface = wholeList.get(i);

            final List<Surface> oriented = new ArrayList<Surface>();
            final List<Surface> ret = new ArrayList<Surface>();

            for (final Surface m : wholeList) {
                if (m.isOrientedAs(surface,
                    SeparationTreatmentWallsRoofs.MIDDLE_ANGLE_ERROR)) {
                    oriented.add(m);
                }
            }

            surface.returnNeighbours(ret, oriented);

            for (final Surface m : ret) {
                if (m != surface) {
                    surface.addAll(m);
                    wholeList.remove(m);
                    wallTreatedList.remove(m);
                    roofTreatedList.remove(m);
                }
            }
        }
    }

    /**
     * Adds the noise to the surface which is its neighbour, and which has the
     * same orientation. The error orientation is determined by the
     * LARGE_ANGLE_ERROR.
     * 
     * @param wallList
     *            the list of walls as meshes
     * @param roofList
     *            the list of roofs as meshes
     * @param noise
     *            the mesh containing the noise
     * @param grounds
     *            the mesh containing the grounds
     */
    private static void treatNoise(final List<Surface> wallList,
        final List<Surface> roofList, final Mesh noise, final Mesh grounds) {

        // Adds the oriented and neighbour noise to the walls.
        Algos.blockTreatOrientedNoise(wallList, noise,
            SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

        // Adds the oriented and neighbour noise to the roofs.
        Algos.blockTreatOrientedNoise(roofList, noise,
            SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);
    }

    /**
     * Writes the roofs in STL files. Used for debugging.
     * 
     * @param fileName
     *            the beginning of the name of the file to write in
     */
    private void writeSTLRoofs(final String fileName) {
        int counterRoof = 1;
        for (final Polyline p : this.roofs) {

            p.returnCentroidMesh()
                .writeSTL(
                    fileName + FilesNames.SEPARATOR + "computedRoof"
                        + FilesNames.SEPARATOR + counterRoof
                        + FilesNames.EXTENSION);
            counterRoof = counterRoof + 1;

        }
    }

    /**
     * Writes the walls in STL files. Used for debugging.
     * 
     * @param fileName
     *            the beginning of the name of the file to write in
     */
    private void writeSTLWalls(final String fileName) {
        int counterWall = 1;
        for (final Polyline p : this.walls) {

            p.returnCentroidMesh()
                .writeSTL(
                    fileName + FilesNames.SEPARATOR + "computedWall"
                        + FilesNames.SEPARATOR + counterWall
                        + FilesNames.EXTENSION);
            counterWall = counterWall + 1;

        }
    }
}
