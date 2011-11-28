package fr.nantes1900.models.islets.buildings.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationGrounds;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;
import fr.nantes1900.utils.Algos;

/**
 * Implements a step of the treatment. This step is after the base change and
 * before the separation between grounds and buildings.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep1 extends AbstractBuildingsIsletStep
{

    /**
     * The initial total mesh after the base change.
     */
    private Surface  initialTotalMeshAfterBaseChange;
    /**
     * The normal to the ground.
     */
    private Vector3d groundNormal;

    /**
     * Constructor.
     * @param initialTotalMesh
     *            the mesh after the base change representing the total islet
     */
    public BuildingsIsletStep1(final Surface initialTotalMesh)
    {
        this.initialTotalMeshAfterBaseChange = initialTotalMesh;
    }

    /**
     * Getter.
     * @return the mesh
     */
    public final Surface getInitialTotalSurfaceAfterBaseChange()
    {
        return this.initialTotalMeshAfterBaseChange;
    }

    /**
     * Extracts the grounds, using the groundExtract method.
     * @return the ground extracted
     */
    private Ground groundExtraction()
    {
        // Searches for ground-oriented triangles with an error.
        Mesh meshOriented = this.initialTotalMeshAfterBaseChange.getMesh()
                .orientedAs(this.groundNormal,
                        SeparationGroundBuilding.getAngleGroundError());

        List<Mesh> thingsList;
        List<Mesh> groundsList = new ArrayList<>();
        // Extracts the blocks in the oriented triangles.
        thingsList = Algos.blockExtract(meshOriented);

        Mesh wholeGround = new Mesh();
        for (final Mesh f : thingsList)
        {
            wholeGround.addAll(f);
        }

        // We consider the altitude of the blocks on an axis parallel to the
        // normal ground.
        final double highDiff = this.initialTotalMeshAfterBaseChange.getMesh()
                .zMax() - this.initialTotalMeshAfterBaseChange.getMesh().zMin();

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
                            * SeparationGroundBuilding.getAltitureError())
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
            if (m.size() > SeparationGrounds.getBlockGroundsSizeError())
            {
                groundsList.add(m);
            }
        }

        // Now that we found the real grounds, we extract the other
        // triangles
        // which are almost ground-oriented to add them.
        meshOriented = this.initialTotalMeshAfterBaseChange.getMesh()
                .orientedAs(this.groundNormal,
                        SeparationGroundBuilding.getLargeAngleGroundError());

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

        return new Ground(wholeGround);
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #launchTreatment()
     */
    @Override
    public final BuildingsIsletStep2 launchTreatment()
            throws NullArgumentException
    {
        if (this.groundNormal == null)
        {
            throw new NullArgumentException();
        }
        Ground initialGround = this.groundExtraction();
        Surface initialBuildings = new Surface(new Mesh(
                this.initialTotalMeshAfterBaseChange.getMesh()));
        initialBuildings.getMesh().remove(initialGround.getMesh());
        return new BuildingsIsletStep2(initialBuildings, initialGround);
    }

    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                this.initialTotalMeshAfterBaseChange);
        return node;
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to the ground
     */
    public final void setArguments(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }
}
