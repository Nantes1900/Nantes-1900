package fr.nantes1900.models.islets.buildings;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationGrounds;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.models.middle.Surface;
import fr.nantes1900.utils.Algos;

public abstract class AbstractBuildingsIslet extends AbstractIslet
{

    public class UnimplementedException extends Exception
    {

        private static final long serialVersionUID = 1L;

    }

    protected List<Building> buildings   = new ArrayList<>();

    protected Mesh           initialBuilding;

    protected Mesh           initialGround;

    public int               progression = 0;

    protected Ground         ground;
    protected Surface        groundForAlgorithm;

    protected Mesh           noise;

    protected Vector3d       groundNormal;

    public AbstractBuildingsIslet(Mesh m)
    {
        super(m);
    }

    /**
     * Extracts buildings by extracting the blocks after the ground extraction.
     * @param Mesh
     *            the Mesh containing the buildinfs
     * @param noise
     *            the Mesh to stock the noise
     * @return a list of buildings as Meshes
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
     * Cut in a building zone the forms which are not buildings : little walls,
     * chimneys. Method not implemented.
     * @param buildingsList
     *            the list of buildings to treat
     * @return the list of the forms
     */
    private void carveRealBuildings(final List<Building> buildingsList)
    {
        // TODO : implement this method.
    }

    public List<Building> getBuildings()
    {
        return this.buildings;
    }

    public Ground getGround()
    {
        return this.ground;
    }

    public Surface getGroundForAlgorithm()
    {
        return this.groundForAlgorithm;
    }

    public Mesh getInitialBuilding()
    {
        return this.initialBuilding;
    }

    public Mesh getInitialGround()
    {
        return this.initialGround;
    }

    /**
     * Extracts the grounds, using the groundExtract method.
     * @param mesh
     *            the Mesh to extract from
     * @param groundNormal
     *            the normal to the ground (not the gravity-oriented normal, of
     *            course...)
     * @return a Mesh containing the ground
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
     * @throws UnimplementedException
     */
    public void launchStep0() throws UnimplementedException
    {
        if (this.matrix == null)
        {
            throw new UnimplementedException();
            // TODO
        }

        // Be careful to the normal you use.
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
     * @throws UnimplementedException
     */
    public void launchStep1() throws UnimplementedException
    {
        if (this.initialTotalMesh == null)
        {
            throw new UnimplementedException();
            // TODO
        }

        this.groundExtraction();
        this.initialBuilding = new Mesh(this.initialTotalMesh);
        this.initialBuilding.remove(this.initialGround);
    }

    /**
     * SeparationBuildings
     * @throws UnimplementedException
     */
    public void launchStep2() throws UnimplementedException
    {
        if (this.initialBuilding == null)
        {
            throw new UnimplementedException();
            // TODO
        }

        this.noise = new Mesh();
        this.buildingsExtraction();

        this.ground = this.noiseTreatment();
    }

    /**
     * CarveWallsBetweenBuildings
     */
    public void launchStep3()
    {
        this.carveRealBuildings(this.getBuildings());
    }

    /**
     * SeparationWallRoof
     */
    public void launchStep4()
    {
        for (Building b : this.getBuildings())
        {
            b.separateWallRoof(this.gravityNormal);
        }
    }

    /**
     * SeparationWallsAndSeparationRoofs
     */
    public void launchStep5()
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
    public void launchStep6()
    {
        for (Building b : this.getBuildings())
        {
            b.determinateNeighbours(this.groundForAlgorithm);
        }
    }

    /**
     * SortNeighbours
     */
    public void launchStep7()
    {
        for (Building b : this.getBuildings())
        {
            b.sortSurfaces();
            b.orderNeighbours(this.groundForAlgorithm);
        }
    }

    /**
     * SimplificationSurfaces
     */
    public void launchStep8()
    {
        for (Building b : this.getBuildings())
        {
            b.determinateContours(this.groundNormal);
        }
    }

    /**
     * RecomputationGround
     */
    public void launchStep9()
    {
        for (Building b : this.getBuildings())
        {
            b.reComputeGroundBounds();
        }
    }

    private Ground noiseTreatment()
    {
        List<Mesh> list = Algos.blockExtract(this.initialGround);
        return new Ground(Algos.blockTreatNoise(list, this.noise));
    }

    public void setBuildings(List<Building> buildingsIn)
    {
        this.buildings = buildingsIn;
    }

    public void setGroundNormal(Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }

}
