/**
 * 
 */
package fr.nantes1900.control.isletprocess;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.constants.SimplificationSurfaces;
import fr.nantes1900.view.isletprocess.ParametersView;

/**
 * @author Camille
 */
// FIXME : Javadoc
public class ParametersController {

    private ParametersView pView;
    private IsletProcessController parentController;

    public ParametersController(IsletProcessController parentController) {
        this.setParent(parentController);
        this.pView = new ParametersView();
    }

    public void displayProcessingParameters(int i) {
        this.pView.displayParameters(i);
    }

    public IsletProcessController getParent() {
        return parentController;
    }

    public ParametersView getView() {
        return pView;
    }

    public void loadNewParameters() {
        SeparationGroundBuilding.setAltitureError(this.pView
                .getValueProperty(1));
        SeparationGroundBuilding.setAngleGroundError(this.pView
                .getValueProperty(2));
        SeparationGroundBuilding.setLargeAngleGroundError(this.pView
                .getValueProperty(3));
        SeparationGroundBuilding.setBlockGroundsSizeError(this.pView
                .getValueProperty(4));
        SeparationBuildings.setBlockBuildingSize(this.pView.getValueProperty(5));
        SeparationWallRoof.setNormalToError(this.pView.getValueProperty(6));
        SeparationWallsSeparationRoofs.setLargeAngleError(this.pView.getValueProperty(7));
        SeparationWallsSeparationRoofs.setMiddleAngleError(this.pView.getValueProperty(8));
        SeparationWallsSeparationRoofs.setPlanesError(this.pView.getValueProperty(9));
        SeparationWallsSeparationRoofs.setRoofAngleError(this.pView.getValueProperty(10));
        SeparationWallsSeparationRoofs.setRoofSizeError(this.pView.getValueProperty(11));
        SeparationWallsSeparationRoofs.setWallAngleError(this.pView.getValueProperty(12));
        SeparationWallsSeparationRoofs.setWallSizeError(this.pView.getValueProperty(13));
        SimplificationSurfaces.setIsOrientedFactor(this.pView.getValueProperty(14));
    }

    public void setParent(IsletProcessController parentController) {
        this.parentController = parentController;
    }
}
