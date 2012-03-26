package fr.nantes1900.utils;

import fr.nantes1900.models.islets.steps.Writable;

/**
 * The abstract writer used to save data in a file. Enables the possibility to
 * add new writers without changing the rest of the code.
 * Implemented writers have to inherit the AbstractWriter.
 * 
 * @author Nicolas REY
 */
public abstract class AbstractWriter {

    /**
     * The name of the file to write.
     */
    protected String fileName;
    /**
     * The data source to be written. Takes a BuildingIsletStep with step>=4
     */
    protected Writable writable;

    /**
     * Type of writer to use to write final data in file.
     */
    public static final int STL_WRITER = 0;

    /**
     * Type of writer to use to write final data in file.
     */
    public static final int CITYGML_WRITER = 1;

    /**
     * Generate the file content. Necessary to call it before write()
     */
    public abstract void makeFileFromWritable();

    /**
     * Write the content in the file.
     */
    public abstract void write();

    /**
     * Getter
     * @return The name of file file to be written
     */
    public final String getFileName() {
        return this.fileName;
    }

    /**
     * Setter
     * Set the name of the file to be written.
     * @param fileNameIn
     *            The name of file file to be written
     */
    public final void setFileName(final String fileNameIn) {
        this.fileName = fileNameIn;
    }
}
