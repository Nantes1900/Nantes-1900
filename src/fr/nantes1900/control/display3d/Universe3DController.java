package fr.nantes1900.control.display3d;

import java.util.ArrayList;
import java.util.List;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.PolygonView;
import fr.nantes1900.view.display3d.TriangleView;
import fr.nantes1900.view.display3d.Universe3DView;

public class Universe3DController
{
    /**
     * The view of the 3D objets to show.
     */
    private Universe3DView           u3DView;

    private IsletSelectionController parentController;

    private ArrayList<MeshView>      meshesViewSelected;
    private ArrayList<TriangleView>  trianglesViewSelected;
    private ArrayList<PolygonView>   polygonsViewSelected;

    public Universe3DController(
            IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        this.u3DView = new Universe3DView();
    }

    /**
     * Returns the list of Triangle associated with the trianglesView contained
     * in trianglesViewSelected.
     * @return
     */
    public List<Triangle> getTrianglesSelected()
    {
        List<Triangle> trianglesList = new ArrayList<>();
        // by Nicolas and Siju
        return trianglesList;
    }

    public Universe3DView getUniverse3DView()
    {
        return this.u3DView;
    }
}
