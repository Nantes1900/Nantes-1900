package fr.nantes1900.models.islets;

import java.io.IOException;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.utils.ParserSTL;

/**
 * Implements an islet.
 * @author Daniel
 */
public abstract class AbstractIslet
{

    /**
     * Parses a STL file. Uses the ParserSTL class.
     * @param fileName
     *            the name of the file
     * @return the mesh parsed
     */
    public static final Mesh parseFile(final String fileName)
    {
        try
        {
            final ParserSTL parser = new ParserSTL(fileName);
            return parser.read();

        } catch (final IOException e)
        {
            System.out.println("Error : the file is badly formed, not found or unreadable !");
            System.exit(1);
            return null;
        }
    }
}
