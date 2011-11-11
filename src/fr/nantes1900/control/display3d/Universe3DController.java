package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.display3d.TriangleView;
import fr.nantes1900.view.display3d.Universe3DView;

public class Universe3DController implements MouseListener, MouseMotionListener {

    /**
     * The view of the 3D objets to show.
     */
    private Universe3DView u3DView;

    private PickCanvas pickCanvas;
    private NewMouseRotate mouseRotate;

    private IsletSelectionController parentController;

    // private ArrayList<MeshView> meshesViewSelected;
    private ArrayList<TriangleView> trianglesViewSelected;

    // private ArrayList<PolygonView> polygonsViewSelected;

    public Universe3DController(
            IsletSelectionController isletSelectionController) {
        this.parentController = isletSelectionController;
        this.u3DView = new Universe3DView(this);
        this.trianglesViewSelected = new ArrayList<TriangleView>();
    }

    public NewMouseRotate getMouseRotate() {
        return this.mouseRotate;
    }

    /**
     * Returns the list of Triangle associated with the trianglesView contained
     * in trianglesViewSelected.
     * @return
     */
    public List<Triangle> getTrianglesSelected() {
        List<Triangle> trianglesList = new ArrayList<>();
        for (TriangleView triangleView : this.trianglesViewSelected) {
            trianglesList.add(triangleView.getTriangle());
        }
        return trianglesList;
    }

    public Universe3DView getUniverse3DView() {
        return this.u3DView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int buttonDown = e.getButton();

        if (buttonDown == MouseEvent.BUTTON1) { // Left button of the mouse
            this.pickCanvas.setShapeLocation(e);
            PickResult result = this.pickCanvas.pickClosest();
            if (result == null) {
                System.out.println("Nothing picked");
            } else {
                PickIntersection pi = result.getIntersection(0);
                TriangleView ta = (TriangleView) pi.getGeometryArray();
                if (ta != null) {
                    if (this.trianglesViewSelected.contains(ta)) {
                        this.trianglesViewSelected.remove(ta);
                        // FIXME
                        // ta.changeColor(Color.blue);
                    } else {
                        this.trianglesViewSelected.add(ta);
                        // FIXME
                        // ta.changeColor(Color.red);
                    }
                    this.mouseRotate.setCenter(ta);
                } else {
                    System.out.println("null");
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void setMouseRotate(NewMouseRotate mouseRotate) {
        this.mouseRotate = mouseRotate;
    }

    public void setPickCanvas(BranchGroup branchGroup) {
        this.pickCanvas = new PickCanvas(this.u3DView.getSimpleUniverse()
                .getCanvas(), branchGroup);
        this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        this.u3DView.getSimpleUniverse().getCanvas().addMouseListener(this);
        this.u3DView.getSimpleUniverse().getCanvas()
                .addMouseMotionListener(this);

    }
}
