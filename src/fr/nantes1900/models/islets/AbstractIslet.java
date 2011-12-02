package fr.nantes1900.models.islets;

import java.io.IOException;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.utils.ParserSTL;

/**
 * Implements an islet.
 * @author Daniel
 */
public abstract class AbstractIslet {

    /**
     * Parses a STL file. Uses the ParserSTL class.
     * @param fileName
     *            the name of the file
     * @return the mesh parsed
     * @throws IOException
     *             if the file is badly formed, not found or unreadable !
     */
    public static final Mesh parseFile(final String fileName)
            throws IOException {
        final ParserSTL parser = new ParserSTL(fileName);
        return parser.read();
    }
}
