/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JTabbedPane;

import fr.nantes1900.view.components.PFrame;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * @author Camille
 */
public class IsletProcessView extends PFrame
{

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    private CaracteristicsView cView;
    private IsletTreeView      itView;
    private NavigationBarView  nbView;
    private ParametersView     pView;
    private Universe3DView     u3DView;
    private JTabbedPane        tabs;

    public IsletProcessView(CaracteristicsView caracteristicsView,
            IsletTreeView isletTreeView, NavigationBarView navigationBarView,
            ParametersView parametersView, Universe3DView universe3dView)
    {
        super();
        this.setMinimumSize(new Dimension(800, 800));
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setLocationRelativeTo(null);

        this.cView = caracteristicsView;
        this.itView = isletTreeView;
        this.nbView = navigationBarView;
        this.pView = parametersView;
        this.u3DView = universe3dView;
        this.tabs = new JTabbedPane();
        this.tabs.addTab("Paramètres", pView);
        this.tabs.addTab("Arbre", itView);
        
        this.getComponentsPanel().setLayout(new GridBagLayout());
        
        this.getComponentsPanel().add(
                nbView,
                new GridBagConstraints(0, 0, 2, 1, 1.0, 0.20,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(10, 10, 5, 10), 0, 0));
        
        this.getComponentsPanel().add(
                this.tabs,
                new GridBagConstraints(0, 1, 1, 2, 0.40, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 10, 5, 5), 0, 0));
        this.getComponentsPanel().add(
                u3DView,
                new GridBagConstraints(1, 1, 1, 1, 0.60, 0.60,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 10), 0, 0));
        this.getComponentsPanel().add(
                cView,
                new GridBagConstraints(1, 2, 1, 1, 0.60, 0.20,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 5, 10, 10), 0, 0));
    }

    public void setCharacteristicsView(CaracteristicsView view)
    {
        this.cView = view;
        validate();
        repaint();
    }
}
