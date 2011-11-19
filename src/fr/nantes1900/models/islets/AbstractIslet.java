package fr.nantes1900.models.islets;

import java.io.IOException;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;
import fr.nantes1900.utils.ParserSTL;

/**
 * Implements an islet.
 * @author Daniel
 */
public abstract class AbstractIslet
{

    /**
     * The initial mesh before every treatment.
     */
    private Mesh       initialTotalMesh;

    /**
     * Change base matrix from the current base to a base which is ground-like
     * oriented.
     */
    private double[][] matrix = new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

    /**
     * The normal to the gravity.
     */
    private Vector3d   gravityNormal;

    /**
     * TODO.
     * @param m
     *            TODO.
     */
    public AbstractIslet(final Mesh m)
    {
        this.initialTotalMesh = m;
    }

    /**
     * TODO.
     * @throws UnCompletedParametersException
     *             TODO.
     */
    public final void changeBase() throws UnCompletedParametersException
    {
        if (this.matrix == null || this.initialTotalMesh == null)
        {
            throw new UnCompletedParametersException();
        }
        this.initialTotalMesh.changeBase(this.matrix);
    }

    /**
     * Creates a change base matrix with the normal to the ground. See the
     * MatrixMethod class for more informations.
     */
    public final void createChangeBaseMatrix()
    {
        try
        {
            // Base change
            this.matrix = MatrixMethod.createOrthoBase(this.gravityNormal);
            MatrixMethod.changeBase(this.gravityNormal, this.matrix);

        } catch (final SingularMatrixException e)
        {
            System.out.println("Error : the matrix is badly formed !");
            System.exit(1);
        }
    }

    /**
     * Getter.
     * @return the gravity normal
     */
    public final Vector3d getGravityNormal()
    {
        return this.gravityNormal;
    }

    /**
     * Getter.
     * @return the initial total mesh
     */
    public final Mesh getInitialTotalMesh()
    {
        return this.initialTotalMesh;
    }

    /**
     * Parses a STL file. Uses the ParserSTL class.
     * @param fileName
     *            the name of the file
     */
    public final void parseFile(final String fileName)
    {

        try
        {
            final ParserSTL parser = new ParserSTL(fileName);
            this.initialTotalMesh = parser.read();

        } catch (final IOException e)
        {
            System.out.println("Error : the file is badly formed, not found or unreadable !");
            System.exit(1);

        }
    }

    /**
     * Setter.
     * @param newNormal
     *            the new gravity normal
     */
    public final void setGravityNormal(final Vector3d newNormal)
    {
        this.gravityNormal = newNormal;
    }

    /**
     * Setter.
     * @param initialTotalMeshIn
     *            TODO.
     */
    public final void setInitialTotalMesh(final Mesh initialTotalMeshIn)
    {
        this.initialTotalMesh = initialTotalMeshIn;
    }

    /**
     * Implements an exception if parameters have not been completed.
     * @author Daniel
     */
    public final class UnCompletedParametersException extends Exception
    {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public UnCompletedParametersException()
        {
        }
    }
}
