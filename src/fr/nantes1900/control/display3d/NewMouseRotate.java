package fr.nantes1900.control.display3d;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import fr.nantes1900.view.display3d.TriangleView;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

public class NewMouseRotate extends MouseRotate{
	
    double x_angle, y_angle;
    double x_factor = .03;
    double y_factor = .03;
    private MouseBehaviorCallback callback = null;
    
	private TransformGroup tg1;
	private TransformGroup tg2;
	private TransformGroup tg3;
	private Transform3D translation1 = new Transform3D();
	private Transform3D translation2 = new Transform3D();
	private Point3d center;
    
    public Point3d getCenter() {
		return center;
	}

	public void setCenter(Point3d center) {
		this.center = center;
	}

	public void setCenter(TriangleView triangle) {
		double x,y,z;
		x = (triangle.getTriangle().getP1().getX()+ 
				triangle.getTriangle().getP2().getX() +
				triangle.getTriangle().getP3().getX())/3;
		y = (triangle.getTriangle().getP1().getY()+ 
				triangle.getTriangle().getP2().getY() +
				triangle.getTriangle().getP3().getY())/3;
		z = (triangle.getTriangle().getP1().getZ()+ 
				triangle.getTriangle().getP2().getZ() +
				triangle.getTriangle().getP3().getZ())/3;
		setCenter(new Point3d(x,y,z));
	}
	
	public NewMouseRotate(TransformGroup TG1, TransformGroup TG2, TransformGroup TG3){
    	super();
    	this.tg1=TG1;
    	this.tg2=TG2;
    	this.tg3=TG3;    	    	    	 
    }
    
    public void processStimulus(@SuppressWarnings("rawtypes") Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] events;
        MouseEvent evt;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof  WakeupOnAWTEvent) {
                events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                if (events.length > 0) {
                    evt = (MouseEvent) events[events.length - 1];
                    
                    Point3d centerpoint=new Point3d();
                    centerpoint.x=this.center.getX();
                    centerpoint.y=this.center.getY();
                    centerpoint.z=this.center.getZ();
                    
                    this.translation2.transform(centerpoint);
                    currXform.transform(centerpoint);
                    this.translation1.transform(centerpoint);
   
                    
            		Vector3d vector1 = new Vector3d();
            		Vector3d vector2 = new Vector3d();
            		vector1.set(centerpoint.x,centerpoint.y,centerpoint.z);
            		vector2.set(-this.center.getX(), -this.center.getY(), -this.center.getZ());            		
            		
            		this.translation1.setTranslation(vector1);
            		this.translation2.setTranslation(vector2);
                    
            		tg1.setTransform(translation1);
                    doNewProcess(evt);
                    tg3.setTransform(translation2);
                }
            }

            else if (wakeup instanceof  WakeupOnBehaviorPost) {
                while (true) {
                    // access to the queue must be synchronized
                    synchronized (mouseq) {
                        if (mouseq.isEmpty())
                            break;
                        evt = (MouseEvent) mouseq.remove(0);
                        // consolidate MOUSE_DRAG events
                        while ((evt.getID() == MouseEvent.MOUSE_DRAGGED)
                                && !mouseq.isEmpty()
                                && (((MouseEvent) mouseq.get(0))
                                        .getID() == MouseEvent.MOUSE_DRAGGED)) {
                            evt = (MouseEvent) mouseq.remove(0);
                        }
                    }
                    doNewProcess(evt);
                }
            }

        }
        wakeupOn(mouseCriterion);
    }
    
    
    void doNewProcess(MouseEvent evt) {
        int id;
        int dx, dy;

        processMouseEvent(evt);
        if (((buttonPress) && ((flags & MANUAL_WAKEUP) == 0))
                || ((wakeUp) && ((flags & MANUAL_WAKEUP) != 0))) {
            id = evt.getID();
            if ((id == MouseEvent.MOUSE_DRAGGED) && !evt.isMetaDown()
                    && !evt.isAltDown() && !evt.isShiftDown()) {
                x = evt.getX();
                y = evt.getY();

                dx = x - x_last;
                dy = y - y_last;

                if (!reset) {
                    x_angle = dy * y_factor;
                    y_angle = dx * x_factor;

                    transformX.rotX(x_angle);
                    transformY.rotY(y_angle);

                    tg2.getTransform(currXform);

                    Matrix4d mat = new Matrix4d();
                    // Remember old matrix
                    currXform.get(mat);

                    // Translate to origin
                    currXform.setTranslation(new Vector3d(.0, .0, .0));
                    if (invert) {
                        currXform.mul(currXform, transformX);
                        currXform.mul(currXform, transformY);
                    } else {
                        currXform.mul(transformX, currXform);
                        currXform.mul(transformY, currXform);
                    }

                    // Set old translation back
                    Vector3d translation = new Vector3d(mat.m03,mat.m13, mat.m23);
                    currXform.setTranslation(translation);

                    // Update xform
                    tg2.setTransform(currXform);

                    transformChanged(currXform);

                    if (callback != null)
                        callback
                                .transformChanged(
                                        MouseBehaviorCallback.ROTATE,
                                        currXform);
                } else {
                    reset = false;
                }

                x_last = x;
                y_last = y;
            } else if (id == MouseEvent.MOUSE_PRESSED) {
                x_last = evt.getX();
                y_last = evt.getY();
            }
        }
    }
}
