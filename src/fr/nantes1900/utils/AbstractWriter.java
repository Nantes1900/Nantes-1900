/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nantes1900.utils;

import fr.nantes1900.models.islets.steps.Writable;

/**
 * @author tagazok
 */
public abstract class AbstractWriter {

    /**
     * The name of the file to write
     */
    protected String fileName;
    /**
     * The data source to be written. Takes a BuildingIsletStep with step>=4
     */
    protected Writable writable;
    
    /**
     * Type of writer to use to write final data in file
     */
    public static final int STL_WRITER = 1;
    
    /**
     * Type of writer to use to write final data in file
     */
    public static final int CITYGML_WRITER = 1;

    /**
     * Generate the file content. Necessary to call it before write()
     */
    public abstract void makeFileFromWritable();

    /**
     * Write the content in the file
     */
    public abstract void write();

    /**
     * @return The name of file file to be written
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Set the name of the file to be written
     * @param fileName 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
