/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Camille
 */
public class ParametersView extends JPanel
{   //FIX ME
    //plein de trucs a fixer
    protected JLabel property1;
    protected ValueProperty bob1;
    
    public ParametersView(){
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.property1 = new JLabel("bob1");
        this.add(property1);
        this.bob1 = new ValueProperty((float)1.2);
        this.add(bob1);
    }
    
    public class ValueProperty extends JFormattedTextField
    {
        public ValueProperty(float f){
            super(new DecimalFormat());
            this.setForeground(Color.pink);
            this.setValue(f);
            this.setPreferredSize(new Dimension(50, 30));
            this.setEnabled(false);
        }
    }
}
