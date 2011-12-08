package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToolBar;

import fr.nantes1900.control.isletprocess.IsletProcessController.UnexistingStepException;
import fr.nantes1900.view.isletprocess.NavigationBarView;

/**
 * Controller of the NavigationBarView : buttons to launch a new process, to
 * come back to the previous one, to abort the process, or to save the results.
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

        // Implements the back button.
        this.nbView.getBackButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                try {
                    NavigationBarController.this.getParentController()
                            .goToPreviousProcess();
                } catch (UnexistingStepException e) {
                    // TODO by Luc : pop-up.
                    e.printStackTrace();
                }
            }
        });

        // Implements the launch button.
        this.nbView.getLaunchButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                try {
                    NavigationBarController.this.getParentController()
                            .loadParameters();
                    NavigationBarController.this.getParentController()
                            .launchProcess();
                } catch (UnexistingStepException e) {
                    // TODO by Luc : pop-up.
                    e.printStackTrace();
                }
            }
        });

        // Implements the save button.
        this.nbView.getSaveButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                // FIXME by Camille : JFileChooser
                String fileName = "test.stl";
                NavigationBarController.this.getParentController()
                        .getBiController().saveFinalResults(fileName);
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

}
