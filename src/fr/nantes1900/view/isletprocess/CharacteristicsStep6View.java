package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * TODO.
 * @author Camille Bouquet TODO .
 */
public class CharacteristicsStep6View extends CharacteristicsView
{
    /**
     * Default serial UID.
     */
    private static final long         serialVersionUID = 1L;
    /**
     * TODO .
     */
    private JList<Surface>            lNeighbours;
    /**
     * TODO .
     */
    private DefaultListModel<Surface> dlm              = new DefaultListModel<>();
    /**
     * TODO .
     */
    private JButton                   upButton;
    /**
     * TODO .
     */
    private JButton                   downButton;
    /**
     * TODO .
     */
    private HelpButton                helpButton;

    /**
     * TODO .
     * @param neighbours
     *            TODO .
     */
    public CharacteristicsStep6View(final List<Surface> neighbours)
    {
        super();
        // TODO modify with arrows
        this.upButton = new JButton("^");
        this.upButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                int selectedIndex = lNeighbours.getSelectedIndex();
                // TODO change to Surface when link ok
                Surface selectedSurface = lNeighbours.getSelectedValue();

                dlm.removeElement(selectedSurface);
                dlm.insertElementAt(selectedSurface, selectedIndex - 1);
                lNeighbours.setSelectedValue(selectedSurface, true);
            }
        });
        this.downButton = new JButton("v");
        this.downButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                int selectedIndex = lNeighbours.getSelectedIndex();
                // TODO change to Surface when link ok
                Surface selectedSurface = lNeighbours.getSelectedValue();

                dlm.removeElement(selectedSurface);
                dlm.insertElementAt(selectedSurface, selectedIndex + 1);
                lNeighbours.setSelectedValue(selectedSurface, true);
            }
        });

        for (Surface neighbour : neighbours)
        {
            this.dlm.addElement(neighbour);
        }

        this.lNeighbours = new JList<>(this.dlm);
        this.lNeighbours.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.lNeighbours.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if (lNeighbours.getSelectedIndex() == 0)
                {
                    upButton.setEnabled(false);
                } else if (!upButton.isEnabled())
                {
                    upButton.setEnabled(true);
                }

                if (lNeighbours.getSelectedIndex() == CharacteristicsStep6View.this.dlm
                        .getSize() - 1)
                {
                    downButton.setEnabled(false);
                } else if (!downButton.isEnabled())
                {
                    downButton.setEnabled(true);
                }

            }
        });

        JScrollPane jsp = new JScrollPane(this.lNeighbours);
        jsp.setPreferredSize(new Dimension(100, 50));
        jsp.setMaximumSize(new Dimension(100, 50));
        jsp.setMinimumSize(new Dimension(100, 50));

        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new GridBagLayout());
        sortPanel.add(jsp, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 10, 0, 10), 0, 0));
        sortPanel.add(this.upButton, new GridBagConstraints(1, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 10), 0, 0));
        sortPanel.add(this.downButton, new GridBagConstraints(1, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 10, 0, 10), 0, 0));

        this.helpButton = new HelpButton();

        this.addCaracteristic(createSimpleCaracteristic(sortPanel,
                FileTools.readElementText(TextsKeys.KEY_SORTOUTNEIGHBOURS),
                this.helpButton));
        this.bValidate.setEnabled(true);
    }

    /**
     * TODO .
     * @param neighbours
     *            TODO .
     */
    public final void setList(final ArrayList<Surface> neighbours)
    {
        this.dlm.removeAllElements();
        for (Surface neighbour : neighbours)
        {
            this.dlm.addElement(neighbour);
        }
    }

    /**
     * TODO .
     * @param enable
     *            TODO .
     */
    public final void setModificationsEnabled(final boolean enable)
    {
        this.lNeighbours.setEnabled(enable);
        this.upButton.setEnabled(enable);
        this.downButton.setEnabled(enable);
        this.bValidate.setEnabled(enable);
        this.helpButton.setEnabled(enable);
    }
}
