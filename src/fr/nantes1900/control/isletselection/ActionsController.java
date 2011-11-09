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
    
    /**
     * Action listener of the launch button.
     */
    private LaunchActionListener    laListener;

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
                    }
                }
            }

        });

        laListener = new LaunchActionListener(false);
        this.aView.getLaunchButton().addActionListener(laListener);

    }

    /**
     * Returns the actions view associated with this controller.
     * @return The actions view.
     */
    public ActionsView getActionsView()
    {
        return this.aView;
    }

    public void setComputeNormalMode()
    {
        laListener.setComputeNormalMode(true);
        this.getActionsView().getHelpButton().setTooltip("Créez la normale orientée selon la gravité");
        this.getActionsView().getHelpButton().setHelpMessage("La normale orientée selon la gravité représente la verticale." +
        		"\nSélectionnez un ensemble de triangles dans l'îlot le plus approprié qui soient le plus plat possible." +
        		"\n\nCette normale est valable pour l'ensemble des mesures sur un même morceau." +
        		"\nVous n'avez donc besoin de la choisir qu'une fois par morceau de maquette.", "Choisir la normale orientée selon la gravité");
        this.aView.getLaunchButton().setText("Sauver");
    }
    
    public void setLaunchMode()
    {
        laListener.setComputeNormalMode(false);
        this.getActionsView().getHelpButton().setTooltip("Lancez le traitement");
        this.getActionsView().getHelpButton().setHelpMessage("Pour lancer le traitement, choisissez un ensemble de triangles qui représentent l'orientation moyenne du sol dans l'îlot à traiter." +
        		"\nUne fois ces triangles sélectionnés, cliquez sur le bouton lancer.", "Lancer un traitement");
    }
    
    public class LaunchActionListener implements ActionListener
    {
        private boolean computeNormal;
        
        public LaunchActionListener(boolean computeNormal)
        {
            this.computeNormal = computeNormal;
        }

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            // TODO : make the test for gravity normal
            // If no gravity normal have been choosen
            if (computeNormal)
            {
                ActionsController.this.parentController.computeGravityNormal();
            } else
            {
                // If every normals have been choosen
                ActionsController.this.parentController
                .launchIsletTreatment();
            }
        }
        
        public void setComputeNormalMode(boolean computeNormal)
        {
            this.computeNormal = computeNormal;
        }
    }
}
