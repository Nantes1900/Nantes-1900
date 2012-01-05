package fr.nantes1900.models.islets.steps;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.constants.WeirdResultMessages;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.exceptions.NullArgumentException;
import fr.nantes1900.models.exceptions.WeirdResultException;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

/**
 * Implements a step of the process. This step is after the parsing and before
 * the base change.
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep0 extends AbstractBuildingsIsletStep {

    /**
     * The initial mesh before every process.
     */
    private Surface initialTotalSurface;

    /**
     * Change base matrix from the current base to a base which is ground-like
     * oriented.
     */
    private double[][] matrix = new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

    /**
     * The normal to the gravity.
     */
    private Vector3d gravityNormal;

    /**
     * Constructor.
     * @param mesh
     *            the mesh representing the entire islet
     */
    public BuildingsIsletStep0(final Mesh mesh) {
        this.initialTotalSurface = new Surface(mesh);
    }

    /**
     * Changes the base of the mesh.
     * @throws NullArgumentException
     *             if the matrix or the mesh has not been initialized
     */
    public final void changeBase() throws NullArgumentException {
        if (this.matrix == null || this.initialTotalSurface == null) {
            throw new NullArgumentException();
        }
        this.initialTotalSurface.getMesh().changeBase(this.matrix);
    }

    /**
     * Creates a change base matrix with the normal to the ground. See the
     * MatrixMethod class for more informations.
     * @throws WeirdResultException
     *             if the result is not coherent
     */
    public final void createChangeBaseMatrix() throws WeirdResultException {
        try {
            // Base change
            this.matrix = MatrixMethod.createOrthoBase(this.gravityNormal);
            MatrixMethod.changeBase(this.gravityNormal, this.matrix);

        } catch (final SingularMatrixException e) {
            throw new WeirdResultException(FileTools.readInformationMessage(
                    WeirdResultMessages.BAD_MATRIX,
                    TextsKeys.MESSAGETYPE_MESSAGE));
        }
    }

    /**
     * Getter.
     * @return the initial total mesh
     */
    public final Surface getInitialTotalSurface() {
        return this.initialTotalSurface;
    }

    /**
     * Getter.
     * @return the matrix
     */
    public final double[][] getMatrix() {
        return this.matrix;
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #launchProcess()
     */
    @Override
    public final BuildingsIsletStep1 launchProcess()
            throws NullArgumentException, WeirdResultException {
        if (this.gravityNormal == null) {
            throw new NullArgumentException();
        }
        this.createChangeBaseMatrix();
        this.changeBase();

        return new BuildingsIsletStep1(this.initialTotalSurface);
    }

    /*
     * (non-Javadoc)
     * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
     * #returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode() {
        this.initialTotalSurface.setNodeString("Surface totale");
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                this.initialTotalSurface);
        return node;
    }

    /**
     * Initialize some arguments needed for the treatement.
     * @param gravityNormalIn
     *            the normal to the gravity
     */
    public final void setArguments(final Vector3d gravityNormalIn) {
        this.gravityNormal = gravityNormalIn;
    }
}
