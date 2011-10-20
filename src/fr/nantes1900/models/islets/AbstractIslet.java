package fr.nantes1900.models.islets;

import java.io.IOException;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.middle.TriangleMesh;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

public abstract class AbstractIslet {

    protected TriangleMesh initialTotalMesh;

    /**
     * Change base matrix from the current base to a base which is ground-like
     * oriented.
     */
    protected double[][] matrix = new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

    public AbstractIslet(TriangleMesh m) {
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
    protected void createChangeBaseMatrix(final Vector3d normalGround) {

	try {
	    // Base change
	    this.matrix = MatrixMethod.createOrthoBase(normalGround);
	    MatrixMethod.changeBase(normalGround, this.matrix);

	} catch (final SingularMatrixException e) {
	    System.out.println("Error : the matrix is badly formed !");
	    System.exit(1);
	}
    }

    /**
     * Reads the ground file and returns the average normal as ground normal.
     * 
     * @param fileName
     *            the name of the ground file
     * @return the normal to the ground as Vector3d
     */
    protected Vector3d extractGroundNormal(final String fileName) {

	this.parseFile(fileName);

	// Extract of the normal of the ground
	return this.initialTotalMesh.averageNormal();
    }

    protected void changeBase() {
	// TODO
    }
}
