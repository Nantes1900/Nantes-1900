package fr.nantes1900.models.extended;

import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.constants.SeparationTreatmentWallsRoofs;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.Polyline.EmptyPolylineException;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.Surface.ImpossibleNeighboursOrderException;
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
 * Implements a building as two lists of surfaces : walls and roofs.
 * 
 * @author Daniel Lefevre
 */
public class Building {

    // FIXME : consider refactoring this class : less methods, more clean,
    // quicker.

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
     * @param normalFloor
     *            the normal to the ground
     * @param grounds
     *            the mesh containing all the grounds
     */
    public final void buildFromMesh(final Mesh building, final Mesh grounds,
        final Vector3d normalFloor) {

        // Creates a new mesh.
        final Mesh noise = new Mesh();

        // Applies the first algorithms : extract the walls, and after this,
        // extract the roofs.
        // FIXME : improve the speed of sortWalls and sortRoofs...
        final List<Mesh> wallList =
            this.sortWalls(building, normalFloor, noise);
        final List<Mesh> roofList =
            this.sortRoofs(building, normalFloor, noise);

        // Treats the noise.
        this.treatNoise(wallList, roofList, noise, grounds);

        // TODO : maybe convert in surfaces from the beginning to avoid this
        // step.
        final List<Surface> wallTreatedList = this.vectorizeSurfaces(wallList);
        final List<Surface> roofTreatedList = this.vectorizeSurfaces(roofList);
        final Surface groundsTreated = new Surface(grounds);

        // TODO : translate this.
        // TODO : voir si on peut pas réduire le nombre de fonctions dans ce
        // bazar.

        // Finds the neighbours, searching deeply (add noise to the walls
        // and roofs to add every edge to one of these surface. Then find
        // the surfaces which share one edge.
        this.searchForNeighbours(wallTreatedList, roofTreatedList,
            groundsTreated, noise);

        // Treats the new neighbours.
        this.treatNewNeighbours(wallTreatedList, roofTreatedList);

        // Finds the neighbours of each roof and wall (and the grounds).
        this.determinateNeighbours(wallTreatedList, roofTreatedList,
            groundsTreated);

        // Find all the surface which have 0, or 1, or 2 neigbhours and then
        // cannot be treated.
        this.sortSurfaces(wallTreatedList, roofTreatedList);

        // From all the neighbours, computes the wrap line and returns the
        // surfaces as polylines.
        this.findEdgesFromNeighbours(wallTreatedList, roofTreatedList,
            normalFloor, groundsTreated);

        // FIXME : remove when possible.
        this.writeSTL("files/St-Similien/m02/results/");
    }

    /**
     * For each surface (wall or roof), orders the neighbours, and after this,
     * computes the edges. If an exception is throwed by these two methods, the
     * surface is not treated at all.
     * 
     * @param wallTreatedList
     *            the list of walls as surfaces
     * @param roofTreatedList
     *            the list of roofs as surfaces
     * @param normalFloor
     *            the normal to the ground
     * @param grounds
     *            the mesh containing the grounds
     */
    public final void findEdgesFromNeighbours(
        final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList, final Vector3d normalFloor,
        final Surface grounds) {

        final Map<Point, Point> pointMap = new HashMap<Point, Point>();
        final Map<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

        // Adds all the surfaces
        final List<Surface> wholeTreatedList = new ArrayList<Surface>();
        wholeTreatedList.addAll(wallTreatedList);
        wholeTreatedList.addAll(roofTreatedList);

        int counterWellTreated = 0;
        int counterNotTreated = 0;

        for (Surface surface : wholeTreatedList) {
            try {

                // Orders its neighbours in order to treat them.
                // If the neighbours of one surface are not 2 per 2 neighbours
                // each other, then it tries to correct it.
                surface.orderNeighbours(wholeTreatedList, grounds);

                // When the neighbours are sorted, finds the intersection of
                // them to find the edges of this surface.
                final Polyline p =
                    surface.findEdges(wallTreatedList, pointMap, edgeMap,
                        normalFloor);

                // If it is a wall, adds it to the wall list, otherwise to the
                // roof list (obvious...).
                if (wallTreatedList.contains(surface)) {
                    this.walls.add(p);
                } else {
                    this.roofs.add(p);
                }

                ++counterWellTreated;

            } catch (ImpossibleNeighboursOrderException e) {
                // If there is a problem, the treatment cannot continue.
                // LOOK : maybe return the unsorted bounds to have a result.
                // FIXME : remove that syso
                System.err.println("Impossible order !");
                ++counterNotTreated;

            } catch (InvalidSurfaceException e) {
                // If there is a problem, we cannot continue the treatment.
                // LOOK : maybe return the unsorted bounds to have a result.
                // FIXME : remove that syso
                System.err.println("Invalid edge computing !");
                ++counterNotTreated;
            }
        }

        // FIXME : logger.
        System.out.println("Parts correctly treated : " + counterWellTreated);
        System.out.println("Parts not treated : " + counterNotTreated);
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
     * @param wallTreatedList
     *            the list of walls as meshes
     * @param roofTreatedList
     *            the list of roofs as meshes
     * @param grounds
     *            the grounds as one mesh
     * @param noise
     */
    private void determinateNeighbours(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList, final Surface grounds) {

        final List<Polyline> wallsBoundsList = new ArrayList<Polyline>();
        final List<Polyline> roofsBoundsList = new ArrayList<Polyline>();

        final Polyline groundsBounds = grounds.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallTreatedList);
        wholeList.addAll(roofTreatedList);

        // TODO : improve that mess method...

        // LOOK : maybe clear those neighbours after the treatNewNeighbours,
        // because they still stay in memory otherwise...

        // First we clear the neighbours.
        for (Surface s : wholeList) {
            s.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        grounds.getNeighbours().clear();

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
            // TODO? Maybe no need to check if roofs are neighbours of ground.
            if (polyline1.isNeighbour(groundsBounds)) {
                wholeList.get(i).addNeighbour(grounds);
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
     * @param grounds
     *            the grounds as one mesh
     * @param noise
     *            the mesh containing the noise
     */
    private void searchForNeighbours(final List<Surface> wallList,
        final List<Surface> roofList, final Surface grounds, final Mesh noise) {

        final Polyline groundsBounds = grounds.returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<Surface>();
        wholeList.addAll(wallList);
        wholeList.addAll(roofList);

        // First we clear the neighbours.
        for (Surface m : wholeList) {
            m.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        grounds.getNeighbours().clear();

        // FIXME : try this thing... IN DETERMINATE NEIGHBOURS, NOT HERE !
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

        // FIXME : vérifier que la méthode n'existe pas déjà dans Mesh, Polyline
        // ou Surface... pour améliorer la lisibilité de cette méthode.
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
     * @param normalFloor
     *            the normal to the ground
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
            // roofs or noise. If a wall is oriented in direction of the ground,
            // it is not keeped.
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
     * Removes the surfaces which have two neighbours or less : they can't be
     * treated. It's isolated surfaces for example. //FIXME : try to make
     * something of them.
     * 
     * @param wallTreatedList
     *            the list of walls as surfaces
     * @param roofTreatedList
     *            the list of roofs as surfaces
     */
    private void sortSurfaces(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList) {

        for (int i = 0; i < wallTreatedList.size(); ++i) {
            if (wallTreatedList.get(i).getNeighbours().size() < 3) {
                wallTreatedList.remove(wallTreatedList.get(i));
            }
        }
        for (int i = 0; i < roofTreatedList.size(); ++i) {
            if (roofTreatedList.get(i).getNeighbours().size() < 3) {
                roofTreatedList.remove(roofTreatedList.get(i));
            }
        }
    }

    /**
     * Cuts the normal-to-the-ground mesh in walls considering the orientation
     * of the triangles.
     * 
     * @param building
     *            the building to carve
     * @param normalFloor
     *            the normal to the ground
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

    /**
     * Treats the new neighbours : if some surfaces are neighbours, are have the
     * same orientation, then it merges it to form only one surface. FIXME :
     * Needs to call determinateNeighbours first... FIXME : make only one
     * method.
     * 
     * @param wallTreatedList
     *            the list of walls as surfaces
     * @param roofTreatedList
     *            the list of roofs as surfaces
     */
    // FIXME : improve the velocity of this method.
    // FIXME : comment more.
    private void treatNewNeighbours(final List<Surface> wallTreatedList,
        final List<Surface> roofTreatedList) {

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
    private void treatNoise(final List<Mesh> wallList,
        final List<Mesh> roofList, final Mesh noise, final Mesh grounds) {

        try {
            // Add the oriented and neighbour noise to the walls.
            Algos.blockTreatOrientedNoise(wallList, noise,
                SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

            // Add the oriented and neighbour noise to the roofs.
            Algos.blockTreatOrientedNoise(roofList, noise,
                SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR);

            // FIXME : if some roofs are neighbours to the grounds, remove
            // them...
            // because it's noise. TODO ? Because if they are not noise ?
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            // TODO : this exception shouldn't happen. Maybe remove this try
            // catch.
            e.printStackTrace();
        }
    }

    /**
     * Change the meshes into surfaces.
     * 
     * @param list
     *            the list of meshes
     * @return the list of surfaces created from the meshes
     */
    // TODO : change this name !
    private List<Surface> vectorizeSurfaces(final List<Mesh> list) {

        final List<Surface> treatedList = new ArrayList<Surface>();

        for (Mesh m : list) {
            treatedList.add(new Surface(m));
        }

        return treatedList;
    }

    /**
     * Writes the roofs in STL files. Used for debugging.
     * 
     * @param directoryName
     *            the directory to write in
     */
    private void writeSTLRoofs(final String directoryName) {
        int counterRoof = 0;
        for (Polyline p : this.roofs) {
            try {
                p.returnCentroidMesh().writeSTL(
                    directoryName + "computedRoof" + counterRoof
                        + FilesNames.EXTENSION);
                counterRoof = counterRoof + 1;
            } catch (EmptyPolylineException e) {
                // FIXME
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
    private void writeSTLWalls(final String directoryName) {
        int counterWall = 0;
        for (Polyline p : this.walls) {
            try {
                p.returnCentroidMesh().writeSTL(
                    directoryName + "computedWall" + counterWall
                        + FilesNames.EXTENSION);
                counterWall = counterWall + 1;
            } catch (EmptyPolylineException e) {
                // FIXME
                System.out.println("Empty polyline : care !");
            }
        }
    }
}
