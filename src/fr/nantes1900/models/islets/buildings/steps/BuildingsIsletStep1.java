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
import fr.nantes1900.utils.Algos;

public class BuildingsIsletStep1 extends AbstractBuildingsIsletStep
{

    private Mesh initialTotalMeshAfterBaseChange;
    private Vector3d groundNormal;

    public BuildingsIsletStep1(Mesh initialMesh)
    {
        this.initialTotalMeshAfterBaseChange = initialMesh;
    }

    public Mesh getInitialTotalMeshAfterBaseChange()
    {
        return initialTotalMeshAfterBaseChange;
    }

    /**
     * Extracts the grounds, using the groundExtract method.
     * @return TODO.
     */
    private Ground groundExtraction()
    {
        // Searches for ground-oriented triangles with an error.
        Mesh meshOriented = this.initialTotalMeshAfterBaseChange.orientedAs(
                this.groundNormal,
                SeparationGroundBuilding.getAngleGroundError());

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
        final double highDiff = this.initialTotalMeshAfterBaseChange.zMax()
                - this.initialTotalMeshAfterBaseChange.zMin();

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
        meshOriented = this.initialTotalMeshAfterBaseChange.orientedAs(
                this.groundNormal,
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

    /**
     * TODO. SeparationGroundBuilding
     * @return TODO.
     */
    @Override
    public final BuildingsIsletStep2 launchTreatment()
    {
        // TODO : test if the setArguments has been correctly called.
        Ground initialGround = groundExtraction();
        // TODO : implement the method toString in the Mesh part();
        Mesh initialBuildings = new Mesh(this.initialTotalMeshAfterBaseChange);
        initialBuildings.remove(initialGround);
        return new BuildingsIsletStep2(initialBuildings, initialGround);
    }

    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        // TODO : create a method toString in the class Mesh to use it here.
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                this.initialTotalMeshAfterBaseChange);
        return node;
    }

    /**
     * TODO.
     * @param groundNormalIn
     *            TODO.
     */
    public final void setArguments(final Vector3d groundNormalIn)
    {
        this.groundNormal = groundNormalIn;
    }
}
