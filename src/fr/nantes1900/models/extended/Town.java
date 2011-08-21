package fr.nantes1900.models.extended;

import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.constants.SeparationFloorBuilding;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.utils.Algos;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.utils.WriterCityGML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.vecmath.Vector3d;

/**
 * Implements a town containing five types of zones : industrials, residentials,
 * floors, wateries, and special buildings. Contains all the algorithms to parse
 * and build a town, using the building, mesh, floor, and other classes. Allows
 * to write a CityGML file containing this town.
 * 
 * @author Daniel Lefevre
 */
public class Town {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger("logger");

    /**
     * List of industrial zones.
     */
    private List<Building> industrials = new ArrayList<Building>();

    /**
     * List of residential zones.
     */
    private List<Building> residentials = new ArrayList<Building>();

    /**
     * List of floor zones.
     */
    private List<Floor> floors = new ArrayList<Floor>();

    /**
     * List of watery zones.
     */
    private List<Floor> wateries = new ArrayList<Floor>();

    /**
     * List of special buildings.
     */
    private List<SpecialBuilding> specialBuildings =
        new ArrayList<SpecialBuilding>();

    /**
     * Change base matrix from the current base to a base which is floor-like
     * oriented.
     */
    private double[][] matrix =
        new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

    /**
     * Constructor.
     */
    public Town() {
        Town.LOG.setLevel(Level.FINEST);
        final Handler handler =
            new StreamHandler(System.out, new SimpleFormatter());
        handler.setLevel(Level.FINEST);
        Town.LOG.addHandler(handler);
        Town.LOG.setUseParentHandlers(false);
    }

    /**
     * Adds a floor to the attribute list of floors.
     * 
     * @param floor
     *            the floor to add
     */
    public final void addFloor(final Floor floor) {
        if (!this.floors.contains(floor)) {
            this.floors.add(floor);
        }
    }

    /**
     * Adds an industrial building to the attribute list of industrials.
     * 
     * @param building
     *            the industrial to add
     */
    public final void addIndustrial(final Building building) {
        if (!this.industrials.contains(building)) {
            this.industrials.add(building);
        }
    }

    /**
     * Adds a residential building to the attribute list of residentials.
     * 
     * @param building
     *            the residential to add
     */
    public final void addResidential(final Building building) {
        if (!this.residentials.contains(building)) {
            this.residentials.add(building);
        }
    }

    /**
     * Adds a special building to the attribute list of special buildings.
     * 
     * @param specialBuilding
     *            the special building to add
     */
    public final void addSpecialBuilding(final SpecialBuilding specialBuilding) {
        if (!this.specialBuildings.contains(specialBuilding)) {
            this.specialBuildings.add(specialBuilding);
        }
    }

    /**
     * Adds a watery to the attribute list of wateries.
     * 
     * @param watery
     *            the watery to add
     */
    public final void addWatery(final Floor watery) {
        if (!this.floors.contains(watery)) {
            this.floors.add(watery);
        }
    }

    /**
     * Builds a town by computing all the files in the directory. Searches in
     * the directory name the fives directories : inductrials, residentials,
     * floors, wateries, and special_buildings, treats each files and puts the
     * results in the lists.
     * 
     * @param directoryName
     *            the directory name where are the five directories
     */
    public final void buildFromMesh(final String directoryName) {

        final long time = System.nanoTime();

        // Create or clean the directory : directoryName + "/results/" to put
        // the datas in.
        this.cleanResultDirectory(directoryName);

        // Extract the normal gravity oriented and change it to the new
        // base.
        final Vector3d normalFloor =
            this.extractFloorNormal(directoryName + "/floor.stl");
        this.createChangeBaseMatrix(normalFloor);

        // Treat every kind of surfaces put in the algorithms.
        this.treatFloors(directoryName + "/floors/");
        Town.LOG.info("Numbers of floors : " + this.floors.size());

        this.treatWateries(directoryName + "/wateries/");
        Town.LOG.info("Numbers of wateries : " + this.wateries.size());

        this.treatSpecialBuildings(directoryName + "/special_buildings/");
        Town.LOG.info("Numbers of special buildings : "
            + this.specialBuildings.size());

        this.treatIndustrials(directoryName + "/industrials/", normalFloor);
        Town.LOG.info("Numbers of industrials zones : "
            + this.industrials.size());

        this.treatResidentials(directoryName + "/residentials/", normalFloor);
        Town.LOG.info("Numbers of residentials zones : "
            + this.residentials.size());

        Town.LOG.info("Temps total écoulé : " + (System.nanoTime() - time));
    }

    /**
     * Writes the town in a CityGML file. Uses the WriterCityGML.
     * 
     * @param fileName
     *            the name of the file to write in
     */
    public final void writeCityGML(final String fileName) {
        final WriterCityGML writer = new WriterCityGML(fileName);

        writer.addBuildings(this.residentials);
        writer.addBuildings(this.industrials);
        writer.addFloors(this.floors);
        writer.addFloors(this.wateries);
        writer.addSpecialBuildings(this.specialBuildings);

        writer.write();
    }

    /**
     * Writes every members of the lists as STL files.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTL(final String directoryName) {
        this.writeSTLFloors(directoryName);
        this.writeSTLIndustrials(directoryName);
        this.writeSTLResidentials(directoryName);
        this.writeSTLSpecialBuildings(directoryName);
        this.writeSTLWateries(directoryName);
    }

    /**
     * Writes the floors as STL files. Used for debugging.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTLFloors(final String directoryName) {
        int counterFloor = 1;

        for (Floor f : this.floors) {
            f.writeSTL(directoryName + FilesNames.FLOOR_FILENAME
                + FilesNames.SEPARATOR + counterFloor + FilesNames.EXTENSION);
            ++counterFloor;
        }
    }

    /**
     * Writes the industrial buildings as STL files. Used for debugging.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTLIndustrials(final String directoryName) {
        int buildingCounter = 1;

        for (Building m : this.industrials) {
            m.writeSTL(directoryName + FilesNames.INDUSTRIAL_FILENAME
                + FilesNames.SEPARATOR + buildingCounter + FilesNames.EXTENSION);
            ++buildingCounter;
        }
    }

    /**
     * Writes the residential buildings as STL files. Used for debugging.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTLResidentials(final String directoryName) {
        int buildingCounter = 1;

        for (Building m : this.residentials) {
            m.writeSTL(directoryName + FilesNames.RESIDENTIAL_FILENAME
                + FilesNames.SEPARATOR + buildingCounter + FilesNames.EXTENSION);
            ++buildingCounter;
        }
    }

    /**
     * Writes the special buildings as STL files. Used for debugging.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTLSpecialBuildings(final String directoryName) {
        int buildingCounter = 1;

        for (SpecialBuilding m : this.specialBuildings) {
            m.getMesh().writeSTL(
                directoryName + FilesNames.SPECIAL_BUILDING_FILENAME
                    + FilesNames.SEPARATOR + buildingCounter
                    + FilesNames.EXTENSION);
            ++buildingCounter;
        }
    }

    /**
     * Writes the wateries as STL files. Used for debugging.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTLWateries(final String directoryName) {
        int counterWateries = 1;

        for (Floor f : this.wateries) {
            f.writeSTL(directoryName + FilesNames.WATERY_FILENAME
                + FilesNames.SEPARATOR + counterWateries + FilesNames.EXTENSION);
            ++counterWateries;
        }
    }

    /**
     * Extracts buildings by extracting the blocks after the floor extraction.
     * 
     * @param mesh
     *            the mesh containing the buildinfs
     * @param noise
     *            the mesh to stock the noise
     * @return a list of buildings as meshes
     */
    private List<Mesh> buildingsExtraction(final Mesh mesh, final Mesh noise) {

        final List<Mesh> buildingList = new ArrayList<Mesh>();

        List<Mesh> thingsList;
        try {
            // Extraction of the buildings.
            thingsList = Algos.blockExtract(mesh);

            // Algorithm : detection of buildings considering their size.
            for (Mesh m : thingsList) {
                if (m.size() >= SeparationFloorBuilding.BLOCK_BUILDING_SIZE_ERROR) {

                    buildingList.add(m);
                } else {
                    noise.addAll(m);
                }
            }

            if (buildingList.size() == 0) {
                Town.LOG.severe("Error : no building found !");
            }

            return buildingList;

        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cleans the directory "results" to put the results in it.
     * 
     * @param directoryName
     *            the name of the working directory
     */
    private void cleanResultDirectory(final String directoryName) {
        if (new File(directoryName + FilesNames.RESULT_DIRECTORY).exists()) {

            // List all the files of the directory
            final File[] fileList =
                new File(directoryName + FilesNames.RESULT_DIRECTORY)
                    .listFiles();

            for (File f : fileList) {
                // Delete only the files, and the empty directories. Then
                // it's safe to put something in a directory Archives for
                // example.
                f.delete();
            }
        } else {
            new File(directoryName + FilesNames.RESULT_DIRECTORY).mkdir();
        }
    }

    /**
     * Creates a change base matrix with the normal to the floor. See the
     * MatrixMethod class for more informations.
     * 
     * @param normalFloor
     *            the vector to build the matrix.
     */
    private void createChangeBaseMatrix(final Vector3d normalFloor) {

        try {
            // Base change
            this.matrix = MatrixMethod.createOrthoBase(normalFloor);
            MatrixMethod.changeBase(normalFloor, this.matrix);

        } catch (SingularMatrixException e) {
            Town.LOG.severe("Error : the matrix is badly formed !");
            System.exit(1);
        }
    }

    /**
     * Reads the floor file and returns the average normal as floor normal.
     * 
     * @param fileName
     *            the name of the floor file
     * @return the normal to the floor as Vector3d
     */
    private Vector3d extractFloorNormal(final String fileName) {

        final Mesh floorBrut = this.parseFile(fileName);

        // Extract of the normal of the floor
        return floorBrut.averageNormal();
    }

    /**
     * Extracts the floors, using the floorExtract method.
     * 
     * @param mesh
     *            the mesh to extract from
     * @param normalFloor
     *            the normal to the floor (not the gravity-oriented normal, of
     *            course...)
     * @return a mesh containing the floor
     */
    private Mesh floorExtraction(final Mesh mesh, final Vector3d normalFloor) {
        // TODO? Return an exception if there is no floor in the mesh, such as :
        // please get some floors during the previous cut ! Write it in the
        // "protocole de découpage" document.

        // Searches for floor-oriented triangles with an error.
        Mesh meshOriented =
            mesh.orientedAs(normalFloor,
                SeparationFloorBuilding.ANGLE_FLOOR_ERROR);

        List<Mesh> thingsList;
        List<Mesh> floorsList = new ArrayList<Mesh>();
        try {
            // Extracts the blocks in the oriented triangles.
            thingsList = Algos.blockExtract(meshOriented);

            final Mesh wholeFloor = new Mesh();
            for (Mesh f : thingsList) {
                wholeFloor.addAll(f);
            }

            // We consider the altitude of the blocks on an axis parallel to the
            // normal floor.
            final double highDiff = mesh.zMax() - mesh.zMin();

            // Builds an axis normal to the current floor.
            final Edge axisNormalFloor =
                new Edge(new Point(0, 0, 0), new Point(normalFloor.x,
                    normalFloor.y, normalFloor.z));

            // Project the current whole floor centroid on this axis.
            final Point pAverage =
                axisNormalFloor.project(wholeFloor.getCentroid());

            // After this, for each block, consider the distance (on the
            // axisNormalFloor) as an altitude distance. If it is greater than
            // the error, then it's not considered as ground.
            for (Mesh m : thingsList) {
                final Point projectedPoint =
                    axisNormalFloor.project(m.getCentroid());
                if (projectedPoint.getZ() < pAverage.getZ()
                    || projectedPoint.distance(pAverage) < highDiff
                        * SeparationFloorBuilding.ALTITUDE_ERROR) {

                    floorsList.add(m);
                }
            }

            // We consider the size of the blocks : if they're big enough,
            // they're keeped. This is to avoid the parts of roofs, walls,
            // etc...
            thingsList = new ArrayList<Mesh>(floorsList);
            floorsList = new ArrayList<Mesh>();
            for (Mesh m : thingsList) {
                if (m.size() > SeparationFloorBuilding.BLOCK_FLOORS_SIZE_ERROR) {
                    floorsList.add(m);
                }
            }

            // Now that we found the real floors, we extract the other triangles
            // which are almost floor-oriented to add them.
            meshOriented =
                mesh.orientedAs(normalFloor,
                    SeparationFloorBuilding.LARGE_ANGLE_FLOOR_ERROR);

            // If the new floors are neighbours from the old ones, they are
            // added to the real floors.
            thingsList = new ArrayList<Mesh>();
            for (Mesh m : floorsList) {

                final Mesh temp = new Mesh(m);
                temp.addAll(meshOriented);
                final Mesh ret = new Mesh();
                // FIXME : care : if the mesh m doesn't belong anymore to
                // meshOriented ? Because it's already contained in another part
                // computed before ? This is not a real problem because the
                // doublons will be removed at the last lines : when every
                // triangles are put in the same mesh, there is no doublons
                // possible. But it should be improved.
                m.getOne().returnNeighbours(ret, temp);
                meshOriented.remove(ret);
                thingsList.add(ret);
            }
            floorsList = thingsList;
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            e.printStackTrace();
        }

        final Mesh wholeFloor = new Mesh();
        for (Mesh f : floorsList) {
            mesh.remove(f);
            wholeFloor.addAll(f);
        }

        return wholeFloor;
    }

    /**
     * Adds the maximum of noise on floors to complete them. See block extract
     * method in the Algos class. After the completion of the floors, triangles
     * are removed from noise.
     * 
     * @param floorsMesh
     *            the floor
     * @param noise
     *            the noise mesh computed by former algorithms
     * @return a list of floors completed with noise
     */
    // TODO : check if this method works, and then commit this class.
    private List<Mesh> noiseTreatment(final Mesh floorsMesh, final Mesh noise) {

        List<Mesh> list;
        try {

            list = Algos.blockExtract(floorsMesh);
            Algos.blockTreatNoise(list, noise);
            return list;
        } catch (MoreThanTwoTrianglesPerEdgeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses a STL file. Uses the ParserSTL class.
     * 
     * @param fileName
     *            the name of the file
     * @return the mesh parsed
     */
    private Mesh parseFile(final String fileName) {

        try {
            final ParserSTL parser = new ParserSTL(fileName);
            return parser.read();

        } catch (IOException e) {
            Town.LOG
                .severe("Error : the file is badly formed, not found or unreadable !");
            System.exit(1);
            return null;
        }
    }

    /**
     * Treats the files of floors which are in the directory. Creates Floor
     * objects for each files, puts an attribute, and calls the buildFromMesh
     * method of Floor. Then adds it to the list of floors.
     * 
     * @param directoryName
     *            the directory name to find the floors.
     */
    private void treatFloors(final String directoryName) {
        int counterFloors = 1;

        while (new File(directoryName + FilesNames.FLOOR_FILENAME
            + FilesNames.SEPARATOR + counterFloors + FilesNames.EXTENSION)
            .exists()) {

            final Floor floor = new Floor("floor");

            floor.buildFromMesh(this.parseFile(directoryName
                + FilesNames.FLOOR_FILENAME + FilesNames.SEPARATOR
                + counterFloors + FilesNames.EXTENSION));

            floor.getMesh().changeBase(this.matrix);

            this.addFloor(floor);

            ++counterFloors;
        }
    }

    /**
     * Treats the files of industrial zones which are in the directory.
     * Separates floors and buildings, builds a Floor object and calls
     * buildFromMesh method, builds Building objects, puts the buildings in and
     * calls the buildFromMesh methods.
     * 
     * @param directoryName
     *            the name of the directory where are the files
     * @param normalFloor
     *            the normal gravity-oriented
     */
    private void treatIndustrials(final String directoryName,
        final Vector3d normalGravityOriented) {
        // FIXME : maybe create the same private method for things common with
        // treat residentials...
        int counterIndustrials = 1;

        // While there is files correctly named...
        while (new File(directoryName + FilesNames.INDUSTRIAL_FILENAME
            + FilesNames.SEPARATOR + counterIndustrials + FilesNames.EXTENSION)
            .exists()) {

            // ...Parse the meshes of these files
            final Mesh mesh =
                this.parseFile(directoryName + FilesNames.INDUSTRIAL_FILENAME
                    + FilesNames.SEPARATOR + counterIndustrials
                    + FilesNames.EXTENSION);

            Vector3d realNormalToTheFloor;
            // If another floor normal is available, extract it. Otherwise, keep
            // the normal gravity-oriented as the normal to the floor.
            if (new File(directoryName + FilesNames.FLOOR_FILENAME
                + FilesNames.SEPARATOR + counterIndustrials
                + FilesNames.EXTENSION).exists()) {

                realNormalToTheFloor =
                    this.extractFloorNormal(directoryName
                        + FilesNames.FLOOR_FILENAME + FilesNames.SEPARATOR
                        + counterIndustrials + FilesNames.EXTENSION);

                MatrixMethod.changeBase(realNormalToTheFloor, this.matrix);

            } else {
                realNormalToTheFloor = normalGravityOriented;
            }

            // LOOK : after extracting the
            // normalGravityOriented, then make the change base on each mesh
            // and assume that this normal is (0, 0, 1).
            // Then the real normalFloor will not be confonded.
            // Make it on all the source codes !

            // Base change in the gravity-oriented base.
            mesh.changeBase(this.matrix);

            // Extraction of the floor.
            final Mesh wholeFloor =
                this.floorExtraction(mesh, realNormalToTheFloor);

            // Extraction of the buildings : the blocks which are left after the
            // floor extraction.
            final Mesh noise = new Mesh();
            final List<Mesh> buildings = this.buildingsExtraction(mesh, noise);

            // Treatment of the noise : other blocks are added to the floors if
            // possible.
            final List<Mesh> floorsMesh =
                this.noiseTreatment(wholeFloor, noise);

            // FIXME : code this method : cut the little walls, and other things
            // that are not buildings.
            // ArrayList<Mesh> formsList = this.carveRealBuildings(buildings);

            // Foreach building, create an building object, call the algorithm
            // to build it and add it to the list of this town.
            for (Mesh m : buildings) {
                final Building e = new Building();
                e.buildFromMesh(m, wholeFloor, normalGravityOriented);
                this.addIndustrial(e);
            }

            // Foreach ground found, call the algorithm of ground treatment, and
            // add it to the list of this town with an attribute : street.
            for (Mesh m : floorsMesh) {
                final Floor floor = new Floor("street");
                floor.buildFromMesh(m);
            }

            // TODO : treat the not real buildings : the formsList which
            // contains walls, chimneys, maybe trains, boats, and other forms.

            ++counterIndustrials;
        }
    }

    /**
     * Treats the files of residential zones which are in the directory.
     * Separates floors and buildings, builds a Floor object and calls
     * buildFromMesh method, builds Building objects, puts the buildings in and
     * calls the buildFromMesh methods.
     * 
     * @param directoryName
     *            the name of the directory where are the files
     * @param normalGravityOriented
     *            the normal oriented as the gravity vector oriented to the sky
     */
    private void treatResidentials(final String directoryName,
        final Vector3d normalGravityOriented) {
        int counterResidentials = 1;

        // While there is files correctly named...
        while (new File(directoryName + FilesNames.RESIDENTIAL_FILENAME
            + FilesNames.SEPARATOR + counterResidentials + FilesNames.EXTENSION)
            .exists()) {

            // ...Parse the meshes of these files
            final Mesh mesh =
                this.parseFile(directoryName + FilesNames.RESIDENTIAL_FILENAME
                    + FilesNames.SEPARATOR + counterResidentials
                    + FilesNames.EXTENSION);

            Vector3d realNormalToTheFloor;
            // If another floor normal is available, extract it. Otherwise, keep
            // the normal gravity-oriented as the normal to the floor.
            if (new File(directoryName + FilesNames.FLOOR_FILENAME
                + FilesNames.SEPARATOR + counterResidentials
                + FilesNames.EXTENSION).exists()) {

                realNormalToTheFloor =
                    this.extractFloorNormal(directoryName
                        + FilesNames.FLOOR_FILENAME + FilesNames.SEPARATOR
                        + counterResidentials + FilesNames.EXTENSION);

                MatrixMethod.changeBase(realNormalToTheFloor, this.matrix);

            } else {
                realNormalToTheFloor = normalGravityOriented;
            }

            // LOOK : after extracting the normalGravityOriented, then make the
            // change base on each mesh
            // and assume that this normal is (0, 0, 1).
            // Then the real normalFloor will not be confonded.
            // Make it on all the source codes !

            // Base change in the gravity-oriented base.
            mesh.changeBase(this.matrix);

            // Extraction of the floor.
            final Mesh wholeFloor =
                this.floorExtraction(mesh, realNormalToTheFloor);

            // Extraction of the buildings : the blocks which are left after the
            // floor extraction.
            final Mesh noise = new Mesh();
            final List<Mesh> buildings = this.buildingsExtraction(mesh, noise);

            // Treatment of the noise : other blocks are added to the floors if
            // possible.
            final List<Mesh> floorsMesh =
                this.noiseTreatment(wholeFloor, noise);

            // FIXME : code this method : cut the little walls, and other things
            // that are not buildings.
            // ArrayList<Mesh> formsList = this.carveRealBuildings(buildings);

            // Foreach building, create an building object, call the algorithm
            // to build it and add it to the list of this town.
            for (Mesh m : buildings) {
                final Building e = new Building();
                e.buildFromMesh(m, wholeFloor, normalGravityOriented);
                this.addResidential(e);
            }

            // Foreach ground found, call the algorithm of ground treatment, and
            // add it to the list of this town with an attribute : street.
            for (Mesh m : floorsMesh) {
                final Floor floor = new Floor("street");
                floor.buildFromMesh(m);
            }

            // TODO : treat the not real buildings : the formsList which
            // contains walls, chimneys, maybe trains, boats, and other forms.

            ++counterResidentials;
        }
    }

    /**
     * Treats the files of special buildings which are in the directory. Puts
     * them as meshes in the specialBuilding list.
     * 
     * @param directoryName
     *            the name of the directory where are the files
     */
    private void treatSpecialBuildings(final String directoryName) {
        int counterSpecialBuildings = 1;

        while (new File(directoryName + FilesNames.SPECIAL_BUILDING_FILENAME
            + FilesNames.SEPARATOR + counterSpecialBuildings
            + FilesNames.EXTENSION).exists()) {

            final Mesh meshSpecialBuilding =
                this.parseFile(directoryName
                    + FilesNames.SPECIAL_BUILDING_FILENAME
                    + FilesNames.SEPARATOR + counterSpecialBuildings
                    + FilesNames.EXTENSION);

            meshSpecialBuilding.changeBase(this.matrix);

            final SpecialBuilding specialBuilding = new SpecialBuilding();

            specialBuilding.buildFromMesh(meshSpecialBuilding);

            this.addSpecialBuilding(specialBuilding);

            ++counterSpecialBuildings;
        }
    }

    /**
     * Treats the files of wateries which are in the directory. Creates Floor
     * objects for each files, puts an attribute : Water, and calls the
     * buildFromMesh method of Floor. Then adds it to the list of wateries.
     * 
     * @param directoryName
     *            the directory name to find the wateries.
     */
    private void treatWateries(final String directoryName) {
        // FIXME : see in treatFloors, it's the same. Maybe create a private
        // common shared method.

        int counterWateries = 1;

        while (new File(directoryName + FilesNames.WATERY_FILENAME
            + FilesNames.SEPARATOR + counterWateries + FilesNames.EXTENSION)
            .exists()) {

            final Floor watery = new Floor("floor");

            watery.buildFromMesh(this.parseFile(directoryName
                + FilesNames.WATERY_FILENAME + FilesNames.SEPARATOR
                + counterWateries + FilesNames.EXTENSION));

            watery.getMesh().changeBase(this.matrix);

            this.addWatery(watery);

            ++counterWateries;
        }
    }
}
