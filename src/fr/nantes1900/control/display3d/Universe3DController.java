package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;
import javax.swing.event.EventListenerList;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.display3d.MeshView;
<<<<<<< HEAD

=======
import fr.nantes1900.view.display3d.PolygonView;
>>>>>>> 2fffcf6ff167f39b50e46790361a22430d2c412f
import fr.nantes1900.view.display3d.Universe3DView;

// FIXME : Javadoc
/**
 * TODO.
 */
public class Universe3DController implements MouseListener, MouseMotionListener
{

    /**
     * The view of the 3D objets to show.
     */
    private Universe3DView           u3DView;

    /**
     * The canvas used to pick.
     */
    private PickCanvas               pickCanvas;
    /**
     * The controller to change the center of rotation.
     */
    private NewMouseRotate           mouseRotate;

    private IsletSelectionController parentController;

    private final EventListenerList  listeners            = new EventListenerList();
    /**
     * The MeshView selected each time.
     */
    private MeshView                 MeshView;

    public static final int          DISPLAY_MESH_MODE    = 1;

    public static final int          DISPLAY_POLYGON_MODE = 2;

    private int                      displayMode;

    private List<Triangle>           trianglesSelected    = new ArrayList<Triangle>();

    /**
     * @param isletSelectionController
     */
    public Universe3DController(
            IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
        this.u3DView = new Universe3DView(this);
    }

    /**
     * Getter.
     * @return the mouse rotate class
     */
    public NewMouseRotate getMouseRotate()
    {
        return this.mouseRotate;
    }

    /**
     * Returns the list of Triangle associated with the trianglesView contained
     * in trianglesViewSelected.
     * @return the list of selected triangles
     */
    public List<Triangle> getTrianglesSelected()
    {
        return this.trianglesSelected;
    }

    public Universe3DView getUniverse3DView()
    {
        return this.u3DView;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        int buttonDown = e.getButton();

        if (buttonDown == MouseEvent.BUTTON1)
        {
            // left mouse click
            this.pickCanvas.setShapeLocation(e);

            PickResult result = this.pickCanvas.pickClosest();
            if (result != null)
            {
                PickIntersection pickIntersection = result.getIntersection(0);

                GeometryArray meshView = pickIntersection.getGeometryArray();

                // // Get the MeshView selected.
                // this.MeshView = (MeshView) meshView;
                // Get the shape3D picked.
                Shape3D shapePicked = (Shape3D) result
                        .getNode(PickResult.SHAPE3D);
                // If shift is pressed down.
                if (e.isShiftDown())
                {
                    // If the shape3D picked is in the list of the shape3Ds
                    // selected,unselect this shape3D.
                    if (shape3DSelected.contains(shapePicked))
                    {
                        // Unselect this shape3D.Change the material to
                        // matUnSelect.
                        this.unselectShape3D(shapePicked);

                        // If the mesh picked is mesh of triangles.
                        if (meshView.getClass().getSimpleName()
                                .equals("MeshView"))
                        {
                            // Change the select condition of this mesh picked
                            // to
                            // unselect.
                            ((MeshView) meshView).unselectMesh();
                        }
                        // If the mesh picked is a polygon.
                        else
                        {
                            ((PolygonView) meshView).unselect();
                        }

                        // Remove this shape3D from the list of the shape3Ds
                        // selected.
                        shape3DSelected.remove(shapePicked);
                        // Remove this mesh from the list of the meshes
                        // selected.
                        meshSelected.remove(meshView);
                    }
                    // If the shape3D picked is not in the list of the shape3Ds
                    // selected,select this shape3D.
                    else
                    {
                        // Select this shape3D. Change the material to
                        // matSelect.
                        this.selectShape3D(shapePicked);
                        // If the mesh picked is a mesh of triangles.
                        if (meshView.getClass().getSimpleName()
                                .equals("MeshView"))
                        {
                            // Change the select condition of this mesh picked
                            // to
                            // select.
                            ((MeshView) meshView).selectMesh();
                            // Set the center ofrotation.
                            Point3d center = new Point3d(((MeshView) meshView)
                                    .getCentroid().getX(),
                                    ((MeshView) meshView).getCentroid().getY(),
                                    ((MeshView) meshView).getCentroid().getZ());
                            this.mouseRotate.setCenter(center);
                        }
                        // If the mesh picked is a polygon.
                        else
                        {
                            ((PolygonView) meshView).select();
                            // Set the center of rotation.
                            Point3d center = new Point3d(
                                    ((PolygonView) meshView).getCentroid()
                                            .getX(), ((PolygonView) meshView)
                                            .getCentroid().getY(),
                                    ((PolygonView) meshView).getCentroid()
                                            .getZ());
                            this.mouseRotate.setCenter(center);
                        }

                        // Add this shape3D to the list of the shape3Ds
                        // selected.
                        shape3DSelected.add(shapePicked);
                        // Add this mesh to the list of the meshes selected.
                        meshSelected.add(meshView);

                    }
                }

                // If the shift is not pressed down.
                else
                {
                    // If the shape3D picked is in the list of the shape3Ds
                    // selected.
                    if (shape3DSelected.contains(shapePicked))
                    {
                        // If the shape3D picked is not the only one in the list
                        // of the shape3Ds selected.
                        if (shape3DSelected.size() > 1)
                        {
                            // Unselect all the shape3D in the list of the
                            // shape3Ds selected.
                            for (Shape3D shapeSelected : shape3DSelected)
                            {
                                this.unselectShape3D(shapeSelected);
                            }
                            // Unselect all the meshes of triangles in the list
                            // of the meshes selected.
                            for (Object meshViewSelected : meshSelected)
                            {
                                // If the mesh in the list is a mesh of
                                // triangles.
                                if (meshViewSelected.getClass().getSimpleName()
                                        .equals("MeshView"))
                                {
                                    ((MeshView) meshViewSelected)
                                            .unselectMesh();
                                }
                                // If the mesh in the list is a polygon.
                                else
                                {
                                    ((PolygonView) meshViewSelected).unselect();
                                }

                            }
                            // Clear the list of the shape3Ds selected and add
                            // the shape3D picked to the list.
                            shape3DSelected.clear();
                            shape3DSelected.add(shapePicked);

                            // Clear the list of the meshes selected and add the
                            // mesh picked to the list.
                            meshSelected.clear();
                            meshSelected.add(meshView);

                            // Select this shape3D.Change the material to
                            // matSelect.
                            this.selectShape3D(shapePicked);

                            // If the mesh picked is a mesh of triangles,change
                            // the
                            // select condition of this mesh picked to
                            // select.
                            if (meshView.getClass().getSimpleName()
                                    .equals("MeshView"))
                            {
                                ((MeshView) meshView).selectMesh();
                                // Set the center of rotation.
                                Point3d center = new Point3d(
                                        ((MeshView) meshView).getCentroid()
                                                .getX(), ((MeshView) meshView)
                                                .getCentroid().getY(),
                                        ((MeshView) meshView).getCentroid()
                                                .getZ());
                                this.mouseRotate.setCenter(center);
                            }
                            // If the mesh picked is a polygon,change the select
                            // condition of this mesh picked to
                            // select.
                            else
                            {
                                ((PolygonView) meshView).select();
                                // Set the center of rotation.
                                Point3d center = new Point3d(
                                        ((PolygonView) meshView).getCentroid()
                                                .getX(),
                                        ((PolygonView) meshView).getCentroid()
                                                .getY(),
                                        ((PolygonView) meshView).getCentroid()
                                                .getZ());
                                this.mouseRotate.setCenter(center);
                            }
                        }
                        // If the shape3D picked is the only one in the list of
                        // the shape3Ds selected.
                        else
                        {
                            // Unselect this shape3D.Change the material to
                            // matUnSelect.
                            this.unselectShape3D(shapePicked);
                            // Clear the list of the shape3Ds selected.
                            shape3DSelected.clear();
                            // Clear the list of the meshes selected.
                            meshSelected.clear();
                            // If the mesh picked is a mesh of triangles,change
                            // the
                            // select condition of this mesh picked to
                            // unselect.
                            if (meshView.getClass().getSimpleName()
                                    .equals("MeshView"))
                            {
                                ((MeshView) meshView).unselectMesh();
                            }
                            // If the mesh picked is a polygon,change the select
                            // condition of this mesh picked to
                            // unselect.
                            else
                            {
                                ((PolygonView) meshView).unselect();
                            }
                        }
                    }
                    // If the shape3D picked is not in the list of the shape3Ds
                    // selected.
                    else
                    {
                        // Unselect all the shape3D in the list of the shape3Ds
                        // selected.
                        for (Shape3D shapeSelected : shape3DSelected)
                        {
                            this.unselectShape3D(shapeSelected);
                        }
                        // Unselect all the meshes in the list of
                        // the meshes selected.
                        for (Object meshViewSelected : meshSelected)
                        {
                            if (meshViewSelected.getClass().getSimpleName()
                                    .equals("MeshView"))
                            {
                                ((MeshView) meshViewSelected).unselectMesh();
                            } else
                            {
                                ((PolygonView) meshViewSelected).unselect();
                            }

                        }
                        // Clear the list of the shape3Ds selected and add the
                        // shape3D picked to the list.
                        shape3DSelected.clear();
                        shape3DSelected.add(shapePicked);
                        // Clear the list of the meshes selected and add the
                        // mesh picked to the list.
                        meshSelected.clear();
                        meshSelected.add(meshView);
                        // Select this shape3D.Change the material to matSelect.
                        this.selectShape3D(shapePicked);
                        // If the mesh picked is a mesh of triangles,change the
                        // select condition of this mesh picked to
                        // select.
                        if (meshView.getClass().getSimpleName()
                                .equals("MeshView"))
                        {
                            ((MeshView) meshView).selectMesh();
                            // Set the center of rotation.
                            Point3d center = new Point3d(((MeshView) meshView)
                                    .getCentroid().getX(),
                                    ((MeshView) meshView).getCentroid().getY(),
                                    ((MeshView) meshView).getCentroid().getZ());
                            this.mouseRotate.setCenter(center);
                        }
                        // If the mesh picked is a polygon,change the select
                        // condition of this mesh picked to
                        // select.
                        else
                        {
                            ((PolygonView) meshView).select();
                            // Set the center of rotation.
                            Point3d center = new Point3d(
                                    ((PolygonView) meshView).getCentroid()
                                            .getX(), ((PolygonView) meshView)
                                            .getCentroid().getY(),
                                    ((PolygonView) meshView).getCentroid()
                                            .getZ());
                            this.mouseRotate.setCenter(center);
                        }
                    }
                }

            }

        }

        else if (buttonDown == MouseEvent.BUTTON3)
        {

            // Right mouse click

            this.pickCanvas.setShapeLocation(e);
            PickResult result = this.pickCanvas.pickClosest();

            if (result != null)
            {
                // Get the triangle picked.
                PickIntersection pickIntersection = result.getIntersection(0);
                // Get the index of three points of the triangle picked.
                int[] PointIndex = pickIntersection.getPrimitiveVertexIndices();
                // Get the index of the triangle picked in the list of triangles
                // of the MeshView.
                int TriangleIndex = PointIndex[0] / 3;
                // Get the mesh picked.
                GeometryArray meshView = pickIntersection.getGeometryArray();
                // If the mesh picked is a mesh of triangles.
                if (meshView.getClass().getSimpleName().equals("MeshView"))
                {
                    this.MeshView = (MeshView) meshView;
                    // Get the shape3D picked.
                    Shape3D shapePicked = (Shape3D) result
                            .getNode(PickResult.SHAPE3D);
                    // If the shape3D picked is in the list of the shape3Ds
                    // selected.
                    if (shape3DSelected.contains(shapePicked) == true)
                    {
                        // If the triangle picked is not selected.
                        if (MeshView.getTriangleArray().get(TriangleIndex)
                                .isSelected() == false)
                        {
                            // Select the triangle picked.
                            MeshView.select(TriangleIndex);
                            // If the triangle picked in not in the list of
                            // triangles selected.
                            if (this.trianglesViewSelected
                                    .contains(this.MeshView.getTriangleArray()
                                            .get(TriangleIndex)) == false)
                            {
                                // Add the triangle picked to the list of
                                // triangles selected.
                                this.trianglesViewSelected.add(this.MeshView
                                        .getTriangleArray().get(TriangleIndex));
                                // Save the mesh of this triangle picked.
                                this.triangleMesh.add(MeshView);

                            }

                            // A list used to save the index of triangles
                            // selected in each turn of getting neighbours.
                            List<Integer> triangleNewSelected = new ArrayList<Integer>();
                            // Add the index of the triangle picked to the list.
                            triangleNewSelected.add(TriangleIndex);
                            // Times to search the neighbour.
                            int turn = 30;
                            // Select the neighbours of the triangle picked.
                            selectVoisin(TriangleIndex, triangleNewSelected,
                                    MeshView, turn);
                            // Set the center of rotation.
                            this.mouseRotate.setCenter(MeshView
                                    .getTriangleArray().get(TriangleIndex));
                            // Add the index of the last triangle selected in
                            // each time of getting neighbours.
                            this.selectedIndex.add(this.trianglesViewSelected
                                    .size() - 1);
                        }
                        // If the triangle picked is selected.
                        else
                        {

                            // If the triangle picked is in the list of
                            // triangles selected.
                            if (this.trianglesViewSelected
                                    .contains(this.MeshView.getTriangleArray()
                                            .get(TriangleIndex)))
                            {
                                // Get the index of the triangle picked in the
                                // list of triangles selected.
                                int triangleIndex = this.trianglesViewSelected
                                        .indexOf(this.MeshView
                                                .getTriangleArray().get(
                                                        TriangleIndex));
                                // The first index of the selection triangles
                                // which the triangle selected belongs to.
                                int firstIndex = 0;
                                // The last index of the selection triangles
                                // which the triagle selected belongs to.
                                int lastIndex = 0;
                                // The index of the last index in the list of
                                // index of last index.
                                int indexOfLastIndex = 0;

                                // Find the triangle picked belongs to which
                                // time of selection.
                                for (int i = 0; i < this.selectedIndex.size(); i++)
                                {
                                    // If the triangle picked belongs to the
                                    // first time of seleciton.
                                    if (i == 0
                                            && this.selectedIndex.get(i) >= triangleIndex)
                                    {
                                        firstIndex = -1;
                                        lastIndex = this.selectedIndex.get(i);
                                        break;
                                    }
                                    // If the triangle picked doesn't belong to
                                    // the first time of seleciton.
                                    else
                                    {
                                        if (this.selectedIndex.get(i) < triangleIndex
                                                && this.selectedIndex
                                                        .get(i + 1) >= triangleIndex)
                                        {
                                            firstIndex = this.selectedIndex
                                                    .get(i);
                                            lastIndex = this.selectedIndex
                                                    .get(i + 1);

                                            indexOfLastIndex = i + 1;
                                            break;
                                        }
                                    }
                                }

                                // Unselect all the triangles selected in the
                                // time of selection which the triangle picked
                                // belongs to.
                                for (int i = lastIndex; i > firstIndex; i--)
                                {
                                    TriangleView triangleSelected = this.trianglesViewSelected
                                            .get(i);
                                    int index = triangleSelected.getTriangle()
                                            .getTriangleViewIndex();
                                    this.MeshView.unselect(index);
                                    this.trianglesViewSelected.remove(i);

                                }

                                // Refresh the list of last index of triangles
                                // selected in each time of selection.
                                this.selectedIndex.remove(indexOfLastIndex);
                                for (int i = indexOfLastIndex; i < this.selectedIndex
                                        .size(); i++)
                                {
                                    int index = this.selectedIndex.get(i);
                                    this.selectedIndex.set(i, index
                                            - (lastIndex - firstIndex));
                                }
                            }
                        }
                    }
                }
            }
        }

        // click the wheel all the triangles selected will be canceled
        else if (buttonDown == MouseEvent.BUTTON2)
        {
            for (int i = 0; i < this.trianglesViewSelected.size(); i++)
            {
                int index = this.trianglesViewSelected.get(i).getTriangle()
                        .getTriangleViewIndex();
                this.triangleMesh.get(i).unselect(index);
            }
            this.trianglesViewSelected.clear();
            this.selectedIndex.clear();
            this.triangleMesh.clear();
        }

    }

    /**
     * Select the triangles around the triangle clicked.
     * @param TriangleIndex
     *            The index of the triangle clicked in the list of TriangleView.
     * @param triangleNewSelected
     *            The indexs of the triagles selected in each turn.
     * @param MeshView
     *            The MeshView selected.
     * @param turn
     *            The time of the recurrence to find the neighbour.
     */
    public void selectVoisin(int TriangleIndex,
            List<Integer> triangleNewSelected, MeshView MeshView, int turn)
    {

        Vector3d normal = MeshView.getTriangleArray().get(TriangleIndex)
                .getTriangle().getNormal();
        for (int i = 0; i < turn; i++)
        {
            List<Integer> triangleCount = new ArrayList<Integer>();
            for (int j : triangleNewSelected)
            {
                List<Triangle> triangleNeighbour = MeshView.getTriangleArray()
                        .get(j).getTriangle().getNeighbours();

                for (int l = 0; l < triangleNeighbour.size(); l++)
                {

                    int triangleNeighbourIndex = triangleNeighbour.get(l)
                            .getTriangleViewIndex();
                    if (this.trianglesViewSelected.contains(this.MeshView
                            .getTriangleArray().get(triangleNeighbourIndex)) == false)
                    {
                        Boolean isParalle;
                        isParalle = triangleNeighbour.get(l).isParalleTo(
                                normal, 0.5);

                        if (isParalle)
                        {
                            MeshView.select(triangleNeighbourIndex);
                            this.trianglesViewSelected.add(this.MeshView
                                    .getTriangleArray().get(
                                            triangleNeighbourIndex));

                            this.triangleMesh.add(this.MeshView);

                            triangleCount.add(triangleNeighbourIndex);
                        }

                    }

                }

            }
            triangleNewSelected.clear();
            for (int m = 0; m < triangleCount.size(); m++)
            {
                triangleNewSelected.add(triangleCount.get(m));
            }

            triangleCount.clear();
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0)
    {
        // Not used.
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        // Not used.
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // Empty.
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // Not used.
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // Not used.
    }

    /**
     * @param mouseRotate
     */
    public final void setMouseRotate(final NewMouseRotate mouseRotate)
    {
        this.mouseRotate = mouseRotate;
    }

    /**
     * @param branchGroup
     */
    public final void setPickCanvas(final BranchGroup branchGroup)
    {
        this.pickCanvas = new PickCanvas(this.u3DView.getSimpleUniverse()
                .getCanvas(), branchGroup);
        this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        this.u3DView.getSimpleUniverse().getCanvas().addMouseListener(this);
        this.u3DView.getSimpleUniverse().getCanvas()
                .addMouseMotionListener(this);

    }

    public void addElementsSelectedListener(ElementsSelectedListener listener)
    {
        listeners.add(ElementsSelectedListener.class, listener);
    }

    public void
            removeElementsSelectedListener(ElementsSelectedListener listener)
    {
        listeners.remove(ElementsSelectedListener.class, listener);
    }

    public void unselectShape3D(Shape3D shape)
    {

        Appearance app = shape.getAppearance();
        app.setMaterial(fr.nantes1900.view.display3d.MeshShowable.matUnSelected);
        shape.setAppearance(app);

    }

    /**
     * Change the material of the Shape3D
     * @param shape
     *            The Shape3D of the mesh selected.
     */
    public void selectShape3D(Shape3D shape)
    {

        Appearance app = shape.getAppearance();
        app.setMaterial(fr.nantes1900.view.display3d.MeshShowable.matSelected);
        shape.setAppearance(app);

    }

    /**
     * @param MeshView
     *            The triangle
     */
    public void selectMesh(MeshView MeshView)
    {

    }

    private void fireTriangleSelected(Triangle triangleSelected)
    {
        ElementsSelectedListener[] ESListeners = listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.triangleSelected(triangleSelected);
        }
    }

    private void firePolygonSelected(Polygon polygonSelected)
    {
        ElementsSelectedListener[] ESListeners = listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.polygonSelected(polygonSelected);
        }
    }

    private void fireTriangleDeselected(Triangle triangleDeselected)
    {
        ElementsSelectedListener[] ESListeners = listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.triangleSelected(triangleDeselected);
        }
    }

    private void firePolygonDeselected(Polygon polygonDeselected)
    {
        ElementsSelectedListener[] ESListeners = listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.polygonDeselected(polygonDeselected);
        }
    }

    public void selectMeshFromTree(Object mesh)
    {
        // Select the meshView.
        if (mesh.getClass().getSimpleName().equals("MeshView"))
        {
            ((MeshView) mesh).selectMesh();
        } else
        {
            ((PolygonView) mesh).select();
        }

        // Get the index of the meshView in the list of the meshes displayed.
        int meshViewIndex = meshList.indexOf(mesh);

        // Get the shape3D of the meshView to be selected.
        Shape3D shapeSelect = meshShape3D.get(meshViewIndex);
        Appearance appSelect = shapeSelect.getAppearance();

        // Change the material of the mesh to be seleted.
        appSelect.setMaterial(matSelected);
        shapeSelect.setAppearance(appSelect);

        // If the shape3D selected last time is not null.
        if (shape3DSelected != null)
        {

            // If the shape3D selected last time is not this one.
            if (shape3DSelected.contains(shapeSelect))
            {

                // Change back the material of the mesh selected last time.
                for (Shape3D shape : Universe3DController.shape3DSelected)
                {
                    Appearance appUnselect = shape.getAppearance();
                    appUnselect.setMaterial(matUnSelected);
                    shape.setAppearance(appUnselect);
                }

                // Unselect the meshView selected last time.
                for (Object meshObject : Universe3DController.meshSelected)
                {
                    if (meshObject.getClass().getSimpleName()
                            .equals("MeshView"))
                    {
                        ((MeshView) meshObject).unselectMesh();
                    } else
                    {
                        ((PolygonView) meshObject).unselect();
                    }
                }

            }
        }

        // Set the shape3D last selected to be the shape3D picked.
        shape3DSelected.clear();
        shape3DSelected.add(shapeSelect);

        // Set the meshView last selected to be the meshView picked.
        meshSelected.clear();
        meshSelected.add(mesh);
    }

    public int getDisplayMode()
    {
        return displayMode;
    }

    public void setDisplayMode(int displayMode)
    {
        this.displayMode = displayMode;
    }

    @Override
    public void mouseMoved(MouseEvent arg0)
    {
        // Not used.
    }
}
