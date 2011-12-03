package fr.nantes1900.models.extended.steps;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;

/**
 * Implements a building step : a state of the building. This step is after the
 * separation of each buildings and before the separation between walls and
 * roofs.
 * @author Daniel Lefèvre
 */
public class BuildingStep3 extends AbstractBuildingStep {

    /**
     * The initial total mesh representing the building.
     */
    private Surface initialTotalMesh;

    /**
     * The gravity normal.
     */
    private Vector3d gravityNormal;

    /**
     * Constructor.
     * @param mesh
     *            the mesh representing the entire building.
     */
    public BuildingStep3(final Mesh mesh) {
        this.initialTotalMesh = new Surface(mesh);
    }

    /**
     * Getter.
     * @return the entire mesh
     */
    public final Surface getInitialTotalSurface() {
        return this.initialTotalMesh;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#launchProcess ()
     */
    @Override
    public final BuildingStep4 launchProcess() throws NullArgumentException {
        if (this.gravityNormal == null) {
            throw new NullArgumentException();
        }

        // Selects the triangles which are oriented normal to normalGround.
        Surface initialWall = new Surface(this.initialTotalMesh.getMesh()
                .orientedNormalTo(this.gravityNormal,
                        SeparationWallRoof.getNormalToError()));

        // Copies the mesh and removes all the walls.
        Surface initialRoof = new Surface(new Mesh(
                this.initialTotalMesh.getMesh()));
        initialRoof.getMesh().remove(initialWall.getMesh());

        return new BuildingStep4(initialWall, initialRoof);
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode(final int counter) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Building"
                + counter);

        this.initialTotalMesh.setNodeString("Total surface");
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                this.initialTotalMesh);
        root.add(node);

        return root;
    }

    /**
     * Setter.
     * @param gravityNormalIn
     *            the gravity normal
     */
    public final void setArguments(final Vector3d gravityNormalIn) {
        this.gravityNormal = gravityNormalIn;
    }
}
