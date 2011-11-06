package fr.nantes1900.control;

import java.util.ArrayList;

import fr.nantes1900.view.MeshView;
import fr.nantes1900.view.PolygonView;
import fr.nantes1900.view.TriangleView;
import fr.nantes1900.view.Universe3DView;

public class Universe3DController
{
    /**
     * The view of the 3D objets to show.
     */
    private Universe3DView           u3DView;

    @SuppressWarnings("unused")
    private IsletSelectionController parentController;

    private ArrayList<MeshView>      meshesSelected;
    private ArrayList<TriangleView>  trianglesSelected;
    private ArrayList<PolygonView>   polygonSelected;

    public Universe3DController(
            IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        this.u3DView = new Universe3DView();
    }

    public Universe3DView getUniverse3DView()
    {
        return this.u3DView;
    }

    public ArrayList<MeshView> getMeshesSelected()
    {
        return this.meshesSelected;
    }

    public ArrayList<TriangleView> getTrianglesSelected()
    {
        return this.trianglesSelected;
    }

    public ArrayList<PolygonView> getPolygonSelected()
    {
        return this.polygonSelected;
    }
}
