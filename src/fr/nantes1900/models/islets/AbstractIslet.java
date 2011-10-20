package fr.nantes1900.models.islets;

import java.io.IOException;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

public abstract class AbstractIslet {

    protected Mesh initialTotalMesh;

    /**
     * Change base matrix from the current base to a base which is ground-like
     * oriented.
     */
    protected double[][] matrix = new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

    protected Vector3d gravityNormal;

    public AbstractIslet(Mesh m) {
	this.initialTotalMesh = m;
    }

    /**
     * Parses a STL file. Uses the ParserSTL class.
     * 
     * @param fileName
     *            the name of the file
     * @return the mesh parsed
     */
    protected void parseFile(final String fileName) {

	try {
	    final ParserSTL parser = new ParserSTL(fileName);
	    this.initialTotalMesh = parser.read();

	} catch (final IOException e) {
	    System.out
		    .println("Error : the file is badly formed, not found or unreadable !");
	    System.exit(1);
	}
    }

    /**
     * Creates a change base matrix with the normal to the ground. See the
     * MatrixMethod class for more informations.
     * 
     * @param normalGround
     *            the vector to build the matrix.
     */
    protected void createChangeBaseMatrix() {

	try {
	    // Base change
	    this.matrix = MatrixMethod.createOrthoBase(this.gravityNormal);
	    MatrixMethod.changeBase(this.gravityNormal, this.matrix);

	} catch (final SingularMatrixException e) {
	    System.out.println("Error : the matrix is badly formed !");
	    System.exit(1);
	}
    }

    protected void changeBase() {
	// TODO : if there is no matrix, or no mesh...
	this.initialTotalMesh.changeBase(this.matrix);
    }
}
