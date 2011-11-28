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

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import fr.nantes1900.models.basis.Point;

// FIXME : Javadoc
public class NewMouseRotate extends MouseRotate
{

    double                        x_angle, y_angle;
    double                        x_factor     = .03;
    double                        y_factor     = .03;
    private MouseBehaviorCallback callback     = null;

    private TransformGroup        tg1;
    private TransformGroup        tg2;
    private TransformGroup        tg3;
    private Transform3D           translation1 = new Transform3D();
    private Transform3D           translation2 = new Transform3D();
    private Point3d               center;

    public NewMouseRotate(TransformGroup TG1, TransformGroup TG2,
            TransformGroup TG3)
    {
        super();
        this.tg1 = TG1;
        this.tg2 = TG2;
        this.tg3 = TG3;
    }

    void doNewProcess(MouseEvent evt)
    {
        int id;
        int dx, dy;

        processMouseEvent(evt);
        if (((this.buttonPress) && ((this.flags & MANUAL_WAKEUP) == 0))
                || ((this.wakeUp) && ((this.flags & MANUAL_WAKEUP) != 0)))
        {
            id = evt.getID();
            if ((id == MouseEvent.MOUSE_DRAGGED) && !evt.isMetaDown()
                    && !evt.isAltDown() && !evt.isShiftDown())
            {
                this.x = evt.getX();
                this.y = evt.getY();

                dx = this.x - this.x_last;
                dy = this.y - this.y_last;

                if (!this.reset)
                {
                    this.x_angle = dy * this.y_factor;
                    this.y_angle = dx * this.x_factor;

                    this.transformX.rotX(this.x_angle);
                    this.transformY.rotY(this.y_angle);

                    this.tg2.getTransform(this.currXform);

                    Matrix4d mat = new Matrix4d();
                    // Remember old matrix
                    this.currXform.get(mat);

                    // Translate to origin
                    this.currXform.setTranslation(new Vector3d(.0, .0, .0));
                    if (this.invert)
                    {
                        this.currXform.mul(this.currXform, this.transformX);
                        this.currXform.mul(this.currXform, this.transformY);
                    } else
                    {
                        this.currXform.mul(this.transformX, this.currXform);
                        this.currXform.mul(this.transformY, this.currXform);
                    }

                    // Set old translation back
                    Vector3d translation = new Vector3d(mat.m03, mat.m13,
                            mat.m23);
                    this.currXform.setTranslation(translation);

                    // Update xform
                    this.tg2.setTransform(this.currXform);

                    transformChanged(this.currXform);

                    if (this.callback != null)
                    {
                        this.callback.transformChanged(
                                MouseBehaviorCallback.ROTATE, this.currXform);
                    }
                } else
                {
                    this.reset = false;
                }

                this.x_last = this.x;
                this.y_last = this.y;
            } else if (id == MouseEvent.MOUSE_PRESSED)
            {
                this.x_last = evt.getX();
                this.y_last = evt.getY();
            }
        }
    }

    public Point3d getCenter()
    {
        return this.center;
    }

    @Override
    public void processStimulus(Enumeration criteria)
    {
        WakeupCriterion wakeup;
        AWTEvent[] events;
        MouseEvent evt;

        while (criteria.hasMoreElements())
        {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent)
            {
                events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                if (events.length > 0)
                {
                    evt = (MouseEvent) events[events.length - 1];

                    Point3d centerpoint = new Point3d();
                    centerpoint.x = this.center.getX();
                    centerpoint.y = this.center.getY();
                    centerpoint.z = this.center.getZ();

                    this.translation2.transform(centerpoint);
                    this.currXform.transform(centerpoint);
                    this.translation1.transform(centerpoint);

                    Vector3d vector1 = new Vector3d();
                    Vector3d vector2 = new Vector3d();
                    vector1.set(centerpoint.x, centerpoint.y, centerpoint.z);
                    vector2.set(-this.center.getX(), -this.center.getY(),
                            -this.center.getZ());

                    this.translation1.setTranslation(vector1);
                    this.translation2.setTranslation(vector2);

                    this.tg1.setTransform(this.translation1);
                    doNewProcess(evt);
                    this.tg3.setTransform(this.translation2);
                }
            }

            else if (wakeup instanceof WakeupOnBehaviorPost)
            {
                while (true)
                {
                    // access to the queue must be synchronized
                    synchronized (this.mouseq)
                    {
                        if (this.mouseq.isEmpty())
                        {
                            break;
                        }
                        evt = (MouseEvent) this.mouseq.remove(0);
                        // consolidate MOUSE_DRAG events
                        while ((evt.getID() == MouseEvent.MOUSE_DRAGGED)
                                && !this.mouseq.isEmpty()
                                && (((MouseEvent) this.mouseq.get(0)).getID() == MouseEvent.MOUSE_DRAGGED))
                        {
                            evt = (MouseEvent) this.mouseq.remove(0);
                        }
                    }
                    doNewProcess(evt);
                }
            }

        }
        wakeupOn(this.mouseCriterion);
    }

    /**
     * TODO.
     * @param new Point3d(centroid.getX(), centroid.getY() TODO
     */
    public final void setCenter(final Point point)
    {
        this.center = new Point3d(point.getX(), point.getY(), point.getZ());
    }

}
