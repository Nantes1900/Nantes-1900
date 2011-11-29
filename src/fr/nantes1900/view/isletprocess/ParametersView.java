/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.nantes1900.constants.SeparationGroundBuilding;
import fr.nantes1900.control.isletprocess.NavigationBarController;

/**
 * @author Camille
 */
@SuppressWarnings("serial")
public class ParametersView extends JPanel
{   //FIX ME
    //plein de trucs a fixer
    protected JLabel property1;
    protected ValueProperty value1;
    protected JLabel property2;
    protected ValueProperty value2;
    protected JLabel property3;
    protected ValueProperty value3;
    private final Dimension valueDimension = new Dimension(50, 30);
    private final Dimension labelDimension = new Dimension(70, 30);
    
    public ParametersView(){
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.property1 = new JLabel("AltitureError");
        this.property1.setPreferredSize(this.labelDimension);
        this.add(property1);
        this.value1 = new ValueProperty(SeparationGroundBuilding.getAltitureError());
        this.add(value1);
        this.property2 = new JLabel("AngleGroundError");
        this.property2.setPreferredSize(this.labelDimension);
        this.add(property2);
        this.value2 = new ValueProperty(SeparationGroundBuilding.getAngleGroundError());
        this.add(value2);
        this.property3 = new JLabel("LargeAngleGroundError");
        this.property3.setPreferredSize(this.labelDimension);
        this.add(property3);
        this.value3 = new ValueProperty(SeparationGroundBuilding.getLargeAngleGroundError());
        this.add(value3);
    }
    
    @SuppressWarnings("serial")
    public class ValueProperty extends JFormattedTextField
    {
        public ValueProperty(double f){
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
}
