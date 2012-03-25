package fr.nantes1900.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import fr.nantes1900.constants.TextsKeys;

/**
 * Filter for results files.
 * @author Camille Bouquet
 */
public class ResultsFileFilter extends FileFilter {

    /**
     * Results files extension.
     */
    private String extension;

    /**
     * Results files description.
     */
    private String description;

    /**
     * Type of the writer associated with this file filter.
     */
    private int writerType;

    /**
     * Creates a new file filter associated with the writer type STL or CityGML.
     * @param writerTypeIn
     *            Type of the writer associated with this file filter. If
     *            unknown, sets to stl writer.
     */
    public ResultsFileFilter(final int writerTypeIn) {
        switch (writerTypeIn) {
        case AbstractWriter.STL_WRITER:
            this.extension = "stl";
            this.description = FileTools
                    .readElementText(TextsKeys.KEY_FILESTLDESCRIPTION);
            break;
        case AbstractWriter.CITYGML_WRITER:
            this.extension = "citygml";
            this.description = FileTools
                    .readElementText(TextsKeys.KEY_FILECITYGMLDESCRIPTION);
            break;
        default:
            System.err.println("Writer type unknown.");
            this.extension = "stl";
            this.description = FileTools
                    .readElementText(TextsKeys.KEY_FILESTLDESCRIPTION);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public final boolean accept(final File file) {
        return (file.isDirectory() || file.getName().endsWith(this.extension));
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public final String getDescription() {
        return "." + this.extension + " - " + this.description;
    }

    /**
     * Gets the extension of the filer.
     * @return the extension
     */
    public final String getExtension() {
        return this.extension;
    }

    /**
     * Return the type of writer chosen to save the data in a file.
     * Possible choices : AbstractWriter.STL_WRITER or AbstractWriter.CITYGML_WRITER
     * 
     * @return An integer linking to the writer type. The meaning of the value
     * is in the AbstractWriter class.
     */
    public final int getWriterType() {
        return this.writerType;
    }
}
