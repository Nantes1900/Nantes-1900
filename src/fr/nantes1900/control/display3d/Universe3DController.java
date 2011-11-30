package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.swing.event.EventListenerList;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.SurfaceView;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * TODO.
 */
/**
 * @author Daniel
 */
public class Universe3DController implements MouseListener, MouseMotionListener
{

    /**
     * The Universe3DView linked to this controller.
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

    /**
     * The parent controller.
     */
    private ElementsSelectedListener parentController;

    private final EventListenerList  listeners            = new EventListenerList();

    public static final int          DISPLAY_MESH_MODE    = 1;

    public static final int          DISPLAY_POLYGON_MODE = 2;

    private int                      displayMode;

    private List<Triangle>           trianglesSelected    = new ArrayList<>();

    private List<Surface>            surfacesSelected     = new ArrayList<>();

    /**
     * @param isletSelectionController
     */
    public Universe3DController(ElementsSelectedListener parentControllerIn)
    {
        this.parentController = parentControllerIn;
        this.u3DView = new Universe3DView(this);
        this.displayMode = DISPLAY_MESH_MODE;
    }

    public void addElementsSelectedListener(ElementsSelectedListener listener)
    {
        this.listeners.add(ElementsSelectedListener.class, listener);
    }

    /**
     * Changes the rotation center when clicking on a surface.
     * @param surfaceView
     *            The surfaceView becomming the rotation center
     */
    public final void changeRotationCenter(final SurfaceView surfaceView)
    {
        // FIXME handle the case of mesh = null.
        Point center = new Point(
                surfaceView.getMeshView().getCentroid().getX(), surfaceView
                        .getMeshView().getCentroid().getY(), surfaceView
                        .getMeshView().getCentroid().getZ());
        this.mouseRotate.setCenter(center);
    }

    public final void deselectMeshFromTree(Surface surface)
    {
        SurfaceView surfaceView = this.getSurfaceViewFromSurface(surface);

        // Changes the material of the surface seleted.
        surfaceView.getAppearance()
                .setMaterial(SurfaceView.MATERIAL_UNSELECTED);
    }

    private void firePolygonDeselected(Polygon polygonDeselected)
    {
        ElementsSelectedListener[] ESListeners = this.listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.polygonDeselected(polygonDeselected);
        }
    }

    private void firePolygonSelected(Polygon polygonSelected)
    {
        ElementsSelectedListener[] ESListeners = this.listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.polygonSelected(polygonSelected);
        }
    }

    private void fireTriangleDeselected(Triangle triangleDeselected)
    {
        ElementsSelectedListener[] ESListeners = this.listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.triangleSelected(triangleDeselected);
        }
    }

    private void fireTriangleSelected(Triangle triangleSelected)
    {
        ElementsSelectedListener[] ESListeners = this.listeners
                .getListeners(ElementsSelectedListener.class);
        for (ElementsSelectedListener listener : ESListeners)
        {
            listener.triangleSelected(triangleSelected);
        }
    }

    /**
     * Getter.
     * @return the display mode
     */
    public final int getDisplayMode()
    {
        return this.displayMode;
    }

    /**
     * Getter.
     * @return the mouse rotate class
     */
    public final NewMouseRotate getMouseRotate()
    {
        return this.mouseRotate;
    }

    /**
     * Getter.
     * @return the parent controller
     */
    public final ElementsSelectedListener getParentController()
    {
        return this.parentController;
    }

    /**
     * Returns the SurfaceView associated to the surface.
     * @param surface
     *            the surface to search
     * @return the surfaceView associated
     */
    public final SurfaceView getSurfaceViewFromSurface(final Surface surface)
    {
        for (SurfaceView sView : this.u3DView.getSurfaceViewList())
        {
            if (sView.getSurface() == surface)
            {
                return sView;
            }
        }
        return null;
    }

    /**
     * Returns the list of Triangle associated with the trianglesView contained
     * in trianglesViewSelected.
     * @return the list of selected triangles
     */
    public final List<Triangle> getTrianglesSelected()
    {
        return this.trianglesSelected;
    }

    /**
     * Getter.
     * @return the universe 3D view
     */
    public final Universe3DView getUniverse3DView()
    {
        return this.u3DView;
    }

    /**
     * Treats the different clicking actions (left or right).
     * @param e
     *            The MouseEvent caught by the mouseListener when clicking
     */
    @Override
    public final void mouseClicked(final MouseEvent e)
    {
        int buttonDown = e.getButton();
        if (buttonDown == MouseEvent.BUTTON1)
        {
            this.treatLeftClick(e);
        } else if (buttonDown == MouseEvent.BUTTON3)
        {
            this.treatRightClick(e);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent arg0)
    {
        // Not used.
    }

    @Override
    public void mouseEntered(final MouseEvent e)
    {
        // Not used.
    }

    @Override
    public void mouseExited(final MouseEvent e)
    {
        // Not used.
    }

    @Override
    public void mouseMoved(final MouseEvent e)
    {
        // Not used.
    }

    @Override
    public void mousePressed(final MouseEvent e)
    {
        // Not used.
    }

    @Override
    public void mouseReleased(final MouseEvent e)
    {
        // Not used.
    }

    /**
     * TODO .
     * @param listener
     *            TODO
     */
    public final void removeElementsSelectedListener(
            final ElementsSelectedListener listener)
    {
        this.listeners.remove(ElementsSelectedListener.class, listener);
    }

    /**
     * TODO .
     * @param surface
     *            TODO
     */
    public final void selectMeshFromTree(final Surface surface)
    {
        SurfaceView surfaceView = this.getSurfaceViewFromSurface(surface);

        // Changes the material of the surface seleted.
        surfaceView.getAppearance().setMaterial(SurfaceView.MATERIAL_SELECTED);
    }

    /**
     * Changse the appearance of the surfaceView parameter : select or unselect
     * the corresponding surface.
     * @param surfaceView
     *            The surfaceView containing the surface to select
     */
    // TODO : maybe put the content of this method in the treatButton method if
    // it is not used other way.
    public final void selectOrUnselectSurface(final SurfaceView surfaceView)
    {
        Surface surface = surfaceView.getSurface();
        // surface not selected when clicked
        if (!this.surfacesSelected.contains(surface))
        {
            this.surfacesSelected.add(surface);
            surfaceView.setMaterial(SurfaceView.MATERIAL_SELECTED);
        } else
        {
            // surface already selected when clicked
            this.surfacesSelected.remove(surface);
            surfaceView.setMaterial(SurfaceView.MATERIAL_UNSELECTED);
        }
    }

    /**
     * Setter.
     * @param displayModeIn
     *            TODO
     */
    public final void setDisplayMode(final int displayModeIn)
    {
        this.displayMode = displayModeIn;
    }

    /**
     * Setter.
     * @param mouseRotateIn
     *            TODO
     */
    public final void setMouseRotate(final NewMouseRotate mouseRotateIn)
    {
        this.mouseRotate = mouseRotateIn;
    }

    /**
     * Setter.
     * @param parentControllerIn
     *            TODO
     */
    public final void setParentController(
            final IsletSelectionController parentControllerIn)
    {
        this.parentController = parentControllerIn;
    }

    /**
     * TODO.
     * @param branchGroup
     *            TODO
     */
    public final void setPickCanvas(final BranchGroup branchGroup)
    {
        this.pickCanvas = new PickCanvas(this.u3DView.getSimpleUniverse()
                .getCanvas(), branchGroup);
        System.out.println("pickCanvas");
        this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
    }

    /**
     * Treats a left click action : handling of a surface selection.
     * @param e
     *            TODO.
     */
    // TODO : commentaries !
    private void treatLeftClick(final MouseEvent e)
    {
        this.pickCanvas.setShapeLocation(e);

        PickResult result = this.pickCanvas.pickClosest();

        if (result != null)
        {
            SurfaceView surfaceViewPicked = (SurfaceView) result
                    .getNode(PickResult.SHAPE3D);

            if (e.isControlDown())
            {
                this.surfacesSelected.clear();
            }
            this.selectOrUnselectSurface(surfaceViewPicked);
            this.changeRotationCenter(surfaceViewPicked);
        }
    }

    /**
     * Treats a right click action : handling of triangles selection.
     * @param e
     *            TODO
     */
    private void treatRightClick(final MouseEvent e)
    {
        this.pickCanvas.setShapeLocation(e);

        PickResult result = this.pickCanvas.pickClosest();

        if (result != null)
        {
            PickIntersection pickIntersection = result.getIntersection(0);

            // Gets the meshView picked.
            MeshView meshView = (MeshView) pickIntersection.getGeometryArray();

            // Gets the index of the triangle picked.
            int[] pointIndex = pickIntersection.getPrimitiveVertexIndices();
            // TODO : 3 : magic number !
            Triangle trianglePicked = meshView
                    .getTriangleFromArrayPosition(pointIndex[0] / 3);

            // FIXME : create a button in the tool bar to set this number
            // FIXME : magic number ! Put this as a PUBLIC STATIC FINAL INT.
            int turn = 30;

            // Computes the neighbours of the triangle.
            Mesh oriented = meshView.getMesh().orientedAs(
                    trianglePicked.getNormal(), 1);

            Mesh neighbours = new Mesh();

            trianglePicked.returnNeighbours(neighbours, oriented);

            // If this triangle is not already selected.
            if (!this.trianglesSelected.contains(trianglePicked))
            {
                // Selects these triangles.
                for (Triangle t : neighbours)
                {
                    meshView.select(t);
                }

                this.trianglesSelected.addAll(neighbours);

                // Changes the center of rotation.
                this.mouseRotate.setCenter(trianglePicked.getP1());
            } else
            {
                // Selects these triangles.
                for (Triangle t : neighbours)
                {
                    meshView.unselect(t);
                }

                this.trianglesSelected.removeAll(neighbours);
            }
        }
    }
}
