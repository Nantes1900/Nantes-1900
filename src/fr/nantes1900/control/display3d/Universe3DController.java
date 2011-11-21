package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.swing.event.EventListenerList;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.TriangleView;
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
    private Universe3DView          u3DView;
    /**
     * TODO.
     */
    private PickCanvas              pickCanvas;
    /**
     * TODO.
     */
    private NewMouseRotate          mouseRotate;

    /**
     * TODO.
     */
    private ArrayList<TriangleView> trianglesViewSelected;
    /**
     * TODO.
     */
    private int                     xPressed;
    /**
     * TODO.
     */
    private int                     yPressed;

    private final EventListenerList  listeners = new EventListenerList();
    
    /**
     * TODO.
     */
    public Universe3DController()
    {
        this.u3DView = new Universe3DView(this);
        this.trianglesViewSelected = new ArrayList<>();
    }

    /**
     * TODO.
     * @return TODO
     */
    public final NewMouseRotate getMouseRotate()
    {
        return this.mouseRotate;
    }

    /**
     * Returns the list of Triangle associated with the trianglesView contained
     * in trianglesViewSelected.
     * @return the list of selected triangles
     */
    public final List<Triangle> getTrianglesSelected()
    {
        List<Triangle> trianglesList = new ArrayList<>();
        for (TriangleView triangleView : this.trianglesViewSelected)
        {
            trianglesList.add(triangleView.getTriangle());
        }
        return trianglesList;
    }

    /**
     * Getter.
     * @return the universe 3D view
     */
    public final Universe3DView getUniverse3DView()
    {
        return this.u3DView;
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public final void mouseClicked(final MouseEvent e)
    {
        int buttonDown = e.getButton();

        if (buttonDown == MouseEvent.BUTTON1)
        {

            // Bouton gauche enfonc

            this.pickCanvas.setShapeLocation(e);
            PickResult result = this.pickCanvas.pickClosest();
            if (result == null)
            {
                System.out.println("Nothing picked");
            } else
            {
                PickIntersection pickIntersection = result.getIntersection(0);
                int[] pointIndex = pickIntersection.getPrimitiveVertexIndices();
                int triangleIndex = pointIndex[0] / 3;

                MeshView triangleMeshView = (MeshView) pickIntersection.getGeometryArray();

                triangleMeshView.selectOrUnselect(triangleIndex);

                this.mouseRotate.setCenter(triangleMeshView.getTriangleArray()
                        .get(triangleIndex));
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
     * )
     */
    @Override
    public void mouseDragged(final MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public final void mousePressed(final MouseEvent e)
    {
        System.out.println("Mouse Pressed");
        this.xPressed = e.getX();
        this.yPressed = e.getY();
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public final void mouseReleased(final MouseEvent e)
    {
        int buttonDown = e.getButton();
        if (buttonDown == MouseEvent.BUTTON1)
        {
            for (int x = this.xPressed; x < e.getX(); x = x + 2)
            {
                for (int y = this.yPressed; y < e.getY(); y = y + 2)
                {
                    this.pickCanvas.setShapeLocation(x, y);
                    PickResult result = this.pickCanvas.pickClosest();
                    if (result == null)
                    {
                        System.out.println("Nothing picked");
                    } else
                    {
                        PickIntersection PI = result.getIntersection(0);
                        int[] PointIndex = PI.getPrimitiveVertexIndices();
                        int TriangleIndex = PointIndex[0] / 3;
                        MeshView triangleMeshView = (MeshView) PI.getGeometryArray();
                        triangleMeshView.select(TriangleIndex);
                        this.mouseRotate.setCenter(triangleMeshView.getTriangleArray()
                                .get(TriangleIndex));

                    }
                }
            }
        }
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
        this.u3DView.getSimpleUniverse()
                .getCanvas()
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

}
