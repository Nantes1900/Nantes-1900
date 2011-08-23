package fr.nantes1900.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Daniel Lefevre
 */
public class Configuration {

    // FIXME : doc
    private static String configFile;

    public static void setConfigFileName(String name) {
        Configuration.configFile = name;
        if (new File(name).exists()) {
            System.out.println("Configuration file found !");
        }
    }

    /**
     * Loads the coefficients from the configuration file, if it exists.
     */
    public static void loadCoefficients() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(Configuration.configFile));

            SeparationGroundBuilding.ALTITUDE_ERROR =
                new Double(properties.getProperty("ALTITUDE_ERROR"));
            SeparationGroundBuilding.ANGLE_GROUND_ERROR =
                new Double(properties.getProperty("ANGLE_GROUND_ERROR"));
            SeparationGroundBuilding.LARGE_ANGLE_GROUND_ERROR =
                new Double(properties.getProperty("LARGE_ANGLE_GROUND_ERROR"));
            SeparationGroundBuilding.BLOCK_BUILDING_SIZE_ERROR =
                new Double(properties.getProperty("BLOCK_BUILDING_SIZE_ERROR"));
            SeparationGroundBuilding.BLOCK_GROUNDS_SIZE_ERROR =
                new Double(properties.getProperty("BLOCK_GROUNDS_SIZE_ERROR"));

            SeparationTreatmentWallsRoofs.WALL_ANGLE_ERROR =
                new Double(properties.getProperty("WALL_ANGLE_ERROR"));
            SeparationTreatmentWallsRoofs.ROOF_ANGLE_ERROR =
                new Double(properties.getProperty("ROOF_ANGLE_ERROR"));
            SeparationTreatmentWallsRoofs.MIDDLE_ANGLE_ERROR =
                new Double(properties.getProperty("MIDDLE_ANGLE_ERROR"));
            SeparationTreatmentWallsRoofs.LARGE_ANGLE_ERROR =
                new Double(properties.getProperty("LARGE_ANGLE_ERROR"));
            SeparationTreatmentWallsRoofs.NORMALTO_ERROR =
                new Double(properties.getProperty("NORMALTO_ERROR"));
            SeparationTreatmentWallsRoofs.WALL_SIZE_ERROR =
                new Double(properties.getProperty("WALL_SIZE_ERROR"));
            SeparationTreatmentWallsRoofs.ROOF_SIZE_ERROR =
                new Double(properties.getProperty("ROOF_SIZE_ERROR"));
            SeparationTreatmentWallsRoofs.PLANES_ERROR =
                new Double(properties.getProperty("PLANES_ERROR"));

        } catch (FileNotFoundException e) {
            // If the file does not exist, keeps the actual coefficients.
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("The config.properties file is not complete !");
            System.exit(1);
        }
    }
}
