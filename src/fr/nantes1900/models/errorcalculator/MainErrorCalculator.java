package fr.nantes1900.models.errorcalculator;

import java.io.IOException;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.utils.ParserSTL;

/**
 * Tool to use the ErrorCalculator in command line.
 * @author Daniel Lefevre
 */
public final class MainErrorCalculator {

    /**
     * Private constructor.
     */
    private MainErrorCalculator() {
        // Nothing.
    }

    /**
     * @param args
     *            the files containing the two meshes : the mesh before
     *            simplification first, and the simplified after
     * @throws IOException
     *             if something wrong happened in the parsing.
     */
    public static void main(final String[] args) throws IOException {
        ParserSTL parser = new ParserSTL(args[0]);
        Mesh mesh = parser.read();

        parser = new ParserSTL(args[1]);
        Mesh meshDecim = parser.read();

        ErrorCalculator eC = new ErrorCalculator(mesh, meshDecim);

        System.out.println(eC.computeError());
    }
}
