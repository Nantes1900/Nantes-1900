/**
 * 
 */
package fr.nantes1900.control.isletselection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import fr.nantes1900.view.isletselection.ActionsView;

/**
 * @author Camille
 */
public class ActionsController
{
    /**
     * The panel containing buttons to launch the different actions.
     */
    private ActionsView              aView;

    /**
     * The parent controller to give feedback to.
     */
    private IsletSelectionController parentController;

    public IsletSelectionController getParentController()
    {
        return this.parentController;
    }

    /**
     * Creates a new controller to handle the panel containing buttons to launch
     * the different actions.
     * @param isletSelectionController
     */
    public ActionsController(IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        this.aView = new ActionsView();
        this.aView.getOpenButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

                if (fileChooser.showOpenDialog(ActionsController.this
                        .getActionsView()) == JFileChooser.APPROVE_OPTION)
                {
                    File file = fileChooser.getSelectedFile();
                    if (file.isDirectory())
                    {
                        ActionsController.this.getParentController()
                                .updateMockupDirectory(file);
                        aView.getHelpButton()
                                .setHelpMessage(
                                        "Choisissez un îlot à traiter dans l'arbre,\npuis sélectionnez un ensemble de triangle représentant la direction moyenn du sol.\nCliquez ensuite sur le bouton lancer pour lancer le traitement.",
                                        "Sélectionner un îlot à traiter");
                        aView.getHelpButton().setTooltip("Sélectionnez un îlot à traiter");
                    }
                }
            }

        });

        // FIXME : Where do you place the button to validate the normal ?
        // I suppose it is here.

        this.aView.getLaunchButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                // TODO : make the test for gravity normal
                // If no gravity normal have been choosen

                // If no ground normal have been choosen
                ActionsController.this.parentController.computeGroundNormal();

                // If every normals have been choosen
                ActionsController.this.getParentController()
                        .launchIsletTreatment();
            }
        });

    }

    /**
     * Returns the actions view associated with this controller.
     * @return The actions view.
     */
    public ActionsView getActionsView()
    {
        return this.aView;
    }
}
