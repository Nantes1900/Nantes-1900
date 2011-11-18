/**
 * 
 */
package fr.nantes1900.control.isletselection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
     *            TODO.
     */
    public ActionsController(final IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        this.aView = new ActionsView();
        this.aView.getOpenButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

                if (fileChooser.showOpenDialog(ActionsController.this.getActionsView()) == JFileChooser.APPROVE_OPTION)
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

        this.laListener = new LaunchActionListener(false);
        this.aView.getLaunchButton().addActionListener(this.laListener);
    }

    /**
     * Returns the actions view associated with this controller.
     * @return The actions view.
     */
    public final ActionsView getActionsView()
    {
        return this.aView;
    }

    /**
     * TODO.
     * @return TODO.
     */
    public final IsletSelectionController getParentController()
    {
        return this.parentController;
    }

    /**
     * TODO.
     */
    public final void setComputeNormalMode()
    {
        this.laListener.setComputeNormalMode(true);
        this.getActionsView()
                .getHelpButton()
                .setTooltip(FileTools.readHelpMessage(FileTools.KEY_IS_GRAVITYNORMAL,
                        FileTools.MESSAGETYPE_TOOLTIP));
        this.getActionsView()
                .getHelpButton()
                .setHelpMessage(FileTools.readHelpMessage(FileTools.KEY_IS_GRAVITYNORMAL,
                        FileTools.MESSAGETYPE_MESSAGE),
                        FileTools.readHelpMessage(FileTools.KEY_IS_GRAVITYNORMAL,
                                FileTools.MESSAGETYPE_TITLE));
        this.aView.getLaunchButton().setText("Sauver");
        this.aView.getLaunchButton().setEnabled(true);
    }

    /**
     * TODO.
     */
    public final void setLaunchMode()
    {
        this.laListener.setComputeNormalMode(false);
        this.getActionsView()
                .getHelpButton()
                .setTooltip(FileTools.readHelpMessage(FileTools.KEY_IS_LAUNCHPROCESS,
                        FileTools.MESSAGETYPE_TOOLTIP));
        this.getActionsView()
                .getHelpButton()
                .setHelpMessage(FileTools.readHelpMessage(FileTools.KEY_IS_LAUNCHPROCESS,
                        FileTools.MESSAGETYPE_MESSAGE),
                        FileTools.readHelpMessage(FileTools.KEY_IS_LAUNCHPROCESS,
                                FileTools.MESSAGETYPE_TITLE));
        this.aView.getLaunchButton().setText("Lancer");
        this.aView.getLaunchButton().setEnabled(true);
    }

    /**
     * TODO.
     * @author Daniel
     */
    public class LaunchActionListener implements ActionListener
    {

        /**
         * TODO.
         */
        private boolean computeNormal;

        /**
         * TODO.
         * @param computeNormalIn
         *            TODO
         */
        public LaunchActionListener(final boolean computeNormalIn)
        {
            this.computeNormal = computeNormalIn;
        }

        @Override
        public final void actionPerformed(final ActionEvent arg0)
        {
            // If no gravity normal have been choosen
            if (this.computeNormal)
            {
                boolean normalSaved = ActionsController.this.getParentController()
                        .computeGravityNormal();
                if (normalSaved)
                {
                    ActionsController.this.setLaunchMode();
                }
            } else
            {
                // If every normals have been choosen
                ActionsController.this.getParentController()
                        .launchIsletProcess();
            }
        }

        /**
         * TODO.
         * @param computeNormalIn
         *            TODO.
         */
        public final void setComputeNormalMode(final boolean computeNormalIn)
        {
            this.computeNormal = computeNormalIn;
        }
    }
}
