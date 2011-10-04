package fr.nantes1900.constants;

import fr.nantes1900.models.extended.Town;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Implements a static configuration builder. Provides a method to load the
 * configuration file.
 * @author Daniel Lefevre
 */
public final class Configuration {

    /**
     * The name of the configuration file.
     */
    private static String configFile;

    /**
     * Private constructor.
     */
    private Configuration() {
    }

    /**
     * Loads the coefficients from the configuration file, if it exists.
     */
    public static void loadCoefficients() {
        final Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(Configuration.configFile));

            SeparationGroundBuilding.ALTITUDE_ERROR = new Double(
                    properties.getProperty("ALTITUDE_ERROR"));
            SeparationGroundBuilding.ANGLE_GROUND_ERROR = new Double(
                    properties.getProperty("ANGLE_GROUND_ERROR"));
            SeparationGroundBuilding.LARGE_ANGLE_GROUND_ERROR = new Double(
                    properties.getProperty("LARGE_ANGLE_GROUND_ERROR"));
            SeparationBuildings.BLOCK_BUILDING_SIZE_ERROR = new Double(
                    properties.getProperty("BLOCK_BUILDING_SIZE_ERROR"));
            SeparationGrounds.BLOCK_GROUNDS_SIZE_ERROR = new Double(
                    properties.getProperty("BLOCK_GROUNDS_SIZE_ERROR"));

            SeparationWallsSeparationRoofs.WALL_ANGLE_ERROR = new Double(
                    properties.getProperty("WALL_ANGLE_ERROR"));
            SeparationWallsSeparationRoofs.ROOF_ANGLE_ERROR = new Double(
                    properties.getProperty("ROOF_ANGLE_ERROR"));
            SeparationWallsSeparationRoofs.MIDDLE_ANGLE_ERROR = new Double(
                    properties.getProperty("MIDDLE_ANGLE_ERROR"));
            SeparationWallsSeparationRoofs.LARGE_ANGLE_ERROR = new Double(
                    properties.getProperty("LARGE_ANGLE_ERROR"));
            SeparationWallsRoofs.NORMALTO_ERROR = new Double(
                    properties.getProperty("NORMALTO_ERROR"));
            SeparationWallsSeparationRoofs.WALL_SIZE_ERROR = new Double(
                    properties.getProperty("WALL_SIZE_ERROR"));
            SeparationWallsSeparationRoofs.ROOF_SIZE_ERROR = new Double(
                    properties.getProperty("ROOF_SIZE_ERROR"));
            SeparationWallsSeparationRoofs.PLANES_ERROR = new Double(
                    properties.getProperty("PLANES_ERROR"));
            SimplificationWallsRoofs.IS_ORIENTED_FACTOR = new Double(
                    properties.getProperty("IS_ORIENTED_FACTOR"));

        } catch (final FileNotFoundException e) {
            // If the file does not exist, keeps the actual coefficients.
        } catch (final IOException e) {
            // If the file does not exist, keeps the actual coefficients.
        } catch (final NullPointerException e) {
            Town.LOG.severe("The config.properties file is not complete !");
            System.exit(1);
        }
    }

    /**
     * Setter.
     * @param name
     *            the name of the configuration file
     */
    public static void setConfigFileName(final String name) {
        Configuration.configFile = name;
        if (new File(name).exists()) {
            System.out.println("Configuration file found !");
        }
    }
}
