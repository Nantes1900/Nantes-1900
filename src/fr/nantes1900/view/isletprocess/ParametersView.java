/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
    private final Dimension labelDimension = new Dimension(130, 30);
    
    public ParametersView(){
        this.property1 = new JLabel("AltitureError");
        this.property1.setPreferredSize(this.labelDimension);
        this.value1 = new ValueProperty(SeparationGroundBuilding.getAltitureError());
        this.property2 = new JLabel("AngleGroundError");
        this.property2.setPreferredSize(this.labelDimension);
        this.value2 = new ValueProperty(SeparationGroundBuilding.getAngleGroundError());   
        this.property3 = new JLabel("LargeAngleGroundError");
        this.property3.setPreferredSize(this.labelDimension); 
        this.value3 = new ValueProperty(SeparationGroundBuilding.getLargeAngleGroundError());
        this.displayParameters(1);
    }
    
    private void displayParameters(int i){
        this.setLayout(new GridBagLayout());
        this.add(property1, new GridBagConstraints(0,0,1,1,10.0,10.0,GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(8,8,8,8),0,0));
        this.add(value1, new GridBagConstraints(1,0,1,1,10.0,10.0,GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(20,20,20,20),0,0));
        this.add(property2, new GridBagConstraints(0,1,1,1,10.0,10.0,GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(20,20,20,20),0,0));
        this.add(value2, new GridBagConstraints(1,1,1,1,10.0,10.0,GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(20,20,20,20),0,0));
        this.add(property3, new GridBagConstraints(0,2,1,1,10.0,10.0,GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(20,20,20,20),0,0));
        this.add(value3, new GridBagConstraints(1,2,1,1,10.0,10.0,GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL,
                new Insets(20,20,20,20),0,0));
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
