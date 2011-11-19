/**
 * 
 */
package fr.nantes1900.control;

import java.io.File;

import fr.nantes1900.control.BuildingsIsletController.InvalidCaseException;
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
        biController.launchTreatment();
        System.out.println("Traitement lancé sur l'îlot : " + isletFile.getPath());

        this.isletProcessController = new IsletProcessController(this,
                isletFile,
                biController);
        this.isletSelectionController.getWindow().setVisible(false);
    }
}
