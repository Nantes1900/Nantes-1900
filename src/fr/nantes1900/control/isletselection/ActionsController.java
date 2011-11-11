/**
 * 
 */
package fr.nantes1900.control.isletselection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import fr.nantes1900.utils.FileTools;
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
    private LaunchActionListener     laListener;

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

    public IsletSelectionController getParentController()
    {
        return this.parentController;
    }

    public void setComputeNormalMode()
    {
        laListener.setComputeNormalMode(true);
        this.getActionsView()
                .getHelpButton()
                .setTooltip(
                        FileTools.readHelpMessage(
                                FileTools.KEY_IS_GRAVITYNORMAL,
                                FileTools.MESSAGETYPE_TOOLTIP));
        this.getActionsView()
                .getHelpButton()
                .setHelpMessage(
                        FileTools.readHelpMessage(
                                FileTools.KEY_IS_GRAVITYNORMAL,
                                FileTools.MESSAGETYPE_MESSAGE),
                        FileTools.readHelpMessage(
                                FileTools.KEY_IS_GRAVITYNORMAL,
                                FileTools.MESSAGETYPE_TITLE));
        this.aView.getLaunchButton().setText("Sauver");
        this.aView.getLaunchButton().setEnabled(true);
    }

    public void setLaunchMode()
    {
        laListener.setComputeNormalMode(false);
        this.getActionsView()
                .getHelpButton()
                .setTooltip(
                        FileTools.readHelpMessage(
                                FileTools.KEY_IS_LAUNCHPROCESS,
                                FileTools.MESSAGETYPE_TOOLTIP));
        this.getActionsView()
                .getHelpButton()
                .setHelpMessage(
                        FileTools.readHelpMessage(
                                FileTools.KEY_IS_LAUNCHPROCESS,
                                FileTools.MESSAGETYPE_MESSAGE),
                        FileTools.readHelpMessage(
                                FileTools.KEY_IS_LAUNCHPROCESS,
                                FileTools.MESSAGETYPE_TITLE));
        this.aView.getLaunchButton().setText("Lancer");
        this.aView.getLaunchButton().setEnabled(true);
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
            // If no gravity normal have been choosen
            if (computeNormal)
            {
                boolean normalSaved = ActionsController.this.parentController
                        .computeGravityNormal();
                if (normalSaved)
                {
                    ActionsController.this.setLaunchMode();
                }
            } else
            {
                // If every normals have been choosen
                ActionsController.this.parentController.launchIsletTreatment();
            }
        }

        public void setComputeNormalMode(boolean computeNormal)
        {
            this.computeNormal = computeNormal;
        }
    }
}
