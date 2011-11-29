package fr.nantes1900.view.isletprocess;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

public class CharacteristicsStep6View extends CharacteristicsView
{
    /**
     * default serial UID
     */
    private static final long serialVersionUID = 1L;
    private JList<String> lNeighbours;
//    private JList<Surface> lNeighbours;
    
    public CharacteristicsStep6View()
    {
        super();
        DefaultListModel dlm = new DefaultListModel();
        JList list = new JList(dlm);
        dlm.addElement("un");
        dlm.addElement("deux");
        dlm.addElement("deux");
        dlm.addElement("deux");
        dlm.addElement("deux");
        dlm.addElement("deux");
        JScrollPane jsp = new JScrollPane(list);
        jsp.setPreferredSize(new Dimension(100, 50));
        jsp.setMaximumSize(new Dimension(100, 50));
        jsp.setMinimumSize(new Dimension(100, 50));
        
        this.addCaracteristic(createSimpleCaracteristic(jsp, FileTools.readElementText(TextsKeys.KEY_SORTOUTNEIGHBOURS), new HelpButton()));
        this.bValidate.setEnabled(true);
//        lNeighbours = new JList<Surface>();
//        this.addCaracteristic(createSimpleCaracteristic(lNeighbours, FileTools.readElementText(TextsKeys.KEY_SORTOUTNEIGHBOURS), new HelpButton()));
//        this.bValidate.setEnabled(true);
    }
}
