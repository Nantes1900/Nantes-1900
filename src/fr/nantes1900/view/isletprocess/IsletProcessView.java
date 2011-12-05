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

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.PFrame;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * Window to launch process on an islet in order to simplify it.
 * @author Camille Bouquet
 * @author Luc Jallerat
 */
public class IsletProcessView extends PFrame {

    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Current characteristics panel. Is modified contextually.
     */
    private CharacteristicsView cView;
    /**
     * Islet tree. Is Modified at each step and can be when characteristics
     * changed.
     */
    private IsletTreeView itView;
    /**
     * Navigation bar panel. Contains buttons to navigate through steps and
     * indications.
     */
    private NavigationBarView nbView;
    /**
     * Parameters panel. Each step needs parameters that the user can modify.
     */
    private ParametersView pView;
    /**
     * The 3D view of the islet.
     */
    private Universe3DView u3DView;
    /**
     * Tabs containing parameters panel and the tree.
     */
    private JTabbedPane tabs;

    /**
     * Scroll pane containing the parameter pane in order to add scroll bar if
     * there are size issues.
     */
    private JScrollPane jspParameters;

    /**
     * Constructs a new window with the compulsory elements.
     * @param caracteristicsView
     *            the characteristics panel
     * @param isletTreeView
     *            the islet tree
     * @param navigationBarView
     *            the navigation bar
     * @param parametersView
     *            the parameters panel
     * @param universe3dView
     *            the 3D view
     */
    public IsletProcessView(final CharacteristicsView caracteristicsView,
            final IsletTreeView isletTreeView,
            final NavigationBarView navigationBarView,
            final ParametersView parametersView,
            final Universe3DView universe3dView) {
        super();
        this.setMinimumSize(new Dimension(1140, 700));
        this.setPreferredSize(new Dimension(1200, 900));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.cView = caracteristicsView;
        this.itView = isletTreeView;
        this.nbView = navigationBarView;
        this.pView = parametersView;
        this.u3DView = universe3dView;
        this.jspParameters = new JScrollPane(pView);
        jspParameters
                .setMinimumSize(new Dimension(pView.getPreferredWidth(), 0));
        this.tabs = new JTabbedPane();
        this.tabs.addTab(FileTools.readElementText(TextsKeys.KEY_PARAMETERS),
                this.jspParameters);
        this.tabs.addTab(FileTools.readElementText(TextsKeys.KEY_TREEVIEW),
                this.itView);
        this.pComponents.setLayout(new GridBagLayout());

        this.pComponents
                .add(this.nbView, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.10,
                        GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL,
                        new Insets(10, 10, 5, 10), 0, 0));

        this.pComponents.add(this.tabs, new GridBagConstraints(0, 1, 1, 2,
                0.40, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 5, 5), 0, 0));
        this.pComponents.add(this.u3DView, new GridBagConstraints(1, 1, 1, 1,
                0.60, 0.75, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 10), 0, 0));
        this.pComponents.add(this.cView, new GridBagConstraints(1, 2, 1, 1,
                0.60, 0.15, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 10, 10), 0, 0));
        this.setStatusBarText(FileTools
                .readElementText(TextsKeys.KEY_STATUS_STEP1));
    }

    /**
     * Shows a new characteristics panel instead of the older one.
     * @param view
     *            the new characteristics panel
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
