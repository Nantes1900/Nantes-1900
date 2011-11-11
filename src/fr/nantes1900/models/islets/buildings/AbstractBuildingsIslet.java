package fr.nantes1900.models.islets.buildings;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationGrounds;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.utils.Algos;

/**
 * Abstracts a building islet : residential or industrial. This class contains
 * all the methods to apply the treatments on the meshes.
 * @author Daniel Lefèvre
 */
public abstract class AbstractBuildingsIslet extends AbstractIslet
{

    /**
     * The list of buildings contained in the islet after the separation.
     */
    private List<Building> buildings = new ArrayList<>();

    /**
     * The mesh containing all the buildings after the separation from the
     * ground.
     */
    private Mesh initialBuilding;

    /**
     * The mesh containing all the grounds after the separation from the
     * buildings.
     */
    private Mesh initialGround;

    /**
     * The number of the current step.
     */
    private int progression = 0;

    /**
     * The ground contained in the islet. Every grounds, even if separated, are
     * stocked here.
     */
    private Ground ground;

    /**
     * Temporary variable used in the treatments.
     */
    private Surface groundForAlgorithm;

    /**
     * The mesh containing the noise during the treatments.
     */
    private Mesh noise;

    /**
     * The normal to the ground. Used to extract the grounds.
     */
    private Vector3d groundNormal;

    /**
     * Constructor. Stocks the mesh in the initialTotalMesh variable.
     * @param m
     *            the mesh representing the islet
     */
    public AbstractBuildingsIslet(final Mesh m)
    {
        super(m);
    }

    /**
     * Extracts buildings by separating the blocks after the ground extraction.
     */
    private void buildingsExtraction()
    {
        final List<Mesh> buildingList = new ArrayList<>();

        List<Mesh> thingsList;
        // Extraction of the buildings.
        thingsList = Algos.blockExtract(this.initialBuilding);

        // Steprithm : detection of buildings considering their size.
        for (final Mesh m : thingsList)
        {
            if (m.size() >= SeparationBuildings.BLOCK_BUILDING_SIZE_ERROR)
            {
                buildingList.add(m);
            } else
            {
                this.noise.addAll(m);
            }
        }

        if (buildingList.size() == 0)
        {
            System.out.println("Error : no building found !");
        }

        for (Mesh m : buildingList)
        {
            this.buildings.add(new Building(m));
        }
    }

    /**
     * Getter.
     * @return the list of buildings.
     */
    public final List<Building> getBuildings()
    {
        return this.buildings;
    }

    /**
     * Getter.
     * @return the ground.
     */
    public final Ground getGround()
    {
        return this.ground;
    }

    public int getProgression()
    {
        return this.progression;
    }

    /**
     * Extracts the grounds, using the groundExtract method.
     */
    private void groundExtraction()
    {
        // Searches for ground-oriented triangles with an error.
        Mesh meshOriented = this.initialTotalMesh.orientedAs(this.groundNormal,
                SeparationGroundBuilding.ANGLE_GROUND_ERROR);

        List<Mesh> thingsList;
        List<Mesh> groundsList = new ArrayList<>();
        // Extracts the blocks in the oriented triangles.
        thingsList = Algos.blockExtract(meshOriented);

        // FIXME : use MeshOriented.
        Mesh wholeGround = new Mesh();
        for (final Mesh f : thingsList)
        {
            wholeGround.addAll(f);
        }

        // We consider the altitude of the blocks on an axis parallel to the
        // normal ground.
        final double highDiff = this.initialTotalMesh.zMax()
                - this.initialTotalMesh.zMin();

        // Builds an axis normal to the current ground.
        final Edge axisNormalGround = new Edge(new Point(0, 0, 0), new Point(
                this.groundNormal.x, this.groundNormal.y, this.groundNormal.z));

        // Project the current whole ground centroid on this axis.
        final Point pAverage = axisNormalGround.project(wholeGround
                .getCentroid());

        // After this, for each block, consider the distance (on the
        // axisNormalGround) as an altitude distance. If it is greater than
        // the error, then it's not considered as ground.
        for (final Mesh m : thingsList)
        {
            final Point projectedPoint = axisNormalGround.project(m
                    .getCentroid());
            if (projectedPoint.getZ() < pAverage.getZ()
                    || projectedPoint.distance(pAverage) < highDiff
                            * SeparationGroundBuilding.ALTITUDE_ERROR)
            {

                groundsList.add(m);
            }
        }

        // We consider the size of the blocks : if they're big enough,
        // they're keeped. This is to avoid the parts of roofs, walls,
        // etc...
        thingsList = new ArrayList<>(groundsList);
        groundsList = new ArrayList<>();
        for (final Mesh m : thingsList)
        {
            if (m.size() > SeparationGrounds.BLOCK_GROUNDS_SIZE_ERROR)
            {
                groundsList.add(m);
            }
        }

        // Now that we found the real grounds, we extract the other
        // triangles
        // which are almost ground-oriented to add them.
        meshOriented = this.initialTotalMesh.orientedAs(this.groundNormal,
                SeparationGroundBuilding.LARGE_ANGLE_GROUND_ERROR);

        // If the new grounds are neighbours from the old ones, they are
        // added to the real grounds.
        thingsList = new ArrayList<>();
        for (final Mesh m : groundsList)
        {

            final Mesh temp = new Mesh(m);
            temp.addAll(meshOriented);
            final Mesh ret = new Mesh();
            m.getOne().returnNeighbours(ret, temp);
            meshOriented.remove(ret);
            thingsList.add(ret);
        }
        groundsList = thingsList;

        wholeGround = new Mesh();
        for (final Mesh f : groundsList)
        {
            wholeGround.addAll(f);
        }

        this.initialGround = new Ground(wholeGround);
    }

    /**
     * GroundNormal
     */
    public final void launchTreatment0()
    {
        try
        {
            this.changeBase();
        } catch (UnCompletedParametersException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * SeparationGroundBuilding
     */
    public final void launchTreatment1()
    {
        this.groundExtraction();
        this.initialBuilding = new Mesh(this.initialTotalMesh);
        this.initialBuilding.remove(this.initialGround);
    }

    /**
     * SeparationBuildings
     */
    public final void launchTreatment2()
    {
        this.noise = new Mesh();
        this.buildingsExtraction();

        this.ground = this.noiseTreatment();
    }

    /**
     * CarveWallsBetweenBuildings
     */
    public final void launchTreatment3()
    {
        // TODO : implement this method.
    }

    /**
     * SeparationWallRoof
     */
    public final void launchTreatment4()
    {
        for (Building b : this.getBuildings())
        {
            b.separateWallRoof(this.gravityNormal);
        }
    }

    /**
     * SeparationWallsAndSeparationRoofs
     */
    public final void launchTreatment6()
    {
        this.groundForAlgorithm = new Surface(this.ground);

        for (Building building : this.getBuildings())
        {
            building.cutWalls();
            building.cutRoofs(this.groundNormal);
            building.treatNoise();
            building.treatNewNeighbours(this.groundForAlgorithm);
        }
    }

    /**
     * DeterminateNeighbours
     */
    public final void launchTreatment7()
    {
        for (Building b : this.getBuildings())
        {
            b.determinateNeighbours(this.groundForAlgorithm);
        }
    }

    /**
     * SortNeighbours
     */
    public final void launchTreatment8()
    {
        for (Building b : this.getBuildings())
        {
            b.sortSurfaces();
            b.orderNeighbours(this.groundForAlgorithm);
        }
    }

    /**
     * RecomputationGround
     */
    public final void launchTreatment10()
    {
        for (Building b : this.getBuildings())
        {
            b.reComputeGroundBounds();
        }
    }

    /**
     * SimplificationSurfaces
     */
    public final void launchTreatment9()
    {
        for (Building b : this.getBuildings())
        {
            b.determinateContours(this.groundNormal);
        }
    }

    /**
     * Treats the noise by calling the method Algos.blockTreatNoise.
     * @return the ground of this islet
     */
    private Ground noiseTreatment()
    {
        List<Mesh> list = Algos.blockExtract(this.initialGround);
        return new Ground(Algos.blockTreatNoise(list, this.noise));
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to set as ground normal
     */
    public final void setGroundNormal(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }

    /**
     * Exception class used when an attribute has not been defined whereas the
     * algorithm has been launched.
     * @author Daniel Lefèvre
     */
    public final class VoidParameterException extends Exception
    {

        /**
         * Version ID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public VoidParameterException()
        {
        }
    }

    /**
     * Incrementer. Used to increment the progression of the treamtent.
     */
    public final void incProgression()
    {
        this.progression++;
    }

    /**
     * Getter.
     * @return the mesh representing the initial building
     */
    public final Mesh getInitialBuilding()
    {
        return this.initialBuilding;
    }

    /**
     * Getter.
     * @return the mesh representing the initial ground
     */
    public final Mesh getInitialGround()
    {
        return this.initialGround;
    }

    /**
     * Getter.
     * @return the normal to the ground
     */
    public final Vector3d getGroundNormal()
    {
        return this.groundNormal;
    }
}
