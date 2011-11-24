/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToolBar;

import fr.nantes1900.view.isletprocess.NavigationBarView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class NavigationBarController extends JToolBar
{
    private NavigationBarView      nbView;
    private IsletProcessController parentController;

    public NavigationBarController(IsletProcessController parentController)
    {
        this.parentController = parentController;
        this.nbView = new NavigationBarView();
        this.nbView.getAbortButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                NavigationBarController.this.getParentController().getBiController().abortTreatment();
            }
        });
        this.nbView.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                NavigationBarController.this.getParentController().getBiController().lastTreatment();
            }
        });
        this.nbView.getLaunchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                NavigationBarController.this.getParentController().launchProcess();
            }
        });
    }

    public NavigationBarView getView()
    {
        return nbView;
    }
    public IsletProcessController getParentController(){
        return this.parentController;
    }

}
