package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.isletprocess.IsletProcessController.UnexistingStepException;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.NavigationBarView;

/**
 * Controller of the NavigationBarView : buttons to LAUNCH a new process, to
 * come BACK to the previous one, to abort the process, or to SAVE the results.
 * Some informations are also displayed concerning the current step and process.
 * @author Camille Bouquet, Luc Jallerat
 */
public class NavigationBarController extends JToolBar {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The view associated.
     */
    private NavigationBarView nbView;

    /**
     * The parent controller.
     */
    private IsletProcessController parentController;

    /**
     * Constructor : defines the buttons listener and the actions.
     * @param parentControllerIn
     *            the parent controller
     */
    public NavigationBarController(
            final IsletProcessController parentControllerIn) {

        this.parentController = parentControllerIn;
        this.nbView = new NavigationBarView();

        // Implements the abort button.
        this.nbView.getAbortButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                NavigationBarController.this.getParentController()
                        .abortProcess();
            }
        });

        // Implements the BACK button.
        this.nbView.getBackButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                try {
                    NavigationBarController.this.getParentController()
                            .goToPreviousProcess();
                } catch (UnexistingStepException e) {
                    e.printStackTrace();
                }
            }
        });

        // Implements the LAUNCH button.
        this.nbView.getLaunchButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                try {
                    NavigationBarController.this.getParentController()
                            .loadParameters();
                    NavigationBarController.this.getParentController()
                            .launchProcess();
                } catch (UnexistingStepException e) {
                    e.printStackTrace();
                }
            }
        });

        // Implements the SAVE button.
        this.nbView.getSaveButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                ResultsFileChooser fileChooser = new ResultsFileChooser();
                fileChooser.setFileFilter(new ResultsFileFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

                if (fileChooser.showSaveDialog(NavigationBarController.this
                        .getView()) == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getPath();
                    NavigationBarController.this.getParentController()
                            .getBiController().saveFinalResults(fileName);
                }
            }
        });
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
     * @return the navigation bar view associated
     */
    public final NavigationBarView getView() {
        return this.nbView;
    }

    /**
     * File chooser for results files.
     * @author Camille Bouquet
     */
    public class ResultsFileChooser extends JFileChooser {

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

            ResultsFileFilter filter = (ResultsFileFilter) super
                    .getFileFilter();
            if (!absolutetPath.endsWith(filter.getExtension())) {
                absolutetPath += "." + filter.getExtension();
            }
            super.setSelectedFile(new File(absolutetPath));
            super.approveSelection();
        }
    }

    /**
     * Filter for results files.
     * @author Camille Bouquet
     */
    public class ResultsFileFilter extends FileFilter {

        /**
         * STL files extension.
         */
        private String extension = "stl";

        /**
         * STL file description.
         */
        private String description = FileTools
                .readElementText(TextsKeys.KEY_FILESTLDESCRIPTION);

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
