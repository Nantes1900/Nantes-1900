/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.io.File;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.view.isletprocess.IsletProcessView;

/**
 * @author Camille
 *
 */
public class IsletProcessController
{
    private GlobalController parentController;
    private IsletProcessView ipView;
    private CaracteristicsController cController;
    private IsletTreeController itController;
    private NavigationBarController nbController;
    private ParametersController pController;
    private Universe3DController u3DController;
    
    public IsletProcessController(GlobalController parentController, File isletFile, BuildingsIsletController biController)
    {
        this.parentController = parentController;
        
        this.cController = new CaracteristicsController(this);
        this.itController = new IsletTreeController(this);
        this.nbController = new NavigationBarController(this);
        this.pController = new ParametersController(this);
        this.u3DController = new Universe3DController();
        
        this.ipView = new IsletProcessView(cController.getView(), itController.getView(), nbController.getView(), pController.getView(), u3DController.getUniverse3DView());
        this.ipView.setVisible(true);
    }
}
