/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.constants.SimplificationSurfaces;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.utils.FileTools;

/**
 * @author Camille
 */
@SuppressWarnings("serial")
public class ParametersView extends JPanel {
    
    protected JLabel[] property;
    protected ValueProperty[] value;
    private final Dimension valueDimension = new Dimension(50, 30);
    private final Dimension labelDimension = new Dimension(140, 30);

    public ParametersView() {
        this.property = new JLabel[15];
        this.value = new ValueProperty[15];
        this.property[1] = new JLabel(FileTools.readElementText(TextsKeys.KEY_ALTITUDEERROR));        
        this.value[1] = new ValueProperty(
                SeparationGroundBuilding.getAltitureError());
        this.property[2] = new JLabel(FileTools.readElementText(TextsKeys.KEY_ANGLEGROUNDERROR));
        this.value[2] = new ValueProperty(
                SeparationGroundBuilding.getAngleGroundError());
        this.property[3] = new JLabel(FileTools.readElementText(TextsKeys.KEY_LARGEANGLEGROUNDERROR));
        this.value[3] = new ValueProperty(
                SeparationGroundBuilding.getLargeAngleGroundError());
        this.property[4] = new JLabel(FileTools.readElementText(TextsKeys.KEY_BLOCKGROUNDSSIZEERROR));
        this.value[4] = new ValueProperty(
                SeparationGroundBuilding.getBlockGroundsSizeError());
        this.property[5] = new JLabel(FileTools.readElementText(TextsKeys.KEY_BLOCKBUILDINGSIZE));
        this.value[5] = new ValueProperty(
                SeparationBuildings.getBlockBuildingSize());
        this.property[6] = new JLabel(FileTools.readElementText(TextsKeys.KEY_NORMALTOERROR));
        this.value[6] = new ValueProperty(SeparationWallRoof.getNormalToError());
        this.property[7] = new JLabel(FileTools.readElementText(TextsKeys.KEY_LARGEANGLEERROR));
        this.value[7] = new ValueProperty(
                SeparationWallsSeparationRoofs.getLargeAngleError());
        this.property[8] = new JLabel(FileTools.readElementText(TextsKeys.KEY_MIDDLEANGLEERROR));
        this.value[8] = new ValueProperty(
                SeparationWallsSeparationRoofs.getMiddleAngleError());
        this.property[9] = new JLabel(FileTools.readElementText(TextsKeys.KEY_PLANESERROR));
        this.value[9] = new ValueProperty(
                SeparationWallsSeparationRoofs.getPlanesError());
        this.property[10] = new JLabel(FileTools.readElementText(TextsKeys.KEY_ROOFANGLEERROR));
        this.value[10] = new ValueProperty(
                SeparationWallsSeparationRoofs.getRoofAngleError());
        this.property[11] = new JLabel(FileTools.readElementText(TextsKeys.KEY_ROOFSIZEERROR));
        this.value[11] = new ValueProperty(
                SeparationWallsSeparationRoofs.getRoofSizeError());
        this.property[12] = new JLabel(FileTools.readElementText(TextsKeys.KEY_WALLANGLEERROR));
        this.value[12] = new ValueProperty(
                SeparationWallsSeparationRoofs.getWallAngleError());
        this.property[13] = new JLabel(FileTools.readElementText(TextsKeys.KEY_WALLSIZEERROR));
        this.value[13] = new ValueProperty(
                SeparationWallsSeparationRoofs.getWallSizeError());
        this.property[14] = new JLabel(FileTools.readElementText(TextsKeys.KEY_ISORIENTEDFACTOR));
        this.value[14] = new ValueProperty(
                SimplificationSurfaces.getIsOrientedFactor());
        
        for (int i=1; i<=14; i++){
            this.property[i].setPreferredSize(this.labelDimension);
        }
  
        this.displayParameters(AbstractBuildingsIslet.FIRST_STEP);
        
    }

    private void displayOneParameter(int x, int y, int n) {
        this.add(this.property[n], new GridBagConstraints(x, y, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                new Insets(8, 8, 8, 8), 0, 5));
        this.add(this.value[n], new GridBagConstraints(x+1, y, 1, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                new Insets(8, 8, 8, 8), 0, 5));
    }

    public void displayParameters(int i) {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        switch (i) {
        case 1:
            this.displayOneParameter(0, 0, 1);
            this.displayOneParameter(0, 1, 2);
            this.displayOneParameter(0, 2, 3);
            this.displayOneParameter(0, 3, 4);
            break;
        case 2:
            this.displayOneParameter(0, 0, 5);
            break;
        case 3:
            this.displayOneParameter(0, 0, 6);
            break;
        case 4:
            this.displayOneParameter(0, 0, 7);
            this.displayOneParameter(0, 1, 8);
            this.displayOneParameter(0, 2, 9);
            this.displayOneParameter(0, 3, 10);
            this.displayOneParameter(0, 4, 11);
            this.displayOneParameter(0, 5, 12);
            this.displayOneParameter(0, 6, 13);
            break;
        case 5:
            this.displayOneParameter(0, 0, 14);break;
        default:
            break;
        }
    }

    public int getPreferredWidth() {
        // width of each + little margin
        return valueDimension.width + labelDimension.width + 10;
    }

    public double getValueProperty(int i) {
        return (double) (this.value[i]).getValue();
    }

    public class ValueProperty extends JFormattedTextField {
        public ValueProperty(double f) {
            super(new DecimalFormat());
            this.setValue(f);
            this.setPreferredSize(ParametersView.this.valueDimension);
        }
    }
}
