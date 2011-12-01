package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

public class CharacteristicsStep6View extends CharacteristicsView
{
    /**
     * default serial UID
     */
    private static final long serialVersionUID = 1L;
    private JList<Surface> lNeighbours;
    private DefaultListModel<Surface> dlm = new DefaultListModel<Surface>();
    private JButton upButton;
    private JButton downButton;
    private HelpButton helpButton;
    
    public CharacteristicsStep6View(ArrayList<Surface> neighbours)
    {
        super();
        // TODO modify with arrows
        upButton = new JButton("^");
        upButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selectedIndex = lNeighbours.getSelectedIndex();
                // TODO change to Surface when link ok
                Surface selectedSurface = lNeighbours.getSelectedValue();
                
                dlm.removeElement(selectedSurface);
                dlm.insertElementAt(selectedSurface, selectedIndex - 1);
                lNeighbours.setSelectedValue(selectedSurface, true);
            }
        });
        downButton = new JButton("v");
        downButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
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
            dlm.addElement(neighbour);
        }

        lNeighbours = new JList<Surface>(dlm);
        lNeighbours.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lNeighbours.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if (lNeighbours.getSelectedIndex() == 0)
                {
                    upButton.setEnabled(false);
                } else if (! upButton.isEnabled())
                {
                    upButton.setEnabled(true);
                }

                if (lNeighbours.getSelectedIndex() == dlm.getSize() - 1)
                {
                    downButton.setEnabled(false);
                }  else if (! downButton.isEnabled())
                {
                    downButton.setEnabled(true);
                }
                
                
            }
        });
            
        JScrollPane jsp = new JScrollPane(lNeighbours);
        jsp.setPreferredSize(new Dimension(100, 50));
        jsp.setMaximumSize(new Dimension(100, 50));
        jsp.setMinimumSize(new Dimension(100, 50));
        
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new GridBagLayout());
        sortPanel.add(jsp, new GridBagConstraints(0, 0,
                1, 2, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
        sortPanel.add(upButton, new GridBagConstraints(1, 0,
                1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        sortPanel.add(downButton, new GridBagConstraints(1, 1,
                1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        
        helpButton = new HelpButton();
        
        this.addCaracteristic(createSimpleCaracteristic(sortPanel, FileTools.readElementText(TextsKeys.KEY_SORTOUTNEIGHBOURS), helpButton));
        this.bValidate.setEnabled(true);
    }
    
    public void setList(ArrayList<Surface> neighbours)
    {
        dlm.removeAllElements();
        for (Surface neighbour : neighbours)
        {
            dlm.addElement(neighbour);
        }
    }

    public void setModificationsEnabled(boolean enable)
    {
        lNeighbours.setEnabled(enable);
        upButton.setEnabled(enable);
        downButton.setEnabled(enable);
        bValidate.setEnabled(enable);
        helpButton.setEnabled(enable);
    }
}
