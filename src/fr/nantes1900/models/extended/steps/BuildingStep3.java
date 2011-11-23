package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.models.basis.Mesh;

/**
 * Implements a building step : a state of the building. This step is after the
 * separation of each buildings and before the separation between walls and
 * roofs.
 * @author Daniel Lefèvre
 */
public class BuildingStep3 extends AbstractBuildingStep
{

    /**
     * The initial total mesh representing the building.
     */
    private Mesh     initialTotalMesh;

    /**
     * The gravity normal.
     */
    private Vector3d gravityNormal;

    /**
     * Constructor.
     * @param mesh
     *            the mesh representing the entire building.
     */
    public BuildingStep3(final Mesh mesh)
    {
        this.initialTotalMesh = mesh;
    }

    /**
     * Getter.
     * @return the entire mesh
     */
    public final Mesh getInitialTotalMesh()
    {
        return this.initialTotalMesh;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#launchTreatment
     * ()
     */
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

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        // TODO : implement the method toString of the class Mesh
        return new DefaultMutableTreeNode(this.initialTotalMesh);
    }

    /**
     * Setter.
     * @param gravityNormalIn
     *            the gravity normal
     */
    public final void setArguments(final Vector3d gravityNormalIn)
    {
        this.gravityNormal = gravityNormalIn;
    }
}
