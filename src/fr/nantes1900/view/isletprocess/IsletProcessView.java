/**
 * 
 */
package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import fr.nantes1900.view.components.PFrame;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * @author Camille
 */
public class IsletProcessView extends PFrame {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * TODO .
     */
    private CharacteristicsView cView;
    /**
     * TODO .
     */
    private IsletTreeView itView;
    /**
     * TODO .
     */
    private NavigationBarView nbView;
    /**
     * TODO .
     */
    private ParametersView pView;
    /**
     * TODO .
     */
    private Universe3DView u3DView;
    /**
     * TODO .
     */
    private JTabbedPane tabs;

    private JScrollPane jspParameters;

    /**
     * TODO .
     * @param caracteristicsView
     *            TODO .
     * @param isletTreeView
     *            TODO .
     * @param navigationBarView
     *            TODO .
     * @param parametersView
     *            TODO .
     * @param universe3dView
     *            TODO .
     */
    public IsletProcessView(final CharacteristicsView caracteristicsView,
            final IsletTreeView isletTreeView,
            final NavigationBarView navigationBarView,
            final ParametersView parametersView,
            final Universe3DView universe3dView) {
        super();
        this.setMinimumSize(new Dimension(1100, 660));
        this.setPreferredSize(new Dimension(1200, 900));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.cView = caracteristicsView;
        this.itView = isletTreeView;
        this.nbView = navigationBarView;
        this.pView = parametersView;
        this.u3DView = universe3dView;
        this.jspParameters = new JScrollPane(pView);
        jspParameters.setMinimumSize(new Dimension(pView.getPreferredWidth(), 0));
        this.tabs = new JTabbedPane();
        this.tabs.addTab("Paramètres", this.jspParameters);
        this.tabs.addTab("Arbre", this.itView);
        this.pComponents.setLayout(new GridBagLayout());

        this.pComponents.add(this.nbView, new GridBagConstraints(0, 0, 2, 1,
                1.0, 0.20, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 5, 10), 0, 0));

        this.pComponents.add(this.tabs, new GridBagConstraints(0, 1, 1, 2,
                0.40, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 5, 5), 0, 0));
        this.pComponents.add(this.u3DView, new GridBagConstraints(1, 1, 1, 1,
                0.60, 0.60, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 10), 0, 0));
        this.pComponents.add(this.cView, new GridBagConstraints(1, 2, 1, 1,
                0.60, 0.20, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 10, 10), 0, 0));
    }

    /**
     * TODO .
     * @param view
     *            TODO .
     */
    public final void setCharacteristicsView(final CharacteristicsView view) {
        this.pComponents.remove(this.cView);
        this.cView = view;
        this.pComponents.add(this.cView, new GridBagConstraints(1, 2, 1, 1,
                0.60, 0.20, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 10, 10), 0, 0));

        this.pComponents.revalidate();
        this.pComponents.repaint();
    }
}
