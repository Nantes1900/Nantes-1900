/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.Cursor;
import java.io.File;

import fr.nantes1900.constants.Characteristics;
import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.view.isletprocess.IsletProcessView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class IsletProcessController implements ElementsSelectedListener
{
    private GlobalController         parentController;
    private IsletProcessView         ipView;
    private CharacteristicsController cController;
    private IsletTreeController      itController;
    private NavigationBarController  nbController;
    private ParametersController     pController;
    private Universe3DController     u3DController;
    private BuildingsIsletController biController;
    private int                      progression;

    public IsletProcessController(GlobalController parentController,
            File isletFile, BuildingsIsletController biController)
    {
        this.parentController = parentController;
        this.progression = 1;
        this.biController = biController;
        this.cController = new CharacteristicsController(this);
        this.itController = new IsletTreeController(this);
        this.nbController = new NavigationBarController(this);
        this.pController = new ParametersController(this);
        this.u3DController = new Universe3DController();
        this.biController.setUniverse3DController(u3DController);
        this.biController.display();

        this.ipView = new IsletProcessView(cController.getView(),
                itController.getView(), nbController.getView(),
                pController.getView(), u3DController.getUniverse3DView());
        this.ipView.setVisible(true);
        this.u3DController.addElementsSelectedListener(this);
    }

    public BuildingsIsletController getBiController()
    {
        return this.biController;
    }

    public void launchProcess()
    {
        this.ipView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        System.out.println("launched");
        this.biController.launchTreatment();
        System.out.println("finished");
        this.progression++;

        this.itController.refreshView();

        this.ipView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Launches an action depending of the actual step with the given action
     * type.
     * @param actionType
     *            Type of the action to execute
     * @see ActionTypes, {@link BuildingsIsletController}
     */
    public void launchAction(int step, int actionType, int selectionMode)
    {
        switch (step)
        {
            case 2:
                try
                {
                    this.biController.action2(
                            ((CharacteristicsStep2Controller) cController).getTriangles(), actionType);
                } catch (InvalidCaseException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            break;
            
            case 3:
                if (selectionMode == Characteristics.SELECTION_TYPE_ELEMENT)
                {
                    try
                    {
                        this.biController.action3(
                                ((CharacteristicsStep3ElementsController) cController).getElement(), actionType);
                    } catch (InvalidCaseException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (selectionMode == Characteristics.SELECTION_TYPE_TRIANGLE)
                {
                    try
                    {
                        this.biController.action3(
                                ((CharacteristicsStep3TrianglesController) cController).getTriangles(), actionType);
                    } catch (InvalidCaseException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void triangleSelected(Triangle triangleSelected)
    {
        switch (progression)
        {
            case 2:
                // If the characteristic panel is of another type.
                if (!(this.cController instanceof CharacteristicsStep2Controller))
                {
                    this.cController = new CharacteristicsStep2Controller(this,
                            triangleSelected);
                    this.ipView.setCharacteristicsView(cController.getView());
                } else
                {
                    ((CharacteristicsStep2Controller) this.cController)
                            .addTriangleSelected(triangleSelected);
                }
            break;
            case 3:
                // If the characteristic panel is of another type.
                if (!(this.cController instanceof CharacteristicsStep3TrianglesController))
                {
                    this.cController = new CharacteristicsStep3TrianglesController(this,
                            triangleSelected);
                    this.ipView.setCharacteristicsView(cController.getView());
                } else
                {
                    ((CharacteristicsStep3TrianglesController) this.cController)
                    .addTriangleSelected(triangleSelected);
                }
                break;
        }
    }

    @Override
    public void polygonSelected(Polygon trianglesSelected)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void triangleDeselected(Triangle triangleSelected)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void polygonDeselected(Polygon trianglesSelected)
    {
        // TODO Auto-generated method stub

    }
}
