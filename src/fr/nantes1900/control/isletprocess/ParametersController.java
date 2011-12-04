/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.constants.SimplificationSurfaces;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.ParametersView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class ParametersController {

    private ParametersView pView;
    private IsletProcessController parentController;
    private final static String[] parametersKeys = {
            TextsKeys.KEY_ALTITUDEERROR, TextsKeys.KEY_ANGLEGROUNDERROR,
            TextsKeys.KEY_LARGEANGLEGROUNDERROR,
            TextsKeys.KEY_BLOCKGROUNDSSIZEERROR,
            TextsKeys.KEY_BLOCKBUILDINGSIZE, TextsKeys.KEY_NORMALTOERROR,
            TextsKeys.KEY_LARGEANGLEERROR, TextsKeys.KEY_MIDDLEANGLEERROR,
            TextsKeys.KEY_PLANESERROR, TextsKeys.KEY_ROOFANGLEERROR,
            TextsKeys.KEY_ROOFSIZEERROR, TextsKeys.KEY_WALLANGLEERROR,
            TextsKeys.KEY_WALLSIZEERROR, TextsKeys.KEY_ISORIENTEDFACTOR};

    public ParametersController(IsletProcessController parentController) {
        this.setParent(parentController);
        this.pView = new ParametersView();
        this.pView.getLoadButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                ParametersFileChooser fileChooser = new ParametersFileChooser();
                fileChooser.setFileFilter(new ParametersFileFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

                if (fileChooser.showOpenDialog(ParametersController.this
                        .getView()) == JFileChooser.APPROVE_OPTION)
                {
                    File file = fileChooser.getSelectedFile();
                    if (file.isFile())
                    {
                        ParametersController.this.loadProperties(file);
                    }
                }
            }
        });
        this.pView.getSaveButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                ParametersFileChooser fileChooser = new ParametersFileChooser();
                fileChooser.setFileFilter(new ParametersFileFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

                if (fileChooser.showSaveDialog(ParametersController.this
                        .getView()) == JFileChooser.APPROVE_OPTION)
                {
                    File file = fileChooser.getSelectedFile();
                    ParametersController.this.saveProperties(file);
                }
            }
        });
        this.pView.getShowButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                // TODO
            }
        });
    }

    private void loadProperties(File file) {
        Properties parameters = FileTools.readProperties(file);

        for (int i = 0; i < parametersKeys.length; i++)
        {
            try
            {
                this.pView
                        .setValueProperty(i + 1,
                                Double.parseDouble(parameters.getProperty(
                                        parametersKeys[i], String.valueOf(pView
                                                .getValueProperty(i + 1)))));
            } catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(pView, FileTools
                        .readErrorMessage(
                                TextsKeys.KEY_ERROR_INCORRECTPARAMETER,
                                TextsKeys.MESSAGETYPE_MESSAGE), FileTools
                        .readErrorMessage(
                                TextsKeys.KEY_ERROR_INCORRECTPARAMETER,
                                TextsKeys.MESSAGETYPE_TITLE),
                        JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    private void saveProperties(File file) {
        Properties parameters = new Properties();
        for (int i = 0; i < parametersKeys.length; i++)
        {
            parameters.setProperty(parametersKeys[i],
                    String.valueOf(this.pView.getValueProperty(i + 1)));
        }
        FileTools.saveProperties(file, new Properties());
    }

    public void displayProcessingParameters(int i) {
        this.pView.displayParameters(i);
    }

    public IsletProcessController getParent() {
        return parentController;
    }

    public ParametersView getView() {
        return pView;
    }

    public void loadNewParameters() {
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
    }

    public void setParent(IsletProcessController parentController) {
        this.parentController = parentController;
    }

    public class ParametersFileChooser extends JFileChooser {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void approveSelection() {
            String absolutetPath = super.getSelectedFile().getAbsolutePath();

            ParametersFileFilter filter = (ParametersFileFilter) super
                    .getFileFilter();
            if (!absolutetPath.endsWith(filter.getExtension()))
            {
                absolutetPath += "." + filter.getExtension();
            }
            super.setSelectedFile(new File(absolutetPath));
            super.approveSelection();
        }
    }

    public class ParametersFileFilter extends FileFilter {

        private String extension = FileTools
                .readElementText(TextsKeys.KEY_FILEPARAMEXTENSION);
        private String description = FileTools
                .readElementText(TextsKeys.KEY_FILEPARAMDESCRIPTION);

        public boolean accept(File file) {
            return (file.isDirectory() || file.getName().endsWith(
                    this.extension));
        }

        public String getDescription() {
            return "." + this.extension + " - " + this.description;
        }

        public String getExtension() {
            return this.extension;
        }

    }

}
