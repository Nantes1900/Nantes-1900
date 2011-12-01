/**
 * 
 */
package fr.nantes1900.control;

import java.io.File;

import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.control.isletselection.IsletSelectionController;

/**
 * @author Camille
 */
public class GlobalController
{

    /**
     * The controller of the selection window.
     */
    private IsletSelectionController isletSelectionController;
    /**
     * TODO.
     */
    private IsletProcessController   isletProcessController;

    /**
     * Creates a new global controller which creates others global controllers.
     */
    public GlobalController()
    {
        this.isletSelectionController = new IsletSelectionController(this);
    }

    /**
     * TODO.
     */
    public final void launchIsletSelection()
    {
        this.isletSelectionController = new IsletSelectionController(this);
        this.isletProcessController.throwInTheBin();

    }

    /**
     * Launches the process of an islet and opens the new window.
     * @param isletFile
     *            The file containing data of the islet to process.
     * @param biController
     *            TODO.
     */
    public final void launchIsletProcess(final File isletFile,
            final BuildingsIsletController biController)
    {
        // Launches the base change : the treatmen 0.
        biController.launchProcess();

        this.isletProcessController = new IsletProcessController(this,
                isletFile, biController);
        this.isletSelectionController.getWindow().setVisible(false);
    }
}
