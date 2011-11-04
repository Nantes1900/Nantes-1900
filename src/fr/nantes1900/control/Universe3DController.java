package fr.nantes1900.control;

import fr.nantes1900.view.Universe3DView;

public class Universe3DController
{
    /**
     * The view of the 3D objets to show.
     */
    private Universe3DView u3DView;
    
    private IsletSelectionController parentController;
    
    public Universe3DController(
            IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        u3DView = new Universe3DView();
    }

    public Universe3DView getUniverse3DView()
    {
        return this.u3DView;
    }

}
