/**
 * 
 */
package fr.nantes1900.utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * TODO.
 * @author Camille Bouquet
 */
public final class FileTools
{

    /**
     * Text for the type of message status bar.
     */
    public static final String MESSAGETYPE_STATUSBAR = "Statusbar";
    /**
     * Text for the type of message tooltip for properties.
     */
    public static final String MESSAGETYPE_TOOLTIP = "Tooltip";
    /**
     * Text for the type of message title for properties.
     */
    public static final String MESSAGETYPE_TITLE = "Title";
    /**
     * Text for the type of message message for properties.
     */
    public static final String MESSAGETYPE_MESSAGE = "Message";
    /**
     * Key value corresponding to the open directory step.
     */
    public static final String KEY_IS_OPENDIRECTORY = "ISOpenDirectory";
    /**
     * Key value corresponding to the create gravity normal step.
     */
    public static final String KEY_IS_GRAVITYNORMAL = "ISGravityNormal";
    /**
     * Key value corresponding to the select islet to launch process step.
     */
    public static final String KEY_IS_LAUNCHPROCESS = "ISLaunchProcess";

    /**
     * TODO.
     * @param keyName
     *            TODO.
     * @param messageType
     *            TODO.
     * @return TODO.
     */
    public static String readHelpMessage(final String keyName,
            final String messageType)
    {
        return readProperty(new File("helpMessages.txt"), keyName + messageType);
    }

    /**
     * Reads a file containing properties and returns the properties object
     * associated with the file.
     * @param file
     *            The file to read data from.
     * @return read properties.
     */
    public static Properties readProperties(final File file)
    {
        FileReader fr;
        Properties prop = new Properties();

        try
        {
            fr = new FileReader(file);

            prop.load(fr);

            fr.close();

        } catch (FileNotFoundException e)
        {
            System.err.println("File wasn't found:" + e.getMessage());
        } catch (IOException e)
        {
            System.err.println("IO exception when loading properties: "
                    + e.getMessage());
        }

        return prop;
    }

    /**
     * Reads a property from the given file corresponding to the given key.
     * @param file
     *            The file to read the property from.
     * @param key
     *            The key corresponding to the property to read.
     * @return The property to read as a String. If the property wasn't found,
     *         the string is empty.
     */
    public static String readProperty(final File file, final String key)
    {
        Properties prop = readProperties(file);
        String readProperty = prop.getProperty(key, "");

        return readProperty;
    }

    /**
     * Saves or update new properties in the given file. If the file doesn't
     * exists, a new one is created.
     * @param file
     *            The file to save data in.
     * @param prop
     *            Data to save.
     * @return true - Data have been correctly saved.\n false - A problem has
     *         occurred during the saving process.
     */
    public static boolean
            saveProperties(final File file, final Properties prop)
    {
        boolean saveOk = true;
        DataOutputStream dos;
        try
        {
            dos = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file)));
            try
            {
                prop.store(dos, null); // 2nd parameter allows to store comments
            } catch (IOException e)
            {
                saveOk = false;
                System.err.println("IO exception during storage process: "
                        + e.getMessage());
            }
            try
            {
                dos.close();
            } catch (IOException e)
            {
                System.err.println("IO exeption while closing the stream: "
                        + e.getMessage());
            }
        } catch (FileNotFoundException e1)
        {
            saveOk = false;
            System.err.println("File wasn't found: " + e1.getMessage());
        }

        return saveOk;
    }

    /**
     * Private constructor.
     */
    private FileTools()
    {
    }
}
