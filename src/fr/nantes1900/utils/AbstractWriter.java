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
    String fileName;
    /**
     * The data source to be written. Takes a BuildingIsletStep with step>=4
     */
    Writable writable;
    /**
     * Intent to add Ground in the file
     */
    public static final boolean GROUND_ENABLED = true;
    /**
     * Intent not to add Ground in the file
     */
    public static final boolean GROUND_DISABLED = false;
    /**
     * The setting of GROUND. Use the two constants : GROUND_ENABLED or
     * GROUND_DISABLED
     */
    protected boolean groundSetting = AbstractWriter.GROUND_ENABLED;
    
    
	public abstract void write();
    
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Returns the value of the attribute GROUNDSETTING.
     *
     * @return the attribute GROUNDSETTING
     */
    public final boolean getGroundSetting() {
        return this.groundSetting;
    }

    /**
     * Changes the writing mode attribute : GROUNDSETTING.
     *
     * @param groundSetting the new setting
     */
    public final void setGroundSetting(final boolean groundSetting) {
        this.groundSetting = groundSetting;
    }
}
