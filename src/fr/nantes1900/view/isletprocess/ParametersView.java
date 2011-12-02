/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.nantes1900.constants.SeparationBuildings;
import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.constants.SeparationWallRoof;
import fr.nantes1900.constants.SeparationWallsSeparationRoofs;
import fr.nantes1900.constants.SimplificationSurfaces;

/**
 * @author Camille
 */
@SuppressWarnings("serial")
public class ParametersView extends JPanel { // FIX ME
                                             // plein de trucs a fixer
    protected JLabel property1;
    protected ValueProperty value1;
    protected JLabel property2;
    protected ValueProperty value2;
    protected JLabel property3;
    protected ValueProperty value3;
    protected JLabel property4;
    protected ValueProperty value4;
    protected JLabel property5;
    protected ValueProperty value5;
    protected JLabel property6;
    protected ValueProperty value6;
    protected JLabel property7;
    protected ValueProperty value7;
    protected JLabel property8;
    protected ValueProperty value8;
    protected JLabel property9;
    protected ValueProperty value9;
    protected JLabel property10;
    protected ValueProperty value10;
    protected JLabel property11;
    protected ValueProperty value11;
    protected JLabel property12;
    protected ValueProperty value12;
    protected JLabel property13;
    protected ValueProperty value13;
    protected JLabel property14;
    protected ValueProperty value14;
    private final Dimension valueDimension = new Dimension(50, 30);
    private final Dimension labelDimension = new Dimension(140, 30);

    public ParametersView() {
        this.property1 = new JLabel("AltitureError");
        this.property1.setPreferredSize(this.labelDimension);
        this.value1 = new ValueProperty(
                SeparationGroundBuilding.getAltitureError());
        this.property2 = new JLabel("AngleGroundError");
        this.property2.setPreferredSize(this.labelDimension);
        this.value2 = new ValueProperty(
                SeparationGroundBuilding.getAngleGroundError());
        this.property3 = new JLabel("LargeAngleGroundError");
        this.property3.setPreferredSize(this.labelDimension);
        this.value3 = new ValueProperty(
                SeparationGroundBuilding.getLargeAngleGroundError());
        this.property4 = new JLabel("BlockGroundsSizeError");
        this.property4.setPreferredSize(this.labelDimension);
        this.value4 = new ValueProperty(
                SeparationGroundBuilding.getBlockGroundsSizeError());
        this.property5 = new JLabel("BlockBuildingSize");
        this.property5.setPreferredSize(this.labelDimension);
        this.value5 = new ValueProperty(
                SeparationBuildings.getBlockBuildingSize());
        this.property6 = new JLabel("NormalToError");
        this.property6.setPreferredSize(this.labelDimension);
        this.value6 = new ValueProperty(SeparationWallRoof.getNormalToError());
        this.property7 = new JLabel("LargeAngleError");
        this.property7.setPreferredSize(this.labelDimension);
        this.value7 = new ValueProperty(
                SeparationWallsSeparationRoofs.getLargeAngleError());
        this.property8 = new JLabel("MiddleAngleError");
        this.property8.setPreferredSize(this.labelDimension);
        this.value8 = new ValueProperty(
                SeparationWallsSeparationRoofs.getMiddleAngleError());
        this.property9 = new JLabel("PlanesError");
        this.property9.setPreferredSize(this.labelDimension);
        this.value9 = new ValueProperty(
                SeparationWallsSeparationRoofs.getPlanesError());
        this.property10 = new JLabel("RoofAngleError");
        this.property10.setPreferredSize(this.labelDimension);
        this.value10 = new ValueProperty(
                SeparationWallsSeparationRoofs.getRoofAngleError());
        this.property11 = new JLabel("RoofSizeError");
        this.property11.setPreferredSize(this.labelDimension);
        this.value11 = new ValueProperty(
                SeparationWallsSeparationRoofs.getRoofSizeError());
        this.property12 = new JLabel("WallAngleError");
        this.property12.setPreferredSize(this.labelDimension);
        this.value12 = new ValueProperty(
                SeparationWallsSeparationRoofs.getWallAngleError());
        this.property13 = new JLabel("WallSizeError");
        this.property13.setPreferredSize(this.labelDimension);
        this.value13 = new ValueProperty(
                SeparationWallsSeparationRoofs.getWallSizeError());
        this.property14 = new JLabel("IsOrientedFactor");
        this.property14.setPreferredSize(this.labelDimension);
        this.value14 = new ValueProperty(
                SimplificationSurfaces.getIsOrientedFactor());

        this.displayParameters(1);
        this.enableChanges(1);
    }

    public void displayParameters(int i) {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        switch (i)
        {
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
            default:
            break;
        }
    }

    private void displayOneParameter(int x, int y, int n) {
        switch (n)
        {
            case 1:
                this.add(property1, new GridBagConstraints(x, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
                this.add(value1, new GridBagConstraints(x + 1, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
            break;
            case 2:
                this.add(property2, new GridBagConstraints(x, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
                this.add(value2, new GridBagConstraints(x + 1, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
            break;
            case 3:
                this.add(property3, new GridBagConstraints(x, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
                this.add(value3, new GridBagConstraints(x + 1, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
            break;
            case 4:
                this.add(property4, new GridBagConstraints(x, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
                this.add(value4, new GridBagConstraints(x + 1, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
            break;
            case 5:
                this.add(property5, new GridBagConstraints(x, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
                this.add(value5, new GridBagConstraints(x + 1, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
            break;
            case 6:
                this.add(property6, new GridBagConstraints(x, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
                this.add(value6, new GridBagConstraints(x + 1, y, 1, 1, 10.0,
                        10.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(8, 8, 8, 8),
                        0, 0));
            break;
            default:
            break;
        }
    }

    public void enableChanges(int i) {
        switch (i)
        {
            case 1:
                this.value1.setEnabled(true);
                this.value2.setEnabled(true);
                this.value3.setEnabled(true);
                this.value4.setEnabled(true);
            break;
            case 2:
                this.value5.setEnabled(true);
            break;
            case 3:
                this.value6.setEnabled(true);
            break;
            case 4:
                this.value7.setEnabled(true);
                this.value8.setEnabled(true);
                this.value9.setEnabled(true);
                this.value10.setEnabled(true);
                this.value11.setEnabled(true);
                this.value12.setEnabled(true);
                this.value13.setEnabled(true);
            break;
            case 5:
                this.value14.setEnabled(true);
            break;
            default:
            break;
        }
    }

    @SuppressWarnings("serial")
    public class ValueProperty extends JFormattedTextField {
        public ValueProperty(double f) {
            super(new DecimalFormat());
            this.setValue(f);
            this.setPreferredSize(ParametersView.this.valueDimension);
            this.setEnabled(false);
        }
    }

    public double getValueProperty1() {
        return (double) this.value1.getValue();
    }

    public double getValueProperty2() {
        return (double) this.value2.getValue();
    }

    public double getValueProperty3() {
        return (double) this.value3.getValue();
    }
    
    public int getPreferredWidth()
    {
        // width of each + little margin
        return valueDimension.width + labelDimension.width + 10;
    }
}
