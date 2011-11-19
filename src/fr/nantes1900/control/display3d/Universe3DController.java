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
public class Universe3DController implements MouseListener, MouseMotionListener
{

    /**
     * The view of the 3D objets to show.
     */
    private Universe3DView          u3DView;

    private PickCanvas              pickCanvas;
    private NewMouseRotate          mouseRotate;

    // private ArrayList<MeshView> meshesViewSelected;
    private ArrayList<TriangleView> trianglesViewSelected;

    // private ArrayList<PolygonView> polygonsViewSelected;
    private int                     xPressed;
    private int                     yPressed;

    private final EventListenerList  listeners = new EventListenerList();
    
    public Universe3DController()
    {
        this.u3DView = new Universe3DView(this);
        this.trianglesViewSelected = new ArrayList<>();
    }

    public NewMouseRotate getMouseRotate()
    {
        return this.mouseRotate;
    }

    /**
     * Returns the list of Triangle associated with the trianglesView contained
     * in trianglesViewSelected.
     * @return
     */
    public List<Triangle> getTrianglesSelected()
    {
        List<Triangle> trianglesList = new ArrayList<>();
        for (TriangleView triangleView : this.trianglesViewSelected)
        {
            trianglesList.add(triangleView.getTriangle());
        }
        return trianglesList;
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

            // Bouton gauche enfonc

            this.pickCanvas.setShapeLocation(e);
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
                triangleMeshView.selectOrUnselect(TriangleIndex);
                mouseRotate.setCenter(triangleMeshView.getTriangleArray()
                        .get(TriangleIndex));

            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        System.out.println("Mouse Pressed");
        this.xPressed = e.getX();
        this.yPressed = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e)
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
                        mouseRotate.setCenter(triangleMeshView.getTriangleArray()
                                .get(TriangleIndex));

                    }
                }
            }
        }
    }

    public void setMouseRotate(NewMouseRotate mouseRotate)
    {
        this.mouseRotate = mouseRotate;
    }

    public void setPickCanvas(BranchGroup branchGroup)
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
