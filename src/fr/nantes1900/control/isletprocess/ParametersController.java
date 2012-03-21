package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.coefficients.Decimation;
import fr.nantes1900.models.coefficients.SeparationBuildings;
import fr.nantes1900.models.coefficients.SeparationGroundBuilding;
import fr.nantes1900.models.coefficients.SeparationWallRoof;
import fr.nantes1900.models.coefficients.SeparationWallsSeparationRoofs;
import fr.nantes1900.models.coefficients.SimplificationSurfaces;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.ParametersView;

/**
 * Implements the controller of the ParametersView. Displays the parameters for
 * each step and allow the user to modify it. Displays also a SAVE and a load
 * button to sav or load parameters, and a button launching a pop-UP showing
 * every parameters.
 * @author Luc Jallerat, Camille Bouquet
 */
public class ParametersController {

    /**
     * The view associated.
     */
    private ParametersView pView;
    /**
     * The parent controller.
     */
    private IsletProcessController parentController;
    /**
     * An array containing all the names of the parameters.
     */
    public static final String[] PARAMETERS_KEY = {
            TextsKeys.KEY_ALTITUDEERROR, TextsKeys.KEY_ANGLEGROUNDERROR,
            TextsKeys.KEY_LARGEANGLEGROUNDERROR,
            TextsKeys.KEY_BLOCKGROUNDSSIZEERROR,
            TextsKeys.KEY_BLOCKBUILDINGSIZE, TextsKeys.KEY_NORMALTOERROR,
            TextsKeys.KEY_LARGEANGLEERROR, TextsKeys.KEY_MIDDLEANGLEERROR,
            TextsKeys.KEY_PLANESERROR, TextsKeys.KEY_ROOFANGLEERROR,
            TextsKeys.KEY_ROOFSIZEERROR, TextsKeys.KEY_WALLANGLEERROR,
            TextsKeys.KEY_WALLSIZEERROR, TextsKeys.KEY_ISORIENTEDFACTOR,
            TextsKeys.KEY_DECIMATIONPERCENTAGE
    };

    /**
     * Constructor.
     * @param parentControllerIn
     *            the parent controller
     */
    public ParametersController(final IsletProcessController parentControllerIn) {

        this.parentController = parentControllerIn;

        this.pView = new ParametersView();

        // Loads parameters from a file.
        this.pView.getLoadButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                ParametersFileChooser fileChooser = new ParametersFileChooser();
                fileChooser.setFileFilter(new ParametersFileFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

                if (fileChooser.showOpenDialog(ParametersController.this
                        .getView()) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (file.isFile()) {
                        ParametersController.this.loadProperties(file);
                    }
                }
            }
        });

        // Saves parameters in a file.
        this.pView.getSaveButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                ParametersFileChooser fileChooser = new ParametersFileChooser();
                fileChooser.setFileFilter(new ParametersFileFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

                if (fileChooser.showSaveDialog(ParametersController.this
                        .getView()) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    ParametersController.this.saveProperties(file);
                }
            }
        });

        // Opens a pop-UP showing every parameters.
        this.pView.getShowButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                String parametersList = new String();

                for (int i = 1; i < ParametersController.this.pView
                        .getLengthProperty(); i++) {
                    parametersList += ParametersController.this.pView
                            .getNameProperty(i)
                            + "       "
                            + String.valueOf(pView.getValueProperty(i)) + "\n";
                }

                JOptionPane
                        .showMessageDialog(null, parametersList,
                                "Liste des paramètres",
                                JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Displays the parameter used in the step i.
     * @param i
     *            the number of the step
     */
    public final void displayProcessingParameters(final int i) {
        this.pView.displayParameters(i);
    }

    /**
     * Getter.
     * @return the parent controller
     */
    public final IsletProcessController getParentController() {
        return this.parentController;
    }

    /**
     * Getter.
     * @return the view associated
     */
    public final ParametersView getView() {
        return this.pView;
    }

    /**
     * Saves the new parameters after the user changes.
     */
    public final void loadNewParameters() {
        SeparationGroundBuilding.setAltitureError(this.pView
                .getValueProperty(1));
        SeparationGroundBuilding.setAngleGroundError(this.pView
                .getValueProperty(2));
        SeparationGroundBuilding.setLargeAngleGroundError(this.pView
                .getValueProperty(3));
        SeparationGroundBuilding.setBlockGroundsSizeError(this.pView
                .getValueProperty(4));
        SeparationBuildings
                .setBlockBuildingSize(this.pView.getValueProperty(5));
        SeparationWallRoof.setNormalToError(this.pView.getValueProperty(6));
        SeparationWallsSeparationRoofs.setLargeAngleError(this.pView
                .getValueProperty(7));
        SeparationWallsSeparationRoofs.setMiddleAngleError(this.pView
                .getValueProperty(8));
        SeparationWallsSeparationRoofs.setPlanesError(this.pView
                .getValueProperty(9));
        SeparationWallsSeparationRoofs.setRoofAngleError(this.pView
                .getValueProperty(10));
        SeparationWallsSeparationRoofs.setRoofSizeError(this.pView
                .getValueProperty(11));
        SeparationWallsSeparationRoofs.setWallAngleError(this.pView
                .getValueProperty(12));
        SeparationWallsSeparationRoofs.setWallSizeError(this.pView
                .getValueProperty(13));
        SimplificationSurfaces.setIsOrientedFactor(this.pView
                .getValueProperty(14));
        Decimation.setPercentDecimation(this.pView
        		.getValueProperty(15));
    }

    /**
     * Loads properties from a file.
     * @param file
     *            the file to load properties from.
     */
    public final void loadProperties(final File file) {
        Properties parameters = FileTools.readProperties(file);

        try {
            for (int i = 0; i < PARAMETERS_KEY.length; i++) {
                this.pView.setValueProperty(i + 1, Double
                        .parseDouble(parameters.getProperty(PARAMETERS_KEY[i],
                                String.valueOf(this.pView
                                        .getValueProperty(i + 1)))));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this.pView, FileTools
                    .readInformationMessage(
                            TextsKeys.KEY_ERROR_INCORRECTPARAMETER,
                            TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                    .readInformationMessage(
                            TextsKeys.KEY_ERROR_INCORRECTPARAMETER,
                            TextsKeys.MESSAGETYPE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves parameters in the file.
     * @param file
     *            the file to store parameters in
     */
    public final void saveProperties(final File file) {
        Properties parameters = new Properties();
        for (int i = 0; i < PARAMETERS_KEY.length; i++) {
            parameters.setProperty(PARAMETERS_KEY[i],
                    String.valueOf(this.pView.getValueProperty(i + 1)));
        }
        FileTools.saveProperties(file, parameters);
    }

    /**
     * Setter.
     * @param parentControllerIn
     *            the new parent controller
     */
    public final void
            setParent(final IsletProcessController parentControllerIn) {
        this.parentController = parentControllerIn;
    }

    /**
     * File chooser for parameters files.
     * @author Camille Bouquet
     */
    public class ParametersFileChooser extends JFileChooser {

        /**
         * Default serial UID.
         */
        private static final long serialVersionUID = 1L;

        /*
         * (non-Javadoc)
         * @see javax.swing.JFileChooser#approveSelection()
         */
        @Override
        public final void approveSelection() {
            String absolutetPath = super.getSelectedFile().getAbsolutePath();

            ParametersFileFilter filter = (ParametersFileFilter) super
                    .getFileFilter();
            if (!absolutetPath.endsWith(filter.getExtension())) {
                absolutetPath += "." + filter.getExtension();
            }
            super.setSelectedFile(new File(absolutetPath));
            super.approveSelection();
        }
    }

    /**
     * Filter for parameters files.
     * @author Camille Bouquet
     */
    public class ParametersFileFilter extends FileFilter {

        /**
         * Extension of parameters files.
         */
        private String extension = FileTools
                .readElementText(TextsKeys.KEY_FILEPARAMEXTENSION);
        /**
         * Description of the extension.
         */
        private String description = FileTools
                .readElementText(TextsKeys.KEY_FILEPARAMDESCRIPTION);

        /*
         * (non-Javadoc)
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        @Override
        public final boolean accept(final File file) {
            return (file.isDirectory() || file.getName().endsWith(
                    this.extension));
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
    }

}
