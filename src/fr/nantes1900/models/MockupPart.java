package fr.nantes1900.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.nantes1900.constants.FilesNames;
import fr.nantes1900.models.islets.GroundIslet;
import fr.nantes1900.models.islets.IndustrialIslet;
import fr.nantes1900.models.islets.ResidentialIslet;
import fr.nantes1900.models.islets.SpecialBuildingIslet;
import fr.nantes1900.models.islets.WateryIslet;

/**
 * Implements a town containing five types of zones : industrials, residentials,
 * grounds, wateries, and special buildings. Contains all the algorithms to
 * parse and build a town, using the building, mesh, ground, and other classes.
 * Allows to write a CityGML file containing this town.
 * 
 * @author Daniel Lefevre
 */
public class MockupPart {

    /**
     * List of industrial zones.
     */
    private final List<IndustrialIslet> industrialIslets = new ArrayList<IndustrialIslet>();

    /**
     * List of residential zones.
     */
    private final List<ResidentialIslet> residentials = new ArrayList<ResidentialIslet>();

    /**
     * List of ground zones.
     */
    private final List<GroundIslet> groundIslets = new ArrayList<GroundIslet>();

    /**
     * List of watery zones.
     */
    private final List<WateryIslet> wateryIslets = new ArrayList<WateryIslet>();

    /**
     * List of special buildings.
     */
    private final List<SpecialBuildingIslet> specialBuildingIslets = new ArrayList<SpecialBuildingIslet>();

    public MockupPart(String directoryName) {

	// Create or clean the directory : directoryName + "/results/" to put
	// the datas in.
	MockupPart.cleanDirectory(directoryName + FilesNames.RESULT_DIRECTORY);
    }

    /**
     * Adds a ground to the attribute list of grounds.
     * 
     * @param groundIslet
     *            the ground to add
     */
    public final void addGround(final GroundIslet groundIslet) {
	if (!this.groundIslets.contains(groundIslet)) {
	    this.groundIslets.add(groundIslet);
	}
    }

    /**
     * Adds an industrial building to the attribute list of industrials.
     * 
     * @param industrialIslet
     *            the industrial to add
     */
    public final void addIndustrial(final IndustrialIslet industrialIslet) {
	if (!this.industrialIslets.contains(industrialIslet)) {
	    this.industrialIslets.add(industrialIslet);
	}
    }

    /**
     * Adds a list of buildings to the industrial list.
     * 
     * @param listBuildings
     *            the list of buildings to add
     */
    public final void addIndustrials(final List<IndustrialIslet> listIndustrials) {
	for (final IndustrialIslet b : listIndustrials) {
	    this.addIndustrial(b);
	}
    }

    /**
     * Adds a residential building to the attribute list of residentials.
     * 
     * @param building
     *            the residential to add
     */
    public final void addResidential(final ResidentialIslet residential) {
	if (!this.residentials.contains(residential)) {
	    this.residentials.add(residential);
	}
    }

    /**
     * Adds a special building to the attribute list of special buildings.
     * 
     * @param specialBuildingIslet
     *            the special building to add
     */
    public final void addSpecialBuilding(
	    final SpecialBuildingIslet specialBuildingIslet) {
	if (!this.specialBuildingIslets.contains(specialBuildingIslet)) {
	    this.specialBuildingIslets.add(specialBuildingIslet);
	}
    }

    /**
     * Adds a watery to the attribute list of wateries.
     * 
     * @param wateryIslet
     *            the watery to add
     */
    public final void addWatery(final WateryIslet wateryIslet) {
	if (!this.wateryIslets.contains(wateryIslet)) {
	    this.wateryIslets.add(wateryIslet);
	}
    }

    /**
     * Writes the town in a CityGML file. Uses the WriterCityGML.
     * 
     * @param fileName
     *            the name of the file to write in
     */
    public final void writeCityGML(final String fileName) {
	// TODO
    }

    /**
     * Adds a list of buildings to the residential list.
     * 
     * @param listBuildings
     *            the list of buildings to add
     */
    public void addResidentials(final List<ResidentialIslet> listResidential) {
	for (final ResidentialIslet b : listResidential) {
	    this.addResidential(b);
	}
    }

    /**
     * Cleans the directory in parameter, or creates it if it doesn't exist.
     * 
     * @param directoryName
     *            the name of the directory
     */
    private static void cleanDirectory(final String directoryName) {
	if (new File(directoryName).exists()) {

	    // List all the files of the directory
	    final File[] fileList = new File(directoryName).listFiles();

	    for (final File f : fileList) {
		// Delete only the files, and the empty directories. Then
		// it's safe to put something in a directory Archives for
		// example.
		f.delete();
	    }
	} else {
	    new File(directoryName).mkdir();
	}
    }
}
