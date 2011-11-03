/**
 * 
 */
package fr.nantes1900.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import fr.nantes1900.view.ActionsView;

/**
 * @author Camille
 */
public class ActionsController
{
    /**
     * The panel containing buttons to launch the different actions.
     */
    private ActionsView aView;
    
    /**
     * The parent controller to give feedback to.
     */
    private IsletSelectionController parentController;

    /**
     * Creates a new controller to handle the panel containing buttons to launch
     * the different actions.
     * @param isletSelectionController 
     */
    public ActionsController(IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        aView = new ActionsView();
        aView.getOpenButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

                if (fileChooser.showOpenDialog(ActionsController.this.aView) == JFileChooser.APPROVE_OPTION)
                {
                    File file = fileChooser.getSelectedFile();
                    if (file.isDirectory())
                    {
                       parentController.updateMockupDirectory(file);
                    }
                }
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
