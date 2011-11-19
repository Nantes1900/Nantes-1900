package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.models.basis.Mesh;

public class BuildingStep3 extends AbstractBuildingStep
{

    /**
     * TODO.
     */
    private Mesh     initialTotalMesh;

    private Vector3d gravityNormal;

    public BuildingStep3(Mesh mesh)
    {
        this.initialTotalMesh = mesh;
    }

    public Mesh getInitialTotalMesh()
    {
        return this.initialTotalMesh;
    }

    // SeparateWallRoof.
    @Override
    public final BuildingStep4 launchTreatment()
    {
        // Selects the triangles which are oriented normal to normalGround.
        Mesh initialWall = this.initialTotalMesh.orientedNormalTo(this.gravityNormal,
                SeparationWallRoof.getNormalToError());

        Mesh initialRoof = new Mesh(this.initialTotalMesh);
        initialRoof.remove(initialWall);

        return new BuildingStep4(initialWall, initialRoof);
    }

    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        // TODO : implement the method toString of the class Mesh
        return new DefaultMutableTreeNode(this.initialTotalMesh);
    }

    public final void setArguments(final Vector3d gravityNormalIn)
    {
        this.gravityNormal = gravityNormalIn;
    }

}
