package fr.nantes1900.models.extended;

import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge;
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
 * grounds, wateries, and special buildings. Contains all the algorithms to
 * parse and build a town, using the building, mesh, ground, and other classes.
 * Allows to write a CityGML file containing this town.
 * 
 * @author Daniel Lefevre
 */
public class Town {

    /**
     * Logger.
     */
    public static final Logger LOG = Logger.getLogger("logger");

    /**
     * List of industrial zones.
     */
    private List<Building> industrials = new ArrayList<Building>();

    /**
     * List of residential zones.
     */
    private List<Building> residentials = new ArrayList<Building>();

    /**
     * List of ground zones.
     */
    private List<Ground> grounds = new ArrayList<Ground>();

    /**
     * List of watery zones.
     */
    private List<Ground> wateries = new ArrayList<Ground>();

    /**
     * List of special buildings.
     */
    private List<SpecialBuilding> specialBuildings =
        new ArrayList<SpecialBuilding>();

    /**
     * Change base matrix from the current base to a base which is ground-like
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
     * Adds a ground to the attribute list of grounds.
     * 
     * @param ground
     *            the ground to add
     */
    public final void addGround(final Ground ground) {
        if (!this.grounds.contains(ground)) {
            this.grounds.add(ground);
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
     * Adds a list of buildings to the industrial list.
     * 
     * @param listBuildings
     *            the list of buildings to add
     */
    public void addIndustrials(List<Building> listBuildings) {
        for (Building b : listBuildings) {
            this.addIndustrial(b);
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
    public final void addWatery(final Ground watery) {
        if (!this.grounds.contains(watery)) {
            this.grounds.add(watery);
        }
    }

    /**
     * Builds a town by computing all the files in the directory. Searches in
     * the directory name the fives directories : inductrials, residentials,
     * grounds, wateries, and special_buildings, treats each files and puts the
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
        final Vector3d normalGround =
            this.extractGroundNormal(directoryName + "/ground.stl");
        this.createChangeBaseMatrix(normalGround);

        // Treat every kind of surfaces put in the algorithms.
        this.treatGrounds(directoryName + "/grounds/");
        Town.LOG.info("Numbers of grounds : " + this.grounds.size());

        this.treatWateries(directoryName + "/wateries/");
        Town.LOG.info("Numbers of wateries : " + this.wateries.size());

        this.treatSpecialBuildings(directoryName + "/special_buildings/");
        Town.LOG.info("Numbers of special buildings : "
            + this.specialBuildings.size());

        this.treatIndustrials(directoryName + "/industrials/", normalGround);
        Town.LOG.info("Numbers of industrials zones : "
            + this.industrials.size());

        this.treatResidentials(directoryName + "/residentials/", normalGround);
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
        writer.addGrounds(this.grounds);
        writer.addGrounds(this.wateries);
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
        this.writeSTLGrounds(directoryName);
        this.writeSTLIndustrials(directoryName);
        this.writeSTLResidentials(directoryName);
        this.writeSTLSpecialBuildings(directoryName);
        this.writeSTLWateries(directoryName);
    }

    /**
     * Writes the grounds as STL files. Used for debugging.
     * 
     * @param directoryName
     *            the name of the directory to write in
     */
    public final void writeSTLGrounds(final String directoryName) {
        int counterGround = 1;

        for (Ground f : this.grounds) {
            f.writeSTL(directoryName + FilesNames.GROUND_FILENAME
                + FilesNames.SEPARATOR + counterGround + FilesNames.EXTENSION);
            ++counterGround;
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
                + FilesNames.SEPARATOR + buildingCounter);
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
                + FilesNames.SEPARATOR + buildingCounter);
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

        for (Ground f : this.wateries) {
            f.writeSTL(directoryName + FilesNames.WATERY_FILENAME
                + FilesNames.SEPARATOR + counterWateries + FilesNames.EXTENSION);
            ++counterWateries;
        }
    }

    /**
     * Adds a list of buildings to the residential list.
     * 
     * @param listBuildings
     *            the list of buildings to add
     */
    private void addResidentials(List<Building> listBuildings) {
        for (Building b : listBuildings) {
            this.addIndustrial(b);
        }
    }

    /**
     * Extracts buildings by extracting the blocks after the ground extraction.
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
        // Extraction of the buildings.
        thingsList = Algos.blockExtract(mesh);

        // Algorithm : detection of buildings considering their size.
        for (Mesh m : thingsList) {
            if (m.size() >= SeparationGroundBuilding.BLOCK_BUILDING_SIZE_ERROR) {

                buildingList.add(m);
            } else {
                noise.addAll(m);
            }
        }

        if (buildingList.size() == 0) {
            Town.LOG.severe("Error : no building found !");
        }

        return buildingList;
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
     * Creates a change base matrix with the normal to the ground. See the
     * MatrixMethod class for more informations.
     * 
     * @param normalGround
     *            the vector to build the matrix.
     */
    private void createChangeBaseMatrix(final Vector3d normalGround) {

        try {
            // Base change
            this.matrix = MatrixMethod.createOrthoBase(normalGround);
            MatrixMethod.changeBase(normalGround, this.matrix);

        } catch (SingularMatrixException e) {
            Town.LOG.severe("Error : the matrix is badly formed !");
            System.exit(1);
        }
    }

    /**
     * Reads the ground file and returns the average normal as ground normal.
     * 
     * @param fileName
     *            the name of the ground file
     * @return the normal to the ground as Vector3d
     */
    private Vector3d extractGroundNormal(final String fileName) {

        final Mesh groundBrut = this.parseFile(fileName);

        // Extract of the normal of the ground
        return groundBrut.averageNormal();
    }

    /**
     * Extracts the grounds, using the groundExtract method.
     * 
     * @param mesh
     *            the mesh to extract from
     * @param normalGround
     *            the normal to the ground (not the gravity-oriented normal, of
     *            course...)
     * @return a mesh containing the ground
     */
    private Mesh groundExtraction(final Mesh mesh, final Vector3d normalGround) {

        // Searches for ground-oriented triangles with an error.
        Mesh meshOriented =
            mesh.orientedAs(normalGround,
                SeparationGroundBuilding.ANGLE_GROUND_ERROR);

        List<Mesh> thingsList;
        List<Mesh> groundsList = new ArrayList<Mesh>();
        // Extracts the blocks in the oriented triangles.
        thingsList = Algos.blockExtract(meshOriented);

        Mesh wholeGround = new Mesh();
        for (Mesh f : thingsList) {
            wholeGround.addAll(f);
        }

        // We consider the altitude of the blocks on an axis parallel to the
        // normal ground.
        final double highDiff = mesh.zMax() - mesh.zMin();

        // Builds an axis normal to the current ground.
        final Edge axisNormalGround =
            new Edge(new Point(0, 0, 0), new Point(normalGround.x,
                normalGround.y, normalGround.z));

        // Project the current whole ground centroid on this axis.
        final Point pAverage =
            axisNormalGround.project(wholeGround.getCentroid());

        // After this, for each block, consider the distance (on the
        // axisNormalGround) as an altitude distance. If it is greater than
        // the error, then it's not considered as ground.
        for (Mesh m : thingsList) {
            final Point projectedPoint =
                axisNormalGround.project(m.getCentroid());
            if (projectedPoint.getZ() < pAverage.getZ()
                || projectedPoint.distance(pAverage) < highDiff
                    * SeparationGroundBuilding.ALTITUDE_ERROR) {

                groundsList.add(m);
            }
        }

        // We consider the size of the blocks : if they're big enough,
        // they're keeped. This is to avoid the parts of roofs, walls,
        // etc...
        thingsList = new ArrayList<Mesh>(groundsList);
        groundsList = new ArrayList<Mesh>();
        for (Mesh m : thingsList) {
            if (m.size() > SeparationGroundBuilding.BLOCK_GROUNDS_SIZE_ERROR) {
                groundsList.add(m);
            }
        }

        // Now that we found the real grounds, we extract the other
        // triangles
        // which are almost ground-oriented to add them.
        meshOriented =
            mesh.orientedAs(normalGround,
                SeparationGroundBuilding.LARGE_ANGLE_GROUND_ERROR);

        // If the new grounds are neighbours from the old ones, they are
        // added to the real grounds.
        thingsList = new ArrayList<Mesh>();
        for (Mesh m : groundsList) {

            final Mesh temp = new Mesh(m);
            temp.addAll(meshOriented);
            final Mesh ret = new Mesh();
            m.getOne().returnNeighbours(ret, temp);
            meshOriented.remove(ret);
            thingsList.add(ret);
        }
        groundsList = thingsList;

        wholeGround = new Mesh();
        for (Mesh f : groundsList) {
            mesh.remove(f);
            wholeGround.addAll(f);
        }

        return wholeGround;
    }

    /**
     * Adds the maximum of noise on grounds to complete them. See block extract
     * method in the Algos class. After the completion of the grounds, triangles
     * are removed from noise.
     * 
     * @param groundsMesh
     *            the ground
     * @param noise
     *            the noise mesh computed by former algorithms
     * @return a list of grounds completed with noise
     */
    private List<Mesh> noiseTreatment(final Mesh groundsMesh, final Mesh noise) {

        List<Mesh> list;

        list = Algos.blockExtract(groundsMesh);
        Algos.blockTreatNoise(list, noise);
        return list;
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
     * Treat a building zone by calling all the algorithms. Returns a list of
     * buildings.
     * 
     * @param normalGround
     *            the normal to the current ground
     * @param building
     */
    private List<Building> treatBuildingZone(final Vector3d normalGround,
        Mesh building) {
        // Base change in the gravity-oriented base.
        building.changeBase(this.matrix);

        // Extraction of the ground.
        final Mesh wholeGround = this.groundExtraction(building, normalGround);

        // Extraction of the buildings : the blocks which are left after the
        // ground extraction.
        final Mesh noise = new Mesh();
        final List<Mesh> buildings = this.buildingsExtraction(building, noise);

        // Treatment of the noise : other blocks are added to the grounds if
        // possible.
        final List<Mesh> groundsMesh = this.noiseTreatment(wholeGround, noise);

        // Cut the little walls, and other things that are not buildings.
        // ArrayList<Mesh> formsList = this.carveRealBuildings(buildings);

        // Foreach building, create an building object, call the algorithm to
        // build it and add it to the list of this town.
        List<Building> listBuildings = new ArrayList<Building>();

        for (Mesh m : buildings) {
            final Building e = new Building();
            e.buildFromMesh(m, wholeGround, normalGround);
            listBuildings.add(e);
        }

        // Foreach ground found, call the algorithm of ground treatment, and
        // add it to the list of this town with an attribute : street.
        for (Mesh m : groundsMesh) {
            final Ground ground = new Ground("street");
            ground.buildFromMesh(m);
            this.addGround(ground);
        }

        return listBuildings;
    }

    /**
     * Cut in a building zone the forms which are not buildings : little walls,
     * chimneys. Method not implemented.
     * 
     * @param buildings
     *            the list of buildings to treat
     * @return the list of the forms
     */
    @SuppressWarnings("unused")
    private ArrayList<Mesh> carveRealBuildings(List<Mesh> buildings) {
        // TODO : implement this method.
        return null;
    }

    /**
     * Treats the files of grounds which are in the directory. Creates Ground
     * objects for each files, puts an attribute, and calls the buildFromMesh
     * method of Ground. Then adds it to the list of grounds.
     * 
     * @param directoryName
     *            the directory name to find the grounds.
     */
    private void treatGrounds(final String directoryName) {
        int counterGrounds = 1;

        while (new File(directoryName + FilesNames.GROUND_FILENAME
            + FilesNames.SEPARATOR + counterGrounds + FilesNames.EXTENSION)
            .exists()) {

            final Ground ground = new Ground("ground");

            ground.buildFromMesh(this.parseFile(directoryName
                + FilesNames.GROUND_FILENAME + FilesNames.SEPARATOR
                + counterGrounds + FilesNames.EXTENSION));

            this.addGround(this.treatGroundZone(ground));

            ++counterGrounds;
        }
    }

    /**
     * Treats the files of industrial zones which are in the directory.
     * Separates grounds and buildings, builds a Ground object and calls
     * buildFromMesh method, builds Building objects, puts the buildings in and
     * calls the buildFromMesh methods.
     * 
     * @param directoryName
     *            the name of the directory where are the files
     * @param normalGravityOriented
     *            the normal gravity-oriented
     */
    private void treatIndustrials(final String directoryName,
        final Vector3d normalGravityOriented) {
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

            Vector3d realNormalToTheGround;

            // If another ground normal is available, extract it. Otherwise,
            // keep the normal gravity-oriented as the normal to the ground.
            if (new File(directoryName + FilesNames.GROUND_FILENAME
                + FilesNames.SEPARATOR + counterIndustrials
                + FilesNames.EXTENSION).exists()) {

                realNormalToTheGround =
                    this.extractGroundNormal(directoryName
                        + FilesNames.GROUND_FILENAME + FilesNames.SEPARATOR
                        + counterIndustrials + FilesNames.EXTENSION);

                MatrixMethod.changeBase(realNormalToTheGround, this.matrix);

            } else {
                realNormalToTheGround = normalGravityOriented;
            }

            this.addIndustrials(this.treatBuildingZone(realNormalToTheGround,
                mesh));

            ++counterIndustrials;
        }
    }

    /**
     * Treats the files of residential zones which are in the directory.
     * Separates grounds and buildings, builds a Ground object and calls
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

            Vector3d realNormalToTheGround;
            // If another ground normal is available, extract it. Otherwise,
            // keep
            // the normal gravity-oriented as the normal to the ground.
            if (new File(directoryName + FilesNames.GROUND_FILENAME
                + FilesNames.SEPARATOR + counterResidentials
                + FilesNames.EXTENSION).exists()) {

                realNormalToTheGround =
                    this.extractGroundNormal(directoryName
                        + FilesNames.GROUND_FILENAME + FilesNames.SEPARATOR
                        + counterResidentials + FilesNames.EXTENSION);

                MatrixMethod.changeBase(realNormalToTheGround, this.matrix);

            } else {
                realNormalToTheGround = normalGravityOriented;
            }

            this.addResidentials(this.treatBuildingZone(realNormalToTheGround,
                mesh));

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
     * Treats the ground object. For the moment, it just changes base.
     * 
     * @param groundZone
     *            the ground to treat
     * @return the ground treated
     */
    private Ground treatGroundZone(Ground groundZone) {

        groundZone.getMesh().changeBase(this.matrix);

        return groundZone;
    }

    /**
     * Treats the files of wateries which are in the directory. Creates Ground
     * objects for each files, puts an attribute : Water, and calls the
     * buildFromMesh method of Ground. Then adds it to the list of wateries.
     * 
     * @param directoryName
     *            the directory name to find the wateries.
     */
    private void treatWateries(final String directoryName) {

        int counterWateries = 1;

        while (new File(directoryName + FilesNames.WATERY_FILENAME
            + FilesNames.SEPARATOR + counterWateries + FilesNames.EXTENSION)
            .exists()) {

            final Ground watery = new Ground("ground");

            watery.buildFromMesh(this.parseFile(directoryName
                + FilesNames.WATERY_FILENAME + FilesNames.SEPARATOR
                + counterWateries + FilesNames.EXTENSION));

            this.addWatery(this.treatGroundZone(watery));

            ++counterWateries;
        }
    }
}
